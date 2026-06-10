package com.jme3.imgui;

import imgui.ImGui;
import imgui.ImGuiStyle;

/**
 * @author capdevon
 */
public class ImGuiTheme {

    public enum Theme {
        DARK, LIGHT, CLASSIC
    }

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