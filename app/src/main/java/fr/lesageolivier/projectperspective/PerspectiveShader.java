package fr.lesageolivier.projectperspective;

import android.opengl.GLES20;

/**
 * Classe représentant un shader utilisant OpenGL ES
 *
 * @author Olivier LESAGE
 */
public class PerspectiveShader {
    /**
     * Entier permettant d'identifier le shader une fois chargé par OpenGL
     */
    private int mProgram;

    /**
     * Constructeur
     *
     * @param vertex Vertex shader, décrit la projection des points
     * @param fragment Fragment shader, décrit comment sont colorisés les points
     */
    public PerspectiveShader(String vertex, String fragment) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);

        this.mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(this.mProgram, vertexShader);
        GLES20.glAttachShader(this.mProgram, fragmentShader);

        GLES20.glLinkProgram(this.mProgram);
    }

    /**
     * Méthode permettant de chargé le shader dans la mémoire pour l'utiliser ensuite dans les rendus
     *
     * @param type Type du shader
     * @param shaderCode Code source du shader
     *
     * @return Un entier permettant d'identifier le shader une fois chargé par OpenGL
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Getter sur l'attribut {@link #mProgram}
     *
     * @return Un entier permettant d'identifier le shader
     */
    public int getProgram() {
        return mProgram;
    }
}
