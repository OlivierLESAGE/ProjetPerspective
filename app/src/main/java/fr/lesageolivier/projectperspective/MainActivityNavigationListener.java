package fr.lesageolivier.projectperspective;

import android.view.View;

/**
 * Classe représentant le listener de la classe {@link MainActivity}
 * Ce listener doit être appliqué à tous les boutons de l'activité {@link MainActivity}
 *
 * @author Olivier LESAGE
 */
class MainActivityNavigationListener implements View.OnClickListener {
    /**
     * L'activité sur laquelle le listener est appliqué
     */
    private MainActivity app;

    /**
     * Constructeur
     *
     * @param mainActivity L'activité sur laquelle le listener est appliqué
     */
    public MainActivityNavigationListener(MainActivity mainActivity) {
        this.app = mainActivity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_play:
                this.app.play();
        }
    }
}
