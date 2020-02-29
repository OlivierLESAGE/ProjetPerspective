package fr.lesageolivier.projectperspective;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Classe représentant une texture utilisant OpenGL ES
 *
 * @author Olivier LESAGE
 */
public class PerspectiveTexture {

    /**
     * Entier permettant d'identifier la texture une fois chargé par OpenGL
     */
    private int textureHandle[];

    /**
     * Constructeur
     *
     * @param image La texture
     */
    public PerspectiveTexture(Bitmap image) {
        this.textureHandle = new int[1];

        GLES20.glGenTextures(1, this.textureHandle, 0);

        if (this.textureHandle[0] != 0) {
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            image.recycle();
        }

        if (this.textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
    }

    /**
     * Getter sur l'attribut {@link #textureHandle}
     *
     * @return Un nntier permettant d'identifier la texture
     */
    public int getHandle() {
        return this.textureHandle[0];
    }
}
