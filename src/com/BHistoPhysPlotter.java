package src.com;

import java.io.*;
import javax.swing.JFrame;
import java.util.Collections; 

import src.com.BEvent;
import src.com.PhysicsEvent;

import org.jlab.analysis.plotting.H1FCollection2D;
import org.jlab.analysis.plotting.H1FCollection3D;
import org.jlab.analysis.plotting.TCanvasP;
import org.jlab.analysis.plotting.TCanvasPTabbed;
import org.jlab.groot.graphics.EmbeddedCanvas;

import org.jlab.analysis.math.ClasMath;
import org.jlab.clas.physics.Vector3;
import org.jlab.clas.physics.LorentzVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;


public class BHistoPhysPlotter {

    JFrame frame1 = new JFrame();
    JFrame frame2 = new JFrame();
    JFrame frame3 = new JFrame();
    JFrame frame4 = new JFrame();

    EmbeddedCanvas c_phy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c2_phy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c3_phy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c4_phy_mm2 = new EmbeddedCanvas();

    JFrame mc_frame1 = new JFrame();
    JFrame mc_frame2 = new JFrame();
    JFrame mc_frame3 = new JFrame();
    JFrame mc_frame4 = new JFrame();

    EmbeddedCanvas c_mcphy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c2_mcphy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c3_mcphy_q2xt = new EmbeddedCanvas();
    EmbeddedCanvas c4_mcphy_mm2 = new EmbeddedCanvas();

    H1F h_phy_q2; 
    H1F h_phy_xb;
    H1F h_phy_t; 
    H1F h_phy_w;
    H1F h_phy_cm_phi;
    H1F h_phy_cm_theta;
    H1F h_phy_mm2epkp;
    H1F h_phy_mm2ekp; 
    H1F h_phy_mm2epkm;
    H1F h_phy_mm2ekpkm;
    H1F h_phy_mm2ep;
    H2F h_phy_q2x;
    H2F h_phy_q2t;

    H1F h_mcphy_q2; 
    H1F h_mcphy_xb;
    H1F h_mcphy_t; 
    H1F h_mcphy_w;
    H1F h_mcphy_cm_phi;
    H1F h_mcphy_cm_theta;
    H1F h_mcphy_mm2epkp;
    H1F h_mcphy_mm2ekp; 
    H1F h_mcphy_mm2epkm;
    H1F h_mcphy_mm2ekpkm;
    H1F h_mcphy_mm2ep;
    H2F h_mcphy_q2x;
    H2F h_mcphy_q2t;

    public void CreateHistograms(){
	frame1.setSize(1600,800);
	frame2.setSize(1600,800);
	frame3.setSize(1600,800);
	frame4.setSize(1600,800);

	h_phy_q2 = new H1F("h_phy_q2","h_phy_q2",80,0.05, 9.2 );
 	h_phy_xb = new H1F("h_phy_xb","h_phy_xb",80,0.0, 1.2 );
	h_phy_t = new H1F("h_phy_t","h_phy_t",80,0.0, 3.5 );
	h_phy_w = new H1F("h_phy_w","h_phy_w",80,5.0, 50.0 );

	h_phy_cm_phi = new H1F("h_phy_cm_phi","h_phy_cm_phi",100,-180.0, 180.0);
	h_phy_cm_theta = new H1F("h_phy_cm_theta","h_phy_cm_theta",50,-1.0, 1.0);

	h_phy_mm2epkp = new H1F("h_phy_mm2epkp","h_phy_mm2epkp",100,0.2, 0.8 );
	h_phy_mm2ekp = new H1F("h_phy_mm2ekp","h_phy_mm2ekp",100,0.2, 0.8 );
        h_phy_mm2epkm = new H1F("h_phy_mm2epkm","h_phy_mm2epkm",100,0.2, 0.8);
	h_phy_mm2ekpkm= new H1F("h_phy_mm2ekpkm","h_phy_mm2ekpkm",100,0.6, 1.1);
	h_phy_mm2ep = new H1F("h_phy_mm2ep","h_phy_mm2ep",100,-0.5, 1.3);
	
	h_phy_q2x = new H2F("h_phy_q2x","h_phy_q2x",80,0.05, 0.80, 80, 0.0, 9.0);
	h_phy_q2t = new H2F("h_phy_q2t","h_phy_q2t",80,0, 3.5, 80, 0.0, 9.0);
       	
    }

