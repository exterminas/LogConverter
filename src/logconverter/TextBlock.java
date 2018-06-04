/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logconverter;

import java.util.ArrayList;


public class TextBlock {

    private Boolean bold;
    private Boolean italic;
    private Boolean strike;
    private String color;
    private String text;
    private Boolean small;
    private Boolean lower;
    private Boolean underline;
    private Boolean big;
    private Boolean sub;
    private Boolean sup;
    private Boolean link;
    private ArrayList<String> links;
    private String anchor;
    

    public TextBlock(String text) {
        this.text = text;
        bold = false;
        italic = false;
        color = "000000";
        strike = false;
        small = false;
        lower = false;
        underline = false;
        this.big = false;
        this.sub = false;
        this.sup = false;
        this.link = false;
        this.links = new ArrayList<String>();
        this.anchor = "";
    }

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getStrike() {
        return strike;
    }

    public void setStrike(Boolean strike) {
        this.strike = strike;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

    public Boolean getSmall() {
        return small;
    }

    public void setSmall(Boolean small) {
        this.small = small;
    }

    public Boolean getLower() {
        return lower;
    }

    public void setLower(Boolean lower) {
        this.lower = lower;
    }

    public Boolean getUnderline() {
        return underline;
    }

    public void setUnderline(Boolean underline) {
        this.underline = underline;
    }

    public Boolean getBig() {
        return big;
    }

    public void setBig(Boolean big) {
        this.big = big;
    }

    public Boolean getSub() {
        return sub;
    }

    public void setSub(Boolean sub) {
        this.sub = sub;
    }

    public Boolean getSup() {
        return sup;
    }

    public void setSup(Boolean sup) {
        this.sup = sup;
    }

    public Boolean getLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }
    
    

}
