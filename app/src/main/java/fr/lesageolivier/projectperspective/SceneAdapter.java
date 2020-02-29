package fr.lesageolivier.projectperspective;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant un adapter qui permet de lister des scène dans une ListView
 *
 * @author Olivier LESAGE
 */
public class SceneAdapter extends BaseAdapter {
    /**
     * Activité dans laquelle la ListView est affichée
     */
    private AppCompatActivity context;

    /**
     * JSONArray contenant toutes les scènes
     */
    private JSONArray scenes;

    /**
     * Objet permettant de créer les layout
     */
    private LayoutInflater layoutInflater;

    /**
     * Constructeur
     *
     * @param context Activité dans laquelle la ListView est affichée
     * @param array JSONArray contenant toutes les scènes
     */
    public SceneAdapter(AppCompatActivity context, JSONArray array) {
        this.context = context;
        this.scenes = array;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return scenes.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return this.scenes.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SceneViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_scene_item, null);
            holder = new SceneViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.scene_name);
            holder.color = (View) convertView.findViewById(R.id.scene_color);
            convertView.setTag(holder);
        } else {
            holder = (SceneViewHolder) convertView.getTag();
        }

        JSONObject scene = (JSONObject) this.getItem(i);

        try {
            holder.name.setText(scene.getString("name"));
            holder.color.setBackgroundColor(Color.rgb(
                    scene.getInt("red") / 255.0f,
                    scene.getInt("green") / 255.0f,
                    scene.getInt("blue") / 255.0f
            ));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

/**
 * Classe représentant le layout de l'activité {@link SelectionSceneActivity}
 *
 * @author Olivier LESAGE
 */
class SceneViewHolder {
    TextView name;
    View color;
}
