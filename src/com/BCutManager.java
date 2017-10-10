package src.com;

import src.com.MatchElectronPID;
import src.com.MatchProtonPID;
import src.com.MatchKaonMinusPID;
import src.com.MatchKaonPlusPID;

import src.com.MatchElectronPhi;
import src.com.MatchProtonPhi;
import src.com.MatchKaonPhi;

import src.com.MatchElectronTheta;
import src.com.MatchProtonTheta;
import src.com.MatchKaonTheta;

import src.com.MatchElectronMntm;
import src.com.MatchProtonMntm;
import src.com.MatchKaonMntm;

import java.util.*;
import java.io.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

public class BCutManager {

    MatchElectronPID el_pid_cut = new MatchElectronPID();
    MatchProtonPID pr_pid_cut = new MatchProtonPID();
    MatchKaonPlusPID kp_pid_cut = new MatchKaonPlusPID();
    MatchKaonMinusPID km_pid_cut = new MatchKaonMinusPID();
    
    MatchElectronMntm el_mntm_cut = new MatchElectronMntm();
    MatchProtonMntm pr_mntm_cut = new MatchProtonMntm();
    MatchKaonMntm kpkm_mntm_cut = new MatchKaonMntm();

    MatchElectronTheta el_theta_cut = new MatchElectronTheta();
    MatchProtonTheta pr_theta_cut = new MatchProtonTheta();
    MatchKaonTheta kpkm_theta_cut = new MatchKaonTheta();

    MatchElectronPhi el_phi_cut = new MatchElectronPhi();
    MatchProtonPhi pr_phi_cut = new MatchProtonPhi();
    MatchKaonPhi kpkm_phi_cut = new MatchKaonPhi();

    Vector<BICandidate2> applied_electron_cuts = new Vector<BICandidate2>();
    Vector<BICandidate2> applied_proton_cuts = new Vector<BICandidate2>();
    Vector<BICandidate2> applied_kp_cuts = new Vector<BICandidate2>();
    Vector<BICandidate2> applied_km_cuts = new Vector<BICandidate2>();

    boolean status = true;

    String s_electron = "electron";
    String s_proton = "proton";
    String s_kaon_plus = "kaon_plus";
    String s_kaon_minus = "kaon_minus";

    public void InitializeCutsFor(String tempparticlename ){
	//	System.out.println(">> INIT CUTS ");
	if( tempparticlename == s_electron ){
	    ElectronCuts();
	}
	else if ( tempparticlename == s_proton ){
	    ProtonCuts();
	}
	else if( tempparticlename == s_kaon_plus ){
	    KaonPlusCuts();	    
	}
	else if( tempparticlename == s_kaon_minus ){
	    KaonMinusCuts();
	}
    }

    public void ElectronCuts(){	
	applied_electron_cuts.add(el_pid_cut);
	applied_electron_cuts.add(el_mntm_cut);
	applied_electron_cuts.add(el_theta_cut);
	applied_electron_cuts.add(el_phi_cut);
    }

    public void ProtonCuts(){
	applied_proton_cuts.add(pr_pid_cut);
	applied_proton_cuts.add(pr_mntm_cut);
	applied_proton_cuts.add(pr_theta_cut);
	applied_proton_cuts.add(pr_phi_cut);
    }

    public void KaonPlusCuts(){
	applied_kp_cuts.add(kp_pid_cut);
	applied_kp_cuts.add(kpkm_mntm_cut);
	applied_kp_cuts.add(kpkm_theta_cut);
	applied_kp_cuts.add(kpkm_phi_cut);
    }

    public void KaonMinusCuts(){
	applied_km_cuts.add(km_pid_cut);
	applied_km_cuts.add(kpkm_mntm_cut);
	applied_km_cuts.add(kpkm_theta_cut);
	applied_km_cuts.add(kpkm_phi_cut);
	
    }

    public void KaonLongCuts(){
	//FOR LATER

    }

    public void PionCuts(){
	//FOR LATER

    }

    public boolean passCut(String tempparticle, DataEvent tempevent, int index, int rec_i ){
	status = true;
	//System.out.println(">> COMMENCE PASS CUT ROUTINE ");
	
	if( tempparticle == "electron" ){
	    status = passRoutine( applied_electron_cuts, tempevent, index, rec_i );
	}
	else if ( tempparticle == "proton" ){
	    status = passRoutine( applied_proton_cuts, tempevent, index, rec_i );
	}

	else if ( tempparticle == "kaon_plus"){
	    status = passRoutine( applied_kp_cuts, tempevent, index, rec_i );
	}
	else if ( tempparticle == "kaon_minus"){
	    status = passRoutine( applied_km_cuts, tempevent, index, rec_i );
	}
	else{
	    System.out.println(">> PLEASE ENTER 'electron', 'proton', 'kaon_plus', 'kaon_minus'");
	}
	return status;

    }


    public boolean passRoutine( Vector<BICandidate2> tempapplied, DataEvent tempevent, int index, int rec_i ){
	//System.out.println(">> PROCESSING CUTS..");
	//System.out.println(">> NUMBER OF CUTS: " + tempapplied.size() );
	boolean result = true;
	for(BICandidate2 cut: tempapplied ){
	    //System.out.println(i);	    
	    if( !(cut.candidate2(tempevent, index, rec_i)) ){
		result = false;
		break;
	    }
	}
	return result;
    }

   
}
