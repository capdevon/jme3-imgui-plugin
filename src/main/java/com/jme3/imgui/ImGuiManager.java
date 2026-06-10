package com.jme3.imgui;

import java.util.ArrayList;
import java.util.List;

/**
 * @author capdevon
 */
public class ImGuiManager {

    private final List<ImGuiWindow> windows = new ArrayList<>();

    public void addWindow(ImGuiWindow window) {
        windows.add(window);
    }

    public void removeWindow(ImGuiWindow window) {
        windows.remove(window);
    }

    public void render(JmeImGui imGui) {
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