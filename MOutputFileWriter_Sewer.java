package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.math.*;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import rationalpeak.Model.ModelCreation.*;
import rationalpeak.Data.*;



public class MOutputFileWriter_Sewer {

static Hashtable outPutHashtable = new Hashtable();

MCalendar calendar = new MCalendar();

int day = 0;
int month = 0;
int year = 0;

Object key,value,parameterObj,parameterValueObj;
Iterator itValue,itParameter,itVal,itParam;
String stValue,parameter,parameterValue;

public void writeOutputs()throws Exception{
writeOutPutHashtable();
}

private void writeOutPutHashtable()throws Exception{
    day = calendar.getCurrentDate();
    month = calendar.getCurrentMonth();
    year = calendar.getCurrentYear();

   String currentDirectory = System.getProperty("user.dir");
   String fileName4 = currentDirectory + "\\Input_Output\\RP_Sewer\\OutputFile_Sewer.rp";

    try
    {
    System.out.println("WRITING TO OutputFile_Sewer.rp");

// do not append characters to the file
 FileWriter writer3 = new FileWriter( fileName4,false );

 writer3.write( "An Output File Generated from RationalPeak Model: RP_Sewer  "+"\r\n");
 writer3.write( "Simulation Date:  "+day+"-"+month+"-"+year+"\r\n");
 writer3.write( "---------------------------------------------------------------  "+"\r\n");
 writer3.write( "PARAMETER"+"\t\t"+"VALUE"+"\r\n");
 writer3.write( "---------"+"\t\t"+"-----  "+"\r\n");
 writer3.write("    "+"\r\n");
    itValue = outPutHashtable.values().iterator();
    itParameter = outPutHashtable.keySet().iterator();


    while (itParameter.hasNext()) {

         key = itParameter.next();
         value = itValue.next();

    if(value.equals(" "))
     {stValue=("Empity");
     }else{
     stValue = String.valueOf(value).toString();
      }

writer3.write( key+"\t\t\t"+    stValue  +"\r\n");
    }

  writer3.close();
 }
 catch (IOException iox)
 {
 System.out.println("Problem writting to OutputFile.rp");
 }
}


public void setValueOfParameter(String parameter, String value){
  outPutHashtable.put(parameter, value);
  }

public String getValueOfParameter(String parameter){
 String stValue = (outPutHashtable.get(parameter)).toString();
 return stValue;
}


/*The following methods are used to write output to an excel file */

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;

  public void writeExcelFile() throws IOException, WriteException {
          String currentDirectory = System.getProperty("user.dir");
          String excelFileName = currentDirectory + "\\Input_Output\\RP_Sewer\\StormSewer_Output.xls";

          File file = new File(excelFileName/*inputFile*/);
          WorkbookSettings wbSettings = new WorkbookSettings();

          wbSettings.setLocale(new Locale("en", "EN"));

          WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
          workbook.createSheet("ProfileTable", 0);
          WritableSheet excelSheet = workbook.getSheet(0);

          createLabel(excelSheet);
          createContent(excelSheet);
          addToExcelFileInputRelatedParameters(excelSheet);

          workbook.write();
          workbook.close();
  }

    private void createLabel(WritableSheet sheet)
                    throws WriteException {
            // create a times font
            WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
            // Define the cell format
            times = new WritableCellFormat(times10pt);
            // automatically wrap the cells
            times.setWrap(true);

            // create a bold font with unterlines
            WritableFont times10ptBoldUnderline = new WritableFont(
                            WritableFont.TIMES, 10, WritableFont.BOLD, false,
                            UnderlineStyle.SINGLE);
            timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
            // automatically wrap the cells
            timesBoldUnderline.setWrap(true);

            CellView cv = new CellView();
            cv.setFormat(times);
            cv.setFormat(timesBoldUnderline);
            cv.setAutosize(true);

            // Write the headers
            addCaption(sheet, 0, 0, "S.NO");
            addCaption(sheet, 1, 0, "US_MH");
            addCaption(sheet, 2, 0, "DS_MH");
            addCaption(sheet, 3, 0, "PIPE LENGTH");
            addCaption(sheet, 4, 0, "DESIGN FLOW");
            addCaption(sheet, 5, 0, "US_GROUND ELV");
            addCaption(sheet, 6, 0, "DS_GROUND ELV");
            addCaption(sheet, 7, 0, "GROUND SLOPE");
            addCaption(sheet, 8, 0, "PIPE SIZE");
            addCaption(sheet, 9, 0, "US_PIPE INVERT");
            addCaption(sheet, 10, 0, "DS_PIPE INVERT");
            addCaption(sheet, 11, 0, "US_EXCAVATION DEPTH");
            addCaption(sheet, 12, 0, "DS_EXCAVATION DEPTH");
    }

