package com.mrcrayfish.device.programs.system.layout;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.device.MrCrayfishDeviceMod;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.ScrollableLayout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Image;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.object.AppInfo;
import com.mrcrayfish.device.programs.gitweb.component.GitWebFrame;
import com.mrcrayfish.device.programs.system.ApplicationAppStore;
import com.mrcrayfish.device.programs.system.component.SlideShow;
import com.mrcrayfish.device.programs.system.object.AppEntry;
import com.mrcrayfish.device.programs.system.object.LocalEntry;
import com.mrcrayfish.device.programs.system.object.RemoteEntry;
import com.mrcrayfish.device.util.GuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.net.URI;
import java.net.URL;

/**
 * @author MrCrayfish
 */
public class LayoutAppPage extends Layout {
    private final Laptop laptop;
    private final AppEntry entry;
    private final ApplicationAppStore store;

    private Image imageBanner;
    private Image imageIcon;
    private Label labelTitle;
    private Label labelVersion;

    private boolean installed;

    public LayoutAppPage(Laptop laptop, AppEntry entry, ApplicationAppStore store) {
        super(250, 150);
        this.laptop = laptop;
        this.entry = entry;
        this.store = store;
    }

    @Override
    public void init() {
        if (entry instanceof LocalEntry) {
            installed = Laptop.getSystem().getInstalledApplications().contains(((LocalEntry) entry).info());
        }

        this.setBackground((pose, gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
        {
            Color color = new Color(Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
            Gui.fill(pose, x, y + 40, x + width, y + 41, color.brighter().getRGB());
            Gui.fill(pose, x, y + 41, x + width, y + 60, color.getRGB());
            Gui.fill(pose, x, y + 60, x + width, y + 61, color.darker().getRGB());
        });

        ResourceLocation resource = new ResourceLocation(entry.id());

        imageBanner = new Image(0, 0, 250, 40);
        imageBanner.setDrawFull(true);
        if (entry instanceof LocalEntry) {
            imageBanner.setImage(new ResourceLocation(resource.getNamespace(), "textures/app/banner/" + resource.getPath() + ".png"));
        } else if (entry instanceof RemoteEntry) {
            imageBanner.setImage(ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/banner.png");
        }
        this.addComponent(imageBanner);

        if (entry instanceof LocalEntry) {
            LocalEntry localEntry = (LocalEntry) entry;
            AppInfo info = localEntry.info();
            imageIcon = new Image(5, 26, 28, 28, info.getIconU(), info.getIconV(), 14, 14, 224, 224, Laptop.ICON_TEXTURES);
        } else if (entry instanceof RemoteEntry) {
            imageIcon = new Image(5, 26, 28, 28, ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/icon.png");
        }
        this.addComponent(imageIcon);

        if (store.certifiedApps.contains(entry)) {
            int width = Laptop.font.width(entry.name()) * 2;
            Image certifiedIcon = new Image(38 + width + 3, 29, 20, 20, Icons.VERIFIED);
            this.addComponent(certifiedIcon);
        }
        labelTitle = new Label(entry.name(), 38, 32);
        labelTitle.setScale(2);
        this.addComponent(labelTitle);

        String version = entry instanceof LocalEntry ? "v" + entry.version() + " - " + entry.author() : entry.author();
        labelVersion = new Label(version, 38, 50);
        this.addComponent(labelVersion);

        String description = GitWebFrame.parseFormatting(entry.description());
        ScrollableLayout descriptionLayout = ScrollableLayout.create(130, 67, 115, 78, description);
        this.addComponent(descriptionLayout);

        SlideShow slideShow = new SlideShow(5, 67, 120, 78);
        if (entry instanceof LocalEntry) {
            if (entry.screenshots() != null) {
                for (String image : entry.screenshots()) {
                    if (image.startsWith("http://") || image.startsWith("https://")) {
                        slideShow.addImage(image);
                    } else {
                        slideShow.addImage(new ResourceLocation(image));
                    }
                }
            }
        } else if (entry instanceof RemoteEntry) {
            RemoteEntry remoteEntry = (RemoteEntry) entry;
            String screenshotUrl = ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/screenshots/screenshot_%d.png";
            for (int i = 0; i < remoteEntry.screenshots; i++) {
                slideShow.addImage(String.format(screenshotUrl, i));
            }
        }
        this.addComponent(slideShow);

        if (entry instanceof LocalEntry) {
            AppInfo info = ((LocalEntry) entry).info();
            Button btnInstall = new Button(20, 2, installed ? "Delete" : "Install", installed ? Icons.CROSS : Icons.PLUS);
            btnInstall.setSize(55, 16);
            btnInstall.setClickListener((mouseX, mouseY, mouseButton) ->
            {
                if (mouseButton == 0) {
                    if (installed) {
                        laptop.removeApplication(info, (o, success) ->
                        {
                            btnInstall.setText("Install");
                            btnInstall.setIcon(Icons.PLUS);
                            installed = false;
                        });
                    } else {
                        laptop.installApplication(info, (o, success) ->
                        {
                            btnInstall.setText("Delete");
                            btnInstall.setIcon(Icons.CROSS);
                            installed = true;
                        });
                    }
                }
            });
            this.addComponent(btnInstall);

            //TODO implement support button
            /*if(info.getSupport() != null)
            {
                Button btnDonate = new Button(234, 44, Icons.COIN);
                btnDonate.setToolTip("Donate", "Opens a link to donate to author of the application");
                btnDonate.setSize(14, 14);
                this.addComponent(btnDonate);
            }*/
        } else if (entry instanceof RemoteEntry) {
            Button btnDownload = new Button(20, 2, "Download", Icons.IMPORT);
            btnDownload.setSize(66, 16);
            btnDownload.setClickListener((mouseX, mouseY, mouseButton) -> this.openWebLink("https://minecraft.curseforge.com/projects/" + ((RemoteEntry) entry).projectId));
            this.addComponent(btnDownload);
        }
    }

    @Override
    public void renderOverlay(PoseStack pose, Laptop laptop, Minecraft mc, int mouseX, int mouseY, boolean windowActive) {
        super.renderOverlay(pose, laptop, mc, mouseX, mouseY, windowActive);
        if (store.certifiedApps.contains(entry)) {
            int width = Laptop.font.width(entry.name()) * 2;
            if (GuiHelper.isMouseWithin(mouseX, mouseY, xPosition + 38 + width + 3, yPosition + 29, 20, 20)) {
                laptop.renderComponentTooltip(pose, Lists.newArrayList(new TextComponent("Certified App").withStyle(ChatFormatting.GREEN)), mouseX, mouseY);
            }
        }
    }

    private void openWebLink(String url) {
        try {
            URI uri = new URL(url).toURI();
            Class<?> class_ = Class.forName("java.awt.Desktop");
            Object object = class_.getMethod("getDesktop").invoke(null);
            class_.getMethod("browse", URI.class).invoke(object, uri);
        } catch (Throwable throwable1) {
            Throwable throwable = throwable1.getCause();
            MrCrayfishDeviceMod.LOGGER.error("Couldn't open link: {}", throwable == null ? "<UNKNOWN>" : throwable.getMessage());
        }
    }
}
