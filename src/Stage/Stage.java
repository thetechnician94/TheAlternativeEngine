/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stage;

import java.util.ArrayList;

/**
 *
 * @author 94tyl
 */
public class Stage {

    /**
     * @return the combat
     */
    public Combat getCombat() {
        return combat;
    }

    private int id;
    private String text;
    private ArrayList<Option> options;
    private String image;
    private String music;
    private Combat combat;

    public Stage(int id, String text, ArrayList<Option> options, String image, String music, Combat combat) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.image = image;
        this.music = music;
        this.combat = combat;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @return the music
     */
    public String getMusic() {
        return music;
    }
}
