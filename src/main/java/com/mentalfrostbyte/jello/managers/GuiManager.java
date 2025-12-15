package com.mentalfrostbyte.jello.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mentalfrostbyte.Client;
import com.mentalfrostbyte.jello.util.client.ClientMode;
import com.mentalfrostbyte.jello.event.impl.game.render.EventRender2DOffset;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.holders.*;
import com.mentalfrostbyte.jello.gui.base.elements.impl.critical.Screen;
import com.mentalfrostbyte.jello.gui.impl.classic.mainmenu.ClassicMainScreen;
import com.mentalfrostbyte.jello.gui.impl.classic.clickgui.ClassicClickGui;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.*;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.holders.CreditsHolder;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.clickgui.ClickGuiScreen;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.options.buttons.JelloOptionsButton;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.options.JelloOptions;
import com.mentalfrostbyte.jello.gui.impl.jello.ingame.options.CreditsScreen;
import com.mentalfrostbyte.jello.gui.impl.jello.mainmenu.MainMenuScreen;
import com.mentalfrostbyte.jello.gui.combined.holders.NoAddonHolder;
import com.mentalfrostbyte.jello.gui.impl.jello.viamcp.JelloPortalScreen;
import com.mentalfrostbyte.jello.gui.combined.impl.SwitchScreen;
import com.mentalfrostbyte.jello.module.impl.gui.classic.TabGUI;
import com.mentalfrostbyte.jello.util.client.render.theme.ClientColors;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil2;
import com.mentalfrostbyte.jello.util.game.render.RenderUtil;
import com.mentalfrostbyte.jello.util.client.render.Resources;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuHolder;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import team.sdhq.eventBus.EventBus;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;

public class GuiManager {
    public static final Map<Class<? extends net.minecraft.client.gui.screen.Screen>, String> screenToScreenName = new HashMap<>();
    private static final Map<Class<? extends net.minecraft.client.gui.screen.Screen>, Class<? extends Screen>> replacementScreens = new HashMap<>();


    static {
        replacementScreens.put(MainMenuHolder.class, MainMenuScreen.class);
        replacementScreens.put(ClickGuiHolder.class, ClickGuiScreen.class);
        replacementScreens.put(KeyboardHolder.class, KeyboardScreen.class);
        replacementScreens.put(MapsHolder.class, MapsScreen.class);
        replacementScreens.put(SnakeHolder.class, SnakeGameScreen.class);
        replacementScreens.put(BirdHolder.class, BirdGameScreen.class);
        replacementScreens.put(SpotlightHolder.class, SpotlightScreen.class);
        replacementScreens.put(JelloOptionsHolder.class, JelloOptions.class);
        replacementScreens.put(CreditsHolder.class, CreditsScreen.class);
        screenToScreenName.put(ClickGuiHolder.class, "Click GUI");
        screenToScreenName.put(KeyboardHolder.class, "Keybind Manager");
        screenToScreenName.put(MapsHolder.class, "Jello Maps");
        screenToScreenName.put(SnakeHolder.class, "Snake");
        screenToScreenName.put(BirdHolder.class, "Bird");
        screenToScreenName.put(SpotlightHolder.class, "Spotlight");
    }

    public int[] mousePositions = new int[2];
    public double mouseScroll;

    private final List<Integer> keysPressed = new ArrayList<>();
    private final List<Integer> modifiersPressed = new ArrayList<>();
    private final List<Integer> mouseButtonsPressed = new ArrayList<>();
    private final List<Integer> mouseButtonsReleased = new ArrayList<>();
    private final List<Integer> charsTyped = new ArrayList<>();

    private boolean guiBlur = true;
    private boolean hqIngameBlur = true;
    private static boolean hidpiCocoa = true;

    public static float scaleFactor = 1.0F;

    private Screen screen;

