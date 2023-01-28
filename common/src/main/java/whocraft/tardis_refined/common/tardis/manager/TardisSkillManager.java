package whocraft.tardis_refined.common.tardis.manager;

// This class serves as a proof of concept for the TXP feature.

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import whocraft.tardis_refined.common.capability.TardisLevelOperator;
import whocraft.tardis_refined.common.tardis.TardisArchitectureHandler;
import whocraft.tardis_refined.common.util.PlayerUtil;

public class TardisSkillManager {

    private static final int MAX_PROGRESS_PER_LVL = 100;
    private static final int MAX_LEVELS = 100;
    private int txpLevel = 0;
    private int txpProgress = 0;

    private int treeStage = 0;
    private boolean upgradeQueued;


    private TardisLevelOperator operator;

    public TardisSkillManager(TardisLevelOperator operator) {
        this.operator = operator;
    }

    public CompoundTag saveData(CompoundTag tag) {
        tag.putInt("txp_level", txpLevel);
        tag.putInt("txp_progress", txpProgress);
        return tag;
    }

    public void loadData(CompoundTag tag) {
        this.txpLevel = tag.getInt("txp_level");
        this.txpProgress = tag.getInt("txp_progress");
    }


    // The functionality of this system does not need to be enacted every tick, so we'll restrict the operation.
    public void delayedTick() {

        //advanceLevelProgress(1);

        if (operator.getLevel().getGameTime() % (20 * 3) == 0) {
            if (upgradeQueued) {
                advanceTreeStage();
                upgradeQueued = false;
            }
        }
    }

    public boolean shouldUpgradeTree() {
        return this.txpLevel % 10 == 0 && this.txpLevel != 0 && this.txpLevel < 500;
    }

    public void advanceTreeStage() {
        treeStage++;

        // Send this information to the ARS tree system.
        if (operator.getLevel() instanceof ServerLevel serverLevel) {
            TardisArchitectureHandler.generateArsTreeStage(serverLevel, treeStage);
        }

    }

    // Advances the progress value for the TARDIS
    public void advanceLevelProgress(int amount) {
        var totalProgress = amount + txpProgress;

        int quotient = totalProgress / MAX_PROGRESS_PER_LVL;
        int remainder = totalProgress % MAX_PROGRESS_PER_LVL;

        this.txpLevel += quotient;
        this.txpProgress = remainder;
        this.upgradeQueued = shouldUpgradeTree();
        notifyThePlayers();
    }

    // Sets the progress bar level
    public void setLevelProgress(int amount) {
        var progress = amount;
        if (amount < 0) { progress = 0;}
        if (amount > 100) {progress = 100;}
        this.txpProgress = progress;
    }

    public void setTxpLevel(int amount) {
        var level = amount;
        if (amount < 0) { amount = 0;}
        if (amount > 100) {amount = 100;}
        this.txpLevel = amount;

        // Prepare the ARS tree for upgrading.
        this.upgradeQueued = shouldUpgradeTree();
    }


    public void notifyThePlayers() {
        if (operator.getLevel() instanceof ServerLevel serverLevel) {
            PlayerUtil.globalMessage(Component.translatable("TARDIS Advanced "  + this.txpProgress + " / 100 : " + this.txpLevel + "L"), serverLevel.getServer());
        }
    }
}
