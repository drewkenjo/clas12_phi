package src.com;

import java.util.*;
import java.io.*;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.jnp.hipo.schema.*;

import src.com.Calculator;
import src.com.Detectors;

public class DCFiducialR3Cut implements BICandidate{

    public boolean candidate( DataEvent event, int rec_i ){

	boolean b_tbtrack = event.hasBank("TimeBasedTrkg::TBTracks");
	boolean b_tbcross = event.hasBank("TimeBasedTrkg::TBCrosses");
	boolean b_rectrack = event.hasBank("REC::Track");

	if( b_tbtrack && b_tbcross && b_rectrack ){
	    DataBank tbtrack = event.getBank("TimeBasedTrkg::TBTracks");
	    DataBank tbcross = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank rectrack = event.getBank("REC::Track");
	    
	    int dc_sector_r3 = Detectors.getDCSectorR3(event, rec_i) - 1;
	    if( dc_sector_r3 >= 0 ){
		Vector<Double> dc_r3_locxy = Detectors.getDCCrossLocalR3(event, rec_i);
	       
		double x_local = dc_r3_locxy.get(0);
		double y_local = dc_r3_locxy.get(1);
	    
		boolean d_up = y_local > 0.443*x_local + 93.0;
		boolean d_bottom = y_local > -0.434*x_local - 89.9;
		boolean d_left = x_local > -180.0;
		boolea d_right = x_local < 150.0;
		
		if( d_up && d_bottom && d_left && d_right ){
		    return true;
		}
	    }	    
	}		       
	return false;
    }
    

}
