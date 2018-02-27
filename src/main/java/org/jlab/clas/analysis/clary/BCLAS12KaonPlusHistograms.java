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

public class BCLAS12KaonPlusHistograms {

    TDirectory dir = new TDirectory();

    double min_p = 0.0; double max_p = 10.5;
    double min_theta = 0.0; double max_theta = 60.5;
    double min_phi = -180.0; double max_phi = 180.0;
    double min_vz = -10.0; double max_vz = 10.0;
    double min_b = 0.01; double max_b = 1.5;
    double min_timing = 0.0; double max_timing = 700.0;
    double min_delt = -0.07; double max_delt = 0.07;
    double min_delb = -1.0; double max_delb = 1.0;
    double min_rpath = 300.0; double max_rpath = 750.0; 
    double min_tof = 15.0; double max_tof = 35.0;

    H1F h_pr_p;
    H1F h_pr_theta;
    H1F h_pr_phi;
    H1F h_pr_vz;
    H1F h_pr_timing;

    H2F h2_kp_thetap;
    H2F h2_kp_betap;
    H2F h2_kp_deltimep;
    H2F h2_kp_deltabeta;
    H2F h2_kp_tof;

    public void createCLAS12KaonPHistograms( int i ){

	h2_kp_thetap = new H2F("h2_kp_thetap","h2_kp_thetp",100, min_p, max_p, 100, min_theta, max_theta);
	h2_kp_betap = new H2F("h2_kp_betap","h2_kp_betap",100, min_p, max_p, 100, min_b, max_b);
	h2_kp_deltimep = new H2F("h2_kp_deltimep","h2_kp_deltimep",100, min_p, max_p, 100, min_delt, max_delt);
	h2_kp_deltabeta = new H2F("h2_kp_deltabeta","h2_kp_deltabeta",100, min_p, max_p, 100, min_delb, max_delb);
	h2_kp_tof = new H2F("h2_kp_tof","h2_kp_tof",100, min_p, max_p, 100, min_tof, max_tof);

    }

    public void createCLAS12KaonPSectorHistograms( int sector ){

    }

    public void clas12KaonPHistoToHipo(){


	h2_kp_betap.setTitleX("p [GeV]");
	h2_kp_betap.setTitleY("#beta");
	
	h2_kp_thetap.setTitleX("p [GeV]");
	h2_kp_thetap.setTitleY("#theta [deg]");

	h2_kp_deltimep.setTitleX("p [GeV]");
	h2_kp_deltimep.setTitleY("#delta tof [ns]");

	h2_kp_deltabeta.setTitleX("p [GeV]");
	h2_kp_deltabeta.setTitleY("#delta #beta");

	h2_kp_tof.setTitleX("p [GeV]");
	h2_kp_tof.setTitleY("tof [ns]");

  	dir.mkdir("/clas12kaonppid/");
	dir.cd("/clas12kaonppid/");
	dir.addDataSet(h2_kp_betap);
	dir.addDataSet(h2_kp_thetap);
	dir.addDataSet(h2_kp_deltimep);
	dir.addDataSet(h2_kp_deltabeta);
	dir.addDataSet(h2_kp_tof);
	       
    }

    public void viewHipoOut(){

	TBrowser b = new TBrowser(dir);

    }

}
