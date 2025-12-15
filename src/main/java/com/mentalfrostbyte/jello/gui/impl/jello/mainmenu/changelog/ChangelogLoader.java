package com.mentalfrostbyte.jello.gui.impl.jello.mainmenu.changelog;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mentalfrostbyte.jello.gui.impl.jello.mainmenu.ChangelogScreen;
import com.mentalfrostbyte.jello.gui.base.elements.impl.Change;
import net.minecraft.util.Util;

public record ChangelogLoader(ChangelogScreen changelogScreen, JsonArray changelogJson) implements Runnable {

    @Override
    public void run() {
        int var3 = 75;

        try {
            for (int var4 = 0; var4 < this.changelogJson.size(); var4++) {
                JsonObject var5 = this.changelogJson.get(var4).getAsJsonObject();
                Change var6;
                if (var5.has("url")) {
                    Util.getOSType().openLink(var5.get("url").getAsString());
                }

                this.changelogScreen.scrollPanel.getButton().showAlert(var6 = new Change(this.changelogScreen.scrollPanel, "changelog" + var4, var5));
                var6.setY(var3);
                var3 += var6.getHeight();
            }
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }
}
