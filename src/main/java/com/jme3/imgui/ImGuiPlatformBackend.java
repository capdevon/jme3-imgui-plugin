package com.jme3.imgui;

/**
 * Abstraction layer for Dear ImGui platform backends (e.g., GLFW, SDL3).
 * Bridges the low-level native windowing system handles with the ImGui lifecycle.
 *
 * @author capdevon
 */
public interface ImGuiPlatformBackend {

    /**
     * Initializes the platform backend using the native window context.
     *
     * @param windowHandle The OS-specific native window handle pointer.
     */
    void init(long windowHandle);

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
}