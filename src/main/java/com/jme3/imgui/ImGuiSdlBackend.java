package com.jme3.imgui;

import com.jme3.input.lwjgl.SdlEventListener;
import com.jme3.system.lwjgl.LwjglWindow;
import imgui.ImGui;
import imgui.sdl3.ImGuiImplSdl3;
import org.lwjgl.sdl.SDLKeyboard;
import org.lwjgl.sdl.SDL_Event;

/**
 * Platform backend implementation using SDL3.
 *
 * @author capdevon
 */
class ImGuiSdlBackend implements ImGuiPlatformBackend, SdlEventListener {

    private final LwjglWindow window;
    private final ImGuiImplSdl3 sdl = new ImGuiImplSdl3();

    private boolean textInputActive = false;

    /**
     * Initializes the platform backend using the native window context.
     *
     * @param window the jME window instance
     */
    ImGuiSdlBackend(LwjglWindow window) {
        this.window = window;
    }

    @Override
    public void init() {
        if (!sdl.init(window.getWindowHandle())) {
            throw new IllegalStateException("Dear ImGui SDL3 platform backend failed to initialize");
        }
        window.registerSdlEventListener(this);
    }

    @Override
    public void newFrame() {
        sdl.newFrame();

        boolean wanted = ImGui.getIO().getWantTextInput();
        if (wanted != textInputActive) {
            if (wanted) {
                // Record only on success: a refused start is retried on the next frame.
                textInputActive = SDLKeyboard.SDL_StartTextInput(window.getWindowHandle());
            } else {
                SDLKeyboard.SDL_StopTextInput(window.getWindowHandle());
                textInputActive = false;
            }
        }
    }

    @Override
    public void dispose() {
        window.removeSdlEventListener(this);
        if (textInputActive) {
            SDLKeyboard.SDL_StopTextInput(window.getWindowHandle());
            textInputActive = false;
        }

        sdl.shutdown();
    }

    @Override
    public void onSDLEvent(SDL_Event event) {
        sdl.processEvent(event.address());
    }
}