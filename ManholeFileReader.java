package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */


import java.net.URL;
import java.io.*;

import java.util.Vector;
import java.util.StringTokenizer;



public class ManholeFileReader {

  public ManholeFileReader() {
  }


 /**The readManholeFile method is used to read already created Manhole file
  * This file is called by the Load submenu of Manhole Menu
  */

  static double [] us_x_coord,us_y_coord,us_z_coord;
  static int [] pipeNo, us_MH,ds_MH;
  static double [] new_us_x_coord, new_us_y_coord, new_us_z_coord;//to be used with the get and set methods
  static int [] new_pipeNo, new_us_MH, new_ds_MH;
  static int  numberOfRecords = 0;

  public void readManholeFile(String m_manholeFileNamePath)throws Exception{
       String manholeFileNamePath = m_manholeFileNamePath;
       int q = 0;//vertex number of the x and y coordinates generally it is an index of the arrays hlding different manhole information
       //tet the number of records in the manhole file
       int numberOfRecordsInManholeFile = getNoofRecordsInManholeFile(manholeFileNamePath);

//       ManualEntryFrame mEntryFrame = new ManualEntryFrame();
//       int manualEntryNoOfRecords = mEntryFrame.getNoOfRecords();

System.out.println("numberOfRecordsInManholeFile is       :"+numberOfRecordsInManholeFile);

       pipeNo = new int[numberOfRecordsInManholeFile];
       us_MH = new int[numberOfRecordsInManholeFile];
       ds_MH = new int[numberOfRecordsInManholeFile];
       us_x_coord = new double[numberOfRecordsInManholeFile];
       us_y_coord = new double[numberOfRecordsInManholeFile];
       us_z_coord = new double[numberOfRecordsInManholeFile];

       double valueX_Coord = 0.0,  valueY_Coord= 0.0, valueZ_Coord= 0.0;
       int valuePipeNo, valueUS_MH, valueDS_MH;

       String inputFile = manholeFileNamePath;
       BufferedReader in
                    = new BufferedReader(
                           new FileReader(inputFile));
       String line = "Initialise String";
       String token = "Initialise Token";

        while ( (line = in.readLine()) != null)
        {
        numberOfRecords = numberOfRecords+1;

         StringTokenizer thisLine = new StringTokenizer(line," ()");
         String parameter = thisLine.nextToken().trim();
//System.out.println("parameter is   :  "+parameter);
        //the following algorithm is to assign value of x and y coordinates excluding the delimiters i.e. the commas
           boolean endOfPipeNoValue = false, endOfUSMHValue = false, endOfDSMHValue = false, endOfXValue = false, endOfYValue = false, endOfZValue = false;
           String xValue = "",yValue="",zValue="",pipeNoValue="",usMHValue="", dsMHValue="";

          for(int z = 0;z<=parameter.length()-1;z++){
             String charOfParameter = String.valueOf(parameter.charAt(z));
            //read pipeNoValue with in each record
            if(!charOfParameter.equalsIgnoreCase(",")){
                if(!endOfPipeNoValue){
                  pipeNoValue = pipeNoValue+charOfParameter;
                  }
            }else {endOfPipeNoValue = true; }
//System.out.println("endOfPipeNoValue_1 =    "+endOfPipeNoValue);

            //read usMHValue with in each record
            if(!charOfParameter.equalsIgnoreCase(",")&& endOfPipeNoValue){
            //usMHValueStarted = true;
                if(!endOfUSMHValue){
                    usMHValue = usMHValue+charOfParameter;

                    int nextParamLocation = z+1;
                    String charOfParameterToLocateComma = String.valueOf(parameter.charAt(nextParamLocation));
                    if(endOfPipeNoValue && charOfParameterToLocateComma.equalsIgnoreCase(",")){endOfUSMHValue = true;}
                    }    }
//System.out.println("endOfUSMHValue =    "+endOfUSMHValue);

            //read dsMHValue with in each record
            if(!charOfParameter.equalsIgnoreCase(",")&&endOfPipeNoValue&&endOfUSMHValue){
                if(!endOfDSMHValue){
                    dsMHValue = dsMHValue+charOfParameter;

                    int nextParamLocation = z+1;
                    String charOfParameterToLocateComma = String.valueOf(parameter.charAt(nextParamLocation));
//System.out.println("charOfParameterToLocateComma =    "+charOfParameterToLocateComma);
                    if(endOfPipeNoValue && endOfUSMHValue && dsMHValue.length()>1 && charOfParameterToLocateComma.equalsIgnoreCase(",")){endOfDSMHValue = true;}
                    }  }
//System.out.println("endOfDSMHValue =    "+endOfDSMHValue);

            //read xValue with in each record
            if(!charOfParameter.equalsIgnoreCase(",")&&endOfPipeNoValue&&endOfUSMHValue&&endOfDSMHValue){
                if(!endOfXValue){
                    xValue = xValue+charOfParameter;

                    int nextParamLocation = z+1;
                    String charOfParameterToLocateComma = String.valueOf(parameter.charAt(nextParamLocation));
//System.out.println("charOfParameterToLocateComma =    "+charOfParameterToLocateComma);
                    if(endOfPipeNoValue && endOfUSMHValue && endOfDSMHValue && xValue.length()>1 && charOfParameterToLocateComma.equalsIgnoreCase(",")){endOfXValue = true;}
                    }  }
//System.out.println("endOfXValue =    "+endOfXValue);

            //read yValue with in each (single) record
            if(!charOfParameter.equalsIgnoreCase(",")&&endOfPipeNoValue&&endOfUSMHValue&&endOfDSMHValue&&endOfXValue){
                if(!endOfYValue){
                    yValue = yValue+charOfParameter;

                    int nextParamLocation = z+1;

                    String charOfParameterToLocateComma = " ";
           //         if(nextParamLocation<parameter.length()){
                    charOfParameterToLocateComma = String.valueOf(parameter.charAt(nextParamLocation));
//System.out.println("charOfParameterToLocateComma =    "+charOfParameterToLocateComma);
           //         }
                    if(endOfPipeNoValue && endOfUSMHValue && endOfDSMHValue && endOfXValue && yValue.length()>1 && charOfParameterToLocateComma.equalsIgnoreCase(",")){endOfYValue = true;}
                    }  }
//System.out.println("endOfYValue =    "+endOfYValue);

            //read zValue with in each) (single record
            if(!charOfParameter.equalsIgnoreCase(",")&&endOfPipeNoValue&&endOfUSMHValue&&endOfDSMHValue&&endOfXValue&&endOfYValue){
                if(!endOfZValue){
                    zValue = zValue+charOfParameter;
                    System.out.println("Z value is acccessed and its value is=    "+zValue);
                    int nextParamLocation = z+1;
                    String charOfParameterToLocateComma = " ";
                    if(nextParamLocation<parameter.length()){
                    charOfParameterToLocateComma = String.valueOf(parameter.charAt(nextParamLocation));
//System.out.println("charOfParameterToLocateComma at Z value=    "+charOfParameterToLocateComma);
                    }
                    if(endOfPipeNoValue && endOfUSMHValue && endOfDSMHValue && endOfXValue && endOfYValue&& zValue.length()>1 && charOfParameterToLocateComma.equalsIgnoreCase(",")){endOfZValue = true;}
                    }  }
//System.out.println("endOfZValue =    "+endOfZValue);



System.out.println("pipeNoValue is   "+pipeNoValue);
System.out.println("usMHValue is   "+usMHValue);
System.out.println("dsMHValue is   "+dsMHValue);
System.out.println("xValue is   "+xValue);
System.out.println("yValue is   "+yValue);
System.out.println("zValue is   "+zValue);

           }//end of the for statement to assign x and y values in string format


          // the follwing blocks are used to convert the string values to integer or double format
          int lengthOfpipeNoValue = pipeNoValue.length();
          pipeNoValue = pipeNoValue.substring(1,lengthOfpipeNoValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valuePipeNo = Integer.parseInt(pipeNoValue);
//System.out.println("valuePipeNo is   "+valuePipeNo);
          int lengthOfusMHValue = usMHValue.length();
          usMHValue = usMHValue.substring(1,lengthOfusMHValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valueUS_MH = Integer.parseInt(usMHValue);
//System.out.println("valueUS_MH is   "+valueUS_MH);
          int lengthOfdsMHValue = dsMHValue.length();
          dsMHValue = dsMHValue.substring(2,lengthOfdsMHValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valueDS_MH = Integer.parseInt(dsMHValue);
//System.out.println("valueDS_MH is   "+valueDS_MH);
          int lengthOfxValue = xValue.length();
          xValue = xValue.substring(2,lengthOfxValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valueX_Coord = Double.parseDouble(xValue);
//System.out.println("valueX_Coord is   "+valueX_Coord);
          int lengthOfyValue = yValue.length();
          yValue = yValue.substring(2,lengthOfyValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valueY_Coord = Double.parseDouble(yValue);
          int lengthOfzValue = zValue.length();
          zValue = zValue.substring(2,lengthOfzValue-1);//excluded the cotation marks at the begining and end of the xValue string
          valueZ_Coord = Double.parseDouble(zValue);
System.out.println("valuePipeNo is   "+valuePipeNo);
System.out.println("valueUS_MH is   "+valueUS_MH);
System.out.println("valueDS_MH is   "+valueDS_MH);
System.out.println("valueX_Coord is   "+valueX_Coord);
System.out.println("valueY_Coord is   "+valueY_Coord);
System.out.println("valueZ_Coord is   "+valueZ_Coord);


          //System.out.println(" is "+xValue+ "  and YValue is  "+yValue);
              // store each value in its respective array
              pipeNo[q]= valuePipeNo;
              us_MH[q]= valueUS_MH;
              ds_MH[q]= valueDS_MH;
              us_x_coord[q]=valueX_Coord;
              us_y_coord[q]=valueY_Coord;
              us_z_coord[q]= valueZ_Coord;
          q++;//increrease vertex number by one
          }//end of the while statement

        setX_CoordValue(us_x_coord);
        setY_CoordValue(us_y_coord);
        setZ_CoordValue(us_z_coord);
        setNoOfRecordsValue(numberOfRecords);
        setUpstreamManholeID(us_MH);
        setDownstreamManholeID(ds_MH);

      }//end of the readLocation file method
//new_pipeNo, new_us_MH, new_ds_MH, new_us_x_coord, new_us_y_coord, new_us_z_coord

  public void setX_CoordValue(double [] xCoord){
   new_us_x_coord = xCoord;
  }
  public double[] getX_CoordValue(){
  return new_us_x_coord;
  }

  public void setY_CoordValue(double [] yCoord){
  new_us_y_coord = yCoord;
  }
  public double[] getY_CoordValue(){
  return new_us_y_coord;
  }

  public void setZ_CoordValue(double [] zCoord){
  new_us_z_coord = zCoord;
  }
  public double[] getZ_CoordValue(){
  return new_us_z_coord;
  }

  public void setNoOfRecordsValue(int m_numberOfRecords){
  numberOfRecords = m_numberOfRecords;
  }
  public int getNoOfRecordsValue(){
  return numberOfRecords;
  }
  public void setUpstreamManholeID(int [] us_MH){
  new_us_MH = us_MH;
  }
  public int[] getUpstreamManholeID(){
  return new_us_MH;
  }
  public void setDownstreamManholeID(int [] ds_MH){
  new_ds_MH = ds_MH;
  }
  public int[] getDownstreamManholeID(){
  return new_ds_MH;
  }

  static int numberOfRecordsInManholeFile = 0;
  public int getNoofRecordsInManholeFile(String manholeFileNamePath)throws Exception{

     String inputFile = manholeFileNamePath;
     BufferedReader in
                  = new BufferedReader(
                         new FileReader(inputFile));

      String line = "Initialise String";
      while ( (line = in.readLine()) != null)
      {
      numberOfRecordsInManholeFile = numberOfRecordsInManholeFile+1;
      }
      return numberOfRecordsInManholeFile;
  }

 public int getNoofRecordsInManholeFile(){
    return numberOfRecordsInManholeFile;
 }

  }