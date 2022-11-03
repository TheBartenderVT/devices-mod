package com.ultreon.devices;

import com.ultreon.devices.init.DeviceBlocks;
import com.ultreon.devices.init.DeviceItems;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.*;

import static com.ultreon.devices.Devices.id;

public class DeviceTab {
    @SuppressWarnings("UnstableApiUsage")
    public static CreativeModeTab create() {
        System.out.println("creating creative tab");
//        CreativeTabRegistry.modify(CreativeModeTabs.TAB_REDSTONE, output -> {
//            output.acceptAll();
//        });
        return CreativeTabRegistry.create(id("devices_tab_device"), () -> new ItemStack(DeviceBlocks.LAPTOPS.of(DyeColor.RED).get()), (featureFlagSet, output) -> {
            for (RegistrySupplier<Item> laptop : DeviceItems.LAPTOPS) {
                output.accept(laptop.get());
            }
            for (RegistrySupplier<Item> printer : DeviceItems.PRINTERS) {
                output.accept(printer.get());
            }
            for (RegistrySupplier<Item> router : DeviceItems.ROUTERS) {
                output.accept(router.get());
            }
            for (RegistrySupplier<Item> office_chair : DeviceItems.OFFICE_CHAIRS) {
                output.accept(office_chair.get());
            }
            for (RegistrySupplier<Item> flashdrive : DeviceItems.FLASH_DRIVE) {
                output.accept(flashdrive.get());
            }
        });
    }
}
