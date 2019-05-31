/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Engine.AES;
import Engine.Setting;
import Player.Player;
import Player.Stat;
import Player.Weapon;
import Player.Weapons;
import Stage.DuplicateStageException;
import Stage.Option;
import Stage.Stage;
import Stage.StageManager;
import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;

/**
 *
 * @author 94tyl
 */
public class UI extends javax.swing.JFrame {

    private StageManager sm;
    private Setting set;
    private Stage currStage;
    private Player player;
    private SimpleAudioPlayer audioPlayer = null;
    private Weapons weapons;
    private String gameDir;
    private Setting gameSettings;
    private String lastAttemptedLoad = "";

    public UI() {
        initComponents();
        initColorDefaults();
        htmlPanel.add(htmlScroll);
        tabPanel.addTab("Menu", mainMenu);
        titleLabel.setText("<h1>" + "The Alternative Engine 1.0" + "</h1>");
        this.setTitle("The Alternative Engine 1.0");
        initGameSettings();
        if (!gameSettings.getValue("DefaultGame").equals("None")) {
            importGame(gameSettings.getValue("DefaultGame"));
            gameDir = gameSettings.getValue("DefaultGame");
        }
    }

    private void initGameSettings() {
        try {
            File defaultSettings = new File("defaultSettings.txt");
            if (!defaultSettings.exists()) {
                defaultSettings.createNewFile();
            }
            this.gameSettings = new Setting(defaultSettings);
            if (gameSettings.addKey("DefaultGame")) {
                gameSettings.updateValue("DefaultGame", "None");
            }
            gameSettings.writeChanges();
        } catch (IOException ex) {
            error("Critical Error", ex.getMessage());
            System.exit(100);
        }
    }

    private void importGame(String dir) {
        try {
            initSettings(dir);
        } catch (IOException ex) {
            try {
                error("IOException", "Couldn't import last played game. Was it moved?\n" + ex.getMessage());
                gameSettings.updateValue("DefaultGame", "None");
                gameSettings.writeChanges();
                return;
            } catch (FileNotFoundException ex1) {
                error("FileNotFoundException", "Couldn't import last played game. Was it moved?\n" + ex.getMessage());
                return;
            }
        }
        try {
            try {
                sm = new StageManager(new File(dir + "/" + set.getValue("Stages")), set.getValue("StageDelimiter"), Integer.parseInt(set.getValue("MaxCombatLog")));
            } catch (NumberFormatException ex) {
                error("Stage Loading Error", ex.getMessage());
                return;
            } catch (NoSuchElementException ex) {
                error("Stage Loading Error", ex.getMessage());
                return;
            } catch (DuplicateStageException ex) {
                error("Stage Loading Error", ex.getMessage());
                return;
            }
        } catch (FileNotFoundException ex) {
            error("FileNotFoundException", "Error when initializing stages:\n" + ex.getMessage());
            return;
        }
        initTextArea();
        initColor();
        initWeapons(dir);
        this.setTitle(set.getValue("GameName"));
        startGameButton.setEnabled(true);
        saveGameButton.setEnabled(true);
        loadGameButton.setEnabled(true);
        gameDir = dir;
        if (gameSettings.getValue("DefaultGame").equals("None")) {
            try {
                gameSettings.updateValue("DefaultGame", gameDir);
                gameSettings.writeChanges();
            } catch (FileNotFoundException ex) {
                error("FileNotFoundException", "Error when initializing default game save\nThis won't prevent the game from running, but you will have to re-import next time:\n" + ex.getMessage());
            }

        }
        titleLabel.setText("<html><h1>" + formatTitle(set.getValue("GameName")) + "</h1></html>");
    }

    private void initWeapons(String dir) {
        weapons = new Weapons(dir + "/" + set.getValue("Weapons"), set.getValue("StageDelimiter"));
    }

    private String formatTitle(String text) {
        if (set == null) {
            return "<p style='text-align:center;color: " + hexValue("255,255,255") + ";font-size:" + 36 + "px; font-family:" + "Arial" + "'>" + "The Alternative Engine 1.0" + "</p>";
        }
        return "<p style='text-align:center;color: " + hexValue(set.getValue("TextColor")) + ";font-size:" + Integer.parseInt(set.getValue("TitleSize")) + "px; font-family:" + set.getValue("Font") + "'>" + text + "</p>";
    }