    public void CreateMCHistograms(){
	mc_frame1.setSize(1600,800);
	mc_frame2.setSize(1600,800);
	mc_frame3.setSize(1600,800);
	mc_frame4.setSize(1600,800);

	h_mcphy_q2 = new H1F("h_mcphy_q2","h_mcphy_q2",80,0.05, 9.2 );
 	h_mcphy_xb = new H1F("h_mcphy_xb","h_mcphy_xb",80,0.0, 1.2 );
	h_mcphy_t = new H1F("h_mcphy_t","h_mcphy_t",80,0.0, 3.5 );
	h_mcphy_w = new H1F("h_mcphy_w","h_mcphy_w",80,5.0, 50.0 );

	h_mcphy_cm_phi = new H1F("h_mcphy_cm_phi","h_mcphy_cm_phi",100,-180.0, 180.0);
	h_mcphy_cm_theta = new H1F("h_mcphy_cm_theta","h_mcphy_cm_theta",50,-1.0, 1.0);

	h_mcphy_mm2epkp = new H1F("h_mcphy_mm2epkp","h_mcphy_mm2epkp",100, 0.2, 0.8 );
	h_mcphy_mm2ekp = new H1F("h_mcphy_mm2ekp","h_mcphy_mm2ekp",100, 0.2, 0.8 );
        h_mcphy_mm2epkm = new H1F("h_mcphy_mm2epkm","h_mcphy_mm2epkm",100, 0.2, 0.8);
	h_mcphy_mm2ekpkm= new H1F("h_mcphy_mm2ekpkm","h_mcphy_mm2ekpkm",100,0.6, 1.1);
	h_mcphy_mm2ep = new H1F("h_mcphy_mm2ep","h_mcphy_mm2ep",100,-0.5, 1.3);

	h_mcphy_q2x = new H2F("h_mcphy_q2x","h_mcphy_q2x",80,0.05, 0.80, 80, 0.0, 9.0);
	h_mcphy_q2t = new H2F("h_mcphy_q2t","h_mcphy_q2t",80,0.0, 3.5, 80, 0.0, 9.0);



    }

    public void FillPhysicsHistos( PhysicsEvent physicsevent ){
	
	h_phy_q2.fill(-physicsevent.q2);
	h_phy_xb.fill(physicsevent.xB);
	h_phy_t.fill(-physicsevent.t);
	h_phy_w.fill(physicsevent.w2);
	h_phy_cm_phi.fill(physicsevent.cm_phi);
	h_phy_cm_theta.fill(physicsevent.cm_theta);

	h_phy_q2x.fill(physicsevent.xB, -physicsevent.q2);
	h_phy_q2t.fill(-physicsevent.t, -physicsevent.q2);

	int missing_pr = 5;
	int missing_kp = 3;
	int missing_km = 2;
	if( physicsevent.topology == missing_pr ){
	    h_phy_mm2ekpkm.fill(physicsevent.lv_pr.mass());
	}
	if( physicsevent.topology == missing_kp ){
	    h_phy_mm2epkm.fill(physicsevent.lv_kp.mass());	   
	}
	if( physicsevent.topology == missing_km ){
	    h_phy_mm2epkp.fill(physicsevent.lv_km.mass());
	}

    }

