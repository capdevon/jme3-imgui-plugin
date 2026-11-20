package com.jme3.imgui;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.lwjgl.LwjglWindow;
import com.jme3.texture.FrameBuffer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

/**
 * SceneProcessor implementation for Dear ImGui integration into the jMonkeyEngine rendering lifecycle.
 * Manages ImGui context, input routing, and rendering of ImGui windows within the scene graph.
 *
 * @author capdevon
 */
public class JmeImGuiSceneProcessor implements SceneProcessor {

    private JmeContext context;
    private InputManager inputManager;
    private RenderManager renderManager;
    private ViewPort vp;
    private boolean initialized = false;
    private boolean enabled = true;

    private ImGuiRenderer renderer;
    private final List<ImGuiWindow> windows = new ArrayList<>();
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

        AppSettings settings = context.getSettings();
        boolean gles3 = JmeGraphicsApi.forRenderer(settings.getRenderer()) == JmeGraphicsApi.OPENGL_ES;
        boolean useGlfwBackend = cfg.isUseGlfwBackend();

        renderer = ImGuiRenderer.create((LwjglWindow) context, useGlfwBackend, gles3);
        renderer.initialize(cfg);
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        this.renderManager = rm;
        this.vp = vp;
        this.initialized = true;

        if (isEnabled()) {
            onEnable();
        }
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
        // no-op
    }

    @Override
    public final boolean isInitialized() {
        return initialized;
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

        int w = vp.getCamera().getWidth();
        int h = vp.getCamera().getHeight();

        renderer.newFrame();
        ImGui.getIO().setDisplaySize(w, h);
        ImGui.newFrame();

        try {
            for (ImGuiWindow window : windows) {
                if (window.isVisible()) {
                    window.render();
                }
            }
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
    public void cleanup() {
        if (isEnabled()) {
            onDisable();
        }

        renderer.dispose();
        initialized = false;
    }

    @Override
    public void setProfiler(AppProfiler profiler) {
        // no-op
    }

    public void onEnable() {
        inputManager.addRawInputListener(inputSystem);
    }

    public void onDisable() {
        inputManager.removeRawInputListener(inputSystem);
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        this.enabled = enabled;
        if (!isInitialized())
            return;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /**
     * Registers a window panel into the
     * active update/rendering pipeline.
     *
     * @param window The {@link ImGuiWindow} instance to manage.
     */
    public void addWindow(ImGuiWindow window) {
        windows.add(window);
    }

    /**
     * Removes a window panel from the active pipeline,
     * stopping its rendering layout updates.
     *
     * @param window The {@link ImGuiWindow} instance to remove.
     */
    public void removeWindow(ImGuiWindow window) {
        windows.remove(window);
    }

}
