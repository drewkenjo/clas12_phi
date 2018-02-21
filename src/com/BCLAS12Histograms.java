package src.com;

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


//import org.jlab.groot.func.*;
//import org.jlab.groot.histogram.*;

import java.util.*;
import java.io.*;


public class BCLAS12Histograms {

    int run_number = -1;
    String s_run_number = " ";
    public BCLAS12Histograms( int temp_run ){
	run_number = temp_run;
	s_run_number = Integer.toString(run_number);
    }

    TDirectory dir = new TDirectory();

    double min_p = 0.0; double max_p = 10.5;
    double min_theta = 0.0; double max_theta = 60.5;
    double min_phi = -180.0; double max_phi = 180.0;
    double min_vz = -10.0; double max_vz = 10.0;
    double min_timing = 75; double max_timing = 175;
    double min_nphe = 0; double max_nphe = 100;
    double min_ecei = 0.0; double max_ecei = 1.0;
    double min_eceo = 0.0; double max_eceo = 1.0;
    double min_ectot = 0.01; double max_ectot = 2.5;
    double min_pcal1 = 0.0; double max_pcal1 = 5.0;
    double min_pcal2 = 0.0; double max_pcal2 = 5.0;
    double min_pcaltot = 0.0; double max_pcaltot = 5.0;
    double min_beta =0.0; double max_beta = 1.2;
    double min_w = 0.90; double max_w = 2.0;

    H1F h_el_p;
    H1F h_el_theta;
    H1F h_el_phi;
    H1F h_el_w;
    H2F h2_el_thetap;
    H2F h2_el_phip;
    H2F h2_el_ectotp;
    H2F h2_el_etotnphe;
    H2F h2_el_betap;
    H2F h2_el_eieo;
    H2F h2_el_pcalecal;
    H2F h2_el_pcalp;
    H2F h2_el_vzp;

    Vector<H2F> h2_el_sect_thetap = new Vector<H2F>();
    Vector<H2F> h2_el_sect_phip = new Vector<H2F>();
    Vector<H2F> h2_el_sect_ectotp = new Vector<H2F>();
    Vector<H2F> h2_el_sect_etotnphe = new Vector<H2F>();
    Vector<H2F> h2_el_sect_betap = new Vector<H2F>();
    Vector<H2F> h2_el_sect_eieo = new Vector<H2F>();
    Vector<H2F> h2_el_sect_pcalecal = new Vector<H2F>();
    Vector<H2F> h2_el_sect_pcalp = new Vector<H2F>();
    Vector<H2F> h2_el_sect_vzp = new Vector<H2F>();
    
    Vector<H1F> h_el_sect_w = new Vector<H1F>();

    public void createCLAS12ElectronHistograms( int i ){

	h_el_p = new H1F("h_"+s_run_number+"_el_p","h_"+s_run_number+"_el_p", 100, min_p, max_p );
	h_el_w = new H1F("h_"+s_run_number+"_el_w","h_"+s_run_number+"_el_w", 100, min_w, max_w );
	h2_el_thetap = new H2F("h2_"+s_run_number+"_el_thetap","h2_"+s_run_number+"_el_thetap", 100, min_p, max_p, 100, min_theta, max_theta);
	h2_el_phip = new H2F("h2_"+s_run_number+"_el_phip","h2_"+s_run_number+"_el_phip", 100, min_p, max_p, 100, min_phi, max_phi);
	h2_el_ectotp = new H2F("h2_"+s_run_number+"_el_ectotp","h2_"+s_run_number+"_el_ectotp", 100, min_p, max_p, 100, min_ectot, 0.30 );
	h2_el_etotnphe = new H2F("h2_"+s_run_number+"_el_etotnphe","h2_"+s_run_number+"_el_etotnphe", 100, min_nphe, max_nphe, 100, min_ectot, max_ectot );
	h2_el_betap = new H2F("h2_"+s_run_number+"_el_betap","h2_"+s_run_number+"_el_betap",100,min_p, max_p, 100, min_beta, max_beta);

	h2_el_eieo = new H2F("h2_"+s_run_number+"_el_eieo","h2_"+s_run_number+"_el_eieo",100, min_ecei, max_ecei, 100, min_eceo, max_eceo );
	h2_el_pcalecal = new H2F("h2_"+s_run_number+"_el_pcalecal","h2_"+s_run_number+"_el_pcalecal",100, min_ectot, 1.5, 100, min_pcal1, 1.5 );
	h2_el_pcalp = new H2F("h2_"+s_run_number+"_el_pcalp","h2_"+s_run_number+"_el_pcalp",100, min_p, max_p, 100,0.0, 0.35);// min_pcal1, max_pcal1 );
	h2_el_vzp = new H2F("h2_"+s_run_number+"_el_vzp","h2_"+s_run_number+"_el_vzp",200, min_vz, max_vz, 200, min_p, max_p); 

    }


