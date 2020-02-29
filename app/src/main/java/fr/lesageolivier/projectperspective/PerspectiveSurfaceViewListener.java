package fr.lesageolivier.projectperspective;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Classe représentant le listener de la vue 3D
 *
 * @author Olivier LESAGE
 */
public class PerspectiveSurfaceViewListener implements View.OnTouchListener {
    /**
     * Coordonnées X du clique précedent
     */
    private float previousX;

    /**
     * Coordonnées Y du clique précedent
     */
    private float previousY;

    /**
     * Coordonnées X du clique actuel
     */
    private float x;

    /**
     * Coordonnées Y du clique actuel
     */
    private float y;

    /**
     * Détecteur de pinch, permet de savoir si l'utilisateur zoom
     */
    private ScaleGestureDetector pinch;

    /**
     * Caméra qui va être modifiée lors des actions de l'utilisateur
     */
    private PerspectiveCamera camera;

    /**
     * COnstructeur
     *
     * @param context Activité dans laquelle est utilisé le listener
     * @param camera Caméra qui va être modifiée lors des actions de l'utilisateur
     */
    public PerspectiveSurfaceViewListener(Context context, PerspectiveCamera camera) {
        this.pinch = new ScaleGestureDetector(context, new PerspectiveSurfaceViewPinchListener(camera));
        this.camera = camera;

        this.previousX = 0.0f;
        this.previousY = 0.0f;

        this.x = 0.0f;
        this.y = 0.0f;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK);

        this.x = motionEvent.getX();
        this.y = motionEvent.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                // Si il y a un seul doigt sur l'écran
                if (motionEvent.getPointerCount() == 1)
                    this.camera.orienter(this.x - this.previousX, this.y - this.previousY);
                // Sinon
                else this.pinch.onTouchEvent(motionEvent);
                break;
        }

        this.previousX = this.x;
        this.previousY = this.y;

        return true;
    }
}


/**
 * Classe permettant de déctecter les pinch de l'utilisateur
 *
 * @author Olivier LESAGE
 */
class PerspectiveSurfaceViewPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    /**
     * Caméra qui va être modifiée lors des actions de l'utilisateur
     */
    private PerspectiveCamera camera;

    public PerspectiveSurfaceViewPinchListener(PerspectiveCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        this.camera.zoom(1.0f/detector.getScaleFactor());
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return super.onScaleBegin(detector);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        super.onScaleEnd(detector);
    }
}
