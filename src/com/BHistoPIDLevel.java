package src.com;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import org.jlab.analysis.plotting.TCanvasP;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.GraphicsAxis;

import org.jlab.groot.fitter.*;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.Axis;

import org.jlab.groot.math.*;
import org.jlab.clas.physics.*;

import src.com.Calculator;
import src.com.PhysicalConstants;
import src.com.Detectors;
import src.com.BPIDHistograms;
import src.com.BPIDProtonHistograms;
import src.com.BPIDKaonPlusHistograms;

import java.util.*;
import java.io.*;

public class BHistoPIDLevel {

    public BHistoPIDLevel(){
	//normal constructor;
    }

    private int run_number = -1;
    BPIDHistograms h_bpid;                                                                                                                                                                                 
    BPIDProtonHistograms h_bprotonpid;
    public BHistoPIDLevel(int temp_run){
	//constructor
	run_number = temp_run;
	h_bpid = new BPIDHistograms(run_number);
	h_bprotonpid = new BPIDProtonHistograms(run_number);
    }
    
    EmbeddedCanvas c_el = new EmbeddedCanvas();
    BPIDKaonPlusHistograms h_bkppid= new BPIDKaonPlusHistograms();
    
    Vector<H1F> v_el_pass = new Vector<H1F>();
    Vector<H2F> v2_el_pass = new Vector<H2F>();

    public void CreateHistograms(){
	
	h_bpid.createElectronHistoToHipoOut(0);

	for( int i = 0; i <= 6; i++ ){
	    h_bpid.createElectronHistograms(i);
	    h_bprotonpid.createProtonHistograms(i);
	    h_bkppid.createKaonPHistograms(i);
	}

	for( int j = 0; j <= 6; j++ ){
	    h_bpid.createElectronSectorHistograms(j,6);
	    h_bprotonpid.createProtonSectorHistograms(j,4);
	    h_bkppid.createKaonPSectorHistograms(j,3);
	}

	h_bprotonpid.createProtonFTOFHistograms(6,50,4);	   
	// s is for sector
	// p is for ftof bar or panel
	//for( int s = 0; s < 6; s++ ){ 
	//  for( int p = 0; p < 48; p++ ){
		
	//  }
	//}
		
    }

