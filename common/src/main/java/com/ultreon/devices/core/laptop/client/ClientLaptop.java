package com.ultreon.devices.core.laptop.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.devices.core.laptop.common.C2SUpdatePacket;
import com.ultreon.devices.core.laptop.common.TaskBar;
import com.ultreon.devices.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static com.ultreon.devices.core.laptop.client.ClientLaptopScreen.LAPTOP_GUI;


// NO STATICS
public class ClientLaptop {
    public static final HashMap<UUID, ClientLaptop> laptops = new HashMap<>(); // current active client laptops
    public static final int DEVICE_HEIGHT = 216;
    public static final int SCREEN_HEIGHT = DEVICE_HEIGHT - 20;
    public static final int DEVICE_WIDTH = 384;
    public static final int SCREEN_WIDTH = DEVICE_WIDTH - 20;
    public final double[] square = new double[2];
    private UUID uuid;
    private final TaskBar taskbar = new TaskBar(this);

    public ClientLaptop() {
        //super(Component.translatable("laptop")); //todo
    }

    public void handlePacket(String type, CompoundTag nbt) {
        if (type.equals("placeSquare")) {
            System.out.println("moving square lol");
            System.out.println(nbt);
            square[0] = nbt.getDouble("x");
            square[1] = nbt.getDouble("y");
            System.out.println("SET");
        }
    }

    public void sendPacket(String type, CompoundTag nbt) {
        System.out.printf("Sending packet %s, %s%n", type, nbt);
        PacketHandler.sendToServer(new C2SUpdatePacket(this.uuid, type, nbt));
    }

    public void renderBezels(final @NotNull PoseStack pose, final int mouseX, final int mouseY, float partialTicks) { // no bezels

    }

    //@Override
    public void render(final @NotNull PoseStack pose, final int mouseX, final int mouseY, float partialTicks) {
        double[] square = new double[2];
        Minecraft.getInstance().doRunTask(() -> {
            square[0] = this.square[0];
            square[1] = this.square[1];
        });
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, LAPTOP_GUI);
        //RenderSystem.disableBlend();
        Gui.blit(pose, 0, 0, ClientLaptop.SCREEN_WIDTH, ClientLaptop.SCREEN_HEIGHT, 10, 10, 1, 1, 256, 256);
        //RenderSystem.enableBlend();

        Minecraft.getInstance().font.draw(pose, "New Laptop System 0.01% complete", 0, 0, 0xffffff);
        Gui.fill(pose, 0, 0, 10, 10, 0x2e2e2e);
        taskbar.render(pose, this, Minecraft.getInstance(), 0, SCREEN_HEIGHT - 16, mouseX, mouseY, partialTicks);
        System.out.println("x = " + square[0]);
        Gui.fill(pose, (int) square[0], (int) square[1], (int) square[0] + 10, (int) square[1] + 10, 0xffffff);
    }

    public void mouseMoved(double mouseX, double mouseY) {
        var nbt = new CompoundTag();
        nbt.putDouble("x", mouseX);
        nbt.putDouble("y", mouseY);
        sendPacket("mouseMoved", nbt);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
