package src.com;
import src.com.MatchElectronPID;
import src.com.MatchElectronMntm;
import src.com.MatchElectronPhi;
import src.com.MatchElectronTheta;

import src.com.IParticleIdentifier;
import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.jnp.hipo.schema.*;

import java.util.*;
import java.io.*;

public class MatchingElectronPID implements IParticleIdentifier {

    MatchElectronPID el_pid_cut = new MatchElectronPID();
    MatchElectronMntm el_mntm_cut = new MatchElectronMntm();
    MatchElectronTheta el_theta_cut = new MatchElectronTheta();
    MatchElectronPhi el_phi_cut = new MatchElectronPhi();	

    Vector<BICandidate> match_cuts = new Vector<BICandidate>();
    boolean status = false;

    SchemaFactory factory;
    public MatchingElectronPID( ){
	//constructor
	factory  = new SchemaFactory();
	factory.initFromDirectory("CLAS12DIR", "etc/bankdefs/hipo");

	
    }

    public void initializeCuts(){

	match_cuts.add(el_pid_cut);
	match_cuts.add(el_mntm_cut);
	match_cuts.add(el_theta_cut);
	match_cuts.add(el_phi_cut);

    }

    public boolean processCuts( DataEvent tempevent, int rec_i ){

	boolean result = true;
	for( BICandidate cut : match_cuts ){
	    //System.out.println(" >> LOOPING OVER CUTS " );
	    if( !cut.candidate( tempevent, rec_i ) ){
		//System.out.println(" >> PASSED CUT " );
		result = false;
		break;		
	    }
	}
	return result;
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
	    hipoEv.getHipoEvent().getSchemaFactory().addSchema(new Schema("MATCH::electron",300,"index/I"));
	    //System.out.println(" >> 3 ");

	    DataBank pidBank = event.createBank("MATCH::electron",1);
	    //System.out.println(" >> 4 ");

	    pidBank.setInt("index",0,rec_i);
	    //System.out.println(" >> 5 ");

	    event.appendBank(pidBank);
	    ///System.out.println(" >> 6 ");

	}
	//Systeam.out.println(" >> 7 ");

	//return event;
	
    }


    //public 
      

}
