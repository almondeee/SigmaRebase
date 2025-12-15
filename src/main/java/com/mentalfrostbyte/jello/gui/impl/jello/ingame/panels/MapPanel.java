package com.mentalfrostbyte.jello.gui.impl.jello.ingame.panels;

import com.mentalfrostbyte.Client;
import com.mentalfrostbyte.jello.gui.base.elements.Element;
import com.mentalfrostbyte.jello.gui.base.elements.impl.maps.MapFrame;
import com.mentalfrostbyte.jello.gui.base.elements.impl.button.Button;
import com.mentalfrostbyte.jello.gui.base.interfaces.Class9514;
import com.mentalfrostbyte.jello.gui.combined.CustomGuiScreen;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.options.Waypoint2;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.options.WaypointList;
import com.mentalfrostbyte.jello.util.system.math.vector.Vector3m;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.client.render.ResourceRegistry;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import net.minecraft.util.math.vector.Vector3i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class MapPanel extends Element {
   private final List<Button> field20612 = new ArrayList<Button>();
   public int field20613;
   public MapFrame field20614;
   public WaypointList waypointList;
   public int field20616;
   private final List<Class9514> field20617 = new ArrayList<Class9514>();

   public MapPanel(CustomGuiScreen var1, String var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6, false);
      this.field20616 = 260;
      this.addToList(this.waypointList = new WaypointList(this, "waypointList", 0, 65, this.field20616, this.height - 65));

      for (Waypoint2 var10 : Client.getInstance().waypointsManager.getWaypoints()) {
         this.waypointList.addWaypoint(var10.name, new Vector3i(var10.x, 64, var10.z), var10.color);
      }

      this.addToList(this.field20614 = new MapFrame(this, "mapFrame", this.field20616, 0, this.width - this.field20616, this.height));
      this.setListening(false);
   }

   @Override
   public void updatePanelDimensions(int mouseX, int mouseY) {
      super.updatePanelDimensions(mouseX, mouseY);
      if (this.field20909) {
         Client.getInstance().waypointsManager.field36375.clear();
      }
   }

   @Override
   public void draw(float partialTicks) {
      int var4 = 14;
      RenderUtil.drawRoundedRect(
         (float)(this.x + var4 / 2),
         (float)(this.y + var4 / 2),
         (float)(this.width - var4),
         (float)(this.height - var4),
         20.0F,
         partialTicks * 0.9F
      );
      float var5 = 0.88F;
      if (!Client.getInstance().guiManager.getGuiBlur()) {
         var5 = 0.95F;
      }

      RenderUtil.drawRoundedRect(
         (float)this.x,
         (float)this.y,
         (float)this.width,
         (float)this.height,
         14.0F,
         RenderUtil2.applyAlpha(ClientColors.LIGHT_GREYISH_BLUE.getColor(), var5)
      );
      RenderUtil.drawRoundedButton(
         (float)(this.x + this.field20616),
         (float)this.y,
         (float)(this.width - this.field20616),
         (float)this.height,
         14.0F,
         -7687425
      );
      RenderUtil.initStencilBuffer();
      RenderUtil.drawRoundedButton(
         (float)this.x, (float)this.y, (float)this.width, (float)this.height, 14.0F, ClientColors.LIGHT_GREYISH_BLUE.getColor()
      );
      RenderUtil.configureStencilTest();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);
      this.waypointList.draw(partialTicks);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);
      this.field20614.draw(partialTicks);
      GL11.glPopMatrix();
      RenderUtil.restorePreviousStencilBuffer();
      RenderUtil.drawRoundedRect2(
         (float)(this.x + this.field20616),
         (float)(this.y),
         1.0F,
         (float)this.height,
              RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.14F)
      );
      int var6 = RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.6F);
      RenderUtil.drawString(ResourceRegistry.JelloLightFont25, (float)(this.x + 30), (float)(this.y + 25), "Waypoints", var6);
      RenderUtil.drawString(
         ResourceRegistry.JelloMediumFont40,
         (float)this.x,
         (float)((this.parent.getHeight() - this.height) / 2 - 70),
         "Jello Maps",
         ClientColors.LIGHT_GREYISH_BLUE.getColor()
      );
      String var7 = Client.getInstance().waypointsManager.method29998().replace("/", " - ");
      RenderUtil.drawString(
         ResourceRegistry.JelloLightFont24,
         (float)(this.x + this.width - ResourceRegistry.JelloLightFont24.getWidth(var7) - 10),
         (float)((this.parent.getHeight() - this.height) / 2 - 62),
         var7,
              RenderUtil2.applyAlpha(ClientColors.LIGHT_GREYISH_BLUE.getColor(), 0.5F)
      );
   }

   public final void method13043(Class9514 var1) {
      this.field20617.add(var1);
   }

   public final void method13044(int var1, int var2, Vector3m var3) {
      for (Class9514 var7 : this.field20617) {
         var7.method36764(this, var1, var2, var3);
      }
   }
}
