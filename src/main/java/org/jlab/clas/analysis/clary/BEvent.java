package org.jlab.clas.analysis.clary;

import java.io.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.io.hipo.HipoDataSource;

public class BEvent {

    float mc_el_px;
    float mc_el_py;
    float mc_el_pz;
    float mc_el_vx;
    float mc_el_vy;
    float mc_el_vz;

    float mc_pr_px;
    float mc_pr_py;
    float mc_pr_pz;
    float mc_pr_vx;
    float mc_pr_vy;
    float mc_pr_vz;

    float mc_kp_px;
    float mc_kp_py;
    float mc_kp_pz;
    float mc_kp_vx;
    float mc_kp_vy;
    float mc_kp_vz;

    float mc_km_px;
    float mc_km_py;
    float mc_km_pz;
    float mc_km_vx;
    float mc_km_vy;
    float mc_km_vz;

    float rc_el_px;
    float rc_el_py;
    float rc_el_pz;
    float rc_el_vx;
    float rc_el_vy;
    float rc_el_vz;

    float rc_pr_px;
    float rc_pr_py;
    float rc_pr_pz;
    float rc_pr_vx;
    float rc_pr_vy;
    float rc_pr_vz;

    float rc_kp_px;
    float rc_kp_py;
    float rc_kp_pz;
    float rc_kp_vx;
    float rc_kp_vy;
    float rc_kp_vz;

    float rc_km_px;
    float rc_km_py;
    float rc_km_pz;
    float rc_km_vx;
    float rc_km_vy;
    float rc_km_vz;

    public static int golden_electron_index;
    public static int golden_proton_index;
    public static int golden_kp_index;
    public static int golden_km_index;

    public static double total_accumulatedcharge; 
	

    public void setEvent( DataEvent event ){

	//System.out.println("CREATING EVENT");
	DataBank mcBank = event.getBank("MC::Particle");
	
	mc_el_px = mcBank.getFloat("px",0);
        mc_el_py = mcBank.getFloat("py",0);
	mc_el_pz = mcBank.getFloat("pz",0);
	
	mc_pr_px = mcBank.getFloat("px",1);
	mc_pr_py = mcBank.getFloat("py",1);
	mc_pr_pz = mcBank.getFloat("pz",1);
	
	mc_kp_px = mcBank.getFloat("px",2);
	mc_kp_py = mcBank.getFloat("py",2);
	mc_kp_pz = mcBank.getFloat("pz",2);
	
	mc_km_px = mcBank.getFloat("px",3);
	mc_km_py = mcBank.getFloat("py",3);
	mc_km_pz = mcBank.getFloat("pz",3);
	
        mc_el_vx = mcBank.getFloat("vx",0);
        mc_el_vy = mcBank.getFloat("vy",0);
	mc_el_vz = mcBank.getFloat("vz",0);
	
	mc_pr_vx = mcBank.getFloat("vx",1);
	mc_pr_vy = mcBank.getFloat("vy",1);
	mc_pr_vz = mcBank.getFloat("vz",1);
	
	mc_kp_vx = mcBank.getFloat("vx",2);
	mc_kp_vy = mcBank.getFloat("vy",2);
	mc_kp_vz = mcBank.getFloat("vz",2);
	
	mc_km_vx = mcBank.getFloat("vx",3);
	mc_km_vy = mcBank.getFloat("vy",3);
	mc_km_vz = mcBank.getFloat("vz",3);
	
	

	DataBank recBank = event.getBank("REC::Particle");
	//recBank.show();
	

    }

    public void SetGoldenIndices( int tempel, int temppr, int tempkp, int tempkm ){
	SetElectronGoldenIndex(tempel);
	SetProtonGoldenIndex(temppr);
	SetKaonPlusGoldenIndex(tempkp);
	SetKaonMinusGoldenIndex(tempkm);
    }
   public void SetElectronGoldenIndex( int temp_el ){
	golden_electron_index = temp_el;
    }

   public  void SetProtonGoldenIndex( int temp_pr ){
	golden_proton_index = temp_pr;
    }

    public void SetKaonPlusGoldenIndex( int temp_kp ){
	golden_kp_index = temp_kp;
    }

   public  void SetKaonMinusGoldenIndex( int temp_km ){
	golden_km_index = temp_km;
    }

    public void setAccumulatedCharge( double temp_acharge ){
	total_accumulatedcharge = temp_acharge;
    }

    public static int GetElGoldenIndex(){
	return golden_electron_index;
    }
    public static int GetPrGoldenIndex(){
	return golden_proton_index;
    }
    public static int GetKPGoldenIndex(){
	return golden_kp_index;
    }
    public static int GetKMGoldenIndex(){
	return golden_km_index;
    }

    public static double getAccumulatedCharge(){
	return total_accumulatedcharge;
    }
}
