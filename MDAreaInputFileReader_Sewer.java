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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class MDAreaInputFileReader_Sewer {

Hashtable readerHT = new Hashtable();
Hashtable newHT = new Hashtable();
static Hashtable newFinalHT = new Hashtable();

Object parameter = " ", value = "  ", newValue = " ";
Iterator itVal, itParam;
  public MDAreaInputFileReader_Sewer() {
  }

 public void readInpFile()throws Exception{

   String currentDirectory = System.getProperty("user.dir");
   String inputFile = currentDirectory + "\\Input_Output\\RP_Sewer\\SewerDAreaInputFile.rp";
      try{
       BufferedReader in
                    = new BufferedReader(
                           new FileReader(inputFile));

  String line = "Initialise String";
  String token = "Initialise Token";
  in.readLine();
  in.readLine();
  in.readLine();
  in.readLine();
  in.readLine();
  in.readLine();

    while ( (line = in.readLine()) != null)
    {
   StringTokenizer thisLine = new StringTokenizer(line," ()");

   parameter = thisLine.nextToken().trim();

   try{
   value = thisLine.nextToken().trim();
   }catch(Exception e){
         throw new Exception("Value of the parameter, "+parameter+", is null in the input file as detected at "
                                    + this.getClass().getName() + ".");

   }
     //add data objects that hold general information of the catchment here
//      if(parameter.equals("Tot_Area")){parameter="DArea";}
//      else if(parameter.equals("Time_Of_Concentration")){parameter="DTimeOfConcentration";}
//      else if(parameter.equals("T_Years_Design_RF")){parameter="DT_YearsDesignRF";}
//      else if(parameter.equals("T_Hours_Design_RF")){parameter="DT_HoursDesignRF";}
//      else if(parameter.equals("MAP")){parameter="DMeanAnnualRF";}
//      else if(parameter.equals("Urban")){parameter="DUrban";}
//      else if(parameter.equals("Rural")){parameter="DRural";}
//      else if(parameter.equals("Return_Period")){parameter="DReturnPeriod";}
//      else if(parameter.equals("Sub_Catchments_Opn")){parameter="DSubCatchmentOpn";}
//      else if(parameter.equals("Time_Of_Concentration_Opn")){parameter="DTimeOfConcOpn";}
//      else if(parameter.equals("Time_Of_Concentration_Method")){parameter="DTimeOfConcMethod";}
//      else if(parameter.equals("Length_Of_Flow")){parameter="DMainChnlLength";}
//      else if(parameter.equals("Catchment_Slope")){parameter="DCatchmtSlope";}
//      else if(parameter.equals("Runoff_CN")){parameter="DRunoffCN";}
//      else if(parameter.equals("Design_RF_Opn")){parameter="DDesignRFOpn";}
//      else if(parameter.equals("RF_Distrbn_Type")){parameter="DRFDistrbnType";}

      readerHT.put(parameter,value);
      newFinalHT.put(parameter,value);


    System.out.println("parameter is read in MDAreaInputFileReader_Sewer   "+parameter+"    and  value is   "+value);

    }
      in.close();
}
  catch(FileNotFoundException e) {
  System.out.println(e);
    }
  catch (IOException e){
  System.out.println(e);
    }

  putToHT (readerHT);

  }


public void putToHT (Hashtable thisHT) throws Exception{
   int thisValue;
   newHT = thisHT;
//
   Object opnValue = " ";
   Object objValue = " ";
//
//
//opnValue = newHT.get("DSubCatchmentOpn");

    itVal = newHT.values().iterator();
    itParam = newHT.keySet().iterator();
    while (itParam.hasNext()) {

         parameter = itParam.next();
         newValue = itVal.next();


//if(opnValue.equals("YES")){//if multiple subcatchments
//
// objValue = newHT.get("No_Of_Sub_Catchments");
// try{
// thisValue = Integer.parseInt(String.valueOf(objValue));
//       }catch (Exception e){
//        throw new Exception("Value of the data object, "+ "No_Of_Sub_Catchments"+", is not an instance of Integer "+ " as detected at class  "
//                                    + this.getClass().getName() + ".");
//       }
//
// }//end of if opnvalue yes
// else{//consider a single catchment
//
//thisValue = 1;
//}

//         for (int k=1; k<=thisValue;k++){
//
//             String stK = String.valueOf(k);
//             objValue = newHT.get(parameter);

             //add data objects applicable to sub-catchments here
//             if(parameter.equals("Area_"+stK)){parameter="DArea_"+stK;
//             newFinalHT.remove("Area_"+stK);
//             }
//             if(parameter.equals("Slope_"+stK)){parameter="DSlope_"+stK;
//             newFinalHT.remove("Slope_"+stK);}
//             if(parameter.equals("Permeability_"+stK)){parameter="DPermeability_"+stK;
//             newFinalHT.remove("Permeability_"+stK);
//             }
//             if(parameter.equals("Land_Cover_"+stK)){parameter="DLandCover_"+stK;
//             newFinalHT.remove("Land_Cover_"+stK);
//             }
//             if(parameter.equals("No_Of_Sub_Catchments")){parameter="DNoOfSubCatchments";
//             newFinalHT.remove("No_Of_Sub_Catchments");
//             }
//             if(parameter.equals("Catchment_Type_"+stK)){parameter="DCatchmentType_"+stK;
//             newFinalHT.remove("Catchment_Type_"+stK);
//             }
//             if(parameter.equals("DrainageAreaType_"+stK)){parameter="DDrainageAreaType_"+stK;
//             newFinalHT.remove("DrainageAreaType_"+stK);
//             }
//             if(parameter.equals("HydroCondition_"+stK)){parameter="DHydroCondition_"+stK;
//             newFinalHT.remove("HydroCondition_"+stK);
//             }

//
             newFinalHT.put(parameter,newValue);
             System.out.println("parameter is    "+parameter +"  and value is   "+newFinalHT.get(parameter));

               }//end of for

  }

//}

//  public Hashtable getSewerDAreaReaderHT ()throws Exception{
//   readInpFile();
//   return newFinalHT;
//  }

  public String getValueOfParameter(String parameter)throws Exception{

   //readInpFile();
   String stValue = (newFinalHT.get(parameter)).toString();
   return stValue;
  }

}
