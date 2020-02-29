package fr.lesageolivier.projectperspective;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Classe repésentant le renderer de la SurfaceView {@link PerspectiveSurfaceView}
 *
 * @author Olivier LESAGE
 */
public class PerspectiveRenderer implements GLSurfaceView.Renderer {
    /**
     * Scene déssinée par le renderer
     */
    private Scene scene;

    /**
     * Caméra utilisée pour faire le rendu
     */
    private PerspectiveCamera camera;

    /**
     * Mesh déssiné par le renderer
     */
    private PerspectiveMesh mesh;

    /**
     * Shader utilisé pour faire le rendu du {@link #mesh mesh}
     */
    private PerspectiveShader shader;

    /**
     * Texture utilisé pour le rendu du {@link #mesh mesh}
     */
    private PerspectiveTexture texture;

    //private PerspectiveSolution solution;

    /**
     * Largeur du rendu en pixel
     */
    private int width;

    /**
     * Hauteur du rendu en pixel
     */
    private int height;

    /**
     * Constructeur
     *
     * @param scene  Scene qui va être affichée par le renderer
     * @param camera Caméra utilisé pour faire le rendu
     */
    public PerspectiveRenderer(Scene scene, PerspectiveCamera camera) {
        this.scene = scene;
        this.camera = camera;

        this.camera.update();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(this.scene.getRed(), this.scene.getGreen(), this.scene.getBlue(), 1.0f);

        this.mesh = PerspectiveMesh.fromObjFile(this.scene.getObjet().getMesh());
        this.shader = new PerspectiveShader(
                this.scene.getObjet().getShader().getVertex(),
                this.scene.getObjet().getShader().getFragment());
        this.texture = new PerspectiveTexture(this.scene.getObjet().getTexture());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;

        //this.solution = new PerspectiveSolution(width, height, this.camera, this.mesh, new PerspectiveTexture(this.scene.getTextureTarget()), this.shader);

        this.camera.onresize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(this.scene.getRed(), this.scene.getGreen(), this.scene.getBlue(), 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        /*if (this.solution != null) {
            this.solution.draw();
        }
        if (this.solution != null)
            this.mesh.draw(this.shader, this.solution.getTexture(), this.camera.getMatrix());
        else*/

        this.mesh.draw(this.shader, this.texture, this.camera.getMatrix());
    }

    /*public PerspectiveSolution getSolution() {
        return this.solution;
    }*/
}
