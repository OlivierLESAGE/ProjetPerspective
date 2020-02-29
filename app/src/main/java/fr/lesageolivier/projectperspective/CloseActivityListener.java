package fr.lesageolivier.projectperspective;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe représentant un listener permettant de fermer une activité
 *
 * @author Olivier LESAGE
 */
class CloseActivityListener implements View.OnClickListener {
    /**
     * Activité qui sera fermé par le listener lors d'un clique sur le bouton
     */
    private AppCompatActivity app;

    /**
     * Constructeur
     *
     * @param app Activité qui sera fermé par le listener lors d'un clique sur le bouton
     */
    public CloseActivityListener(AppCompatActivity app) {
        this.app = app;
    }

    @Override
    public void onClick(View view) {
        this.app.finish();
    }
}
