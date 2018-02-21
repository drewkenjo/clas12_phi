package src.com;

import src.com.MatchProtonPID;
import src.com.MatchProtonMntm;
import src.com.MatchProtonTheta;
import src.com.MatchProtonPhi;
import src.com.IParticleIdentifier;

import org.jlab.io.hipo.HipoDataEvent;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.jnp.hipo.schema.*;

import java.util.*;
import java.io.*;

public class MatchingProtonPID implements IParticleIdentifier {

    MatchProtonPID pr_pid_cut = new MatchProtonPID();
    MatchProtonMntm pr_mntm_cut = new MatchProtonMntm();
    MatchProtonTheta pr_theta_cut = new MatchProtonTheta();
    MatchProtonPhi pr_phi_cut = new MatchProtonPhi();
    
    Vector<BICandidate> match_cuts = new Vector<BICandidate>();
    boolean status = false;

    SchemaFactory factory;
    public MatchingProtonPID() {
	factory = new SchemaFactory();
	factory.initFromDirectory("CLAS12DIR", "etc/bankdefs/hipo");

    }

    public void initializeCuts(){

	//match_cuts.add(pr_pid_cut);
	match_cuts.add(pr_mntm_cut);
	match_cuts.add(pr_theta_cut);
	match_cuts.add(pr_phi_cut);

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
	
	//System.out.println(" >> GETTING RESULT TO PUT INTO BANK ");
	if( event instanceof HipoDataEvent ){
	    //System.out.println(" >> 1 ");
	    
	    HipoDataEvent hipoEv = (HipoDataEvent) event;
	    //System.out.println(" >> 2 ");

	    hipoEv.getHipoEvent().getSchemaFactory().addSchema(new Schema("PID::proton",300,"index/I"));
	    ///System.out.println(" >> 3 ");

	    DataBank pidBank = event.createBank("PID::proton",1);
	    ///System.out.println(" >> 4 ");

	    pidBank.setInt("index",0,rec_i);
	    ///System.out.println(" >> 5 ");

	    event.appendBank(pidBank);
	    ////System.out.println(" >> 6 ");

	}
	///System.out.println(" >> 7 ");

	//return event;
	
    }



}
