package dev.ultreon.devices.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ultreon.devices.UltreonDevicesMod;
import dev.ultreon.devices.api.app.IIcon;
import dev.ultreon.devices.api.utils.RenderUtil;
import dev.ultreon.devices.api.bios.BiosNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * @author MrCrayfish
 */
public class ClientNotification implements Toast {
    private static final ResourceLocation TEXTURE_TOASTS = new ResourceLocation("devices:textures/gui/toast.png");

    private IIcon icon;
    private String title;
    private String subTitle;

    private ClientNotification() {
    }

    public static ClientNotification of(BiosNotification notification) {
        ClientNotification clientNotification = new ClientNotification();
        clientNotification.icon = notification.icon();
        clientNotification.title = notification.title();
        clientNotification.subTitle = notification.subTitle();

        return clientNotification;
    }

    @NotNull
    @Override
    public Visibility render(@NotNull GuiGraphics graphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE_TOASTS);
        graphics.blit(TEXTURE_TOASTS, 0, 0, 0, 0, 160, 32);
        Font font = toastComponent.getMinecraft().font;

        if (subTitle == null) {
            graphics.drawString(font, font.plainSubstrByWidth(I18n.get(title), 118), 38, 12, -1);
        } else {
            graphics.drawString(font, font.plainSubstrByWidth(I18n.get(title), 118), 38, 7, -1);
            graphics.drawString(font, font.plainSubstrByWidth(I18n.get(subTitle), 118), 38, 18, -1, false);
        }

        RenderSystem.setShaderTexture(0, icon.getIconAsset());
        RenderUtil.drawRectWithTexture(null, graphics, 6, 6, icon.getGridWidth(), icon.getGridHeight(), icon.getU(), icon.getV(), icon.getSourceWidth(), icon.getSourceHeight(), icon.getIconSize(), icon.getIconSize());

        return timeSinceLastVisible >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

    public static ClientNotification loadFromTag(CompoundTag tag) {
        ClientNotification notification = new ClientNotification();

        int ordinal = tag.getCompound("icon").getInt("ordinal");
        String className = tag.getCompound("icon").getString("className");

        try {
            notification.icon = (IIcon) Class.forName(className).getEnumConstants()[ordinal];
        } catch (ClassNotFoundException e) {
            UltreonDevicesMod.LOGGER.error("Failed to load icon", e);
        }

        notification.title = tag.getString("title");
        if (tag.contains("subTitle", Tag.TAG_STRING)) {
            notification.subTitle = tag.getString("subTitle");
        }

        return notification;
    }

    public void push() {
        Minecraft.getInstance().getToasts().addToast(this);
    }
}
