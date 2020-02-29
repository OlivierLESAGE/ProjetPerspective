package fr.lesageolivier.projectperspective;

import android.opengl.GLSurfaceView;

/**
 * Classe représentant la SurfaceView dans laquelle seront affichées les scènes
 *
 * @author Olivier LESAGE
 */
public class PerspectiveSurfaceView extends GLSurfaceView {
    /**
     * Renderer utilisé pour faire les rendus
     */
    private PerspectiveRenderer renderer;

    /**
     * Activité dans laquelle est la SurfaceView
     */
    private PerspectiveActivity app;

    /**
     * Constructeur
     *
     * @param context Activité dans laquelle est la SurfaceView
     * @param camera Cameré utilisée pour faire les rendus
     */
    public PerspectiveSurfaceView(PerspectiveActivity context, PerspectiveCamera camera) {
        super(context);
        this.app = context;

        setEGLContextClientVersion(2);

        this.renderer = new PerspectiveRenderer(this.app.getScene(), camera);

        this.setOnTouchListener(new PerspectiveSurfaceViewListener(this.getContext(), camera));

        setRenderer(this.renderer);
    }
}
