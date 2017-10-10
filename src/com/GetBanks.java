package src.com;

import java.io.*;
import java.util.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

public class GetBanks {

    Vector pr_indices = new Vector();

    public Vector particleIndices(DataEvent tempevent){
	Vector el_indices = new Vector();

	if( tempevent.hasBank("REC::Particle") && tempevent.getBank("REC::Particle").rows()>0 ){	    
	    DataBank recBank = tempevent.getBank("REC::Particle");
	    for( int i = 0; i < tempevent.getBank("REC::Particle").rows(); i++){
		int pid = recBank.getInt("pid",0);
		//if( pid == 11) { el_indices.add(i); System.out.println(" >> el index " + i );}
		//if( pid == 2212 ){ pr_indices.add(i); System.out.println(" >> pr index " + i );}
	    }
	}
	else{
	    el_indices.add(-1);// = null;
	}
	System.out.println(el_indices.size());
	return el_indices;
    }


    



}

