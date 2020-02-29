package fr.lesageolivier.projectperspective;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe représentant une requête HTTP asynchrone permettant de récuperer une image
 *
 * @author Olivier LESAGE
 */
public class HTTPImageGetter extends AsyncTask<String, Bitmap, Bitmap> {
    /**
     * Objet qui attend la réponse de la requête
     */
    private HTTPImageWaiter waiter;

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
    public HTTPImageGetter(String url, HTTPImageWaiter waiter) {
        this.waiter = waiter;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream is = connection.getInputStream();
            Bitmap img = BitmapFactory.decodeStream(is);

            return img;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        super.onPostExecute(image);

        this.waiter.onResponse(image);
    }
}
