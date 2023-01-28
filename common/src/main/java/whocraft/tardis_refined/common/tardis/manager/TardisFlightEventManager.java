package whocraft.tardis_refined.common.tardis.manager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import whocraft.tardis_refined.common.capability.TardisLevelOperator;
import whocraft.tardis_refined.common.entity.ControlEntity;
import whocraft.tardis_refined.common.tardis.TardisArchitectureHandler;
import whocraft.tardis_refined.common.tardis.control.ConsoleControl;
import whocraft.tardis_refined.registry.SoundRegistry;

import java.util.Arrays;
import java.util.List;

public class TardisFlightEventManager {

    private TardisLevelOperator operator;
    private List<ConsoleControl> possibleControls;
    private ConsoleControl controlPrompt;
    private int requiredControlRequests;
    private int controlResponses;

    private final int MIN_DISTANCE_FOR_EVENTS = 23;
    private int controlRequestCooldown = 0;
    private boolean isWaitingForControlResponse = false;
    private int ticksSincePrompted = 0;

    private boolean isInDangerZone = false;
    private int ticksInTheDangerZone = 0;
    private int requiredDangerZoneRequests = 0;
    private int dangerZoneResponses = 0;

    private int collectedTXPPool = 0;

    private float dangerZoneShakeScale = 0f;

    public TardisFlightEventManager(TardisLevelOperator operator) {
        this.operator = operator;
        this.possibleControls = Arrays.stream(ConsoleControl.values()).filter(x -> x != ConsoleControl.MONITOR && x != ConsoleControl.THROTTLE).toList();
    }

    public void loadData(CompoundTag tag) {
        this.isWaitingForControlResponse = tag.getBoolean("isWaitingForControlResponse");
        this.isInDangerZone = tag.getBoolean("isInDangerZone");
        this.ticksInTheDangerZone = tag.getInt("ticksInTheDangerZone");
        this.requiredControlRequests = tag.getInt("requiredDangerZoneRequests");
        this.dangerZoneResponses = tag.getInt("dangerZoneResponses");
        this.controlRequestCooldown = tag.getInt("controlRequestCooldown");
        this.ticksSincePrompted = tag.getInt("ticksSincePrompted");
        this.dangerZoneShakeScale = tag.getInt("dangerZoneShakeScale");
        this.controlPrompt = ConsoleControl.findOr(tag.getString("controlPrompt"), ConsoleControl.THROTTLE);
    }

    public CompoundTag saveData(CompoundTag tag) {
        tag.putBoolean("isWaitingForControlResponse", this.isWaitingForControlResponse);
        tag.putBoolean("isInDangerZone", this.isInDangerZone);
        tag.putInt("ticksInTheDangerZone", this.ticksInTheDangerZone);
        tag.putInt("requiredDangerZoneRequests", this.requiredControlRequests);
        tag.putInt("dangerZoneResponses", this.dangerZoneResponses);
        tag.putInt("controlRequestCooldown", this.controlRequestCooldown);
        tag.putInt("ticksSincePrompted", this.ticksSincePrompted);
        tag.putFloat("dangerZoneShakeScale", this.dangerZoneShakeScale);

        if (this.controlPrompt != null) {
            tag.putString("controlPrompt", this.controlPrompt.getSerializedName());
        }

        return tag;
    }

    public boolean isWaitingForControlResponse() {return isWaitingForControlResponse;}
    public ConsoleControl getWaitingControlPrompt() {return this.controlPrompt;}
    public int getControlResponses() {return this.controlResponses;}
    public int getRequiredControlRequests() {return this.requiredControlRequests;}

    public boolean isInDangerZone() {
        return isInDangerZone;
    }
    public float dangerZoneShakeScale() {
        return this.dangerZoneShakeScale;
    }

    public boolean areControlEventsComplete() {
        return this.controlResponses == this.requiredControlRequests;
    }

    public boolean areDangerZoneEventsComplete() {
        return this.requiredDangerZoneRequests <= this.dangerZoneResponses;
    }

    /*
    * Is a prompt still within the combo time.
    * */
    private boolean isEventInComboTime() {
        return (this.ticksSincePrompted < 3 * 20);
    }

    /*
    * Get the current remaining ticks of cooldown between two controls.
    * */
    private int getControlRequestCooldown() {
        return (isEventInComboTime() ? 20 : 60);  // This will be expanded on when Stats are added.
    }

    /*
    * Calculates the number of required control requests based on the distance between the current and target location.
    * */
    public void calculateTravelLogic() {

        // Only trigger a responses reset if we haven't started flight yet.
        if (this.requiredControlRequests == 0) {
            this.controlResponses = 0;
            this.isWaitingForControlResponse = false;
            this.controlRequestCooldown = getControlRequestCooldown();
            this.collectedTXPPool = 0;
        }

        // Calculate the distance between two points
        var current = this.operator.getExteriorManager().getLastKnownLocation().position;
        var target = this.operator.getControlManager().getTargetLocation().position;
        Vec3 currentVec = new Vec3(current.getX(), current.getY(), current.getZ());
        Vec3 targetVec = new Vec3(target.getX(), target.getY(), target.getZ());

        // Determine if the distance is worth the prompts
        var distance = currentVec.distanceTo(targetVec);
        this.requiredControlRequests = (distance > MIN_DISTANCE_FOR_EVENTS) ? getBlocksPerRequest(distance): 0;
    }

    private int getBlocksPerRequest(double distance) {
        var bpd = (int) (distance / MIN_DISTANCE_FOR_EVENTS);
        return (bpd < 25) ? bpd : 25; // This will be expanded once stats are added.
    }

