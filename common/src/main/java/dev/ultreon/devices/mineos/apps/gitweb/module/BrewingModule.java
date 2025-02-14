package dev.ultreon.devices.mineos.apps.gitweb.module;

import dev.ultreon.devices.mineos.apps.gitweb.component.container.ContainerBox;
import dev.ultreon.devices.mineos.apps.gitweb.component.container.BrewingBox;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author MrCrayfish
 */
public class BrewingModule extends ContainerModule {
    @Override
    public String[] getOptionalData() {
        List<String> optionalData = new ArrayList<>(Arrays.asList(super.getOptionalData()));
        optionalData.add("slot-fuel");
        optionalData.add("slot-input");
        for (int i = 0; i < 3; i++) {
            optionalData.add("slot-output-" + (i + 1));
        }
        return optionalData.toArray(new String[0]);
    }

    @Override
    public int getHeight() {
        return BrewingBox.HEIGHT;
    }

    @Override
    public ContainerBox createContainer(Map<String, String> data) {
        ItemStack fuel = getItem(data, "slot-fuel");
        ItemStack input = getItem(data, "slot-input");
        ItemStack[] output = new ItemStack[3];
        Arrays.fill(output, ItemStack.EMPTY);
        for (int i = 0; i < output.length; i++) {
            output[i] = getItem(data, "slot-output-" + (i + 1));
        }
        return new BrewingBox(fuel, input, output);
    }
}
