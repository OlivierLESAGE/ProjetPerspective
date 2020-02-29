package fr.lesageolivier.projectperspective;

import android.graphics.Bitmap;

/**
 * Interface représentant un objet qui attend une image reçue grâce requête HTTP
 *
 * @author Olivier LESAGE
 */
public interface HTTPImageWaiter {
    /**
     * Méthode permettant de transmettre l'image reçue grâce à un {@link HTTPImageGetter}
     *
     * @param image L'image reçue
     */
    void onResponse(Bitmap image);
}
