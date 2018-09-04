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

    public static void main(String[] args) throws FileNotFoundException, IOException {

        //get files from currenty directory
        String dir = System.getProperty("user.dir");
        //   System.out.println(dir);
        File path = new File(dir);
        File[] files = path.listFiles();

        //go through all the files
        if (!files.equals(null)) {
            for (File child : files) {

                if (isTextFile(child)) {

                    Scanner sc = new Scanner(child, "utf-8");

                    ArrayList<String> contents = new ArrayList<String>();

                    while (sc.hasNextLine()) {
                        contents.add(sc.nextLine());

                    }

                    //hand the contents to a DocumentCreator
                    DocumentCreator creator = new DocumentCreator(shortenName(child), contents);
                    creator.create();

                } else {
                    System.out.print(child.getName() + " is not a text file");
                }

            }
        }

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
        return file.getName().substring(0, file.getName().length() - 4);
    }

}