    public void FillElectronPID( Vector<Boolean> temp_pass, DataEvent event, int rec_i ){

	int j = 0;
	
	for( boolean pass : temp_pass ){
	    if( pass ){		
		DataBank recBank = event.getBank("REC::Particle");
		DataBank eventBank = event.getBank("REC::Event");
		
		LorentzVector lv_el = Calculator.lv_particle(recBank, rec_i, PhysicalConstants.electronID);
		
		double p = lv_el.p();
		double theta = Math.toDegrees(lv_el.theta());
		double phi = Math.toDegrees(lv_el.phi());
		
		double w = Calculator.W(lv_el);
		double q2 = Calculator.Q2(lv_el);

		float vz = recBank.getFloat("vz",rec_i);
		float clas12_t_start = eventBank.getFloat("STTime",0); //USING TIME PROVIDED BY EB		
	
		double nphe = Detectors.getCherenkovNPHE( event, rec_i );
		Map<Integer, Double> m_edep = Detectors.getEDepCal( event, rec_i );
		double ecdep_tot = 0; //ENERGY DEPOSITED WITHIN ALL LAYERS
	
		int sector_ec = Detectors.getSectorECAL( event, rec_i ) - 1; 
		int sector_dc = Detectors.getSectorDC( event, rec_i ) - 1;
		int sector_pcal = Detectors.getSectorPCAL( event, rec_i ) - 1;

		double e_pcal = 0.0;
		double e_ecal_ei = 0.0;
		double e_ecal_eo = 0.0;

		for( Map.Entry<Integer,Double> entry : m_edep.entrySet() ){
		    int layer = entry.getKey();
		    double edep = entry.getValue();		    
		    if( layer == Detectors.pcal ){
			h_bpid.h_el_pcaltot.get(j).fill(edep);	
			if( sector_ec >= 0 ){ h_bpid.h_el_sect_pcal.get(sector_ec).get(j).fill(edep); e_pcal = edep; }
		    }
		    if( layer == Detectors.ec_ei ){
			h_bpid.h_el_ecei.get(j).fill(edep);		
			if( sector_ec >= 0 ) {h_bpid.h_el_sect_ecei.get(sector_ec).get(j).fill(edep); e_ecal_ei = edep; }
		    }
		    if( layer == Detectors.ec_eo ){
			h_bpid.h_el_eceo.get(j).fill(edep);		
			if( sector_ec >= 0 ){h_bpid.h_el_sect_eceo.get(sector_ec).get(j).fill(edep); e_ecal_eo = edep;}
		    }		    
		    ecdep_tot = ecdep_tot + edep;
		}
		
		double e_ecal = e_ecal_ei + e_ecal_eo;
		h_bpid.h2_el_pcalecal.get(j).fill(e_ecal, e_pcal ); 

		h_bpid.h_el_p.get(j).fill( p );
		h_bpid.h_el_theta.get(j).fill( theta );
		h_bpid.h_el_phi.get(j).fill( phi );
		h_bpid.h_el_vz.get(j).fill( vz );
		h_bpid.h_el_timing.get(j).fill( clas12_t_start );
		h_bpid.h_el_nphe.get(j).fill(nphe);
		h_bpid.h_el_ectot.get(j).fill(ecdep_tot);	      
		h_bpid.h_el_edepdiff.get(j).fill(e_ecal - e_pcal);

		h_bpid.h_el_w.get(j).fill(w);

		h_bpid.h2_el_etotnphe.get(j).fill(nphe, ecdep_tot);
		h_bpid.h2_el_ectotp.get(j).fill(p, ecdep_tot/p);

		h_bpid.h2_el_pcalp.get(j).fill(p, e_pcal/p);
		h_bpid.h2_el_eieo.get(j).fill(e_ecal_ei, e_ecal_eo);

		h_bpid.h2_el_thetap.get(j).fill(p, theta);
		h_bpid.h2_el_phip.get(j).fill(p, phi);
		h_bpid.h2_el_edepdiff.get(j).fill( (e_ecal - e_pcal)/(e_ecal + e_pcal), e_ecal + e_pcal );

		

		Map<Integer,ArrayList<Double>> m_echit = Detectors.ECHit(event, rec_i);
		for( Map.Entry<Integer, ArrayList<Double> > entry : m_echit.entrySet() ){ 
		    int detector = entry.getKey(); 
		    ArrayList<Double> hit_pos = entry.getValue(); 
		    double x = hit_pos.get(0);
		    double y = hit_pos.get(1);
		    if( detector == 7 ){
			Vector<Double> ec_hit_rot = Calculator.getRotatedCoordinates(x,y,sector_ec);
			h_bpid.h2_el_echitxy.get(j).fill(x,y);
		    }
		}

  		/*
		if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
		    int dc_sector_r1 = Detectors.getDCSectorR1(event, rec_i) - 1;
		    double dc_x1 = Detectors.getDCCrossX1(event, rec_i);
		    double dc_y1 = Detectors.getDCCrossY1(event, rec_i);

		    int dc_sector_r3 = Detectors.getDCSectorR3(event, rec_i) - 1;
		    double dc_x3 = Detectors.getDCCrossX3(event, rec_i);
		    double dc_y3 = Detectors.getDCCrossY3(event, rec_i);
 		    //System.out.println(" >> DC HIT X Y " + dc_x1 + " " + dc_y1);

		    Vector<Double> dc_r1_rotxy = Calculator.getRotatedCoordinates(dc_x1,dc_y1,dc_sector_r1);
		    Vector<Double> dc_r3_rotxy = Calculator.getRotatedCoordinates(dc_x3,dc_y3,dc_sector_r3);
		    		   
		    if( dc_x1 > -1000 && dc_y1 > -1000 && dc_sector_r1 >= 0){
			Vector<Double> dc_r1_locxy = Detectors.getDCCrossLocalR1(event, rec_i);

			h_bpid.h2_el_dchitxy.get(j).fill(dc_r1_rotxy.get(0), dc_r1_rotxy.get(1));
			h_bpid.h2_el_sect_dc_R1_xy.get(dc_sector_r1).get(j).fill(dc_r1_rotxy.get(0), dc_r1_rotxy.get(1));			
			h_bpid.h2_el_sect_dc_R1_lxy.get(dc_sector_r1).get(j).fill(dc_r1_locxy.get(0), dc_r1_locxy.get(1) );
			h_bpid.h2_el_dchit_R1_lxy.get(j).fill(dc_r1_locxy.get(0), dc_r1_locxy.get(1));
		    }
		    
		    if( dc_x3 > -1000 && dc_y3 > -1000 && dc_sector_r3 >= 0){
			Vector<Double> dc_r3_locxy = Detectors.getDCCrossLocalR3(event, rec_i);

			h_bpid.h2_el_dchit_R3_xy.get(j).fill(dc_r3_rotxy.get(0), dc_r3_rotxy.get(1));
			h_bpid.h2_el_sect_dc_R3_xy.get(dc_sector_r3).get(j).fill(dc_r3_rotxy.get(0), dc_r3_rotxy.get(1) );
			h_bpid.h2_el_sect_dc_R3_lxy.get(dc_sector_r3).get(j).fill(dc_r3_locxy.get(0), dc_r3_locxy.get(1) );
			h_bpid.h2_el_dchit_R3_lxy.get(j).fill(dc_r3_locxy.get(0), dc_r3_locxy.get(1));

			
		    }
		   
		}
		*/
		
		
		Map<Integer,ArrayList<Double>> m_ecal_uvw = Calculator.xyzToUVW(event, rec_i);
		for( Map.Entry<Integer,ArrayList<Double> > entry : m_ecal_uvw.entrySet() ){
		    int detector = entry.getKey();
		    ArrayList<Double> hit_uvw = entry.getValue();
		    double u_cord = hit_uvw.get(0);
		    double v_cord = hit_uvw.get(1);
		    double w_cord = hit_uvw.get(2);
		    //System.out.println(" >> UVW HIT IN HISTO DETECTOR " + detector + " "  + hit_uvw.get(0) + " " + hit_uvw.get(1) + " " + hit_uvw.get(2) );
		    if( detector == 7 ){
			h_bpid.h_el_ecu.get(j).fill(u_cord);
			h_bpid.h_el_ecv.get(j).fill(v_cord);
			h_bpid.h_el_ecw.get(j).fill(w_cord);
		    }
		    
		}
			
		if( sector_dc >= 0 ){
		    h_bpid.h_el_sect_p.get(sector_dc).get(j).fill(p);
		    h_bpid.h_el_sect_theta.get(sector_dc).get(j).fill(theta);
		    h_bpid.h_el_sect_phi.get(sector_dc).get(j).fill(phi);
		    h_bpid.h_el_sect_vz.get(sector_dc).get(j).fill(vz);
		    h_bpid.h_el_sect_timing.get(sector_dc).get(j).fill(clas12_t_start);
		    h_bpid.h_el_sect_nphe.get(sector_dc).get(j).fill(nphe);
		    h_bpid.h_el_sect_ectot.get(sector_dc).get(j).fill(ecdep_tot);
 		    h_bpid.h_el_sect_w.get(sector_dc).get(j).fill(w);

		    h_bpid.h2_el_sect_thetap.get(sector_dc).get(j).fill(p, theta);
		    h_bpid.h2_el_sect_phip.get(sector_dc).get(j).fill(p, phi);
		    h_bpid.h2_el_sect_thetavz.get(sector_dc).get(j).fill(vz, theta);
		    h_bpid.h2_el_sect_phivz.get(sector_dc).get(j).fill(vz, phi);
		    h_bpid.h2_el_sect_pvz.get(sector_dc).get(j).fill(vz,p);		  
		    h_bpid.h2_el_sect_wp.get(sector_dc).get(j).fill(p, w);		  
		    h_bpid.h2_el_sect_q2w.get(sector_dc).get(j).fill(w, q2);
		}
		if( sector_pcal >= 0 ){
		    h_bpid.h2_el_sect_pcalp.get(sector_pcal).get(j).fill(p, e_pcal/p);

		    ArrayList<Double> al_pcalhit = Detectors.PCALHit(event, rec_i);		    
		    Vector<Double> pcal_rot_hit = Calculator.getRotatedCoordinates(al_pcalhit.get(0), al_pcalhit.get(1), sector_pcal);

		    h_bpid.h2_el_pcalhitxy.get(j).fill(pcal_rot_hit.get(0),pcal_rot_hit.get(1));
 		    h_bpid.h2_el_sect_pcalhitxy.get(sector_pcal).get(j).fill(pcal_rot_hit.get(0),pcal_rot_hit.get(1));
		    

		}
		if( sector_ec >= 0 ){
		    h_bpid.h_el_sect_ectot.get(sector_ec).get(j).fill(ecdep_tot);

		    h_bpid.h2_el_sect_etotnphe.get(sector_ec).get(j).fill(nphe,ecdep_tot);
		    h_bpid.h2_el_sect_ectotp.get(sector_ec).get(j).fill(p, ecdep_tot/p);

		    //h_bpid.h2_el_sect_pcalp.get(sector_ec).get(j).fill(p, e_pcal/p);
		    h_bpid.h2_el_sect_eieo.get(sector_ec).get(j).fill(e_ecal_ei, e_ecal_eo);

 		    h_bpid.h2_el_sect_pcalecal.get(sector_ec).get(j).fill(e_ecal, e_pcal);  
		}
		
		if( sector_pcal >= 0 ){

		}
	
	    }
	    else if( !pass ){
		break;
	    }	
	    j++;	    
	}
    }


