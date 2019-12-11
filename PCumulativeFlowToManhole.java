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


 public class PCumulativeFlowToManhole {

  public PCumulativeFlowToManhole() {
  }

  SewerDAreaMInputFile_Writer m_SewerDAreaMInputFile_Writer = new SewerDAreaMInputFile_Writer();
  ManholeFileReader m_ManholeFileReader = new ManholeFileReader();
  MDAreaInputFileReader_Sewer m_MDAreaInputFileReader_Sewer = new MDAreaInputFileReader_Sewer();
  MOutputFileWriter_Sewer m_MOutputFileWriter_Sewer = new MOutputFileWriter_Sewer();


  public void runCummulativeFlowToManholes(){
    calculateCumulativeFlowFromDrainageAreas();
    calculateCumulativeFlowFromManholes();
  }

  public void calculateCumulativeFlowFromDrainageAreas(){
   String totalNoofSubareasStr = "";
  try{
    totalNoofSubareasStr = m_MDAreaInputFileReader_Sewer.getValueOfParameter("totalNoOfDrainageAreas");
    }catch(Exception r){
       System.out.println(r);
      }
    int totalNoofSubareas  = Integer.parseInt(totalNoofSubareasStr);
    int totalNoofManholes = m_ManholeFileReader.getNoOfRecordsValue();

//    System.out.println("totalNoofSubareas in PCumulativeFlowToManhole class is  "+totalNoofSubareas+"  totalNoofManholes  "+totalNoofManholes);

   for (int currentManholeNoCounter=1; currentManholeNoCounter <= totalNoofManholes; currentManholeNoCounter++){
        double totalFlowFromSubareas = 0.0;
        for (int currentSubareaNoCounter=1; currentSubareaNoCounter <= totalNoofSubareas; currentSubareaNoCounter++){
          String manholeIDToWhichSubareaDrained = "";
              try{
              manholeIDToWhichSubareaDrained = m_MDAreaInputFileReader_Sewer.getValueOfParameter("SubareaIDforDrainedToManholeNo_"+currentSubareaNoCounter);
               }catch(Exception r){
              System.out.println(r);
              }

              String currentManholeNoCounterString = String.valueOf(currentManholeNoCounter);

              if(manholeIDToWhichSubareaDrained.equals(currentManholeNoCounterString)){
                String flowFromCurrentSubareaString = m_SewerDAreaMInputFile_Writer.getValueOfParameter("PeakDischarge_"+currentSubareaNoCounter);
                double flowFromCurrentSubarea = Double.parseDouble(flowFromCurrentSubareaString);
                totalFlowFromSubareas = totalFlowFromSubareas + flowFromCurrentSubarea;
//System.out.println("flow to manhole_"+currentManholeNoCounterString+"  from subarea_"+currentSubareaNoCounter+"  is:    "+flowFromCurrentSubarea);
//System.out.println("total flow from subareas to manhole_"+currentManholeNoCounterString+"  is: "+totalFlowFromSubareas);
              }//end of if statement

        }//end of subarea for statement
        String totalFlowFromSubareasString = String.valueOf(totalFlowFromSubareas);
        m_SewerDAreaMInputFile_Writer.setValueOfParameter("totalFlowToManholeFromSubareas"+currentManholeNoCounter,totalFlowFromSubareasString);//(manhole name_id, totalflow from subareas)
        m_MOutputFileWriter_Sewer.setValueOfParameter("TotalFlowFromSubareasToManhole_"+currentManholeNoCounter,totalFlowFromSubareasString);
//        System.out.println("total flow from subareas to manhole_"+currentManholeNoCounter+"  is: "+totalFlowFromSubareas);
    }//end of Manhole for statement

   }//end of calculateCumulativeFlowFromDrainageAreas()  method


    public void calculateCumulativeFlowFromManholes(){

       int numberOfRecordsInManholeFile = m_ManholeFileReader.getNoofRecordsInManholeFile();

       int[] us_ManholeIDs = new int [numberOfRecordsInManholeFile];
       int[] ds_ManholeIDs = new int [numberOfRecordsInManholeFile];

       us_ManholeIDs = m_ManholeFileReader.getUpstreamManholeID();//array
       ds_ManholeIDs = m_ManholeFileReader.getDownstreamManholeID();//array

       double currentUpstreamCumulativeFlow, currentDownstreamCumulativeFlow, currentDownstreamFlowFromSubareas, currentUpstreamFlowFromSubareas;
       double totalCumulativeFlowToManhole = 0.0;

       int totalUS_ManholeIDs = us_ManholeIDs.length;

       //sets initial cumulative flow from us manholes and subareas to zero
       setInitialCumulativeFlowFromUSManholes(numberOfRecordsInManholeFile);

         for (int currentManholeNoCounter=0; currentManholeNoCounter <= totalUS_ManholeIDs-1; currentManholeNoCounter++){

             int upstreamManholeID = us_ManholeIDs[currentManholeNoCounter];
             //int currentDrainedManholeID = currentManholeNoCounter +1;//actual ID
             int downstreamManholeID = ds_ManholeIDs[currentManholeNoCounter];
//System.out.println("upstream manhole id is:   "+upstreamManholeID+"  and downstream manhole id is:   "+downstreamManholeID);
             //check if the current US manhole has US manhole
             boolean currentlyUSManholehasUpstreamManhole = false;
             boolean currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole = false;
             for (int newCurrentManholeNoCounter=0; newCurrentManholeNoCounter < currentManholeNoCounter; newCurrentManholeNoCounter++){
//              if(newCurrentManholeNoCounter >=1){
                  int precedingDownstreamManholeID = ds_ManholeIDs[newCurrentManholeNoCounter];
                  if(precedingDownstreamManholeID==upstreamManholeID){
                  currentlyUSManholehasUpstreamManhole = true;
//System.out.println("currentlyUSManholehasUpstreamManhole   ");

                  break;
                  }
                }

          for (int newCurrentManholeNoCounter=0; newCurrentManholeNoCounter < currentManholeNoCounter; newCurrentManholeNoCounter++){
            int precedingDownstreamManholeID = ds_ManholeIDs[newCurrentManholeNoCounter];
            if(precedingDownstreamManholeID==downstreamManholeID){
            currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole = true;
//System.out.println("currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole ");

                  break;
                  }
                 }

            if(currentlyUSManholehasUpstreamManhole&&currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole){
//System.out.println("currentlyUSManholehasUpstreamManhole=  "+currentlyUSManholehasUpstreamManhole+"  currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole=  "+currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole);

                 //get the cumulative flow of the current upstream manhole(from subareas and upstream manholes)
                  String currentUpstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+upstreamManholeID);
                  currentUpstreamCumulativeFlow = Double.parseDouble(currentUpstreamCumulativeFlowStr);
                  //get the cumulative flow of the current downstream manhole(from subareas and upstream manholes as determined in preceeding iterations (since this manhole has upstream manholes))
                  String currentDownstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+downstreamManholeID);
                  currentDownstreamCumulativeFlow = Double.parseDouble(currentDownstreamCumulativeFlowStr);
                  totalCumulativeFlowToManhole = currentUpstreamCumulativeFlow + currentDownstreamCumulativeFlow;
//          System.out.println("current UpstreamCumulativeFlow is "+currentUpstreamCumulativeFlow);
//          System.out.println("current DownstreamCumulativeFlow is "+currentDownstreamCumulativeFlow);

            }
             if(currentlyUSManholehasUpstreamManhole && !currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole){
//             System.out.println("currentlyUSManholehasUpstreamManhole=  "+currentlyUSManholehasUpstreamManhole+"  currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole=  "+currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole);

                  //get the cumulative flow of the current upstream manhole(from subareas and upstream manholes)
                  String currentUpstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+upstreamManholeID);
                  currentUpstreamCumulativeFlow = Double.parseDouble(currentUpstreamCumulativeFlowStr);
                  //get the cumulative flow of the current downstream manhole(from subareas and upstream manholes as determined in preceeding iterations (since this manhole has upstream manholes))
                  String currentDownstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+downstreamManholeID);
                  currentDownstreamCumulativeFlow = Double.parseDouble(currentDownstreamCumulativeFlowStr);
                  //get flow from subareas directly draining to the currently downstream manhole
                  String currentDownstreamFlowFromSubareasStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("totalFlowToManholeFromSubareas"+downstreamManholeID);
                  currentDownstreamFlowFromSubareas = Double.parseDouble(currentDownstreamFlowFromSubareasStr);
                  totalCumulativeFlowToManhole = currentUpstreamCumulativeFlow + currentDownstreamCumulativeFlow + currentDownstreamFlowFromSubareas;
