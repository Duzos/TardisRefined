package whocraft.tardis_refined.common.tardis.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import whocraft.tardis_refined.common.capability.TardisLevelOperator;
import whocraft.tardis_refined.common.entity.ControlEntity;
import whocraft.tardis_refined.common.mixin.EndDragonFightAccessor;
import whocraft.tardis_refined.common.tardis.TardisArchitectureHandler;
import whocraft.tardis_refined.common.tardis.control.ConsoleControl;
import whocraft.tardis_refined.common.util.TardisHelper;
import whocraft.tardis_refined.registry.SoundRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TardisFlightEventManager extends BaseHandler {

    private final TardisLevelOperator operator;
    private final List<ConsoleControl> possibleControls;
    private ConsoleControl controlPrompt;
    private int requiredFlightEventRolls;
    private int flightEventRolls;

    private final int MIN_DISTANCE_FOR_EVENTS = 30;
    private int controlRequestCooldown = 0;
    private boolean isWaitingForControlResponse = false;
    private int currentEventLifeTimeTicks = 0;

    private boolean isInDangerZone = false;
    private int ticksInTheDangerZone = 0;
    private int requiredDangerZoneRequests = 0;
    private int dangerZoneResponses = 0;

    private float dangerZoneShakeScale = 0f;

    private boolean hasCompletedFlight = false;

    public TardisFlightEventManager(TardisLevelOperator operator) {
        this.operator = operator;
        this.possibleControls = Arrays.stream(ConsoleControl.values()).filter(x -> x != ConsoleControl.MONITOR && x != ConsoleControl.THROTTLE).toList();
    }

    @Override
    public void loadData(CompoundTag tag) {
        this.isWaitingForControlResponse = tag.getBoolean("isWaitingForControlResponse");
        this.isInDangerZone = tag.getBoolean("isInDangerZone");
        this.ticksInTheDangerZone = tag.getInt("ticksInTheDangerZone");
        this.requiredFlightEventRolls = tag.getInt("requiredDangerZoneRequests");
        this.dangerZoneResponses = tag.getInt("dangerZoneResponses");
        this.controlRequestCooldown = tag.getInt("controlRequestCooldown");
        this.currentEventLifeTimeTicks = tag.getInt("ticksSincePrompted");
        this.dangerZoneShakeScale = tag.getInt("dangerZoneShakeScale");
        this.controlPrompt = ConsoleControl.findOr(tag.getString("controlPrompt"), ConsoleControl.THROTTLE);
        this.hasCompletedFlight =  tag.getBoolean("hasCompletedFlight");
    }

    @Override
    public CompoundTag saveData(CompoundTag tag) {
        tag.putBoolean("isWaitingForControlResponse", this.isWaitingForControlResponse);
        tag.putBoolean("isInDangerZone", this.isInDangerZone);
        tag.putInt("ticksInTheDangerZone", this.ticksInTheDangerZone);
        tag.putInt("requiredDangerZoneRequests", this.requiredFlightEventRolls);
        tag.putInt("dangerZoneResponses", this.dangerZoneResponses);
        tag.putInt("controlRequestCooldown", this.controlRequestCooldown);
        tag.putInt("ticksSincePrompted", this.currentEventLifeTimeTicks);
        tag.putFloat("dangerZoneShakeScale", this.dangerZoneShakeScale);
        tag.putBoolean("hasCompletedFlight", this.hasCompletedFlight);

        if (this.controlPrompt != null) {
            tag.putString("controlPrompt", this.controlPrompt.getSerializedName());
        }

        return tag;
    }

    public boolean isWaitingForControlResponse() {
        return isWaitingForControlResponse;
    }

    public ConsoleControl getWaitingControlPrompt() {
        return this.controlPrompt;
    }

    public int getRequiredFlightEventRolls() {
        return this.requiredFlightEventRolls;
    }

    public boolean isInDangerZone() {
        return isInDangerZone;
    }

    public float dangerZoneShakeScale() {
        return this.dangerZoneShakeScale;
    }

    public boolean areControlEventsComplete() {
        return this.hasCompletedFlight;
    }

    public boolean areDangerZoneEventsComplete() {
        return this.requiredDangerZoneRequests <= this.dangerZoneResponses;
    }

    /**
     * @return The total required danger zone requests the player will need to complete
     */
    public int getRequiredDangerZoneRequests() {
        return this.requiredDangerZoneRequests;
    }

    /**
     * @return The total danger zone responses the player has completed so far
     */
    public int getDangerZoneResponses() {
        return this.dangerZoneResponses;
    }

    /*
     * Get the current remaining ticks of cooldown between two controls.
     * */
    private int getControlRequestCooldown() {
        return (60 / this.operator.getPilotingManager().getSpeedModifier());  // This will be expanded on when Stats are added.
    }

    /*
     * Get the current remaining ticks of cooldown between two controls.
     * */
    public int getCurrentControlRequestCooldown() {
        return this.controlRequestCooldown;
    }

    /**
     * All the valid controls that can be used as a flight event and need to be interacted with
     *
     * @return a list of flight event capable controls.
     */
    public List<ConsoleControl> getPossibleControls() {
        return this.possibleControls;
    }

    // The minimum distance that the tardis needs to travel for flight events to happen.
    public int getMinDistanceForEvents() {
        return this.MIN_DISTANCE_FOR_EVENTS;
    }

    // How many ticks since the last flight event prompt
    public int getCurrentEventLifeTimeTicks() {
        return this.currentEventLifeTimeTicks;
    }

    // how many ticks the tardis has been in the danger zone for
    public int getTicksInTheDangerZone() {
        return this.ticksInTheDangerZone;
    }

    // the danger zone shake scale
    public float getDangerZoneShakeScale() {
        return this.dangerZoneShakeScale;
    }

    /*
     * Calculates the number of required control requests based on the distance between the current and target location.
     * */
    public void calculateTravelLogic() {

//        // Only trigger a responses reset if we haven't started flight yet.
//        if (this.requiredFlightEventRolls == 0) {
//            this.controlResponses = 0;
//            this.isWaitingForControlResponse = false;
//            this.controlRequestCooldown = getControlRequestCooldown();
//        }
//
//        // Calculate the distance between two points
//        var current = this.operator.getExteriorManager().getLastKnownLocation().getPosition();
//        var target = this.operator.getPilotingManager().getTargetLocation().getPosition();
//        Vec3 currentVec = new Vec3(current.getX(), current.getY(), current.getZ());
//        Vec3 targetVec = new Vec3(target.getX(), target.getY(), target.getZ());
//
//        // Determine if the distance is worth the prompts
//        var distance = currentVec.distanceTo(targetVec);
//        var dimensionalDistance = (this.operator.getExteriorManager().getLastKnownLocation().getDimensionKey() != this.operator.getPilotingManager().getTargetLocation().getDimensionKey()) ? 10 : 0;
//        this.requiredFlightEventRolls = 3 + dimensionalDistance;
//        if ((distance > MIN_DISTANCE_FOR_EVENTS)) {this.requiredFlightEventRolls += getBlocksPerRequest(distance);}

        calculateFlightParameters();

    }

    public void resetTravelLogic() {
        this.isWaitingForControlResponse = false;
        this.controlRequestCooldown = 0;
        this.flightEventRolls = 0;
        this.hasCompletedFlight = false;
        this.requiredFlightEventRolls = 0;
    }

    public int getBlocksPerRequest(double distance) {
        var bpd = (int) (distance / MIN_DISTANCE_FOR_EVENTS );
        return Math.min(25, bpd) * ((int) (this.operator.getPilotingManager().getSpeedModifier()) + 1);
    }

    // All the logic related to the in-flight events of the TARDIS.
    public void tick() {
//        if (this.operator.getPilotingManager().isInFlight() && !this.operator.getPilotingManager().isAutoLandSet()) {
//            if (!this.operator.getPilotingManager().isCrashing()) {
//                currentEventLifeTimeTicks++;
//
//                if (controlRequestCooldown > 0) controlRequestCooldown--;
//
//                // Prepare the next control for highlighting.
//                if (!isWaitingForControlResponse && controlRequestCooldown == 0 && this.controlResponses < this.requiredControlRequests && !operator.getPilotingManager().isAutoLandSet()) {
//
//                    // Record what control type needs pressing.
//                    this.controlPrompt = possibleControls.get(operator.getLevel().random.nextInt(possibleControls.size()-1));
//
//                    // Set what control needs to be good
//                    isWaitingForControlResponse = true;
//
//                    this.currentEventLifeTimeTicks = 0;
//                }
//
//                if (!this.isInDangerZone && !this.areControlEventsComplete() && currentEventLifeTimeTicks > 30 * 20) {
//                    if (this.operator.getLevel().getGameTime() % (5 * 20) >= 0 && this.operator.getLevel().random.nextInt(10) == 0) {
//                        this.isInDangerZone = true;
//                        this.ticksInTheDangerZone = 0;
//                        this.requiredDangerZoneRequests = this.operator.getLevel().random.nextInt(5); // We want to randomly add to this number count.
//                    }
//                }
//
//                // Get us out of the dangerzone.
//                if (this.isInDangerZone() && this.areDangerZoneEventsComplete()) {
//                    this.isInDangerZone = false;
//                    this.ticksInTheDangerZone = 0;
//                    this.dangerZoneResponses = 0;
//                    this.dangerZoneShakeScale = 0;
//
//                    for (int i = 0; i < 3; i++) {
//                        this.operator.getLevel().playSound(null, TardisArchitectureHandler.DESKTOP_CENTER_POS, SoundRegistry.TIME_BLAST.get(), SoundSource.BLOCKS, 1000f, 1f);
//                    }
//
//                }
//
//                if (this.isInDangerZone) {
//                    tickDangerLevels();
//                }
//            }
//        } else {
//            if (currentEventLifeTimeTicks != 0) {
//                currentEventLifeTimeTicks = 0;}
//            if (requiredControlRequests != 0) {requiredControlRequests = 0;}
//        }

        reworkedFlightTick();
    }

    /*
     * Calculates the distance and the number of required rolls for the upcoming flight. Can be reused mid-flight.
     * */
    public void calculateFlightParameters() {

        // Only trigger a reset if we haven't started flight yet.
        if (this.requiredFlightEventRolls == 0) {
            this.isWaitingForControlResponse = false;
            this.controlRequestCooldown = 0;
            this.flightEventRolls = 0;

        }

        this.hasCompletedFlight = false;

        // Calculate the distance between two points
        var current = this.operator.getExteriorManager().getLastKnownLocation().getPosition();
        var target = this.operator.getPilotingManager().getTargetLocation().getPosition();
        Vec3 currentVec = new Vec3(current.getX(), current.getY(), current.getZ());
        Vec3 targetVec = new Vec3(target.getX(), target.getY(), target.getZ());

        var distance = currentVec.distanceTo(targetVec);
        var dd = 10 / this.operator.getPilotingManager().getSpeedModifier();
        var dimensionalDistance = (this.operator.getExteriorManager().getLastKnownLocation().getDimensionKey() != this.operator.getPilotingManager().getTargetLocation().getDimensionKey()) ?  dd : 0;

        // The required rolls does not mean required flight events to be completed as the TARDIS may beat the check.
        this.requiredFlightEventRolls = 3 + dimensionalDistance;
        if ((distance > MIN_DISTANCE_FOR_EVENTS)) {
            this.requiredFlightEventRolls += getBlocksPerRequest(distance);
        }

        System.out.println("Number of required rolls: " + this.requiredFlightEventRolls);

    }

    // This holds the logic for the intended rework of flight events.
    public void reworkedFlightTick() {

        // If we're crashing, don't run this logic.
        if (this.operator.getPilotingManager().isCrashing()) {
            return;
        }

        // We're in flight but the AUTO land has been triggered. Don't bother with flight events at this time.
        if (this.operator.getPilotingManager().isInFlight() && this.operator.getPilotingManager().isAutoLandSet()) {

            System.out.println("This guy thinks he should be autolanding");

            this.requiredFlightEventRolls = 0;
            this.currentEventLifeTimeTicks = 0;
            this.flightEventRolls = 0;
        } else {

            if (!this.operator.getPilotingManager().isInFlight()) {
                return;
            }

            // Flight events should somewhat be calculated here.
            if (currentEventLifeTimeTicks > 0) {
                currentEventLifeTimeTicks++;
            }

            if (this.controlRequestCooldown > 0) {
                this.controlRequestCooldown--;
            }

            if (!isWaitingForControlResponse && !hasCompletedFlight && controlRequestCooldown <= 0) {

                if (flightEventRolls >= requiredFlightEventRolls) {
                    this.hasCompletedFlight = onTargetReached();
                } else {
                    flightEventCheck();
                }
            }
        }
    }

    /*
     * See if the TARDIS ignores the flight event.
     * */
    private void flightEventCheck() {
        // Roll a d20, you must beat the DC of whatever speed you're flight at in order to avoid having a flight event appear.

        System.out.println("Attempting a roll");

        var flightSpeed = operator.getPilotingManager().getSpeedModifier();

        // TODO: Upgrades add a modifier for the final roll.
        var modifier = 0;

        // Calculate the difficulty of beating a flight event appearance.
        var baseDC = 5;
        var difficultyClass = baseDC + ( flightSpeed * 2 );

        var random = operator.getLevel().getRandom().nextInt(20) + modifier;

        System.out.println("TARDIS rolled a " + random);
        System.out.println("DC was " + difficultyClass);

        if (random < difficultyClass && flightSpeed != 1) {
            // A flight event triggers.
            System.out.println("TARDIS FAILED!");
            setControlForPrompt();
        } else {
            System.out.println("TARDIS passed!");
            this.currentEventLifeTimeTicks = 0;
            isWaitingForControlResponse = false;
            this.controlPrompt = null;
        }

        this.controlRequestCooldown = getControlRequestCooldown();
        this.flightEventRolls++;

    }

    private void setControlForPrompt() {
        // Record what control type needs pressing.
        this.controlPrompt = possibleControls.get(operator.getLevel().random.nextInt(possibleControls.size() - 1));
        isWaitingForControlResponse = true;
        this.currentEventLifeTimeTicks = 1;
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
                TardisHelper.playCloisterBell(operator);
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
            this.operator.getPilotingManager().crash();
            this.isWaitingForControlResponse = false;
            this.isInDangerZone = false;
            this.ticksInTheDangerZone = 0;
            this.dangerZoneShakeScale = 3f;
        }
    }

    public void respondToWaitingControl(ControlEntity entity, ConsoleControl control) {
        // Assign a cooldown between the controls determined by stats.
        this.controlRequestCooldown = getControlRequestCooldown();

        // Increment the number of control responses
        // If we're in the danger zone, we don't want to advance the normal flight. We'd rather force the player to get out of it first.
        if (isInDangerZone()) {
            this.dangerZoneResponses++;
        }

        this.isWaitingForControlResponse = false;
    }

    public boolean onTargetReached() {

        // Is the target acceptable?
        var targetPosition = operator.getPilotingManager().getTargetLocation();

        for (Player player : this.operator.getLevel().players()) {

            if (targetPosition.getLevel().dimension() != Level.END) {
                operator.getLevel().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 10, 1);
                return true;
            }

            // Check has the dragon been beaten?
            if (targetPosition.getLevel().dragonFight != null) {
                if (((EndDragonFightAccessor) targetPosition.getLevel().dragonFight).isDragonKilled()) {
                    operator.getLevel().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 10, 1);
                    return true;
                }
            }

            MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.DARKNESS, 20, 20, false, false);
            player.addEffect(mobEffectInstance);
        }


        this.requiredFlightEventRolls += 5;
        var level = operator.getLevel();
        operator.getPilotingManager().getTargetLocation().setLevel(targetPosition.getLevel().getServer().overworld());


        for (Player player : this.operator.getLevel().players()) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.AMBIENT, 1, 1);
            level.playSound(player, player.blockPosition(), SoundRegistry.TARDIS_MISC_SPARKLE.get(), SoundSource.AMBIENT, 10, 1);
        }

        //level.explode(null, entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ(), 0.1f, Level.ExplosionInteraction.NONE);

        return false;

    }

    public boolean dangerZoneSecondsPast(int seconds) {
        return (this.ticksInTheDangerZone >= seconds * 20);
    }

    public float getPercentComplete() {
        return (float) this.flightEventRolls / (float) this.requiredFlightEventRolls;
    }


}
