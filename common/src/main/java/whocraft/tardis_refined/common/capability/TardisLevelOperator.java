package whocraft.tardis_refined.common.capability;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import whocraft.tardis_refined.api.event.TardisEvents;
import whocraft.tardis_refined.client.TardisClientData;
import whocraft.tardis_refined.common.blockentity.desktop.door.RootShellDoorBlockEntity;
import whocraft.tardis_refined.common.blockentity.door.TardisInternalDoor;
import whocraft.tardis_refined.common.capability.upgrades.UpgradeHandler;
import whocraft.tardis_refined.common.dimension.DelayedTeleportData;
import whocraft.tardis_refined.common.tardis.ExteriorShell;
import whocraft.tardis_refined.common.tardis.TardisArchitectureHandler;
import whocraft.tardis_refined.common.tardis.manager.*;
import whocraft.tardis_refined.compat.ModCompatChecker;
import whocraft.tardis_refined.compat.portals.ImmersivePortals;
import whocraft.tardis_refined.constants.NbtConstants;

import java.util.Optional;

public class TardisLevelOperator {

    private final Level level;
    private boolean hasInitiallyGenerated = false;
    private TardisInternalDoor internalDoor = null;

    // Managers
    private final TardisExteriorManager exteriorManager;
    private final TardisInteriorManager interiorManager;
    private final TardisPilotingManager pilotingManager;
    private final TardisWaypointManager tardisWaypointManager;
    private final TardisFlightEventManager tardisFlightEventManager;
    private final TardisClientData tardisClientData;
    private final UpgradeHandler upgradeHandler;
    private final TardisHADSManager tardisHADSManager;
    private final AestheticHandler aestheticHandler;