//          System.out.println("current UpstreamCumulativeFlow is "+currentUpstreamCumulativeFlow);
//          System.out.println("current DownstreamCumulativeFlow is "+currentDownstreamCumulativeFlow);
//          System.out.println("currentDownstreamFlow FromSubareas is "+currentDownstreamFlowFromSubareas);

               }
             if(!currentlyUSManholehasUpstreamManhole&&currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole){
//          System.out.println("currentlyUSManholehasUpstreamManhole=  "+currentlyUSManholehasUpstreamManhole+"  currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole=  "+currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole);
                  //get the cumulative flow of the current upstream manhole(from subareas and upstream manholes)
                  String currentUpstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+upstreamManholeID);
                  currentUpstreamCumulativeFlow = Double.parseDouble(currentUpstreamCumulativeFlowStr);
                  //get the cumulative flow of the current downstream manhole(from subareas and upstream manholes as determined in preceeding iterations (since this manhole has upstream manholes))
                  String currentDownstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+downstreamManholeID);
                  currentDownstreamCumulativeFlow = Double.parseDouble(currentDownstreamCumulativeFlowStr);
                  //get flow from subareas directly draining to the currently downstream manhole
                  String currentUpstreamFlowFromSubareasStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("totalFlowToManholeFromSubareas"+upstreamManholeID);
                  currentUpstreamFlowFromSubareas = Double.parseDouble(currentUpstreamFlowFromSubareasStr);
                  totalCumulativeFlowToManhole = currentUpstreamCumulativeFlow + currentDownstreamCumulativeFlow + currentUpstreamFlowFromSubareas;
