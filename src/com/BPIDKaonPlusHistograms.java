package src.com;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.*;

import java.awt.*;
import org.jlab.groot.group.*;
import org.jlab.groot.ui.TBrowser;
import org.jlab.groot.tree.*;
import org.jlab.groot.data.TDirectory;
import org.jlab.jnp.hipo.io.*;
import org.jlab.groot.graphics.EmbeddedCanvas;

import java.util.*;
import java.io.*;

public class BPIDKaonPlusHistograms {

    TDirectory dir = new TDirectory();
    
    double min_p = 0.0; double max_p = 6.5;
    double min_theta = 0.0; double max_theta = 60.0;
    double min_phi = -180.0; double max_phi = 180.0;
    double min_vz = -5.0; double max_vz = 10.0;
    double min_timing = 0.0; double max_timing = 700.0;
    double min_delt = -1.57; double max_delt = 1.57;
    double min_delb = -1.0; double max_delb = 1.0;
    double min_b = 0.0; double max_b = 1.4;
    double min_rpath = 300.0; double max_rpath = 750.0; 
    double min_tof = 15.0; double max_tof = 35.0;

    Vector<H1F> h_kp_p = new Vector<H1F>(); 
    Vector<H1F> h_kp_theta = new Vector<H1F>(); 
    Vector<H1F> h_kp_phi = new Vector<H1F>(); 
    Vector<H1F> h_kp_vz = new Vector<H1F>(); 
    Vector<H1F> h_kp_timing = new Vector<H1F>(); 

    Vector<H2F> h2_kp_deltimep = new Vector<H2F>(); 
    Vector<H2F> h2_kp_deltabeta = new Vector<H2F>();
    Vector<H2F> h2_kp_betap = new Vector<H2F>();
    Vector<H2F> h2_kp_thetap = new Vector<H2F>();

    Vector<H1F> h_kp_rpath = new Vector<H1F>();
    Vector<H2F> h2_kp_tof = new Vector<H2F>();
    Vector<H1F> h_kp_beta_time = new Vector<H1F>();
    Vector<H1F> h_kp_beta_mntm = new Vector<H1F>();

    Vector<Vector<H1F>> h_kp_sect_p = new Vector< Vector<H1F> >();
    Vector<Vector<H1F>> h_kp_sect_theta = new Vector< Vector<H1F> >();
    Vector<Vector<H1F>> h_kp_sect_phi = new Vector< Vector<H1F> >();

    Vector<Vector<H2F>> h2_kp_sect_betap = new Vector< Vector<H2F> >();
    Vector<Vector<H2F>> h2_kp_sect_deltabeta = new Vector<Vector<H2F> >();
    Vector<Vector<H2F>> h2_kp_sect_deltimep = new Vector<Vector<H2F> >();
    

    Vector<Vector<H2F>> h_kp_sect_panel_deltp = new Vector< Vector<H2F> >();   
    Vector< Vector< Vector< H2F > > > h_kp_sect_panel_deltimep = new Vector< Vector< Vector<H2F> > >(); 

    public BPIDKaonPlusHistograms() {
	//constructor
    }

