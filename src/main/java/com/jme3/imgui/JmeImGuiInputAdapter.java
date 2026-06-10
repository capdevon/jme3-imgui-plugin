package com.jme3.imgui;

import com.jme3.input.Joystick;
import com.jme3.input.JoystickConnectionListener;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiKey;

import java.util.logging.Logger;

/**
 * @author capdevon
 */
public class JmeImGuiInputAdapter implements RawInputListener, JoystickConnectionListener {

    private static final Logger logger = Logger.getLogger(JmeImGuiInputAdapter.class.getName());

    @Override
    public void beginInput() {
    }

    @Override
    public void endInput() {
    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {
    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {
    }

    @Override
    public void onTouchEvent(TouchEvent evt) {
    }

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        ImGuiIO io = ImGui.getIO();
        io.addMousePosEvent(evt.getX(), io.getDisplaySizeY() - evt.getY());

        if (evt.getDeltaWheel() != 0) {
            io.addMouseWheelEvent(0.0f, evt.getDeltaWheel());
        }

        if (io.getWantCaptureMouse()) {
            evt.setConsumed();
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        ImGuiIO io = ImGui.getIO();
        io.addMouseButtonEvent(evt.getButtonIndex(), evt.isPressed());

        if (io.getWantCaptureMouse()) {
            evt.setConsumed();
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        ImGuiIO io = ImGui.getIO();

        int imguiKey = mapKey(evt.getKeyCode());

        if (imguiKey != ImGuiKey.None) {
            io.addKeyEvent(imguiKey, evt.isPressed());
        }

        if (evt.isPressed()) {
            char c = evt.getKeyChar();

            if (c > 0 && c != 65535) {
                io.addInputCharacter(c);
            }
        }

//        updateModifiers(io); // FIXME

        if (io.getWantCaptureKeyboard()) {
            evt.setConsumed();
        }
    }

//    private static void updateModifiers(ImGuiIO io) {
//        io.addKeyEvent(
//                ImGuiKey.ImGuiMod_Ctrl,
//                io.getKeysDown(ImGuiKey.LeftCtrl)
//                        || io.getKeysDown(ImGuiKey.RightCtrl)
//        );
//
//        io.addKeyEvent(
//                ImGuiKey.ImGuiMod_Shift,
//                io.getKeysDown(ImGuiKey.LeftShift)
//                        || io.getKeysDown(ImGuiKey.RightShift)
//        );
//
//        io.addKeyEvent(
//                ImGuiKey.ImGuiMod_Alt,
//                io.getKeysDown(ImGuiKey.LeftAlt)
//                        || io.getKeysDown(ImGuiKey.RightAlt)
//        );
//
//        io.addKeyEvent(
//                ImGuiKey.ImGuiMod_Super,
//                io.getKeysDown(ImGuiKey.LeftSuper)
//                        || io.getKeysDown(ImGuiKey.RightSuper)
//        );
//    }

    private static int mapKey(int key) {

        switch (key) {

            case com.jme3.input.KeyInput.KEY_TAB:
                return ImGuiKey.Tab;

            case com.jme3.input.KeyInput.KEY_LEFT:
                return ImGuiKey.LeftArrow;

            case com.jme3.input.KeyInput.KEY_RIGHT:
                return ImGuiKey.RightArrow;

            case com.jme3.input.KeyInput.KEY_UP:
                return ImGuiKey.UpArrow;

            case com.jme3.input.KeyInput.KEY_DOWN:
                return ImGuiKey.DownArrow;

            case com.jme3.input.KeyInput.KEY_PGUP:
                return ImGuiKey.PageUp;

            case com.jme3.input.KeyInput.KEY_PGDN:
                return ImGuiKey.PageDown;

            case com.jme3.input.KeyInput.KEY_HOME:
                return ImGuiKey.Home;

            case com.jme3.input.KeyInput.KEY_END:
                return ImGuiKey.End;

            case com.jme3.input.KeyInput.KEY_INSERT:
                return ImGuiKey.Insert;

            case com.jme3.input.KeyInput.KEY_DELETE:
                return ImGuiKey.Delete;

            case com.jme3.input.KeyInput.KEY_BACK:
                return ImGuiKey.Backspace;

            case com.jme3.input.KeyInput.KEY_SPACE:
                return ImGuiKey.Space;

            case com.jme3.input.KeyInput.KEY_RETURN:
                return ImGuiKey.Enter;

            case com.jme3.input.KeyInput.KEY_ESCAPE:
                return ImGuiKey.Escape;

            case com.jme3.input.KeyInput.KEY_LCONTROL:
                return ImGuiKey.LeftCtrl;

            case com.jme3.input.KeyInput.KEY_RCONTROL:
                return ImGuiKey.RightCtrl;

            case com.jme3.input.KeyInput.KEY_LSHIFT:
                return ImGuiKey.LeftShift;

            case com.jme3.input.KeyInput.KEY_RSHIFT:
                return ImGuiKey.RightShift;

            case com.jme3.input.KeyInput.KEY_LMENU:
                return ImGuiKey.LeftAlt;

            case com.jme3.input.KeyInput.KEY_RMENU:
                return ImGuiKey.RightAlt;

            case com.jme3.input.KeyInput.KEY_LMETA:
                return ImGuiKey.LeftSuper;

            case com.jme3.input.KeyInput.KEY_RMETA:
                return ImGuiKey.RightSuper;
        }

        if (key >= com.jme3.input.KeyInput.KEY_A
                && key <= com.jme3.input.KeyInput.KEY_Z) {

            return ImGuiKey.A + (key - com.jme3.input.KeyInput.KEY_A);
        }

        if (key >= com.jme3.input.KeyInput.KEY_0
                && key <= com.jme3.input.KeyInput.KEY_9) {

            return ImGuiKey._0 + (key - com.jme3.input.KeyInput.KEY_0);
        }

        return ImGuiKey.None;
    }

    public void setFocused(boolean focused) {
        ImGui.getIO().addFocusEvent(focused);
    }

    @Override
    public void onConnected(Joystick joystick) {
        logger.info("Joystick connected: " + joystick);
    }

    @Override
    public void onDisconnected(Joystick joystick) {
        logger.info("Joystick disconnected: " + joystick);
    }
}