package com.jme3.imgui;

import imgui.sdl3.ImGuiImplSdl3;

/**
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