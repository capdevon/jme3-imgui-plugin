package com.jme3.imgui;

/**
 * Abstraction layer for Dear ImGui platform backends (e.g., GLFW, SDL3).
 * Bridges the low-level native windowing system handles with the ImGui lifecycle.
 *
 * @author capdevon
 */
public interface ImGuiPlatformBackend {

    /**
     * Initializes the platform backend and maps
     * any required native OS resources.
     */
    void init();

    /**
     * Updates the platform backend state
     * at the start of a new frame.
     */
    void newFrame();

    /**
     * Shuts down the platform backend and
     * releases any mapped native OS resources.
     */
    void dispose();

    /**
     * Whether this backend can drive ImGui multi-viewport (platform windows).
     */
    default boolean supportsViewports() {
        return false;
    }

    /**
     * Renders secondary platform windows, if ViewportsEnable is active.
     * Called after the main draw data.
     */
    default void renderPlatformWindows() {
    }
}