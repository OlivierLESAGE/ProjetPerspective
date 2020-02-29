package fr.lesageolivier.projectperspective;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant un shader
 *
 * @author Olivier LESAGE
 */
public class Shader {
    /**
     * Code source du vertex shader
     */
    private String vertex;

    /**
     * Code source du fragment shader
     */
    private String fragment;

    /**
     * Objet qui utilise ce shader
     */
    private Objet objet;

    /**
     * Constructeur
     *
     * @param obj Objet qui utilise ce shader
     * @param vertex Code source du vertex shader
     * @param fragment Code source du fragment shader
     */
    public Shader(Objet obj, String vertex, String fragment) {
        this.objet = obj;
        this.vertex = vertex;
        this.fragment = fragment;
    }

    /**
     * Constructeur
     *
     * @param obj Objet qui utilise ce shader
     * @param json JSON contenant les informations du shader
     * @throws JSONException
     */
    public Shader(Objet obj, JSONObject json) throws JSONException {
        this(
                obj,
                json.getString("vertexShader"),
                json.getString("fragmentShader")
        );
    }

    /**
     * Getter sur l'attribut {@link #vertex}
     *
     * @return Le code source du vertex shader
     */
    public String getVertex() {
        return this.vertex;
    }

    /**
     * Getter sur l'attribut {@link #fragment}
     *
     * @return Le code source du fragment shader
     */
    public String getFragment() {
        return this.fragment;
    }
}
