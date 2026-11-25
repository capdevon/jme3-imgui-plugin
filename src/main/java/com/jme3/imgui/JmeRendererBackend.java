package com.jme3.imgui;

public enum JmeRendererBackend {

    OPENGL,
    OPENGL_ES;

    private static final String LWJGL_OPENGL = "LWJGL-OpenGL";
    private static final String ANGLE_GLES3 = "ANGLE_GLES3";

    /**
     * Resolves the graphics backend from a jME renderer string.
     *
     * @param jmeRenderer the renderer string (e.g., "LWJGL-OpenGL32", "ANGLE_GLES3")
     * @return the matching backend
     * @throws IllegalStateException if the renderer is unsupported
     */
    public static JmeRendererBackend forRenderer(String jmeRenderer) {

        if (jmeRenderer != null) {
            if (jmeRenderer.startsWith(LWJGL_OPENGL)) {
                return OPENGL;
            }
            if (jmeRenderer.equals(ANGLE_GLES3)) {
                return OPENGL_ES;
            }
        }
        throw new IllegalStateException("Unsupported jME renderer: " + jmeRenderer);
    }
}