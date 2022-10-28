package com.ultreon.devices.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.devices.core.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class GuiButtonClose extends Button {
    public GuiButtonClose(int x, int y) {
        super(x, y, 11, 11, Component.literal(""), (button) -> {

        }, (a, b, c, d) ->{}, (a)->{
            return MutableComponent.create(ComponentContents.EMPTY);
        });
    }

    @Override
    public void renderButton(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Font font = Minecraft.getInstance().font;
            RenderSystem.setShaderTexture(0, Window.WINDOW_GUI);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 0);
            RenderSystem.blendFunc(770, 771);

            int state = this.isHovered ? 1 : 0;
            blit(pose, this.getX(), this.getY(), state * this.width + 15, 0, this.width, this.height);
        }
    }

    public boolean isHovered() {
        return isHovered;
    }
}
