package fr.lesageolivier.projectperspective;

import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe représentant une dialog affichant un message
 *
 * @author Olivier LESAGE
 */
public class DialogMessage {
    /**
     * Activité dans laquelle la dialog va être affichée
     */
    private AppCompatActivity app;
    /**
     * Dialog qui va être affichée
     */
    private AlertDialog dialog;

    /**
     * Constructeur
     *
     * @param app Activité dans laquelle la dialog va être affichée
     * @param text Le texte de la dialog
     */
    public DialogMessage(AppCompatActivity app, String text) {
        this.app = app;

        View v = this.app.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.app);

        builder.setView(v);

        TextView texte = (TextView) v.findViewById(R.id.dialog_text);
        texte.setText(text);

        builder.setPositiveButton(R.string.dialog_ok, new DialogMessageListener(this));

        this.dialog = builder.create();
        this.dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Méthode permettant d'afficher la dialog
     */
    public void show() {
        this.dialog.show();
    }

    /**
     * Méthode permettant de cacher la dialog
     */
    public void hide() {
        this.dialog.dismiss();
    }
}

