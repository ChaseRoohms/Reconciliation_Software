package GUI;
//MAIN IS AT BOTTOM OF PAGE

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import org.imgscalr.Scalr;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class App {
    /*FIELDS                                                                                    */
    /*==========================================================================================*/
    private File currDupeFile;

    private File currMismatchFile;
    private File lastMismatchFile;

    private File currReconcileFile;
    private File lastReconcileFile;

    private static JLabel logoL;
    private static JLabel logoR;

    //DIMENSIONS
    private final static Dimension PREF_BUTTON_DIM = new Dimension(800, 40);
    private final static Dimension MIN_BUTTON_DIM = new Dimension(650, 40);
    private final static Dimension PREF_WINDOW_DIM = new Dimension(1200, 750);
    private final static Dimension MIN_WINDOW_DIM = new Dimension(900, 400);
    private final static Dimension PREF_FILE_DIM = new Dimension(700, 400);

    //PUBLIC FONTS TO BE USED ACROSS ALL FILES
    //INITIALIZED IN "App.resourceLoader()";
    public static Font TITLEFONT;
    public static Font BUTTONFONT;
    public static Font REPORTFONT;



    /*CONSTRUCTOR                                                                               */
    /*==========================================================================================*/
    public App() {
        //INITIALIZE FONTS AND LOGOS
        resourceLoader();

        //FILES INITIALIZE TO NULL
        currDupeFile = null;
        currMismatchFile = null;
        lastMismatchFile = null;
        currReconcileFile = null;
        lastReconcileFile = null;

        logoL.setBorder(BorderFactory.createEmptyBorder(48, 0, 0, 5));
        logoR.setBorder(BorderFactory.createEmptyBorder(48, 5, 0, 0));

        JFrame app = new JFrame();  //New Window
        app.setMinimumSize(MIN_WINDOW_DIM);
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);    //End program when closing window
        CardLayout navigation = new CardLayout();   //Layout for menu navigation
        app.setLayout(navigation);
        app.setSize(PREF_WINDOW_DIM);//Default window size
        app.setLocationRelativeTo(null);    //Center in screen

        //Menu GUI
        JPanel menu = new JPanel(); //Panel added to Card Layout
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));  //Vertical Stack

        JPanel menuOptions = new JPanel();//Sub-Panel for Menu Buttons
        JPanel menuTitle = new JPanel();//Menu Title

        menuOptions.setLayout(new GridBagLayout());
        menuTitle.setLayout(new GridBagLayout());
        GridBagConstraints gbm = new GridBagConstraints();

        //QUIT BUTTON
        gbm.gridx = 0; gbm.gridy = 1; gbm.gridheight = 1; gbm.gridwidth = 1;
        JButton menuBackOut = new JButton("Quit");
        menuBackOut.setFont(BUTTONFONT);
        menuBackOut.setPreferredSize(PREF_BUTTON_DIM);
        menuBackOut.setMinimumSize(MIN_BUTTON_DIM);
        menuOptions.add(menuBackOut, gbm);
        menuBackOut.addActionListener(e -> System.exit(0));

        //BUTTON SPACER
        gbm.gridy = 2;
        JLabel menuSpacer = new JLabel();
        menuSpacer.setText("");
        menuSpacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        menuOptions.add(menuSpacer, gbm);

        //NAVIGATE TO DUPLICATES PANEL
        gbm.gridy = 3;
        JButton dupes = new JButton("Check One File for Duplicates");
        dupes.setPreferredSize(PREF_BUTTON_DIM);
        dupes.setMinimumSize(MIN_BUTTON_DIM);
        dupes.setFont(BUTTONFONT);
        menuOptions.add(dupes, gbm);
        dupes.addActionListener(e -> navigation.show(app.getContentPane(), "DUPES"));

        //NAVIGATE TO MISMATCH PANEL
        gbm.gridy = 4;
        JButton mismatch = new JButton("Check For Mismatches in Initial and Ending Balances");
        mismatch.setPreferredSize(PREF_BUTTON_DIM);
        mismatch.setMinimumSize(MIN_BUTTON_DIM);
        mismatch.setFont(BUTTONFONT);
        menuOptions.add(mismatch, gbm);
        mismatch.addActionListener(e -> navigation.show(app.getContentPane(), "MISMATCH"));

        //NAVIGATE TO FULL ANALYSIS PANEL
        gbm.gridy = 5;
        JButton reconcile = new JButton("Complete Full Reconciliation Report");
        reconcile.setPreferredSize(PREF_BUTTON_DIM);
        reconcile.setMinimumSize(MIN_BUTTON_DIM);
        reconcile.setFont(BUTTONFONT);
        menuOptions.add(reconcile, gbm);
        reconcile.addActionListener(e -> navigation.show(app.getContentPane(), "FULL_RECONCILE"));

        gbm.gridx = 0;
        JLabel menuLogoL = logoL;
        menuTitle.add(menuLogoL, gbm);

        //MENU TITLE
        gbm.gridx = 1;
        JLabel menuTitleWord = new JLabel();
        menuTitleWord.setText("Travis County Reconciliation");
        menuTitleWord.setFont(TITLEFONT);
        menuTitleWord.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        menuTitle.add(menuTitleWord, gbm);

        gbm.gridx = 2;
        JLabel menuLogoR = logoR;
        menuTitle.add(menuLogoR, gbm);

        menu.add(menuTitle);
        menu.add(menuOptions);

        //Duplicates GUI
        JPanel dupesPanel = new JPanel();   //Panel for adding to card layout
        dupesPanel.setLayout(new BoxLayout(dupesPanel, BoxLayout.Y_AXIS));  //Vertical Stack

        JPanel dupesOptions = new JPanel(); //Sub Panel for buttons
        JPanel dupesTitle = new JPanel();   //Sub Panel for title

        dupesOptions.setLayout(new GridBagLayout());
        dupesTitle.setLayout(new GridBagLayout());

        GridBagConstraints gbd = new GridBagConstraints();

        //NAVIGATE BACK TO MENU
        gbd.gridx = 0; gbd.gridy = 1; gbd.gridheight = 1; gbd.gridwidth = 1;
        JButton dupesBackOut = new JButton("Back To Menu");
        dupesBackOut.setFont(BUTTONFONT);
        dupesBackOut.setPreferredSize(PREF_BUTTON_DIM);
        dupesBackOut.setMinimumSize(MIN_BUTTON_DIM);
        dupesOptions.add(dupesBackOut, gbd);
        dupesBackOut.addActionListener(e -> navigation.show(app.getContentPane(), "MENU"));

        //BUTTON SPACER
        gbd.gridy = 2;
        JLabel dupesSpacer = new JLabel();
        dupesSpacer.setText("");
        dupesSpacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        dupesOptions.add(dupesSpacer, gbd);

        //SELECT FILE TO CHECK FOR DUPLICATES
        gbd.gridy = 3;
        JButton dupesFileSelect = new JButton("Select an Excel File");
        dupesFileSelect.setFont(BUTTONFONT);
        dupesFileSelect.setPreferredSize(PREF_BUTTON_DIM);
        dupesFileSelect.setMinimumSize(MIN_BUTTON_DIM);
        dupesOptions.add(dupesFileSelect, gbd);
        dupesFileSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(PREF_FILE_DIM);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                currDupeFile = fileChooser.getSelectedFile();
                dupesFileSelect.setText("Current Month: " + currDupeFile.getName());
            }
        });

        //CHECK FOR DUPLICATES
        gbd.gridy = 4;
        JButton dupesFindIt = new JButton("Find Duplicates");
        dupesFindIt.setFont(BUTTONFONT);
        dupesFindIt.setPreferredSize(PREF_BUTTON_DIM);
        dupesFindIt.setMinimumSize(MIN_BUTTON_DIM);
        dupesOptions.add(dupesFindIt, gbd);
        dupesFindIt.addActionListener(e -> {
            if(currDupeFile != null){
                ReportWindowDupe dupeWin = new ReportWindowDupe(currDupeFile);
                dupeWin.setVisible(true);
                navigation.show(app.getContentPane(), "MENU");
            }
        });

        //DUPLICATE TITLE
        JLabel dupesTitleWord = new JLabel();
        dupesTitleWord.setText("Check For Duplicates");
        dupesTitleWord.setFont(TITLEFONT);
        dupesTitleWord.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        dupesTitle.add(dupesTitleWord);

        dupesPanel.add(dupesTitle);
        dupesPanel.add(dupesOptions);


        //Mismatch GUI
        JPanel mismatchPanel = new JPanel();    //Panel for adding to card layout
        mismatchPanel.setLayout(new BoxLayout(mismatchPanel, BoxLayout.Y_AXIS)); //Vertical stack

        JPanel mismatchOptions = new JPanel();  //Sub Panel for buttons
        JPanel mismatchTitle = new JPanel();    //Sub Panel for title

        mismatchOptions.setLayout(new GridBagLayout());
        mismatchTitle.setLayout(new GridBagLayout());
        GridBagConstraints gbb = new GridBagConstraints();

        //NAVIGATE BACK TO MENU
        gbb.gridx = 0; gbb.gridy = 1; gbb.gridheight = 1; gbb.gridwidth = 1;
        JButton mismatchBackOut = new JButton("Back To Menu");
        mismatchBackOut.setFont(BUTTONFONT);
        mismatchBackOut.setPreferredSize(PREF_BUTTON_DIM);
        mismatchBackOut.setMinimumSize(MIN_BUTTON_DIM);
        mismatchOptions.add(mismatchBackOut, gbb);
        mismatchBackOut.addActionListener(e -> navigation.show(app.getContentPane(), "MENU"));

        //BUTTON SPACER
        gbb.gridy = 2;
        JLabel mismatchSpacer = new JLabel();
        mismatchSpacer.setText("");
        mismatchSpacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        mismatchOptions.add(mismatchSpacer, gbb);

        //SELECT LAST MONTH'S FILE
        gbb.gridy = 3;
        JButton mismatchLastFileSelect = new JButton("Select Last Month's Excel File");
        mismatchLastFileSelect.setFont(BUTTONFONT);
        mismatchLastFileSelect.setPreferredSize(PREF_BUTTON_DIM);
        mismatchLastFileSelect.setMinimumSize(MIN_BUTTON_DIM);
        mismatchOptions.add(mismatchLastFileSelect, gbb);
        mismatchLastFileSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(PREF_FILE_DIM);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                lastMismatchFile = fileChooser.getSelectedFile();
                mismatchLastFileSelect.setText("Last Month: " + lastMismatchFile.getName());
            }
        });

        //SELECT CURRENT MONTH'S FILE
        gbb.gridy = 4;
        JButton mismatchCurrFileSelect = new JButton("Select This Month's Excel File");
        mismatchCurrFileSelect.setFont(BUTTONFONT);
        mismatchCurrFileSelect.setPreferredSize(PREF_BUTTON_DIM);
        mismatchCurrFileSelect.setMinimumSize(MIN_BUTTON_DIM);
        mismatchOptions.add(mismatchCurrFileSelect, gbb);
        mismatchCurrFileSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(PREF_FILE_DIM);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                currMismatchFile = fileChooser.getSelectedFile();
                mismatchCurrFileSelect.setText("Current Month: " + currMismatchFile.getName());
            }
        });

        //FIND MISMATCHED BALANCES BETWEEN THE TWO SELECTED FILES
        gbb.gridy = 5;
        JButton mismatchFindIt = new JButton("Find Mismatched Balances");
        mismatchFindIt.setFont(BUTTONFONT);
        mismatchFindIt.setPreferredSize(PREF_BUTTON_DIM);
        mismatchFindIt.setMinimumSize(MIN_BUTTON_DIM);
        mismatchOptions.add(mismatchFindIt, gbb);
        mismatchFindIt.addActionListener(e -> {
            if(lastMismatchFile != null && currMismatchFile != null){
                ReportWindowMismatch mismatchWin = new ReportWindowMismatch(currMismatchFile, lastMismatchFile);
                mismatchWin.setVisible(true);
                navigation.show(app.getContentPane(), "MENU");
            }
        });

        //MISMATCHES TITLE
        JLabel mismatchTitleWord = new JLabel();
        mismatchTitleWord.setText("Check For Balance Mismatches");
        mismatchTitleWord.setFont(TITLEFONT);
        mismatchTitleWord.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        mismatchTitle.add(mismatchTitleWord);

        mismatchPanel.add(mismatchTitle);
        mismatchPanel.add(mismatchOptions);

        //Full Analysis GUI
        JPanel fullAnalysisPanel = new JPanel();    //Panel for adding to card layout
        fullAnalysisPanel.setLayout(new BoxLayout(fullAnalysisPanel, BoxLayout.Y_AXIS)); //Vertical Stack

        JPanel fullAnalysisOptions = new JPanel();  //Sub Panel for buttons
        JPanel fullAnalysisTitle = new JPanel();    //Sub Panel for title

        fullAnalysisOptions.setLayout(new GridBagLayout());
        fullAnalysisTitle.setLayout(new GridBagLayout());
        GridBagConstraints gbf = new GridBagConstraints();

        //NAVIGATE TO MENU
        gbf.gridx = 0; gbf.gridy = 1; gbf.gridheight = 1; gbf.gridwidth = 1;
        JButton fullAnalysisBackOut = new JButton("Back To Menu");
        fullAnalysisBackOut.setFont(BUTTONFONT);
        fullAnalysisBackOut.setPreferredSize(PREF_BUTTON_DIM);
        fullAnalysisBackOut.setMinimumSize(MIN_BUTTON_DIM);
        fullAnalysisOptions.add(fullAnalysisBackOut, gbf);
        fullAnalysisBackOut.addActionListener(e -> navigation.show(app.getContentPane(), "MENU"));

        //BUTTON SPACER
        gbf.gridy = 2;
        JLabel fullAnalysisSpacer = new JLabel();
        fullAnalysisSpacer.setText("");
        fullAnalysisSpacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        fullAnalysisOptions.add(fullAnalysisSpacer, gbf);

        //SELECT LAST MONTH'S FILE
        gbf.gridy = 3;
        JButton fullAnalysisLastFileSelect = new JButton("Select Last Month's Excel File");
        fullAnalysisLastFileSelect.setFont(BUTTONFONT);
        fullAnalysisLastFileSelect.setPreferredSize(PREF_BUTTON_DIM);
        fullAnalysisLastFileSelect.setMinimumSize(MIN_BUTTON_DIM);
        fullAnalysisOptions.add(fullAnalysisLastFileSelect, gbf);
        fullAnalysisLastFileSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(PREF_FILE_DIM);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                lastReconcileFile = fileChooser.getSelectedFile();
                fullAnalysisLastFileSelect.setText("Last Month: " + lastReconcileFile.getName());
            }
        });

        //SELECT CURRENT MONTH'S FILE
        gbf.gridy = 4;
        JButton fullAnalysisCurrFileSelect = new JButton("Select This Month's Excel File");
        fullAnalysisCurrFileSelect.setFont(BUTTONFONT);
        fullAnalysisCurrFileSelect.setPreferredSize(PREF_BUTTON_DIM);
        fullAnalysisCurrFileSelect.setMinimumSize(MIN_BUTTON_DIM);
        fullAnalysisOptions.add(fullAnalysisCurrFileSelect, gbf);
        fullAnalysisCurrFileSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(PREF_FILE_DIM);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                currReconcileFile = fileChooser.getSelectedFile();
                fullAnalysisCurrFileSelect.setText("Current Month: " + currReconcileFile.getName());
            }
        });

        //COMPLETE FULL RECONCILIATION REPORT OF THE TWO SELECTED FILES
        gbf.gridy = 5;
        JButton fullAnalysisFindIt = new JButton("Run Full Reconciliation Report");
        fullAnalysisFindIt.setFont(BUTTONFONT);
        fullAnalysisFindIt.setPreferredSize(PREF_BUTTON_DIM);
        fullAnalysisFindIt.setMinimumSize(MIN_BUTTON_DIM);
        fullAnalysisOptions.add(fullAnalysisFindIt, gbf);
        fullAnalysisFindIt.addActionListener(e -> {
            if(lastReconcileFile != null && currReconcileFile != null){
                ReportWindowFull fullRepWin = new ReportWindowFull(currReconcileFile, lastReconcileFile);
                fullRepWin.setVisible(true);
                navigation.show(app.getContentPane(), "MENU");
            }
        });

        //RECONCILIATION TITLE
        JLabel fullAnalysisTitleWord = new JLabel();
        fullAnalysisTitleWord.setText("Full Reconciliation");
        fullAnalysisTitleWord.setFont(TITLEFONT);
        fullAnalysisTitleWord.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        fullAnalysisTitle.add(fullAnalysisTitleWord);

        fullAnalysisPanel.add(fullAnalysisTitle);
        fullAnalysisPanel.add(fullAnalysisOptions);

        //ADD PANELS TO WINDOW
        app.add(mismatchPanel, "MISMATCH");
        app.add(dupesPanel, "DUPES");
        app.add(fullAnalysisPanel, "FULL_RECONCILE");
        app.add(menu, "MENU");

        //NAVIGATE TO THE MENU
        navigation.show(app.getContentPane(), "MENU");

        //MAKE WINDOW VISIBLE
        app.setVisible(true);
    }



        /*FUNCTION                                                                                  */
        /*==========================================================================================*/
        public void resourceLoader() {
            try {
                //Create the font to use. Specify the size!
                Font title = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(
                        "/Montserrat-Black.ttf"))).deriveFont(48f);
                Font button = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(
                        "/Montserrat-Regular.ttf"))).deriveFont(25f);
                Font report = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(
                        "/Montserrat-Regular.ttf"))).deriveFont(19f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                //Register the font
                ge.registerFont(title);
                ge.registerFont(button);
                TITLEFONT = title;
                BUTTONFONT = button;
                REPORTFONT = report;
            } catch (IOException | FontFormatException e) {
                System.out.println(e.getMessage());
            }

            try{
                BufferedImage logoBufferedL = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/fileIcon.png")));
                BufferedImage logoBufferedR = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/fileIconFlip.png")));
                BufferedImage logoScaledL = Scalr.resize(logoBufferedL, 40);
                BufferedImage logoScaledR = Scalr.resize(logoBufferedR, 40);

                logoL = new JLabel(new ImageIcon(logoScaledL));
                logoR = new JLabel(new ImageIcon(logoScaledR));

            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }



        /*MAIN                                                                                      */
        /*==========================================================================================*/
        public static void main(String[] args) {
            FlatLightLaf.setup();   //INITIALIZE FLAT LIGHT LOOK AND FEEL, UI THEME DEPENDENCY
                   //RUN FUNCTION "resourceLoader", WHICH PREPARES CUSTOM FONTS

            //HAS THE POTENTIAL TO RECEIVE A NULL LOOK AND FEEL
            try {
                UIManager.setLookAndFeel(new FlatMaterialDarkerIJTheme()); //Orange and Grey
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //OPEN UI
            new App();
        }
}