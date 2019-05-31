/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author 94tyl
 */
public class StageManager {

    private ArrayList<Stage> stages = new ArrayList();
    private String delimiter = "";
    private int maxLog;

    public StageManager(File stageFile, String delimiter, int max) throws FileNotFoundException, NumberFormatException, NoSuchElementException, DuplicateStageException {
        this.delimiter = delimiter;
        this.maxLog = max;
        loadStages(stageFile);

    }

    private void loadStages(File stageFile) throws FileNotFoundException, NumberFormatException, NoSuchElementException, DuplicateStageException {
        Scanner scanner = new Scanner(stageFile);
        String stageText = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith(";") || line.isBlank() && stageText.isEmpty()) {
                continue;
            }
            if (line.equals("")) {
                try {
                    addStage(interpretStage(stageText));
                } catch (NoSuchElementException ex) {
                    throw new NoSuchElementException("Error parsing stages\n" + stageText + "\n" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    throw new NumberFormatException("Error parsing stages\n" + stageText + "\n" + ex.getMessage());
                }
                stageText = "";
            } else {
                stageText += line + "\n";
            }
        }
        if (!stageText.isEmpty()) {
            try {
                addStage(interpretStage(stageText));
            } catch (NoSuchElementException ex) {
                throw new NoSuchElementException("Error parsing stages\n" + stageText + "\n" + ex.getMessage());
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Error parsing stages\n" + stageText + "\n" + ex.getMessage());
            }
        }
    }

    private Stage interpretStage(String bulkText) throws NoSuchElementException, NumberFormatException {
        int id = -1;
        String text = "";
        String music = "";
        String image = "";
        Combat combat = null;
        ArrayList<Option> options = new ArrayList();
        Scanner scanner = new Scanner(bulkText);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            switch (line.split(delimiter)[0].toLowerCase()) {
                case "i":
                    id = Integer.parseInt(line.split(delimiter)[1]);
                    break;
                case "t":
                    text = line.split(delimiter)[1].trim();
                    break;
                case "o":
                    int optionId = options.size() + 1;
                    String optionText = line.substring(2, line.length());
                    options.add(parseOption(optionId, optionText));
                    break;
                case "p":
                    image = line.split(delimiter)[1].trim();
                    break;
                case "m":
                    music = line.split(delimiter)[1].trim();
                    break;
                case "c":
                    combat = parseCombat(line.substring(2, line.length()));
                    break;
            }
        }
        return new Stage(id, text, options, image, music, combat);
    }

    private void addStage(Stage stage) throws DuplicateStageException {
        if (getStage(stage.getId()) == null) {
            stages.add(stage);
        } else {
            throw new DuplicateStageException("A stage with id " + stage.getId() + " already exists.\nStage Text:\n" + stage.getText());
        }
    }

    private Combat parseCombat(String line) {
        String enemyName = "";
        int enemyHealth = 0;
        int enemyBaseDamage = 0;
        int enemyDamageRange = 0;
        String enemyType = "";
        int hitChance = 0;
        int deathStage = 0;
        int winStage = 0;
        int firstAttack = 0;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\t");
        enemyName = scanner.next();
        while (scanner.hasNext()) {
            String next = scanner.next();
            String[] options = next.split(delimiter);
            switch (options[0].toLowerCase()) {
                case "h":
                    enemyHealth = Integer.parseInt(options[1]);
                    break;
                case "bd":
                    enemyBaseDamage = Integer.parseInt(options[1]);
                    break;
                case "dr":
                    enemyDamageRange = Integer.parseInt(options[1]);
                    break;
                case "hc":
                    hitChance = Integer.parseInt(options[1]);
                    break;
                case "ws":
                    winStage = Integer.parseInt(options[1]);
                    break;
                case "ds":
                    deathStage = Integer.parseInt(options[1]);
                    break;
                case "et":
                    enemyType = options[1];
                    break;
                case "fa":
                    firstAttack = Integer.parseInt(options[1]);
                    break;

            }
        }
        return new Combat(enemyName, enemyHealth, enemyBaseDamage, enemyDamageRange, enemyType, hitChance, deathStage, winStage, firstAttack, maxLog);
    }

    private Option parseOption(int id, String bulkText) throws NoSuchElementException, NumberFormatException {
        String text = "";
        int nextStage = -1;
        ArrayList<String> reqStat = new ArrayList();
        ArrayList<Integer> reqStatAmt = new ArrayList();
        ArrayList<String> statMod = new ArrayList();
        ArrayList<Integer> statModAmt = new ArrayList();
        ArrayList<String> reqItem = new ArrayList();
        ArrayList<Integer> reqItemAmt = new ArrayList();
        ArrayList<String> itemMod = new ArrayList();
        ArrayList<Integer> itemModAmt = new ArrayList();
        Scanner scanner = new Scanner(bulkText);
        scanner.useDelimiter("\t");
        text = scanner.next();
        while (scanner.hasNext()) {
            String next = scanner.next();
            String[] options = next.split(delimiter);
            switch (options[0].toLowerCase()) {
                case "ns":
                    if (options.length < 2) {
                        throw new NoSuchElementException("Next Stage missing arguments for input " + next);
                    }
                    try {
                        nextStage = Integer.parseInt(options[1]);
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Expected integer in " + next);
                    }
                    break;
                case "rs":
                    if (options.length < 3) {
                        throw new NoSuchElementException("Required stat missing arguments for input " + next);
                    }
                    reqStat.add(titleCase(options[1]));
                    try {
                        reqStatAmt.add(Integer.parseInt(options[2]));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Expected integer in " + next);
                    }
                    break;
                case "sm":
                    if (options.length < 3) {
                        throw new NoSuchElementException("Stat mod missing arguments for input " + next);
                    }
                    statMod.add(titleCase(options[1]));
                    try {
                        statModAmt.add(Integer.parseInt(options[2]));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Expected integer in " + next);
                    }
                    break;
                case "ri":
                    if (options.length < 3) {
                        throw new NoSuchElementException("Required item missing arguments for input " + next);
                    }
                    reqItem.add(titleCase(options[1]));
                    try {
                        reqItemAmt.add(Integer.parseInt(options[2]));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Expected integer in " + next);
                    }
                    break;
                case "im":
                    if (options.length < 3) {
                        throw new NoSuchElementException("Item mod missing arguments for input " + next);
                    }
                    itemMod.add(titleCase(options[1]));
                    try {
                        itemModAmt.add(Integer.parseInt(options[2]));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Expected integer in " + next);
                    }
                    break;
            }

        }
        if (nextStage < 0) {
            throw new NoSuchElementException("Next Stage must be defined");
        }
        return new Option(id, text, nextStage,
                reqStat.toArray(new String[0]), convertIntegers(reqStatAmt),
                statMod.toArray(new String[0]), convertIntegers(statModAmt),
                reqItem.toArray(new String[0]), convertIntegers(reqItemAmt),
                itemMod.toArray(new String[0]), convertIntegers(itemModAmt));
    }

    public Stage getStage(int id) {
        for (Stage stage : stages) {
            if (stage.getId() == id) {
                return stage;
            }
        }
        return null;
    }

    private int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private String titleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
