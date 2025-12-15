package com.mentalfrostbyte.jello.gui.base.elements.impl.button.types;

import com.mentalfrostbyte.jello.gui.base.elements.impl.Bezier;
import com.mentalfrostbyte.jello.gui.combined.AnimatedIconPanel;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;

public class BezierButton extends AnimatedIconPanel {
   public Bezier field20737;

   public BezierButton(Bezier var1, int var2, String var3) {
      super(var1, "bezierButton-" + var3, 0, 0, var2, var2, true);
      this.method13215(true);
      this.field20886 = true;
      this.field20737 = var1;
   }

   @Override
   public void updatePanelDimensions(int mouseX, int mouseY) {
      super.updatePanelDimensions(mouseX, mouseY);
      int var5 = this.field20737.getWidth() - this.field20737.field20610;
      int var6 = this.field20737.getHeight() - this.getHeight();
      int var7 = this.field20737.field20610;
      if (this.getX() > var5) {
         this.setX(var5);
      }

      if (this.getY() > var6) {
         this.setY(var6);
      }

      if (this.getX() < var7) {
         this.setX(var7);
      }
   }

   public void method13144(float var1, float var2) {
      this.x = (int)var1;
      this.y = (int)var2;
   }

   @Override
   public void draw(float partialTicks) {
      RenderUtil.drawCircle(
         (float)(this.x + 5),
         (float)(this.y + 5),
         10.0F,
         RenderUtil2.applyAlpha(!this.method13216() ? ClientColors.DARK_GREEN.getColor() : ClientColors.DARK_BLUE_GREY.getColor(), partialTicks)
      );
      super.draw(partialTicks);
   }
}
