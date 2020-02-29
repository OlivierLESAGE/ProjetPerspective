package fr.lesageolivier.projectperspective;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Classe représentant la caméra 3D de la {@link PerspectiveSurfaceView}
 *
 * Cette caméra est de type trakball, c'est à dire qu'elle se déplace sur une sphère
 *
 * @author Olivier LESAGE
 */
public class PerspectiveCamera {
    /**
     * Coefficient de vitesse
     * Permet d'augmenter ou diminuer la vitesse de rotation de la caméra
     */
    public static final float SPEED = 0.2f;

    /**
     * Zoom initiale de la caméra
     */
    public static final float ZOOM = 0.01f;

    /**
     * Distance entre la caméra et le centre du repère
     */
    public static final float DISTANCE = 5.0f;

    /**
     * Angle correspondant à la "hauteur" de la caméra sur la sphere
     * Plus il est élevé, plus la caméra seras haute par rapport à l'objet
     */
    protected float phi;

    /**
     * Angle de rotation autour de l'axe Y
     */
    protected float theta;

    /**
     * Zoom de la caméra
     */
    protected float zoom;

    /**
     * Ratio de la vue de la caméra
     * correspond aux ratio de l'écran
     */
    protected float ratio;

    /**
     * Matrice view
     */
    protected float[] view;

    /**
     * Matrice projection
     */
    protected float[] projection;

    /**
     * Matrice de postition
     */
    protected float[] position;

    /**
     * Point cible de la caméra
     * Correspond au point fixé par la caméra
     */
    protected float[] pointCible;

    /**
     * Vecteur décrivant l'axe vertical de la caméra
     */
    protected float[] axeVertical;

    /**
     * Constructeur
     */
    public PerspectiveCamera() {
        this.phi = 45.0f;
        this.theta = 45.0f;
        this.zoom = ZOOM;
        this.ratio = 1;

        this.view = new float[16];
        this.projection = new float[16];
        this.position = new float[16];

        this.pointCible = new float[]{0, 0, 0};
        this.axeVertical = new float[]{0, 1, 0};
    }

    /**
     * Méthod permettant de faire tourner la caméra
     *
     * @param xRel Décalage sur l'angle phi
     * @param yRel Décalage sur l'angle théta
     */
    public void orienter(float xRel, float yRel) {
        this.phi += -yRel * SPEED;
        this.theta += -xRel * SPEED;

        this.update();
    }

    /**
     * Méthode permettant de modifier le zoom par un facteur
     *
     * @param factor Facteur de modification du zoom
     */
    public void zoom(float factor) {
        this.zoom *= factor;
        Log.d("ZOOM", this.zoom + "");
        this.update();
    }

    /**
     * Méthode permettant de modifier le ratio de la caméra
     *
     * @param width Largeur de la vue de la caméra
     * @param height Hauteur de la vue de la caméra
     */
    public void onresize(int width, int height) {
        this.ratio = (float) width / height;
        Matrix.frustumM(
                this.projection,
                0,
                -ratio * this.zoom,
                ratio * this.zoom,
                -1 * this.zoom,
                1 * this.zoom,
                0.01f,
                10.0f);
    }

    /**
     * Méthode permettant de mettre à jour la position de la caméra en fonction des angles phi et théta
     */
    public void update() {
        float phiRadian = (float) (this.phi * Math.PI / 180.0f);
        float thetaRadian = (float) (this.theta * Math.PI / 180.0f);

        this.position[0] = (float) (Math.sin(thetaRadian) * Math.sin(phiRadian)) * DISTANCE;
        this.position[1] = (float) (Math.cos(phiRadian) * DISTANCE);
        this.position[2] = (float) (Math.sin(phiRadian) * Math.cos(thetaRadian)) * DISTANCE;

        Matrix.setLookAtM(this.view, 0, this.position[0], this.position[1], this.position[2], this.pointCible[0], this.pointCible[1], this.pointCible[2], this.axeVertical[0], this.axeVertical[1], this.axeVertical[2]);
        Matrix.frustumM(
                this.projection,
                0,
                -ratio * this.zoom,
                ratio * this.zoom,
                -1 * this.zoom,
                1 * this.zoom,
                0.01f,
                10.0f);
    }

    /**
     * Méthode permettant de récuperer la matrix VP (view projection) de la caméra
     *
     * @return La matrice view projection
     */
    public float[] getMatrix() {
        float[] matrix = new float[16];

        Matrix.multiplyMM(matrix, 0, this.projection, 0, this.view, 0);

        return matrix;
    }

    /**
     * Getter sur l'attribut {@link #zoom}
     *
     * @return Le zoom de la caméra
     */
    public float getZoom() {
        return this.zoom;
    }
}
