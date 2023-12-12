package whocraft.tardis_refined.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.client.ModelRegistry;
import whocraft.tardis_refined.client.ParticleGallifrey;
import whocraft.tardis_refined.client.TRParticles;
import whocraft.tardis_refined.client.neoforge.ModelRegistryImpl;
import whocraft.tardis_refined.client.renderer.blockentity.RootPlantRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.console.GlobalConsoleRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.device.ConsoleConfigurationRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.device.EngineInterfaceRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.door.BulkHeadDoorRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.door.GlobalDoorRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.door.RootShellDoorRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.life.ArsEggRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.shell.GlobalShellRenderer;
import whocraft.tardis_refined.client.renderer.blockentity.shell.RootShellRenderer;
import whocraft.tardis_refined.client.renderer.entity.ControlEntityRenderer;
import whocraft.tardis_refined.registry.BlockEntityRegistry;
import whocraft.tardis_refined.registry.EntityRegistry;
import whocraft.tardis_refined.registry.ItemRegistry;
import whocraft.tardis_refined.registry.RegistrySupplier;

@Mod.EventBusSubscriber(modid = TardisRefined.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBus {

    @SubscribeEvent
    public static void onBuildTabsContent(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ItemRegistry.MAIN_TAB.get()) {
            for (RegistrySupplier<Item> item : ItemRegistry.TAB_ITEMS.stream().toList()) {
                event.accept(item.get());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModelRegistry.init();
        ModelRegistryImpl.register(event);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(TRParticles.GALLIFREY.get(), (ParticleEngine.SpriteParticleRegistration) (ParticleGallifrey.Provider::new));
    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BlockEntityRegistry.ROOT_PLANT.get(), RootPlantRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.ROOT_SHELL.get(), RootShellRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.ROOT_SHELL_DOOR.get(), RootShellDoorRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.GLOBAL_SHELL_BLOCK.get(), GlobalShellRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.GLOBAL_DOOR_BLOCK.get(), GlobalDoorRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.GLOBAL_CONSOLE_BLOCK.get(), GlobalConsoleRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.ARS_EGG.get(), ArsEggRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.BULK_HEAD_DOOR.get(), BulkHeadDoorRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.CONSOLE_CONFIGURATION.get(), ConsoleConfigurationRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.ENGINE_INTERFACE.get(), EngineInterfaceRenderer::new);

        EntityRenderers.register(EntityRegistry.CONTROL_ENTITY.get(), ControlEntityRenderer::new);
    }


}