    private void initSettings(String dir) throws FileNotFoundException, IOException {

        set = new Setting(new File(dir + "/" + "Settings.txt"));

        if (set.addKey("GameName")) {
            set.updateValue("GameName", "Test Game");
        }
        if (set.addKey("Stages")) {
            set.updateValue("Stages", "Stages.txt");
        }
        if (set.addKey("Weapons")) {
            set.updateValue("Weapons", "Weapons.txt");
        }
        if (set.addKey("StageDelimiter")) {
            set.updateValue("StageDelimiter", "@");
        }
        if (set.addKey("WindowBackground")) {
            set.updateValue("WindowBackground", "0,0,0");
        }
        if (set.addKey("TextColor")) {
            set.updateValue("TextColor", "255,255,255");
        }
        if (set.addKey("OptionColor")) {
            set.updateValue("OptionColor", "255,255,255");
        }
        if (set.addKey("TextSize")) {
            set.updateValue("TextSize", "14");
        }
        if (set.addKey("TitleSize")) {
            set.updateValue("TitleSize", "24");
        }
        if (set.addKey("Font")) {
            set.updateValue("Font", "Arial");
        }
        if (set.addKey("UseStats")) {
            set.updateValue("UseStats", "TRUE");
        }
        if (set.addKey("HitChanceStat")) {
            set.updateValue("HitChanceStat", "Agility");
        }
        if (set.addKey("HitHandicap")) {
            set.updateValue("HitHandicap", "0.25");
        }
        if (set.addKey("DamageStat")) {
            set.updateValue("DamageStat", "Strength");
        }
        if (set.addKey("DamageHandicap")) {
            set.updateValue("DamageHandicap", "0.25");
        }
        if (set.addKey("MaxCombatLog")) {
            set.updateValue("MaxCombatLog", "2");
        }
        if (set.addKey("MaxStat")) {
            set.updateValue("MaxStat", "40");
        }
        if (set.addKey("UseHealth")) {
            set.updateValue("UseHealth", "TRUE");
        }
        if (set.addKey("MaxHealth")) {
            set.updateValue("MaxHealth", "100");
        }
        if (set.addKey("StatFile")) {
            set.updateValue("StatFile", "Stats.txt");
        }
        if (set.addKey("UseInventory")) {
            set.updateValue("UseInventory", "FALSE");
        }
        if (set.addKey("UseDebug")) {
            set.updateValue("UseDebug", "FALSE");
        }
        if (set.addKey("UseHealth")) {
            set.updateValue("UseHealth", "TRUE");
        }
        if (set.addKey("ShowOptionsWhereReqsAreNotMet")) {
            set.updateValue("ShowOptionsWhereReqsAreNotMet", "TRUE");
        }
        if (set.addKey("UnusableOptionColor")) {
            set.updateValue("UnusableOptionColor", "120,120,120");
        }
        if (set.addKey("UnmetRequirementColor")) {
            set.updateValue("UnmetRequirementColor", "120,60,60");
        }
        if (set.addKey("PartialMetRequirementColor")) {
            set.updateValue("PartialMetRequirementColor", "60,120,60");
        }
        if (set.addKey("FullyMetRequirementColor")) {
            set.updateValue("FullyMetRequirementColor", "0,255,0");
        }
        set.writeChanges();
    }

    private void initPlayer(String name) {
        try {
            Setting stats = new Setting(new File(gameDir + "/" + set.getValue("StatFile")));
            if (Boolean.parseBoolean(set.getValue("UseHealth"))) {
                if (stats.addKey("Health")) {
                    stats.updateValue("Health", "100");
                    stats.writeChanges();
                }
            }
            String[] statNames = stats.getKeys();
            String[] statValues = stats.getValues();
            ArrayList<Stat> statArray = new ArrayList();
            for (int i = 0; i < statNames.length; i++) {
                try {
                    statArray.add(new Stat(statNames[i], Integer.parseInt(statValues[i]), Integer.parseInt(set.getValue("MaxStat"))));
                } catch (NumberFormatException ex) {
                    error("Stat Loading Error", ex.getMessage());
                    System.exit(-8);
                }
            }
            player = new Player(name, statArray);
            refreshStats();
            refreshInventory();
        } catch (IOException ex) {
            error("Couldn't load stat file", ex.getMessage());
            System.exit(-7);
        }
    }

    private void initTabs() {

        if (Boolean.parseBoolean(set.getValue("UseInventory"))) {
            tabPanel.addTab("Inv", invPanel);
        }
        if (Boolean.parseBoolean(set.getValue("UseStats"))) {
            tabPanel.addTab("Stats", statPanel);
        }
        if (Boolean.parseBoolean(set.getValue("UseDebug"))) {
            tabPanel.addTab("Debug", debugPanel);
        }

    }

    private void refreshStats() {
        statText.setText(player.getName() + "\n" + player.getStats());
    }

    private void refreshInventory() {
        invText.setText("Inventory\n" + player.getInventory());
    }

    private Color getColor(String colorString) {
        try {
            Scanner colorStringScan = new Scanner(colorString);
            colorStringScan.useDelimiter(",");
            int r = Integer.parseInt(colorStringScan.next());
            int g = Integer.parseInt(colorStringScan.next());
            int b = Integer.parseInt(colorStringScan.next());
            return new Color(r, g, b);
        } catch (Exception e) {
            error("Invalid Setting", "A color value specified in settings is not in the correct format");
        }
        return null;

    }

    private void initTextArea() {
        htmlScroll.setBounds(htmlPanel.getBounds());
        htmlScroll.setLocation(0, 0);
        htmlScroll.setVisible(true);
        htmlEditor.setBounds(htmlPanel.getBounds());
        htmlEditor.setLocation(0, 0);
        htmlEditor.setVisible(true);
    }

    private void updateText(String text) {
        String html = "<html>" + text + "</html>";
        htmlEditor.setText(html);
        htmlEditor.setCaretPosition(0);
    }

    private String hexValue(String color) {
        Color col = getColor(color);
        return String.format("#%02X%02X%02X", col.getRed(), col.getGreen(), col.getBlue());
    }

