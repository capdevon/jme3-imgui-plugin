package jme3examples;

import com.jme3.imgui.ImGuiWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

/**
 * @author capdevon
 */
public class TestWindow extends ImGuiWindow {

    private final String[] themeOptions = {"Dark", "Light", "Classic"};
    private final ImInt colorChoice = new ImInt(2);

    private static final ImBoolean SHOW_DEMO_WINDOW = new ImBoolean(false);

    public TestWindow() {
        super("Test Window");
    }

    @Override
    public void render() {
        ImGui.setNextWindowPos(10, 10, ImGuiCond.Once);
//        ImGui.setNextWindowSize(400, 200);

        if (ImGui.begin(getId())) {
            ImGui.text("Hello ImGui + jMonkeyEngine!");
            ImGui.separator();

            ImGuiIO io = ImGui.getIO();
            ImGui.text("ImGui Version: " + ImGui.getVersion());
            ImGui.text("FPS: " + String.format("%.2f", io.getFramerate()));
            ImGui.text("Display=" + io.getDisplaySizeX() + " x " + io.getDisplaySizeY());
            ImGui.text("Mouse= " + io.getMousePosX() + ", " + io.getMousePosY());

            sidebarStyleSection();

            ImGui.checkbox("Show Demo Window", SHOW_DEMO_WINDOW);
            if (SHOW_DEMO_WINDOW.get()) {
                ImGui.showDemoWindow(SHOW_DEMO_WINDOW);
            }
        }
        ImGui.end();
    }

    private void sidebarStyleSection() {
        if (ImGui.combo("Color Theme", colorChoice, themeOptions, themeOptions.length)) {
            if (colorChoice.get() == 0) {
                ImGui.styleColorsDark();
            } else if (colorChoice.get() == 1) {
                ImGui.styleColorsLight();
            } else if (colorChoice.get() == 2) {
                ImGui.styleColorsClassic();
            }
        }
    }
}