package com.jme3.imgui;

/**
 * Configuration settings for initializing {@link JmeImGui}.
 */
public class JmeImGuiConfig {

    private boolean useGlfwBackend = false;
    private boolean enableDocking = true;
    private boolean enableViewports = true;
    private String iniFilename = null; // null disables .ini persistence
    private ImGuiTheme.Theme theme = ImGuiTheme.Theme.CLASSIC;

    public JmeImGuiConfig setUseGlfwBackend(boolean useGlfwBackend) {
        this.useGlfwBackend = useGlfwBackend;
        return this;
    }

    public JmeImGuiConfig setEnableDocking(boolean enableDocking) {
        this.enableDocking = enableDocking;
        return this;
    }

    public JmeImGuiConfig setEnableViewports(boolean enableViewports) {
        this.enableViewports = enableViewports;
        return this;
    }

    public JmeImGuiConfig setIniFilename(String iniFilename) {
        this.iniFilename = iniFilename;
        return this;
    }

    public JmeImGuiConfig setTheme(ImGuiTheme.Theme theme) {
        this.theme = theme;
        return this;
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