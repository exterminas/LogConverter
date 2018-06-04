package logconverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class LogConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        

        //get files from currenty directory
        String dir = System.getProperty("user.dir");
     //   System.out.println(dir);
        File path = new File(dir);
        File[] files = path.listFiles();

        
     

        //go through all the files
        if (!files.equals(null)) {
            for (File child : files) {
               // System.out.println(child.getName());

                //see if they are .txt
                if (isTextFile(child)) {
                    
                   // display(child.getName() + "is a text file");
                    Scanner sc = new Scanner(child, "utf-8");
                    
                    ArrayList<String> contents = new ArrayList<String>();
                    
                    while (sc.hasNextLine()) {
                        contents.add(sc.nextLine());
                    
                    }
                                        
                    
                    DocumentCreator creator = new DocumentCreator(shortenName(child), contents);
                    creator.create();
                    
                    
                    
                    

                } else {
                 //   display(child.getName() + " is not a text file");
                }

            }
        }

        //go through all files
        //check if it's a .txt log
        //for those, read out the content
        //make a new text file. 
        //Put the content in there.
        //format
    }

    public static void display(String text) {
        JFrame frame;

        frame = new JFrame("LogConverter");
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTextArea display = new JTextArea(text);

        // BorderLayout layout = new BorderLayout();
        frame.add(display, BorderLayout.PAGE_START);

        frame.pack();
        frame.setVisible(true);

    }

    private static boolean isTextFile(File child) {
        String name = child.getName();

        if (name.length() >= 4) {
            if (name.substring(name.length() - 4).equals(".txt")) {
                return true;

            }

        }

        return false;
    }
    
        private static String shortenName(File file) {
        return file.getName().substring(0, file.getName().length()-4);
    }
    

}
