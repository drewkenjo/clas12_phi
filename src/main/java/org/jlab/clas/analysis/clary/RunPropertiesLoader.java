package org.jlab.clas.analysis.clary;

import java.io.*;
import com.google.gson.*;
import org.json.*;
import org.jlab.jnp.utils.json.*;
import org.jlab.clas.analysis.clary.RunInformation;

public class RunPropertiesLoader{

    static double beamEnergy;
    static double total_accumulated_charge;
    String run_number;
    RunInformation runinfo;

    public RunPropertiesLoader(){

    }

    public void loadRunProperties(int run){
	run_number = "r" + Integer.toString(run);
	
	try{
	    Gson gson = new Gson();
	    BufferedReader br = new BufferedReader( new FileReader("/u/home/bclary/CLAS12/phi_analysis/v2/v1/run_db/CutDB_v3.json") );
	    runinfo = gson.fromJson(br, RunInformation.class );
	}
	catch( IOException e ){
	    System.out.println(" >> ERROR LOADING CUT DB JSON FILE " );
	}	
    }

    public void setRunProperties(){
	setBeamEnergy();
    }


    public void setBeamEnergy(){
	beamEnergy = runinfo.getRunParameters().get(run_number).getBeamEnergy();
    }

    static public double getBeamEnergy(){
	return beamEnergy;	
    } 

    public void setTotalAccumulatedCharge( double temp_totcharge ){
	total_accumulated_charge = temp_totcharge;
    }

    static public double getTotalAccumulatedCharge(){
	return total_accumulated_charge;
    }
    

}

