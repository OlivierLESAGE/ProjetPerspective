package fr.lesageolivier.projectperspective;

import android.content.DialogInterface;

/**
 * Classe représentant le listener des {@link DialogMessage}
 *
 * @author Olivier LESAGE
 */
class DialogMessageListener implements DialogInterface.OnClickListener {
    /**
     * La dialog sur laquelle le listener est appliqué
     */
    DialogMessage dialog;

    /**
     * Constructeur
     *
     * @param dialogMessage La dialog sur laquelle le listener est appliqué
     */
    public DialogMessageListener(DialogMessage dialogMessage) {
        this.dialog = dialogMessage;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        this.dialog.hide();
    }
}
