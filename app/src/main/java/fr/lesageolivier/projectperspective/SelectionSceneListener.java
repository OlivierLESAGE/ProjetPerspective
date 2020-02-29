package fr.lesageolivier.projectperspective;

import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

/**
 * Classe représentant le listener de la ListView de l'activité {@link SelectionSceneActivity}
 *
 * @author Olivier LESAGE
 */
public class SelectionSceneListener implements AdapterView.OnItemClickListener {
    /**
     * Activité contenant la ListView
     */
    private SelectionSceneActivity app;

    /**
     * Constructeur
     *
     * @param app Activité contenant la ListView
     */
    public SelectionSceneListener(SelectionSceneActivity app) {
        this.app = app;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.app.selectSence((JSONObject) adapterView.getItemAtPosition(i));
    }
}
