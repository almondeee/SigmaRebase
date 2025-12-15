package com.mentalfrostbyte.jello.gui.base.elements.impl.button;

import com.mentalfrostbyte.jello.gui.base.elements.Element;
import com.mentalfrostbyte.jello.gui.combined.CustomGuiScreen;
import com.mentalfrostbyte.jello.util.client.render.FontSizeAdjust;
import com.mentalfrostbyte.jello.util.client.render.theme.ColorHelper;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;
import org.newdawn.slick.TrueTypeFont;

public class Button extends Element {
    public float field20584;
    private int field20585 = 0;
    public int field20586 = 0;

    public Button(CustomGuiScreen screen, String iconName, int x, int y, int width, int height) {
        super(screen, iconName, x, y, width, height, false);
    }

    public Button(CustomGuiScreen screen, String iconName, int x, int y, int width, int var6, ColorHelper var7) {
        super(screen, iconName, x, y, width, var6, var7, false);
    }

    public Button(CustomGuiScreen screen, String iconName, int x, int y, int width, int var6, ColorHelper var7, String text) {
        super(screen, iconName, x, y, width, var6, var7, text, false);
    }

    public Button(CustomGuiScreen screen, String iconName, int x, int y, int width, int height, ColorHelper var7, String var8, TrueTypeFont font) {
        super(screen, iconName, x, y, width, height, var7, var8, font, false);
    }

    @Override
    public void updatePanelDimensions(int mouseX, int mouseY) {
        super.updatePanelDimensions(mouseX, mouseY);
        this.field20584 = this.field20584 + (!this.method13298() ? -0.1F : 0.1F);
        this.field20584 = Math.min(Math.max(0.0F, this.field20584), 1.0F);
    }

    @Override
    public void draw(float partialTicks) {
        float var4 = !this.isHovered() ? 0.3F : (!this.method13216() ? (!this.method13212() ? Math.max(partialTicks * this.field20584, 0.0F) : 1.5F) : 0.0F);
        int color = RenderUtil2.applyAlpha(
                RenderUtil2.shiftTowardsOther(this.textColor.getPrimaryColor(), this.textColor.getSecondaryColor(), 1.0F - var4),
                (float) (this.textColor.getPrimaryColor() >> 24 & 0xFF) / 255.0F * partialTicks
        );
        if (this.field20586 <= 0) {
            RenderUtil.drawRoundedRect(
                    (float) this.getX(),
                    (float) this.getY(),
                    (float) (this.getX() + this.getWidth()),
                    (float) (this.getY() + this.getHeight()),
                    color
            );
        } else {
            RenderUtil.drawRoundedButton(
                    (float) this.getX(), (float) this.getY(), (float) this.getWidth(), (float) this.getHeight(), (float) this.field20586, color
            );
        }

        int var10 = this.getX()
                + (
                this.textColor.method19411() != FontSizeAdjust.NEGATE_AND_DIVIDE_BY_2
                        ? 0
                        : (this.textColor.method19411() != FontSizeAdjust.WIDTH_NEGATE ? this.getWidth() / 2 : this.getWidth())
        );
        int var11 = this.getY()
                + (
                this.textColor.method19413() != FontSizeAdjust.NEGATE_AND_DIVIDE_BY_2
                        ? 0
                        : (this.textColor.method19413() != FontSizeAdjust.HEIGHT_NEGATE ? this.getHeight() / 2 : this.getHeight())
        );
        if (this.getText() != null) {
            RenderUtil.drawString(
                    this.getFont(),
                    (float) (this.field20585 + var10),
                    (float) var11,
                    this.getText(),
                    RenderUtil2.applyAlpha(this.textColor.getTextColor(), partialTicks),
                    this.textColor.method19411(),
                    this.textColor.method19413()
            );
        }

        super.draw(partialTicks);
    }

    public void method13034(int var1) {
        this.field20585 = var1;
    }

    public int method13035() {
        return this.field20585;
    }
}
