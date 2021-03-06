package org.jlab.clas.analysis.clary;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.jnp.hipo.schema.*;

import org.jlab.clas.analysis.clary.Detectors;

import java.io.*;
import java.util.*;

class PCALFiducialCut implements BICandidate {

    public boolean candidate( DataEvent event, int rec_i ){

	if( event.hasBank("REC::Calorimeter") ){
	    DataBank pcalBank = event.getBank("REC::Calorimeter");

	    for( int i = 0; i < pcalBank.rows(); i++ ){
		int pindex  = pcalBank.getShort("pindex",i);
		
		if( pindex == rec_i ){
		    int layer = pcalBank.getInt("layer",i);
		    int sector_pcal = Detectors.getSectorPCAL( event, rec_i ) - 1; 
		    
		    if( layer == 1){
			ArrayList<Double> v_pcal_hit = Detectors.PCALHit(event, rec_i);
			Vector<Double> pcal_rotxy = Calculator.getRotatedCoordinates(v_pcal_hit.get(0), v_pcal_hit.get(1), sector_pcal);

			double x_rot = pcal_rotxy.get(0);
			double y_rot = pcal_rotxy.get(1);

			boolean d_left = y_rot > 1.86*x_rot + 51.0;
			boolean d_right = y_rot > -1.876*x_rot + 49.0;
			boolean d_up = y_rot < 330.0;
			boolean d_down = y_rot >  52.0;
			if( d_left && d_right && d_up && d_down ){
			    return true;
			}
		    }
		}
	    
	    }
	}
	return false;
    }

}