    public void fillProtonPID(Vector<Boolean> temp_pass, DataEvent event, int rec_i ){ 

	int j = 0;
	
	for( boolean pass : temp_pass ){
	    if( pass ){		
		DataBank recBank = event.getBank("REC::Particle");
		DataBank eventBank = event.getBank("REC::Event");
 		DataBank scintBank = event.getBank("REC::Scintillator");

		LorentzVector lv_pr = Calculator.lv_particle(recBank,rec_i, PhysicalConstants.protonID);
		float pr_beta_clas12 = recBank.getFloat("beta",rec_i);

		double p = lv_pr.p();
		double theta = Math.toDegrees(lv_pr.theta());
		double phi = Math.toDegrees(lv_pr.phi());
		
		//CALCULATE DELTA TIME FOR EACH HIT
		for( int i = 0; i < scintBank.rows(); i++){
			    
		    int pindex = scintBank.getShort("pindex",i);
		    if( pindex == rec_i ){
			
			int scint_sector = scintBank.getInt("sector",i) - 1;
			int scint_detector = scintBank.getByte("detector",i);
			int scint_layer = scintBank.getByte("layer",i);
			int scint_bar = scintBank.getInt("component",i) - 1  ;
			
			double start_time = eventBank.getFloat("STTime",0);
			double r_path = scintBank.getFloat("path",i);
			double t_ftof = scintBank.getFloat("time",i);
			double pr_tof = t_ftof - start_time;
			double pr_beta_mntm = p/Math.sqrt(p*p + PhysicalConstants.mass_proton * PhysicalConstants.mass_proton);
			double pr_beta_time = r_path/pr_tof * (1.0/30.0); 
			double pr_tmeas = r_path/pr_beta_mntm * (1.0/30.0);
			double pr_deltime = -pr_tmeas + pr_tof;
			double pr_delbeta = pr_beta_clas12 - pr_beta_mntm;

		       
			//if( scint_sector >= 0 ){
			//}
			//System.out.println(" >> scint det " + scint_detector );
			if( scint_sector >= 0 && scint_layer == 1 && scint_bar >= 0 && scint_detector == 12){
			    //System.out.println(" >> " + scint_sector );
			    h_bprotonpid.h_pr_rpath.get(j).fill(r_path);
			    h_bprotonpid.h2_pr_tof.get(j).fill(p,pr_tof);		       
			    h_bprotonpid.h_pr_beta_mntm.get(j).fill(pr_beta_mntm);
			    
			    h_bprotonpid.h2_pr_deltimep.get(j).fill(p,pr_deltime);			
			    h_bprotonpid.h2_pr_betap.get(j).fill(p,pr_beta_clas12);		
			    
			    h_bprotonpid.h2_pr_deltabeta.get(j).fill(p, pr_delbeta);
			    h_bprotonpid.h_pr_p.get(j).fill(p);
			  
			    h_bprotonpid.h2_pr_sect_betap.get(scint_sector).get(j).fill(p,pr_beta_clas12);
			    h_bprotonpid.h2_pr_sect_deltabeta.get(scint_sector).get(j).fill(p, pr_delbeta);
			    h_bprotonpid.h2_pr_sect_deltimep.get(scint_sector).get(j).fill(p, pr_deltime);			    
			    h_bprotonpid.h_pr_beta_time.get(j).fill(pr_beta_clas12);

			    //////////////////////////
  
			    h_bprotonpid.m_pr_sect_panel_deltp.get(scint_sector).get(scint_bar).get(j).fill(p,pr_deltime);
			    //h_bprotonpid.h_pr_sect_panel_deltimep.get(scint_sector).get(scint_bar).get(j).fill(p,pr_deltime);
			}			
		    }
		}					       
	    }		    	    	  	    
	    else if( !pass) {
		break;
	    }
	    j++;
	}
    }