    private void initColor() {
        htmlScroll.setBackground(getColor(set.getValue("WindowBackground")));
        htmlScroll.setForeground(getColor(set.getValue("TextColor")));
        htmlEditor.setForeground(getColor(set.getValue("TextColor")));
        htmlEditor.setBackground(getColor(set.getValue("WindowBackground")));
        statPanel.setBackground(getColor(set.getValue("WindowBackground")));
        statPanel.setForeground(getColor(set.getValue("TextColor")));
        mainMenu.setBackground(getColor(set.getValue("WindowBackground")));
        mainMenu.setForeground(getColor(set.getValue("TextColor")));
        invPanel.setBackground(getColor(set.getValue("WindowBackground")));
        invPanel.setForeground(getColor(set.getValue("TextColor")));
        statScroll.setBackground(getColor(set.getValue("WindowBackground")));
        statScroll.setForeground(getColor(set.getValue("TextColor")));
        invScroll.setBackground(getColor(set.getValue("WindowBackground")));
        invScroll.setForeground(getColor(set.getValue("TextColor")));
        statText.setForeground(getColor(set.getValue("TextColor")));
        statText.setBackground(getColor(set.getValue("WindowBackground")));
        invText.setForeground(getColor(set.getValue("TextColor")));
        invText.setBackground(getColor(set.getValue("WindowBackground")));
        titleLabel.setBackground(getColor(set.getValue("WindowBackground")));
        titleLabel.setForeground(getColor(set.getValue("TextColor")));
        titleLabel.setFont(new Font(set.getValue("Font"), Font.BOLD, 16 + Integer.parseInt(set.getValue("TextSize"))));
        tabPanel.setFont(new Font(set.getValue("Font"), Font.BOLD, Integer.parseInt(set.getValue("TextSize"))));
        statText.setFont(new Font(set.getValue("Font"), Font.BOLD, Integer.parseInt(set.getValue("TextSize"))));
        invText.setFont(new Font(set.getValue("Font"), Font.BOLD, Integer.parseInt(set.getValue("TextSize"))));
        jCheckBox1.setBackground(getColor(set.getValue("WindowBackground")));
        jCheckBox1.setForeground(getColor(set.getValue("TextColor")));
        debugPanel.setBackground(getColor(set.getValue("WindowBackground")));
        debugPanel.setForeground(getColor(set.getValue("TextColor")));
        jLabel1.setFont(new Font(set.getValue("Font"), Font.BOLD, 14));
        jLabel2.setFont(new Font(set.getValue("Font"), Font.BOLD, 14));
        jLabel3.setFont(new Font(set.getValue("Font"), Font.BOLD, 14));
        jLabel1.setForeground(getColor(set.getValue("TextColor")));
        jLabel2.setForeground(getColor(set.getValue("TextColor")));
        jLabel3.setForeground(getColor(set.getValue("TextColor")));
    }

    private void initColorDefaults() {
        htmlScroll.setBackground(Color.BLACK);
        htmlScroll.setForeground(Color.WHITE);
        htmlEditor.setForeground(Color.WHITE);
        htmlEditor.setBackground(Color.BLACK);
        statPanel.setBackground(Color.BLACK);
        statPanel.setForeground(Color.WHITE);
        mainMenu.setBackground(Color.BLACK);
        mainMenu.setForeground(Color.WHITE);
        invPanel.setBackground(Color.BLACK);
        invPanel.setForeground(Color.WHITE);
        statScroll.setBackground(Color.BLACK);
        statScroll.setForeground(Color.WHITE);
        invScroll.setBackground(Color.BLACK);
        invScroll.setForeground(Color.WHITE);
        statText.setForeground(Color.WHITE);
        statText.setBackground(Color.BLACK);
        invText.setForeground(Color.WHITE);
        invText.setBackground(Color.BLACK);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        tabPanel.setFont(new Font("Courier New", Font.BOLD, 24));
        statText.setFont(new Font("Courier New", Font.BOLD, 24));
        invText.setFont(new Font("Courier New", Font.BOLD, 24));
        jCheckBox1.setBackground(Color.BLACK);
        jCheckBox1.setForeground(Color.WHITE);
        debugPanel.setBackground(Color.BLACK);
        debugPanel.setForeground(Color.WHITE);
        jLabel1.setFont(new Font("Courier New", Font.BOLD, 14));
        jLabel2.setFont(new Font("Courier New", Font.BOLD, 14));
        jLabel3.setFont(new Font("Courier New", Font.BOLD, 14));
        jLabel1.setForeground(Color.WHITE);
        jLabel2.setForeground(Color.WHITE);
        jLabel3.setForeground(Color.WHITE);

    }

    private void error(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private boolean confirm(String title, String message) {
        if (JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION) == 0) {
            return true;
        }
        return false;
    }

