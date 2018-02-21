package src.com;

import java.io.*;
import java.util.*; //ArrayList;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import src.com.Detectors;

public class ElectronCuts {
    
    static public int negativecharge;// = -1;
    static public int idFTOF;

    public ElectronCuts(){
	//System.out.println(" >> ELECTRON CUTS CONSTRUCTOR ");	
	this.negativecharge = -1;
	this.idFTOF = 12; //for ftof. 4 is for ctof
    }

    

}




class FTOFHitCut implements BICandidate {

    public boolean candidate( DataEvent event, int rec_index ){
	//System.out.println(">>FTOFCUT - index " + rec_index );
	DataBank recBank = event.getBank("REC::Particle");
	boolean scintBank_present = event.hasBank("REC::Scintillator");
	if( scintBank_present ){
	    //  System.out.println(" >> scint bank present " );
	    DataBank scintBank = event.getBank("REC::Scintillator");
	    for( int i = 0; i < scintBank.rows(); i++ ){
		int pindex = scintBank.getShort("pindex",i);
		int scint_detector = scintBank.getInt("detector",i);
		//	System.out.println(" >> element " + i + " pindex " + pindex + " detector " + scint_detector );
		if( pindex == rec_index && scint_detector == ElectronCuts.idFTOF ){
		    //System.out.println( " >>> PASSING element " + i + " pindex " + pindex + " detector " + scint_detector ); 
		    return true;
		}
	    }
	}
	return false;
    }

}


class ECSamplingFractionCut implements BICandidate {
    
    
    public boolean candidate( DataEvent event, int rec_i ){
	//System.out.println(" >> IN ECSAMPLINGFRACTION CUT " );
	
	if( event.hasBank("REC::Calorimeter") ){
	    //  System.out.println(" >> HIT HAS CALORIMETER TRACKS " );
	    HashMap<Integer, Double> m_edep = Detectors.getEDepCal( event, rec_i );

	    //double edep_1 = m_edep.getItem(1);
	    //double edep_2 = m_edep.getItem(2);
	    //double edep_3 = m_edep.getItem(3);

	    double edep_tot = 0;// edep_1 + edep_2 + edep_3;
	    
	    DataBank recBank = event.getBank("REC::Particle");	    
	    double p = Calculator.lv_particle(recBank, rec_i, 11).p();
	    
	    for( Map.Entry<Integer, Double> entry : m_edep.entrySet() ){
		int detector = entry.getKey();
		double edep = entry.getValue();
		//	System.out.println(" >> MAP KEY " + detector + " MAP VALUE " + edep );		
		edep_tot = edep_tot + edep;
	    }
	    double ec_sf = edep_tot/p;
	    //System.out.println(" >> etot " + edep_tot + " p " + p );
	    //System.out.println(" >> SAMPLING FRACTION " + ec_sf );

	    if ( ec_sf > 0.12 ){
		return true;
	    }
	}
	return false;
    }
    
}


class PhotoElectronCut implements BICandidate {

    public boolean candidate( DataEvent event, int rec_i ){

	int nphe = 0;
	//System.out.println(" >> NPHE CUT " );
	if( event.hasBank("REC::Cherenkov") ){
	    //System.out.println(" >> HAS CHERENKOV " );
	    DataBank chkovBank = event.getBank("REC::Cherenkov");
	    for( int i = 0; i < chkovBank.rows(); i++ ){
		int pindex =  chkovBank.getShort("pindex",i); 
		//System.out.println(" >> PINDEX " + pindex );
		if( pindex == rec_i ){		    
		    nphe = chkovBank.getShort("nphe",i);
		    //System.out.println(" >> NPHE " + nphe );		    
		}
	    }
	    if( nphe > 0 ){
	    return true;
	    }
	}
	return false;


    }
}


class ECHitCut implements BICandidate {
    
    public boolean candidate( DataEvent event, int rec_i ){
	
	HashMap<Integer,ArrayList< Double >> m_ec_hit = new HashMap<Integer, ArrayList<Double> >();
	//System.out.println(" >> TESTING EC HIT POS " );
	if( event.hasBank("REC::Calorimeter") ){
	    DataBank ecBank = event.getBank("REC::Calorimeter");
	    //System.out.println(" >> HAS CAL BANK " );
	    m_ec_hit = Detectors.ECHit(event, rec_i);
	    if( m_ec_hit.size() > 0 ){
		//System.out.println(" HIT EI OR EO " );
		for(Map.Entry<Integer, ArrayList<Double> > entry : m_ec_hit.entrySet() ){
		    ArrayList<Double> hit_pos = new ArrayList();
		    int detector = entry.getKey();
		    hit_pos = entry.getValue();
		    if( hit_pos.size() > 0 ){
			//System.out.println(" >> EC HIT  " + detector + " " + hit_pos.get(0) + " " +  hit_pos.get(1) + " " +  hit_pos.get(2) );		    
		    }
		}
		return true;
	    }
	}
	return false;			          	
    }    
}

class PCALHitCut implements BICandidate {

    public boolean candidate( DataEvent event, int rec_i ){

	ArrayList< Double > pcal_hit = new ArrayList();
	if( event.hasBank("REC::Calorimeter") ){
	    DataBank ecBank = event.getBank("REC::Calorimeter");	    
	    pcal_hit = Detectors.PCALHit(event, rec_i);
	    //CHANGE LATER
	    if( pcal_hit.size() > 0 ){
		//System.out.println(" >> PCAL HIT POSITION " + pcal_hit.get(0) + " " + pcal_hit.get(1) + " " + pcal_hit.get(2) );	    
		
		//if( pcal_hit.get(0) > 0 && pcal_hit.get(1) > 0 ){		
		return true;
		
		//}
	    }
	}
	return false;			      
    }
}
    
    
//CHECK FOR A HIT IN EITHER PCAL OR ECAL
class CalorimeterCut implements BICandidate {
    
    public boolean candidate( DataEvent event, int rec_i ){
	
	ArrayList< Double > pcal_hit = new ArrayList();
	HashMap<Integer,ArrayList< Double >> m_ec_hit = new HashMap<Integer, ArrayList<Double> >();
	//System.out.println(" >> cal test " );
	if( event.hasBank("REC::Calorimeter") ){
	    DataBank ecBank = event.getBank("REC::Calorimeter");	    
	    pcal_hit = Detectors.PCALHit(event, rec_i);
	    m_ec_hit = Detectors.ECHit(event, rec_i);
	    
	    //if( pcal_hit.size() > 0  || m_ec_hit.size() > 0 ){
	    return true;
		//}
	}
	return false;			      
    }
}




