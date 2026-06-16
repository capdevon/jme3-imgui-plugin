package jme3examples;

import com.jme3.imgui.ImGuiWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiMouseButton;
import imgui.type.ImBoolean;
import imgui.type.ImString;

/**
 * @author capdevon
 */
public class TestWindow extends ImGuiWindow {

    private ImBoolean checked = new ImBoolean(false);
    private float[] value = new float[]{50f};
    private ImString buffer = new ImString(32);

    public TestWindow() {
        super("Test Window");
    }

    @Override
    public void render() {
//        ImGui.setNextWindowPos(10, 10);
//        ImGui.setNextWindowSize(400, 200);

        if (ImGui.begin(getId())) {
            ImGui.text("Hello ImGui + jMonkeyEngine!");
            ImGui.separator();

            ImGuiIO io = ImGui.getIO();
            ImGui.text("ImGui Version: " + ImGui.getVersion());
            ImGui.text("FPS: " + String.format("%.2f", io.getFramerate()));
            ImGui.text("Display=" + io.getDisplaySizeX() + " x " + io.getDisplaySizeY());
            ImGui.text("Mouse= " + io.getMousePosX() + ", " + io.getMousePosY());
            ImGui.text("MouseDown=" + io.getMouseDown(ImGuiMouseButton.Left));

            if (ImGui.button("Button 1")) {
                System.out.println("Button Clicked!");
            }

            ImGui.sliderFloat("Slider", value, 0f, 100f);

            ImGui.inputText("Text", buffer);

            ImGui.checkbox("Check", checked);
        }
        ImGui.end();
    }
}