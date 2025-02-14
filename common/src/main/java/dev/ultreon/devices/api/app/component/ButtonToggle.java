package dev.ultreon.devices.api.app.component;

import dev.ultreon.devices.api.app.IIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class ButtonToggle extends Button implements RadioGroup.Item {
    protected boolean toggle = false;
    protected RadioGroup group = null;

    /**
     * Alternate button constructor
     *
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     * @param text text to be displayed in the button
     */
    public ButtonToggle(int left, int top, String text) {
        super(left, top, text);
    }

    /**
     * Alternate button constructor
     *
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     * @param icon
     */
    public ButtonToggle(int left, int top, IIcon icon) {
        super(left, top, icon);
    }

    /**
     * Alternate button constructor
     *
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     * @param icon
     */
    public ButtonToggle(int left, int top, String text, IIcon icon) {
        super(left, top, text, icon);
    }

    /**
     * Alternate button constructor
     *
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     */
    public ButtonToggle(int left, int top, ResourceLocation iconResource, int iconU, int iconV, int iconWidth, int iconHeight) {
        super(left, top, iconResource, iconU, iconV, iconWidth, iconHeight);
    }

    /**
     * Alternate button constructor
     *
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     */
    public ButtonToggle(int left, int top, String text, ResourceLocation iconResource, int iconU, int iconV, int iconWidth, int iconHeight) {
        super(left, top, text, iconResource, iconU, iconV, iconWidth, iconHeight);
    }

    /**
     * Sets the radio group for this button.
     *
     * @param group the radio group.
     */
    public void setRadioGroup(RadioGroup group) {
        this.group = group;
        this.group.add(this);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (!this.visible || !this.enabled) return;

        if (super.isInside(mouseX, mouseY)) {
            playClickSound(Minecraft.getInstance().getSoundManager());
            if (group != null) {
                group.deselect();
                this.toggle = true;
            } else {
                this.toggle = !toggle;
            }
            if (clickListener != null) {
                clickListener.onClick(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        if (toggle) {
            return 2;
        }
        return super.getHoverState(mouseOver);
    }

    @Override
    public boolean isSelected() {
        return toggle;
    }

    @Override
    public void setSelected(boolean selected) {
        this.toggle = selected;
    }
}
