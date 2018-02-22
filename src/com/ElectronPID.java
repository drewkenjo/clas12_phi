package src.com;

import src.com.NegativeChargeCut;
import src.com.ECHitCut;
import src.com.FTOFHitCut;
import src.com.ECSamplingFractionCut;
import src.com.PCALHitCut;
import src.com.PhotoElectronCut;
import src.com.SectorMatchCut;
import src.com.DCFiducialCut;
import src.com.DCFiducialR2Cut;
import src.com.DCFiducialR3Cut;
import src.com.ECALInnerOuterCut;
import src.com.PCALFiducialCut;


import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.jnp.hipo.schema.*;

import java.util.*;
import java.io.*;

public class ElectronPID implements IParticleIdentifier{

    Vector<BICandidate> v_cuts = new Vector<BICandidate>();
    boolean status = false;

    NegativeChargeCut charge_cut = new NegativeChargeCut();
    ECHitCut echit_cut = new ECHitCut();
    FTOFHitCut ftof_cut = new FTOFHitCut();
    ECSamplingFractionCut ecsf_cut = new ECSamplingFractionCut();
    PCALHitCut pcalhit_cut = new PCALHitCut();
    PhotoElectronCut nphe_cut = new PhotoElectronCut();
    SectorMatchCut sectormatch_cut = new SectorMatchCut();
    ECALInnerOuterCut ec_eieo_cut = new ECALInnerOuterCut();
    DCFiducialCut dcr1_fiducial_cut = new DCFiducialCut();
    DCFiducialR2Cut dcr2_fiducial_cut = new DCFiducialR2Cut();
    DCFiducialR3Cut dcr3_fiducial_cut = new DCFiducialR3Cut();
    PCALFiducialCut pcal_fiducial_cut = new PCALFiducialCut();

    SchemaFactory factory;
    public ElectronPID() {

	factory = new SchemaFactory();
	factory.initFromDirectory("CLAS12DIR","etc/bankdefs/hipo");

    }

    public void initializeCuts(){

	v_cuts.add(charge_cut);
	v_cuts.add(sectormatch_cut);
	//////////////
	//ENERGY CUTS
	v_cuts.add(ecsf_cut);	
	//v_cuts.add(ec_eieo_cut);
	v_cuts.add(pcalhit_cut);
	/////////////
	//FID CUTS
	v_cuts.add(pcal_fiducial_cut);
	v_cuts.add(dcr1_fiducial_cut);
	v_cuts.add(dcr2_fiducial_cut);
	v_cuts.add(dcr3_fiducial_cut);

    }

    public boolean processCuts( DataEvent tempevent, int rec_i ){
	//System.out.println(" >> in bool processCut ");
	boolean result = true;
	//int j = 0;
	for( BICandidate cut : v_cuts ){
	    if( !cut.candidate( tempevent, rec_i ) ){
		result = false;
		//System.out.println(" false " + j);
		//j++;
		break;
	    }
	}
	return result;
    }


    public Vector<Boolean> processCutsVector( DataEvent tempevent, int rec_i ){
	//System.out.println(" >> here " );
	Vector<Boolean> v_results = new Vector<Boolean>();
	v_results.clear();
	int i = 0;
	for( BICandidate cut : v_cuts ){
	    v_results.add(cut.candidate(tempevent, rec_i ));
	    i++;
	    //System.out.println( " CUT RESULT " + i + " "  + cut.candidate(tempevent, rec_i ) );
	}
	return v_results;

    } 

    public boolean processCutsVectorResults( Vector<Boolean> v_tempcuts ){
	
	boolean outcome = false;
	for( boolean results : v_tempcuts ){
	    if( !results ){
		outcome = false;
		break;
		//return false;
	    }
	    else if( results ){
		outcome = true;
	    }	    
	}
	return outcome;

    }

    public void getResult( DataEvent event, int rec_i ){
	//System.out.println("overwriting ");
	//DataBank tempBank = event.createBank("PID::Electron",1);
	//tempBank.setInt("index",0,rec_i);
	//event.appendBank(tempBank);
	
	//System.out.println(" >> GETTING RESULT TO PUT INTO BANK ");
	if( event instanceof HipoDataEvent ){
	    //System.out.println(" >> 1 ");
	    
	    HipoDataEvent hipoEv = (HipoDataEvent) event;
	    //System.out.println(" >> 2 ");

	    //hipoEv.getHipoEvent().getSchemaFactory().addSchema("PID::electron","index/I");
	    hipoEv.getHipoEvent().getSchemaFactory().addSchema(new Schema("PID::electron",300,"index/I"));
	    //System.out.println(" >> 3 ");

	    DataBank pidBank = event.createBank("PID::electron",1);
	    //System.out.println(" >> 4 ");

	    pidBank.setInt("index",0,rec_i);
	    //System.out.println(" >> 5 ");

	    event.appendBank(pidBank);
	    ///System.out.println(" >> 6 ");

	}
	//Systeam.out.println(" >> 7 ");

	//return event;      
    }

}
