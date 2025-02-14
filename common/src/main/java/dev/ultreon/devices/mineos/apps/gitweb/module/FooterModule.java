package dev.ultreon.devices.mineos.apps.gitweb.module;

import dev.ultreon.devices.api.app.Icons;
import dev.ultreon.devices.api.app.Layout;
import dev.ultreon.devices.api.app.component.Button;
import dev.ultreon.devices.api.utils.RenderUtil;
import dev.ultreon.devices.mineos.apps.gitweb.component.GitWebFrame;

import java.awt.*;
import java.util.Map;

/**
 * @author MrCrayfish
 */
public class FooterModule extends Module {
    @Override
    public String[] getRequiredData() {
        return new String[]{"title", "sub-title", "home-page"};
    }

    @Override
    public String[] getOptionalData() {
        return new String[]{"color"};
    }

    @Override
    public int calculateHeight(Map<String, String> data, int width) {
        return 28;
    }

    @Override
    public void generate(GitWebFrame frame, Layout layout, int width, Map<String, String> data) {
        Button buttonScroll = new Button(0, 5, Icons.ARROW_UP);
        buttonScroll.left = width - buttonScroll.getWidth() - 5;
        buttonScroll.setToolTip("Scroll to Top", "Brings you back to the top of the page");
        buttonScroll.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (mouseButton == 0) {
                frame.scrollToTop();
            }
        });
        layout.addComponent(buttonScroll);

        Button buttonHome = new Button(0, 5, Icons.HOME);
        buttonHome.left = buttonScroll.left - buttonHome.getWidth() - 3;
        buttonHome.setToolTip("Go to Homepage", data.get("home-page"));
        buttonHome.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (mouseButton == 0) {
                frame.loadWebsite(data.get("home-page"));
            }
        });
        layout.addComponent(buttonHome);

        int color = Color.DARK_GRAY.getRGB();
        if (data.containsKey("color")) {
            color = Integer.parseInt(data.get("color"));
        }

        String title = GitWebFrame.parseFormatting(data.get("title"));
        String subTitle = GitWebFrame.parseFormatting(data.get("sub-title"));
        int finalColor = color;
        layout.setBackground((graphics, mc, x, y, width1, height, mouseX, mouseY, windowActive) -> {
            graphics.fill(x, y, x + width1, y + height, finalColor);

            RenderUtil.drawStringClipped(graphics, title, x + 5, y + 5, buttonHome.left - 10, -1, true);
            RenderUtil.drawStringClipped(graphics, subTitle, x + 5, y + 16, buttonHome.left - 10, Color.LIGHT_GRAY.getRGB(), false);
        });
    }
}
