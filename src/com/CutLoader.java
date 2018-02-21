package src.com;
import com.google.gson.*;

import java.io.*;
import java.util.*;

import src.com.RunInformation;
import org.json.*;
import org.jlab.jnp.utils.json.*;


public class CutLoader{

    String run_number = null;
    String cut_type = null;

    RunInformation runinfo;
    static List<Double> ecsf_max_s1;
    static List<Double> ecsf_max_s2;
    static List<Double> ecsf_max_s3; 
    static List<Double> ecsf_max_s4;
    static List<Double> ecsf_max_s5;
    static List<Double> ecsf_max_s6;

    static List<Double> ecsf_min_s1;
    static List<Double> ecsf_min_s2;
    static List<Double> ecsf_min_s3;
    static List<Double> ecsf_min_s4;
    static List<Double> ecsf_min_s5;
    static List<Double> ecsf_min_s6;

    public CutLoader(){

	
    }

    public void loadRunCuts(int run, String temp_cut_type){

	run_number = "r" + Integer.toString(run);
	cut_type = temp_cut_type;

	try{
	    Gson gson = new Gson();
	    BufferedReader br = new BufferedReader( new FileReader("/u/home/bclary/CLAS12/phi_analysis/v2/v1/run_db/CutDB_v3.json") );
	    runinfo = gson.fromJson(br, RunInformation.class );
	}
	catch( IOException e ){
	    System.out.println(" >> ERROR LOADING CUT DB JSON FILE " );
	}
	    
    }


    public void setECMaxSectorAllCuts( ){

	ecsf_max_s1 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector1();
	ecsf_max_s2 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector2();
	ecsf_max_s3 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector3();
	ecsf_max_s4 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector4();
	ecsf_max_s5 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector5();
	ecsf_max_s6 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMaxFitParametersSector6();

    }

    public void setECMinSectorAllCuts( ){

	ecsf_min_s1 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector1();
	ecsf_min_s2 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector2();
	ecsf_min_s3 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector3();
	ecsf_min_s4 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector4();
	ecsf_min_s5 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector5();
	ecsf_min_s6 = runinfo.getRunParameters().get(run_number).getCutType().get(cut_type).getECCutParameters().get("ec_sf_cut").getMinFitParametersSector6();

    }

    public void setRunCuts(){

	setECMaxSectorAllCuts();
	setECMinSectorAllCuts();

    }

    public void printRunCuts(){
	for( int parameter = 0; parameter < ecsf_max_s1.size(); parameter++){
	    System.out.println("EC Sampling Fraction MAX: S1: " + ecsf_max_s1.get(parameter) + " S2: " + ecsf_max_s2.get(parameter) + " S3: " + ecsf_max_s3.get(parameter) + " S4: " + ecsf_max_s4.get(parameter) + " S5 " + + ecsf_max_s5.get(parameter) + " S6: " + ecsf_max_s6.get(parameter) );
	}

	for( int parameter = 0; parameter < ecsf_min_s1.size(); parameter++){
	    System.out.println("EC Sampling Fraction MIN: S1: " + ecsf_min_s1.get(parameter) + " S2: " + ecsf_min_s2.get(parameter) + " S3: " + ecsf_min_s3.get(parameter) + " S4: " + ecsf_min_s4.get(parameter) + " S5 " + + ecsf_min_s5.get(parameter) + " S6: " + ecsf_min_s6.get(parameter) );
	}

    }

    static public List<Double> getECMaxSector1Cut(){ return ecsf_max_s1; }
    static public List<Double> getECMaxSector2Cut(){ return ecsf_max_s2; }
    static public List<Double> getECMaxSector3Cut(){ return ecsf_max_s3; }
    static public List<Double> getECMaxSector4Cut(){ return ecsf_max_s4; }
    static public List<Double> getECMaxSector5Cut(){ return ecsf_max_s5; }
    static public List<Double> getECMaxSector6Cut(){ return ecsf_max_s6; }

    static public List<Double> getECMinSector1Cut(){ return ecsf_min_s1; }
    static public List<Double> getECMinSector2Cut(){ return ecsf_min_s2; }
    static public List<Double> getECMinSector3Cut(){ return ecsf_min_s3; }
    static public List<Double> getECMinSector4Cut(){ return ecsf_min_s4; }
    static public List<Double> getECMinSector5Cut(){ return ecsf_min_s5; }
    static public List<Double> getECMinSector6Cut(){ return ecsf_min_s6; }


    

}
