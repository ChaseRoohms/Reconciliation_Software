package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class ReportWindowFull extends JFrame{
    /*FIELD                                                                                     */
    /*==========================================================================================*/
    JTextArea ta;



    /*CONSTRUCTOR                                                                               */
    /*==========================================================================================*/
    public ReportWindowFull(File currFile, File lastFile) {
        String lastMonth = "Last Month";
        String currMonth = "Current Month";

        String currCheck = Algorithm.monthName(currFile);
        String lastCheck = Algorithm.monthName(lastFile);

        if(!currCheck.isEmpty()){currMonth = currCheck;}
        if(!lastCheck.isEmpty()){lastMonth = lastCheck;}


        setTitle("REPORT - FULL ANALYSIS");
        setLayout(new BorderLayout());
        setSize(1200, 750);
        setLocationRelativeTo(null);

        ta = new JTextArea(100,40);
        ta.setFont(App.REPORTFONT);

        add(BorderLayout.CENTER, new JScrollPane(ta));
        PrintStream outStream = new PrintStream(new TextAreaOutputStream(ta));

        Algorithm fullReport = new Algorithm(currFile, lastFile, outStream);
        fullReport.compareFiles();
        fullReport.checkDupes();

        JLabel reportHeader = new JLabel();
        reportHeader.setText("Full Report: " + lastMonth + " - " + currMonth);
        reportHeader.setFont(App.TITLEFONT);
        add(BorderLayout.NORTH, reportHeader);



        JButton toText = new JButton("Save Report as Text File");
        toText.setFont(App.BUTTONFONT);
        toText.setPreferredSize(new Dimension(800, 80));
        toText.setMinimumSize(new Dimension(650, 80));
        add(BorderLayout.SOUTH, toText);
        toText.addActionListener(e -> {
            saveAs();
        });

        setVisible(true);
    }



    /*FUNCTION                                                                                  */
    /*==========================================================================================*/
    public void saveAs() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // !! File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        try (BufferedWriter outFile = new BufferedWriter(new FileWriter(file))) {

            ta.write(outFile);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}