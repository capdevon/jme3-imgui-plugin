package com.jme3.imgui;

import imgui.sdl3.ImGuiImplSdl3;

/**
 * Platform backend implementation using SDL3.
 * Bridges SDL3 native runtime contexts with the ImGui frame cycle.
 *
 * @author capdevon
 */
public class ImGuiSdlBackend implements ImGuiPlatformBackend {

    private final ImGuiImplSdl3 sdl = new ImGuiImplSdl3();

    @Override
    public void init(long windowHandle) {
        sdl.init(windowHandle);
    }

    @Override
    public void newFrame() {
        sdl.newFrame();
    }

    @Override
    public void dispose() {
        sdl.shutdown();
    }
}