package com.mentalfrostbyte.jello.gui.base.elements.impl.button.types;

import com.mentalfrostbyte.Client;
import com.mentalfrostbyte.jello.gui.base.elements.impl.VerticalScrollBar;
import com.mentalfrostbyte.jello.gui.impl.jello.buttons.ScrollableContentPanel;
import com.mentalfrostbyte.jello.gui.combined.AnimatedIconPanel;
import com.mentalfrostbyte.jello.util.client.ClientMode;
import com.mentalfrostbyte.jello.util.client.render.Resources;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;

public class VerticalScrollBarButton extends AnimatedIconPanel {
    private final ScrollableContentPanel field20780;
    public final VerticalScrollBar field20781;

    public VerticalScrollBarButton(VerticalScrollBar var1, VerticalScrollBar var2, int var3) {
        super(var2, "verticalScrollBarButton", 0, 0, var3, 10, true);
        this.field20781 = var1;
        this.field20886 = true;
        this.field20780 = (ScrollableContentPanel) var2.getParent();
        this.setListening(false);
    }

    @Override
    public void updatePanelDimensions(int mouseX, int mouseY) {
        super.updatePanelDimensions(mouseX, mouseY);
        float var5 = (float) this.field20780.getButton().getHeight();
        float var6 = (float) this.parent.getParent().getHeight();
        float var7 = (float) this.parent.getHeight();
        float var8 = var6 / var5;
        float var9 = var7 * var8;
        float var10 = 20.0F;
        if (!(var9 < 20.0F)) {
            if (var9 > var7) {
                var9 = var7;
            }
        } else {
            var9 = 20.0F;
        }

        this.setHeight((int) var9);
        if (!this.field20877 && this.getHeight() != this.parent.getHeight()) {
            if (this.field20781.offset >= 0) {
                if (this.field20781.offset + this.parent.getParent().getHeight() > this.field20780.getButton().getHeight()) {
                    this.field20781.offset = this.field20780.getButton().getHeight() - this.parent.getParent().getHeight();
                }
            } else {
                this.field20781.offset = 0;
            }

            float var16 = var5 - var6;
            float var13 = (float) this.field20781.offset / var16;
            float var14 = (float) (this.parent.getHeight() - this.getHeight());
            float var15 = var14 * var13 + 0.5F;
            this.setY((int) var15);
        } else if (this.method13216()) {
            float var12 = (float) this.getY() / (float) this.parent.getHeight();
            this.field20781.offset = (int) (var12 * (float) this.field20780.getButton().getHeight());
            if (this.field20781.offset >= 0) {
                if (this.field20781.offset + this.parent.getParent().getHeight() > this.field20780.getButton().getHeight()) {
                    this.field20781.offset = this.field20780.getButton().getHeight() - this.parent.getParent().getHeight();
                }
            } else {
                this.field20781.offset = 0;
            }

            this.field20781.field20797.reset();
            this.field20781.field20797.start();
        }
    }

    @Override
    public void draw(float partialTicks) {
        int var4 = 5;
        partialTicks *= !this.field20877 ? (!this.field20908 ? 0.3F : 0.7F) : 0.75F;
        int var5 = this.x;
        int var6 = this.width;
        if (Client.getInstance().clientMode != ClientMode.JELLO) {
            var4 = 0;
            var6 -= 7;
            var5 += 7;
            RenderUtil.drawRoundedRect(
                    (float) var5,
                    (float) (this.y + var4),
                    (float) (var5 + var6),
                    (float) (this.y + this.getHeight() - var4),
                    RenderUtil2.applyAlpha(ClientColors.MID_GREY.getColor(), partialTicks)
            );
        } else {
            RenderUtil.drawImage((float) var5, (float) this.y, (float) var6, (float) var4, Resources.verticalScrollBarTopPNG, partialTicks);
            RenderUtil.drawImage((float) var5, (float) (this.y + this.height - var4), (float) var6, (float) var4, Resources.verticalScrollBarBottomPNG, partialTicks);
            RenderUtil.drawRoundedRect(
                    (float) var5,
                    (float) (this.y + var4),
                    (float) (var5 + var6),
                    (float) (this.y + this.getHeight() - var4),
                    RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.45F * partialTicks)
            );
        }

        super.draw(partialTicks);
    }
}
