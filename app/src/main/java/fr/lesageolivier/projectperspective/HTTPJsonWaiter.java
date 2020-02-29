package fr.lesageolivier.projectperspective;

import org.json.JSONObject;

/**
 * Interface représentant un objet qui attend un JSON reçu grâce requête HTTP
 *
 * @author Olivier LESAGE
 */
public interface HTTPJsonWaiter {
    /**
     * Méthode permettant de transmettre le JSON reçu grâce à un {@link HTTPJsonGetter}
     *
     * @param json L'image reçue
     */
    void onResponse(JSONObject json);
}
