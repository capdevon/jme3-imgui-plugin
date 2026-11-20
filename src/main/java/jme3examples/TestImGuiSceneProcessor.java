package jme3examples;

import com.jme3.app.SimpleApplication;
import com.jme3.imgui.JmeImGuiConfig;
import com.jme3.imgui.JmeImGuiSceneProcessor;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 * @author capdevon
 */
public class TestImGuiSceneProcessor extends SimpleApplication {

    public static void main(String[] args) {
        TestImGuiSceneProcessor app = new TestImGuiSceneProcessor();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
//        settings.setRenderer(AppSettings.ANGLE_GLES3);
        settings.setResolution(800, 600);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    private JmeImGuiSceneProcessor imGuiProcessor;
    private Geometry geom;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(15f);
        flyCam.setDragToRotate(true);

        JmeImGuiConfig cfg = new JmeImGuiConfig();
//        cfg.setUseGlfwBackend(true);

        imGuiProcessor = new JmeImGuiSceneProcessor(this, cfg);
        imGuiProcessor.addWindow(new TestWindow());
        guiViewPort.addProcessor(imGuiProcessor);

        Box box = new Box(1f, 1f, 1f);
        geom = makeShape("Box", box, ColorRGBA.Blue);
        rootNode.attachChild(geom);

        inputManager.addMapping("toggleUI", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
            if (isPressed) {
                boolean enabled = imGuiProcessor.isEnabled();
                imGuiProcessor.setEnabled(!enabled);
            }
        }, "toggleUI");
    }

    private Geometry makeShape(String name, Mesh mesh, ColorRGBA color) {
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        return geom;
    }

    @Override
    public void simpleUpdate(float tpf) {
        geom.rotate(0, tpf, 0);
    }

    @Override
    public void destroy() {
        guiViewPort.removeProcessor(imGuiProcessor);
        super.destroy();
    }

}
