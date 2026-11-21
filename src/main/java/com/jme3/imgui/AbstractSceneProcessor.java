package com.jme3.imgui;

import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract {@link SceneProcessor} that manages the initialization, cleanup and
 * enable/disable lifecycle of its subclasses.
 *
 * @author capdevon
 */
public abstract class AbstractSceneProcessor implements SceneProcessor {

    private static final Logger log = Logger.getLogger(AbstractSceneProcessor.class.getName());

    private RenderManager renderManager;
    private ViewPort viewPort;
    private boolean enabled = true;
    private boolean initialized = false;

    @Override
    public void setProfiler(AppProfiler profiler) {
        // no-op
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        log.log(Level.FINEST, "initialize():{0}", this);
        this.renderManager = rm;
        this.viewPort = vp;

        this.initialized = true;
        onInitialize();

        if (isEnabled()) {
            log.log(Level.FINEST, "onEnable():{0}", this);
            onEnable();
        }
    }

    @Override
    public void cleanup() {
        log.log(Level.FINEST, "cleanup():{0}", this);

        if (isEnabled()) {
            log.log(Level.FINEST, "onDisable():{0}", this);
            onDisable();
        }
        onCleanup();
        initialized = false;
    }

    /**
     * @return {@code true} if initialized and not yet cleaned up
     */
    @Override
    public final boolean isInitialized() {
        return initialized;
    }

    /**
     * @return {@code true} if this processor is enabled
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables this processor. Does nothing if the value is unchanged.
     * When not initialized, the change is deferred until {@link #initialize}
     *
     * @param enabled {@code true} to enable the processor, {@code false} to disable it
     */
    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;

        this.enabled = enabled;

        if (!isInitialized())
            return;

        if (enabled) {
            log.log(Level.FINEST, "onEnable():{0}", this);
            onEnable();
        } else {
            log.log(Level.FINEST, "onDisable():{0}", this);
            onDisable();
        }
    }

    /**
     * @return the render manager, or {@code null} if not initialized
     */
    public final RenderManager getRenderManager() {
        return renderManager;
    }

    /**
     * @return the attached viewport, or {@code null} if not initialized
     */
    public final ViewPort getViewPort() {
        return viewPort;
    }

    /**
     * Called once when the processor is initialized.
     */
    protected abstract void onInitialize();

    /**
     * Called once when the processor is cleaned up.
     */
    protected abstract void onCleanup();

    /**
     * Called when the processor becomes enabled while initialized.
     */
    protected abstract void onEnable();

    /**
     * Called when the processor becomes disabled while initialized.
     */
    protected abstract void onDisable();

}
