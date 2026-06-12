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
import org.lwjgl.glfw.GLFW;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author capdevon
 */
public class JmeImGui {

    private static final Logger logger = Logger.getLogger(JmeImGui.class.getName());

    public static final float DEFAULT_FPS = 1f / 60f; // 60 FPS target

    private JmeContext context;
    private ViewPort viewPort;

    private ImGuiImplGl3 imGuiGl3;
    private ImGuiImplGles3 imGuiGles3;
    private ImGuiPlatformBackend platformBackend;
    private boolean isAngleMode;
    private boolean initialized = false;

    public void init(JmeContext context, ViewPort viewPort, boolean useGlfwBackend) {
        if (!(context instanceof LwjglWindow)) {
            throw new IllegalStateException("JmeImGui requires a context of type LwjglWindow.");
        }
        this.viewPort = viewPort;
        this.context = context;
        long windowHandle = ((LwjglWindow) context).getWindowHandle();

        AppSettings settings = context.getSettings();
        isAngleMode = settings.getRenderer().equals("ANGLE_GLES3");

        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);

        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        if (useGlfwBackend) {
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        }

        io.getFonts().build();

        ImGuiTheme.apply(ImGuiTheme.Theme.CLASSIC);

        if (useGlfwBackend) {
            platformBackend = new ImGuiGlfwBackend();
            platformBackend.init(windowHandle);
            logger.log(Level.INFO, "ImGui: Using GLFW Platform Backend");
        } else {
            logger.log(Level.INFO, "ImGui: Using custom JmeImGuiInput (no platform backend)");
        }

        if (!isAngleMode) {
            imGuiGl3 = new ImGuiImplGl3();
            imGuiGl3.init(getGlslVersion());
            imGuiGl3.newFrame();
        } else {
            imGuiGles3 = new ImGuiImplGles3();
            imGuiGles3.init("#version 300 es");
            imGuiGles3.newFrame();
        }
        initialized = true;
    }

    /**
     * Starts a new ImGui frame. Call this at the beginning of the application's
     * update/render loop, before defining any ImGui UI.
     */
    public void startFrame() {
        if (!initialized) {
            return;
        }

        ImGuiIO io = ImGui.getIO();

        Camera cam = viewPort.getCamera();
        int displayW = cam.getWidth();
        int displayH = cam.getHeight();
        io.setDisplaySize(displayW, displayH);

        // --------------------------------------------------------------------
        // jME 3.9.0-stable:
        //     Use fixed framebuffer scale (1.0).
        //
        // jME 3.10+:
        //     Uncomment the HiDPI framebuffer scale calculation below.
        //     Comment out the fixed scale assignment.
        // --------------------------------------------------------------------

        // Framebuffer scale (monitor HiDPI/Retina)
//        float fbW = viewPort.getRenderTargetWidth();
//        float fbH = viewPort.getRenderTargetHeight();
//
//        float fbScaleX = Math.max(Math.round(fbW / displayW), 1);
//        float fbScaleY = Math.max(Math.round(fbH / displayH), 1);
//        io.setDisplayFramebufferScale(fbScaleX, fbScaleY);

        float fbScaleX = 1f;
        float fbScaleY = 1f;
        io.setDisplayFramebufferScale(fbScaleX, fbScaleY);

        float tpf = context.getTimer().getTimePerFrame();
        if (tpf <= 0.0f) tpf = DEFAULT_FPS;
        io.setDeltaTime(Math.min(tpf, 0.1f));

        if (platformBackend != null) {
            platformBackend.newFrame();
        }
        ImGui.newFrame();
    }

    /**
     * Ends the current ImGui frame and prepares the draw data.
     * Call this after defining all ImGui UI elements for the frame.
     */
    public void endFrame() {
        if (!initialized) {
            return;
        }

        try {
            ImGui.render();

            if (!isAngleMode) {
                imGuiGl3.renderDrawData(ImGui.getDrawData());
            } else {
                imGuiGles3.renderDrawData(ImGui.getDrawData());
            }

            // TODO: not ported — multi-viewport (ImGui_ImplSDL3_InitMultiViewportSupport) requires C function pointers.
            // Optional handling for multiple viewports (if enabled in IO flags)
            if (platformBackend != null && ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Releases all resources used by ImGui (context, backends).
     * Call this method when the application terminates or ImGui is no longer needed.
     */
    public void dispose() {
        if (!initialized) {
            return;
        }

        try {
            if (platformBackend != null) {
                platformBackend.dispose();
                platformBackend = null;
            }

            if (isAngleMode) {
                imGuiGles3.shutdown();
            } else {
                imGuiGl3.shutdown();
            }
            ImGui.destroyContext();

            initialized = false;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Chooses the appropriate GLSL version string based on the operating system.
     *
     * @return The GLSL version string to use.
     */
    private String getGlslVersion() {
        return JmeSystem.getPlatform().getOs() == Platform.Os.MacOS ? "#version 150" : "#version 330";
    }

    public boolean isInitialized() {
        return initialized;
    }
}