    public TardisLevelOperator(Level level) {
        this.level = level;
        this.exteriorManager = new TardisExteriorManager(this);
        this.interiorManager = new TardisInteriorManager(this);
        this.pilotingManager = new TardisPilotingManager(this);
        this.tardisWaypointManager = new TardisWaypointManager(this);
        this.tardisFlightEventManager = new TardisFlightEventManager(this);
        this.tardisClientData = new TardisClientData(level.dimension());
        this.upgradeHandler = new UpgradeHandler(this);
        this.tardisHADSManager = new TardisHADSManager(this);
        this.aestheticHandler = new AestheticHandler(this);
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    public TardisClientData tardisClientData() {
        return tardisClientData;
    }

    public TardisHADSManager getTardisHADSManager() {
        return tardisHADSManager;
    }

    public AestheticHandler getAestheticHandler() {
        return aestheticHandler;
    }

    @ExpectPlatform
    public static Optional<TardisLevelOperator> get(ServerLevel level) {
        throw new AssertionError();
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putBoolean(NbtConstants.TARDIS_IS_SETUP, this.hasInitiallyGenerated);

        if (this.internalDoor != null) {
            compoundTag.putString(NbtConstants.TARDIS_INTERNAL_DOOR_ID, this.internalDoor.getID());
            compoundTag.put(NbtConstants.TARDIS_INTERNAL_DOOR_POSITION, NbtUtils.writeBlockPos(this.internalDoor.getDoorPosition()));
        }

        compoundTag = this.exteriorManager.saveData(compoundTag);
        compoundTag = this.interiorManager.saveData(compoundTag);
        compoundTag = this.pilotingManager.saveData(compoundTag);
        compoundTag = this.tardisWaypointManager.saveData(compoundTag);
        compoundTag = this.tardisFlightEventManager.saveData(compoundTag);
        compoundTag = this.upgradeHandler.saveData(compoundTag);
        compoundTag = this.tardisHADSManager.saveData(compoundTag);
        compoundTag = this.aestheticHandler.saveData(compoundTag);

        return compoundTag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.hasInitiallyGenerated = tag.getBoolean(NbtConstants.TARDIS_IS_SETUP);

        CompoundTag doorPos = tag.getCompound(NbtConstants.TARDIS_INTERNAL_DOOR_POSITION);
        if (doorPos != null) {
            if (level.getBlockEntity(NbtUtils.readBlockPos(doorPos)) instanceof TardisInternalDoor door) {
                this.internalDoor = door;
            }
        }

        // Managers
        this.exteriorManager.loadData(tag);
        this.interiorManager.loadData(tag);
        this.pilotingManager.loadData(tag);
        this.tardisFlightEventManager.loadData(tag);
        this.tardisWaypointManager.loadData(tag);
        this.upgradeHandler.loadData(tag);
        this.tardisHADSManager.loadData(tag);
        this.aestheticHandler.loadData(tag);
        tardisClientData.sync();
    }

    public Level getLevel() {
        return level;
    }

    public void tick(ServerLevel level) {
        interiorManager.tick(level);
        pilotingManager.tick(level);
        tardisFlightEventManager.tick();
        tardisHADSManager.tick();
        upgradeHandler.tick();

        var shouldSync = level.getGameTime() % 40 == 0;
        if (shouldSync) {
            tardisClientData.setFlying(pilotingManager.isInFlight());
            tardisClientData.setThrottleDown(pilotingManager.shouldThrottleBeDown());
            tardisClientData.setIsLanding(exteriorManager.isLanding());
            tardisClientData.setIsTakingOff(exteriorManager.isTakingOff());
            tardisClientData.setInDangerZone(tardisFlightEventManager.isInDangerZone());
            tardisClientData.setFlightShakeScale(tardisFlightEventManager.dangerZoneShakeScale());
            tardisClientData.setIsOnCooldown(pilotingManager.isOnCooldown());

            tardisClientData.setShellTheme(aestheticHandler.getShellTheme());
            tardisClientData.setShellPattern(aestheticHandler.shellPattern().id());

            tardisClientData.sync();
        }
    }

    public boolean hasInitiallyGenerated() {
        return hasInitiallyGenerated;
    }

    public void setInitiallyGenerated(boolean hasInitiallyGenerated) {
        this.hasInitiallyGenerated = hasInitiallyGenerated;
    }

    /**
     * Moves the entity into the TARDIS. If the TARDIS has no door established, the player is sent to 0,0,0.
     *
     * @param player Player Entity.
     **/
    public void enterTardis(ExteriorShell shell, Player player, BlockPos externalPos, ServerLevel level, Direction direction) {

        if (player instanceof ServerPlayer serverPlayer) {
            if (internalDoor != null) {
                BlockPos targetPosition = internalDoor.getEntryPosition();
                Direction dir = internalDoor.getEntryRotation();

                ChunkAccess chunk = getLevel().getChunk(internalDoor.getDoorPosition());
                if (getLevel() instanceof ServerLevel serverLevel) {
                    serverLevel.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);
                }
                level.getChunkSource().updateChunkForced(chunk.getPos(), true);
                DelayedTeleportData.getOrCreate(serverPlayer.serverLevel()).schedulePlayerTeleport(serverPlayer, getLevel().dimension(), Vec3.atCenterOf(targetPosition), dir.get2DDataValue() * (360 / 4));
            } else {

                // TODO: Scan for console units near the center to warp to.

                ChunkAccess chunk = getLevel().getChunk(TardisArchitectureHandler.DESKTOP_CENTER_POS);

                if (getLevel() instanceof ServerLevel serverLevel) {
                    serverLevel.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);
                }
                level.getChunkSource().updateChunkForced(chunk.getPos(), true);
                DelayedTeleportData.getOrCreate(serverPlayer.serverLevel()).schedulePlayerTeleport(serverPlayer, getLevel().dimension(), Vec3.atCenterOf(TardisArchitectureHandler.DESKTOP_CENTER_POS.above()), 0);
            }
        }

