package com.jme3.imgui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard jMonkeyEngine AppState interface wrapper for ImGui integration.
 * Automatically wires up runtime lifecycles, lifecycle setups, raw input hooks,
 * and render synchronization.
 *
 * @author capdevon
 */
public class ImGuiAppState extends BaseAppState {

    private JmeImGui imgui;
    private final JmeImGuiConfig cfg;
    private final List<ImGuiWindow> windows = new ArrayList<>();
    private final JmeImGuiInputListener inputSystem = new JmeImGuiInputListener();

    private InputManager inputManager;

    /**
     * Creates an ImGui AppState with a custom configuration.
     *
     * @param cfg configuration options for ImGui initialization
     */
    public ImGuiAppState(JmeImGuiConfig cfg) {
        this.cfg = cfg;
    }

    @Override
    protected void initialize(Application app) {
        // Bootstrap ImGui context and graphics pipeline
        imgui = new JmeImGui();
        imgui.init(app.getContext(), app.getGuiViewPort(), cfg);
        this.inputManager = app.getInputManager();
    }

    @Override
    protected void cleanup(Application app) {
        imgui.dispose();
    }

    @Override
    protected void onEnable() {
        inputManager.addRawInputListener(inputSystem);
    }

    @Override
    protected void onDisable() {
        inputManager.removeRawInputListener(inputSystem);
    }

    @Override
    public void postRender() {
        imgui.startFrame();
        try {
            for (ImGuiWindow window : windows) {
                if (window.isVisible()) {
                    window.render();
                }
            }
        } finally {
            imgui.endFrame();
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