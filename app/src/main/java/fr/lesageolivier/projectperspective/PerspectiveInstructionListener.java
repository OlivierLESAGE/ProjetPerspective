package fr.lesageolivier.projectperspective;

import android.view.View;

/**
 * Classe représentant le listener du bouton information de l'activité {@link PerspectiveActivity}
 *
 * @author Olivier LESAGE
 */
class PerspectiveInformationListener implements View.OnClickListener {
    /**
     * Dialog affichée lors du clique sur le bouton
     */
    private DialogMessage dialogMessage;

    /**
     * Constructeur
     *
     * @param dialogInstructions Dialog affichée lors du clique sur le bouton
     */
    public PerspectiveInformationListener(DialogMessage dialogInstructions) {
        this.dialogMessage = dialogInstructions;
    }

    @Override
    public void onClick(View view) {
        this.dialogMessage.show();
    }
}
