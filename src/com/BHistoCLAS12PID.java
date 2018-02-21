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

import org.jlab.groot.math.*;
import org.jlab.clas.physics.*;

import src.com.Calculator;
import src.com.PhysicalConstants;
import src.com.Detectors;
import src.com.BCLAS12Histograms;
import src.com.BCLAS12ProtonHistograms;
import src.com.BCLAS12KaonPlusHistograms;

import java.util.*;
import java.io.*;

public class BHistoCLAS12PID {

    int run_number = -1;
    BCLAS12Histograms h_clas12;
    BCLAS12ProtonHistograms h_clas12proton;
    BCLAS12KaonPlusHistograms h_clas12kp;

    public BHistoCLAS12PID( int temp_run ){
	run_number = temp_run;
	h_clas12 = new BCLAS12Histograms(run_number);
	h_clas12proton = new BCLAS12ProtonHistograms(run_number);
	h_clas12kp = new BCLAS12KaonPlusHistograms();
    }


    public void createHistograms(){
	
	h_clas12.createCLAS12ElectronHistograms(-1);
	h_clas12proton.createCLAS12ProtonHistograms(-1);
	h_clas12kp.createCLAS12KaonPHistograms(-1);

	for(int i = 0; i < 6; i++ ){	   
	    h_clas12.createCLAS12ElectronSectorHistograms( i );
	    h_clas12proton.createCLAS12ProtonSectorHistograms( i );
	}

	h_clas12proton.createProtonFTOFHistograms( 6, 48 );

    }

    public void fillElectronCLAS12PID( DataEvent event, int rec_i ){

	DataBank recBank = event.getBank("REC::Particle");

 	LorentzVector lv_el = Calculator.lv_particle(recBank, rec_i, PhysicalConstants.electronID);
	double p = lv_el.p();
	double theta = lv_el.theta() * 180.0/3.14159265;
	double phi = lv_el.phi() * 180.0/3.14159265;
	double w = Calculator.W(lv_el);
	
	Map<Integer, Double> m_edep = Detectors.getEDepCal( event, rec_i );
	double ecdep_tot = 0.0;
	double e_pcal = 0.0;
	double e_ecal_ei = 0.0;
	double e_ecal_eo = 0.0;

	for( Map.Entry<Integer,Double> entry : m_edep.entrySet() ){
	    int layer = entry.getKey();
	    double edep = entry.getValue();		    
	    ecdep_tot = ecdep_tot + edep;
	    if( layer == Detectors.pcal ){
		e_pcal = edep;
	    }
	    if( layer == Detectors.ec_ei ){
		e_ecal_ei = edep;
	    }
	    if( layer == Detectors.ec_eo ){
		e_ecal_eo = edep;
	    }
	}	
	//System.out.println(" ecdep " + ecdep_tot );

	double nphe = Detectors.getCherenkovNPHE( event, rec_i );

	float clas12_beta = recBank.getFloat("beta",rec_i);
	double clas12_vz = recBank.getFloat("vz",rec_i);

	h_clas12.h_el_p.fill(p);
	h_clas12.h2_el_thetap.fill(p, theta );
	h_clas12.h2_el_phip.fill(p, phi );
	h_clas12.h2_el_ectotp.fill(lv_el.p(), ecdep_tot/p );
	h_clas12.h2_el_etotnphe.fill(nphe, ecdep_tot );
	h_clas12.h2_el_betap.fill(p, clas12_beta );
	h_clas12.h2_el_eieo.fill(e_ecal_ei,e_ecal_eo);
	h_clas12.h2_el_pcalp.fill(p,e_pcal);
	h_clas12.h2_el_pcalecal.fill(e_ecal_ei + e_ecal_eo, e_pcal);
	h_clas12.h_el_w.fill(w);

	int sector_ec = Detectors.getSectorECAL(event, rec_i ) - 1;
	int sector_dc = Detectors.getSectorDC(event, rec_i ) - 1;
	int sector_pcal = Detectors.getSectorDC(event, rec_i ) - 1;

	if( sector_dc >= 0 ){
	    h_clas12.h2_el_sect_thetap.get(sector_dc).fill(p, theta);
	    h_clas12.h2_el_sect_phip.get(sector_dc).fill(p, phi);
	    h_clas12.h2_el_sect_vzp.get(sector_dc).fill(clas12_vz, p);
	    h_clas12.h_el_sect_w.get(sector_dc).fill(w);
	}       
	if( sector_pcal >= 0 ){
	    h_clas12.h2_el_sect_pcalp.get(sector_pcal).fill(p, e_pcal/p);	    
	}
	if( sector_ec >= 0 ){
 	    h_clas12.h2_el_sect_ectotp.get(sector_ec).fill(p, ecdep_tot/p);
	    h_clas12.h2_el_sect_etotnphe.get(sector_ec).fill(nphe, ecdep_tot);
	    h_clas12.h2_el_sect_betap.get(sector_ec).fill(p, clas12_beta);
	    h_clas12.h2_el_sect_pcalecal.get(sector_ec).fill(e_ecal_ei + e_ecal_eo, e_pcal );
	    h_clas12.h2_el_sect_eieo.get(sector_ec).fill(e_ecal_ei,e_ecal_eo);
	}
	
    }

