package com.mentalfrostbyte.jello.module.settings.impl;

import com.google.gson.JsonObject;
import com.mentalfrostbyte.jello.module.settings.Setting;
import com.mentalfrostbyte.jello.module.settings.SettingType;
import com.mentalfrostbyte.jello.util.system.other.GsonUtil;

public class NumberSetting extends Setting<Float> {
    private float minValue;
    private float maxValue;
    private float increment;

    public NumberSetting(String name, String description, float defaultValue, float minimum, float maximum, float increment) {
        super(name, description, SettingType.NUMBER, defaultValue);
        this.minValue = minimum;
        this.maxValue = maximum;
        this.increment = increment;
    }

    public int getPlaces() {
        if (this.increment != 1.0F) {
            String stepString = Float.toString(Math.abs(this.increment));
            int decimalPointIndex = stepString.indexOf('.');
            return stepString.length() - decimalPointIndex - 1;
        } else {
            return 0;
        }
    }

    @Override
    public JsonObject loadCurrentValueFromJSONObject(JsonObject jsonObject) {
        this.currentValue = GsonUtil.getFloatOrDefault(jsonObject, "value", this.getDefaultValue());
        return jsonObject;
    }

    public float getMin() {
        return this.minValue;
    }

    public void setMin(float var1) {
        this.minValue = var1;
    }

    public float getMax() {
        return this.maxValue;
    }

    public void setMax(float var1) {
        this.maxValue = var1;
    }

    public float getIncrement() {
        return this.increment;
    }

    public void setIncrement(float var1) {
        this.increment = var1;
    }
}
