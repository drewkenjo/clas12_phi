package org.jlab.clas.analysis.clary;

import com.google.gson.*;

import java.io.*;
import java.util.*;

import org.jlab.clas.analysis.clary.ECCutParameters;//CutDetectors;

public class CutType{

    int cutlvl;
    Map<String, ECCutParameters> cut_ecdetectors;

    public CutType(){

    }

    public int getCutLevel(){
	return cutlvl;
    }

    public Map<String, ECCutParameters> getECCutParameters() { //CutDetectors> getCutDetectors(){
	return cut_ecdetectors;
    }
    

}
