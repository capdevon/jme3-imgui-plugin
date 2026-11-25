package com.jme3.imgui;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * Configures ImGui fonts, including the FontAwesome icon font.
 *
 * @author capdevon
 */
public class ImGuiFontConfig {

    /**
     * Initializes fonts using the provided configuration.
     * FontAwesome is always merged into the selected base font.
     *
     * @param io ImGui IO instance
     * @param cfg font configuration
     */
    public static void initFonts(final ImGuiIO io, final JmeImGuiConfig cfg) {
        final ImFontAtlas atlas = io.getFonts();
        final float size = cfg.getFontSize();

        // 1. Configure the base font
        ImFontConfig baseFontConfig = new ImFontConfig();
        try {
            // Improve quality on HiDPI displays
            baseFontConfig.setOversampleH(2);
            baseFontConfig.setOversampleV(2);
            baseFontConfig.setRasterizerMultiply(1.25f);

            if (cfg.getFontPreset() == FontPreset.DEFAULT) {
                // Use the built-in ImGui ProggyClean font
                atlas.addFontDefault(baseFontConfig);
            } else {
                // Load the custom font from resources
                String fontPath = "/Fonts/" + cfg.getFontPreset().getFileName();
                atlas.addFontFromMemoryTTF(loadFromResources(fontPath), size, baseFontConfig);
            }
        } finally {
            baseFontConfig.destroy();
        }

        // 2. Configure and merge the FontAwesome icon font
        ImFontConfig iconConfig = new ImFontConfig();
        try {
            iconConfig.setMergeMode(true);
            iconConfig.setGlyphMinAdvanceX(size);
            iconConfig.setGlyphOffset(0, 1); // Correct FontAwesome's typical vertical misalignment

            short[] iconRanges = FontAwesomeIcons._IconRange;
            
            atlas.addFontFromMemoryTTF(
                    loadFromResources("/fa-solid-900.ttf"), 
                    size, 
                    iconConfig, 
                    iconRanges);
        } finally {
            iconConfig.destroy();
        }
    }

    /**
     * Loads a font resource into a byte array.
     *
     * @param name resource path
     * @return font data
     * @throws IllegalArgumentException if the resource is missing
     * @throws RuntimeException if the resource cannot be read
     */
    static byte[] loadFromResources(String name) {
        try (InputStream is = ImGuiFontConfig.class.getResourceAsStream(name)) {
            if (is == null) {
                throw new IllegalArgumentException("Font resource not found: " + name);
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font resource: " + name, e);
        }
    }
}