package whocraft.tardis_refined.common.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import whocraft.tardis_refined.common.blockentity.device.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.common.blockentity.device.ManipulatorCraftingRecipeItem;
import whocraft.tardis_refined.registry.TRBlockRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Before 50 says anything. Yes, this should be made into a codec. Will I do it? No.
public class ManipulatorCrafting {

    public static List<ManipulatorCraftingRecipe> MANIPULATOR_CRAFTING_RECIPES = new ArrayList<ManipulatorCraftingRecipe>();

    public static void registerRecipes() {

        MANIPULATOR_CRAFTING_RECIPES.clear();

        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), Blocks.REDSTONE_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 1), Blocks.STONE_BUTTON),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 2), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 0), Blocks.STONE_BUTTON),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 1), Blocks.GLASS),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 2), Blocks.STONE_BUTTON),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 1), Blocks.STONE_BUTTON),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 2), Blocks.IRON_TRAPDOOR)

        ), TRBlockRegistry.GLOBAL_CONSOLE_BLOCK.get()));

        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), TRBlockRegistry.ZEITON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), Blocks.STONE),

                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 1), Blocks.GLASS_PANE),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 2), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 0), Blocks.GLASS_PANE),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 1), Blocks.EMERALD_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 2), Blocks.GLASS_PANE),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 1), Blocks.GLASS_PANE),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 2), Blocks.STONE),

                new ManipulatorCraftingRecipeItem(new BlockPos(0, 2, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 2, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 2, 2), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 2, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 2, 1), Blocks.DAYLIGHT_DETECTOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 2, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 2, 0), Blocks.STONE),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 2, 1), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 2, 2), Blocks.STONE)

        ), TRBlockRegistry.TERRAFORMER_BLOCK.get().asItem()));


        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), Blocks.PISTON),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get())

        ), TRBlockRegistry.GRAVITY_WELL.get().asItem()));


        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), Blocks.DAYLIGHT_DETECTOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get())

        ), TRBlockRegistry.FLIGHT_DETECTOR.get().asItem()));


        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), Blocks.OBSERVER),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), TRBlockRegistry.ZEITON_FUSED_COPPER_BLOCK.get())

        ), TRBlockRegistry.CONSOLE_CONFIGURATION_BLOCK.get().asItem()));

        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), Blocks.TARGET),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), Blocks.AMETHYST_BLOCK),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),

                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 0), Blocks.CUT_COPPER_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 1), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 2), Blocks.CUT_COPPER_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 1), Blocks.AIR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 2), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 0), Blocks.CUT_COPPER_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 1), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 2), Blocks.CUT_COPPER_SLAB)

        ), TRBlockRegistry.LANDING_PAD.get().asItem()));


        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 1), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 0), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 1), TRBlockRegistry.LANDING_PAD.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 0, 2), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 0), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 1), Blocks.SMOOTH_STONE_SLAB),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 0, 2), TRBlockRegistry.ZEITON_FUSED_IRON_BLOCK.get()),

                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 1), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 2), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 1), Blocks.AIR),
                new ManipulatorCraftingRecipeItem(new BlockPos(1, 1, 2), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 0), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 1), Blocks.IRON_TRAPDOOR),
                new ManipulatorCraftingRecipeItem(new BlockPos(2, 1, 2), Blocks.IRON_TRAPDOOR)

        ), TRBlockRegistry.CORRIDOR_TELEPORTER.get().asItem()));

        register(new ManipulatorCraftingRecipe(Arrays.asList(
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 0, 0), TRBlockRegistry.ZEITON_BLOCK.get()),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 1, 0), Blocks.POLISHED_DEEPSLATE_WALL),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 2, 0), Blocks.POLISHED_DEEPSLATE_WALL),
                new ManipulatorCraftingRecipeItem(new BlockPos(0, 3, 0), TRBlockRegistry.ZEITON_LANTERN.get())

        ), TRBlockRegistry.ARTRON_PILLAR.get()));
    }

    public static ManipulatorCraftingRecipe register(ManipulatorCraftingRecipe manipulatorCraftingRecipe) {
        MANIPULATOR_CRAFTING_RECIPES.add(manipulatorCraftingRecipe);
        ;
        return manipulatorCraftingRecipe;
    }


}
