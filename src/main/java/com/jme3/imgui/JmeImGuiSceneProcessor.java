package com.jme3.imgui;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.lwjgl.LwjglWindow;
import com.jme3.texture.FrameBuffer;
import imgui.ImGui;

/**
 * SceneProcessor implementation for Dear ImGui integration into the jMonkeyEngine rendering lifecycle.
 * Manages ImGui context, input routing, and rendering of ImGui windows within the scene graph.
 *
 * @author capdevon
 */
public class JmeImGuiSceneProcessor extends AbstractSceneProcessor {

    private JmeContext context;
    private InputManager inputManager;

    private ImGuiRenderer renderer;
    private JmeImGuiConfig cfg;
    private final JmeImGuiInputListener inputSystem = new JmeImGuiInputListener();

    /**
     * Creates a new ImGui scene processor with the specified application context and configuration.
     *
     * @param app the jME application instance
     * @param cfg configuration options for ImGui initialization
     */
    public JmeImGuiSceneProcessor(Application app, JmeImGuiConfig cfg) {
        this.context = app.getContext();
        this.inputManager = app.getInputManager();
        this.cfg = cfg;
    }

    @Override
    public void onInitialize() {
        AppSettings settings = context.getSettings();
        boolean gles3 = JmeRendererBackend.forRenderer(settings.getRenderer()) == JmeRendererBackend.OPENGL_ES;
        boolean useGlfwBackend = false;

        renderer = ImGuiRenderer.create((LwjglWindow) context, useGlfwBackend, gles3);
        renderer.initialize(cfg);
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
        // no-op
    }

    @Override
    public void preFrame(float tpf) {
        // no-op
    }

    @Override
    public void postQueue(RenderQueue rq) {
        if (!isEnabled()) {
            return;
        }

        renderer.newFrame();
        ImGui.newFrame();

        try {
            ImGuiWindowManager.render();
        } finally {
            ImGui.render();
            renderer.renderDrawData();
        }
    }

    @Override
    public void postFrame(FrameBuffer out) {
        // no-op
    }

    @Override
    public void onCleanup() {
        ImGuiWindowManager.clear();
        renderer.dispose();
    }

    @Override
    public void onEnable() {
        inputManager.addRawInputListener(inputSystem);
    }

    @Override
    public void onDisable() {
        inputManager.removeRawInputListener(inputSystem);
    }

    /**
     * Registers a window panel into the
     * active update/rendering pipeline.
     *
     * @param window The {@link ImGuiWindow} instance to manage.
     */
    public void addWindow(ImGuiWindow window) {
        ImGuiWindowManager.addWindow(window);
    }

    /**
     * Removes a window panel from the active pipeline,
     * stopping its rendering layout updates.
     *
     * @param window The {@link ImGuiWindow} instance to remove.
     */
    public void removeWindow(ImGuiWindow window) {
        ImGuiWindowManager.removeWindow(window);
    }

}
