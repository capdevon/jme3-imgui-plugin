package com.jme3.imgui;

import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.system.Platform;
import com.jme3.system.lwjgl.LwjglWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.internal.ImGuiContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core manager for Dear ImGui inside the jMonkeyEngine rendering lifecycle.
 * Handles context bootstrap, multi-backend abstraction (GL3 vs ANGLE GLES3),
 * frame sizing calculations, and viewport rendering.
 *
 * @author capdevon
 */
public class JmeImGui {

    private static final Logger logger = Logger.getLogger(JmeImGui.class.getName());

    private ViewPort viewPort;

    private ImGuiContext context;
    private ImGuiImplGl3 imGuiGl3;
    private ImGuiImplGles3 imGuiGles3;
    private ImGuiPlatformBackend platformBackend;
    private boolean useGles3Renderer;
    private boolean initialized = false;

    /**
     * Initializes ImGui context, input backend, and renderer interface.
     *
     * @param jmeContext the jME context (must be an instance of {@link LwjglWindow})
     * @param viewPort   the main viewport for display metrics
     * @param cfg        configuration options for ImGui initialization
     */
    public void init(JmeContext jmeContext, ViewPort viewPort, JmeImGuiConfig cfg) {
        if (initialized) {
            throw new IllegalStateException("JmeImGui is already initialized");
        }
        this.viewPort = viewPort;

        // Bootstrap requested windowing abstractions
        LwjglWindow window = (LwjglWindow) jmeContext;

        if (cfg.isUseGlfwBackend()) {
            platformBackend = new ImGuiGlfwBackend(window);
            logger.log(Level.INFO, "ImGui: Using GLFW Platform Backend");
        } else {
            platformBackend = new ImGuiSdlBackend(window);
            logger.log(Level.INFO, "ImGui: Using SDL3 Platform Backend");

            AppSettings settings = jmeContext.getSettings();
            useGles3Renderer = JmeGraphicsApi.forRenderer(settings.getRenderer()) == JmeGraphicsApi.OPENGL_ES;
        }

        this.context = ImGui.createContext();
        applyConfig(cfg);
        platformBackend.init();
        initImGuiRenderer();
        initialized = true;
    }

    private void applyConfig(JmeImGuiConfig cfg) {
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(cfg.getIniFilename());

        // Let frame exceptions reach the host instead of imgui-java calling System.exit(1)
        // on error-recovery assertions.
        io.setConfigErrorRecoveryEnableAssert(false);

        if (cfg.isEnableDocking()) {
            io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        }
        if (cfg.isEnableViewports() && platformBackend.supportsViewports()) {
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        }

        io.getFonts().build();

        if (cfg.getTheme() != null) {
            ImGuiTheme.apply(cfg.getTheme());
        }
    }

    private void initImGuiRenderer() {
        // Bootstrap appropriate rendering pipeline based on jME platform configuration
        if (useGles3Renderer) {
            imGuiGles3 = new ImGuiImplGles3();
            if (!imGuiGles3.init()) {
                throw new IllegalStateException("Dear ImGui OpenGL ES 3 renderer backend failed to initialize");
            }
        } else {
            imGuiGl3 = new ImGuiImplGl3();
            if (!imGuiGl3.init(getGlslVersion())) {
                throw new IllegalStateException("Dear ImGui OpenGL renderer backend failed to initialize");
            }
        }
    }

    /**
     * Starts a new ImGui frame. Call this at the beginning of the application's
     * update/render loop, before defining any ImGui UI.
     */
    public void startFrame() {
        checkInitialized();

        if (useGles3Renderer) {
            imGuiGles3.newFrame();
        } else {
            imGuiGl3.newFrame();
        }
        platformBackend.newFrame();

        Camera cam = viewPort.getCamera();
        ImGui.getIO().setDisplaySize(cam.getWidth(), cam.getHeight());

        ImGui.newFrame();
    }

    /**
     * Ends the current ImGui frame and prepares the draw data.
     * Call this after defining all ImGui UI elements for the frame.
     */
    public void endFrame() {
        checkInitialized();

        ImGui.render();

        // Delegate draw calls to target hardware interface pipeline
        if (useGles3Renderer) {
            imGuiGles3.renderDrawData(ImGui.getDrawData());
        } else {
            imGuiGl3.renderDrawData(ImGui.getDrawData());
        }

        // TODO: not ported — multi-viewport (ImGui_ImplSDL3_InitMultiViewportSupport) requires C function pointers.
        platformBackend.renderPlatformWindows();
    }

    /**
     * Releases all resources used by ImGui (context, backends).
     * Call this method when the application terminates or ImGui is no longer needed.
     */
    public void dispose() {
        if (!initialized) {
            return;
        }

        initialized = false;

        // Cleanup: renderer backend → platform backend → ImGui context
        if (useGles3Renderer) {
            imGuiGles3.shutdown();
        } else {
            imGuiGl3.shutdown();
        }
        platformBackend.dispose();
        ImGui.destroyContext(context);
    }

    /**
     * Checks if ImGui has been successfully initialized.
     */
    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("JmeImGui is not initialized");
        }
    }

    /**
     * Determines the appropriate GLSL version string based on the current OS.
     *
     * @return The GLSL version string to use.
     */
    private static String getGlslVersion() {
        return JmeSystem.getPlatform().getOs() == Platform.Os.MacOS ? "#version 150" : "#version 330";
    }

    /**
     * Checks if ImGui has been successfully initialized.
     *
     * @return {@code true} if initialized; {@code false} otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
}
