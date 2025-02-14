package dev.ultreon.devices.mineos.apps.gitweb.module;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.datafixers.util.Pair;
import dev.ultreon.devices.api.app.Component;
import dev.ultreon.devices.api.app.Layout;
import dev.ultreon.devices.mineos.client.MineOS;
import dev.ultreon.devices.debug.DebugLog;
import dev.ultreon.devices.mineos.apps.gitweb.component.GitWebFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.ultreon.devices.mineos.apps.gitweb.module.ContainerModule.getItem;

public class BannerIIModule extends Module {
    @Override
    public String[] getRequiredData() {
        return new String[]{"banner"};
    }

    @Override
    public String[] getOptionalData() {
        return new String[]{"waving"};
    }

    @Override
    public int calculateHeight(Map<String, String> data, int width) {
        return LoomBox.HEIGHT;
    }

    @Override
    public void generate(GitWebFrame frame, Layout layout, int width, Map<String, String> data) {
        layout.addComponent(createContainer(data));
    }

    public LoomBox createContainer(Map<String, String> data) {
        return new LoomBox(getItem(data, "banner"), Boolean.parseBoolean(data.get("waving")));
    }

    public static class LoomBox extends Component {
        public static final int HEIGHT = 84;
        private final ItemStack banner;
        private final ModelPart flag;
        private final List<Pair<Holder<BannerPattern>, DyeColor>> resultBannerPatterns;

        public LoomBox(ItemStack banner, boolean waving) {
            super(0, 0);
            this.banner = banner;
            this.flag = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");

            if (!banner.isEmpty())
                this.resultBannerPatterns = BannerBlockEntity.createPatterns(((BannerItem)this.banner.getItem()).getColor(), BannerBlockEntity.getItemPatterns(this.banner));
            else
                this.resultBannerPatterns = new ArrayList<>();
        }

        @Override
        protected void render(GuiGraphics graphics, MineOS laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
            super.render(graphics, laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
            if (banner.isEmpty())return;
            Lighting.setupForFlatItems();
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            graphics.pose().pushPose();
            //pose.translate((double)(i + 139), (double)(j + 52), 0.0D);
            graphics.pose().translate(x +139, y +90,0.0D);
            graphics.pose().scale(48.0F, -48.0F, 48.0F);
        //    pose.scale(24.0F, -24.0F, 1.0F);
            graphics.pose().translate(0.5D, 0.5D, 0.5D);
            float f = 0.6666667F;
            graphics.pose().scale(f, -f, -f);
            long l = System.currentTimeMillis()/50;
            DebugLog.log(l);
            float h = ((float)Math.floorMod(l, 100L) + partialTicks) / 100.0f;

            this.flag.yRot = (float) Math.toRadians(30);
            this.flag.xRot = (-0.0125f + 0.01f * Mth.cos((float)Math.PI * 2 * h)) * (float)Math.PI;
           // this.flag.xRot = 0.0F;
            this.flag.y = -32.0F;
            BannerRenderer.renderPatterns(graphics.pose(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, this.flag, ModelBakery.BANNER_BASE, true, this.resultBannerPatterns);
            graphics.pose().popPose();
            bufferSource.endBatch();


        }
    }
}
// 128, 72