    public void createCLAS12ElectronSectorHistograms( int sector ){

	h_el_sect_w.add( new H1F("h_"+s_run_number+"_el_"+Integer.toString(sector)+"_w", "h_el_"+Integer.toString(sector)+"_w", 200, min_w, max_w ));

	h2_el_sect_thetap.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_thetap","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_thetap", 100, min_p, max_p, 100, min_theta, max_theta));
	h2_el_sect_phip.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_phip","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_phip", 100, min_p, max_p, 100, min_phi, max_phi));
	h2_el_sect_ectotp.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_ectotp","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_ectotp", 100, min_p, max_p, 100, 0.01, 0.35));
	h2_el_sect_etotnphe.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_etotnphe","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_etotnphe", 100, min_p, max_p, 100, 0.1, max_ectot));
	h2_el_sect_betap.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_betap","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_betap", 100, min_p, max_p, 100, min_beta, max_beta));
	h2_el_sect_eieo.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_eieo","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_eieo", 100, 0.01, max_ecei, 100, 0.01, max_eceo));
	h2_el_sect_pcalecal.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_pcalecal","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_pcalecal", 100, 0.0, 1.5, 100, 0.0, 1.5));
	h2_el_sect_pcalp.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_pcalp","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_pcalp", 100, min_p, max_p, 100, 0.01, 0.35));
	h2_el_sect_vzp.add( new H2F("h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_vzp","h2_"+s_run_number+"_el_"+Integer.toString(sector)+"_vzp", 200, min_vz, max_vz, 200, min_p, max_p));
		
    }

    public void clas12ElectronHistoToHipo(){

 	dir.mkdir("/clas12pid/");
	dir.cd("/clas12pid/");
	dir.addDataSet(h_el_p);      
	dir.addDataSet(h_el_w);
	dir.addDataSet(h2_el_thetap);      
	dir.addDataSet(h2_el_phip);      
	dir.addDataSet(h2_el_ectotp);
	dir.addDataSet(h2_el_etotnphe);
	dir.addDataSet(h2_el_betap);
	dir.addDataSet(h2_el_eieo);
	dir.addDataSet(h2_el_pcalecal);
	dir.addDataSet(h2_el_pcalp);
	dir.addDataSet(h2_el_vzp);
	

	dir.mkdir("/clas12pid_sector_w/");
	dir.cd("/clas12pid_sector_w/");
	for( H1F h_temp : h_el_sect_w ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_thetap/");
	dir.cd("/clas12pid_sector_thetap/");
	for( H2F h_temp : h2_el_sect_thetap ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_phip/");
	dir.cd("/clas12pid_sector_phip/");
 	for( H2F h_temp : h2_el_sect_phip ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_ectotp/");
	dir.cd("/clas12pid_sector_ectotp/");
	for( H2F h_temp : h2_el_sect_ectotp ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_etotnphe/");
	dir.cd("/clas12pid_sector_etotnphe/");
	for( H2F h_temp : h2_el_sect_etotnphe ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_betap/");
	dir.cd("/clas12pid_sector_betap/");
	for(H2F h_temp : h2_el_sect_betap ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_eieo/");
	dir.cd("/clas12pid_sector_eieo/");
	for(H2F h_temp : h2_el_sect_eieo ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_pcalecal/");
	dir.cd("/clas12pid_sector_pcalecal/");
	for(H2F h_temp : h2_el_sect_pcalecal ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_pcalp/");
	dir.cd("/clas12pid_sector_pcalp/");
	for(H2F h_temp : h2_el_sect_pcalp ){
	    dir.addDataSet(h_temp);
	}

	dir.mkdir("/clas12pid_sector_vzp/");
	dir.cd("/clas12pid_sector_vzp/");
	for(H2F h_temp : h2_el_sect_vzp ){
	    dir.addDataSet(h_temp);
	}
       
	dir.writeFile("clas12_pid.hipo");


	

    }


    public void printHistograms(){


	for( H1F h_temp : h_el_sect_w ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("W [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}       

	for(H2F h_temp : h2_el_sect_thetap ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#theta [deg]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for(H2F h_temp : h2_el_sect_phip ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#phi [deg]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for( H2F h_temp : h2_el_sect_ectotp ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("Total Energy / p");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for( H2F h_temp : h2_el_sect_etotnphe  ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("Nphe");
	    h_temp.setTitleY("Total Energy [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for( H2F h_temp : h2_el_sect_betap ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("#beta");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for( H2F h_temp : h2_el_sect_eieo ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("EC inner [GeV]");
	    h_temp.setTitleY("EC outer [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}
	for( H2F h_temp : h2_el_sect_pcalecal ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("ECAL [GeV]");
	    h_temp.setTitleY("PCAL [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}
	for( H2F h_temp : h2_el_sect_pcalp ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("p [GeV]");
	    h_temp.setTitleY("PCAL [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}

	for( H2F h_temp : h2_el_sect_vzp ){
	    EmbeddedCanvas c_temp = new EmbeddedCanvas();
	    c_temp.setSize(800,800);
	    h_temp.setTitleX("vz [cm]");
	    h_temp.setTitleY("p [GeV]");
	    c_temp.draw(h_temp);
	    c_temp.save("/lustre/expphy/work/hallb/clas12/bclary/pics/"+h_temp.getTitle()+".png");
	}


    }


    public void viewHipoOut(){
	//dir.readFile("clas12_pid.hipo");
	//dir.ls();
	TBrowser b = new TBrowser(dir);

    }
    

}

