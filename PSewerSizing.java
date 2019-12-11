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

public class PSewerSizing {

  public PSewerSizing() {
  }

    double[] dischargeDifferenceArray = new double[13];
    SewerDAreaMInputFile_Writer m_SewerDAreaMInputFile_Writer = new SewerDAreaMInputFile_Writer();
    MDAreaInputFileReader_Sewer m_MDAreaInputFileReader_Sewer = new MDAreaInputFileReader_Sewer();
    ManholeFileReader m_ManholeFileReader = new ManholeFileReader();
    MOutputFileWriter_Sewer m_MOutputFileWriter_Sewer = new MOutputFileWriter_Sewer();

  public void calculateSewerSize(){

  //SewerDAreaMInputFile_Writer m_SewerDAreaMInputFile_Writer = new SewerDAreaMInputFile_Writer();

  double selfCleansingVelocity = 0.8;//in m/sec
  double currentDischargeDifference = 0.0;
  int noOfManholes = m_ManholeFileReader.getNoofRecordsInManholeFile()+1/*1 is added to acomodate the final destination manhole*/;

  System.out.println("noOfManholes in PSewerSizing is:  "+noOfManholes);
      for(int currentMHCounter = 1; currentMHCounter < noOfManholes; currentMHCounter++){
        String cumulativeFlowStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+currentMHCounter);
        //set value of parameter to be written as final output
        m_MOutputFileWriter_Sewer.setValueOfParameter("cumulativeFlowToManhole_"+currentMHCounter, cumulativeFlowStr);
System.out.println("cumulativeFlow of current Manhole is:   "+cumulativeFlowStr);
        double cumulativeFlowAtManhole = Double.parseDouble(cumulativeFlowStr);
        for (int currentStandardPipeID = 0; currentStandardPipeID < 13/*noOfStandardPipes*/; currentStandardPipeID++){
          double pipediameter = getStandardPipeSizes(currentStandardPipeID);
          //get discharge at self cleansing velocity for the given standard pipe diameter
          double fullDepthDischargeOfPipe = getFullDepthDischargeForStandardPipeSizes(currentStandardPipeID);
          currentDischargeDifference = cumulativeFlowAtManhole - fullDepthDischargeOfPipe;
          dischargeDifferenceArray[currentStandardPipeID]= currentDischargeDifference;
          //}
//          double discharge = Math.round(((Math.PI * 0.001*pipediameter * 0.001*pipediameter * selfCleansingVelocity)/4)*1000)/1000.0;//in cubic metre per second
System.out.println("pipediameter=  "+pipediameter+"  discharge=   "+fullDepthDischargeOfPipe+"  dischargeDifference=   "+currentDischargeDifference);
        }// end of for statement related to standard pipes

          int minDischargeDfceIndex = 0;
         //determine minimum discharge difference in the current manhole
           for (int dischargeDifferenceIndex = 1; dischargeDifferenceIndex < 13/*noOfDischargeDifferenceIndices*/; dischargeDifferenceIndex++){
              double currentMinDischargeDfce = dischargeDifferenceArray[minDischargeDfceIndex];
              currentMinDischargeDfce = Math.abs(currentMinDischargeDfce);
              double nextDischargeDfce = dischargeDifferenceArray[dischargeDifferenceIndex];
              nextDischargeDfce = Math.abs(nextDischargeDfce);
              if(nextDischargeDfce <= currentMinDischargeDfce){
               minDischargeDfceIndex = dischargeDifferenceIndex;
              }else{
               minDischargeDfceIndex = minDischargeDfceIndex;
              }
            }//end of for statement related to discharge difference
            System.out.println("minDischargeDfceIndex is:  "+minDischargeDfceIndex);
            determineFinalDesignParameterValues(currentMHCounter, minDischargeDfceIndex);
      }//end of for statement related to manholes

  }//end of calculateSewerSize method


  //This method sets pipe diameter, slope and design discharge of each pipe running from one manhole to another
  public void determineFinalDesignParameterValues(int currentManhole, int indexOfPipeDiameterAndFullDepthFlowGivingMinDischargeDfce){
    //m_ManholeFileReader.readManholeFile();

    int m_currentManhole = currentManhole;
    int m_minDischargeDfceIndex = indexOfPipeDiameterAndFullDepthFlowGivingMinDischargeDfce;
    // get pipe diameter that resulted to minimum discharge difference (close to design discharge at the current manhole)
    double fittingPipeDiameter = getStandardPipeSizes(m_minDischargeDfceIndex);

    //get discharge at self cleansing velocity for the given standard pipe diameter
    double fullDepthDischargeOfPipe = getFullDepthDischargeForStandardPipeSizes(m_minDischargeDfceIndex);

    //currentDischargeDifference = cumulativeFlowAtManhole - fullDepthDischargeOfPipe;
    double minDischargeDfce = dischargeDifferenceArray[m_minDischargeDfceIndex];
    System.out.println("minDischargeDfce is:   "+minDischargeDfce);

    //get cumulative flow to the manhole under consideration
    String designFlowToManholeStr = m_SewerDAreaMInputFile_Writer.getValueOfParameter("cumulativeFlowToManhole_"+m_currentManhole);
    double designFlowToManhole = Double.parseDouble(designFlowToManholeStr);

    //double manningRoughnessCoeff = 0.013;

    String manningRoughnessCoeffStr = "0.0";
    try{
        // the the Manning roughness coefficient, n value, of the pipe
        manningRoughnessCoeffStr =  m_MDAreaInputFileReader_Sewer.getValueOfParameter("PipeManning_n");
      }catch(Exception r){
        System.out.println(r);
      }
    double manningRoughnessCoeff = Double.parseDouble(manningRoughnessCoeffStr);
    System.out.println("PipeManning_n is:   "+manningRoughnessCoeff);

    double flowVelocity = 0.0;
    double slope = 0.0;
    if(minDischargeDfce > 0.0){/* if size of sewer selected is having less flow, then the carrying capacity of that sewer is to be
                                increased by increasing velocity for which slope is to be increased*/
      flowVelocity = ((designFlowToManhole/1000) * 4)/(3.14 * Math.pow((fittingPipeDiameter*0.001), 2));//in m/sec
      //calculate slope using Manning Equation
      slope = Math.pow((flowVelocity * manningRoughnessCoeff)/(0.003968 * Math.pow((fittingPipeDiameter), 0.666)), 2);//fraction

    }else{/*if minDischargeDfce < 0.0, check whether the design discharge of that sewer is greater than half full flow of the selected sewer*/

      if(designFlowToManhole > 0.5*fullDepthDischargeOfPipe){

           flowVelocity = 0.8/*self cleansing velocity*/;

           slope = Math.pow((flowVelocity/*self cleansing velocity*/ * manningRoughnessCoeff)/(0.003968 * Math.pow(fittingPipeDiameter, 0.666)), 2);
        System.out.println("pipe diameter is greater than required size but SelfCleansingVelocity is achieved  ");
      }else{//if design flow to manhole is less than half of full depth discharge of sewer
        System.out.println("design discharge of that sewer is less than half full flow of the selected sewer---SelfCleansing is not achieved or flushig recommended");
      }//end of second else block
    }//end of first else block

   flowVelocity = Math.round(flowVelocity*1000)/1000.0;//roundoff three decimal places
   String flowVelocityStr = String.valueOf(flowVelocity);
   slope = slope*100.0;//convert slope from fraction to percentage
   slope = Math.round(slope*1000)/1000.0;//roundoff slope to three decimal places
   String slopeStr = String.valueOf(slope);
   fittingPipeDiameter = Math.round(fittingPipeDiameter*1000)/1000.0;
   String fittingPipeDiameterStr = String.valueOf(fittingPipeDiameter);

    m_MOutputFileWriter_Sewer.setValueOfParameter("FlowVelocityAtManhole_"+currentManhole, flowVelocityStr);
    m_MOutputFileWriter_Sewer.setValueOfParameter("PipeSlopeAtManhole_"+currentManhole, slopeStr);
    m_MOutputFileWriter_Sewer.setValueOfParameter("PipeDiameterAtmanhole_"+currentManhole, fittingPipeDiameterStr);

System.out.println("current Manhole is:   "+currentManhole);
System.out.println("FlowVelocity =   "+flowVelocity+"   PipeSize =   "+fittingPipeDiameter+"  PipeSlope =   "+slope);
//System.out.println("PipeSize =   "+fittingPipeDiameter);
//System.out.println("  PipeSlope =   "+slope);
  }//end of determineFinalDesignParameterValues method


  public double getStandardPipeSizes(int index){//pipe diameter in mm
   int m_index = index;
   double[] standardPipeSize = {200, 250, 300, 350, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200};
   return standardPipeSize[m_index];
  }
  public double getFullDepthDischargeForStandardPipeSizes(int index){//in litres per second
   int m_index = index;
   double[] dischargeForStandardPipeSizes = {25.12, 39.25, 56.52, 76.93, 100.48, 157.00, 226.08, 307.72, 401.92, 508.68, 628.00, 759.88, 904.32};
   return dischargeForStandardPipeSizes[m_index];
  }


    // the follwoing arrays are meant to convey pair of x and y coordinate values to drawSewer method of RP_SewerInterface
    double[] x_CoordArrayNew = new double[2];
    double[] y_CoordArrayNew = new double[2];
    double[] z_CoordArrayNew = new double[2];

  /*This method is called by menuSewerRun action performed method in RP_SewerInterface Class to draw the pipes on the viewer and compte each pipe length*/
  public void sewerLocationAndLength(int usManholeIndex){

    int currentManholeCounter = usManholeIndex;
    int noOfManholes = m_ManholeFileReader.getNoofRecordsInManholeFile()+1/*1 is added to acomodate the final destination manhole*/;
    double[] x_CoordArray = new double[noOfManholes];
    double[] y_CoordArray = new double[noOfManholes];
    double[] z_CoordArray = new double[noOfManholes];
    int[] us_Manholes = new int[noOfManholes];
    int[] ds_Manholes = new int[noOfManholes];

    x_CoordArray = m_ManholeFileReader.getX_CoordValue();
    y_CoordArray = m_ManholeFileReader.getY_CoordValue();
    z_CoordArray = m_ManholeFileReader.getZ_CoordValue();
    us_Manholes = m_ManholeFileReader.getUpstreamManholeID();
    ds_Manholes = m_ManholeFileReader.getDownstreamManholeID();

       int usManhole = us_Manholes[currentManholeCounter];
       int dsManhole = ds_Manholes[currentManholeCounter];
System.out.println("in class PSewerSizing, usManhole is:  "+usManhole+"  and dsManhole is:  "+dsManhole);
       double usX_Coord = x_CoordArray[currentManholeCounter];
       double usY_Coord = y_CoordArray[currentManholeCounter];
       double usZ_Coord = z_CoordArray[currentManholeCounter];

       x_CoordArrayNew[0] = usX_Coord;
       y_CoordArrayNew[0] = usY_Coord;
       z_CoordArrayNew[0] = usZ_Coord;

       int indexAtWhichDSisUS = usManhole + (dsManhole-usManhole)-1;//this is the index of an array at which the currently downstream manhole is located as upstream manhole

       double dsX_Coord = x_CoordArray[indexAtWhichDSisUS];
       double dsY_Coord = y_CoordArray[indexAtWhichDSisUS];
       double dsZ_Coord = z_CoordArray[indexAtWhichDSisUS];

       x_CoordArrayNew[1] = dsX_Coord;
       y_CoordArrayNew[1] = dsY_Coord;
       z_CoordArrayNew[1] = dsZ_Coord;

  }//end of sewerLocationAndLength method

  //this method is called by runSeweractionperformed method in RP_SewerInterface Method
  public void pipeInvertAndExcavationDepth(){

    int noOfManholes = m_ManholeFileReader.getNoofRecordsInManholeFile();
    int[] currentManholeIDArray= m_ManholeFileReader.getUpstreamManholeID();
    int[] currentDSManholeIDArray= m_ManholeFileReader.getDownstreamManholeID();

    for(int currentManholeCounter = 0; currentManholeCounter < noOfManholes-1; currentManholeCounter++){
      double[] z_coordArray = getZ_CoordArrayToDrawLineBetweenManholes(currentManholeCounter);
      double usMHGroundElevation = z_coordArray[0];
      double dsMHGroundElevation = z_coordArray[1];
      double groundElevationDfce = usMHGroundElevation - dsMHGroundElevation;

      int currentManholeID = currentManholeIDArray[currentManholeCounter];
      int currentDSManholeID = currentDSManholeIDArray[currentManholeCounter];
      String distanceBetweenManholesStr = m_MOutputFileWriter_Sewer.getValueOfParameter("PipeLength_"+currentManholeID);
      double distanceBetweenManholes = Double.parseDouble(distanceBetweenManholesStr);
      double groundSlope = (groundElevationDfce/distanceBetweenManholes)*100.0;//ground slope in percentage
      groundSlope = Math.round(groundSlope*1000)/1000.0;
      String groundSlopeStr = String.valueOf(groundSlope);
      m_MOutputFileWriter_Sewer.setValueOfParameter("GroundSlope_"+currentManholeID, groundSlopeStr);

      String pipeSlopeStr = m_MOutputFileWriter_Sewer.getValueOfParameter("PipeSlopeAtManhole_"+currentManholeID);
      double pipeSlope = (Double.parseDouble(pipeSlopeStr))/100.0;//convert pipeSlope to double fraction from String percentage
      double pipeInvertElevationDfce = distanceBetweenManholes*pipeSlope;


      double upstreamExcavationDepth = 0.0;
      double downstreamExcavationDepth = 0.0;
      double upstreamPipeInvertElevation = 0.0;
      double downstreamPipeInvertElevation = 0.0;
      boolean currentManholeHasUpstreamMH = checkIfCurrentManholeHasUpstreamManhole(currentManholeID, noOfManholes);
      if(!currentManholeHasUpstreamMH){      //if current manhole does not have an upstream manhole then set excavation depth to 1.00 meter (minimum depth for safety of the sewer)
        upstreamExcavationDepth = 1.00;//in meter
        upstreamPipeInvertElevation = usMHGroundElevation - upstreamExcavationDepth;
        downstreamPipeInvertElevation = upstreamPipeInvertElevation - pipeInvertElevationDfce;
        downstreamExcavationDepth = dsMHGroundElevation - downstreamPipeInvertElevation;

         //if the downstream manhole was previously drained
        boolean currentDSManholePreviouslyReceivedFlowFromUpstreamMH = checkIfCurrentManholeHasUpstreamManhole(currentManholeID, noOfManholes);
        if(currentDSManholePreviouslyReceivedFlowFromUpstreamMH){
        String previouslySetDSPipeInvertElevationStr= m_MOutputFileWriter_Sewer.getValueOfParameter("PipeInvertElevationAtManhole_"+currentDSManholeID);
        String previouslySetDSExcavationDepthStr = m_MOutputFileWriter_Sewer.getValueOfParameter("ExcavationDepthAtManhole_"+currentDSManholeID);
        double previouslySetDSPipeInvertElevation = Double.parseDouble(previouslySetDSPipeInvertElevationStr);
        double previouslySetDSExcavationDepth = Double.parseDouble(previouslySetDSExcavationDepthStr);
            if (previouslySetDSPipeInvertElevation < downstreamPipeInvertElevation){
              downstreamPipeInvertElevation = previouslySetDSPipeInvertElevation;
              downstreamExcavationDepth = previouslySetDSExcavationDepth;
            }
        }//end of if(currentDSManholePreviouslyReceivedFlowFromUpstreamMH)
//calculate downstreamexcavationdepth

      //m_MOutputFileWriter_Sewer.setValueOfParameter("ExcavationDepthAtManhole_"+currentManholeID, excavationDepth);
      }else{//if current upstream manhole has an upstream manhole then simply get upstreamPipeInvertElevation from previously set data
System.out.println("currentUSManholeID in if current us MH has an upstream MH is   "+currentManholeID);
        String upstreamPipeInvertElevationStr = m_MOutputFileWriter_Sewer.getValueOfParameter("PipeInvertElevationAtManhole_"+currentManholeID);
        upstreamPipeInvertElevation = Double.parseDouble(upstreamPipeInvertElevationStr);


        String upstreamExcavationDepthStr = m_MOutputFileWriter_Sewer.getValueOfParameter("ExcavationDepthAtManhole_"+currentManholeID);
        upstreamExcavationDepth= Double.parseDouble(upstreamExcavationDepthStr);

        downstreamPipeInvertElevation = upstreamPipeInvertElevation - pipeInvertElevationDfce;
        downstreamExcavationDepth = dsMHGroundElevation - downstreamPipeInvertElevation;

        boolean currentDSManholePreviouslyReceivedFlowFromUpstreamMH = checkIfCurrentlyDSManholePreviouslyReceivedFlowFromAnotherManhole(currentManholeID, noOfManholes);
        if(currentDSManholePreviouslyReceivedFlowFromUpstreamMH){
  System.out.println("currentDSManholeID is:  "+currentDSManholeID+" and currentDSManholePreviouslyReceivedFlowFromUpstreamMH is  "+currentDSManholePreviouslyReceivedFlowFromUpstreamMH);
        String previouslySetDSPipeInvertElevationStr= m_MOutputFileWriter_Sewer.getValueOfParameter("PipeInvertElevationAtManhole_"+currentDSManholeID);
        String previouslySetDSExcavationDepthStr = m_MOutputFileWriter_Sewer.getValueOfParameter("ExcavationDepthAtManhole_"+currentDSManholeID);
        double previouslySetDSPipeInvertElevation = Double.parseDouble(previouslySetDSPipeInvertElevationStr);
        double previouslySetDSExcavationDepth = Double.parseDouble(previouslySetDSExcavationDepthStr);
            if (previouslySetDSPipeInvertElevation < downstreamPipeInvertElevation){
              downstreamPipeInvertElevation = previouslySetDSPipeInvertElevation;
              downstreamExcavationDepth = previouslySetDSExcavationDepth;
            }
        }//end of if(currentDSManholePreviouslyReceivedFlowFromUpstreamMH)

      }//end of else block (if upsteam ManholeHas Upstream manhole)

      /*if minimum depth is less than 1.00 m, set downstream excavationDepth to 1.00 and make adjustment to pipe invert elevation*/
                  //        if(downstreamExcavationDepth < 1.00){
                  //            double additionalExcavationDepth = 1.00 - downstreamExcavationDepth;
                  //            downstreamPipeInvertElevation = downstreamPipeInvertElevation - additionalExcavationDepth;
                  //            downstreamExcavationDepth = 1.00;
                  // here need to include a statement for updating pipe slope and subsequent pipe flow velocity
                  //          }
      downstreamPipeInvertElevation = Math.round(downstreamPipeInvertElevation*1000)/1000.0;//roundoff three decimal places
      downstreamExcavationDepth = Math.round(downstreamExcavationDepth*1000)/1000.0;//roundoff three decimal places
      upstreamPipeInvertElevation = Math.round(upstreamPipeInvertElevation*1000)/1000.0;//roundoff three decimal places
      upstreamExcavationDepth = Math.round(upstreamExcavationDepth*1000)/1000.0;//roundoff three decimal places
      System.out.println("usExcavationDepthAtManhole= "+String.valueOf(upstreamExcavationDepth)+" dsExcavationDepthAtManhole = "+String.valueOf(downstreamExcavationDepth)+"  usPipeInvertElevationAtManhole= "+String.valueOf(upstreamPipeInvertElevation)+"  dsPipeInvertElevationAtManhole =  "+ String.valueOf(downstreamPipeInvertElevation) );
      m_MOutputFileWriter_Sewer.setValueOfParameter("ExcavationDepthAtManhole_"+currentManholeID, String.valueOf(upstreamExcavationDepth));
      m_MOutputFileWriter_Sewer.setValueOfParameter("ExcavationDepthAtManhole_"+currentDSManholeID, String.valueOf(downstreamExcavationDepth));
      m_MOutputFileWriter_Sewer.setValueOfParameter("PipeInvertElevationAtManhole_"+currentManholeID, String.valueOf(upstreamPipeInvertElevation));
      m_MOutputFileWriter_Sewer.setValueOfParameter("PipeInvertElevationAtManhole_"+currentDSManholeID, String.valueOf(downstreamPipeInvertElevation));
    }//end of for statement

  }//end of method

  public boolean checkIfCurrentManholeHasUpstreamManhole(int currentUpstreamManholeID, int totalNoOfUSManholes){
            int m_currentUpstreamManholeID = currentUpstreamManholeID;
            int m_totalNoOfUSManholes = totalNoOfUSManholes;

            int[] ds_Manholes = new int[m_totalNoOfUSManholes];
            ds_Manholes = m_ManholeFileReader.getDownstreamManholeID();

            boolean currentlyUSManholehasUpstreamManhole = false;

             for (int newCurrentManholeNoCounter=0; newCurrentManholeNoCounter < m_currentUpstreamManholeID; newCurrentManholeNoCounter++){
                  int precedingDownstreamManholeID = ds_Manholes[newCurrentManholeNoCounter];
                  if(precedingDownstreamManholeID==m_currentUpstreamManholeID){
                  currentlyUSManholehasUpstreamManhole = true;
                  break;
                  }//end of if statement
                }//end of for statement
                return currentlyUSManholehasUpstreamManhole;
    }//end of method


   public boolean checkIfCurrentlyDSManholePreviouslyReceivedFlowFromAnotherManhole(int currentUpstreamManholeID, int totalNoOfUSManholes){
    int m_currentUpstreamManholeID = currentUpstreamManholeID;
    int ds_ManholeIndex = m_currentUpstreamManholeID - 1;
    int m_totalNoOfUSManholes = totalNoOfUSManholes;

    int[] ds_Manholes = new int[m_totalNoOfUSManholes];
    ds_Manholes = m_ManholeFileReader.getDownstreamManholeID();
    int downstreamManholeID = ds_Manholes[ds_ManholeIndex];
    System.out.println("downstreamManholeID in checkIfpreviously drained method is=  "+downstreamManholeID+"  and ds_ManholeIndex is=  "+ds_ManholeIndex);
     boolean currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole = false;

        for (int newCurrentManholeNoCounter=0; newCurrentManholeNoCounter < /*m_currentUpstreamManholeID*/ds_ManholeIndex; newCurrentManholeNoCounter++){
          int precedingDownstreamManholeID = ds_Manholes[newCurrentManholeNoCounter];
          if(precedingDownstreamManholeID==downstreamManholeID){
          currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole = true;
          break;
          }
         }//end of for statement
         return currentlyDSManholePreviouslyReceivedFlowFromAnotherManhole;
    }//end of method








  public double[] getX_CoordArrayToDrawLineBetweenManholes(int usManholeIndex){
    int m_usManholeIndex = usManholeIndex;
    sewerLocationAndLength(m_usManholeIndex);
    return x_CoordArrayNew;
    }
  public double[] getY_CoordArrayToDrawLineBetweenManholes(int usManholeIndex){
    int m_usManholeIndex = usManholeIndex;
    sewerLocationAndLength(m_usManholeIndex);
    return y_CoordArrayNew;
  }
  public double[] getZ_CoordArrayToDrawLineBetweenManholes(int usManholeIndex){
    int m_usManholeIndex = usManholeIndex;
    sewerLocationAndLength(m_usManholeIndex);
    return z_CoordArrayNew;
  }

}//end of class