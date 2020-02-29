package fr.lesageolivier.projectperspective;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant l'activité de selection des scènes
 *
 * @author Olivier LESAGE
 */
public class SelectionSceneActivity extends AppCompatActivity implements HTTPJsonWaiter {
    /**
     * ListView contenant les scènes
     */
    private ListView listView;

    /**
     * Dialog de chargement
     */
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_scene);

        this.listView = (ListView) findViewById(R.id.list_view_selection_scene);
        this.listView.setOnItemClickListener(new SelectionSceneListener(this));

        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Chargement ...");
        this.progress.setCancelable(false);

        this.progress.show();

        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            HTTPJsonGetter jsonGetter = new HTTPJsonGetter("http://www.lesageolivier.fr/projetperspective/ajax/scene/get_all.php", this);
            jsonGetter.execute();
        } else {
            this.onNoInternet();
        }
    }

    @Override
    public void onResponse(JSONObject json) {
        if (json != null) {
            try {
                JSONArray array = json.getJSONArray("list");
                this.listView.setAdapter(new SceneAdapter(this, array));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.onProblemServer();
        }

        this.progress.dismiss();
    }

    /**
     * Méthode permettant d'afficher un message si il n'y a pas de connexion internet
     */
    public void onNoInternet() {
        this.progress.dismiss();
        new DialogMessageProblem(this, R.string.dialog_internet_prolem).show();
    }

    /**
     * Méthode permettant d'afficher un message si il y'a un problème avec la connexion au serveur
     */
    public void onProblemServer() {
        this.progress.dismiss();
        new DialogMessageProblem(this, R.string.dialog_server_prolem).show();
    }

    /**
     * Méthode permettant de lancer l'activité de visualisation des scènes ({@link PerspectiveActivity})
     *
     * @param scene La scène qui va être affichée
     */
    public void selectSence(JSONObject scene) {
        Intent intent = new Intent(this, PerspectiveActivity.class);
        intent.putExtra("scene", scene.toString());
        startActivity(intent);
    }
}
