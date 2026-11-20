package com.jme3.imgui;

/**
 * Configuration settings for initializing {@link JmeImGui}.
 */
public class JmeImGuiConfig {

    private boolean useGlfwBackend = false;
    private boolean enableDocking = true;
    private boolean enableViewports = false;
    private String iniFilename = null; // null disables .ini persistence
    private ImGuiTheme.Theme theme = ImGuiTheme.Theme.CLASSIC;

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

    public void setTheme(ImGuiTheme.Theme theme) {
        this.theme = theme;
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

    ImGuiTheme.Theme getTheme() {
        return theme;
    }
}