package fr.lesageolivier.projectperspective;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Classe représentant une "solution"
 *
 * Une solution est une vue secondaire de la scène, qui dessine en blanc (ou en couleur) une zone.
 * Cette zone doit être cliqué pour résoudre la scène.
 *
 * @author Olivier LESAGE
 */
public class PerspectiveSolution {
    /**
     * Caméra utilisée pour faire le rendu
     */
    private PerspectiveCamera camera;

    /**
     * Mesh déssiné dans la solution
     */
    private PerspectiveMesh mesh;

    /**
     * Shader utilisé pour faire le rendu du {@link #mesh}
     */
    private PerspectiveShader shader;

    /**
     * Texture utilisé pour le rendu du {@link #mesh}
     */
    private PerspectiveTexture texture;

    /**
     * FrameBuffer dans lequel la solution va être rendue
     */
    private int[] fboMain;

    /**
     * Entier désignant la texture dans lequel la solution va être rendue
     */
    private int[] textureId;

    /**
     * Entier désignant le RenderBuffer
     */
    private int[] renderBufferId;

    /**
     * Tableau contenant les pixel du buffer
     */
    private int[] data;

    /**
     * Largeur du rendu
     */
    private int width;

    /**
     * Hauteur du rendu
     */
    private int height;

    /**
     * Constructeur
     *
     * @param width Largeur du rendu
     * @param height Hauteur du rendu
     * @param camera Caméra utilisée pour faire le rendu
     * @param mesh Mesh déssiné dans la solution
     * @param texture Texture contenant les cibles de la solution
     * @param shader Shader utilisé pour faire le rendu
     */
    public PerspectiveSolution(int width, int height, PerspectiveCamera camera, PerspectiveMesh mesh, PerspectiveTexture texture, PerspectiveShader shader) {
        this.camera = camera;
        this.mesh = mesh;
        this.texture = texture;
        this.shader = shader;

        this.fboMain = new int[1];
        this.textureId = new int[1];
        this.renderBufferId = new int[1];

        this.width = width;
        this.height = height;

        // create framebuffers
        GLES20.glGenFramebuffers(this.fboMain.length, this.fboMain, 0);

        // create texture object
        GLES20.glGenTextures(1, textureId, 0);

        // create render buffer
        GLES20.glGenRenderbuffers(1, renderBufferId, 0);

        // Bind Frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.fboMain[0]);

        // Bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        this.data = new int[this.width * this.height];
        IntBuffer texBuffer;
        texBuffer = ByteBuffer.allocateDirect(this.data.length*4).order(ByteOrder.nativeOrder()).asIntBuffer();


        // Texture parameters
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0,
                GLES20.GL_RGBA, this.width, this.height, 0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, texBuffer);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_GENERATE_MIPMAP_HINT,
                GLES20.GL_TRUE); // automatic mipmap

        // Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferId[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, this.width, this.height);

        // Attach texture FBO color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId[0], 0);

        // Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderBufferId[0]);

        // Reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    /**
     * Méthode permettant de faire le rendu de la soltion dans le FrameBuffer
     */
    public void draw() {
        // Setup render to texture
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.fboMain[0]);
        GLES20.glViewport(0, 0, this.width, this.height);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Draw
        this.mesh.draw(this.shader, this.texture, this.camera.getMatrix());

        // Draw to buffer
        GLES20.glFlush();

        // switch to screen output
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    /**
     * Méthode permettant de lite la valeur d'un pixel dans le framebuffer
     *
     * @param x Position X du pixel
     * @param y Position Y du pixel
     * @return
     */
    public int readPixel(int x, int y) {
        ByteBuffer ss = ByteBuffer.allocateDirect(4);
        ss.order(ByteOrder.nativeOrder()).asIntBuffer();

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.fboMain[0]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture());

        GLES20.glFlush();


        GLES20.glReadPixels(x, y, 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ss.clear());
        ss.rewind();

        Log.d("BUFFER", "capacity" + ss.capacity());

        byte b[] = new byte[4];
        ss.get(b);

        Log.d("BUFFER", x+  " " +y);
        Log.d("BUFFER", b[0] + " " +b[1] + " " +b[2] + " " +b[3]);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        return 0;
    }

    /**
     * Méthode permettant de récuperer la texture du FrameBuffer
     *
     * @return La texture du FrameBuffer (contenant le rendu de la solution)
     */
    public int getTexture() {
        return this.textureId[0];
    }
}
