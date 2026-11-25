package com.jme3.imgui;

/**
 * Configuration settings for initializing {@link JmeImGui}.
 */
public class JmeImGuiConfig {

    private boolean useGlfwBackend = false;
    private boolean enableDocking = true;
    private boolean enableViewports = true;
    private String iniFilename = null; // null disables .ini persistence
    private FontPreset fontPreset = FontPreset.DEFAULT;
    private float fontSize = 14.0f;

    public void setUseGlfwBackend(boolean useGlfwBackend) {
        this.useGlfwBackend = useGlfwBackend;
    }

    public void setEnableDocking(boolean enableDocking) {
        this.enableDocking = enableDocking;
    }

    public void setEnableViewports(boolean enableViewports) {
        this.enableViewports = enableViewports;
    }

    public void setIniFilename(String iniFilename) {
        this.iniFilename = iniFilename;
    }

    public void setFontPreset(FontPreset fontPreset) {
        this.fontPreset = fontPreset;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    FontPreset getFontPreset() {
        return fontPreset;
    }

    float getFontSize() {
        return fontSize;
    }

    boolean isUseGlfwBackend() {
        return useGlfwBackend;
    }

    boolean isEnableDocking() {
        return enableDocking;
    }

    boolean isEnableViewports() {
        return enableViewports;
    }

    String getIniFilename() {
        return iniFilename;
    }

}