    public void FillMCPhysicsHistograms( PhysicsEvent physicsevent ){
	
	h_mcphy_q2.fill(-physicsevent.mc_q2);
	h_mcphy_xb.fill(physicsevent.mc_xB);
	h_mcphy_t.fill(-physicsevent.mc_t);
	h_mcphy_w.fill(physicsevent.mc_w2);
	h_mcphy_cm_phi.fill(physicsevent.mc_cm_phi);
	h_mcphy_cm_theta.fill(physicsevent.mc_cm_theta);

	h_mcphy_q2x.fill(physicsevent.mc_xB, -physicsevent.mc_q2);
	h_mcphy_q2t.fill(-physicsevent.mc_t, -physicsevent.mc_q2);

	h_mcphy_mm2epkp.fill(physicsevent.mc_missing_km.mass());
	h_mcphy_mm2epkm.fill(physicsevent.mc_missing_kp.mass());
	h_mcphy_mm2ekpkm.fill(physicsevent.mc_missing_pr.mass());
	
    }

    public void ViewHistograms( boolean toggleView ){

	c_phy_q2xt.setSize(1600,800);
	c_phy_q2xt.divide(2,2);
	c_phy_q2xt.cd(0);
	h_phy_q2.setTitleX("Q^2 [GeV^2 ]");
	h_phy_q2.setOptStat(1110);
	c_phy_q2xt.draw(h_phy_q2);
	c_phy_q2xt.cd(1);
	h_phy_xb.setTitleX("Xb");
	h_phy_xb.setOptStat(1110);
	c_phy_q2xt.draw(h_phy_xb);
	c_phy_q2xt.cd(2);
	h_phy_t.setTitleX("t [GeV]");
	h_phy_t.setOptStat(1110);
 	c_phy_q2xt.draw(h_phy_t);
	c_phy_q2xt.cd(3);
	h_phy_w.setTitleX("W [GeV]");
	h_phy_w.setOptStat(1110);
 	c_phy_q2xt.draw(h_phy_w);

	c2_phy_q2xt.setSize(1600,800);
	c2_phy_q2xt.divide(2,1);
	c2_phy_q2xt.cd(0);
	h_phy_q2x.setTitleX("Xb");
	h_phy_q2x.setTitleY("Q^2 [GeV^2 ]");
	c2_phy_q2xt.draw(h_phy_q2x,"colz");
	c2_phy_q2xt.cd(1);
	h_phy_q2t.setTitleX("t [GeV]");
	h_phy_q2t.setTitleY("Q^2 [GeV^2 ]");
	c2_phy_q2xt.draw(h_phy_q2t,"colz");

	c3_phy_q2xt.setSize(1600,800);
	c3_phy_q2xt.divide(2,1);
	c3_phy_q2xt.cd(0);
	h_phy_cm_phi.setTitleX("Center of Mass phi [Deg]");
	c3_phy_q2xt.draw(h_phy_cm_phi);
	c3_phy_q2xt.cd(1);
	h_phy_cm_theta.setTitleX("Center of Mass cos( #theta ) [Deg]");
	c3_phy_q2xt.draw(h_phy_cm_theta);

	c4_phy_mm2.setSize(1600,800);
 	c4_phy_mm2.divide(3,1);
	c4_phy_mm2.cd(0);
	h_phy_mm2epkp.setTitle("Missing Mass^2 [GeV^2] of K^-");
	h_phy_mm2epkp.setOptStat(1110);
	c4_phy_mm2.draw(h_phy_mm2epkp);
	c4_phy_mm2.cd(1);
	h_phy_mm2epkm.setTitle("Missing Mass^2 [GeV^2] of K^+");
	h_phy_mm2epkm.setOptStat(1110);
	c4_phy_mm2.draw(h_phy_mm2epkm);
	c4_phy_mm2.cd(2);
	h_phy_mm2ekpkm.setTitle("Missing Mass^2 [GeV^2] of Proton");
	h_phy_mm2ekpkm.setOptStat(1110);
	c4_phy_mm2.draw(h_phy_mm2ekpkm);

	frame1.add(c_phy_q2xt);
	frame2.add(c2_phy_q2xt);
	frame3.add(c3_phy_q2xt);
	frame4.add(c4_phy_mm2);
	
	frame1.setVisible(toggleView);
	frame2.setVisible(toggleView);
	frame3.setVisible(toggleView);
	frame4.setVisible(toggleView);
	
    }

