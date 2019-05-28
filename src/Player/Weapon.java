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
public class Weapon {

    private String name;
    private int damage;
    private String reqItem;
    private int reqItemAmt;
    //  private int useChance;

    public Weapon(String name, int damage, String reqItem, int reqItemAmt) {
        this.name = name;
        this.damage = damage;
        this.reqItem = reqItem;
        this.reqItemAmt = reqItemAmt;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the reqItem
     */
    public String getReqItem() {
        return reqItem;
    }

    /**
     * @return the reqItemAmt
     */
    public int getReqItemAmt() {
        return reqItemAmt;
    }
}
