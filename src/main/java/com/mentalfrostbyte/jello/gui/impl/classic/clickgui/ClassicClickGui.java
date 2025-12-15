package com.mentalfrostbyte.jello.gui.impl.classic.clickgui;

import com.google.gson.JsonObject;
import com.mentalfrostbyte.jello.gui.base.animations.Animation;
import com.mentalfrostbyte.jello.gui.base.elements.impl.critical.Screen;
import com.mentalfrostbyte.jello.gui.impl.classic.clickgui.panel.ClickGuiPanel;
import com.mentalfrostbyte.jello.module.data.ModuleCategory;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import net.minecraft.client.Minecraft;

public class ClassicClickGui extends Screen {
    private static final Minecraft field21078 = Minecraft.getInstance();
    private static Animation field21079;
    private static final boolean field21080 = true;
    private ClickGuiPanel category;

    public ClassicClickGui() {
        super("ClassicScreen");
        field21079 = new Animation(250, 200, Animation.Direction.FORWARDS);
        this.method13419();
        RenderUtil2.blur();
    }

    public void method13417() {
        this.addRunnable(() -> this.method13419());
    }

    public void method13418(String var1, ModuleCategory... var2) {
        this.addRunnable(() -> {
            if (this.category != null) {
                this.removeChildren(this.category);
            }

            this.addToList(this.category = new ModuleSettingGroup(this, var1, this.getWidth() / 2, this.getHeight() / 2, var2));
        });
    }

    private void method13419() {
        if (this.category != null) {
            this.removeChildren(this.category);
        }

        this.addToList(this.category = new CategoryHolder(this, "Sigma", this.getWidth() / 2, this.getHeight() / 2));
    }

    @Override
    public void updatePanelDimensions(int mouseX, int mouseY) {
        super.updatePanelDimensions(mouseX, mouseY);
    }

    @Override
    public int getFPS() {
        return Minecraft.getFps();
    }

    @Override
    public JsonObject toConfigWithExtra(JsonObject config) {
        RenderUtil2.resetShaders();
        return super.toConfigWithExtra(config);
    }

    @Override
    public void loadConfig(JsonObject config) {
        super.loadConfig(config);
    }

    @Override
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        if (keyCode == 256) {
            field21078.displayGuiScreen(null);
        }
    }

    @Override
    public void draw(float partialTicks) {
        float var4 = field21079.calcPercent();
        RenderUtil.drawRoundedRect(
                (float) this.x,
                (float) this.y,
                (float) (this.x + this.width),
                (float) (this.y + this.height),
                RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), var4 * 0.35F)
        );
        super.draw(partialTicks);
    }
}
