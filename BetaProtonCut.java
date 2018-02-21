import java.util.*;
import java.io.*;


import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import src.com.Detectors;

class BetaProtonCut implements BICandidate {


    public boolean candidate( DataEvent event, int rec_i ) {


	if( event.hasBank("REC::Scintillator") ){

	    DataBank scintBank = event.getBank("REC::Scintillator");
	    for( int i = 0; i < scintBank.rows(); i++ ){

		int pindex = scintBank.getShort("pindex",i);
		int layer = scintBank.getByte("layer",i);

		if( rec_i == pindex && layer == 2 ){
		    float time = scintBank.getFloat("time",i);
		    //CALCULATE BETA HERE FOR PROTON 

		}
		    


	}

    }

}
