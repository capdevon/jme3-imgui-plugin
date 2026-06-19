package com.jme3.imgui;

import imgui.glfw.ImGuiImplGlfw;

/**
 * Platform backend implementation using GLFW.
 *
 * @author capdevon
 */
public class ImGuiGlfwBackend implements ImGuiPlatformBackend {

    private final ImGuiImplGlfw glfw = new ImGuiImplGlfw();

    @Override
    public void init(long windowHandle) {
        // Let jME handle its own window/input callbacks.
        boolean installCallbacks = false;
        glfw.init(windowHandle, installCallbacks);
    }

    @Override
    public void newFrame() {
        glfw.newFrame();
    }

    @Override
    public void dispose() {
        glfw.shutdown();
    }
}