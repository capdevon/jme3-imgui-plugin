package com.jme3.imgui;

import imgui.ImGui;
import imgui.ImGuiStyle;

/**
 * Utility class for managing and applying built-in ImGui themes .
 *
 * @author capdevon
 */
public final class ImGuiTheme {

    /**
     * Supported visual themes for the ImGui interface.
     */
    public enum Theme {
        /** Default dark theme. */
        DARK,
        /** Standard light theme. */
        LIGHT,
        /** Legacy ImGui classic theme. */
        CLASSIC
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ImGuiTheme() {}

    /**
     * Applies the specified theme to the current ImGui context.
     * * @param theme the {@link Theme} to apply
     */
    public static void apply(Theme theme) {
        switch (theme) {
            case DARK:
                ImGui.styleColorsDark();
                break;
            case LIGHT:
                ImGui.styleColorsLight();
                break;
            case CLASSIC:
                ImGui.styleColorsClassic();
                break;
        }
    }

}