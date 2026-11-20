package com.jme3.imgui;

import imgui.ImDrawData;

/** OpenGL ES3 variant (jME ANGLE renderer), using the plugin's ImGuiImplGles3 port. */
public final class ImGuiGles3Renderer extends ImGuiRenderer {

    private ImGuiImplGles3 gles3;

    public ImGuiGles3Renderer(ImGuiPlatformBackend platform) {
        super(platform);
    }

    @Override
    protected void initRendererBackend() {
        ImGuiImplGles3 impl = new ImGuiImplGles3();
        if (!impl.init()) {
            throw new IllegalStateException("Dear ImGui OpenGL ES3 renderer backend failed to initialize");
        }
        this.gles3 = impl;
    }

    @Override
    protected void rendererNewFrame() {
        gles3.newFrame();
    }

    @Override
    protected void renderDrawData(ImDrawData drawData) {
        gles3.renderDrawData(drawData);
    }

    @Override
    protected void shutdownRendererBackend() {
        if (gles3 != null) {
            gles3.shutdown();
        }
    }

    @Override
    protected void clearRendererBackend() {
        gles3 = null;
    }
}