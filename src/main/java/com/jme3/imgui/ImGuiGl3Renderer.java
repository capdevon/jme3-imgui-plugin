package com.jme3.imgui;

import com.jme3.system.JmeSystem;
import com.jme3.system.Platform;
import imgui.ImDrawData;
import imgui.gl3.ImGuiImplGl3;

/** OpenGL 3.2+ core variant. */
public final class ImGuiGl3Renderer extends ImGuiRenderer {

    private ImGuiImplGl3 gl3;

    public ImGuiGl3Renderer(ImGuiPlatformBackend platform) {
        super(platform);
    }

    @Override
    protected void initRendererBackend() {
        ImGuiImplGl3 impl = new ImGuiImplGl3();
        if (!impl.init(glslVersion())) {
            throw new IllegalStateException("Dear ImGui OpenGL renderer backend failed to initialize");
        }
        this.gl3 = impl;
    }

    @Override
    protected void rendererNewFrame() {
        gl3.newFrame();
    }

    @Override
    protected void renderDrawData(ImDrawData drawData) {
        gl3.renderDrawData(drawData);
    }

    @Override
    protected void shutdownRendererBackend() {
        if (gl3 != null) {
            gl3.shutdown();
        }
    }

    @Override
    protected void clearRendererBackend() {
        gl3 = null;
    }

    private static String glslVersion() {
        return JmeSystem.getPlatform().getOs() == Platform.Os.MacOS ? "#version 150" : "#version 330";
    }
}