    public void createKaonPHistograms(int i){

	h_kp_p.add( new H1F("h_kp_p_cutlvl"+Integer.toString(i),"h_kp_p_cutlvl"+Integer.toString(i), 100, min_p, max_p ) );
	h_kp_theta.add( new H1F("h_kp_theta_cutlvl"+Integer.toString(i),"h_kp_theta_cutlvl"+Integer.toString(i), 100, min_theta, max_theta) );
	h_kp_phi.add( new H1F("h_kp_phi_cutlvl"+Integer.toString(i),"h_kp_phi_cutlvl"+Integer.toString(i), 100, min_phi, max_phi ) );
	h_kp_vz.add( new H1F("h_kp_vz_cutlvl"+Integer.toString(i),"h_kp_vz_cutlvl"+Integer.toString(i), 100, min_vz, max_vz ) );
	h_kp_timing.add( new H1F("h_kp_timing_cutlvl"+Integer.toString(i),"h_kp_timing_cutlvl"+Integer.toString(i), 100, min_timing, max_timing ) );
	h_kp_rpath.add( new H1F("h_kp_rpath_cutlvl"+Integer.toString(i),"h_kp_rpath_cutlvl"+Integer.toString(i), 300, min_rpath, max_rpath ) );
	h_kp_beta_time.add( new H1F("h_kp_betatime_cutlvl"+Integer.toString(i),"h_kp_betatime_cutlvl"+Integer.toString(i), 100, min_b, max_b ) );
	h_kp_beta_mntm.add( new H1F("h_kp_betamntm_cutlvl"+Integer.toString(i),"h_kp_betamntm_cutlvl"+Integer.toString(i), 100, min_b, max_b ) );
	h2_kp_tof.add( new H2F("h2_kp_tof_cutlvl"+Integer.toString(i),"h2_kp_tof_cutlvl"+Integer.toString(i), 200, min_p, max_p, 200, min_tof, max_tof ) );
	
	h2_kp_deltimep.add( new H2F("h2_kp_deltimep_cutlvl"+Integer.toString(i),"h2_kp_deltimep_cutlvl"+Integer.toString(i),200, min_p, max_p, 200, min_delt, max_delt ));
	h2_kp_betap.add( new H2F("h2_kp_betap_cutlvl"+Integer.toString(i),"h2_kp_betap_cutlvl"+Integer.toString(i),200, min_p, max_p, 200, min_b, max_b ));
	h2_kp_deltabeta.add( new H2F("h2_kp_deltabeta_cutlvl"+Integer.toString(i),"h2_kp_deltabeta_cutlvl"+Integer.toString(i),200, min_p, max_p, 200, min_delb, max_delb ));

	h2_kp_thetap.add( new H2F("h2_kp_thetap_cutlvl"+Integer.toString(i),"h2_kp_thetap_cutlvl"+Integer.toString(i),100, min_p, max_p, 100, min_theta, max_theta));

    }

    public void createKaonPSectorHistograms( int sector, int max_cuts ){
	h_kp_sect_p.add( new Vector<H1F>() );
 	h_kp_sect_theta.add( new Vector<H1F>() );
	h_kp_sect_phi.add( new Vector<H1F>() );
	h2_kp_sect_betap.add( new Vector<H2F>() );
	h2_kp_sect_deltabeta.add( new Vector<H2F>() );
	h2_kp_sect_deltimep.add( new Vector<H2F>() );
	
	for( int i = 0; i <= max_cuts; i++ ){
	    (h_kp_sect_p.get(sector)).add( new H1F("h_kp_" + Integer.toString(sector) + "_p_cutlvl"+Integer.toString(i),"h_kp_" + Integer.toString(sector)  + "_p_cutlvl"+Integer.toString(i), 300, min_p, max_p ) );  
	    (h2_kp_sect_betap.get(sector)).add( new H2F("h2_kp_" + Integer.toString(sector) + "_betap_cutlvl"+Integer.toString(i),"h2_kp_" + Integer.toString(sector)  + "_betap_cutlvl"+Integer.toString(i), 200, min_p, max_p, 200, min_b, max_b ) );  
	    (h2_kp_sect_deltabeta.get(sector)).add( new H2F("h2_kp_" + Integer.toString(sector) + "_deltabeta_cutlvl"+Integer.toString(i),"h2_kp_" + Integer.toString(sector)  + "_deltabeta_cutlvl"+Integer.toString(i), 200, min_p, max_p, 200, min_delb, max_delb ) );  
	    (h2_kp_sect_deltimep.get(sector)).add( new H2F("h2_kp_" + Integer.toString(sector) + "_deltimep_cutlvl"+Integer.toString(i),"h2_kp_" + Integer.toString(sector)  + "_deltimep_cutlvl"+Integer.toString(i), 200, min_p, max_p, 200, min_delt, max_delt ) );  
	}
    }


