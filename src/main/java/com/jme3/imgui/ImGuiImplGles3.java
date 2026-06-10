package com.jme3.imgui;

import imgui.ImDrawData;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec4;

import imgui.flag.ImGuiBackendFlags;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLESCapabilities;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_POLYGON_MODE;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_BOX;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_ACTIVE_TEXTURE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_BLEND_DST_ALPHA;
import static org.lwjgl.opengl.GL14.GL_BLEND_DST_RGB;
import static org.lwjgl.opengl.GL14.GL_BLEND_SRC_ALPHA;
import static org.lwjgl.opengl.GL14.GL_BLEND_SRC_RGB;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.GL_BLEND_EQUATION_ALPHA;
import static org.lwjgl.opengl.GL20.GL_BLEND_EQUATION_RGB;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.glBlendEquationSeparate;
import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;
import static org.lwjgl.opengl.GL33.GL_SAMPLER_BINDING;
import static org.lwjgl.opengl.GL33.glBindSampler;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.*;

public class ImGuiImplGles3 {

    public boolean init(final String glslVersion) {
        // TODO: add gles3 implementation
        return false;
    }

    public void shutdown() {
//        final ImGuiIO io = ImGui.getIO();
//
//        shutdownPlatformInterface();
//        destroyDeviceObjects();
//
//        io.setBackendRendererName(null);
//        // In C++: io.BackendFlags also clears RendererHasTextures, then platform_io.ClearRendererHandlers() runs.
//        // In Java: RendererHasTextures is never set (see init), and ClearRendererHandlers is not exposed in imgui-binding (follow-up).
//        io.removeBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset | ImGuiBackendFlags.RendererHasViewports);
//        data = null;
    }

    public void newFrame() {
//        if (data.shaderHandle == 0) {
//            createDeviceObjects();
//        }
//        if (data.fontTexture == 0) {
//            createFontsTexture();
//        }
    }