    // All the logic related to the in-flight events of the TARDIS.
    public void tick() {
        if (this.operator.getControlManager().isInFlight() && !this.operator.getControlManager().isAutoLandSet()) {

            if (!this.operator.getControlManager().isCrashing()) {
                ticksSincePrompted++;

                if (controlRequestCooldown > 0) controlRequestCooldown--;

                // Prepare the next control for highlighting.
                if (!isWaitingForControlResponse && controlRequestCooldown == 0 && this.controlResponses < this.requiredControlRequests && !operator.getControlManager().isAutoLandSet()) {

                    // Record what control type needs pressing.
                    this.controlPrompt = possibleControls.get(operator.getLevel().random.nextInt(possibleControls.size()-1));

                    // Set what control needs to be good
                    isWaitingForControlResponse = true;

                    this.ticksSincePrompted = 0;
                }

                if (!this.isInDangerZone && !this.areControlEventsComplete() && ticksSincePrompted > 30 * 20 && this.requiredControlRequests > 0) {
                    if (this.operator.getLevel().getGameTime() % (5 * 20) >= 0 && this.operator.getLevel().random.nextInt(10) == 0) {
                        this.isInDangerZone = true;
                        this.ticksInTheDangerZone = 0;
                        this.requiredDangerZoneRequests = this.operator.getLevel().random.nextInt(5); // We want to randomly add to this number count.
                    }
                }

                // Get us out of the dangerzone.
                if (this.isInDangerZone() && this.areDangerZoneEventsComplete()) {
                    this.isInDangerZone = false;
                    this.ticksInTheDangerZone = 0;
                    this.dangerZoneResponses = 0;
                    this.dangerZoneShakeScale = 0;

                    for (int i = 0; i < 3; i++) {
                        this.operator.getLevel().playSound(null, TardisArchitectureHandler.DESKTOP_CENTER_POS, SoundRegistry.TIME_BLAST.get(), SoundSource.BLOCKS, 1000f, 1f);
                    }

                }

                if (this.isInDangerZone) {
                    tickDangerLevels();
                }
            }
        } else {
            if (ticksSincePrompted != 0) {ticksSincePrompted = 0;}
            if (requiredControlRequests != 0) {requiredControlRequests = 0;}
        }
    }

    public void tickDangerLevels() {
        ticksInTheDangerZone++;
        // Tick every second
        var scaleForDanger = 0.5f;

        if (operator.getLevel().getGameTime() % (10 * 20) == 0) {
            this.requiredDangerZoneRequests++;
        }

        if (dangerZoneSecondsPast(10)) {
            if (operator.getLevel().getGameTime() % (6 * 20) == 0) {
                this.operator.getLevel().playSound(null, TardisArchitectureHandler.DESKTOP_CENTER_POS, SoundEvents.MINECART_INSIDE, SoundSource.BLOCKS, 1000f, 0.1f);
            }

            scaleForDanger = 0.7f;
        }

        if (dangerZoneSecondsPast(30)) {
            if (operator.getLevel().getGameTime() % (3 * 20) == 0) {
                playCloisterBell();
            }

            scaleForDanger = 1f;
        }

        if (dangerZoneSecondsPast(60)) {
            for (Player player : this.operator.getLevel().players()) {
                MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.DARKNESS, 10, 0, false, false);
                player.addEffect(mobEffectInstance);
            }

            scaleForDanger = 1.75f;
        }

        this.dangerZoneShakeScale = scaleForDanger;


        // The requests are too great, the TARDIS needs to crash.
        if (this.requiredDangerZoneRequests >= 10) {
            this.collectedTXPPool = 0; // You crash, so you gain no TXP.
            this.operator.getControlManager().crash();
            this.isWaitingForControlResponse = false;
            this.isInDangerZone = false;
            this.ticksInTheDangerZone = 0;
            this.dangerZoneShakeScale = 3f;
        }
    }

    public void respondToWaitingControl(ControlEntity entity, ConsoleControl control) {
        // Assign a cooldown between the controls determined by stats.
        this.controlRequestCooldown = getControlRequestCooldown();

        var gainedTXP = 0;

        // Increment the number of control responses
        // If we're in the danger zone, we don't want to advance the normal flight. We'd rather force the player to get out of it first.
        if (isInDangerZone()) {
            this.dangerZoneResponses++;
            gainedTXP++; // Gain an additional TXP if you're in the danger-zone.
        } else {
            this.controlResponses++;
        }

        this.isWaitingForControlResponse = false;

        if (this.controlResponses == this.requiredControlRequests) {
            operator.getLevel().playSound(null, entity.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 10, 1);
            gainedTXP += operator.getLevel().random.nextInt(2);
        } else {
            if (this.isEventInComboTime()) {
                float pitch = 1.25f * getPercentComplete();
                operator.getLevel().playSound(null, entity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 10, pitch);
                gainedTXP += operator.getLevel().random.nextInt(3);
            }
        }

        this.collectedTXPPool += gainedTXP; // Add the gained TXP to the TXP pool.

    }

    private boolean dangerZoneSecondsPast(int seconds) {
        return (this.ticksInTheDangerZone >= seconds * 20);
    }

    public float getPercentComplete() {
        return (float)this.controlResponses / (float)this.requiredControlRequests;
    }

    private void playCloisterBell() {
        for (int i = 0; i < 3; i++) {
            this.operator.getLevel().playSound(null, TardisArchitectureHandler.DESKTOP_CENTER_POS, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1000f, 0.1f);

        }
    }

    public int getCollectedTXPPool() {
        return collectedTXPPool;
    }
}
