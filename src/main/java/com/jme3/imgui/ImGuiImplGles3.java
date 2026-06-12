package com.jme3.imgui;

import imgui.ImDrawData;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec4;

import imgui.flag.ImGuiBackendFlags;
import imgui.type.ImInt;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLESCapabilities;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.*;

public class ImGuiImplGles3 {

    protected static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    protected static final boolean IS_APPLE = OS.contains("mac") || OS.contains("darwin");

    /**
     * Data class to store implementation specific fields.
     * Same as {@code ImGui_ImplOpenGL3_Data}.
     */
    protected static class Data {
        protected int glVersion = 0; // Extracted at runtime using GL_MAJOR_VERSION, GL_MINOR_VERSION queries (e.g. 320 for GL 3.2)
        //protected boolean glProfileIsES2;
        protected boolean glProfileIsES3;
        protected boolean glProfileIsCompat;
        protected int glProfileMask;
        protected int maxTextureSize;
        protected GLCapabilities glCapabilities = null;
        protected String glslVersion = "";
        protected int fontTexture = 0;
        protected int shaderHandle = 0;
        protected int attribLocationTex = 0; // Uniforms location
        protected int attribLocationProjMtx = 0;
        protected int attribLocationVtxPos = 0; // Vertex attributes location
        protected int attribLocationVtxUV = 0;
        protected int attribLocationVtxColor = 0;
        protected int vboHandle = 0;
        protected int elementsHandle = 0;
        // protected int vertexBufferSize;
        // protected int indexBufferSize;
        protected boolean hasPolygonMode;
        protected boolean hasBindSampler;
        protected boolean hasClipOrigin;
    }

    /**
     * Internal class to store containers for frequently used arrays.
     * This class helps minimize the number of object allocations on the JVM side,
     * thereby improving performance and reducing garbage collection overhead.
     */
    private static final class Properties {
        private final ImVec4 clipRect = new ImVec4();
        private final float[] orthoProjMatrix = new float[4 * 4];
        private final int[] lastActiveTexture = new int[1];
        private final int[] lastProgram = new int[1];
        private final int[] lastTexture = new int[1];
        private final int[] lastSampler = new int[1];
        private final int[] lastArrayBuffer = new int[1];
        private final int[] lastVertexArrayObject = new int[1];
        private final int[] lastPolygonMode = new int[2];
        private final int[] lastViewport = new int[4];
        private final int[] lastScissorBox = new int[4];
        private final int[] lastBlendSrcRgb = new int[1];
        private final int[] lastBlendDstRgb = new int[1];
        private final int[] lastBlendSrcAlpha = new int[1];
        private final int[] lastBlendDstAlpha = new int[1];
        private final int[] lastBlendEquationRgb = new int[1];
        private final int[] lastBlendEquationAlpha = new int[1];
        private boolean lastEnableBlend = false;
        private boolean lastEnableCullFace = false;
        private boolean lastEnableDepthTest = false;
        private boolean lastEnableStencilTest = false;
        private boolean lastEnableScissorTest = false;
        private boolean lastEnablePrimitiveRestart = false;
    }

    protected ImGuiImplGles3.Data data = null;
    private final ImGuiImplGles3.Properties props = new ImGuiImplGles3.Properties();

    protected ImGuiImplGles3.Data newData() {
        return new ImGuiImplGles3.Data();
    }

    public boolean init() {
        return init(null);
    }

    public boolean init(final String glslVersion) {

        data = newData();

        final ImGuiIO io = ImGui.getIO();
        io.setBackendRendererName("imgui-java_impl_opengles3");

        data.glProfileIsES3 = true;
        data.glVersion = 300;

        data.glslVersion = "#version 300 es";

//        io.addBackendFlags(ImGuiBackendFlags.RendererHasViewports);
        io.removeBackendFlags(ImGuiBackendFlags.RendererHasViewports);

        data.hasPolygonMode = false;
        data.hasBindSampler = false;
        data.hasClipOrigin = false;

        return true;
    }

