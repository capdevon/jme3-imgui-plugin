package com.jme3.imgui;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry and layout coordinator for registered {@link ImGuiWindow} instances.
 *
 * @author capdevon
 */
public class ImGuiManager {

    /**
     * Managed collection of registered UI windows.
     */
    private final List<ImGuiWindow> windows = new ArrayList<>();

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

    protected void render(JmeImGui imGui) {
        imGui.startFrame();

        for (ImGuiWindow window : windows) {
            if (!window.isVisible()) {
                continue;
            }
            window.render();
        }

        imGui.endFrame();
    }
}