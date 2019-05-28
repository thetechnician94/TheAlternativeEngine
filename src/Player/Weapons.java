/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 94tyl
 */
public class Weapons {

    private ArrayList<Weapon> weapons = new ArrayList();
    private String delimiter;

    public Weapons(String weaponFile, String delimiter) {
        this.delimiter = delimiter;
        loadWeapons(weaponFile);
    }

    private void loadWeapons(String weaponFile) {
        File wf = new File(weaponFile);
        if (!wf.exists()) {
            return;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(wf);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Weapons.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(delimiter);
            if (parts.length == 2) {
                getWeapons().add(new Weapon(parts[0], Integer.parseInt(parts[1]), "", 0));
            }
            if (parts.length == 4) {
                getWeapons().add(new Weapon(parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3])));
            }
        }
    }

    /**
     * @return the weapons
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public Weapon getWeapon(String name) {
        for (Weapon weapon : weapons) {
            if (weapon.getName().equals(name)) {
                return weapon;
            }
        }
        return null;
    }
}
