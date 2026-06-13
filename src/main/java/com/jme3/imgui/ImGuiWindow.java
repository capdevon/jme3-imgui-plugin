package com.jme3.imgui;

/**
 * Base abstract class representing a modular
 * Dear ImGui window panel inside jMonkeyEngine.
 *
 * @author capdevon
 */
public abstract class ImGuiWindow {

    private final String id;
    private boolean visible = true;

    /**
     * Creates a new ImGuiWindow abstraction
     * instance with a unique title/identifier.
     *
     * @param id The unique string ID (and default window title) for this panel.
     */
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

    /**
     * Defines the immediate-mode layout, widgets,
     * and logic for this window.
     */
    public abstract void render();
}