        int serialNo = 0;
        private void createContent(WritableSheet sheet) throws WriteException,
                    RowsExceededException {

            itVal = outPutHashtable.values().iterator();
            itParam = outPutHashtable.keySet().iterator();
            while (itParam.hasNext()) {

                 parameterObj = itParam.next();
                 parameterValueObj = itVal.next();
                 String parameter = String.valueOf(parameterObj);
                 String parameterValue = String.valueOf(parameterValueObj);
 System.out.println("parameter is    "+parameter +"  and value is   "+parameterValue);

 int ID= Integer.parseInt(String.valueOf(parameter.charAt(parameter.length()-1)));//get the last index of parameter in int form
    serialNo = serialNo+1;
    String serialNoStr = String.valueOf(serialNo);
     if (parameter.equals("PipeLength_"+ID)){addParameterValue(sheet, 3, ID, parameterValue); }
     if (parameter.equals("CumulativeFlowToManhole_"+ID)){addParameterValue(sheet, 4, ID, parameterValue); }
     if (parameter.equals("GroundSlope_"+ID)){addParameterValue(sheet, 7, ID, parameterValue); }
     if (parameter.equals("PipeDiameterAtmanhole_"+ID)){addParameterValue(sheet, 8, ID, parameterValue); }
     if (parameter.equals("PipeInvertElevationAtManhole_"+ID)){addParameterValue(sheet, 9, ID, parameterValue); }
     if (parameter.equals("ExcavationDepthAtManhole_"+ID)){addParameterValue(sheet, 11, ID, parameterValue); }
           }//end of while statement
    	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addParameterValue(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

      public void addToExcelFileInputRelatedParameters(WritableSheet sheet) throws WriteException,
			RowsExceededException {
      ManholeFileReader m_ManholeFileReader = new ManholeFileReader();
        int noOfManholes = m_ManholeFileReader.getNoofRecordsInManholeFile();
        int[] usManholeIDArray= m_ManholeFileReader.getUpstreamManholeID();
        int[] dsSManholeIDArray= m_ManholeFileReader.getDownstreamManholeID();

        int currentDSManholeID = 0;
        int serNo = 0;
        int currentManholeCounter = 0;
        for(currentManholeCounter = 0; currentManholeCounter < noOfManholes-1; currentManholeCounter++){

          serNo = currentManholeCounter + 1;
          String serNoStr = String.valueOf(serNo);
          double[] z_coordArray = m_ManholeFileReader.getZ_CoordValue();
          double usMHGroundElevation = z_coordArray[currentManholeCounter];

          int currentUSManholeID = usManholeIDArray[currentManholeCounter];
          currentDSManholeID = dsSManholeIDArray[currentManholeCounter];

          int indexAtWhichDSisUS = currentUSManholeID + (currentDSManholeID-currentUSManholeID)-1;//this is the index of an array at which the currently downstream manhole is located as upstream manhole
          double dsMHGroundElevation = z_coordArray[indexAtWhichDSisUS];

          String currentUSManholeIDStr= String.valueOf(currentUSManholeID);
          String currentDSManholeIDStr= String.valueOf(currentDSManholeID);
          String usMHGroundElevationStr = String.valueOf(usMHGroundElevation);
          String dsMHGroundElevationStr = String.valueOf(dsMHGroundElevation);

          String dsInvertElev= outPutHashtable.get("PipeInvertElevationAtManhole_"+currentDSManholeIDStr).toString();
          String dsExcavationDepth= outPutHashtable.get("ExcavationDepthAtManhole_"+currentDSManholeIDStr).toString();

          addParameterValue(sheet, 0, currentManholeCounter+1, serNoStr);
          addParameterValue(sheet, 1, currentManholeCounter+1, currentUSManholeIDStr);
          addParameterValue(sheet, 2, currentManholeCounter+1, currentDSManholeIDStr);
          addParameterValue(sheet, 5, currentManholeCounter+1, usMHGroundElevationStr);
          addParameterValue(sheet, 6, currentManholeCounter+1, dsMHGroundElevationStr);

          addParameterValue(sheet, 10, currentManholeCounter+1, dsInvertElev);
          addParameterValue(sheet, 12, currentManholeCounter+1, dsExcavationDepth);
      }//end of for statement

        //add serNO as well as upstream and downstream manhole IDs to the last row
        int lastUSManholeID = currentDSManholeID;
        int lastDSManholeID = currentDSManholeID+1;
        int lastSerNo = serNo+1;
        String lastUSManholeIDStr = String.valueOf(lastUSManholeID);
        String lastDSManholeIDStr = String.valueOf(lastDSManholeID);
        String lastSerNoStr = String.valueOf(lastSerNo);
        addParameterValue(sheet, 0, currentManholeCounter+1, lastSerNoStr);
        addParameterValue(sheet, 1, currentManholeCounter+1, lastUSManholeIDStr);
        addParameterValue(sheet, 2, currentManholeCounter+1, lastDSManholeIDStr);
    }//end of method
  }//end of class