    private void message(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statPanel = new javax.swing.JPanel();
        statScroll = new javax.swing.JScrollPane();
        statText = new javax.swing.JTextArea();
        htmlScroll = new javax.swing.JScrollPane();
        htmlEditor = new javax.swing.JEditorPane();
        invPanel = new javax.swing.JPanel();
        invScroll = new javax.swing.JScrollPane();
        invText = new javax.swing.JTextArea();
        mainMenu = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        startGameButton = new javax.swing.JButton();
        saveGameButton = new javax.swing.JButton();
        loadGameButton = new javax.swing.JButton();
        importGameButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        jFileChooser1 = new javax.swing.JFileChooser();
        debugPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        tabPanel = new javax.swing.JTabbedPane();
        htmlPanel = new javax.swing.JPanel();

        statPanel.setBackground(new java.awt.Color(0, 0, 0));
        statPanel.setForeground(new java.awt.Color(255, 255, 255));

        statScroll.setBorder(null);

        statText.setEditable(false);
        statText.setColumns(20);
        statText.setLineWrap(true);
        statText.setRows(5);
        statText.setText("Stats");
        statText.setWrapStyleWord(true);
        statText.setBorder(null);
        statScroll.setViewportView(statText);

        javax.swing.GroupLayout statPanelLayout = new javax.swing.GroupLayout(statPanel);
        statPanel.setLayout(statPanelLayout);
        statPanelLayout.setHorizontalGroup(
            statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        statPanelLayout.setVerticalGroup(
            statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
        );

        htmlScroll.setBorder(null);

        htmlEditor.setEditable(false);
        htmlEditor.setBackground(new java.awt.Color(0, 0, 0));
        htmlEditor.setContentType("text/html"); // NOI18N
        htmlEditor.setForeground(new java.awt.Color(255, 255, 255));
        htmlEditor.setToolTipText("");
        htmlEditor.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                htmlEditorHyperlinkUpdate(evt);
            }
        });
        htmlScroll.setViewportView(htmlEditor);

        invText.setEditable(false);
        invText.setColumns(20);
        invText.setLineWrap(true);
        invText.setRows(5);
        invText.setText("Inventory");
        invText.setWrapStyleWord(true);
        invText.setBorder(null);
        invScroll.setViewportView(invText);

        javax.swing.GroupLayout invPanelLayout = new javax.swing.GroupLayout(invPanel);
        invPanel.setLayout(invPanelLayout);
        invPanelLayout.setHorizontalGroup(
            invPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(invScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );
        invPanelLayout.setVerticalGroup(
            invPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(invScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
        );

        mainMenu.setPreferredSize(new java.awt.Dimension(285, 591));

        jCheckBox1.setText("Mute Music");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        startGameButton.setText("New Game");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });

        saveGameButton.setText("Save");
        saveGameButton.setEnabled(false);
        saveGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGameButtonActionPerformed(evt);
            }
        });

        loadGameButton.setText("Load");
        loadGameButton.setEnabled(false);
        loadGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadGameButtonActionPerformed(evt);
            }
        });

        importGameButton.setText("Import Game");
        importGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importGameButtonActionPerformed(evt);
            }
        });

        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout mainMenuLayout = new javax.swing.GroupLayout(mainMenu);
        mainMenu.setLayout(mainMenuLayout);
        mainMenuLayout.setHorizontalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainMenuLayout.createSequentialGroup()
                        .addComponent(saveGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainMenuLayout.createSequentialGroup()
                        .addComponent(importGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addGap(0, 9, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainMenuLayout.setVerticalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(importGameButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveGameButton)
                    .addComponent(loadGameButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startGameButton)
                .addContainerGap())
        );

        jLabel1.setText("Stage Number:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Inventory Mod:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Stat Mod:");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton1.setText("Refresh Stages From File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout debugPanelLayout = new javax.swing.GroupLayout(debugPanel);
        debugPanel.setLayout(debugPanelLayout);
        debugPanelLayout.setHorizontalGroup(
            debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, debugPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addGroup(debugPanelLayout.createSequentialGroup()
                        .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1)
                            .addGroup(debugPanelLayout.createSequentialGroup()
                                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                    .addComponent(jTextField5))))))
                .addContainerGap())
        );
        debugPanelLayout.setVerticalGroup(
            debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(debugPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(debugPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 439, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));

        tabPanel.setBackground(new java.awt.Color(0, 0, 0));
        tabPanel.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPanel.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        htmlPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                htmlPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout htmlPanelLayout = new javax.swing.GroupLayout(htmlPanel);
        htmlPanel.setLayout(htmlPanelLayout);
        htmlPanelLayout.setHorizontalGroup(
            htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );
        htmlPanelLayout.setVerticalGroup(
            htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(htmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                .addGap(29, 29, 29))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(htmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (confirm("Exit Game?", "Are you sure you want to exit? All unsaved progress will be lost")) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing
    private void gotoStage(int stage) {
        Stage tempStage = currStage;
        currStage = sm.getStage(stage);
        if (currStage == null) {
            currStage = tempStage;
            error("Staging Error", "The stage this option references does not exist");
            return;
        }
        updateDebugInfo();
        playMusic();
        drawStage();
    }

    private void drawStage() {
        if (currStage.getCombat() != null) {
            saveGameButton.setEnabled(false);
            if (currStage.getCombat().isAttackingFirst()) {
                attackPlayer();
            }
            drawCombatStage();
            return;
        }
        saveGameButton.setEnabled(true);
        if (currStage.getImage().isEmpty()) {
            setStageText(false);
        } else {
            File image = new File(gameDir + "/" + currStage.getImage());
            if (!image.exists()) {
                setStageText(false);
            } else {
                setStageText(true);
            }
        }
    }

    private void setStageText(boolean hasImage) {
        if (!hasImage) {
            updateText(formatText(currStage.getText()) + "<br>" + formatOptions(currStage.getOptions()));
        } else {
            File image = new File(gameDir + "/" + currStage.getImage());
            String path = "";
            try {
                path = image.toURI().toURL().toExternalForm();
            } catch (MalformedURLException ex) {
                updateText(formatText(currStage.getText()) + "<br>" + formatOptions(currStage.getOptions()));
                return;
            }
            String pic = "<p style='text-align:center'><img " + "src='" + path + "' alt='Image not found'/></p>";
            updateText(formatText(currStage.getText()) + "<br>" + pic + "<br>" + formatOptions(currStage.getOptions()));

        }
    }

    private void setCombatStageText(boolean hasImage, boolean playerDead, boolean enemyDead) {
        if (!playerDead && !enemyDead) {
            if (!hasImage) {
                updateText(formatText(currStage.getText()) + "<br>" + formatCombatOptions() + formatOptions(currStage.getOptions()) + formatCombatLog());
            } else {
                File image = new File(gameDir + "/" + currStage.getImage());
                String path = "";
                try {
                    path = image.toURI().toURL().toExternalForm();
                } catch (MalformedURLException ex) {
                    updateText(formatText(currStage.getText()) + "<br>" + formatCombatOptions() + formatOptions(currStage.getOptions()) + formatCombatLog());
                    return;
                }
                String pic = "<p style='text-align:center'><img " + "src='" + path + "' alt='Image not found'/></p>";
                String output = formatText(currStage.getText()) + "<br>" + pic + "<br>" + formatText("Attack") + "<br>" + formatCombatOptions();
                if (currStage.getOptions().size() > 0) {
                    output += "<br>" + formatText("Other") + "<br>" + formatOptions(currStage.getOptions());
                }
                output += "<br>" + formatCombatLog();
                updateText(output);
            }
        } else if (playerDead) {
            refreshStats();
            refreshInventory();
            if (!hasImage) {
                updateText(formatText("The " + currStage.getCombat().getEnemyName() + " has struck a fatal blow") + "<br>" + formatCombatLog());
            } else {
                File image = new File(gameDir + "/" + currStage.getImage());
                String path = "";
                try {
                    path = image.toURI().toURL().toExternalForm();
                } catch (MalformedURLException ex) {
                    updateText(formatText("The " + currStage.getCombat().getEnemyName() + " has struck a fatal blow") + "<br>" + formatCombatLog());
                    return;
                }
                String pic = "<p style='text-align:center'><img " + "src='" + path + "' alt='Image not found'/></p>";
                updateText(formatText("The " + currStage.getCombat().getEnemyName() + " has struck a fatal blow") + "<br>" + pic + "<br>" + formatCombatLog());
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            currStage.getCombat().resetCombat();
            gotoStage(currStage.getCombat().getDeathStage());
        } else {
            refreshStats();
            refreshInventory();
            if (!hasImage) {
                updateText(formatText("The " + currStage.getCombat().getEnemyName() + " lays defeated") + "<br>" + formatCombatLog());
            } else {
                File image = new File(gameDir + "/" + currStage.getImage());
                String path = "";
                try {
                    path = image.toURI().toURL().toExternalForm();
                } catch (MalformedURLException ex) {
                    updateText(formatText("The " + currStage.getCombat().getEnemyName() + " lays defeated") + "<br>" + formatCombatLog());
                    return;
                }
                String pic = "<p style='text-align:center'><img " + "src='" + path + "' alt='Image not found'/></p>";
                updateText(formatText("The " + currStage.getCombat().getEnemyName() + " lays defeated") + "<br>" + pic + "<br>" + formatCombatLog());
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            currStage.getCombat().resetCombat();
            gotoStage(currStage.getCombat().getWinStage());
        }
    }

    private void drawCombatStage() {
        boolean playerDead = player.getStat("Health") == 0;
        boolean enemyDead = currStage.getCombat().getEnemyHealth() == 0;
        if (currStage.getImage().isEmpty()) {
            setCombatStageText(false, playerDead, enemyDead);
        } else {
            File image = new File(gameDir + "/" + currStage.getImage());
            if (!image.exists()) {
                setCombatStageText(false, playerDead, enemyDead);
            } else {
                setCombatStageText(true, playerDead, enemyDead);
            }
        }
        refreshStats();
        refreshInventory();
    }

    private String formatCombatLog() {
        return "<p style='color: " + hexValue(set.getValue("TextColor"))
                + "; font-size: " + set.getValue("TextSize")
                + "px; font-family: " + set.getValue("Font") + "'>"
                + currStage.getCombat().getCombatLog() + "</p>";
    }

    private String formatCombatOptions() {
        String html = "<ul style='color: " + hexValue(set.getValue("TextColor"))
                + "; font-size: " + set.getValue("TextSize")
                + "px; font-family: " + set.getValue("Font") + "'>";
        for (Weapon weapon : weapons.getWeapons()) {
            if (player.getItem(weapon.getName()) > 0) {
                if (!weapon.getReqItem().isEmpty()) {
                    if (player.getItem(weapon.getReqItem()) >= weapon.getReqItemAmt()) {
                        html += "<li><a style='color: "
                                + hexValue(set.getValue("TextColor"))
                                + "; text-decoration:none; font-size: " + set.getValue("TextSize")
                                + "px; font-family: " + set.getValue("Font")
                                + "' href='c," + weapon.getName() + "'>"
                                + formatColor("<c " + hexValue(set.getValue("FullyMetRequirementColor"))
                                        + ">[" + weapon.getReqItem()
                                        + ": " + weapon.getReqItemAmt()
                                        + "]</c>")
                                + " Use " + weapon.getName() + "</a></li>";
                    } else {
                        html += "<li><span style='color: "
                                + hexValue(set.getValue("UnusableOptionColor"))
                                + "; text-decoration:none; font-size: " + set.getValue("TextSize")
                                + "px; font-family: " + set.getValue("Font") + "'>"
                                + formatColor("<c " + hexValue(set.getValue("UnmetRequirementColor"))
                                        + ">[" + weapon.getReqItem()
                                        + ": " + weapon.getReqItemAmt()
                                        + "]</c>")
                                + " Use " + weapon.getName() + "</span></li>";
                    }
                } else {
                    html += "<li><a style='color: "
                            + hexValue(set.getValue("TextColor"))
                            + "; text-decoration:none; font-size: " + set.getValue("TextSize")
                            + "px; font-family: " + set.getValue("Font")
                            + "' href='c," + weapon.getName() + "'>Use " + weapon.getName() + "</a></li>";
                }
            }
        }
        return html + "</ul>";
    }

    private void playMusic() {
        if (!currStage.getMusic().isEmpty() && !jCheckBox1.isSelected()) {
            if (!new File(gameDir + "/" + currStage.getMusic()).exists()) {
                error("FileNotFound", "Could not locate music file for stage " + currStage.getId());
                return;
            }
            try {
                if (audioPlayer == null) {
                    audioPlayer = new SimpleAudioPlayer(gameDir + "/" + currStage.getMusic());
                    audioPlayer.play();
                } else {
                    if (audioPlayer.filePath.equals(gameDir + "/" + currStage.getMusic())) {
                        return;
                    }
                    audioPlayer.stop();
                    audioPlayer = new SimpleAudioPlayer(gameDir + "/" + currStage.getMusic());
                    audioPlayer.play();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void htmlPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_htmlPanelComponentResized
        initTextArea();
    }//GEN-LAST:event_htmlPanelComponentResized
    private String formatText(String text) {
        return "<p style='color: " + hexValue(set.getValue("TextColor")) + ";font-size:" + set.getValue("TextSize") + "px; font-family:" + set.getValue("Font") + "'>" + formatColor(text.replace("<PLAYERNAME>", player.getName())) + "</p>";
    }

    private String formatColor(String text) {
        text = text.replace("<c", "<span style='font-size:" + set.getValue("TextSize") + "px; font-family:" + set.getValue("Font") + ";color:");
        text = text.replace("</c>", "</span>");
        text = text.replace(">", "'>");
        text = text.replace("</span'>", "</span>");
        return text;
    }

    private String formatOptions(ArrayList<Option> options) {
        String html = "<ul style='color: " + hexValue(set.getValue("OptionColor"))
                + "; font-size: " + set.getValue("TextSize")
                + "px; font-family: " + set.getValue("Font") + "'>";
        boolean showOption = Boolean.parseBoolean(set.getValue("ShowOptionsWhereReqsAreNotMet"));
        for (Option option : options) {
            boolean reqsMet = reqsMet(option);
            String req = formatReqs(reqsMet, option);
            if (reqsMet) {
                html += "<li><a style='color: "
                        + hexValue(set.getValue("OptionColor"))
                        + "; text-decoration:none; font-size: " + set.getValue("TextSize")
                        + "px; font-family: " + set.getValue("Font")
                        + "' href='" + option.getId() + "," + option.getNextStage()
                        + "'>" + req + formatColor(option.getText())
                        + "</a></li>";
            } else if (showOption) {
                html += "<li><span style='color: "
                        + hexValue(set.getValue("UnusableOptionColor"))
                        + "; text-decoration:none; font-size: " + set.getValue("TextSize")
                        + "px; font-family: " + set.getValue("Font")
                        + "'>" + req + formatColor(option.getText())
                        + "</a></li>";
            }
        }
        html += "</ul>";
        return html;
    }

    private boolean reqsMet(Option option) {
        boolean reqsMet = true;
        if (option.getReqItem().length > 0) {
            for (int i = 0; i < option.getReqItem().length; i++) {
                if (player.getItem(option.getReqItem()[i]) < option.getReqItemAmt()[i]) {
                    reqsMet = false;
                }
            }
        }
        if (option.getReqStat().length > 0) {
            for (int i = 0; i < option.getReqStat().length; i++) {
                if (player.getStat(option.getReqStat()[i]) < option.getReqStatAmt()[i]) {
                    reqsMet = false;
                }
            }
        }
        return reqsMet;
    }

    private String formatReqs(boolean reqsMet, Option option) {
        String html = "";
        if (option.getReqItem().length > 0) {
            for (int i = 0; i < option.getReqItem().length; i++) {
                if (reqsMet) {
                    html += formatColor("<c " + hexValue(set.getValue("FullyMetRequirementColor")) + ">[" + option.getReqItem()[i] + ": " + option.getReqItemAmt()[i] + "] </c>");
                } else {
                    if (player.getItem(option.getReqItem()[i]) < option.getReqItemAmt()[i]) {
                        html += formatColor("<c " + hexValue(set.getValue("UnmetRequirementColor")) + ">[" + option.getReqItem()[i] + ": " + option.getReqItemAmt()[i] + "] </c>");
                    } else {
                        html += formatColor("<c " + hexValue(set.getValue("PartialMetRequirementColor")) + ">[" + option.getReqItem()[i] + ": " + option.getReqItemAmt()[i] + "] </c>");
                    }
                }

            }
        }

        if (option.getReqStat().length > 0) {
            for (int i = 0; i < option.getReqStat().length; i++) {
                if (reqsMet) {
                    html += formatColor("<c " + hexValue(set.getValue("FullyMetRequirementColor")) + ">[" + option.getReqStat()[i] + ": " + option.getReqStatAmt()[i] + "] </c>");
                } else {
                    if (player.getStat(option.getReqStat()[i]) < option.getReqStatAmt()[i]) {
                        html += formatColor("<c " + hexValue(set.getValue("UnmetRequirementColor")) + ">[" + option.getReqStat()[i] + ": " + option.getReqStatAmt()[i] + "] </c>");
                    } else {
                        html += formatColor("<c " + hexValue(set.getValue("PartialMetRequirementColor")) + ">[" + option.getReqStat()[i] + ": " + option.getReqStatAmt()[i] + "] </c>");
                    }
                }
            }
        }
        return html;
    }

    private void runOptionAlterations(int optionId) {
        Option option = currStage.getOptions().get(optionId - 1);
        if (option.getStatMod().length > 0) {
            for (int i = 0; i < option.getStatMod().length; i++) {
                player.adjustStat(option.getStatMod()[i], option.getStatModAmt()[i], Integer.parseInt(set.getValue("MaxStat")));
            }
        }
        if (option.getItemMod().length > 0) {
            for (int i = 0; i < option.getItemMod().length; i++) {
                player.adjustInv(option.getItemMod()[i], option.getItemModAmt()[i]);
            }
        }
        refreshStats();
        refreshInventory();
    }

    private void attackPlayer() {
        int enemyDamage = currStage.getCombat().enemyAttacks();
        player.adjustStat("Health", enemyDamage * -1, Integer.parseInt(set.getValue("MaxStat")));
        if (enemyDamage > 0) {
            currStage.getCombat().addToLog("The " + currStage.getCombat().getEnemyName() + " attacks you for " + enemyDamage + " damage");
        } else {
            currStage.getCombat().addToLog("The " + currStage.getCombat().getEnemyName() + " misses its attack");
        }
    }

    private void processAttack(String name) {
        Weapon weapon = weapons.getWeapon(name);
        int dmg = weapon.getDamage();
        int dmgHandicap = (int) Math.ceil((Double.parseDouble(set.getValue("DamageHandicap")) * ((double) (dmg))));
        int maxStat = Integer.parseInt(set.getValue("MaxStat"));
        int rand = (int) (Math.random() * maxStat);
        int buffer = (int) Math.ceil((Double.parseDouble(set.getValue("HitHandicap")) * (double) (maxStat)));
        int hitStat = player.getStat(set.getValue("HitChanceStat")) + buffer;
        int damageStat = player.getStat(set.getValue("DamageStat"));
        double damagePercent = (double) (damageStat) / (double) (maxStat);
        dmg = dmgHandicap + (int) Math.ceil((double) damagePercent * (double) dmg);
        if (hitStat > rand) {
            currStage.getCombat().attackEnemy(dmg);
            currStage.getCombat().addToLog("You attack the  " + currStage.getCombat().getEnemyName() + " with your " + name + " dealing " + dmg + " damage");
        } else {
            currStage.getCombat().addToLog("Your attack misses");
        }
        if (!weapon.getReqItem().isEmpty()) {
            player.adjustInv(weapon.getReqItem(), weapon.getReqItemAmt() * -1);
        }
        if (currStage.getCombat().getEnemyHealth() > 0) {
            attackPlayer();
        }
        refreshStats();
        refreshInventory();
    }
    private void htmlEditorHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_htmlEditorHyperlinkUpdate
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            new Thread(new UpdateThread(evt.getDescription())).start();
            return;
        }
    }//GEN-LAST:event_htmlEditorHyperlinkUpdate

    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        if (player != null) {
            if (!confirm("Restarting game", "Are you sure you want to start a new game? Any unsaved progress will be lost")) {
                return;
            }
        }
        String name = "";
        while (name.isEmpty()) {
            name = JOptionPane.showInputDialog(this, "Please enter your player's name: ");
            if (name == null) {
                return;
            }
        }
        initPlayer(name);
        initTabs();
        gotoStage(1);
        
    }//GEN-LAST:event_startGameButtonActionPerformed

    private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadGameButtonActionPerformed
        File saveGameDir = new File(gameDir + "/SavedGames");
        if (!saveGameDir.exists()) {
            saveGameDir.mkdir();
        }
        jFileChooser1.setCurrentDirectory(saveGameDir);
        jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            loadGame(file);
        }
    }//GEN-LAST:event_loadGameButtonActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (audioPlayer == null) {
            return;
        }
        if (jCheckBox1.isSelected()) {
            try {
                audioPlayer.stop();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                Logger.getLogger(UI.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                audioPlayer.resetAudioStream();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                Logger.getLogger(UI.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void importGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importGameButtonActionPerformed
        if (player != null) {
            if (!confirm("Restarting game", "Are you sure you want to import a game? Any unsaved progress will be lost")) {
                return;
            }
        }
        if (lastAttemptedLoad.equals("")) {
            jFileChooser1.setCurrentDirectory(null);
        } else {
            jFileChooser1.setCurrentDirectory(new File(lastAttemptedLoad));
        }
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            lastAttemptedLoad = file.getAbsolutePath();
            importGame(file.getAbsolutePath());
        }

    }//GEN-LAST:event_importGameButtonActionPerformed

    private void saveGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGameButtonActionPerformed
        File saveGameDir = new File(gameDir + "/SavedGames");
        if (!saveGameDir.exists()) {
            saveGameDir.mkdir();
        }
        jFileChooser1.setCurrentDirectory(saveGameDir);
        jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = jFileChooser1.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            if (!file.getName().endsWith(".ges")) {
                file = new File(file.getAbsolutePath() + ".ges");
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (!confirm("File Exists", "This file already exists. Would you like to overwrite it?")) {
                    return;
                }
            }
            saveGame(file);
        }

    }//GEN-LAST:event_saveGameButtonActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        int stage = Integer.parseInt(jTextField1.getText());
        gotoStage(stage);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        debugItemMod();
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        debugItemMod();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        debugStatMod();
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        debugStatMod();
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            sm = new StageManager(new File(gameDir + "/" + set.getValue("Stages")), set.getValue("StageDelimiter"), Integer.parseInt(set.getValue("MaxStat")));
        } catch (Exception ex) {
            error("Load Error", ex.getMessage());
        }
        drawStage();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void updateDebugInfo() {
        jTextField1.setText(currStage.getId() + "");
    }

    private void debugStatMod() {
        String stat = "";
        int amt = 0;
        try {
            stat = jTextField4.getText();
            amt = Integer.parseInt(jTextField5.getText());
        } catch (NumberFormatException e) {
            error("Bad value", e.getMessage());
            return;
        }
        if (stat.isEmpty()) {
            error("Bad value", "Item cannot be empty");
            return;
        }
        player.adjustStat(stat, amt, Integer.parseInt(set.getValue("MaxStat")));
        refreshStats();
    }

    private void debugItemMod() {
        String item = "";
        int qty = 0;
        try {
            item = jTextField2.getText();
            qty = Integer.parseInt(jTextField3.getText());
        } catch (NumberFormatException e) {
            error("Bad value", e.getMessage());
            return;
        }
        if (item.isEmpty()) {
            error("Bad value", "Item cannot be empty");
            return;
        }
        player.adjustInv(item, qty);
        refreshInventory();
    }

    private void saveGame(File file) {
        AES aes = new AES();
        aes.setKey("4psJTBdj4WTtPxVDpHDZGd2nHWdL");
        String inv = player.getInventory();
        String stats = player.getStats();
        String stage = currStage.getId() + "";
        PrintStream printer = null;
        try {
            printer = new PrintStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        String output = player.getName() + "\n" + "########\n" + inv + "########\n" + stats + "########\n" + stage;
        output = aes.encrypt(output);
        printer.print(output);
    }

    private void loadGame(File file) {
        AES aes = new AES();
        aes.setKey("4psJTBdj4WTtPxVDpHDZGd2nHWdL");
        Scanner fileScan = null;
        String fileText = "";
        int stage = 1;
        try {
            fileScan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        int step = 0;
        while (fileScan.hasNextLine()) {
            fileText += fileScan.nextLine();
        }
        fileText = aes.decrypt(fileText);
        fileScan = new Scanner(fileText);
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            if (line.equals("########")) {
                step++;
                continue;
            }
            if (step == 0) {
                initPlayer(line);
                player.resetPlayer();
            } else if (step == 1) {
                String[] parse = line.split(":");
                if (parse.length == 1) {
                    player.setInv(parse[0], 1);
                } else {
                    player.setInv(parse[0], Integer.parseInt(parse[1].trim()));
                }
            } else if (step == 2) {
                String[] parse = line.split(":");
                player.setStat(parse[0], Integer.parseInt(parse[1].trim()), Integer.parseInt(set.getValue("MaxStat")));
            } else {
                stage = Integer.parseInt(line);

            }
        }
        initTabs();
        refreshStats();
        refreshInventory();
        gotoStage(stage);
    }

    public class UpdateThread implements Runnable {

        String desc = "";

        public UpdateThread(String desc) {
            this.desc = desc;
        }

        @Override
        public void run() {
            if (desc.split(",")[0].equals("c")) {
                processAttack(desc.split(",")[1]);
                if (currStage.getCombat() != null) {
                    drawCombatStage();
                }
                return;
            }

            int nextStage = -1;
            nextStage = Integer.parseInt(desc.split(",")[1]);
            int selectedOption = Integer.parseInt(desc.split(",")[0]);
            if (selectedOption > 0) {
                runOptionAlterations(selectedOption);
            }
            if (nextStage > 0) {
                gotoStage(nextStage);
            } else {
                drawStage();
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel debugPanel;
    private javax.swing.JEditorPane htmlEditor;
    private javax.swing.JPanel htmlPanel;
    private javax.swing.JScrollPane htmlScroll;
    private javax.swing.JButton importGameButton;
    private javax.swing.JPanel invPanel;
    private javax.swing.JScrollPane invScroll;
    private javax.swing.JTextArea invText;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton loadGameButton;
    private javax.swing.JPanel mainMenu;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton saveGameButton;
    private javax.swing.JButton startGameButton;
    private javax.swing.JPanel statPanel;
    private javax.swing.JScrollPane statScroll;
    private javax.swing.JTextArea statText;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
