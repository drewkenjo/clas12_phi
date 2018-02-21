package src.com;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

public class ProtonCuts{

    static public int positivecharge;
    static public int ftof_layer1;

    public ProtonCuts( ){
	positivecharge = 1;
	ftof_layer1 = 1;
	
    }
}

class PositiveChargeCut implements BICandidate{


    public boolean candidate( DataEvent event, int rec_index ){

	DataBank recBank = event.getBank("REC::Particle");
	int charge = recBank.getInt("charge",rec_index);
	if( charge == ProtonCuts.positivecharge ){
	    return true;
	}
	return false;
    }

}

class FTOFHitLayer1Cut implements BICandidate{

    public boolean candidate( DataEvent event, int rec_index ){
	boolean layer1 = false;
	boolean layer2 = false;
	boolean ctof = false;
	if( event.hasBank("REC::Scintillator") ){
	    DataBank scintBank = event.getBank("REC::Scintillator");	   	    
	    for( int i = 0; i < event.getBank("REC::Scintillator").rows(); i++ ){
		if( scintBank.getShort("pindex", i) == rec_index ){
		    int scint_layer = scintBank.getInt("layer",i);
		    if( scint_layer == ProtonCuts.ftof_layer1  ){
			layer1 = true;			
			//return true;
		    }
		    if( scint_layer == 2 ){
			layer2 = true;
		    }
		    if( scint_layer == 0 ){
			ctof = true;
		    }
		}
	    }
	}
	if( layer1 && layer2 && !ctof ){ return true; }
 	return false;
    }

}
