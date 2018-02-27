package org.jlab.clas.analysis.clary;
import com.google.gson.*;

import java.io.*;
import java.util.*;

import org.jlab.clas.analysis.clary.CutType;

public class RunParameters{

    Map<String,CutType> cut_type;
    CutType cut_type2;
    int run_number;
    double beam_energy;

    public RunParameters(){
	
    }

    public int getRunNumber(){
	return run_number;
    }

    public double getBeamEnergy(){
	return beam_energy;
    }

    public Map<String, CutType> getCutType(){
    	return cut_type;
    }

    //public CutInformation getCutType2(){
    //	return cut_type2;
    //}




}
