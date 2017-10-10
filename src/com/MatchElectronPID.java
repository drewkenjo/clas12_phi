package src.com;

import java.io.*;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

public class MatchElectronPID implements BICandidate2 {

    public boolean candidate2(DataEvent tempdevent, int index, int rec_i){
	//System.out.println("Applying Charge Cuts" );

	DataBank recdbank = tempdevent.getBank("REC::Particle");

	int rec_pid = recdbank.getInt("pid",rec_i);
	
	if( rec_pid == 11 ){
	    return true;
	}
	return false;
    }
}
