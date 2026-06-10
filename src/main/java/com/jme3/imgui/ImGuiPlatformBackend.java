package com.jme3.imgui;

public interface ImGuiPlatformBackend {

    void init(long windowHandle);

    void newFrame();

    void dispose();
}