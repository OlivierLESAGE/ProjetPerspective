package fr.lesageolivier.projectperspective;

import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe représentant le listener des {@link DialogMessageProblem}
 *
 * @author Olivier LESAGE
 */
class DialogProblemListener implements DialogInterface.OnClickListener {
    /**
     * Activité qui va être fermée lors du click sur le bouton de la dialog
     */
    private AppCompatActivity app;

    /**
     * Constructeur
     *
     * @param app Activité qui va être fermée lors du click sur le bouton de la dialog
     */
    public DialogProblemListener(AppCompatActivity app) {
        this.app = app;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        this.app.finish();
    }
}
