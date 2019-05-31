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
public class Item implements Comparable {

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    private String name;
    private int qty;

    public Item(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }

    public void adjustQty(int adjustment) {
        if (this.qty >= 0) {
            setQty(qty + adjustment);
        }
        if (qty < 0) {
            setQty(0);
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    @Override
    public int compareTo(Object cmp) {
        Item item = (Item) cmp;
        return this.name.compareTo(item.getName());
    }

}
