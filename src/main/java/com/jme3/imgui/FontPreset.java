package com.jme3.imgui;

/**
 * Predefined font presets available for the ImGui editor.
 * Note: For presets other than DEFAULT, the corresponding .ttf file 
 * must be present in src/main/resources/Fonts/
 *
 * @author capdevon
 */
public enum FontPreset {

    DEFAULT("Built-in ImGui Font (ProggyClean)"),
    ROBOTO("Roboto-Regular.ttf"),
    OPEN_SANS("OpenSans-Regular.ttf"),
    JETBRAINS_MONO("JetBrainsMono-Regular.ttf"),
    INTER("Inter-Regular.ttf");

    private final String fileName;

    FontPreset(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    
}