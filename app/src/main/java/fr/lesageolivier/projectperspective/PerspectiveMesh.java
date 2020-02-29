package fr.lesageolivier.projectperspective;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe représentant un mesh utilisant OpenGL ES
 *
 * @author Olivier LESAGE
 */
public class PerspectiveMesh {
    /**
     * Nombre de coordonnées par vertex.
     */
    public static final int COORDS_PER_VERTEX = 3;

    /**
     * Taille en octet d'un vertex.
     */
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    /**
     * Nombre de coordonnées par point UV
     */
    public static final int COORDS_PER_UV = 2;

    /**
     * Taille en octet d'un vertex.
     */
    public static final int TEXTURE_STRIDE = COORDS_PER_UV * 4;


    /**
     * Buffer contenant les vertices.
     */
    protected FloatBuffer vertexBuffer;
    /**
     * Buffer contenant les coordonnées UV du maillage.
     */
    protected FloatBuffer textureBuffer;

    /**
     * Tableau contenant tous les vertices.
     */
    protected float vertexCoord[];

    /**
     * Tableau contenant toutes les coordonnées UV.
     */
    protected float textureCoord[];

    /**
     * Nombre de vertex.
     */
    private int vertexCount;

    /**
     * Constructeur
     *
     * @param vertexCoord Tableau contenant les coordonnées des vertices de chaque face
     * @param textureCoord Tableau contenant les coordonnées UV de chaque face
     */
    public PerspectiveMesh(float vertexCoord[], float textureCoord[]) {
        this.vertexCoord = vertexCoord;
        this.vertexCount = vertexCoord.length / COORDS_PER_VERTEX;
        this.textureCoord = textureCoord;

        // Création du buffer contenant les vertices.
        ByteBuffer bb = ByteBuffer.allocateDirect(this.vertexCoord.length * 4);
        bb.order(ByteOrder.nativeOrder());

        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(this.vertexCoord);
        this.vertexBuffer.position(0);

        // Création du buffer contenant les coordonnées UV.
        ByteBuffer tb = ByteBuffer.allocateDirect(this.textureCoord.length * 4);
        tb.order(ByteOrder.nativeOrder());

        this.textureBuffer = tb.asFloatBuffer();
        this.textureBuffer.put(this.textureCoord);
        this.textureBuffer.position(0);
    }

    /**
     * Méthode permettant de rendre le mesh
     *
     * @param shader Shader utilisé pour faire le rendu
     * @param texture Texture utilisé pour faire le rendu
     * @param mvpMatrix Matrice MVP (correspond à la matrice view projection de la caméra)
     */
    public void draw(PerspectiveShader shader, PerspectiveTexture texture, float mvpMatrix[]) {
        this.draw(shader, texture.getHandle(), mvpMatrix);
    }

    /**
     *
     *
     * @param shader Shader utilisé pour faire le rendu
     * @param texture Handler de la {@link PerspectiveTexture texture}
     * @param mvpMatrix Matrice MVP (correspond à la matrice view projection de la caméra)
     */
    public void draw(PerspectiveShader shader, int texture, float mvpMatrix[]) {
        // Linkage du shader.
        int mProgram = shader.getProgram();
        GLES20.glUseProgram(mProgram);

        // Envoi des vertices.
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, this.vertexBuffer);

        // Envoi des coordonnées UV.
        int mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2,
                GLES20.GL_FLOAT, false,
                TEXTURE_STRIDE, this.textureBuffer);

        // Envoi de la texture
        int mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Envoi de la matrice
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Dessine le maillage
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.vertexCount);

        // Désactive les buffer.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureUniformHandle);
    }

    /**
     * Méthode permettant de créer un {@link PerspectiveMesh} à partir d'un fichier au format .obj
     *
     * @param file Le contenu du fichier .obj
     *
     * @return Un {@link PerspectiveMesh} correspondant au fichier
     */
    public static PerspectiveMesh fromObjFile(String file) {
        ArrayList<Vertex> vertexList = new ArrayList<>(0);
        ArrayList<Coord> textureList = new ArrayList<>(0);
        ArrayList<PointFace> faceList = new ArrayList<>(0);

        Pattern pattern;
        Matcher matcher;

        /*Récupère les vertex*/
        pattern = Pattern.compile("v [-]?[0-9]*.[0-9]* [-]?[0-9]*.[0-9]* [-]?[0-9]*.[0-9]*");
        matcher = pattern.matcher(file);
        while (matcher.find()) {
            String[] split = matcher.group().split(" ");
            vertexList.add(new Vertex(
                    Float.parseFloat(split[1]),
                    Float.parseFloat(split[2]),
                    Float.parseFloat(split[3])
            ));
        }

        /*Récupère les coordonnées UV*/
        pattern = Pattern.compile("vt [-]?[0-9]*.[0-9]* [-]?[0-9]*.[0-9]*");
        matcher = pattern.matcher(file);
        while (matcher.find()) {
            String[] split = matcher.group().split(" ");
            textureList.add(new Coord(
                    Float.parseFloat(split[1]),
                    Float.parseFloat(split[2])
            ));
        }

        /*Récupère les faces*/
        pattern = Pattern.compile("f [0-9]*/[0-9]* [0-9]*/[0-9]* [0-9]*/[0-9]*");
        matcher = pattern.matcher(file);
        while (matcher.find()) {
            String[] split = matcher.group().split(" ");
            for (int i = 1; i < split.length; ++i) {
                String[] face = split[i].split("/");
                faceList.add(new PointFace(Integer.parseInt(face[0]) - 1, Integer.parseInt(face[1]) - 1));
            }
        }

        int nbVertex = 0;
        int nbTexture = 0;

        float[] vertexArray = new float[faceList.size() * COORDS_PER_VERTEX];
        float[] textureArray = new float[faceList.size() * COORDS_PER_UV];

        for (PointFace face : faceList) {
            Vertex v = vertexList.get(face.v);
            Coord c = textureList.get(face.t);

            vertexArray[nbVertex] = v.x;
            vertexArray[nbVertex + 1] = v.y;
            vertexArray[nbVertex + 2] = v.z;

            nbVertex += COORDS_PER_VERTEX;

            textureArray[nbTexture] = c.u;
            textureArray[nbTexture + 1] = 1 - c.v;

            nbTexture += COORDS_PER_UV;
        }

        return new PerspectiveMesh(vertexArray, textureArray);
    }
}

/**
 * Classe représentant un Vertex
 *
 * @author Olivier LESAGE
 */
class Vertex {
    /**
     * Coordonnée X
     */
    public float x;

    /**
     * Coordonnée Y
     */
    public float y;

    /**
     * Coordonnée Z
     */
    public float z;

    /**
     * Constructeur
     *
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

/**
 * Classe représentant un point UV
 *
 * @author Olivier LESAGE
 */
class Coord {
    /**
     * Coordonnées U
     */
    public float u;

    /**
     * Coordonnée V
     */
    public float v;

    /**
     * Constructeur
     *
     * @param u Coordonnée U
     * @param v Coordonnée V
     */
    public Coord(float u, float v) {
        this.u = u;
        this.v = v;
    }
}

/**
 * Classe représentant une point d'une face
 *
 * @author Olivier LESAGE
 */
class PointFace {
    /**
     * Indice du vertex
     */
    public int v;

    /**
     * Indice du point UV
     */
    public int t;

    /**
     * Constructeur
     *
     * @param v Indice du vertex
     * @param t Indice du point UV
     */
    public PointFace(int v, int t) {
        this.v = v;
        this.t = t;
    }
}

