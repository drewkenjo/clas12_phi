package org.jlab.clas.analysis.clary;

import java.util.*;
import java.io.*;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.jnp.hipo.schema.*;

import org.jlab.clas.analysis.clary.Calculator;
import org.jlab.clas.analysis.clary.Detectors;

public class DCFiducialR2Cut implements BICandidate{

    public boolean candidate( DataEvent event, int rec_i ){

	boolean b_tbtrack = event.hasBank("TimeBasedTrkg::TBTracks");
	boolean b_tbcross = event.hasBank("TimeBasedTrkg::TBCrosses");
	boolean b_rectrack = event.hasBank("REC::Track");

	if( b_tbtrack && b_tbcross && b_rectrack ){
	    DataBank tbtrack = event.getBank("TimeBasedTrkg::TBTracks");
	    DataBank tbcross = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank rectrack = event.getBank("REC::Track");
	    
	    int dc_sector_r2 = Detectors.getDCSectorR2(event, rec_i) - 1;
	    if( dc_sector_r2 >= 0 ){
		Vector<Double> dc_r2_locxy = Detectors.getDCCrossLocalR2(event, rec_i);
		
		double x_local = dc_r2_locxy.get(0);
		double y_local = dc_r2_locxy.get(1);
	    
		boolean d_up = y_local < 0.459*x_local + 69.0;
		boolean d_bottom = y_local > -0.460*x_local - 68.0;
		boolean d_left = x_local > -112.0;
		boolean d_right = x_local < 80.0;
		
		if( d_up && d_bottom && d_left && d_right ){
		    return true;
		}
	    }	    
	}		       
	return false;
    }
    

}
