package com.jme3.imgui;

import com.jme3.input.RawInputListenerAdapter;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import imgui.ImGui;

/**
 * Bridges jMonkeyEngine raw input events with the Dear ImGui input pipeline.
 *
 * @author capdevon
 */
class JmeImGuiInputListener extends RawInputListenerAdapter {

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if (ImGui.getIO().getWantCaptureMouse() && (evt.getDeltaWheel() == 0 || isWheelCaptured())) {
            evt.setConsumed();
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (ImGui.getIO().getWantCaptureMouse()) {
            evt.setConsumed();
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        if (ImGui.getIO().getWantCaptureKeyboard()) {
            evt.setConsumed();
        }
    }

    /**
     * Override to let the wheel reach the game when the hovered ImGui window
     * is not scrollable.
     */
    protected boolean isWheelCaptured() {
        return true;
    }

}