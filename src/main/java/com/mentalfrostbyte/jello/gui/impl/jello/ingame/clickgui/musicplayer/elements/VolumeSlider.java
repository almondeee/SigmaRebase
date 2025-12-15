package com.mentalfrostbyte.jello.gui.impl.jello.ingame.clickgui.musicplayer.elements;

import com.mentalfrostbyte.jello.gui.base.elements.Element;
import com.mentalfrostbyte.jello.gui.combined.AnimatedIconPanel;
import com.mentalfrostbyte.jello.gui.combined.CustomGuiScreen;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;

import java.util.ArrayList;
import java.util.List;

public class VolumeSlider extends Element {
    private static String[] field21371;
    private float volume = 1.0F;
    private boolean field21373 = false;
    private final List<Class6649> field21374 = new ArrayList<>();

    /**
     * Constructs a new VolumeSlider instance.
     *
     * @param parent   The CustomGuiScreen that this VolumeSlider belongs to.
     * @param iconName The name of the icon associated with this VolumeSlider.
     * @param xV       The x-coordinate of the VolumeSlider.
     * @param yV       The y-coordinate of the VolumeSlider.
     * @param width    The width of the VolumeSlider.
     * @param height   The height of the VolumeSlider.
     */
    public VolumeSlider(CustomGuiScreen parent, String iconName, int xV, int yV, int width, int height) {
        super(parent, iconName, xV, yV, width, height, false);
    }

    @Override
    public void draw(float partialTicks) {
        RenderUtil.drawRoundedRect(
                (float) this.x,
                (float) this.y,
                (float) (this.x + this.width),
                (float) this.y + (float) this.height * this.volume,
                RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.2F)
        );
        RenderUtil.drawRoundedRect(
                (float) this.x,
                (float) (this.y + this.height),
                (float) (this.x + this.width),
                (float) this.y + (float) this.height * this.volume,
                RenderUtil2.applyAlpha(ClientColors.LIGHT_GREYISH_BLUE.getColor(), 0.2F)
        );
        super.draw(partialTicks);
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseButton) {
        if (!super.onClick(this.x, this.y, mouseButton)) {
            this.field21373 = true;
            return false;
        } else {
            return true;
        }
    }

    public float method13706(int var1) {
        return (float) (var1 - this.method13272()) / (float) this.height;
    }

    @Override
    public void updatePanelDimensions(int mouseX, int mouseY) {
        super.updatePanelDimensions(mouseX, mouseY);
        if (this.field21373) {
            this.setVolume(this.method13706(mouseY));
            this.method13710();
        }
    }

    @Override
    public void onClick2(int mouseX, int mouseY, int mouseButton) {
        super.onClick2(mouseX, mouseY, mouseButton);
        this.field21373 = false;
    }

    @Override
    public void onScroll(float scroll) {
        if (this.method13298()) {
            this.setVolume(this.getVolume() - scroll / 2000.0F);
            this.method13710();
        }

        super.onScroll(scroll);
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float value) {
        this.volume = Math.min(Math.max(value, 0.0F), 1.0F);
    }

    public AnimatedIconPanel method13709(Class6649 var1) {
        this.field21374.add(var1);
        return this;
    }

    public void method13710() {
        for (Class6649 var4 : this.field21374) {
            var4.method20301(this);
        }
    }

    public interface Class6649 {
        void method20301(VolumeSlider var1);
    }
}