    public void fillProtonCLAS12PID( DataEvent event, int rec_i ){

	DataBank recBank = event.getBank("REC::Particle");
	DataBank eventBank = event.getBank("REC::Event");
	DataBank scintBank = event.getBank("REC::Scintillator");
	
 	LorentzVector lv_pr = Calculator.lv_particle(recBank, rec_i, PhysicalConstants.protonID);
	
	double p = lv_pr.p();
	
	float pr_beta = recBank.getFloat("beta",rec_i);
	double pr_vz = recBank.getFloat("vz",rec_i);

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
		double pr_delbeta = pr_beta - pr_beta_mntm;

		
		//if( scint_sector >= 0 ){
		//}

		if( scint_sector >= 0 && scint_layer == 1 && scint_bar >=0 && scint_detector == 12 ){
		    h_clas12proton.h2_pr_sect_betap.get(scint_sector).fill(p, pr_beta);
		    h_clas12proton.h_pr_sect_vz.get(scint_sector).fill(pr_vz);

		    h_clas12proton.h2_pr_betap.fill(p, pr_beta);
		    h_clas12proton.m_pr_sect_panel_deltp.get(scint_sector).get(scint_bar).fill(p, pr_deltime);
		}
	    }
	}
	

    }

    public void fillKaonPCLAS12PID( DataEvent event, int rec_i ){
	DataBank recBank = event.getBank("REC::Particle");
	DataBank scintBank = event.getBank("REC::Scintillator");
	DataBank eventBank = event.getBank("REC::Event");

	LorentzVector lv_kp = Calculator.lv_particle(recBank,rec_i,PhysicalConstants.kaonplusID);
	double p = lv_kp.p();

	float kp_beta_clas12 = recBank.getFloat("beta",rec_i);
	double theta = Math.toDegrees(lv_kp.theta());
	double phi = Math.toDegrees(lv_kp.phi());

	h_clas12kp.h2_kp_betap.fill(p,kp_beta_clas12);
	h_clas12kp.h2_kp_thetap.fill(p,theta);

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
		double kp_tof = t_ftof - start_time;
		double kp_beta_mntm = p/Math.sqrt(p*p + PhysicalConstants.mass_kaon*PhysicalConstants.mass_kaon);
		double kp_beta_time = r_path/kp_tof * (1.0/30.0); 
		double kp_tmeas = r_path/kp_beta_clas12 * (1.0/30.0);
		double kp_deltime = -kp_tmeas + kp_tof;
		double kp_delbeta = kp_beta_clas12 - kp_beta_mntm;
		
		h_clas12kp.h2_kp_deltimep.fill(p,kp_deltime);
		h_clas12kp.h2_kp_deltabeta.fill(p,kp_delbeta);
		h_clas12kp.h2_kp_tof.fill(p,kp_tof);

	    }
	}
	
	
    }

    public void saveCLAS12PIDHistograms( boolean view ){

	h_clas12.h_el_p.setTitle("Electron p");
	h_clas12.h_el_p.setTitleX("p [GeV]");

	EmbeddedCanvas c1 = new EmbeddedCanvas();
	c1.setSize(800,800);
 	h_clas12.h2_el_betap.setTitle("Electron #beta vs p");
	h_clas12.h2_el_betap.setTitleX("p [GeV]");
	h_clas12.h2_el_betap.setTitleY("#beta");
	c1.draw(h_clas12.h2_el_betap,"colz");
	c1.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/clas12pid_el_betap.png");

	EmbeddedCanvas c2 = new EmbeddedCanvas();
	c2.setSize(800,800);
	h_clas12.h2_el_etotnphe.setTitle("Electron Etot vs Nphe");
	h_clas12.h2_el_etotnphe.setTitleX("Nphe");
	h_clas12.h2_el_etotnphe.setTitleY("Etot [GeV]");
	c2.draw(h_clas12.h2_el_etotnphe,"colz");
	c2.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/clas12pid_el_etotnphe.png");


	h_clas12.h2_el_phip.setTitle("Electron #phi vs p");
	h_clas12.h2_el_phip.setTitleX("p [GeV]");
	h_clas12.h2_el_phip.setTitleY("#phi [deg]");

	h_clas12.h2_el_ectotp.setTitle("Electron SF vs p");
	h_clas12.h2_el_ectotp.setTitleX("p [GeV]");
	h_clas12.h2_el_ectotp.setTitleY("etot/p");

	h_clas12.h2_el_thetap.setTitle("Electron #theta vs p");
	h_clas12.h2_el_thetap.setTitleX("p [GeV]");
	h_clas12.h2_el_thetap.setTitleY("#theta [deg]");

	
	


	//h_clas12.clas12ElectronHistoToHipo();
	//h_clas12proton.clas12ProtonHistoToHipo();
	//h_clas12kp.clas12KaonPHistoToHipo();

	if( view ){
	    //h_clas12.viewHipoOut();
	    //h_clas12proton.viewHipoOut();
	    //h_clas12kp.viewHipoOut();
	    h_clas12.printHistograms();
	}
    }

}