    public GuiManager() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
        scaleFactor = (float) (Minecraft.getInstance().getMainWindow().getFramebufferHeight() / Minecraft.getInstance().getMainWindow().getHeight());
    }

    public static boolean method33457(net.minecraft.client.gui.screen.Screen screen) {
        if (screen instanceof MultiplayerScreen && !(screen instanceof JelloPortalScreen)) {
            Minecraft.getInstance().currentScreen = null;
            Minecraft.getInstance().displayGuiScreen(new JelloPortalScreen(((MultiplayerScreen) screen).parentScreen));
            return true;
        } else if (screen instanceof IngameMenuScreen && !(screen instanceof JelloOptionsButton)) {
            Minecraft.getInstance().currentScreen = null;
            Minecraft.getInstance().displayGuiScreen(new JelloOptionsButton());
            return true;
        } else if (Client.getInstance().clientMode == ClientMode.NOADDONS && screen instanceof MainMenuHolder && !(screen instanceof NoAddonHolder)) {
            Minecraft.getInstance().currentScreen = null;
            Minecraft.getInstance().displayGuiScreen(new NoAddonHolder());
            return true;
        } else {
            return false;
        }
    }

    public Screen getScreen() {
        return this.screen;
    }

    public static Screen handleScreen(net.minecraft.client.gui.screen.Screen screen) {
        if (screen == null) {
            return null;
        } else if (Client.getInstance().clientMode == ClientMode.INDETERMINATE) {
            return new SwitchScreen();
        } else if (method33457(screen)) {
            return null;
        } else if (!replacementScreens.containsKey(screen.getClass())) {
            return null;
        } else {
            try {
                return replacementScreens.get(screen.getClass()).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Client.LOGGER.error("Error creating replacement screen", e);
            }

            return null;
        }
    }

    public void useClassic() {
        replacementScreens.clear();
        replacementScreens.put(MainMenuHolder.class, ClassicMainScreen.class);
        replacementScreens.put(ClickGuiHolder.class, ClassicClickGui.class);
    }

    public void useJello() {
        replacementScreens.clear();
        replacementScreens.put(MainMenuHolder.class, MainMenuScreen.class);
        replacementScreens.put(ClickGuiHolder.class, ClickGuiScreen.class);
        replacementScreens.put(KeyboardHolder.class, KeyboardScreen.class);
        replacementScreens.put(MapsHolder.class, MapsScreen.class);
        replacementScreens.put(SnakeHolder.class, SnakeGameScreen.class);
        replacementScreens.put(BirdHolder.class, BirdGameScreen.class);
        replacementScreens.put(SpotlightHolder.class, SpotlightScreen.class);
        replacementScreens.put(JelloOptionsHolder.class, JelloOptions.class);
        replacementScreens.put(CreditsHolder.class, CreditsScreen.class);
        screenToScreenName.put(ClickGuiHolder.class, "Click GUI");
        screenToScreenName.put(KeyboardHolder.class, "Keybind Manager");
        screenToScreenName.put(MapsHolder.class, "Jello Maps");
        screenToScreenName.put(SnakeHolder.class, "Snake");
        screenToScreenName.put(BirdHolder.class, "Bird");
        screenToScreenName.put(SpotlightHolder.class, "Spotlight");
    }

    /**
     * @see net.minecraft.client.KeyboardListener#onKeyEvent
     */
    public void handleKeyEvent(int key, int action) {
        if (action == 1 || action == 2) {
            this.keysPressed.add(key);
        } else if (action == 0) {
            this.modifiersPressed.add(key);
        }
    }

    public void addTypedChar(int codePoint) {
        this.charsTyped.add(codePoint);
    }

    public void onScroll(double yOffset) {
        this.mouseScroll += yOffset;
    }

    public void onMouseButtonCallback(int button, int action) {
        if (action != 1) {
            if (action == 0) {
                this.mouseButtonsReleased.add(button);
            }
        } else {
            this.mouseButtonsPressed.add(button);
        }
    }

    public void endTick() {
        if (this.screen != null) {
            this.mousePositions[0] = Math.max(0, Math.min(Minecraft.getInstance().getMainWindow().getWidth(), (int) Minecraft.getInstance().mouseHelper.getMouseX()));
            this.mousePositions[1] = Math.max(0, Math.min(Minecraft.getInstance().getMainWindow().getHeight(), (int) Minecraft.getInstance().mouseHelper.getMouseY()));

            for (int key : this.keysPressed) {
                this.screen.keyPressed(key);
            }

            for (int modifier : this.modifiersPressed) {
                this.screen.modifierPressed(modifier);
            }

            if (Minecraft.getInstance().loadingGui == null) {
                for (int mouseButton : this.mouseButtonsPressed) {
                    this.screen.onClick(this.mousePositions[0], this.mousePositions[1], mouseButton);
                }

                for (int mouseButton : this.mouseButtonsReleased) {
                    this.screen.onClick2(this.mousePositions[0], this.mousePositions[1], mouseButton);
                }
            }

            for (int chr : this.charsTyped) {
                this.screen.charTyped((char) chr);
            }

            this.keysPressed.clear();
            this.modifiersPressed.clear();
            this.mouseButtonsPressed.clear();
            this.mouseButtonsReleased.clear();
            this.charsTyped.clear();

            if (this.mouseScroll != 0.0) {
                if (Minecraft.getInstance().loadingGui == null) {
                    this.screen.onScroll((float) this.mouseScroll);
                }
                this.mouseScroll = 0.0;
            }

            if (this.screen != null) {
                this.screen.updatePanelDimensions(this.mousePositions[0], this.mousePositions[1]);
            }
        }
    }

    public void renderWatermark() {
        if (Minecraft.getInstance().world != null) {
            GL11.glDisable(GL11.GL_LIGHTING);
            int x = 0;
            int y = 0;
            int width = 170;

            if (Minecraft.getInstance().gameSettings.showDebugInfo) {
                x = Minecraft.getInstance().getMainWindow().getWidth() / 2 - width / 2;
            }

            if (Client.getInstance().clientMode != ClientMode.JELLO) {
                float var7 = 0.5F + TabGUI.animationProgress.calcPercent() * 0.5F;
                GL11.glAlphaFunc(516, 0.1F);
                RenderUtil.drawRoundedRect2(4.0F, 2.0F, 106.0F, 28.0F, RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.6F * var7));
                RenderUtil.drawString(Resources.bold22, 9.0F, 2.0F, "Sigma", RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.5F * var7));
                RenderUtil.drawString(
                        Resources.bold22, 8.0F, 1.0F, "Sigma", RenderUtil2.applyAlpha(ClientColors.LIGHT_GREYISH_BLUE.getColor(), Math.min(1.0F, var7 * 1.2F))
                );
                int var8 = Color.getHSBColor((float) (System.currentTimeMillis() % 4000L) / 4000.0F, 1.0F, 1.0F).getRGB();
                RenderUtil.drawString(Resources.bold14, 73.0F, 2.0F, "5.1.0", RenderUtil2.applyAlpha(ClientColors.DEEP_TEAL.getColor(), 0.5F));
                RenderUtil.drawString(Resources.bold14, 72.0F, 1.0F, "5.1.0", RenderUtil2.applyAlpha(var8, Math.min(1.0F, var7 * 1.4F)));
            } else {
                if (!(scaleFactor > 1.0F)) {
                    Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("com/mentalfrostbyte/gui/resources/sigma/jello_watermark.png"));
                } else {
                    Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("com/mentalfrostbyte/gui/resources/sigma/jello_watermark@2x.png"));
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                AbstractGui.blit(new MatrixStack(), x, y, 0, 0, (int) 170.0F, (int) 104.0F, (int) 170.0F, (int) 104.0F);

                // Reset states
                RenderSystem.disableBlend();
            }

            EventBus.call(new EventRender2DOffset());
        }

        if (this.screen != null && Minecraft.getInstance().loadingGui == null) {
            this.screen.draw(1.0F);
        }
    }

    public void saveConfig(JsonObject uiConfig) {
        if (this.screen != null) {
            JsonObject json = this.screen.toConfigWithExtra(new JsonObject());
            if (json.size() != 0) {
                uiConfig.add(this.screen.getName(), json);
            }
        }

        uiConfig.addProperty("guiBlur", this.guiBlur);
        uiConfig.addProperty("hqIngameBlur", this.hqIngameBlur);
        uiConfig.addProperty("hidpicocoa", hidpiCocoa);
    }

    public void setGuiBlur(boolean guiBlur) {
        this.guiBlur = guiBlur;
    }

    public boolean getGuiBlur() {
        return this.guiBlur;
    }

    public void setHqIngameBlur(boolean hqIngameBlur) {
        this.hqIngameBlur = hqIngameBlur;
    }

    public boolean getHqIngameBlur() {
        return this.hqIngameBlur;
    }

    public void setHidpiCocoa(boolean hidpiCocoa) {
        GuiManager.hidpiCocoa = hidpiCocoa;
    }

    public boolean getHidpiCocoa() {
        return hidpiCocoa;
    }

    public void loadUIConfig(JsonObject uiConfig) {
        if (this.screen != null) {
            JsonObject var4 = null;

            try {
                var4 = Client.getInstance().config.getAsJsonObject(this.screen.getName());
            } catch (Exception var9) {
                var4 = new JsonObject();
            } finally {
                this.screen.loadConfig(var4);
            }
        }

        if (uiConfig.has("guiBlur")) {
            this.guiBlur = uiConfig.get("guiBlur").getAsBoolean();
        }

        if (uiConfig.has("hqIngameBlur")) {
            this.hqIngameBlur = uiConfig.get("hqIngameBlur").getAsBoolean();
        }
    }

    public Class<? extends net.minecraft.client.gui.screen.Screen> method33477(String var1) {
        for (Entry var5 : screenToScreenName.entrySet()) {
            if (var1.equals(var5.getValue())) {
                return (Class<? extends net.minecraft.client.gui.screen.Screen>) var5.getKey();
            }
        }

        return null;
    }

    public String getNameForTarget(Class<? extends net.minecraft.client.gui.screen.Screen> screen) {
        if (screen == null) {
            return "";
        } else {
            for (Entry var5 : screenToScreenName.entrySet()) {
                if (screen == var5.getKey()) {
                    return (String) var5.getValue();
                }
            }

            return "";
        }
    }

    public void onResize() throws JsonParseException {
        if (this.screen != null) {
            this.saveConfig(Client.getInstance().config);

            try {
                this.screen = this.screen.getClass().newInstance();
            } catch (IllegalAccessException | InstantiationException exc) {
                Client.LOGGER.warn(exc);
            }

            this.loadUIConfig(Client.getInstance().config);
        }

        if (Minecraft.getInstance().getMainWindow().getWidth() != 0 && Minecraft.getInstance().getMainWindow().getHeight() != 0) {
            scaleFactor = (float) Math.max(
                    Minecraft.getInstance().getMainWindow().getFramebufferWidth() / Minecraft.getInstance().getMainWindow().getWidth(),
                    Minecraft.getInstance().getMainWindow().getFramebufferHeight() / Minecraft.getInstance().getMainWindow().getHeight()
            );
        }
    }

    public Screen getCurrentScreen() {
        return this.screen;
    }

    public void handleCurrentScreen() throws JsonParseException {
        this.handleScreen(handleScreen(Minecraft.getInstance().currentScreen));
    }

    public void handleScreen(Screen screen) {
        if (this.screen != null) {
            this.saveConfig(Client.getInstance().config);
        }

        this.screen = screen;
        this.loadUIConfig(Client.getInstance().config);
        if (this.screen != null) {
            this.screen.updatePanelDimensions(this.mousePositions[0], this.mousePositions[1]);
        }
    }

    public boolean hasReplacement(net.minecraft.client.gui.screen.Screen screen) {
        return replacementScreens.containsKey(screen.getClass());
    }
}