    public void kaonpHistoToHipo(){

	dir.mkdir("/kp/cutlvls/h_kp_p/");
	dir.cd("/kp/cutlvls/h_kp_p/");
	for( H1F h_temp : h_kp_p ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h_kp_thetap/");
	dir.cd("/kp/cutlvls/h_kp_thetap/");
	for( H2F h_temp : h2_kp_thetap ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("TOF [ns]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h2_kp_tof/");
	dir.cd("/kp/cutlvls/h2_kp_tof/");
	for( H2F h_temp : h2_kp_tof ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("TOF [ns]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h_kp_rpath/");
	dir.cd("/kp/cutlvls/h_kp_rpath/");
	for( H1F h_temp : h_kp_rpath ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h_kp_beta_time/");
	dir.cd("/kp/cutlvls/h_kp_beta_time/");
	for( H1F h_temp : h_kp_beta_time ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("#beta_{clas12}");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h_kp_beta_mntm/");
	dir.cd("/kp/cutlvls/h_kp_beta_mntm/");
	for( H1F h_temp : h_kp_beta_mntm ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h2_kp_deltabeta/");
	dir.cd("/kp/cutlvls/h2_kp_deltabeta/");
	for( H2F h_temp : h2_kp_deltabeta ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#delta #beta");	    
	    c_temp.draw(h_temp,"colz");
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}
    	
	dir.mkdir("/kp/cutlvls/h2_kp_deltimep/");
	dir.cd("/kp/cutlvls/h2_kp_deltimep/");
	for( H2F h_temp : h2_kp_deltimep ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#delta time of flight");	    
	    c_temp.draw(h_temp,"colz");
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h2_kp_betap/");
	dir.cd("/kp/cutlvls/h2_kp_betap/");
	for( H2F h_temp : h2_kp_betap ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#beta");	    
	    c_temp.draw(h_temp,"colz");
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+h_temp.getTitle()+".png");
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/kp/cutlvls/h2_kp_sect_betap/");
	dir.cd("/kp/cutlvls/h2_kp_sect_betap/");
	for( int i = 0; i < h2_kp_sect_betap.size(); i++ ){
 	   Vector<H2F> v_temp = h2_kp_sect_betap.get(i);       
	    for( int j = 0; j < v_temp.size(); j++){		
		dir.addDataSet(v_temp.get(j));
		EmbeddedCanvas c_temp = new EmbeddedCanvas();
		c_temp.setSize(800,800);
		v_temp.get(j).setTitleX("p [GeV]");
		v_temp.get(j).setTitleY("#beta");	    
		c_temp.draw(v_temp.get(j),"colz");
		c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+v_temp.get(j).getTitle()+".png");		
	    }
	}

	dir.mkdir("/kp/cutlvls/h2_kp_sect_betap/");
	dir.cd("/kp/cutlvls/h2_kp_sect_betap/");
	for( int i = 0; i < h2_kp_sect_deltabeta.size(); i++ ){
 	   Vector<H2F> v_temp = h2_kp_sect_deltabeta.get(i);       
	    for( int j = 0; j < v_temp.size(); j++){		
		dir.addDataSet(v_temp.get(j));
		EmbeddedCanvas c_temp = new EmbeddedCanvas();
		c_temp.setSize(800,800);
		v_temp.get(j).setTitleX("p [GeV]");
		v_temp.get(j).setTitleY("#Delta #beta");	    
		c_temp.draw(v_temp.get(j),"colz");
		c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/pid_clary/"+v_temp.get(j).getTitle()+".png");		
	    }
	}
    }

    public void viewHipoOut(){

	TBrowser browser = new TBrowser(dir);

    }

}
