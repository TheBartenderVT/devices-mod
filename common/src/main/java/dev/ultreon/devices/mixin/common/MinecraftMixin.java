package dev.ultreon.devices.mixin.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void devices$init(GameConfig gameConfig, CallbackInfo ci) {
//        LOGGER.info("reloading applications");
//        ApplicationManager.getAllApplications().forEach(AppInfo::reload);
//        ClientModEvents.generateIconAtlas();
    }
}
