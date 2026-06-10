package com.jme3.imgui;

/**
 * @author capdevon
 */
public abstract class ImGuiWindow {

    private final String id;
    private boolean visible = true;

    protected ImGuiWindow(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract void render();
}