package com.mentalfrostbyte.jello.gui.impl.jello.ingame;

import com.mentalfrostbyte.jello.gui.base.elements.impl.critical.Screen;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.buttons.SpotlightDialog;
import net.minecraft.client.Minecraft;

public class SpotlightScreen extends Screen {
    public SpotlightDialog dialog;

    public SpotlightScreen() {
        super("Spotlight");
        this.setListening(false);
        int x = (this.getWidth() - 675) / 2;
        this.addToList(this.dialog = new SpotlightDialog(this, "search", x, (int) ((float) this.height * 0.25F), 675, 60, true));
    }

    @Override
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        if (keyCode == 256) {
            Minecraft.getInstance().displayGuiScreen(null);
        }
    }
}
