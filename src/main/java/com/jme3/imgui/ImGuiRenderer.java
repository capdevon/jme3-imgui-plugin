package com.jme3.imgui;

import com.jme3.system.lwjgl.LwjglWindow;
import imgui.ImDrawData;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.internal.ImGuiContext;

import java.util.Objects;

/**
 * Encapsulates a Dear ImGui platform backend + renderer backend pair, and owns the
 * Dear ImGui context.
 * Not thread-safe: use on the jME render thread.
 *
 * @author capdevon
 */
public abstract class ImGuiRenderer {

    private final ImGuiPlatformBackend platform;

    private ImGuiContext context;
    private boolean initialized = false;

    protected ImGuiRenderer(ImGuiPlatformBackend platform) {
        this.platform = Objects.requireNonNull(platform, "platform");
    }

    /**
     * Picks the platform backend from the config and the graphics variant from the jME renderer setting.
     */
    public static ImGuiRenderer create(LwjglWindow window, boolean useGlfwBackend, boolean gles3) {
        ImGuiPlatformBackend platform = useGlfwBackend
                ? new ImGuiGlfwBackend(window) : new ImGuiSdlBackend(window);
        return gles3 ? new ImGuiGles3Renderer(platform) : new ImGuiGl3Renderer(platform);
    }

    /**
     * Creates the Dear ImGui context, applies the configuration and initializes both backends.
     */
    public final void initialize(JmeImGuiConfig cfg) {
        if (initialized) {
            throw new IllegalStateException("ImGuiRenderer is already initialized");
        }
        this.context = ImGui.createContext();
        applyConfig(cfg);
        platform.init();
        initRendererBackend();
        initialized = true;
    }

    /**
     * Starts the backend frames (graphics + platform).
     * Call before ImGui.newFrame().
     */
    public final void newFrame() {
        checkInitialized();
        rendererNewFrame();
        platform.newFrame();
    }

    /**
     * Draws the current ImGui draw data, then any secondary platform windows.
     * Call after ImGui.render().
     */
    public final void renderDrawData() {
        checkInitialized();
        renderDrawData(ImGui.getDrawData());
        platform.renderPlatformWindows();
    }

    /**
     * Releases backends and context.
     */
    public final void dispose() {
        if (!initialized) {
            return;
        }
        initialized = false;

        // Cleanup: renderer backend → platform backend → ImGui context
        shutdownRendererBackend();
        platform.dispose();
        ImGui.destroyContext(context);

        clearRendererBackend();
        context = null;
    }

    public final boolean isInitialized() {
        return initialized;
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("ImGuiRenderer is not initialized");
        }
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

        if (cfg.isEnableViewports() && platform.supportsViewports()) {
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        }

        ImGuiFontConfig.initFonts(io, cfg);
    }

    // Graphics-side hooks

    protected abstract void initRendererBackend();

    protected abstract void rendererNewFrame();

    protected abstract void renderDrawData(ImDrawData drawData);

    protected abstract void shutdownRendererBackend();

    protected abstract void clearRendererBackend();
}