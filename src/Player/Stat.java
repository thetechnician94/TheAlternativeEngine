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
public class Stat implements Comparable{

    private String name;
    private int value;
    private int maxValue;

    public Stat(String name, int value, int max) {
        this.name = name;
        this.value = value;
        this.maxValue = max;
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
        if (this.value > maxValue) {
            this.setValue(maxValue);
        }

    }

    @Override
    public int compareTo(Object cmp) {
        Stat stat = (Stat) cmp;
        return this.name.compareTo(stat.getName());
    }

}
