package fr.lesageolivier.projectperspective;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représenant l'activité qui peremt la visualistion des objets en 3D
 *
 * @author Olivier LESAGE
 */
public class PerspectiveActivity extends AppCompatActivity {
    /**
     * Scene qui va être affichée par l'activité
     */
    private Scene scene;

    /**
     * Surface view
     */
    private PerspectiveSurfaceView surfaceView;

    /**
     * FrameLayout dans lequel la {@link #surfaceView}
     */
    private FrameLayout perspectiveView;

    /**
     * Bouton information
     */
    private Button buttonInformation;

    /**
     * Bouton retour
     */
    private Button buttonBack;

    /**
     * Dialog de chargement
     */
    private ProgressDialog progress;

    /**
     * Dialog qui affiche les informations
     */
    private DialogMessage dialogInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perspective);

        this.perspectiveView = (FrameLayout) findViewById(R.id.perspective_view);
        this.buttonInformation = (Button) findViewById(R.id.perspective_info);
        this.buttonBack = (Button) findViewById(R.id.perspective_back);

        this.buttonBack.setOnClickListener(new CloseActivityListener (this));

        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Chargement ...");
        this.progress.setCancelable(false);

        this.progress.show();

        try {
            this.scene = new Scene(this, new JSONObject(getIntent().getStringExtra("scene")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode permttant d'afficher la surface view
     * Appelée lorsque la scène est chargée
     */
    public void loaded() {
        this.surfaceView = new PerspectiveSurfaceView(this, new PerspectiveCamera());
        this.perspectiveView.addView(this.surfaceView);

        this.dialogInformation = new DialogMessage(this, this.scene.getInformaion());
        this.buttonInformation.setOnClickListener(new PerspectiveInformationListener(this.dialogInformation));

        this.progress.dismiss();
    }

    /**
     * Getter sur l'attribut {@link #scene}
     * @return La scène
     */
    public Scene getScene() {
        return this.scene;
    }
}
