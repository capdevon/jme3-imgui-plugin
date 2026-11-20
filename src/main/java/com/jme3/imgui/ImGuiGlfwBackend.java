package com.jme3.imgui;

import com.jme3.system.lwjgl.LwjglWindow;
import imgui.glfw.ImGuiImplGlfw;

/**
 * Platform backend implementation using GLFW.
 *
 * @author capdevon
 */
class ImGuiGlfwBackend implements ImGuiPlatformBackend {

    private final LwjglWindow window;
    private final ImGuiImplGlfw glfw = new ImGuiImplGlfw();

    /**
     * Initializes the platform backend using the native window context.
     *
     * @param window the jME window instance
     */
    ImGuiGlfwBackend(LwjglWindow window) {
        this.window = window;
    }

    @Override
    public void init() {
        boolean installCallbacks = true;
        if (!glfw.init(window.getWindowHandle(), installCallbacks)) {
            throw new IllegalStateException("Dear ImGui GLFW platform backend failed to initialize");
        }
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