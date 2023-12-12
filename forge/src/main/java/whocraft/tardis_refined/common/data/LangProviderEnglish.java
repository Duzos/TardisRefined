package whocraft.tardis_refined.common.data;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.LanguageProvider;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.capability.upgrades.Upgrade;
import whocraft.tardis_refined.common.capability.upgrades.Upgrades;
import whocraft.tardis_refined.common.tardis.control.ConsoleControl;
import whocraft.tardis_refined.common.tardis.themes.ShellTheme;
import whocraft.tardis_refined.constants.ModMessages;
import whocraft.tardis_refined.registry.BlockRegistry;
import whocraft.tardis_refined.registry.EntityRegistry;
import whocraft.tardis_refined.registry.ItemRegistry;
import whocraft.tardis_refined.registry.SoundRegistry;

public class LangProviderEnglish extends LanguageProvider {

    public LangProviderEnglish(DataGenerator gen) {
        super(gen.getPackOutput(), TardisRefined.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        /*Sounds*/
        addSound(SoundRegistry.TARDIS_LAND.get(), "TARDIS lands");
        addSound(SoundRegistry.TARDIS_SINGLE_FLY.get(), "TARDIS flies");
        addSound(SoundRegistry.TARDIS_TAKEOFF.get(), "TARDIS takes off");
        addSound(SoundRegistry.TARDIS_CRASH_LAND.get(), "TARDIS crash lands");
        addSound(SoundRegistry.STATIC.get(), "Screen display static");
        addSound(SoundRegistry.PATTERN_MANIPULATOR.get(), "Pattern Manipulator activates");
        addSound(SoundRegistry.TARDIS_MISC_SPARKLE.get(), "TARDIS arriving");
        addSound(SoundRegistry.TIME_BLAST.get(), "Time Vortex blast");
        addSound(SoundRegistry.DESTINATION_DING.get(), "TARDIS reaches destination");
        addSound(SoundRegistry.ARS_HUM.get(), "ARS Tree Hum");

        /*Block*/
        add(BlockRegistry.ARS_EGG.get(), "ARS Egg");
        add(BlockRegistry.ARS_LEAVES.get(), "ARS Leaves");
        add(BlockRegistry.ARS_LEAVES_FENCE.get(), "ARS Fence");
        add(BlockRegistry.ARS_LEAVES_SLAB.get(), "ARS Slab");
        add(BlockRegistry.BULK_HEAD_DOOR.get(), "Bulk Head Door");
        add(BlockRegistry.ROOT_PLANT_BLOCK.get(), "Root Plant");
        add(BlockRegistry.ROOT_SHELL_BLOCK.get(), "Root Shell");
        add(BlockRegistry.TERRAFORMER_BLOCK.get(), "Terraformer");
        add(BlockRegistry.GLOBAL_CONSOLE_BLOCK.get(), "Console");
        add(BlockRegistry.INTERNAL_DOOR_BLOCK.get(), "Internal Door");
        add(BlockRegistry.GLOBAL_DOOR_BLOCK.get(), "Tardis Door");
        add(BlockRegistry.ROOT_SHELL_DOOR.get(), "Root Door");
        add(BlockRegistry.AIR_LOCK_GENERATION_BLOCK.get(), "Air Lock Generator");
        add(BlockRegistry.CONSOLE_CONFIGURATION_BLOCK.get(), "Console Configurator");
        add(BlockRegistry.LANDING_PAD.get(), "Landing Pad");
        add(BlockRegistry.GROWTH_STONE.get(), "Growth Stone");
        add(BlockRegistry.HARDENED_GROWTH_STONE.get(), "Hardened Growth Stone");
        add(BlockRegistry.FLIGHT_DETECTOR.get(), "Flight Detector");
        add(BlockRegistry.GLOBAL_SHELL_BLOCK.get(), "TARDIS");

        /*Items*/
        add(ItemRegistry.PATTERN_MANIPULATOR.get(), "Pattern Manipulator");
        add(ItemRegistry.KEY.get(), "Tardis Key");
        add(ItemRegistry.DRILL.get(), "Growth Drill");
        add(ItemRegistry.AERIAL_UPGRADE.get(), "Aerial");
        add(ItemRegistry.DEFENSE_UPGRADE.get(), "Defense Circuit");
        add(ItemRegistry.NAVIGATION_UPGRADE.get(), "Navigation Circuit");
        add(ModMessages.ITEM_KEYCHAIN, "Tardis Keyset");

        /*Entity*/
        add(EntityRegistry.CONTROL_ENTITY.get(), "Generic Control");

        /*Controls*/
        addControl(ConsoleControl.DOOR_TOGGLE, "Door Toggle");
        addControl(ConsoleControl.X, "X");
        addControl(ConsoleControl.Y, "Y");
        addControl(ConsoleControl.Z, "Z");
        addControl(ConsoleControl.INCREMENT, "Increment");
        addControl(ConsoleControl.ROTATE, "Direction");
        addControl(ConsoleControl.RANDOM, "Randomizer");
        addControl(ConsoleControl.THROTTLE, "Throttle");
        addControl(ConsoleControl.MONITOR, "Computer Bank");
        addControl(ConsoleControl.FAST_RETURN, "Fast Return");
        addControl(ConsoleControl.DIMENSION, "Dimension");

        /*Messages*/
        add(ModMessages.MSG_EXTERIOR_COOLDOWN, "You must wait %s seconds");
        add(ModMessages.MSG_KEY_BOUND, "Key Bound to %s");
        add(ModMessages.MSG_KEY_CYCLED, "Main: %s");

        /*Command*/
        add(ModMessages.CMD_DIM_NOT_A_TARDIS, ChatFormatting.RED + "%s is not a TARDIS Dimension!");
        add(ModMessages.CMD_NO_INTERNAL_DOOR, ChatFormatting.RED + "No Internal Door found in dimension %s! Consider using the default teleport command %s");
        add(ModMessages.CMD_EXPORT_DESKTOP_IN_PROGRESS, "Generating datapack for desktop %s, this may take some time depending on the structure's size...");
        add(ModMessages.CMD_EXPORT_DESKTOP_SUCCESS, ChatFormatting.GREEN + "Successfully exported desktop %s to datapack %s! Use the %s command to see changes.");
        add(ModMessages.CMD_EXPORT_DESKTOP_RESOURCE_PACK, ChatFormatting.BLUE + "To define the Desktop's preview image, please create a Resource Pack. See some example Resource Packs at: %s");
        add(ModMessages.CMD_EXPORT_DESKTOP_FAIL, ChatFormatting.RED + "Failed to export desktop %s!");

        /*Creative Tab*/
        add("itemGroup.tardis_refined.tardis_refined", "Tardis Refined");
        add("itemGroup.tardis_refined", "Tardis Refined");

        /*GUI*/
        add(ModMessages.UI_MONITOR_MAIN_TITLE, "COMPUTER BANK");
        add(ModMessages.UI_MONITOR_WAYPOINTS, "WAYPOINTS");
        add(ModMessages.UI_MONITOR_UPLOAD_WAYPOINTS, "WAYPOINT NAVIGATION");
        add(ModMessages.UI_MONITOR_UPLOAD_COORDS, "COORD NAVIGATION");
        add(ModMessages.UI_MONITOR_GPS, "GPS");
        add(ModMessages.UI_MONITOR_DESTINATION, "Destination");
        add(ModMessages.UI_LIST_SELECTION, "Currently selected: &s");
        add(ModMessages.UI_EXTERNAL_SHELL, "EXTERNAL SHELL CONFIGURATION");
        add(ModMessages.UI_SHELL_SELECTION, "EXTERNAL SHELL CONFIGURATION");
        add(ModMessages.UI_DESKTOP_SELECTION, "DESKTOP CONFIGURATION");
        add(ModMessages.UI_DESKTOP_CONFIGURATION, "DESKTOP CONFIGURATION");
        add(ModMessages.UI_DESKTOP_CANCEL_TITLE, "OPERATION IN PROGRESS");
        add(ModMessages.UI_DESKTOP_CANCEL_DESCRIPTION, "Systems disabled as a Desktop reconfiguration has been scheduled.");
        add(ModMessages.UI_DESKTOP_CANCEL_DESKTOP, "Would you like to cancel the upcoming reconfiguration?");
        add(ModMessages.UI_DESKTOP_CANCEL, "Cancel Desktop Reconfiguration");
        add(ModMessages.UI_MONITOR_NO_WAYPOINTS, "No Waypoints Saved!");
        add(ModMessages.UI_MONITOR_UPLOAD, "Upload");
        add(ModMessages.UI_MONITOR_ISSUES, "Issues:");
        add(ModMessages.UI_MONITOR_WAYPOINT_NAME, "Waypoint Name:");
        add(ModMessages.UI_UPGRADES, "Tardis Upgrades");
        add(ModMessages.UI_UPGRADES_BUY, "Purchase Upgrade?");
        add(ModMessages.UI_NO_INSTALLED_SUBSYSTEMS, "No Available Sub-Systems");

        /*Shell Themes*/
        addShell(ShellTheme.FACTORY.getId(), "Factory");
        addShell(ShellTheme.POLICE_BOX.getId(), "Police Box");
        addShell(ShellTheme.PHONE_BOOTH.getId(), "Phone Booth");
        addShell(ShellTheme.MYSTIC.getId(), "Mystic");
        addShell(ShellTheme.DRIFTER.getId(), "Drifter");
        addShell(ShellTheme.PRESENT.getId(), "Present");
        addShell(ShellTheme.BRIEFCASE.getId(), "Briefcase");
        addShell(ShellTheme.GROENING.getId(), "Groening");
        addShell(ShellTheme.VENDING.getId(), "Vending Machine");
        addShell(ShellTheme.BIG_BEN.getId(), "Big Ben");
        addShell(ShellTheme.NUKA.getId(), "Nuka");
        addShell(ShellTheme.GROWTH.getId(), "Growth");
        addShell(ShellTheme.PORTALOO.getId(), "Portaloo");
        addShell(ShellTheme.PAGODA.getId(), "Pagoda");
        addShell(ShellTheme.LIFT.getId(), "Lift");
        addShell(ShellTheme.HIEROGLYPH.getId(), "Hieroglyph");
        addShell(ShellTheme.CASTLE.getId(), "Castle");

        /*Tool Tips*/
        add(ModMessages.TOOLTIP_TARDIS_LIST_TITLE, "Key Set:");
        add(ModMessages.CONTROL_DIMENSION_SELECTED, "Selected: %s");

        /*Config*/
        add(ModMessages.CONFIG_IP_COMPAT, "Immersive Portals Compatibility?");
        add(ModMessages.CONFIG_CONTROL_NAMES, "Render control names?");
        add(ModMessages.CONFIG_BANNED_DIMENSIONS, "Banned Dimensions");
        add(ModMessages.CONFIG_IDLE_CONSOLE_ANIMS, "Play idle console animations");

        /*Upgrades*/
        addUpgrade(Upgrades.COORDINATE_INPUT.get(), "Coordinate Input", "Allows the Pilot to input coordinates with the monitor");
        addUpgrade(Upgrades.CHAMELEON_CIRCUIT_SYSTEM.get(), "Chameleon Circuit", "Allows the TARDIS to change it's shape");
        addUpgrade(Upgrades.DEFENSE_SYSTEM.get(), "Defense System", "Enables Defense Protocols");
        addUpgrade(Upgrades.HOSTILE_DISPLACEMENT.get(), "Hostile Action Displacement", "Enables the displacement of the TARDIS when attacked");
        addUpgrade(Upgrades.WAYPOINTS.get(), "Waypoints", "Allows the Pilot to create saved locations");
        addUpgrade(Upgrades.NAVIGATION_SYSTEM.get(), "Navigation System", "Allows upgrades to the TARDIS Navigation System");
        addUpgrade(Upgrades.TARDIS_XP.get(), "System Upgrades", "Allows upgrades to the TARDIS");
        addUpgrade(Upgrades.MATERIALIZE_AROUND.get(), "Materialize Around", "Allows the TARDIS to have entities enter while materalizing");
        addUpgrade(Upgrades.ARCHITECTURE_SYSTEM.get(), "Architecture", "Enables TARDIS Architecture Upgrades");
        addUpgrade(Upgrades.INSIDE_ARCHITECTURE.get(), "Desktop Reconfiguration", "Allows the Pilot to change the appearance of the TARDIS Desktop");
        addUpgrade(Upgrades.EXPLORER.get(), "Explorer I", "1000k Increment");
        addUpgrade(Upgrades.EXPLORER_II.get(), "Explorer II", "2500k Increment");
        addUpgrade(Upgrades.EXPLORER_III.get(), "Explorer III", "5000k Increment");
        addUpgrade(Upgrades.DIMENSION_TRAVEL.get(), "Inter-Dimensional Travel", "Allows the TARDIS to move between dimensions");
        addUpgrade(Upgrades.LANDING_PAD.get(), "Landing Pad", "Allows the TARDIS to be summoned to a landing pad");
    }

    public void addControl(ConsoleControl control, String name) {
        add(control.getTranslationKey(), name);
    }

    public void addShell(ResourceLocation theme, String name) {
        add(ModMessages.shell(theme.getPath()), name);
    }

    public void addUpgrade(Upgrade upgrade, String title, String description) {
        add(Util.makeDescriptionId("upgrade", Upgrades.UPGRADE_REGISTRY.getKey(upgrade)), title);
        add(Util.makeDescriptionId("upgrade", Upgrades.UPGRADE_REGISTRY.getKey(upgrade)) + ".description", description);
    }

    public void addSound(SoundEvent soundEvent, String lang) {
        String subtitleKey = SoundProvider.createSubtitle(soundEvent.getLocation().getPath());
        add(subtitleKey, lang);
    }

}
