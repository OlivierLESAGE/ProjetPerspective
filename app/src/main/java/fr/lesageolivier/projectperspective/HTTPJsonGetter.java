package fr.lesageolivier.projectperspective;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe représentant une requête HTTP asynchrone permettant de récuperer un JSON
 *
 * @author Olivier LESAGE
 */
public class HTTPJsonGetter extends AsyncTask<String, String, String> {
    /**
     * Objet qui attend la réponse de la requête
     */
    private HTTPJsonWaiter waiter;

    /**
     * URL de la requête
     */
    private String url;

    /**
     * Constructeur
     *
     * @param url URL de la requête
     * @param waiter Objet qui attend la réponse de la requête
     */
    public HTTPJsonGetter(String url, HTTPJsonWaiter waiter) {
        this.waiter = waiter;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "";
            String line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            Log.d("JSON", content);

            return content;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            this.waiter.onResponse((s != null) ? new JSONObject(s) : null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
