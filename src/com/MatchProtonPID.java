package src.com;

import src.com.BICandidate;

import java.io.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

class MatchProtonPID implements BICandidate {

    public boolean candidate( DataEvent tempdevent, int rec_i ){

	DataBank recdbank = tempdevent.getBank("REC::Particle");

	int rec_pid = recdbank.getInt("pid",rec_i);
	//	System.out.println(" >> PID " + rec_pid );
	
	if( rec_pid == 2212 ){
	    return true;
	}
	return false;

	
    }
}