    public void fillKaonPPID (Vector<Boolean> temp_pass, DataEvent event, int rec_i ){ 

	int j = 0;	
	for( boolean pass : temp_pass ){
	    if( pass ){		
		DataBank recBank = event.getBank("REC::Particle");
		DataBank eventBank = event.getBank("REC::Event");
		DataBank scintBank = event.getBank("REC::Scintillator");
		
		LorentzVector lv_kp = Calculator.lv_particle(recBank,rec_i, PhysicalConstants.kaonplusID);
		float kp_beta_clas12 = recBank.getFloat("beta",rec_i);
		h_bkppid.h_kp_beta_time.get(j).fill(kp_beta_clas12);

		double p = lv_kp.p();
		double theta = Math.toDegrees(lv_kp.theta());
		double phi = Math.toDegrees(lv_kp.phi());
		double vz = recBank.getFloat("vz",rec_i);
		
		h_bkppid.h_kp_p.get(j).fill(p);
		h_bkppid.h_kp_theta.get(j).fill(theta);
		h_bkppid.h_kp_phi.get(j).fill(phi);
		h_bkppid.h_kp_vz.get(j).fill(vz);
		h_bkppid.h2_kp_thetap.get(j).fill(p, theta);

		//CALCULATE DELTA TIME FOR EACH HIT
		for( int i = 0; i < scintBank.rows(); i++){
			    
		    int pindex = scintBank.getShort("pindex",i);
		    if( pindex == rec_i ){

			int scint_sector = scintBank.getInt("sector",i) - 1;
 			double start_time = eventBank.getFloat("STTime",0);
			double r_path = scintBank.getFloat("path",i);
			double t_ftof = scintBank.getFloat("time",i);
			double kp_tof = t_ftof - start_time;
			double kp_beta_mntm = p/Math.sqrt(p*p + PhysicalConstants.mass_kaon*PhysicalConstants.mass_kaon);
			double kp_beta_time = r_path/kp_tof * (1.0/30.0); 
			double kp_tcalc = r_path/kp_beta_mntm * (1.0/30.0);
			double kp_tmeas = r_path/kp_beta_clas12 * (1.0/30.0);
			double kp_deltime = -kp_tcalc + kp_tof;
			double kp_delbeta = kp_beta_clas12 - kp_beta_mntm;

			h_bkppid.h_kp_rpath.get(j).fill(r_path);
			h_bkppid.h2_kp_tof.get(j).fill(p,kp_tof);		       
			h_bkppid.h_kp_beta_mntm.get(j).fill(kp_beta_mntm);

			h_bkppid.h2_kp_deltimep.get(j).fill(p,kp_deltime);			
			h_bkppid.h2_kp_betap.get(j).fill(p,kp_beta_clas12);		

			h_bkppid.h2_kp_deltabeta.get(j).fill(p, kp_delbeta);
			h_bkppid.h_kp_p.get(j).fill(p);
			
			if( scint_sector >= 0 ){
			    h_bkppid.h2_kp_sect_betap.get(scint_sector).get(j).fill(p,kp_beta_clas12);
			    h_bkppid.h2_kp_sect_deltabeta.get(scint_sector).get(j).fill(p,kp_delbeta);
			    h_bkppid.h2_kp_sect_deltimep.get(scint_sector).get(j).fill(p,kp_deltime);		   
			}		
		    }
		}	       
	    }
	    else if( !pass) {
		break;
	    }
	    j++;
	}
	
	
    }

