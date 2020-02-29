package fr.lesageolivier.projectperspective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * Classe représentant l'axtivité principale de l'application
 *
 * @author Olivier LESAGE
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = (Button) findViewById(R.id.button_play);
        playButton.setOnClickListener(new MainActivityNavigationListener(this));
    }

    /**
     * Méthode permettant de lancer l'activité de sélection des scènes
     */
    public void play() {
        Intent intent = new Intent(this, SelectionSceneActivity.class);
        startActivity(intent);
    }
}