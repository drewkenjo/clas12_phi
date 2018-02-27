package org.jlab.clas.analysis.clary;

import java.io.*;
import java.util.*;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.*;

import org.jlab.groot.group.*;
import org.jlab.groot.ui.TBrowser;
import org.jlab.groot.tree.*;
import org.jlab.groot.data.TDirectory;
import org.jlab.io.hipo.HipoDataSource;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class BCLAS12ProtonHistograms {

    int run_number = -1;
    String s_run_number = " ";
    public BCLAS12ProtonHistograms( int temp_run ){
	run_number = temp_run;
	s_run_number = Integer.toString(run_number);
    }

    TDirectory dir = new TDirectory();

    double min_p = 0.0; double max_p = 10.5;
    double min_theta = 0.0; double max_theta = 60.5;
    double min_phi = -180.0; double max_phi = 180.0;
    double min_vz = -5.5; double max_vz = 5.5;
    double min_b = 0.0; double max_b = 1.5;

    H2F h2_pr_betap;
    Vector<H2F> h2_pr_sect_betap = new Vector< H2F >();
    Vector<H1F> h_pr_sect_vz = new Vector<H1F >();

    HashMap<Integer, Vector<H2F> > m_pr_sect_panel_deltp = new HashMap<Integer, Vector< H2F>  >(); 

    public void createCLAS12ProtonHistograms( int i ){

	h2_pr_betap = new H2F("h2_"+s_run_number+"_clas12pr_betap","h2_clas12pr_betap",100, min_p, max_p, 100, 0.01, 1.5);

    }

    public void createCLAS12ProtonSectorHistograms( int sector ){

	    h2_pr_sect_betap.add( new H2F("h2_"+s_run_number+"_clas12pr_"+Integer.toString(sector)+"_betap","h2_"+s_run_number+"_clas12pr_"+Integer.toString(sector)+"_betap", 200, min_p, max_p, 200, min_b, max_b));	    
	    h_pr_sect_vz.add( new H1F("h_"+s_run_number+"_clas12pr_"+Integer.toString(sector)+"_vz","h_"+s_run_number+"_clas12pr_"+Integer.toString(sector)+"_vz", 200, min_vz, max_vz));	    

    }

    public void createProtonFTOFHistograms( int sector, int panel ){

	for( int s = 0; s < sector; s++ ){
	    Vector< H2F  > p_temp = new Vector<H2F>();
	    for( int p = 0; p < panel; p++ ){
		p_temp.add( new H2F("h_"+s_run_number+"_clas12pr_deltimep_"+Integer.toString(s)+"_"+Integer.toString(p),"h_"+s_run_number+"_clas12pr_deltimep_"+Integer.toString(s)+"_"+Integer.toString(p), 200, 0.0, 10.5, 200, -10.0, 10.0) );
	    }
	    m_pr_sect_panel_deltp.put(s, p_temp); 
	}

    }

    public void clas12ProtonHistoToHipo(){

 	dir.mkdir("/clas12Protonpid/betap/");
	dir.cd("/clas12Protonpid/betap/");
	dir.addDataSet(h2_pr_betap);      
 
 	dir.mkdir("/clas12Protonpid/deltimep/");
	dir.cd("/clas12Protonpid/deltimep/");
	for( int s = 0; s < 6; s++){
	    EmbeddedCanvas c_sp_deltp = new EmbeddedCanvas();
	    c_sp_deltp.setSize(1600,800);
 	    c_sp_deltp.divide(9,6);
	    for( int p = 0; p < 48; p++ ){
		c_sp_deltp.cd(p);
		m_pr_sect_panel_deltp.get(s).get(p).setTitleX("S" + Integer.toString(s) + " P" + Integer.toString(p));
		c_sp_deltp.draw(m_pr_sect_panel_deltp.get(s).get(p),"colz");
		dir.addDataSet(m_pr_sect_panel_deltp.get(s).get(p));
	    }
	    c_sp_deltp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/h_"+s_run_number+"_clas12pr_deltimep_"+Integer.toString(s)+"_panel.png");

	}

 	dir.mkdir("/clas12Protonpid/h_pr_sect_betap/");
	dir.cd("/clas12Protonpid/h_pr_sect_betap/");	
	EmbeddedCanvas c_betap = new EmbeddedCanvas();
	c_betap.setSize(800,800);
	c_betap.divide(3,2);
	for( int s = 0; s < 6; s++ ){	   
	    c_betap.cd(s);
	    h2_pr_sect_betap.get(s).setTitleX(Integer.toString(s));
	    c_betap.draw(h2_pr_sect_betap.get(s),"colz");
	    dir.addDataSet(h2_pr_sect_betap.get(s));
	}
	c_betap.save("/lustre/expphy/work/hallb/clas12/bclary/pics/h_"+s_run_number+"_clas12pr_betap_allsect.png"); 

 	dir.mkdir("/clas12Protonpid/h_pr_sect_vz/");
	dir.cd("/clas12Protonpid/h_pr_sect_vz/");	
	EmbeddedCanvas c_vz = new EmbeddedCanvas();
	c_vz.setSize(800,800);
	c_vz.divide(3,2);
	for( int s = 0; s < 6; s++ ){	   
	    c_vz.cd(0);
	    h_pr_sect_vz.get(s).setTitleX(Integer.toString(s));
	    c_vz.draw(h_pr_sect_vz.get(s));
	    dir.addDataSet(h_pr_sect_vz.get(s));
	}
	c_vz.save("/lustre/expphy/work/hallb/clas12/bclary/pics/h_"+s_run_number+"_clas12pr_vz_allsect.png"); 


	//dir.addDataSet(h_el_w);
	//dir.addDataSet(h2_el_thetap);      

	/*dir.mkdir("/clas12pid_sector_w/");
	dir.cd("/clas12pid_sector_w/");
	for( H1F h_temp : h_el_sect_w ){
	dir.addDataSet(h_temp);
	}*/

	dir.writeFile("clas12_protonpid.hipo");

    }


    public void viewHipoOut(){
	//dir.readFile("clas12_pid.hipo");
	//dir.ls();
    	TBrowser b = new TBrowser(dir);
    }

}
