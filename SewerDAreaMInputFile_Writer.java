package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */
 
import java.io.BufferedReader;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import java.math.*;
import java.util.*;

import rationalpeak.Model.ModelCreation.*;

//N.B This class is the formerly IOInputHashTable class
public class SewerDAreaMInputFile_Writer {

public static Hashtable inpHashtable = new Hashtable();
//static Hashtable outPutHashtable = new Hashtable();

//MRationalPeakStandard m_MRationalPeakStandard = new MRationalPeakStandard();
MCalendar calendar = new MCalendar();

public void MInputFile_Writer() {

}


/*A method used to set value of each parameter in the GUIs*/
public void setValueOfParameter(String parameter, String value){
  inpHashtable.put(parameter, value);
  }

public String getValueOfParameter(String parameter){
 String stValue = (inpHashtable.get(parameter)).toString();
 return stValue;
}

Object key,value;
Iterator itValue,itParameter;
String stValue;

public void writeHashTable() throws Exception {
int day = calendar.getCurrentDate();
int month = calendar.getCurrentMonth();
int year = calendar.getCurrentYear();

   String currentDirectory = System.getProperty("user.dir");
   String fileName3 = currentDirectory + "\\Input_Output\\RP_Sewer\\SewerDAreaInputFile.rp";

   try
    {

 System.out.println("WRITING TO InputFile.rp");
 // do not append characters to the file
 FileWriter writer2 = new FileWriter( fileName3,false );
// FileWriter writerCsv = new FileWriter( fileNameCsv,false );
 writer2.write( "RationalPeak Model: Input File  "+"\r\n");
 writer2.write( "Simulation Date:  "+day+"-"+month+"-"+year+"\r\n");
 writer2.write( "---------------------------------------------------------------  "+"\r\n");
 writer2.write( "PARAMETER"+"\t\t\t"+"    VALUE  "+"\r\n");
 writer2.write( "---------"+"\t\t\t"+"    -----  "+"\r\n");
 writer2.write("    "+"\r\n");
    itValue = inpHashtable.values().iterator();
    itParameter = inpHashtable.keySet().iterator();


    while (itParameter.hasNext()) {

         key = itParameter.next();
         value = itValue.next();

stValue = String.valueOf(value).toString();
writer2.write( key+"                            "+    stValue  +"\r\n");
    }

writer2.close();
 }
 catch (IOException iox)
 {
 System.out.println("Problem writting to InputFile.rp");
 }
}

public void clearHashTable(){
inpHashtable.clear();
}

}