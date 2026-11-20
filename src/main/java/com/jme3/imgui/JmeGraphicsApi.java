package com.jme3.imgui;

enum JmeGraphicsApi {

    OPENGL,
    OPENGL_ES;

    private static final String LWJGL_OPENGL = "LWJGL-OpenGL";
    private static final String ANGLE_GLES3 = "ANGLE_GLES3";

    /**
     * Determines the appropriate graphics API based on the jME renderer string.
     *
     * @param jmeRenderer the jME renderer string (e.g., "LWJGL-OpenGL32", "ANGLE_GLES3")
     * @return the corresponding JmeGraphicsApi enum value
     * @throws IllegalStateException if the jME renderer is unsupported
     */
    static JmeGraphicsApi forRenderer(String jmeRenderer) {
        if (ANGLE_GLES3.equals(jmeRenderer)) {
            return OPENGL_ES;
        }
        if (jmeRenderer != null && jmeRenderer.startsWith(LWJGL_OPENGL)) {
            return OPENGL;
        }
        throw new IllegalStateException("Unsupported jME renderer '" + jmeRenderer + "'");
    }
}