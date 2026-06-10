package jme3examples;

import com.jme3.app.SimpleApplication;
import com.jme3.imgui.ImGuiAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 * @author capdevon
 */
public class TestImGuiApplication extends SimpleApplication {

    public static void main(String[] args) {
        TestImGuiApplication app = new TestImGuiApplication();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        settings.setResolution(640, 480);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    private Geometry geom;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(15f);
        flyCam.setDragToRotate(true);
//        flyCam.setEnabled(false);

//        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        ImGuiAppState imgui = new ImGuiAppState(false);
        stateManager.attach(imgui);

        imgui.getManager().addWindow(new TestWindow());

        Box box = new Box(1f, 1f, 1f);
        geom = makeShape("Box", box, ColorRGBA.Blue);
        rootNode.attachChild(geom);

        System.out.println(cam.getWidth() + " x " + cam.getHeight());
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

}
