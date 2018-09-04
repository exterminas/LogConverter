package logconverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/*
This class is responsible for creating a .doc file and converting objects of 
the TextBlock class into appropriately formated text.


 */
public class DocumentCreator {

    private String name;
    private ArrayList<String> contents;

    public DocumentCreator(String name, ArrayList<String> contents) throws FileNotFoundException {
        this.name = name;
        this.contents = contents;

    }

    public void create() throws FileNotFoundException, IOException {

        Boolean started = false;
        XWPFDocument doc = new XWPFDocument();
        
        FileOutputStream os = new FileOutputStream(new File(name + ".docx"));

        //display("made is past file creation");
        for (String line : contents) {

            ArrayList<TextBlock> blocks = new ArrayList<TextBlock>();

            if (isNewPost(line) && started) {

                XWPFParagraph empty = doc.createParagraph();
                empty.setSpacingBetween(1.5);
            }

            blocks.add(new TextBlock(line));

            BBCodeParser parse = new BBCodeParser(blocks);
            parse.parse();

            XWPFParagraph para = doc.createParagraph();
            para.setSpacingBetween(1.5);

            for (TextBlock block : blocks) {
                
                XWPFRun run = para.createRun();
                run.setText(block.getText());
                
               

                if (block.getBold()) {
                    run.setBold(true);
                }

                if (block.getItalic()) {
                    run.setItalic(true);
                }

                if (block.getStrike()) {
                    run.setStrikeThrough(true);
                }

                if (block.getSub()) {
                    run.setSubscript(VerticalAlign.SUBSCRIPT);
                }

                if (block.getUnderline()) {
                    run.setUnderline(UnderlinePatterns.SINGLE);
                }

                if (block.getBig()) {
                    run.setFontSize(16);
                }

                if (block.getSmall()) {
                    run.setFontSize(10);
                }

                if (block.getSup()) {
                    run.setSubscript(VerticalAlign.SUPERSCRIPT);
                }

                if (block.getLink()) {
                    

                    System.out.println("Found this link in this list:" + block.getLinks().get(0));
                    
                    String url = block.getLinks().get(block.getLinks().size() - 1);
                    
                    String id = para.getDocument().getPackagePart().addExternalRelationship(url, XWPFRelation.HYPERLINK.getRelation()).getId();

                    CTHyperlink link = para.getCTP().addNewHyperlink();
                    
                    link.setId(id);

                    //link.setAnchor(block.getLinks().get(block.getLinks().size() - 1));

                    CTText text = CTText.Factory.newInstance();
                    text.setStringValue(block.getAnchor());
                    CTR ctr = CTR.Factory.newInstance();
                    ctr.setTArray(new CTText[]{text});

                    link.setRArray(new CTR[]{ctr});
                    
                    run.setText("");

                }

                run.setColor(block.getColor());

            }

            started = true;
        }

        //   display("trying to write");
        doc.write(os);
        os.close();

        //  display("wrote a file");
    }

    private boolean isNewPost(String line) {

        if (line.length() >= 17) {

            Pattern timeStamp = Pattern.compile("[\\Q[\\E[\\d]*-[\\d]*-[\\d]* [\\d]*:[\\d]\\Q]\\E]*");
            Matcher matcher = timeStamp.matcher(line.substring(0, 17));

            if (matcher.matches()) {

                return true;

            }
            ;
            return false;
        }

        return false;

    }

    public static void display(String text) {
        JFrame frame;

        frame = new JFrame("LogConverter");
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTextArea display = new JTextArea(text);

        // BorderLayout layout = new BorderLayout();
        frame.add(display, BorderLayout.PAGE_START);

        frame.pack();
        frame.setVisible(true);

    }

}
