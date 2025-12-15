package com.mentalfrostbyte.jello.gui.combined;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mentalfrostbyte.Client;
import com.mentalfrostbyte.jello.gui.base.interfaces.Class7261;
import com.mentalfrostbyte.jello.gui.base.interfaces.IGuiEventListener;
import com.mentalfrostbyte.jello.gui.base.interfaces.IWidthSetter;
import com.mentalfrostbyte.jello.gui.impl.jello.buttons.ScrollableContentPanel;
import com.mentalfrostbyte.jello.gui.impl.jello.buttons.TextField;
import com.mentalfrostbyte.jello.util.client.render.theme.ColorHelper;
import com.mentalfrostbyte.jello.util.client.render.ResourceRegistry;
import com.mentalfrostbyte.jello.util.system.other.GsonUtil;
import org.newdawn.slick.TrueTypeFont;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CustomGuiScreen implements IGuiEventListener {
    private final List<CustomGuiScreen> children = new ArrayList<>();
    private final List<IWidthSetter> field20894 = new ArrayList<>();
    private final List<CustomGuiScreen> field20916 = new ArrayList<>();
    private final List<CustomGuiScreen> field20918 = new ArrayList<>();
    private final List<Class7914> field20920 = new ArrayList<>();
    private final List<MouseListener> mouseButtonListeners = new ArrayList<>();
    private final List<IRunnable> runnables = new ArrayList<>();
    private final List<KeyPressedListener> keyPressedListeners = new ArrayList<>();
    private final List<CharTypedListener> charTypedListeners = new ArrayList<>();
    public String name;
    public CustomGuiScreen parent;
    public int x;
    public int y;
    public int width;
    public int height;
    public float field20899 = 1.0F;
    public float field20900 = 1.0F;
    public int field20901 = 0;
    public int field20902 = 0;
    public boolean visible;
    public boolean hovered;
    public boolean focused;
    public boolean reAddChildren;
    public boolean field20907;
    public boolean field20908;
    public boolean field20909;
    public boolean listening;
    public boolean saveSize;
    public String text;
    public TrueTypeFont font;
    public ColorHelper textColor;
    private final ArrayList<Runnable> runOnDimensionUpdate = new ArrayList<Runnable>();
    private boolean field20917;
    private CustomGuiScreen field20919;
    private int mouseX;
    private int mouseY;

    public CustomGuiScreen(CustomGuiScreen parent, String name) {
        this(parent, name, 0, 0, 0, 0);
    }

    public CustomGuiScreen(CustomGuiScreen parent, String name, int x, int y, int width, int height) {
        this(parent, name, x, y, width, height, ColorHelper.field27961);
    }

    public CustomGuiScreen(CustomGuiScreen parent, String name, int x, int y, int width, int height, ColorHelper textColor) {
        this(parent, name, x, y, width, height, textColor, null);
    }

    public CustomGuiScreen(CustomGuiScreen parent, String name, int x, int y, int width, int height, ColorHelper textColor, String text) {
        this(parent, name, x, y, width, height, textColor, text, ResourceRegistry.JelloLightFont25);
    }

    /**
     * Constructs a new CustomGuiScreen with specified parameters.
     *
     * @param parent    The parent CustomGuiScreen object.
     * @param name      The name of this CustomGuiScreen.
     * @param x        The x-coordinate of the top-left corner.
     * @param y        The y-coordinate of the top-left corner.
     * @param width    The width of the screen.
     * @param height   The height of the screen.
     * @param textColor The color of the text.
     * @param text      The initial typed text (can be null).
     * @param font      The TrueTypeFont to be used for rendering text.
     */
    public CustomGuiScreen(CustomGuiScreen parent, String name, int x, int y, int width, int height, ColorHelper textColor, String text, TrueTypeFont font) {
        this.name = name;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.textColor = textColor;
        this.font = font;
        this.visible = true;
        this.hovered = true;
        this.listening = true;
        this.saveSize = false;
    }

    private void method13220() {
        for (CustomGuiScreen screen : new ArrayList<CustomGuiScreen>(this.children)) {
            if (screen.shouldReAddChildren()) {
                this.children.remove(screen);
                this.children.add(screen);
            }

            if (screen.method13293()) {
                this.children.remove(screen);
                this.children.add(0, screen);
            }
        }
    }

    public CustomGuiScreen getChildByName(String var1) {
        for (CustomGuiScreen var5 : this.children) {
            if (var5.getName().equals(var1)) {
                return var5;
            }
        }

        return null;
    }

    public void addRunnable(Runnable runnable) {
        synchronized (this) {
            if (runnable != null) {
                this.runOnDimensionUpdate.add(runnable);
            }
        }
    }

    /**
     * Manages the arrangement and removal of CustomGuiScreen objects within various lists.
     * This method performs the following operations:
     * 1. Removes specified screens from iconPanelList and clears field20919 if necessary.
     * 2. Clears and repopulates iconPanelList with elements from field20916.
     * 3. Ensures field20919, if not null, is at the end of iconPanelList.
     * 4. Calls method13220() to further arrange the iconPanelList.
     * <p>
     * This method does not take any parameters and does not return a value.
     * It operates on the class's internal lists and fields.
     */
    private void method13223() {
        for (CustomGuiScreen var4 : this.field20918) {
            this.children.remove(var4);
            if (this.field20919 == var4) {
                this.field20919 = null;
            }
        }

        this.field20916.clear();

        this.children.addAll(this.field20916);

        this.field20916.clear();
        if (this.field20919 != null) {
            this.children.remove(this.field20919);
            this.children.add(this.field20919);
        }

        this.method13220();
    }

    public void updatePanelDimensions(int mouseX, int mouseY) {
        this.mouseY = mouseY;
        this.mouseX = mouseX;
        this.field20908 = this.isVisible() && this.method13229(mouseX, mouseY);

        try {
            for (Runnable runnable : this.runOnDimensionUpdate) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        } catch (ConcurrentModificationException e) {
            Client.getInstance().LOGGER.info("kys concurrent modification exception go away");
        }

        this.runOnDimensionUpdate.clear();
        this.field20917 = true;

        try {
            for (CustomGuiScreen iconPanel : this.children) {
                iconPanel.updatePanelDimensions(mouseX, mouseY);
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }

        this.field20909 = this.field20909 & this.field20908;

        for (IWidthSetter var11 : this.method13260()) {
            if (this.visible) {
                var11.setWidth(this, this.getParent());
            }
        }

        this.method13223();
        this.field20917 = false;
    }

    public void method13224() {
        GL11.glTranslatef((float) (this.getX() + this.getWidth() / 2), (float) (this.getY() + this.getHeight() / 2), 0.0F);
        GL11.glScalef(this.method13273(), this.method13275(), 0.0F);
        GL11.glTranslatef((float) (-this.getX() - this.getWidth() / 2), (float) (-this.getY() - this.getHeight() / 2), 0.0F);
    }

    public void method13225() {
        GL11.glTranslatef((float) this.method13280(), (float) this.method13282(), 0.0F);
    }

    public void draw(float partialTicks) {
        this.drawChildren(partialTicks);
    }

    public final void drawChildren(float partialTicks) {
        GlStateManager.enableAlphaTest();
        GL11.glAlphaFunc(519, 0.0F);
        GL11.glTranslatef((float) this.getX(), (float) this.getY(), 0.0F);

        for (CustomGuiScreen child : this.children) {
            if (child.isSelfVisible()) {
                GL11.glPushMatrix();
                child.draw(partialTicks);
                GL11.glPopMatrix();
            }
        }
    }

    public boolean method13227() {
        for (CustomGuiScreen var4 : this.getChildren()) {
            if (var4 instanceof TextField && var4.focused) {
                return true;
            }

            if (var4.method13227()) {
                return true;
            }
        }

        return false;
    }

    public void modifierPressed(int var1) {
        for (CustomGuiScreen var5 : this.children) {
            if (var5.isHovered() && var5.isSelfVisible()) {
                var5.modifierPressed(var1);
            }
        }
    }

    @Override
    public void charTyped(char typed) {
        for (CustomGuiScreen var5 : this.children) {
            if (var5.isHovered() && var5.isSelfVisible()) {
                var5.charTyped(typed);
            }
        }

        this.callCharTypedListeners(typed);
    }

    @Override
    public void keyPressed(int keyCode) {
        for (CustomGuiScreen child : this.children) {
            if (child.isHovered() && child.isSelfVisible()) {
                child.keyPressed(keyCode);
            }
        }

        this.callKeyPressedListeners(keyCode);
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseButton) {
        boolean var6 = false;

        for (int var7 = this.children.size() - 1; var7 >= 0; var7--) {
            CustomGuiScreen var8 = this.children.get(var7);
            boolean var9 = var8.getParent() != null
                    && var8.getParent() instanceof ScrollableContentPanel
                    && var8.getParent().method13114(mouseX, mouseY)
                    && var8.getParent().isSelfVisible()
                    && var8.getParent().isHovered();
            if (var6 || !var8.isHovered() || !var8.isSelfVisible() || !var8.method13114(mouseX, mouseY) && !var9) {
                var8.setFocused(false);
                if (var8 != null) {
                    for (CustomGuiScreen var12 : var8.getChildren()) {
                        var12.setFocused(false);
                    }
                }
            } else {
                var8.onClick(mouseX, mouseY, mouseButton);
                var6 = !var9;
            }
        }

        if (!var6) {
            this.field20909 = this.field20908 = true;
            this.method13242();
            this.method13248(mouseButton);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick2(int mouseX, int mouseY, int mouseButton) {
        this.field20908 = this.method13114(mouseX, mouseY);

        for (CustomGuiScreen var7 : this.children) {
            if (var7.isHovered() && var7.isSelfVisible()) {
                var7.onClick2(mouseX, mouseY, mouseButton);
            }
        }

        this.onMouseButtonUsed(mouseButton);
        if (this.method13212() && this.method13298()) {
            this.onClick3(mouseX, mouseY, mouseButton);
        }

        this.field20909 = false;
    }

    @Override
    public void onClick3(int mouseX, int mouseY, int mouseButton) {
        this.onClick(mouseButton);
    }

    @Override
    public void onScroll(float scroll) {
        for (CustomGuiScreen var5 : this.children) {
            if (var5.isHovered() && var5.isSelfVisible()) {
                var5.onScroll(scroll);
            }
        }
    }

    public boolean method13114(int mouseX, int mouseY) {
        mouseX -= this.method13271();
        mouseY -= this.method13272();
        return mouseX >= 0 && mouseX <= this.width && mouseY >= 0 && mouseY <= this.height;
    }

    public boolean method13228(int mouseX, int mouseY, boolean var3) {
        boolean var6 = this.method13114(mouseX, mouseY);
        if (var6 && this.parent != null) {
            if (var3) {
                for (CustomGuiScreen var8 : this.getChildren()) {
                    if (var8.isSelfVisible() && var8.method13114(mouseX, mouseY)) {
                        return false;
                    }
                }
            }

            CustomGuiScreen var11 = this;

            for (CustomGuiScreen var12 = this.getParent(); var12 != null; var12 = var12.getParent()) {
                for (int var9 = var12.findChild(var11) + 1; var9 < var12.getChildren().size(); var9++) {
                    CustomGuiScreen var10 = var12.getChildren().get(var9);
                    if (var10 != var11 && var10.isSelfVisible() && var10.method13114(mouseX, mouseY)) {
                        return false;
                    }
                }

                var11 = var12;
            }
        }

        return var6;
    }

    public boolean method13229(int mouseX, int mouseY) {
        return this.method13228(mouseX, mouseY, true);
    }

    public void addToList(CustomGuiScreen var1) {
        if (var1 != null) {
            for (CustomGuiScreen var5 : this.getChildren()) {
                if (var5.getName().equals(var1.getName())) {
                    System.out.println("Children with duplicate IDs! Child with id \"" + var5.getName() + "\" already exists in view \"" + this.getName() + "\"!");
                    return;
                }
            }

            var1.setParent(this);
            if (this.field20917) {
                this.field20916.add(var1);
            } else {
                try {
                    this.children.add(var1);
                } catch (ConcurrentModificationException var6) {
                    this.field20916.add(var1);
                }
            }
        }
    }

    public boolean isntQueue(String var1) {
        for (CustomGuiScreen var5 : this.getChildren()) {
            if (var5.getName().equals(var1)) {
                return true;
            }
        }

        return false;
    }

    public void method13232(CustomGuiScreen var1) {
        if (var1 != null) {
            for (CustomGuiScreen var5 : this.getChildren()) {
                if (var5.getName().equals(var1.getName())) {
                    throw new RuntimeException("Children with duplicate IDs!");
                }
            }

            var1.setParent(this);
            this.field20916.add(var1);
        }
    }

    public void showAlert(CustomGuiScreen var1) {
        if (var1 != null) {
            for (CustomGuiScreen var5 : this.getChildren()) {
                if (var5.getName().equals(var1.getName())) {
                    throw new RuntimeException("Children with duplicate IDs!");
                }
            }

            var1.setParent(this);

            try {
                this.children.add(var1);
            } catch (ConcurrentModificationException var6) {
            }
        }
    }

    public void method13234(CustomGuiScreen var1) {
        if (this.field20917) {
            this.field20918.add(var1);
        } else {
            this.removeChildren(var1);
        }
    }

    public void removeChildren(CustomGuiScreen guiIn) {
        this.children.remove(guiIn);
        if (this.field20919 != null && this.field20919.equals(guiIn)) {
            this.field20919 = null;
        }

        this.field20916.remove(guiIn);
    }

    public void method13237(CustomGuiScreen var1) {
        for (CustomGuiScreen var5 : this.getChildren()) {
            if (var5.name.equals(var1.name)) {
                this.method13234(var5);
            }
        }
    }

    public void clearChildren() {
        this.children.clear();
    }

    public boolean hasChild(CustomGuiScreen child) {
        return this.children.contains(child);
    }

    public int findChild(CustomGuiScreen child) {
        return this.children.indexOf(child);
    }

    public List<CustomGuiScreen> getChildren() {
        return this.children;
    }

    public void method13242() {
        this.setFocused(true);
        if (this.parent != null) {
            this.parent.field20919 = this;
            this.parent.method13242();
        }
    }

    public void method13243() {
        for (CustomGuiScreen var4 : this.parent.getChildren()) {
            if (var4 == this) {
                return;
            }

            var4.method13242();
        }
    }

    public JsonObject toConfigWithExtra(JsonObject config) {
        if (this.isListening()) {
            config.addProperty("id", this.getName());
            config.addProperty("x", this.getX());
            config.addProperty("y", this.getY());
            if (this.shouldSaveSize()) {
                config.addProperty("width", this.getWidth());
                config.addProperty("height", this.getHeight());
            }

            config.addProperty("index", this.parent == null ? 0 : this.parent.findChild(this));
            return this.toConfig(config);
        } else {
            return config;
        }
    }

    public final JsonObject toConfig(JsonObject base) {
        JsonArray children = new JsonArray();

        for (CustomGuiScreen child : this.children) {
            if (child.isListening()) {
                JsonObject var7 = child.toConfigWithExtra(new JsonObject());
                if (var7.size() > 0) {
                    children.add(var7);
                }
            }
        }

        base.add("children", children);
        return base;
    }

    public void loadConfig(JsonObject config) {
        if (this.isListening()) {
            this.x = GsonUtil.getIntOrDefault(config, "x", this.x);
            this.y = GsonUtil.getIntOrDefault(config, "y", this.y);
            if (this.shouldSaveSize()) {
                this.width = GsonUtil.getIntOrDefault(config, "width", this.width);
                this.height = GsonUtil.getIntOrDefault(config, "height", this.height);
            }

            JsonArray children = GsonUtil.getJSONArrayOrNull(config, "children");
            if (children != null) {
                List<CustomGuiScreen> childrenArray = new ArrayList<>(this.children);

                for (int i = 0; i < children.size(); i++) {
                    JsonObject childJson = null;
                    try {
                        childJson = children.get(i).getAsJsonObject();
                    } catch (JsonParseException e) {
                        throw new RuntimeException(e);
                    }
                    String id = GsonUtil.getStringOrDefault(childJson, "id", null);
                    int index = GsonUtil.getIntOrDefault(childJson, "index", -1);

                    for (CustomGuiScreen child : childrenArray) {
                        if (child.getName().equals(id)) {
                            child.loadConfig(childJson);
                            if (index >= 0) {
                                this.children.remove(child);
                                if (index > this.children.size()) {
                                    this.children.add(child);
                                } else {
                                    this.children.add(index, child);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this != other) {
            if (!(other instanceof CustomGuiScreen to)) {
                return false;
            } else {
                return this.name.equals(to.name) && (this.getParent() == null || this.getParent().equals(to.getParent()));
            }
        } else {
            return true;
        }
    }

    public void method13246(Class7261 var1) {
        var1.method22796(this);
    }

    public final CustomGuiScreen method13247(Class7914 var1) {
        this.field20920.add(var1);
        return this;
    }

    public void method13248(int var1) {
        for (Class7914 var5 : this.field20920) {
            var5.method26544(this, var1);
        }
    }

    public CustomGuiScreen method13249(MouseListener var1) {
        this.mouseButtonListeners.add(var1);
        return this;
    }

    public void onMouseButtonUsed(int mouseButton) {
        for (MouseListener mouse : this.mouseButtonListeners) {
            mouse.mouseButtonUsed(this, mouseButton);
        }
    }

    public CustomGuiScreen onClick(IRunnable runnable) {
        this.runnables.add(runnable);
        return this;
    }

    public void onClick(int mouseButton) {
        for (IRunnable IRunnable : this.runnables) {
            IRunnable.run(this, mouseButton);
        }
    }

    public final void addKeyPressListener(KeyPressedListener listener) {
        this.keyPressedListeners.add(listener);
    }

    public void callKeyPressedListeners(int key) {
        for (KeyPressedListener listener : this.keyPressedListeners) {
            listener.keyPressed(this, key);
        }
    }

    public void callCharTypedListeners(char chr) {
        for (CharTypedListener listener : this.charTypedListeners) {
            listener.charTyped(chr);
        }
    }

    public String getName() {
        return this.name;
    }

    public CustomGuiScreen getParent() {
        return this.parent;
    }

    public void setParent(CustomGuiScreen var1) {
        this.parent = var1;
    }

    public List<IWidthSetter> method13260() {
        return this.field20894;
    }

    public void setSize(IWidthSetter var1) {
        this.field20894.add(var1);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int method13271() {
        return this.parent == null ? this.x : this.parent.method13271() + this.x;
    }

    public int method13272() {
        return this.parent == null ? this.y : this.parent.method13272() + this.y;
    }

    public float method13273() {
        return this.field20899;
    }

    public float method13275() {
        return this.field20900;
    }

    public void method13277(float var1) {
        this.field20899 = var1;
    }

    public void method13278(float var1) {
        this.field20900 = var1;
    }

    public void method13279(float var1, float var2) {
        this.field20899 = var1;
        this.field20900 = var2;
    }

    public int method13280() {
        return this.field20901;
    }

    public int method13282() {
        return this.field20902;
    }

    public void method13284(int var1) {
        this.field20901 = var1;
    }

    public void drawBackground(int var1) {
        this.field20902 = var1;
    }

    public void draw(int var1, int var2) {
        this.field20901 = var1;
        this.field20902 = var2;
    }

    /**
     * @return If this screen is visible.
     * doesn't account for the parent, but {@link CustomGuiScreen#isVisible()} does
     * @see CustomGuiScreen#isVisible()
     */
    public boolean isSelfVisible() {
        return this.visible;
    }

    public void setSelfVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return If this screen and its parent (if it has one) are visible.
     * @see CustomGuiScreen#isSelfVisible()
     */
    public boolean isVisible() {
        return this.parent == null ? this.visible : this.visible && this.parent.isVisible();
    }

    /**
     * used in {@link CustomGuiScreen#method13220} to re-add a child (if this returns true)
     */
    public boolean shouldReAddChildren() {
        return this.reAddChildren;
    }

    public void setReAddChildren(boolean reAddChildren) {
        this.reAddChildren = reAddChildren;
    }

    public boolean method13293() {
        return this.field20907;
    }

    public void method13294(boolean var1) {
        this.field20907 = var1;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean method13298() {
        return this.field20908;
    }

    public boolean method13212() {
        return this.field20909;
    }

    public boolean isListening() {
        return this.listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public boolean shouldSaveSize() {
        return this.saveSize;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TrueTypeFont getFont() {
        return this.font;
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
    }

    public ColorHelper getTextColor() {
        return this.textColor;
    }

    public void setTextColor(ColorHelper textColor) {
        this.textColor = textColor;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public interface IRunnable {
        void run(CustomGuiScreen parent, int mouseButton);
    }

    public interface CharTypedListener {
        void charTyped(char chr);
    }

    public interface MouseListener {
        void mouseButtonUsed(CustomGuiScreen screen, int mouseButton);
    }

    public interface KeyPressedListener {
        void keyPressed(CustomGuiScreen screen, int key);
    }

    public interface Class7914 {
        void method26544(CustomGuiScreen screen, int var2);
    }
}