    public void shutdown() {
        final ImGuiIO io = ImGui.getIO();

        shutdownPlatformInterface();
        destroyDeviceObjects();

        io.setBackendRendererName(null);
        // In C++: io.BackendFlags also clears RendererHasTextures, then platform_io.ClearRendererHandlers() runs.
        // In Java: RendererHasTextures is never set (see init), and ClearRendererHandlers is not exposed in imgui-binding (follow-up).
        io.removeBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset | ImGuiBackendFlags.RendererHasViewports);
        data = null;
    }

    public void newFrame() {
        if (data.shaderHandle == 0) {
            createDeviceObjects();
        }
        if (data.fontTexture == 0) {
            createFontsTexture();
        }
    }

    public void renderDrawData(final ImDrawData drawData) {
        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
        final int fbWidth = (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        final int fbHeight = (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());
        if (fbWidth <= 0 || fbHeight <= 0) {
            return;
        }

        if (drawData.getCmdListsCount() <= 0) {
            return;
        }

        // In C++: iterates draw_data->Textures and calls ImGui_ImplOpenGL3_UpdateTexture for each non-OK status.
        // In Java: ImTextureData is not exposed in imgui-binding (follow-up); we keep the legacy createFontsTexture
        //          path triggered from newFrame(), so dynamic atlas updates are not honored here yet.

        glGetIntegerv(GL_ACTIVE_TEXTURE, props.lastActiveTexture);
        glActiveTexture(GL_TEXTURE0);
        glGetIntegerv(GL_CURRENT_PROGRAM, props.lastProgram);
        glGetIntegerv(GL_TEXTURE_BINDING_2D, props.lastTexture);
        if (data.hasBindSampler) {
            glGetIntegerv(GL_SAMPLER_BINDING, props.lastSampler);
        }
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, props.lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, props.lastVertexArrayObject);
//        if (data.hasPolygonMode) {
//            glGetIntegerv(GL_POLYGON_MODE, props.lastPolygonMode);
//        }
        glGetIntegerv(GL_VIEWPORT, props.lastViewport);
        glGetIntegerv(GL_SCISSOR_BOX, props.lastScissorBox);
        glGetIntegerv(GL_BLEND_SRC_RGB, props.lastBlendSrcRgb);
        glGetIntegerv(GL_BLEND_DST_RGB, props.lastBlendDstRgb);
        glGetIntegerv(GL_BLEND_SRC_ALPHA, props.lastBlendSrcAlpha);
        glGetIntegerv(GL_BLEND_DST_ALPHA, props.lastBlendDstAlpha);
        glGetIntegerv(GL_BLEND_EQUATION_RGB, props.lastBlendEquationRgb);
        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, props.lastBlendEquationAlpha);
        props.lastEnableBlend = glIsEnabled(GL_BLEND);
        props.lastEnableCullFace = glIsEnabled(GL_CULL_FACE);
        props.lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST);
        props.lastEnableStencilTest = glIsEnabled(GL_STENCIL_TEST);
        props.lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST);
//        if (!data.glProfileIsES3 && data.glVersion >= 310) {
//            props.lastEnablePrimitiveRestart = glIsEnabled(GL_PRIMITIVE_RESTART);
//        }

        // Setup desired GL state
        // Recreate the VAO every time (this is to easily allow multiple GL contexts to be rendered to. VAO are not shared among GL contexts)
        // The renderer would actually work without any VAO bound, but then our VertexAttrib calls would overwrite the default one currently bound.
        final int vertexArrayObject = glGenVertexArrays();
        setupRenderState(drawData, fbWidth, fbHeight, vertexArrayObject);

        // Will project scissor/clipping rectangles into framebuffer space
        final float clipOffX = drawData.getDisplayPosX(); // (0,0) unless using multi-viewports
        final float clipOffY = drawData.getDisplayPosY(); // (0,0) unless using multi-viewports
        final float clipScaleX = drawData.getFramebufferScaleX(); // (1,1) unless using retina display which are often (2,2)
        final float clipScaleY = drawData.getFramebufferScaleY(); // (1,1) unless using retina display which are often (2,2)

        // Render command lists
        for (int n = 0; n < drawData.getCmdListsCount(); n++) {
            // FIXME: this is a straightforward port from Dear ImGui and it doesn't work with multi-viewports.
            //        So we keep solution we used before.
            // Upload vertex/index buffers
            // final int vtxBufferSize = drawData.getCmdListVtxBufferSize(n) * ImDrawData.sizeOfImDrawVert();
            // final int idxBufferSize = drawData.getCmdListIdxBufferSize(n) * ImDrawData.sizeOfImDrawIdx();
            // if (data.vertexBufferSize < vtxBufferSize) {
            //     data.vertexBufferSize = vtxBufferSize;
            //     glBufferData(GL_ARRAY_BUFFER, data.vertexBufferSize, GL_STREAM_DRAW);
            // }
            // if (data.indexBufferSize < idxBufferSize) {
            //     data.indexBufferSize = idxBufferSize;
            //     glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.indexBufferSize, GL_STREAM_DRAW);
            // }
            // glBufferSubData(GL_ARRAY_BUFFER, 0, drawData.getCmdListVtxBufferData(n));
            // glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, drawData.getCmdListIdxBufferData(n));

            // In C++: also has an UseBufferSubData branch (orphaning + glBufferSubData).
            // In Java: upstream forces UseBufferSubData = false, so we mirror only the glBufferData path here.
            glBufferData(GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(n), GL_STREAM_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(n), GL_STREAM_DRAW);

            for (int cmdIdx = 0; cmdIdx < drawData.getCmdListCmdBufferSize(n); cmdIdx++) {
                // TODO:
                // if userCallback
                // else

                drawData.getCmdListCmdBufferClipRect(props.clipRect, n, cmdIdx);

                final float clipMinX = (props.clipRect.x - clipOffX) * clipScaleX;
                final float clipMinY = (props.clipRect.y - clipOffY) * clipScaleY;
                final float clipMaxX = (props.clipRect.z - clipOffX) * clipScaleX;
                final float clipMaxY = (props.clipRect.w - clipOffY) * clipScaleY;

                if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) {
                    continue;
                }

                // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
                glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));

                // Bind texture, Draw
                final long textureId = drawData.getCmdListCmdBufferTextureId(n, cmdIdx);
                final int elemCount = drawData.getCmdListCmdBufferElemCount(n, cmdIdx);
                final int idxOffset = drawData.getCmdListCmdBufferIdxOffset(n, cmdIdx);
                final int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(n, cmdIdx);
                final long indices = idxOffset * (long) ImDrawData.sizeOfImDrawIdx();
                final int type = ImDrawData.sizeOfImDrawIdx() == 2 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;

                glBindTexture(GL_TEXTURE_2D, (int) textureId);

