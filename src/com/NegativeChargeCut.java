package src.com;

import java.io.*;
import java.util.*;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;


class NegativeChargeCut implements BICandidate {

    public boolean candidate( DataEvent event, int rec_index ){

	DataBank recBank = event.getBank("REC::Particle");
	int charge = recBank.getInt("charge",rec_index);
	if( charge == -1 ){
	    //System.out.println(" >> IN ELECTRON CHARGE CUT " + charge  );
	    
	    return true;
	}
	return false;
    }

}
