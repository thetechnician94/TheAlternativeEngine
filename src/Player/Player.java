/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author 94tyl
 */
public class Player {

    private String name;
    private ArrayList<Stat> stats;
    private ArrayList<Item> inv;

    public Player(String name, ArrayList<Stat> stats) {
        this.name = name;
        this.stats = stats;
        inv = new ArrayList();
    }

    public int getStat(String name) {
        for (Stat stat : stats) {
            if (stat.getName().equals(name)) {
                return stat.getValue();
            }
        }
        return -1;
    }

    public String getStats() {
        Collections.sort(stats);
        String out = "";
        for (Stat stat : stats) {
            if (stat.getName().equals("Health")) {
                out += stat.getName() + ": " + stat.getValue() + "/" + stat.getMaxValue() + "\n";
            } else {
                out += stat.getName() + ": " + stat.getValue() + "\n";
            }
        }
        return out;
    }

    public String getStatsSave() {
        Collections.sort(stats);
        String out = "";
        for (Stat stat : stats) {
            out += stat.getName() + ": " + stat.getValue() + "\n";
        }
        return out;
    }

    public void adjustStat(String name, int adjustment, int max) {
        for (Stat stat : stats) {
            if (stat.getName().equals(name)) {
                stat.adjustStat(adjustment);
                return;
            }
        }
        stats.add(new Stat(name, 0, max));
        for (Stat stat : stats) {
            if (stat.getName().equals(name)) {
                stat.adjustStat(adjustment);
                return;
            }
        }

    }

    public int getItem(String name) {
        for (Item item : inv) {
            if (item.getName().equals(name)) {
                return item.getQty();
            }
        }
        return -1;
    }

    public String getInventory() {
        Collections.sort(inv);
        String out = "";
        for (Item item : inv) {
            if (item.getQty() == 0) {
                continue;
            } else if (item.getQty() == 1) {
                out += item.getName() + "\n";
            } else {
                out += item.getName() + ": " + item.getQty() + "\n";
            }
        }
        return out;
    }

    public void adjustInv(String name, int adjustment) {
        for (Item item : inv) {
            if (item.getName().equals(name)) {
                item.adjustQty(adjustment);
                return;

            }
        }
        if (adjustment >= 0) {
            inv.add(new Item(name, adjustment));
        } else {
            inv.add(new Item(name, 0));
        }
    }

    public void resetPlayer() {
        stats.clear();
        inv.clear();
    }

    public void setStat(String name, int adjustment, int max) {
        stats.add(new Stat(name, adjustment, max));
    }

    public void setInv(String name, int adjustment) {
        inv.add(new Item(name, adjustment));
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