//                if (data.glVersion >= 320) {
//                    glDrawElementsBaseVertex(GL_TRIANGLES, elemCount, type, indices, vtxOffset);
//                } else {
                    glDrawElements(GL_TRIANGLES, elemCount, type, indices);
//                }
            }
        }

        // Destroy the temporary VAO
        glDeleteVertexArrays(vertexArrayObject);

        // Restore modified GL state
        // This "glIsProgram()" check is required because if the program is "pending deletion" at the time of binding backup, it will have been deleted by now and will cause an OpenGL error. See #6220.
        if (props.lastProgram[0] == 0 || glIsProgram(props.lastProgram[0])) {
            glUseProgram(props.lastProgram[0]);
        }
        glBindTexture(GL_TEXTURE_2D, props.lastTexture[0]);
        if (data.hasBindSampler) {
            glBindSampler(0, props.lastSampler[0]);
        }
        glActiveTexture(props.lastActiveTexture[0]);
        glBindVertexArray(props.lastVertexArrayObject[0]);
        glBindBuffer(GL_ARRAY_BUFFER, props.lastArrayBuffer[0]);
        glBlendEquationSeparate(props.lastBlendEquationRgb[0], props.lastBlendEquationAlpha[0]);
        glBlendFuncSeparate(props.lastBlendSrcRgb[0], props.lastBlendDstRgb[0], props.lastBlendSrcAlpha[0], props.lastBlendDstAlpha[0]);
        if (props.lastEnableBlend) glEnable(GL_BLEND);
        else glDisable(GL_BLEND);
        if (props.lastEnableCullFace) glEnable(GL_CULL_FACE);
        else glDisable(GL_CULL_FACE);
        if (props.lastEnableDepthTest) glEnable(GL_DEPTH_TEST);
        else glDisable(GL_DEPTH_TEST);
        if (props.lastEnableStencilTest) glEnable(GL_STENCIL_TEST);
        else glDisable(GL_STENCIL_TEST);
        if (props.lastEnableScissorTest) glEnable(GL_SCISSOR_TEST);
        else glDisable(GL_SCISSOR_TEST);
//        if (!data.glProfileIsES3 && data.glVersion >= 310) {
//            if (props.lastEnablePrimitiveRestart) {
//                glEnable(GL_PRIMITIVE_RESTART);
//            } else {
//                glDisable(GL_PRIMITIVE_RESTART);
//            }
//        }
        // Desktop OpenGL 3.0 and OpenGL 3.1 had separate polygon draw modes for front-facing and back-facing faces of polygons
