package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */

 import rationalpeak.Data.*;
 import rationalpeak.Processes.PTableOfCoefficients;

 import java.lang.Math;

 import java.util.Vector;
 import java.util.Enumeration;
 import java.util.Hashtable;
 import java.util.Iterator;



 /**
 This process determines the runoff coefficient of each subarea drained to a given manhole.

*/
public class PRunoffCoefficient_Sewer {

//DData data = new DData();
PTableOfCoefficients m_PTableOfCoefficients = new PTableOfCoefficients();
SewerDAreaMInputFile_Writer m_IOInputHashTable = new SewerDAreaMInputFile_Writer();

Vector landuseCatAreaVector = new Vector(208000,100);
Vector runoffCoeffVector = new Vector(208000,100);

int intCurrentSubAreaNo = 0;
int subAreaNoCounter = 0;


public PRunoffCoefficient_Sewer() {
  }

public void runProcess()throws Exception{

      Object key,value;
      Iterator itValue,itParameter;
      String stKey = " ";
      String stValue = " ";
      String landUseCategory = " ";
      String defaultHydroCondn="Poor";//the default hydrological condition is assume as "Poor" in order to be on the safe side with due consideration to the impervious areas
      //int subAreaNoCounter = 1;//subareaCounter
      double runoffCoeff = 0.0;

      String stTotalNumberOfDrainageAreas = m_IOInputHashTable.getValueOfParameter("totalNoOfDrainageAreas");
      int totalNumberOfDrainageAreas = Integer.parseInt(stTotalNumberOfDrainageAreas);
      Hashtable inpHashtable = m_IOInputHashTable.inpHashtable;


System.out.println("totalNumberOfDrainageAreas in Prunoffcoeffiecient_sewer is  "+totalNumberOfDrainageAreas);

  for(subAreaNoCounter = 1;   subAreaNoCounter <=totalNumberOfDrainageAreas;   subAreaNoCounter++){

      itValue = inpHashtable.values().iterator();
      itParameter = inpHashtable.keySet().iterator();

      String totSubAreaString = m_IOInputHashTable.getValueOfParameter("AreaofDrainageArea_"+subAreaNoCounter);
System.out.println("totSubAreaString in Prunoffcoeffiecient_sewer is  "+totSubAreaString);

      while (itParameter.hasNext()) {

         key = itParameter.next();
         value = itValue.next();

      stKey = String.valueOf(key).toString();
      stValue = String.valueOf(value).toString();
//System.out.println("stKey in Prunoffcoeffiecient_sewer is    "+stKey+"    and stValue is     "+stValue);

      String firstSevenChars = stKey.substring(0,7);
//System.out.println("firstSevenChars in Prunoffcoeffiecient_sewer is  "+firstSevenChars);

      if(firstSevenChars.equalsIgnoreCase("Landuse")){
        String stCurrentSubAreaNo = stKey.substring((stKey.length()-1));//last character in parameter name
        intCurrentSubAreaNo = Integer.parseInt(stCurrentSubAreaNo);
       }


      if(firstSevenChars.equalsIgnoreCase("Landuse") && intCurrentSubAreaNo==subAreaNoCounter){
//System.out.println("intCurrentSubAreaNo in Prunoffcoeffiecient_sewer is    "+intCurrentSubAreaNo+"    and subAreaNoCounter is     "+subAreaNoCounter);
        landUseCategory = stKey.substring(0,stKey.length()-2);//last character in parameter name
          if (landUseCategory.equals("LanduseDowntown_areas")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Downtown_areas",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseNeighborhood_areas")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Neighborhood_areas",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseSingle_family_areas")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Single_family_areas",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseSuburban")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Suburban",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseLight_areas")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Light_areas",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseHeavy_areas")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Heavy_areas",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseParks")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Parks",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LandusePlaygrounds")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Playgrounds",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
          if (landUseCategory.equals("LanduseStreets")){
            runoffCoeff = m_PTableOfCoefficients.getUrbanRunoffCoefficient("Streets",defaultHydroCondn);//hydro condition is assumed poor for all urban landuses in RP_Sewer
            }
            landuseCatAreaVector.addElement(String.valueOf(stValue));
            runoffCoeffVector.addElement(String.valueOf(runoffCoeff));
System.out.println("runoffCoeff in Prunoffcoeffiecient_sewer is    "+runoffCoeff+"    and landUsecategArea is     "+stValue);

        }//end of if(intCurrentSubAreaNo==subAreaNoCounter) statement
      }//end of while statement
    calculateOverallRunoffCoefficient();

    }//end of for (int k = 1; k <=totalNumberOfDrainageAreas;k++) statement


  }//end of the runProcess method


 public void calculateOverallRunoffCoefficient()throws Exception{

     double areaWeightedCoeff = 0.0;
     double subArea_areaWeightedCoeff = 0.0;
     double landuseCatAreaPercentage = 0.0;

     for ( int j=0; j <= landuseCatAreaVector.size()-1; j++ ){

        String landuseCatAreaStringPercentage = String.valueOf(landuseCatAreaVector.elementAt(j)).toString();
        landuseCatAreaPercentage = Double.parseDouble(landuseCatAreaStringPercentage);

        String runoffCoeffString = String.valueOf(runoffCoeffVector.elementAt(j)).toString();
        double runoffCoeff = Double.parseDouble(runoffCoeffString);

//        System.out.println("runoffCoeffString in Prunoffcoeffiecient_sewer is    "+runoffCoeffString);
//        System.out.println("landuseCatAreaPercentage in Prunoffcoeffiecient_sewer is    "+landuseCatAreaPercentage);

        areaWeightedCoeff = (landuseCatAreaPercentage/100.00)*runoffCoeff;
//System.out.println("areaWeightedCoeffOfIndividualcategories in Prunoffcoeffiecient_sewer is  "+areaWeightedCoeff);

        subArea_areaWeightedCoeff = subArea_areaWeightedCoeff + areaWeightedCoeff;
        //areaWeightedCoeff += areaWeightedCoeff;

     }//end of for statement

     //round off to three decimal places
     subArea_areaWeightedCoeff = Math.round(subArea_areaWeightedCoeff*1000)/1000.0;
     String subArea_areaWeightedCoeffString = String.valueOf(subArea_areaWeightedCoeff);
System.out.println("areaWeightedCoeffOfSubarea_"+subAreaNoCounter/*intCurrentSubAreaNo*/+"   in Prunoffcoeffiecient_sewer is  "+subArea_areaWeightedCoeff);
      m_IOInputHashTable.setValueOfParameter("WeightedRunoffCoeff_"+/*intCurrentSubAreaNo*/subAreaNoCounter,subArea_areaWeightedCoeffString);
//     data.setDoubleValue("rationalpeak.Data.DRunoffCoefficient",overAllCoeff);

       subArea_areaWeightedCoeff = 0.0;//reset to nil
       landuseCatAreaVector.clear();
       runoffCoeffVector.clear();
    }

}