        tardisClientData.sync();
        TardisEvents.TARDIS_ENTRY_EVENT.invoker().onEnterTardis(this, shell, player, externalPos, level, direction);
    }

    public boolean isTardisReady() {
        return !this.getInteriorManager().isGeneratingDesktop();
    }

    public boolean exitTardis(Player player) {

        if (!this.internalDoor.isOpen()) {
            return false;
        }



        if(aestheticHandler.getShellTheme() != null) {
            ResourceLocation theme = aestheticHandler.getShellTheme();
            if(ModCompatChecker.immersivePortals() && !(this.internalDoor instanceof RootShellDoorBlockEntity)) {
               if(ImmersivePortals.exteriorHasPortalSupport(theme)) {
                   return false;
               }
            }
        }

        if (this.exteriorManager != null) {
            if (this.exteriorManager.getLastKnownLocation() != null) {
                BlockPos targetPosition = this.exteriorManager.getLastKnownLocation().getPosition();
                ServerLevel targetLevel = this.exteriorManager.getLastKnownLocation().getLevel();

                ChunkAccess preloadedArea = this.exteriorManager.getLastKnownLocation().getLevel().getChunk(targetPosition);

                if (player instanceof ServerPlayer serverPlayer) {
                    if (targetLevel.getBlockEntity(targetPosition) instanceof ExteriorShell shellBaseBlockEntity) {
                        BlockPos landingArea = shellBaseBlockEntity.getExitPosition();
                        DelayedTeleportData.getOrCreate(serverPlayer.serverLevel()).schedulePlayerTeleport(serverPlayer, targetLevel.dimension(), Vec3.atCenterOf(landingArea), this.exteriorManager.getLastKnownLocation().getDirection().get2DDataValue() * (360 / 4));
                    }
                }
            }
        }
        return true;
    }

    public void setDoorClosed(boolean closeDoor) {
        TardisExteriorManager extManager = getExteriorManager();
        TardisInternalDoor intDoor = getInternalDoor();

        if (intDoor != null) {
            intDoor.setClosed(closeDoor);
        }
        if(closeDoor) {
            TardisEvents.DOOR_CLOSED_EVENT.invoker().onDoorClosed(this);
        } else {
            TardisEvents.DOOR_OPENED_EVENT.invoker().onDoorOpen(this);
        }

        if (extManager != null) {
            if (extManager.getLastKnownLocation() != null) {
                extManager.setDoorClosed(closeDoor);
            }
        }
    }

    public void setShellTheme(ResourceLocation theme, boolean setupTardis) {
        this.getAestheticHandler().setShellTheme(theme, setupTardis, getExteriorManager().getLastKnownLocation());
        tardisClientData.setShellTheme(theme);
        tardisClientData.setShellPattern(aestheticHandler.shellPattern().id());
        tardisClientData.sync();
        TardisEvents.SHELL_CHANGE_EVENT.invoker().onShellChange(this, theme, setupTardis);
    }

    /**
     * Sets the main operating door of an interior.
     *
     * @param door Internal door object.
     **/
    public void setInternalDoor(TardisInternalDoor door) {
        if (this.internalDoor != null) {
            this.internalDoor.onSetMainDoor(false);
        }
        this.internalDoor = door;
        if (door != null) //If the new door value is not null
            this.internalDoor.onSetMainDoor(true);
    }

    public TardisExteriorManager getExteriorManager() {
        return this.exteriorManager;
    }

    public TardisInternalDoor getInternalDoor() {
        return this.internalDoor;
    }

    public TardisInteriorManager getInteriorManager() {
        return this.interiorManager;
    }

    public TardisPilotingManager getPilotingManager() {
        return this.pilotingManager;
    }

    public TardisFlightEventManager getTardisFlightEventManager() {
        return this.tardisFlightEventManager;
    }

    public TardisWaypointManager getTardisWaypointManager() {
        return tardisWaypointManager;
    }
}
