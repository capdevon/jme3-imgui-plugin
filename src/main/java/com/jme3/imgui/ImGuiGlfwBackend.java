package com.jme3.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

/**
 * Platform backend implementation using GLFW.
 * Used when multi-viewport rendering or standard desktop windowing hooks are enabled.
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

        ImGuiIO io = ImGui.getIO();
        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(String text) {
                GLFW.glfwSetClipboardString(windowHandle, text);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                String text = GLFW.glfwGetClipboardString(windowHandle);
                return text != null ? text : "";
            }
        });
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