    public void renderDrawData(final ImDrawData drawData) {
//        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
//        final int fbWidth = (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
//        final int fbHeight = (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());
//        if (fbWidth <= 0 || fbHeight <= 0) {
//            return;
//        }
//
//        if (drawData.getCmdListsCount() <= 0) {
//            return;
//        }
//
//        // In C++: iterates draw_data->Textures and calls ImGui_ImplOpenGL3_UpdateTexture for each non-OK status.
//        // In Java: ImTextureData is not exposed in imgui-binding (follow-up); we keep the legacy createFontsTexture
//        //          path triggered from newFrame(), so dynamic atlas updates are not honored here yet.
//
//        glGetIntegerv(GL_ACTIVE_TEXTURE, props.lastActiveTexture);
//        glActiveTexture(GL_TEXTURE0);
//        glGetIntegerv(GL_CURRENT_PROGRAM, props.lastProgram);
//        glGetIntegerv(GL_TEXTURE_BINDING_2D, props.lastTexture);
//        if (data.hasBindSampler) {
//            glGetIntegerv(GL_SAMPLER_BINDING, props.lastSampler);
//        }
//        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, props.lastArrayBuffer);
//        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, props.lastVertexArrayObject);
//        if (data.hasPolygonMode) {
//            glGetIntegerv(GL_POLYGON_MODE, props.lastPolygonMode);
//        }
//        glGetIntegerv(GL_VIEWPORT, props.lastViewport);
//        glGetIntegerv(GL_SCISSOR_BOX, props.lastScissorBox);
//        glGetIntegerv(GL_BLEND_SRC_RGB, props.lastBlendSrcRgb);
//        glGetIntegerv(GL_BLEND_DST_RGB, props.lastBlendDstRgb);
//        glGetIntegerv(GL_BLEND_SRC_ALPHA, props.lastBlendSrcAlpha);
//        glGetIntegerv(GL_BLEND_DST_ALPHA, props.lastBlendDstAlpha);
//        glGetIntegerv(GL_BLEND_EQUATION_RGB, props.lastBlendEquationRgb);
//        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, props.lastBlendEquationAlpha);
//        props.lastEnableBlend = glIsEnabled(GL_BLEND);
//        props.lastEnableCullFace = glIsEnabled(GL_CULL_FACE);
//        props.lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST);
//        props.lastEnableStencilTest = glIsEnabled(GL_STENCIL_TEST);
//        props.lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST);
//        if (!data.glProfileIsES3 && data.glVersion >= 310) {
//            props.lastEnablePrimitiveRestart = glIsEnabled(GL_PRIMITIVE_RESTART);
//        }
//
//        // Setup desired GL state
//        // Recreate the VAO every time (this is to easily allow multiple GL contexts to be rendered to. VAO are not shared among GL contexts)
//        // The renderer would actually work without any VAO bound, but then our VertexAttrib calls would overwrite the default one currently bound.
//        final int vertexArrayObject = glGenVertexArrays();
//        setupRenderState(drawData, fbWidth, fbHeight, vertexArrayObject);
//
//        // Will project scissor/clipping rectangles into framebuffer space
//        final float clipOffX = drawData.getDisplayPosX(); // (0,0) unless using multi-viewports
//        final float clipOffY = drawData.getDisplayPosY(); // (0,0) unless using multi-viewports
//        final float clipScaleX = drawData.getFramebufferScaleX(); // (1,1) unless using retina display which are often (2,2)
//        final float clipScaleY = drawData.getFramebufferScaleY(); // (1,1) unless using retina display which are often (2,2)
//
//        // Render command lists
//        for (int n = 0; n < drawData.getCmdListsCount(); n++) {
//            // FIXME: this is a straightforward port from Dear ImGui and it doesn't work with multi-viewports.
//            //        So we keep solution we used before.
//            // Upload vertex/index buffers
//            // final int vtxBufferSize = drawData.getCmdListVtxBufferSize(n) * ImDrawData.sizeOfImDrawVert();
//            // final int idxBufferSize = drawData.getCmdListIdxBufferSize(n) * ImDrawData.sizeOfImDrawIdx();
//            // if (data.vertexBufferSize < vtxBufferSize) {
//            //     data.vertexBufferSize = vtxBufferSize;
//            //     glBufferData(GL_ARRAY_BUFFER, data.vertexBufferSize, GL_STREAM_DRAW);
//            // }
//            // if (data.indexBufferSize < idxBufferSize) {
//            //     data.indexBufferSize = idxBufferSize;
//            //     glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.indexBufferSize, GL_STREAM_DRAW);
//            // }
//            // glBufferSubData(GL_ARRAY_BUFFER, 0, drawData.getCmdListVtxBufferData(n));
//            // glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, drawData.getCmdListIdxBufferData(n));
//
//            // In C++: also has an UseBufferSubData branch (orphaning + glBufferSubData).
//            // In Java: upstream forces UseBufferSubData = false, so we mirror only the glBufferData path here.
//            glBufferData(GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(n), GL_STREAM_DRAW);
//            glBufferData(GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(n), GL_STREAM_DRAW);
//
//            for (int cmdIdx = 0; cmdIdx < drawData.getCmdListCmdBufferSize(n); cmdIdx++) {
//                // TODO:
//                // if userCallback
//                // else
//
//                drawData.getCmdListCmdBufferClipRect(props.clipRect, n, cmdIdx);
//
//                final float clipMinX = (props.clipRect.x - clipOffX) * clipScaleX;
//                final float clipMinY = (props.clipRect.y - clipOffY) * clipScaleY;
//                final float clipMaxX = (props.clipRect.z - clipOffX) * clipScaleX;
//                final float clipMaxY = (props.clipRect.w - clipOffY) * clipScaleY;
//
//                if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) {
//                    continue;
//                }
//
//                // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
//                glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));
//
//                // Bind texture, Draw
//                final long textureId = drawData.getCmdListCmdBufferTextureId(n, cmdIdx);
//                final int elemCount = drawData.getCmdListCmdBufferElemCount(n, cmdIdx);
//                final int idxOffset = drawData.getCmdListCmdBufferIdxOffset(n, cmdIdx);
//                final int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(n, cmdIdx);
//                final long indices = idxOffset * (long) ImDrawData.sizeOfImDrawIdx();
//                final int type = ImDrawData.sizeOfImDrawIdx() == 2 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;
//
//                glBindTexture(GL_TEXTURE_2D, (int) textureId);
//
//                if (data.glVersion >= 320) {
//                    glDrawElementsBaseVertex(GL_TRIANGLES, elemCount, type, indices, vtxOffset);
//                } else {
//                    glDrawElements(GL_TRIANGLES, elemCount, type, indices);
//                }
//            }
//        }
//
//        // Destroy the temporary VAO
//        glDeleteVertexArrays(vertexArrayObject);
//
//        // Restore modified GL state
//        // This "glIsProgram()" check is required because if the program is "pending deletion" at the time of binding backup, it will have been deleted by now and will cause an OpenGL error. See #6220.
//        if (props.lastProgram[0] == 0 || glIsProgram(props.lastProgram[0])) {
//            glUseProgram(props.lastProgram[0]);
//        }
//        glBindTexture(GL_TEXTURE_2D, props.lastTexture[0]);
//        if (data.hasBindSampler) {
//            glBindSampler(0, props.lastSampler[0]);
//        }
//        glActiveTexture(props.lastActiveTexture[0]);
//        glBindVertexArray(props.lastVertexArrayObject[0]);
//        glBindBuffer(GL_ARRAY_BUFFER, props.lastArrayBuffer[0]);
//        glBlendEquationSeparate(props.lastBlendEquationRgb[0], props.lastBlendEquationAlpha[0]);
//        glBlendFuncSeparate(props.lastBlendSrcRgb[0], props.lastBlendDstRgb[0], props.lastBlendSrcAlpha[0], props.lastBlendDstAlpha[0]);
//        if (props.lastEnableBlend) glEnable(GL_BLEND);
//        else glDisable(GL_BLEND);
//        if (props.lastEnableCullFace) glEnable(GL_CULL_FACE);
//        else glDisable(GL_CULL_FACE);
//        if (props.lastEnableDepthTest) glEnable(GL_DEPTH_TEST);
//        else glDisable(GL_DEPTH_TEST);
//        if (props.lastEnableStencilTest) glEnable(GL_STENCIL_TEST);
//        else glDisable(GL_STENCIL_TEST);
//        if (props.lastEnableScissorTest) glEnable(GL_SCISSOR_TEST);
//        else glDisable(GL_SCISSOR_TEST);
//        if (!data.glProfileIsES3 && data.glVersion >= 310) {
//            if (props.lastEnablePrimitiveRestart) {
//                glEnable(GL_PRIMITIVE_RESTART);
//            } else {
//                glDisable(GL_PRIMITIVE_RESTART);
//            }
//        }
//        // Desktop OpenGL 3.0 and OpenGL 3.1 had separate polygon draw modes for front-facing and back-facing faces of polygons
//        if (data.hasPolygonMode) {
//            if (data.glVersion <= 310 || data.glProfileIsCompat) {
//                glPolygonMode(GL_FRONT, props.lastPolygonMode[0]);
//                glPolygonMode(GL_BACK, props.lastPolygonMode[1]);
//            } else {
//                glPolygonMode(GL_FRONT_AND_BACK, props.lastPolygonMode[0]);
//            }
//        }
//        glViewport(props.lastViewport[0], props.lastViewport[1], props.lastViewport[2], props.lastViewport[3]);
//        glScissor(props.lastScissorBox[0], props.lastScissorBox[1], props.lastScissorBox[2], props.lastScissorBox[3]);
    }
}