    public void fillPNDriftChamber( DataEvent event, int rec_i ){

	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    int dc_sector_r1 = Detectors.getDCSectorR1(event, rec_i) - 1;
	    double dc_x1 = Detectors.getDCCrossX1(event, rec_i);
	    double dc_y1 = Detectors.getDCCrossY1(event, rec_i);
	

	    int dc_sector_r2 = Detectors.getDCSectorR2(event, rec_i) - 1;
    
	    int dc_sector_r3 = Detectors.getDCSectorR3(event, rec_i) - 1;
	    double dc_x3 = Detectors.getDCCrossX3(event, rec_i);
	    double dc_y3 = Detectors.getDCCrossY3(event, rec_i);
	    
	    Vector<Double> dc_r1_rotxy = Calculator.getRotatedCoordinates(dc_x1,dc_y1,dc_sector_r1);
	    Vector<Double> dc_r3_rotxy = Calculator.getRotatedCoordinates(dc_x3,dc_y3,dc_sector_r3);
	    
	    if( dc_x1 > -1000 && dc_y1 > -1000 && dc_sector_r1 >= 0){
		Vector<Double> dc_r1_locxy = Detectors.getDCCrossLocalR1(event, rec_i);
		
		h_bpid.h2_el_dc_R1_allcharge.fill(dc_r1_locxy.get(0), dc_r1_locxy.get(1));
 	    }

	    if( dc_sector_r2 >= 0){
		Vector<Double> dc_r2_locxy = Detectors.getDCCrossLocalR2(event, rec_i);
		
		h_bpid.h2_el_dc_R2_allcharge.fill(dc_r2_locxy.get(0), dc_r2_locxy.get(1));
	    }

	    if( dc_x3 > -1000 && dc_y3 > -1000 && dc_sector_r3 >= 0){
		Vector<Double> dc_r3_locxy = Detectors.getDCCrossLocalR3(event, rec_i);
		
		h_bpid.h2_el_dc_R3_allcharge.fill(dc_r3_locxy.get(0), dc_r3_locxy.get(1));
	    }
	    
	}
	
       
    }


    public void sliceNFitHistograms(){

	int counter = 0;
	HashMap<Integer, Vector<Double> > h2_temp_binX = new HashMap<Integer, Vector<Double> >();


	for( Vector<H2F> h2_temp : h_bpid.h2_el_sect_ectotp ){
	    System.out.println(">> PREPARING TO SLICE 'N' FIT " + h2_temp.get(0).getTitle() );
	    H2F h2_temp_rebinX = h2_temp.get(0).rebinX(5);
	    H2F h2_temp_rebinXY = h2_temp_rebinX.rebinY(5);

	    ArrayList <H1F> h_temp_rebinXY_sliceX = new ArrayList<H1F>();
	    h_temp_rebinXY_sliceX = h2_temp_rebinXY.getSlicesX();

	    Vector<Double> bin_center_temp = new Vector<Double>();
	    for( int bin = 1; bin < h2_temp_rebinXY.getXAxis().getNBins(); bin++ ){
		double bin_center = h2_temp_rebinXY.getXAxis().getBinCenter(bin);
		//System.out.println(" >> SECTOR " + counter + " BIN CENTER " + bin_center ); 
		bin_center_temp.add(bin_center);
		h2_temp_binX.put(counter,bin_center_temp);
		
	    }
		
	    h_bpid.m_el_sect_slices_ectotp.put(counter, h_temp_rebinXY_sliceX);
	    
	    //ParallelSliceFitter fit_temp = new ParallelSliceFitter(h2_temp_rebinXY);
	    //fit_temp.setMinBin(1);
	    //fit_temp.setMaxBin(18);
 	    //fit_temp.fitSlicesX();
	    //GraphErrors temp_mean = fit_temp.getMeanSlices();
	    //GraphErrors temp_sigma = fit_temp.getSigmaSlices();	   
	    //h_bpid.g_sf_sect_meansfits.add(temp_mean);
	    //h_bpid.g_sf_sect_sigmasfits.add(temp_sigma);
	    
	    counter=counter+1;
	}
	
	//System.out.println(" >> FITTING HISTOGRAMS ACROSS ALL SECTORS ");
	for( int sector = 0; sector < 6; sector++ ){
	    //System.out.println(" FITTING SECTOR " + sector );
	    ArrayList<H1F> al_h_ectotp = h_bpid.m_el_sect_slices_ectotp.get(sector);
	    //System.out.println( " >> NUMBER OF SLICES " + al_h_ectotp.size());

	    GraphErrors g_temp = h_bpid.g_sf_sect_means_bc.get(sector);
	    GraphErrors g_temp_sigmas = h_bpid.g_sf_sect_sigmas_bc.get(sector);
	    g_temp.setTitle("Mean Sector " + Integer.toString(sector));
	    g_temp_sigmas.setTitle(" SIGMAS SECTOR " + Integer.toString(sector));
	    
	    for( int n_htemp = 1; n_htemp < al_h_ectotp.size() -1; n_htemp++){
		System.out.println(" >> n_htemp " + n_htemp );
		H1F h_temp = al_h_ectotp.get(n_htemp);
		F1D fit_temp = Calculator.fitHistogram(h_temp);
		double fit_mean = fit_temp.getParameter(1);
		double fit_sigma = fit_temp.getParameter(2);
		double fit_mean_err = fit_temp.parameter(1).error();
		double fit_sigma_err = fit_temp.parameter(2).error();
		//System.out.println(" >> size of bin vector " + h2_temp_binX.get(sector).size() );
		//System.out.println(" >> ADDING POINT " + h2_temp_binX.get(sector).get(n_htemp) +  " " + fit_mean + " ERROR " + fit_mean_err + " SIG ERROR " + fit_sigma_err);
		if( fit_mean > 1.0 || fit_mean < 0.0 || Math.abs(fit_sigma) > 1.0 ){ continue; }
		g_temp.addPoint(h2_temp_binX.get(sector).get(n_htemp), fit_mean, fit_mean_err, fit_sigma_err);
		g_temp_sigmas.addPoint(h2_temp_binX.get(sector).get(n_htemp), fit_sigma, fit_mean_err, fit_sigma_err);
	    }

	}

    }

    public void savePIDHistograms( boolean view ){

	sliceNFitHistograms();
	
	h_bpid.electronHistoToHipo();
	h_bpid.slicedFitValuesToText();

	//h_bprotonpid.protonHistoToHipo();
	//h_bkppid.kaonpHistoToHipo();
	if( view ){
	    h_bpid.viewHipoOut();
	    //h_bprotonpid.viewHipoOut();
	    //h_bkppid.viewHipoOut();
	    h_bpid.printHistograms();
	}

    }

}
