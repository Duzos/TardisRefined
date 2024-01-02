package whocraft.tardis_refined.common.tardis.control.flight;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import whocraft.tardis_refined.common.capability.TardisLevelOperator;
import whocraft.tardis_refined.common.entity.ControlEntity;
import whocraft.tardis_refined.common.tardis.control.Control;
import whocraft.tardis_refined.common.tardis.manager.TardisPilotingManager;
import whocraft.tardis_refined.common.tardis.themes.ConsoleTheme;
import whocraft.tardis_refined.common.tardis.themes.console.sound.PitchedSound;
import whocraft.tardis_refined.common.util.PlayerUtil;

public class SpeedModifierControl extends Control {

    @Override
    public boolean onLeftClick(TardisLevelOperator operator, ConsoleTheme theme, ControlEntity controlEntity, Player player) {
        if (!operator.getLevel().isClientSide()){
            TardisPilotingManager pilotManager = operator.getPilotingManager();

            pilotManager.deincrementSpeedModifier();
            var currentSpeed = pilotManager.getSpeedModifier();

            PlayerUtil.sendMessage(player, Component.literal(getSpeedVisual(currentSpeed, pilotManager.getMaxSpeedModifier())), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onRightClick(TardisLevelOperator operator, ConsoleTheme theme, ControlEntity controlEntity, Player player) {
        if (!operator.getLevel().isClientSide()){
            TardisPilotingManager pilotManager = operator.getPilotingManager();

            pilotManager.incrementSpeedModifier();
            var currentSpeed = pilotManager.getSpeedModifier();

            System.out.println(currentSpeed);

            PlayerUtil.sendMessage(player, Component.literal(getSpeedVisual(currentSpeed, pilotManager.getMaxSpeedModifier())), true);
            return true;
        }
        return false;
    }
    @Override
    public PitchedSound getFailSound(TardisLevelOperator operator, ConsoleTheme theme, boolean leftClick) {
        return new PitchedSound(SoundEvents.NOTE_BLOCK_BIT.value(), 1F);
    }

    private String getSpeedVisual(int speed, int maxSpeed) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[  ");

        for (int i = 0; i < speed; i++) {
            stringBuilder.append("x ");
        }

        for (int i = 0; i < maxSpeed - speed; i++) {
            stringBuilder.append("- ");
        }

        stringBuilder.append(" ]");

        return stringBuilder.toString();
    }
}