//        if (data.hasPolygonMode) {
//            if (data.glVersion <= 310 || data.glProfileIsCompat) {
//                glPolygonMode(GL_FRONT, props.lastPolygonMode[0]);
//                glPolygonMode(GL_BACK, props.lastPolygonMode[1]);
//            } else {
//                glPolygonMode(GL_FRONT_AND_BACK, props.lastPolygonMode[0]);
//            }
//        }
        glViewport(props.lastViewport[0], props.lastViewport[1], props.lastViewport[2], props.lastViewport[3]);
        glScissor(props.lastScissorBox[0], props.lastScissorBox[1], props.lastScissorBox[2], props.lastScissorBox[3]);
    }

    protected boolean checkShader(final int handle, final String desc) {
        final int[] status = new int[1];
        final int[] logLength = new int[1];
        glGetShaderiv(handle, GL_COMPILE_STATUS, status);
        glGetShaderiv(handle, GL_INFO_LOG_LENGTH, logLength);
        if (status[0] == GL_FALSE) {
            System.err.printf("%s: failed to compile %s! With GLSL: %s\n", this, desc, data.glslVersion);
        }
        if (logLength[0] > 1) {
            final String log = glGetShaderInfoLog(handle);
            System.err.println(log);
        }
        return status[0] == GL_TRUE;
    }

    protected boolean checkProgram(final int handle, final String desc) {
        final int[] status = new int[1];
        final int[] logLength = new int[1];
        glGetProgramiv(handle, GL_LINK_STATUS, status);
        glGetProgramiv(handle, GL_INFO_LOG_LENGTH, logLength);
        if (status[0] == GL_FALSE) {
            System.err.printf("%s: failed to link %s! With GLSL: %s\n", this, desc, data.glslVersion);
        }
        if (logLength[0] > 1) {
            final String log = glGetProgramInfoLog(handle);
            System.err.println(log);
        }
        return status[0] == GL_TRUE;
    }

    protected int parseGlslVersionString(final String glslVersion) {
        final Pattern p = Pattern.compile("\\d+");
        final Matcher m = p.matcher(glslVersion);

        if (m.find()) {
            return Integer.parseInt(m.group());
        }

        return 130;
    }

    protected boolean createDeviceObjects() {

        final int[] lastTexture = new int[1];
        final int[] lastArrayBuffer = new int[1];

        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);

        final CharSequence vertexShader = vertexShaderGlsl300es();
        final CharSequence fragmentShader = fragmentShaderGlsl300es();

        final int vertHandle = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertHandle, vertexShader);
        glCompileShader(vertHandle);
        checkShader(vertHandle, "vertex shader");

        final int fragHandle = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragHandle, fragmentShader);
        glCompileShader(fragHandle);
        checkShader(fragHandle, "fragment shader");

        data.shaderHandle = glCreateProgram();
        glAttachShader(data.shaderHandle, vertHandle);
        glAttachShader(data.shaderHandle, fragHandle);
        glLinkProgram(data.shaderHandle);

        checkProgram(data.shaderHandle, "shader program");

        glDetachShader(data.shaderHandle, vertHandle);
        glDetachShader(data.shaderHandle, fragHandle);

        glDeleteShader(vertHandle);
        glDeleteShader(fragHandle);

        data.attribLocationTex =
                glGetUniformLocation(data.shaderHandle, "Texture");

        data.attribLocationProjMtx =
                glGetUniformLocation(data.shaderHandle, "ProjMtx");

        data.attribLocationVtxPos = 0;
        data.attribLocationVtxUV = 1;
        data.attribLocationVtxColor = 2;

        data.vboHandle = glGenBuffers();
        data.elementsHandle = glGenBuffers();

        createFontsTexture();

        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);

        return true;
    }

    public void destroyDeviceObjects() {
        if (data.vboHandle != 0) {
            glDeleteBuffers(data.vboHandle);
            data.vboHandle = 0;
        }
        if (data.elementsHandle != 0) {
            glDeleteBuffers(data.elementsHandle);
            data.elementsHandle = 0;
        }
        if (data.shaderHandle != 0) {
            glDeleteProgram(data.shaderHandle);
            data.shaderHandle = 0;
        }
        // In C++: iterates ImGui::GetPlatformIO().Textures and calls ImGui_ImplOpenGL3_DestroyTexture for each entry with RefCount == 1.
        // In Java: ImTextureData is not exposed in imgui-binding (follow-up); we delete the legacy fontTexture directly.
        destroyFontsTexture();
    }

    public void destroyFontsTexture() {
        final ImGuiIO io = ImGui.getIO();
        if (data.fontTexture != 0) {
            glDeleteTextures(data.fontTexture);
            io.getFonts().setTexID(0);
            data.fontTexture = 0;
        }
    }

    protected void setupRenderState(
            final ImDrawData drawData,
            final int fbWidth,
            final int fbHeight,
            final int vao) {

        glEnable(GL_BLEND);

        glBlendEquation(GL_FUNC_ADD);

        glBlendFuncSeparate(
                GL_SRC_ALPHA,
                GL_ONE_MINUS_SRC_ALPHA,
                GL_ONE,
                GL_ONE_MINUS_SRC_ALPHA);

        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);

        glEnable(GL_SCISSOR_TEST);

        glViewport(0, 0, fbWidth, fbHeight);

        float L = drawData.getDisplayPosX();
        float R = drawData.getDisplayPosX() + drawData.getDisplaySizeX();
        float T = drawData.getDisplayPosY();
        float B = drawData.getDisplayPosY() + drawData.getDisplaySizeY();

        props.orthoProjMatrix[0] = 2.0f / (R - L);
        props.orthoProjMatrix[5] = 2.0f / (T - B);
        props.orthoProjMatrix[10] = -1.0f;
        props.orthoProjMatrix[12] = (R + L) / (L - R);
        props.orthoProjMatrix[13] = (T + B) / (B - T);
        props.orthoProjMatrix[15] = 1.0f;

        glUseProgram(data.shaderHandle);

        glUniform1i(data.attribLocationTex, 0);

        glUniformMatrix4fv(
                data.attribLocationProjMtx,
                false,
                props.orthoProjMatrix);

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, data.vboHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, data.elementsHandle);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(
                0, 2, GL_FLOAT, false,
                ImDrawData.sizeOfImDrawVert(), 0);

        glVertexAttribPointer(
                1, 2, GL_FLOAT, false,
                ImDrawData.sizeOfImDrawVert(), 8);

        glVertexAttribPointer(
                2, 4, GL_UNSIGNED_BYTE, true,
                ImDrawData.sizeOfImDrawVert(), 16);
    }

    public boolean createFontsTexture() {

        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();

        final ImInt width = new ImInt();
        final ImInt height = new ImInt();

        final ByteBuffer pixels =
                fontAtlas.getTexDataAsRGBA32(width, height);

        final int[] lastTexture = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);

        data.fontTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, data.fontTexture);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER,
                GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER,
                GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_S,
                GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_T,
                GL_CLAMP_TO_EDGE);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width.get(),
                height.get(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                pixels);

        fontAtlas.setTexID(data.fontTexture);

        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);

        return true;
    }

    protected void shutdownPlatformInterface() {
        ImGui.destroyPlatformWindows();
    }

    protected String vertexShaderGlsl120() {
        return data.glslVersion + "\n"
                + "uniform mat4 ProjMtx;\n"
                + "attribute vec2 Position;\n"
                + "attribute vec2 UV;\n"
                + "attribute vec4 Color;\n"
                + "varying vec2 Frag_UV;\n"
                + "varying vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n"
                + "}\n";
    }

    protected String vertexShaderGlsl130() {
        return data.glslVersion + "\n"
                + "uniform mat4 ProjMtx;\n"
                + "in vec2 Position;\n"
                + "in vec2 UV;\n"
                + "in vec4 Color;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n"
                + "}\n";
    }

    private String vertexShaderGlsl300es() {
        return data.glslVersion + "\n"
                + "precision highp float;\n"
                + "layout (location = 0) in vec2 Position;\n"
                + "layout (location = 1) in vec2 UV;\n"
                + "layout (location = 2) in vec4 Color;\n"
                + "uniform mat4 ProjMtx;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n"
                + "}\n";
    }

    protected String vertexShaderGlsl410Core() {
        return data.glslVersion + "\n"
                + "layout (location = 0) in vec2 Position;\n"
                + "layout (location = 1) in vec2 UV;\n"
                + "layout (location = 2) in vec4 Color;\n"
                + "uniform mat4 ProjMtx;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n"
                + "}\n";
    }

    protected String fragmentShaderGlsl120() {
        return data.glslVersion + "\n"
                + "#ifdef GL_ES\n"
                + "    precision mediump float;\n"
                + "#endif\n"
                + "uniform sampler2D Texture;\n"
                + "varying vec2 Frag_UV;\n"
                + "varying vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    gl_FragColor = Frag_Color * texture2D(Texture, Frag_UV.st);\n"
                + "}\n";
    }

    protected String fragmentShaderGlsl130() {
        return data.glslVersion + "\n"
                + "uniform sampler2D Texture;\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n"
                + "}\n";
    }

    protected String fragmentShaderGlsl300es() {
        return data.glslVersion + "\n"
                + "precision mediump float;\n"
                + "uniform sampler2D Texture;\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "layout (location = 0) out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n"
                + "}\n";
    }

    protected String fragmentShaderGlsl410Core() {
        return data.glslVersion + "\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "uniform sampler2D Texture;\n"
                + "layout (location = 0) out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n"
                + "}\n";
    }
}
