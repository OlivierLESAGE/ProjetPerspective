package fr.lesageolivier.projectperspective;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant une scène
 *
 * @author Olivier LESAGE
 */
public class Scene implements HTTPImageWaiter, HTTPJsonWaiter {
    /**
     * Le nom de la scène
     */
    private String name;

    /**
     * Information sur la scène
     */
    private String information;

    /**
     * Composante rouge de la couleur de fond de la scène
     */
    private float red;

    /**
     * Composante verte de la couleur de fond de la scène
     */
    private float green;

    /**
     * Composante bleue de la couleur de fond de la scène
     */
    private float blue;

    /**
     * Nom de la texture cible
     */
    private String textureTargetName;

    /**
     * Texture cible, contenant les informations sur les zones cliquable
     */
    private Bitmap textureTarget;

    /**
     * Id de l'objet de la scène
     */
    private int objetId;

    /**
     * Objet de la scène
     */
    private Objet objet;

    /**
     * Activité dans laquelle la scène est affichée
     */
    private PerspectiveActivity app;

    /**
     * Booléen valant vrai si l'objet est chargé, faux sinon
     */
    private boolean isLoaded;

    /**
     * Constructeur
     *
     * @param app Activité dans laquelle la scène est affichée
     * @param name Le nom de la scène
     * @param information Information sur la scène
     * @param red Composante rouge de la couleur de fond de la scène
     * @param green Composante verte de la couleur de fond de la scène
     * @param blue Composante bleue de la couleur de fond de la scène
     * @param objetId Id de l'objet de la scène
     * @param textureTargetName Nom de la texture cible
     */
    public Scene(PerspectiveActivity app, String name, String information, float red, float green, float blue, int objetId, String textureTargetName) {
        this.app = app;
        this.isLoaded = false;

        this.name = name;
        this.information = information;
        this.red = red;
        this.green = green;
        this.blue = blue;

        this.textureTargetName = textureTargetName;
        this.textureTarget = null;

        this.objetId = objetId;
        this.objet = null;

        this.loadTexture();
        this.loadObjet();
    }

    /**
     * Constructeur
     *
     * @param app Activité dans laquelle la scène est affichée
     * @param json JSON contenant les informations de la scène
     * @throws JSONException Excepetion levé si les champs du JSON ne correspondent pas à ceux demander.
     */
    public Scene(PerspectiveActivity app, JSONObject json) throws JSONException {
        this(
                app,
                json.getString("name"),
                json.getString("instructions"),
                json.getInt("red") / 255.0f,
                json.getInt("green") / 255.0f,
                json.getInt("blue") / 255.0f,
                json.getInt("objet"),
                json.getString("textureTarget"));
    }

    /**
     * Méthode permettant de charger la texture cible de la scène
     */
    public void loadTexture() {
        HTTPImageGetter imageGetter = new HTTPImageGetter("http://www.lesageolivier.fr/projetperspective/asset/texture/" + this.textureTargetName,this);
        imageGetter.execute();
    }

    /**
     * Méthode permettant de charger l'objet de la scène
     */
    public void loadObjet() {
        HTTPJsonGetter jsonGetter = new HTTPJsonGetter("http://www.lesageolivier.fr/projetperspective/ajax/objet/get_one.php?objetId=" + this.objetId, this);
        jsonGetter.execute();
    }

    /**
     * Méthode permettant de traiter la reception de la texture après le téléchargement
     *
     * @param image Image correspondant à la texture cible de la scène
     */
    public void onResponse(Bitmap image) {
       this.textureTarget = image;
       this.isLoaded = true;
       this.loaded();
    }

    /**
     * Méthode permettant de traiter la reception du shader après le téléchargement
     *
     * @param json JSON correspondant au shader de l'objet
     */
    @Override
    public void onResponse(JSONObject json) {
        try {
            this.objet = new Objet(this, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode permettant de savoir si la scène est chargée
     *
     * @return true si la scène est chargée, false sinon
     */
    public boolean isLoaded() {
        return this.isLoaded;
    }

    /**
     * Méthode permettant de notifier l'activité lorsque la scène et l'objet de la scène sont chargés
     */
    public void loaded() {
        if(this.isLoaded() && this.objet!=null && this.objet.isLoaded()) {
            this.app.loaded();
        }
    }

    /**
     * Getter sur l'attribut {@link #objet}
     *
     * @return L'objet de la scène
     */
    public Objet getObjet() {
        return this.objet;
    }

    /**
     * Getter sur l'attribut {@link #textureTarget}
     *
     * @return La texture cible
     */
    public Bitmap getTextureTarget() {
        return this.textureTarget;
    }

    /**
     * Getter sur l'attribut {@link #name}
     * @return Le nom de la scène
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter sur l'attribut {@link #information}
     *
     * @return Les informations sur la scène
     */
    public String getInformaion() {
        return this.information;
    }

    /**
     * Getter sur l'attribut {@link #red}
     *
     * @return La composante rouge de la couleur de la scène
     */
    public float getRed() {
        return this.red;
    }

    /**
     * Getter sur l'attribut {@link #green}
     *
     * @return La composante verte de la couleur de la scène
     */
    public float getGreen() {
        return this.green;
    }

    /**
     * Getter sur l'attribut {@link #blue}
     *
     * @return La composante bleue de la couleur de la scène
     */
    public float getBlue() {
        return this.blue;
    }

    /**
     * Getter sur l'attribut {@link #textureTargetName}
     *
     * @return Le nom de la texture cible
     */
    public String getTextureTargetName() {
        return this.textureTargetName;
    }

    /**
     * Getter sur l'attribut {@link #objetId}
     *
     * @return L'Id de l'objet de la scène
     */
    public int getObjetId() {
        return this.objetId;
    }

    /**
     * Getter sur l'attribut {@link #app}
     *
     * @return L'activité dans laquelle la scène a été crée
     */
    public PerspectiveActivity getApp() {
        return this.app;
    }
}
