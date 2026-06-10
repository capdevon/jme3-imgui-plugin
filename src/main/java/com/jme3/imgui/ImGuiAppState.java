package com.jme3.imgui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

/**
 * @author capdevon
 */
public class ImGuiAppState extends BaseAppState {

    private final ImGuiManager manager = new ImGuiManager();
    private final JmeImGui imgui = new JmeImGui();
    private final JmeImGuiInputAdapter input = new JmeImGuiInputAdapter();

    private final boolean useGlfwBackend;

    public ImGuiAppState() {
        this(false); // default: custom input
    }

    public ImGuiAppState(boolean useGlfwBackend) {
        this.useGlfwBackend = useGlfwBackend;
    }

    @Override
    protected void initialize(Application app) {
        imgui.init(app.getContext(), useGlfwBackend);
        app.getInputManager().addRawInputListener(input);
        app.getInputManager().addJoystickConnectionListener(input);
    }

    @Override
    protected void cleanup(Application app) {
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
        manager.render(imgui);
    }

    public ImGuiManager getManager() {
        return manager;
    }

}