/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stage;

/**
 *
 * @author 94tyl
 */
public class Option {

    private int id;
    private String text;
    private int nextStage;
    private String[] reqStat;
    private int[] reqStatAmt;
    private String[] statMod;
    private int[] statModAmt;
    private String[] reqItem;
    private int[] reqItemAmt;
    private String[] itemMod;
    private int[] itemModAmt;

    public Option(int id, String text, int nextStage, String[] reqStat, int[] reqStatAmt, String[] statMod, int[] statModAmt, String[] reqItem, int[] reqItemAmt, String[] itemMod, int[] itemModAmt) {
        this.id = id;
        this.text = text;
        this.nextStage = nextStage;
        this.reqStat = reqStat;
        this.reqStatAmt = reqStatAmt;
        this.statMod = statMod;
        this.statModAmt = statModAmt;
        this.reqItem = reqItem;
        this.reqItemAmt = reqItemAmt;
        this.itemMod = itemMod;
        this.itemModAmt = itemModAmt;
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

    /**
     * @return the nextStage
     */
    public int getNextStage() {
        return nextStage;
    }

    /**
     * @return the reqStat
     */
    public String[] getReqStat() {
        return reqStat;
    }

    /**
     * @return the reqStatAmt
     */
    public int[] getReqStatAmt() {
        return reqStatAmt;
    }

    /**
     * @return the statMod
     */
    public String[] getStatMod() {
        return statMod;
    }

    /**
     * @return the statModAmt
     */
    public int[] getStatModAmt() {
        return statModAmt;
    }

    /**
     * @return the reqItem
     */
    public String[] getReqItem() {
        return reqItem;
    }

    /**
     * @return the reqItemAmt
     */
    public int[] getReqItemAmt() {
        return reqItemAmt;
    }

    /**
     * @return the itemMod
     */
    public String[] getItemMod() {
        return itemMod;
    }

    /**
     * @return the itemModAmt
     */
    public int[] getItemModAmt() {
        return itemModAmt;
    }
}
