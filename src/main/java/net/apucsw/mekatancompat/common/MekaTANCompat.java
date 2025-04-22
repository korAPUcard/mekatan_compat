package net.apucsw.mekatancompat.common;

import mekanism.common.config.IMekanismConfig;
import mekanism.common.registration.impl.*;
import mekanism.common.registries.MekanismCreativeTabs;
import mekanism.common.util.MekanismUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@SuppressWarnings({"Convert2MethodRef", "unused", "forremoval"})
@Mod(MekaTANCompat.MODID)
public class MekaTANCompat
{
    public static final String MODID = "mekatan_compat";
    public static final Logger logger = LogUtils.getLogger();

    //public static final DeviceConfig devconf = new DeviceConfig();
    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();

    public static final ModuleDeferredRegister MODULES =  new ModuleDeferredRegister(MekaTANCompat.MODID);
    //public static final ModuleRegistryObject<?> EXAMPLE_UNIT = MODULES.registerMarker("example_unit", () -> MekaTANCompat.MODULE_EXAMPLE);

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MekaTANCompat.MODID);
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekaTANCompat.MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public MekaTANCompat(IEventBus modEventBus, ModContainer modContainer) {
        MekaTANCompat.ITEMS.register(modEventBus);
        MekaTANCompat.MODULES.register(modEventBus);
        //MekaTANCompat.ENTITY_TYPES.register(modEventBus);// DUMMY_DATA: Maybe further use, not now.
        //MekanismConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, devconf);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::buildCreativeModeTabContents);
        //modEventBus.addListener(this::sendCustomModules);
        //modEventBus.addListener(this::registerRenderers);// DUMMY_DATA: Maybe further use, not now.
        //NeoForge.EVENT_BUS.addListener(this::opticalSensorLaser);// DUMMY_DATA: Maybe further use, not now.
    }

    @NotNull
    @Contract("_ -> new")
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MekaTANCompat.MODID, path);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static ResourceLocation getResource(@NotNull MekanismUtils.ResourceType type, String name) {
        return MekaTANCompat.rl(type.getPrefix() + name);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        MekaTANCompat.logger.info("Loaded 'MekaTAN Compat' module.");
    }

    private void buildCreativeModeTabContents(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MekanismCreativeTabs.MEKANISM.get()) {
            ITEMS.getEntries().forEach(entry -> event.accept(entry.get()));
        }
    }

//    private void sendCustomModules(InterModEnqueueEvent event) {
//        final String ADD_SMARTPHONE_MODULES = "add_smartphone_modules";
//        MekanismIMC.addModuleContainer((Holder<Item>) ModularElectronicsItems.SMARTPHONE, ADD_SMARTPHONE_MODULES);
//        MekanismIMC.sendModuleIMC(ADD_SMARTPHONE_MODULES, MekanismModules.ENERGY_UNIT, MekanismModules.TELEPORTATION_UNIT);
//    }

    @EventBusSubscriber(modid = MekaTANCompat.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            if (ModList.get().isLoaded("curios")) {
                MekaTANCompat.logger.info("Curios API detected.");
                //CuriosRendererRegistry.register(MekaTANCompat.EXAMPLE.get(), WeaponsRenderer::new);
            }

//            event.enqueueWork(() -> {
//                ClientRegistrationUtil.setPropertyOverride(MekaTANCompat.EXAMPLE, Mekanism.rl("pull"), (stack, world, entity, seed) -> {
//                    if (entity != null && entity.getUseItem() == stack && stack.getItem() instanceof ItemMekaBow bow) {
//                        return (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / bow.getUseTick(stack);
//                    }
//                    return 0;
//                });
//                ClientRegistrationUtil.setPropertyOverride(MekaTANCompat.EXAMPLE, Mekanism.rl("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
//            });
        }
    }
}
