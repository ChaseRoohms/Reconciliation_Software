package GUI;

import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.Math;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Algorithm {
    /*FIELDS                                                                                    */
    /*==========================================================================================*/
    private final File currFile;
    private final File lastFile;
    private final PrintStream outstream;
    private static Sheet currMonth;
    private static Sheet lastMonth;



    /*CONSTRUCTORS                                                                              */
    /*==========================================================================================*/
    //CONSTRUCTOR: ONE FILE, AND A PRINT STREAM
    public Algorithm(File userCurrFile, PrintStream userOutstream) {
        currFile = userCurrFile;
        lastFile = null;
        outstream = userOutstream;

        System.setOut(outstream);   //Print everything to given print stream
        XSSFWorkbook currBook;      //Define new excel workbook
        try {
            FileInputStream fip = new FileInputStream(currFile);    //Try to open file
            currBook = new XSSFWorkbook(fip);                       //Assign a workbook from the file
            currMonth = currBook.getSheetAt(0);               //Set currMonth to the first page of workbook
        } catch (FileNotFoundException e) {
            System.out.println(currFile.getName() + " either does not exist or can not open");
        } catch (IOException e) {
            System.out.println(currFile.getName() + " IO Exception, needs more testing");
        }

    }

    //CONSTRUCTOR: TWO FILES, AND A PRINT STREAM
    public Algorithm(File userCurrFile, File userLastFile, PrintStream userOutstream) {
        currFile = userCurrFile;
        lastFile = userLastFile;
        outstream = userOutstream;

        System.setOut(outstream);   //Print everything to users print stream
        XSSFWorkbook currBook;      //Define new excel workbook
        XSSFWorkbook lastBook;      //Define new excel workbook
        try {
            FileInputStream fip = new FileInputStream(currFile);    //Try to open file
            currBook = new XSSFWorkbook(fip);                       //Assign a workbook from the file
            currMonth = currBook.getSheetAt(0);               //Set currMonth to the first page of workbook
        } catch (FileNotFoundException e) {
            System.out.println(currFile.getName() + " either does not exist or can not open");
        } catch (IOException e) {
            System.out.println("Issues with: " + currFile.getName());
            System.out.println(e.getMessage());
        }
        try {
            FileInputStream fip = new FileInputStream(lastFile);    //Try to open file
            lastBook = new XSSFWorkbook(fip);                       //Assign a workbook from the file
            lastMonth = lastBook.getSheetAt(0);               //Set lastMonth to the first page of workbook
        } catch (FileNotFoundException e) {
            System.out.println("WARNING - ERROR " + lastFile.getName() + " either does not exist or can not open");
        } catch (IOException e) {
            System.out.println("WARNING - ERROR " + lastFile.getName() + " IO Exception, needs more testing");
        }

    }



    /*FUNCTIONS                                                                                 */
    /*==========================================================================================*/
    //ATTEMPTS TO FIND THE MONTH NAME FROM THE FILE NAME
    //IF IT FAILS RETURNS AN EMPTY STRING
    public static String monthName(File file){
        String foundMonthName = "";
        String fileName = file.getName().toLowerCase();

        //Finds month name
        if(fileName.contains("jan")){foundMonthName = "January";}
        else if(fileName.contains("feb")){foundMonthName = "February";}
        else if(fileName.contains("mar")){foundMonthName = "March";}
        else if(fileName.contains("apr")){foundMonthName = "April";}
        else if(fileName.contains("may")){foundMonthName = "May";}
        else if(fileName.contains("jun")){foundMonthName = "June";}
        else if(fileName.contains("jul")){foundMonthName = "July";}
        else if(fileName.contains("aug")){foundMonthName = "August";}
        else if(fileName.contains("sep")){foundMonthName = "September";}
        else if(fileName.contains("oct")){foundMonthName = "October";}
        else if(fileName.contains("nov")){foundMonthName = "November";}
        else if(fileName.contains("dec")){foundMonthName = "December";}

        return foundMonthName;
    }

    //RETURNS THE COLUMN THAT A SPECIFIED STRING IS FOUND IN
    public int findColNumber(String title, Sheet sheet){
        int column = 0;
        int count = 0;
        int row = 0;
        Row rowCursor;
        Cell cursor;

        while (true) {
            if (count > 50) {//Edge cases check for a file that has been shifted off of cell A1, only encountered once
                count = 0;
                column = 0;
                row++;
            }
            rowCursor = sheet.getRow(row);      //Get current row
            cursor = rowCursor.getCell(column); //Go to cell at current row, column
                //Check to see if it matches title
                if (cursor != null && cursor.toString().replace(" ", "").toLowerCase().contains(title.toLowerCase())) {
                    return column;
                }
            column++;   //If no match, check the next one
            count++;    //Again just to prevent the odd case where a file has been shifted off of starting at cell A1
        }
    }

    //RETURNS THE ROW THAT A GIVEN STRING IS FOUND IN, IN A SPECIFIED COLUMN
    public int findRowNumberInCol(String title, Sheet sheet, int column){
        int row = 0;
        Row rowCursor;
        Cell cursor;

        //Gets Grand Total Row  - Last Month
        while (true) {
            rowCursor = sheet.getRow(row);      //Go to current row
            cursor = rowCursor.getCell(column); //Get the cell in row, given column
                //Check to see if it matches title
                if (cursor != null && cursor.toString().replace(" ", "").toLowerCase().contains(title.toLowerCase())) {
                    return row;
                }
            row++;
        }
    }

    //CHECK FOR DUPLICATES GL ACCOUNTS IN "currMonth" AND PRINT TO "outstream"
    public void checkDupes() {
        String lastRead;
        int GLColCurr;
        int GLRowCurr;

        Row row;
        Cell cell;

        //Navigate to the Column and Row of GLAccount's title
        GLColCurr = findColNumber("GLAccount", currMonth);
        GLRowCurr = findRowNumberInCol("GLAccount", currMonth, GLColCurr);
        row = currMonth.getRow(GLRowCurr);
        cell = row.getCell(GLColCurr);

        System.out.println("[Duplicates]");
        System.out.println("-----------------------------------------------------------------------------------------");
        lastRead = cell.getStringCellValue();   //Store current cell for referencing
        boolean areDupes = false;               //Remembers if any duplicates have been found
        for (int i = GLRowCurr + 1; i < 100; i++) {
            row = currMonth.getRow(i);          //For every row
            cell = row.getCell(GLColCurr);      //Get the first column's cell
            if (cell != null && cell.getCellType() == CellType.STRING) {
                if (lastRead.equals(cell.getStringCellValue())) {   //Current cell matches last read cell
                    areDupes = true;
                    System.out.println("[Duplicate Found - Current Month]");
                    System.out.printf("%s%s%s%s%s%n", "    Line ", (i + 1), " | \"", lastRead, "\"");
                }
                lastRead = cell.getStringCellValue(); //Store current cell for referencing
            }
        }
        if(!areDupes){System.out.println("[No Duplicates]");}
    }

    public void compareFiles() {
        double grandTotalEnd;
        double grandTotalInit;
        int totalRowCurr;
        int totalRowLast;
        int divisionColCurr;
        int divisionColLast;
        int endingBalCol;
        int initialBalCol;
        Cell cursor;
        Row rowCursor;

        //Gets the initial Grand Total Value from the current month's sheet
        //Gets Division Column - Current Month
        divisionColCurr = findColNumber("Division", currMonth);
        //Gets Initial Balance Column - Current Month
        initialBalCol = findColNumber("InitialBalance", currMonth);
        //Gets Grand Total Row - Current Month
        totalRowCurr = findRowNumberInCol("GrandTotal", currMonth, divisionColCurr);

        rowCursor = currMonth.getRow(totalRowCurr);     //In the grand total row
        cursor = rowCursor.getCell(initialBalCol);      //Go to the initial balance column
        grandTotalInit = cursor.getNumericCellValue();  //Store the initial grand total from this month


        //Gets the ending Grand Total Value from last month's sheet
        //Gets Division Column - Last Month
        divisionColLast = findColNumber("Division", lastMonth);
        //Gets Ending Balance Column - Last Month
        endingBalCol = findColNumber("EndingBalance", lastMonth);
        //Gets Grand Total Row  - Last Month
        totalRowLast = findRowNumberInCol("GrandTotal", lastMonth, divisionColLast);

        rowCursor = lastMonth.getRow(totalRowLast);     //In the grand total row
        cursor = rowCursor.getCell(endingBalCol);       //Go to the ending balance column
        grandTotalEnd = cursor.getNumericCellValue();   //Store the ending grand total from last month

        System.out.println("[Grand Totals]");
        System.out.println("-----------------------------------------------------------------------------------------");
        if (Math.abs(grandTotalInit - grandTotalEnd) < 0.001) {
            System.out.println("[Match]");
            System.out.printf("%s%,.2f%n", "    Ending Grand Total From Last Month:    $", grandTotalEnd);
            System.out.printf("%s%,.2f%n", "    Initial Grand Total From This Month:       $", grandTotalInit);
            System.out.println();
            System.out.println();
        } else {
            System.out.println("[Don't Match]");
            System.out.printf("%s%,.2f%n", "    Ending Grand Total From Last Month:    $", grandTotalEnd);
            System.out.printf("%s%,.2f%n", "    Initial Grand Total From This Month:       $", grandTotalInit);
            System.out.println();
            System.out.println();
            //FIND THE SOURCE OF THE MISMATCH
            int divRowCurr = findRowNumberInCol("Division", currMonth, divisionColCurr);
            int divRowLast = findRowNumberInCol("Division", lastMonth, divisionColLast);
            compareFilesDeeper(totalRowCurr, totalRowLast,
                    divisionColCurr, divisionColLast, endingBalCol, initialBalCol, divRowCurr, divRowLast);
        }
    }

    public static void compareFilesDeeper(int totalRowCurr, int totalRowLast,
                                          int divisionColCurr, int divisionColLast,
                                          int endingBalCol, int initialBalCol,
                                          int headerRowCurr, int headerRowLast) {
        //Arrays to store both month's division totals
        ArrayList<double[]> currTotals = new ArrayList<>();
        ArrayList<double[]> lastTotals = new ArrayList<>();

        Row rowCursor;
        Cell cursor;


        for (int i = headerRowCurr+1; i < totalRowCurr; i++) {  //Start right after the header row
            rowCursor = currMonth.getRow(i);                    //Get current row
            cursor = rowCursor.getCell(divisionColCurr);        //Get the cell in current row, division column
            //Check if it matches "Division Total"
            if (cursor != null && cursor.toString().replace(" ", "").equals("DivisionTotal")) {
                cursor = rowCursor.getCell(initialBalCol);  //Get the Balance
                double[] divTotal = new double[2];          //Create a new array
                double value = cursor.getNumericCellValue();//Get the cell value
                divTotal[0] = value;                        //Store the cell value in index 0
                divTotal[1] = i;                            //Store the row number in index 1
                currTotals.add(divTotal);                   //Add it to the array list for current totals
            }
        }
        for (int i = headerRowLast+1; i < totalRowLast; i++) {  //Start right after the header row
            rowCursor = lastMonth.getRow(i);                    //Get current row
            cursor = rowCursor.getCell(divisionColLast);        //Get the cell in current row, division column
            //Check if it matches "Division Total"
            if (cursor != null && cursor.toString().replace(" ", "").equals("DivisionTotal")) {
                cursor = rowCursor.getCell(endingBalCol);  //Get the Balance
                double[] divTotal = new double[2];          //Create a new array
                double value = cursor.getNumericCellValue();//Get the cell value
                divTotal[0] = value;                        //Store the cell value in index 0
                divTotal[1] = i;                            //Store the row number in index 1
                lastTotals.add(divTotal);                   //Add it to the array list for current totals
            }
        }

        System.out.println("[Division Totals]");
        System.out.println("-----------------------------------------------------------------------------------------");
        //Iterate through the smaller of the two arraylists
        for (int i = 0; i < Math.min(currTotals.size(), lastTotals.size()); i++) {
            //Pull the current index's values
            double lastTotal = lastTotals.get(i)[0];
            double currTotal = currTotals.get(i)[0];

            //Compare equality with a tolerance (Double comparison issues mixed with Excel format issues)
            //Prevents compiler thinking that ($5.010000001 != $5.010000002)
            //When dealing with money we only care about ($5.01) so that is all we look at
            if (Math.abs(lastTotal - currTotal) > 0.001) {
                int currLine = (int)((currTotals.get(i)[1]) + 1);
                int lastLine = (int)((lastTotals.get(i)[1]) + 1);

                System.out.println("[Mismatch Found]");
                System.out.println("    Division total found on Line: " + lastLine + " in the last month");
                System.out.println("    Division total found on Line: " + currLine + " in the current month");
                System.out.printf("%s%,.2f%n", "    Last months:           $", lastTotal);
                System.out.printf("%s%,.2f%n", "    Current months:    $", currTotal);
                System.out.println();
            }
        }
        System.out.println();
    }
}
