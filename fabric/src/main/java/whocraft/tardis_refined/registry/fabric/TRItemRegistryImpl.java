package whocraft.tardis_refined.registry.fabric;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import whocraft.tardis_refined.constants.ModMessages;
import whocraft.tardis_refined.registry.TRBlockRegistry;
import whocraft.tardis_refined.registry.TRItemRegistry;
import whocraft.tardis_refined.registry.RegistrySupplier;

public class TRItemRegistryImpl {

    public static final CreativeModeTab ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(TRBlockRegistry.GLOBAL_CONSOLE_BLOCK.get())).displayItems((enabledFeatures, entries) -> {
        for (RegistrySupplier<Item> item : TRItemRegistry.TAB_ITEMS) {
            entries.accept(item.get());
        }
    }).title(Component.translatable(ModMessages.ITEM_GROUP)).build();

    public static CreativeModeTab getCreativeTab() {
        return ITEM_GROUP;
    }


}
