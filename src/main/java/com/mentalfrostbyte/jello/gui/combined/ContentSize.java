package com.mentalfrostbyte.jello.gui.combined;

import com.mentalfrostbyte.jello.gui.base.interfaces.IWidthSetter;

public class ContentSize implements IWidthSetter {

    @Override
    public void setWidth(CustomGuiScreen forScreen, CustomGuiScreen fromWidthOfThisScreen) {
        int var5 = 0;
        int var6 = 0;

        for (CustomGuiScreen var8 : forScreen.getChildren()) {
            if (var8.getX() + var8.getWidth() > var5) {
                var5 = var8.getX() + var8.getWidth();
            }

            if (var8.getY() + var8.getHeight() > var6) {
                var6 = var8.getY() + var8.getHeight();
            }
        }

        forScreen.setWidth(var5);
        forScreen.setHeight(var6);
    }
}