//          System.out.println("current UpstreamCumulativeFlow is "+currentUpstreamCumulativeFlow);
//          System.out.println("current DownstreamCumulativeFlow is "+currentDownstreamCumulativeFlow);
//          System.out.println("current UpstreamFlowFromSubareas is "+currentUpstreamFlowFromSubareas);

               }
             if(!currentlyUSManholehasUpstreamManhole && !currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole){
//             System.out.println("currentlyUSManholehasUpstreamManhole=  "+currentlyUSManholehasUpstreamManhole+"  currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole=  "+currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole);

                  //get the cumulative flow of the current upstream manhole(from subareas and upstream manholes)
                  String currentUpstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+upstreamManholeID);
                  currentUpstreamCumulativeFlow = Double.parseDouble(currentUpstreamCumulativeFlowStr);
                  //get the cumulative flow of the current downstream manhole(from subareas and upstream manholes as determined in preceeding iterations (since this manhole has upstream manholes))
                  String currentDownstreamCumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+downstreamManholeID);
                  currentDownstreamCumulativeFlow = Double.parseDouble(currentDownstreamCumulativeFlowStr);
                  //get flow from subareas directly draining to the currently downstream manhole
                  String currentDownstreamFlowFromSubareasStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("totalFlowToManholeFromSubareas"+downstreamManholeID);
                  currentDownstreamFlowFromSubareas = Double.parseDouble(currentDownstreamFlowFromSubareasStr);
                  String currentUpstreamFlowFromSubareasStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("totalFlowToManholeFromSubareas"+upstreamManholeID);
                  currentUpstreamFlowFromSubareas = Double.parseDouble(currentUpstreamFlowFromSubareasStr);
                  totalCumulativeFlowToManhole = currentUpstreamCumulativeFlow + currentDownstreamCumulativeFlow + currentDownstreamFlowFromSubareas + currentUpstreamFlowFromSubareas;

                  // set cumulative flow to upstream manhole which was not previously drained
                  m_SewerDAreaMInputFile_Writer.setValueOfParameter("cumulativeFlowToManhole_"+upstreamManholeID, currentUpstreamFlowFromSubareasStr);
                  m_MOutputFileWriter_Sewer.setValueOfParameter("CumulativeFlowToManhole_"+upstreamManholeID, currentUpstreamFlowFromSubareasStr);
          System.out.println("New current UpstreamCumulativeFlow is "+currentUpstreamCumulativeFlow);
//          System.out.println("current DownstreamCumulativeFlow is "+currentDownstreamCumulativeFlow);
          System.out.println("New current UpstreamFlowFromSubareas is "+currentUpstreamFlowFromSubareas);
//          System.out.println("current DownstreamFlowFromSubareas is "+currentDownstreamFlowFromSubareas);

               }
            totalCumulativeFlowToManhole = Math.round(totalCumulativeFlowToManhole*1000)/1000.0;//roundoff three decimal places
            String totalCumulativeFlowToManholeStr = String.valueOf(totalCumulativeFlowToManhole);
System.out.println("cumulativeFlowToManhole"+downstreamManholeID+"  is:  "+ totalCumulativeFlowToManholeStr);
            m_SewerDAreaMInputFile_Writer.setValueOfParameter("cumulativeFlowToManhole_"+downstreamManholeID, totalCumulativeFlowToManholeStr);
            if(currentManholeNoCounter < numberOfRecordsInManholeFile){//this if statement is prepared to exclude the nominal last manhole value
              m_MOutputFileWriter_Sewer.setValueOfParameter("CumulativeFlowToManhole_"+downstreamManholeID, totalCumulativeFlowToManholeStr);
            }
        }//end of for statement

   }//end of calculateCumulativeFlowFromManholes() method



//sets initial cumulative flow from upstream manholes to zero to avoid null found exception
 public void setInitialCumulativeFlowFromUSManholes(int totalUS_ManholeIDs){
 int m_totalUS_ManholeIDs = totalUS_ManholeIDs;
   for (int currentManholeNoCounter=1; currentManholeNoCounter <= m_totalUS_ManholeIDs+1/*including the final destination manhole*/; currentManholeNoCounter++){
   //set initial cumulativeFlowToManhole (from subarea directly draining to the manhole under consideration and upstream manholes) to zero
   m_SewerDAreaMInputFile_Writer.setValueOfParameter("cumulativeFlowToManhole_"+currentManholeNoCounter, "0.0");
   // if curent manhole is the last destination manhole set total flow from subareas to zero cause no area is assumed to directly drain to it
   if(currentManholeNoCounter==m_totalUS_ManholeIDs+1){
    m_SewerDAreaMInputFile_Writer.setValueOfParameter("totalFlowToManholeFromSubareas"+currentManholeNoCounter, "0.0");
    }
   }

 }
  }//end of class