    public void viewMCHistograms( boolean toggleView ){

	c_mcphy_q2xt.setSize(1600,800);
	c_mcphy_q2xt.divide(2,2);
	c_mcphy_q2xt.cd(0);
	h_mcphy_q2.setTitleX("Q^2 [GeV^2 ]");
	h_mcphy_q2.setOptStat(1110);
	c_mcphy_q2xt.draw(h_mcphy_q2);
	c_mcphy_q2xt.cd(1);
	h_mcphy_xb.setTitleX("Xb");
	h_mcphy_xb.setOptStat(1110);
	c_mcphy_q2xt.draw(h_mcphy_xb);
	c_mcphy_q2xt.cd(2);
	h_mcphy_t.setTitleX("t [GeV]");
	h_mcphy_t.setOptStat(1110);
 	c_mcphy_q2xt.draw(h_mcphy_t);
	c_mcphy_q2xt.cd(3);
	h_mcphy_w.setTitleX("W [GeV]");
	h_mcphy_w.setOptStat(1110);
 	c_mcphy_q2xt.draw(h_mcphy_w);

	c2_mcphy_q2xt.setSize(1600,800);
	c2_mcphy_q2xt.divide(2,1);
	c2_mcphy_q2xt.cd(0);
	h_mcphy_q2x.setTitleX("Xb");
	h_mcphy_q2x.setTitleY("Q^2 [GeV^2 ]");
	c2_mcphy_q2xt.draw(h_mcphy_q2x,"colz");
	c2_mcphy_q2xt.cd(1);
	h_mcphy_q2t.setTitleX("t [GeV]");
	h_mcphy_q2t.setTitleY("Q^2 [GeV^2 ]");
	c2_mcphy_q2xt.draw(h_mcphy_q2t,"colz");

	c3_mcphy_q2xt.setSize(1600,800);
	c3_mcphy_q2xt.divide(2,1);
	c3_mcphy_q2xt.cd(0);
	h_mcphy_cm_phi.setTitleX("Center of Mass phi [Deg]");
	c3_mcphy_q2xt.draw(h_mcphy_cm_phi);
	c3_mcphy_q2xt.cd(1);
	h_mcphy_cm_theta.setTitleX("Center of Mass cos( #theta ) [Deg]");
	c3_mcphy_q2xt.draw(h_mcphy_cm_theta);

	c4_mcphy_mm2.setSize(1600,800);
 	c4_mcphy_mm2.divide(3,1);
	c4_mcphy_mm2.cd(0);
	h_mcphy_mm2epkp.setTitle("Missing Mass^2 [GeV^2] of K^-");
	c4_mcphy_mm2.draw(h_mcphy_mm2epkp);
	c4_mcphy_mm2.cd(1);
	h_mcphy_mm2epkm.setTitle("Missing Mass^2 [GeV^2] of K^+");
	c4_mcphy_mm2.draw(h_mcphy_mm2epkm);
	c4_mcphy_mm2.cd(2);
	h_mcphy_mm2ekpkm.setTitle("Missing Mass^2 [GeV^2] of Proton");
	c4_mcphy_mm2.draw(h_mcphy_mm2ekpkm);

	mc_frame1.add(c_mcphy_q2xt);
	mc_frame2.add(c2_mcphy_q2xt);
	mc_frame3.add(c3_mcphy_q2xt);
	mc_frame4.add(c4_mcphy_mm2);
       
	mc_frame1.setVisible(toggleView);
	mc_frame2.setVisible(toggleView);
	mc_frame3.setVisible(toggleView);
	mc_frame4.setVisible(toggleView);
	
    }

    public void SavePhysHistograms(){

	c_phy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_phy_q2xt.png");
	c2_phy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h2_phy_q2xt.png");
	c3_phy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_phy_angles.png");
	c4_phy_mm2.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_phy_mm2.png");
       
    }

    public void SaveMCPhysHistograms(){

	c_mcphy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mcphy_q2xt.png");
	c2_mcphy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h2_mcphy_q2xt.png");
	c3_mcphy_q2xt.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mcphy_angles.png");
	c4_mcphy_mm2.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mcphy_mm2.png");
	
    }


}
