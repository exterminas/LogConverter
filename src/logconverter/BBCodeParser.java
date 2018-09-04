/*

This class handles the conversion of BBCode-Tags
It detects tags in the plain text, removes them, and sets the tagged text 
into appropriately flagged objects of the TextBlock class
TextBlocks are later used as the basis for .doc generation.


 */
package logconverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class BBCodeParser {

    private ArrayList<ArrayList<String>> openingTags;
    private HashMap<ArrayList<String>, String> tagMap;
    private ArrayList<TextBlock> blocks;
    private ArrayList<String> simpleTags;
    private ArrayList<String> simpleOpenings;

    public BBCodeParser(ArrayList<TextBlock> blocks) {
        this.blocks = blocks;
        this.openingTags = new ArrayList<ArrayList<String>>();
        this.simpleTags = new ArrayList<String>();
        this.simpleTags = new ArrayList<String>();

        String[] arrayOpeners = {"[b]", "[i]", "[s]", "[color=red]", "[color=blue]", "[color=white]", "[color=yellow]", "[color=pink]", "[color=gray]", "[color=green]", "[color=orange]", "[color=cyan]", "[color=brown]", "[color=black]", "[color=purple]", "[sub]", "[u]", "[big]", "[small]", "[sup]"};
        String[] arrayClosers = {"[/b]", "[/i]", "[/s]", "[/color]", "[/sub]", "[/u]", "[/big]", "[/small]", "[/sup]"};

        simpleTags.addAll(Arrays.asList(arrayClosers));
        simpleTags.addAll(Arrays.asList(arrayOpeners));

        this.simpleOpenings = new ArrayList<String>();
        simpleOpenings.addAll(Arrays.asList(arrayOpeners));

        setupTagList();

    }

    private void setupTagList() {

        tagMap = new HashMap<ArrayList<String>, String>();

        addTags(openingTags, tagMap, "[b]", "[/b]");

        addTags(openingTags, tagMap, "[i]", "[/i]");

        ArrayList<String> cList = new ArrayList<String>();
        cList.add("\\Q[color=red]\\E");
        cList.add("\\Q[color=blue]\\E");
        cList.add("\\Q[color=white]\\E");
        cList.add("\\Q[color=yellow]\\E");
        cList.add("\\Q[color=pink]\\E");
        cList.add("\\Q[color=gray]\\E");
        cList.add("\\Q[color=green]\\E");
        cList.add("\\Q[color=orange]\\E");
        cList.add("\\Q[color=cyan]\\E");
        cList.add("\\Q[color=brown]\\E");
        cList.add("\\Q[color=black]\\E");
        cList.add("\\Q[color=purple]\\E");
        this.openingTags.add(cList);
        this.tagMap.put(cList, "\\Q[/color]\\E");

        addTags(openingTags, tagMap, "[s]", "[/s]");

        addTags(openingTags, tagMap, "[sub]", "[/sub]");

        addTags(openingTags, tagMap, "[u]", "[/u]");

        addTags(openingTags, tagMap, "[big]", "[/big]");

        addTags(openingTags, tagMap, "[small]", "[/small]");
        addTags(openingTags, tagMap, "[sup]", "[/sup]");

    }

    public void parse() {

        ListIterator<TextBlock> it = blocks.listIterator();

        //iterate through all text blocks
        while (it.hasNext()) {

            //check if it has any tags
            if (hasTags(it.next().getText())) {

                TextBlock curBlock = it.previous();

                 System.out.println("The current Text is:" + curBlock.getText());
                //find the first tag in the first text block
                String firstTag = findFirst(curBlock);

                 System.out.println("The first Tag I found is:" + firstTag);
                //split the String at the first occurence of the first tag
                String[] firstSplit = curBlock.getText().split("\\Q" + firstTag + "\\E", 2);

                  System.out.println("I broke the text into these parts:");
                  for (String blub : firstSplit) {
                      System.out.println(blub);
                  }
                String[] secondSplit;

                if (!firstTag.contains("url")) {

                    System.out.println("First tag found: " + firstTag);
                    //find the matching tagFamily of the first Tag;
                    ArrayList<String> tagFamily = findFamily(firstTag);

                     System.out.println("Splitting these parts at: " + tagMap.get(tagFamily));
                    //split the rest at the first instance of the matching closing tagt
                    secondSplit = firstSplit[1].split(tagMap.get(tagFamily), 2);

                } else {

                    System.out.println("SPECIAL URL CASE TRIGGERED");
                    secondSplit = firstSplit[1].split("\\Q[/url]\\E", 2);

                }

                if (secondSplit.length < 2) {
                    display("WARNING: One or more of your logs contained improper use of BBCode. There will be some unresolved tags left!");
                    break;
                }

                // System.out.println("I made these parts in the second split:");
                //  for (String part : secondSplit) {
                //      System.out.println(part);
                // }
                it.remove();

                TextBlock before = new TextBlock(firstSplit[0]);
                inherit(before, curBlock);
                it.add(before);

                TextBlock between = new TextBlock(secondSplit[0]);
                inherit(between, curBlock);
                applyTag(between, firstTag);
                it.add(between);

                TextBlock after = new TextBlock(secondSplit[1]);
                inherit(after, curBlock);
                it.add(after);

                it.previous();
                it.previous();

            }
        }

    }

    private String findFirst(TextBlock block) {

        int curSmall = block.getText().length();
        String curTag = "";

        //    System.out.println("Scanning this text:" + block.getText());
        for (String tag : this.simpleOpenings) {

            //    System.out.println("checking for this tag" + tag);
            Integer firstFind = block.getText().indexOf(tag);

            //  System.out.println("found it at index" + firstFind);
            if (firstFind < curSmall && !(firstFind == -1)) {

                //  System.out.println("This is smaller than the previous smallest, which was" + curSmall);
                curSmall = firstFind;

                curTag = tag;

                //  System.out.println("So right now the first opening tag I found is " + curTag);
            } else {
                // System.out.println("Since this is -1; " + firstFind + " I won't change a thing");
            }

        }

        Pattern link = Pattern.compile("\\Q[\\Eurl=(.)*?\\Q]\\E");

        Matcher matcher = link.matcher(block.getText());

        Integer firstURL = matcher.find() ? matcher.start() : -1;

        if (firstURL < curSmall && !(firstURL == -1)) {
          //  System.out.println("found this url: " + matcher.group());
            return matcher.group();

        }
        


        // System.out.println("The first Tag in " + block.getText() + " is " + curTag);
        return curTag;
    }

    private boolean hasTags(String line) {

        for (String tag : simpleTags) {
            if (line.contains(tag)) {

                return true;

            }

        }

        if (line.contains("[url") || line.contains("[/url]")) {
            return true;
        }

        return false;

    }

    private void inherit(TextBlock before, TextBlock curBlock) {
        if (curBlock.getBold()) {
            before.setBold(Boolean.TRUE);

        }

        if (curBlock.getItalic()) {
            before.setItalic(Boolean.TRUE);

        }

        if (curBlock.getStrike()) {
            before.setStrike(Boolean.TRUE);
        }

        if (!curBlock.getColor().equals("")) {
            before.setColor(curBlock.getColor());
        }

        if (curBlock.getSmall()) {
            before.setSmall(true);
        }

        if (curBlock.getLower()) {
            before.setLower(Boolean.TRUE);
        }

        if (curBlock.getUnderline()) {
            before.setUnderline(Boolean.TRUE);
        }

        if (curBlock.getBig()) {
            before.setBig(Boolean.TRUE);
        }

        if (curBlock.getSmall()) {
            before.setSmall(Boolean.TRUE);
        }

        if (curBlock.getSub()) {
            before.setSub(Boolean.TRUE);
        }

        if (curBlock.getSup()) {
            before.setSup(Boolean.TRUE);
        }

        if (curBlock.getLink()) {
            before.setLink(Boolean.TRUE);
            before.getLinks().add(curBlock.getLinks().get(curBlock.getLinks().size()));
        }

    }

    private void applyTag(TextBlock between, String firstTag) {
        if (firstTag.equals("[b]")) {
            between.setBold(Boolean.TRUE);
            //  System.out.println("setting " + between.getText() + " to bold");
        }

        if (firstTag.equals("[i]")) {
            between.setItalic(Boolean.TRUE);

        }

        if (firstTag.equals("[s]")) {
            between.setStrike(Boolean.TRUE);

        }

        if (firstTag.equals("[color=red]")) {
            between.setColor("FF0000");

        }

        if (firstTag.equals("[color=blue]")) {
            between.setColor("0000FF");

        }

        if (firstTag.equals("[color=white]")) {
            between.setColor("FFFFFF");

        }

        if (firstTag.equals("[color=yellow]")) {
            between.setColor("FFFF00");

        }

        if (firstTag.equals("[color=pink]")) {
            between.setColor("FD67DF");

        }

        if (firstTag.equals("[color=gray]")) {
            between.setColor("808080");

        }

        if (firstTag.equals("[color=green]")) {
            between.setColor("006600");

        }

        if (firstTag.equals("[color=orange]")) {
            between.setColor("CC6600");

        }

        if (firstTag.equals("[color=cyan]")) {
            between.setColor("00FFFF");

        }

        if (firstTag.equals("[color=brown]")) {
            between.setColor("A52A2A");

        }

        if (firstTag.equals("[color=black]")) {
            between.setColor("000000");

        }

        if (firstTag.equals("[color=purple]")) {
            between.setColor("800080");

        }

        if (firstTag.equals("[sub]")) {
            between.setSub(Boolean.TRUE);

        }

        if (firstTag.equals("[u]")) {
            between.setUnderline(Boolean.TRUE);

        }

        if (firstTag.equals("[big]")) {
            between.setBig(Boolean.TRUE);
        }

        if (firstTag.equals("[small]")) {
            between.setSmall(Boolean.TRUE);
        }

        if (firstTag.equals("[sup]")) {
            between.setSup(Boolean.TRUE);
        }

        if (firstTag.contains("url")) {
            between.setLink(Boolean.TRUE);
            String url = extractURL(firstTag);
            // System.out.println(url);
            between.getLinks().add(url);
            between.setAnchor(between.getText());
            between.setText("");

        }

    }

    private ArrayList<String> findFamily(String firstTag) {
        for (ArrayList<String> list : this.openingTags) {
            if (list.contains("\\Q" + firstTag + "\\E")) {
                return list;

            }

        }

        System.out.println("Error: Tag Family not found for " + firstTag);
        return null;
    }

    public static void display(String text) {
        JFrame frame;

        frame = new JFrame("LogConverter");
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTextArea display = new JTextArea(1, 2);
        display.setText(text);
        display.setEditable(false);
        display.setWrapStyleWord(true);
        display.setLineWrap(true);
        display.setCaretPosition(display.getDocument().getLength());

        
        frame.add(display, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

    }

    private void addTags(ArrayList<ArrayList<String>> openingTags, HashMap<ArrayList<String>, String> tagMap, String open, String close) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("\\Q" + open + "\\E");
        this.openingTags.add(list);
        this.tagMap.put(list, "\\Q" + close + "\\E");
    }

    private String extractURL(String firstTag) {

        String firstClean = firstTag.substring(5, firstTag.length() - 1);
        //System.out.println(firstClean);
        String[] parts = firstClean.split("]");

        // System.out.println(parts[0]);
        return parts[0];
    }

    private String extractAnchor(String firstTag) {
        String firstClean = firstTag.substring(5, firstTag.length() - 6);
        //System.out.println(firstClean);
        String[] parts = firstClean.split("]");

        // System.out.println(parts[0]);
        return parts[1];
    }

}
