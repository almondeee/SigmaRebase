package com.mentalfrostbyte.jello.module.settings.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mentalfrostbyte.jello.module.settings.Setting;
import com.mentalfrostbyte.jello.module.settings.SettingType;
import com.mentalfrostbyte.jello.util.system.other.GsonUtil;

import java.util.Arrays;
import java.util.List;

public class SubOptionSetting extends Setting<Boolean> {
    private final List<Setting<?>> subSettings;

    public SubOptionSetting(String name, String description, boolean defaultValue, List<Setting<?>> subSettings) {
        super(name, description, SettingType.SUBOPTION, defaultValue);
        this.subSettings = subSettings;
    }

    public SubOptionSetting(String name, String description, boolean defaultValue, Setting<?>... subSettings) {
        this(name, description, defaultValue, Arrays.asList(subSettings));
    }

    @Override
    public JsonObject loadCurrentValueFromJSONObject(JsonObject jsonObject) throws JsonParseException {
        JsonArray array = GsonUtil.getJSONArrayOrNull(jsonObject, this.getName());
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                JsonObject settingObject = array.get(i).getAsJsonObject();
                String settingName = GsonUtil.getStringOrDefault(settingObject, "name", null);

                for (Setting<?> setting : this.subSettings) {
                    if (setting.getName().equals(settingName)) {
                        setting.loadCurrentValueFromJSONObject(settingObject);
                        break;
                    }
                }
            }
        }

        this.currentValue = GsonUtil.getBooleanOrDefault(jsonObject, "value", this.getDefaultValue());
        return jsonObject;
    }

    @Override
    public JsonObject buildUpSettingData(JsonObject jsonObject) {
        JsonArray children = new JsonArray();

        for (Setting<?> setting : this.subSettings) {
            children.add(setting.buildUpSettingData(new JsonObject()));
        }

        jsonObject.add("children", children);
        jsonObject.addProperty("name", this.getName());
        return super.buildUpSettingData(jsonObject);
    }

    public List<Setting<?>> getSubSettings() {
        return subSettings;
    }
}