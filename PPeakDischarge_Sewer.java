package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */
 
 import java.lang.Math;

 import java.util.Vector;
 import java.util.Enumeration;
 import java.util.Hashtable;
 import java.util.Iterator;

public class PPeakDischarge_Sewer {

 SewerDAreaMInputFile_Writer m_IOInputHashTable = new SewerDAreaMInputFile_Writer();
 MDAreaInputFileReader_Sewer m_MDAreaInputFileReader_Sewer = new MDAreaInputFileReader_Sewer();
 MOutputFileWriter_Sewer m_MOutputFileWriter_Sewer = new MOutputFileWriter_Sewer();

  public PPeakDischarge_Sewer() {
  }

  public void calculatePeakDischarge(){

  //String totalNoOfSubareasString = m_IOInputHashTable.getValueOfParameter("totalNoOfDrainageAreas");
   String totalNoOfSubareasString = "";
        try{
      totalNoOfSubareasString = m_MDAreaInputFileReader_Sewer.getValueOfParameter("totalNoOfDrainageAreas");
                 }catch(Exception r){
              System.out.println(r);
              }
  double totalNoOfSubareas = Integer.parseInt(totalNoOfSubareasString);

  for (int currentSubAreaNo = 1; currentSubAreaNo <= totalNoOfSubareas; currentSubAreaNo++){
      //get area of each subarea in sq. meter
      String areaOfSubareaString = "";
      try{
      areaOfSubareaString = m_MDAreaInputFileReader_Sewer.getValueOfParameter("AreaofDrainageArea_"+currentSubAreaNo);//value set in RP_SewerInterface
                       }catch(Exception r){
              System.out.println(r);
              }

      double areaOfSubarea = Double.parseDouble(areaOfSubareaString);

      //get runoff Coeff Of each Subarea
      String runoffCoeffOfSubareaString="";
      try{
      runoffCoeffOfSubareaString = m_MDAreaInputFileReader_Sewer.getValueOfParameter("WeightedRunoffCoeff_"+currentSubAreaNo);//value set in PRunoffCoefficient_Sewer
                         }catch(Exception r){
          System.out.println(r);
          }
      double runoffCoeffOfSubarea = Double.parseDouble(runoffCoeffOfSubareaString);

      //get critical rainfall intensity of each Subarea in mm/hr
      String rainfallIntensityString = "";
      try{
      rainfallIntensityString = m_MDAreaInputFileReader_Sewer.getValueOfParameter("RainfallIntensity_"+currentSubAreaNo);//value set in GeneralDAreaInfoFrame
                               }catch(Exception r){
          System.out.println(r);
          }
      double rainfallIntensity = Double.parseDouble(rainfallIntensityString);

      System.out.println("areaOfSubarea:  "+areaOfSubarea+"  runoffCoeffOfSubarea:   "+runoffCoeffOfSubarea+"  RainfallIntensity:  "+rainfallIntensity);

      //calculate peak discharge in metre cube per second
      double peakDischarge = (areaOfSubarea*runoffCoeffOfSubarea*rainfallIntensity)/(1000/*mm to meter*/*3600/*hr to seconds*/);

      //convert peak discharge to litre per second
      peakDischarge = peakDischarge*1000;
      //roundoff to three decimal places
      peakDischarge = Math.round(peakDischarge*1000)/1000.0;
      //set peak discharge value
      String peakDischargeStr = String.valueOf(peakDischarge);
      m_IOInputHashTable.setValueOfParameter("PeakDischarge_"+currentSubAreaNo,peakDischargeStr);
      m_MOutputFileWriter_Sewer.setValueOfParameter("PeakDischargeFromSubarea_"+currentSubAreaNo,peakDischargeStr);
//System.out.println("Peak discharge in litre per socond is  "+peakDischarge);
  }//end of for statement



  }
}