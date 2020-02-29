package fr.lesageolivier.projectperspective;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant un objet 3D.
 *
 * @see HTTPImageGetter
 * @see HTTPJsonGetter
 *
 * @author Olivier LESAGE
 */
public class Objet implements HTTPImageWaiter, HTTPJsonWaiter {
    /**
     * Nom de l'objet
     */
    private String name;

    /**
     * Mesh de l'objet au format .obj
     */
    private String mesh;

    /**
     * Nom de la texture de l'objet
     */
    private String textureName;

    /**
     * ID du shader de l'objet
     */
    private int shaderId;

    /**
     * Scène dans laquelle l'objet est utilisé
     */
    private Scene scene;

    /**
     * Texture de l'objet
     */
    private Bitmap texture;

    /**
     * Shader de l'objet
     */
    private Shader shader;

    /**
     * Constructeur
     *
     * @param scene Scène dans laquelle l'objet est utilisé
     * @param name Nom de l'objet
     * @param textureName Nom de la texture
     * @param shaderId ID du shader
     * @param mesh Mesh de l'objet
     */
    public Objet(Scene scene, String name, String textureName, int shaderId, String mesh) {
        this.scene = scene;

        this.name = name;
        this.mesh = mesh;
        this.textureName = textureName;
        this.shaderId = shaderId;

        this.shader = null;
        this.texture = null;

        this.loadTexture();
        this.loadShader();
    }

    /**
     * Constructeur
     * @param scene Scene dans laquelle l'objet est utilisé
     * @param json JSON contenant les informations de l'objet
     * @throws JSONException Excepetion levé si les champs du JSON ne correspondent pas à ceux demander.
     */
    public Objet(Scene scene, JSONObject json) throws JSONException {
        this(
                scene,
                json.getString("name"),
                json.getString("texture"),
                json.getInt("shader"),
                json.getString("mesh"));
    }

    /**
     * Méthode permettant de charger la texture de l'objet
     */
    public void loadTexture() {
        HTTPImageGetter imageGetter = new HTTPImageGetter("http://www.lesageolivier.fr/projetperspective/asset/texture/" + this.textureName, this);
        imageGetter.execute();
    }

    /**
     * Méthode permettant de chager le shader de l'objet
     */
    public void loadShader() {
        HTTPJsonGetter jsonGetter = new HTTPJsonGetter("http://www.lesageolivier.fr/projetperspective/ajax/shader/get_one.php?shaderId=" + this.shaderId, this);
        jsonGetter.execute();
    }

    /**
     * Méthode permettant de savoir si l'objet est chargé, c'est à dire si la texture et le shader ont été téléchargés
     *
     * @return true si l'objet est chargé, false sinon
     */
    public boolean isLoaded() {
        return this.shader != null && this.texture != null;
    }

    /**
     * Méthode permettant de traiter la reception de la texture après le téléchargement
     *
     * @param image Image correspondant à la texture de l'objet
     */
    @Override
    public void onResponse(Bitmap image) {
        this.texture = image;
        this.scene.loaded();
    }

    /**
     * Méthode permettant de traiter la reception du shader après le téléchargement
     *
     * @param json JSON correspondant au shader de l'objet
     */
    @Override
    public void onResponse(JSONObject json) {
        try {
            this.shader = new Shader(this, json);
            this.scene.loaded();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter sur l'attribut {@link #name}
     *
     * @return Le nom
     */
    public String getName() {
        return name;
    }

    /**
     * Getter sur l'attribut {@link #mesh}
     *
     * @return Le mesh au format .obj
     */
    public String getMesh() {
        return mesh;
    }

    /**
     * Getter sur l'attribut {@link #textureName}
     *
     * @return Le nom de la texture
     */
    public String getTextureName() {
        return textureName;
    }

    /**
     * Getter sur l'attribut {@link #shaderId}
     *
     * @return L'ID du shader
     */
    public int getShaderId() {
        return shaderId;
    }

    /**
     * Getter sur l'attribut {@link #scene}
     *
     * @return Scène dans laquelle l'objet est utilisé
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Getter sur l'attribut {@link #shader}
     *
     * @return Shader de l'objet
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Getter sur l'attribut {@link #texture}
     *
     * @return La texture de l'objet
     */
    public Bitmap getTexture() {
        return this.texture;
    }
}
