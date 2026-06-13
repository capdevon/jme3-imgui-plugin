package com.jme3.imgui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

/**
 * Standard jMonkeyEngine AppState interface wrapper for ImGui integration.
 * Automatically wires up runtime lifecycles, lifecycle setups, raw input hooks,
 * and render synchronization.
 *
 * @author capdevon
 */
public class ImGuiAppState extends BaseAppState {

    private final ImGuiManager manager = new ImGuiManager();
    private final JmeImGui imgui = new JmeImGui();
    private final JmeImGuiInputAdapter input = new JmeImGuiInputAdapter();

    private final boolean useGlfwBackend;

    /**
     * Creates an ImGui AppState using the default custom input routing layer.
     */
    public ImGuiAppState() {
        this(false); // default: custom input
    }

    /**
     * Creates an ImGui AppState with an explicit option
     * to use native window platform backends.
     *
     * @param useGlfwBackend True to leverage native GLFW platform integrations.
     */
    public ImGuiAppState(boolean useGlfwBackend) {
        this.useGlfwBackend = useGlfwBackend;
    }

    @Override
    protected void initialize(Application app) {
        // Bootstrap ImGui context and graphics pipeline
        imgui.init(app.getContext(), app.getViewPort(), useGlfwBackend);

        // Link the engine's raw input system to our custom ImGui translation driver
        app.getInputManager().addRawInputListener(input);
        app.getInputManager().addJoystickConnectionListener(input);
    }

    @Override
    protected void cleanup(Application app) {
        // Unbind listeners safely to prevent memory leaks or dead inputs upon state detach
        app.getInputManager().removeRawInputListener(input);
        app.getInputManager().removeJoystickConnectionListener(input);
        imgui.dispose();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void postRender() {
        // Ensure UI commands are rendered at the final
        // layer of the hardware execution pipeline
        manager.render(imgui);
    }

    /**
     * Returns the master manager instance responsible
     * for tracking UI state delegates.
     *
     * @return The active ImGuiManager.
     */
    public ImGuiManager getManager() {
        return manager;
    }

}