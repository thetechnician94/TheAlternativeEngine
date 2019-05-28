/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

/**
 *
 * @author 94tyl
 */
public class Stat {

    private String name;
    private int value;

    public Stat(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    public void adjustStat(int adjustment) {
        if (this.value >= 0) {
            this.setValue(this.value + adjustment);
        }
        if (this.value < 0) {
            this.setValue(0);
        }
        if (this.value > 100) {
            this.setValue(100);
        }

    }

}
