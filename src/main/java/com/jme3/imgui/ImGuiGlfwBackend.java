package com.jme3.imgui;

import imgui.glfw.ImGuiImplGlfw;

/**
 * @author capdevon
 */
public class ImGuiGlfwBackend implements ImGuiPlatformBackend {

    private final ImGuiImplGlfw glfw = new ImGuiImplGlfw();

    @Override
    public void init(long windowHandle) {
        boolean installCallbacks = false; // Let jME handle input callbacks
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