package fr.lesageolivier.projectperspective;

import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Dialog représentant une dialog affichant un message
 * Lors de la fermeture de la dialog, cela ferme l'activité dans laquelle la dialog est affichée
 *
 * @author Olivier LESAGE
 */
public class DialogMessageProblem {
    private AppCompatActivity app;
    private AlertDialog dialog;

    /**
     * Constructeur
     *
     * @param app Activité dans laquelle la dialog va être affichée
     * @param text Le texte de la dialog
     */
    public DialogMessageProblem(AppCompatActivity app, int text) {
        this.app = app;

        View v = this.app.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.app);
        builder.setView(v);

        TextView texte = (TextView) v.findViewById(R.id.dialog_text);
        texte.setText(text);

        builder.setPositiveButton(R.string.dialog_ok, new DialogProblemListener(this.app));

        this.dialog = builder.create();
        this.dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Méthode permettant d'afficher la dialog
     */
    public void show() {
        this.dialog.show();
    }
}
