package com.jme3.imgui;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized Service Locator for managing {@link ImGuiWindow} instances.
 * <p>
 * This class provides a global, decoupled access point to register, remove,
 * and retrieve ImGui windows.
 * </p>
 *
 * @author capdevon
 */
public final class ImGuiWindowManager {

    /**
     * List containing all registered {@link ImGuiWindow} instances.
     */
    private static final List<ImGuiWindow> windows = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private ImGuiWindowManager() {
        // no-op
    }

    /**
     * Renders all registered windows.
     */
    static void render() {
        for (ImGuiWindow window : windows) {
            window.render();
        }
    }

    /**
     * Retrieves the first registered window matching the given class type.
     *
     * @param <T>         the window type.
     * @param windowClass the class to search for.
     * @return the matched instance, or {@code null} if not found.
     */
    public static <T extends ImGuiWindow> T getWindow(Class<T> windowClass) {
        for (ImGuiWindow window : windows) {
            if (windowClass.isAssignableFrom(window.getClass())) {
                return windowClass.cast(window);
            }
        }
        return null;
    }

    /**
     * Registers a window to the manager.
     *
     * @param window the window to add.
     */
    public static void addWindow(ImGuiWindow window) {
        windows.add(window);
    }

    /**
     * Removes a window from the manager.
     *
     * @param window the window to remove.
     * @return {@code true} if the window was removed.
     */
    public static boolean removeWindow(ImGuiWindow window) {
        return windows.remove(window);
    }

    /**
     * Removes all registered windows.
     */
    public static void clear() {
        windows.clear();
    }

}