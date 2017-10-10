package src.com;

import java.io.*;

import src.com.BEvent;
import src.com.PhysicsBuilder;
import src.com.Calculator;

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
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.F1D;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

public class BHistoPlotter {

    EmbeddedCanvas c_mc_el = new EmbeddedCanvas();
    EmbeddedCanvas c_mc_pr = new EmbeddedCanvas();
    EmbeddedCanvas c_mc_kp = new EmbeddedCanvas();
    EmbeddedCanvas c_mc_km = new EmbeddedCanvas();

    EmbeddedCanvas c_rc_el = new EmbeddedCanvas();
    EmbeddedCanvas c_rc_pr = new EmbeddedCanvas();
    EmbeddedCanvas c_rc_kp = new EmbeddedCanvas();
    EmbeddedCanvas c_rc_km = new EmbeddedCanvas();

    EmbeddedCanvas c_rec_el = new EmbeddedCanvas();
    
    H1F h_mc_el_px; H1F h_mc_el_py; H1F h_mc_el_pz;
    H1F h_mc_pr_px; H1F h_mc_pr_py; H1F h_mc_pr_pz;
    H1F h_mc_kp_px; H1F h_mc_kp_py; H1F h_mc_kp_pz;
    H1F h_mc_km_px; H1F h_mc_km_py; H1F h_mc_km_pz;

    H1F h_rc_el_px; H1F h_rc_el_py; H1F h_rc_el_pz; H1F h_rc_el_e;
    H1F h_rc_pr_px; H1F h_rc_pr_py; H1F h_rc_pr_pz; H1F h_rc_pr_e;
    H1F h_rc_kp_px; H1F h_rc_kp_py; H1F h_rc_kp_pz; H1F h_rc_kp_e;
    H1F h_rc_km_px; H1F h_rc_km_py; H1F h_rc_km_pz; H1F h_rc_km_e;

    H2F h2_rc_el_ptheta;
    H2F h2_rc_pr_ptheta;
    H2F h2_rc_kp_ptheta;
    H2F h2_rc_km_ptheta;
    
    H2F h2_rc_el_vzphi;
    H2F h2_rc_pr_vzphi;
    H2F h2_rc_kp_vzphi;
    H2F h2_rc_km_vzphi;

    H2F h2_rc_el_thetaphi;
    H2F h2_rc_pr_thetaphi;
    H2F h2_rc_kp_thetaphi;
    H2F h2_rc_km_thetaphi;

    public void CreateHistograms(){

	h_mc_el_px = new H1F("h_mc_el_px","h_mc_el_px",100, -2.0, 2.0 );
	h_mc_el_py = new H1F("h_mc_el_px","h_mc_el_py",100, -2.0, 2.0 );
	h_mc_el_pz = new H1F("h_mc_el_px","h_mc_el_pz",100, 0.0, 12.0 );

	h_mc_pr_px = new H1F("h_mc_pr_px","h_mc_pr_px",100, -2.0, 2.0 );
	h_mc_pr_py = new H1F("h_mc_pr_py","h_mc_pr_py",100, -2.0, 2.0 );
	h_mc_pr_pz = new H1F("h_mc_pr_pz","h_mc_pr_pz",100, 0.0, 12.0 );

	h_mc_kp_px = new H1F("h_mc_kp_px","h_mc_kp_px",100, -2.0, 2.0 );
	h_mc_kp_py = new H1F("h_mc_kp_py","h_mc_kp_py",100, -2.0, 2.0 );
	h_mc_kp_pz = new H1F("h_mc_kp_pz","h_mc_kp_pz",100, 0.0, 12.0 );

	h_mc_km_px = new H1F("h_mc_kp_px","h_mc_km_px",100, -2.0, 2.0 );
	h_mc_km_py = new H1F("h_mc_kp_py","h_mc_km_py",100, -2.0, 2.0 );
	h_mc_km_pz = new H1F("h_mc_kp_pz","h_mc_km_pz",100, 0.0, 12.0 );

	h_rc_el_px = new H1F("h_rc_el_px","h_rc_el_px",100, -2.0, 2.0 );
	h_rc_el_py = new H1F("h_rc_el_py","h_rc_el_py",100, -2.0, 2.0 );
	h_rc_el_pz = new H1F("h_rc_el_pz","h_rc_el_pz",100, 0.0, 12.0 );
	h_rc_el_e = new H1F("h_rc_el_e","h_rc_el_e",100, 0.0, 12.0 );

	h_rc_pr_px = new H1F("h_rc_pr_px","h_rc_pr_px",100, -2.0, 2.0 );
	h_rc_pr_py = new H1F("h_rc_pr_py","h_rc_pr_py",100, -2.0, 2.0 );
	h_rc_pr_pz = new H1F("h_rc_pr_pz","h_rc_pr_pz",100, 0.0, 12.0 );
	h_rc_pr_e = new H1F("h_rc_pr_e","h_rc_pr_e",100, 0.0, 12.0 );

	h_rc_kp_px = new H1F("h_rc_kp_px","h_rc_kp_px",100, -2.0, 2.0 );
	h_rc_kp_py = new H1F("h_rc_kp_py","h_rc_kp_py",100, -2.0, 2.0 );
	h_rc_kp_pz = new H1F("h_rc_kp_pz","h_rc_kp_pz",100, 0.0, 12.0 );
	h_rc_kp_e = new H1F("h_rc_kp_e","h_rc_kp_e",100, 0.0, 12.0 );

	h_rc_km_px = new H1F("h_rc_kp_px","h_rc_km_px",100, -2.0, 2.0 );
	h_rc_km_py = new H1F("h_rc_kp_py","h_rc_km_py",100, -2.0, 2.0 );
	h_rc_km_pz = new H1F("h_rc_kp_pz","h_rc_km_pz",100, 0.0, 12.0 );
	h_rc_km_e = new H1F("h_rc_km_e","h_rc_km_e",100, 0.0, 12.0 );

	h2_rc_el_ptheta = new H2F("h2_rc_el_ptheta","h2_rc_el_ptheta",100,0,9.0,100,0,50.0);
	h2_rc_pr_ptheta = new H2F("h2_rc_pr_ptheta","h2_rc_pr_ptheta",100,0,3.0,100,10,70.0);
	h2_rc_kp_ptheta = new H2F("h2_rc_kp_ptheta","h2_rc_kp_ptheta",100,0,4.0,100,0,50.0);
	h2_rc_km_ptheta = new H2F("h2_rc_km_ptheta","h2_rc_km_ptheta",100,0,4.0,100,0,50.0);

	h2_rc_el_vzphi = new H2F("h2_rc_el_vzphi","h2_rc_el_vzphi",100,-180.0,180.0,100,-10.0,10.0);
	h2_rc_pr_vzphi = new H2F("h2_rc_pr_vzphi","h2_rc_pr_vzphi",100,-180.0,180.0,100,-10.0,10.0);
	h2_rc_kp_vzphi = new H2F("h2_rc_kp_vzphi","h2_rc_kp_zphi",100,-180.0,180.0,100,-10.0,10.0);
	h2_rc_km_vzphi = new H2F("h2_rc_km_vzphi","h2_rc_km_vzphi",100,-180.0,180.0,100,-10.0,10.0);

	h2_rc_el_thetaphi = new H2F("h2_rc_el_thetaphi","h2_rc_el_thetaphi",100,-180.0,180.0,100,0.0,40.0);
	h2_rc_pr_thetaphi = new H2F("h2_rc_pr_thetaphi","h2_rc_pr_thetaphi",100,-180.0,180.0,100,30.0,70.0);
	h2_rc_kp_thetaphi = new H2F("h2_rc_kp_thetaphi","h2_rc_kp_thetaphi",100,-180.0,180.0,100,0.0,40.0);
	h2_rc_km_thetaphi = new H2F("h2_rc_km_thetaphi","h2_rc_km_thetaphi",100,-180.0,180.0,100,0.0,40.0);

    }

    public void CreatePhysHistograms(){

	

    }

    public void FillMCHistograms(BEvent tempevent){
	
	h_mc_el_px.fill(tempevent.mc_el_px);
	h_mc_el_py.fill(tempevent.mc_el_py);
	h_mc_el_pz.fill(tempevent.mc_el_pz);

	h_mc_pr_px.fill(tempevent.mc_pr_px);
	h_mc_pr_py.fill(tempevent.mc_pr_py);
	h_mc_pr_pz.fill(tempevent.mc_pr_pz);

	h_mc_kp_px.fill(tempevent.mc_kp_px);
	h_mc_kp_py.fill(tempevent.mc_kp_py);
	h_mc_kp_pz.fill(tempevent.mc_kp_pz);

	h_mc_km_px.fill(tempevent.mc_km_px);
	h_mc_km_py.fill(tempevent.mc_km_py);
	h_mc_km_pz.fill(tempevent.mc_km_pz);
	
    }

    public void FillElectronHistograms(DataEvent tempdevent, int rec_i){
	DataBank recbank = tempdevent.getBank("REC::Particle");
	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_e = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz + PhysicalConstants.mass_electron*PhysicalConstants.mass_electron );
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double rec_theta = Calculator.lv_theta(tempdevent,rec_i);
	double rec_phi = Calculator.lv_phi(tempdevent,rec_i);

	h_rc_el_px.fill(rec_px);
	h_rc_el_py.fill(rec_py);
	h_rc_el_pz.fill(rec_pz);
	h_rc_el_e.fill(rec_e);
	h2_rc_el_ptheta.fill(rec_p, rec_theta);
	h2_rc_el_vzphi.fill(rec_phi, rec_vz);
	h2_rc_el_thetaphi.fill(rec_phi, rec_theta);

    }

    public void FillProtonHistograms(DataEvent tempdevent, int rec_i){

	DataBank recbank = tempdevent.getBank("REC::Particle");
 	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_e = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz + PhysicalConstants.mass_proton*PhysicalConstants.mass_proton);
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double rec_theta = Calculator.lv_theta(tempdevent,rec_i);
	double rec_phi = Calculator.lv_phi(tempdevent,rec_i);

	h_rc_pr_px.fill(rec_px);
	h_rc_pr_py.fill(rec_py);
	h_rc_pr_pz.fill(rec_pz);
	h_rc_pr_e.fill(rec_e);
	h2_rc_pr_ptheta.fill(rec_p, rec_theta);
	h2_rc_pr_vzphi.fill(rec_phi, rec_vz);
	h2_rc_pr_thetaphi.fill(rec_phi, rec_theta);

    }


    public void FillKaonPlusHistograms(DataEvent tempdevent, int rec_i){

	DataBank recbank = tempdevent.getBank("REC::Particle");
 	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_e = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz + PhysicalConstants.mass_kaon*PhysicalConstants.mass_kaon);
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double rec_theta = Calculator.lv_theta(tempdevent,rec_i);
	double rec_phi = Calculator.lv_phi(tempdevent,rec_i);

	h_rc_kp_px.fill(rec_px);
	h_rc_kp_py.fill(rec_py);
	h_rc_kp_pz.fill(rec_pz);
	h_rc_kp_e.fill(rec_e);
	h2_rc_kp_ptheta.fill(rec_p, rec_theta);
	h2_rc_kp_vzphi.fill(rec_phi, rec_vz);
	h2_rc_kp_thetaphi.fill(rec_phi, rec_theta);


    }

    public void FillKaonMinusHistograms(DataEvent tempdevent, int rec_i){

	DataBank recbank = tempdevent.getBank("REC::Particle");
 	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_e = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz + PhysicalConstants.mass_kaon*PhysicalConstants.mass_kaon);
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double rec_theta = Calculator.lv_theta(tempdevent,rec_i);
	double rec_phi = Calculator.lv_phi(tempdevent,rec_i);

	h_rc_km_px.fill(rec_px);
	h_rc_km_py.fill(rec_py);
	h_rc_km_pz.fill(rec_pz);
	h_rc_km_e.fill(rec_e);

	h2_rc_km_ptheta.fill(rec_p, rec_theta);
	h2_rc_km_vzphi.fill(rec_phi, rec_vz);
	h2_rc_km_thetaphi.fill(rec_phi, rec_theta);

    }
    
    
    public void FillPhysicsHistograms(PhysicsEvent physicsevent ){

	h_rc_el_px.fill(physicsevent.lv_el.px() );
	h_rc_el_py.fill(physicsevent.lv_el.py() );
	h_rc_el_pz.fill(physicsevent.lv_el.pz() );
	h_rc_el_e.fill(physicsevent.lv_el.e() );

	double el_theta = Math.acos(physicsevent.lv_el.pz()/ physicsevent.lv_el.p()) * 180.0/Math.PI;
	//System.out.println(">> " + physicsevent.lv_el.pz() + " " + physicsevent.lv_el.p() + " " + el_theta);
	h2_rc_el_ptheta.fill( physicsevent.lv_el.p(), Math.toDegrees(physicsevent.lv_el.theta()) );
	
	h_rc_pr_px.fill(physicsevent.lv_pr.px() );
	h_rc_pr_py.fill(physicsevent.lv_pr.py() );
	h_rc_pr_pz.fill(physicsevent.lv_pr.pz() );
	h_rc_pr_e.fill(physicsevent.lv_pr.e() );
	double pr_theta = Math.acos(physicsevent.lv_pr.pz()/ physicsevent.lv_pr.p()) * 180.0/Math.PI;
	h2_rc_pr_ptheta.fill( physicsevent.lv_pr.p(), Math.toDegrees(physicsevent.lv_pr.theta()) );


	h_rc_kp_px.fill(physicsevent.lv_kp.px() );
	h_rc_kp_py.fill(physicsevent.lv_kp.py() );
	h_rc_kp_pz.fill(physicsevent.lv_kp.pz() );
	h_rc_kp_e.fill(physicsevent.lv_kp.e() );
	double kp_theta = Math.acos(physicsevent.lv_kp.pz()/ physicsevent.lv_kp.p()) * 180.0/Math.PI;
	h2_rc_kp_ptheta.fill( physicsevent.lv_kp.p(), kp_theta);


	h_rc_km_px.fill(physicsevent.lv_km.px() );
	h_rc_km_py.fill(physicsevent.lv_km.py() );
	h_rc_km_pz.fill(physicsevent.lv_km.pz() );
	h_rc_km_e.fill(physicsevent.lv_km.e() );
	double km_theta = Math.acos(physicsevent.lv_km.pz()/ physicsevent.lv_km.p()) * 180.0/Math.PI;
	h2_rc_km_ptheta.fill( physicsevent.lv_km.p(), km_theta);

       
    }


    public void ViewHistograms(){

	c_mc_el.setSize(1600,400);
	c_mc_el.divide(3,1);

	c_mc_el.cd(0);
	h_mc_el_px.setTitleX("p_x [GeV]");
 	c_mc_el.draw(h_mc_el_px);
	h_rc_el_px.setLineColor(21);
	h_rc_el_px.setOptStat(1110);
 	c_mc_el.draw(h_rc_el_px,"same");

	c_mc_el.cd(1);
	h_mc_el_py.setTitleX("GEN p_y [GeV]");
 	c_mc_el.draw(h_mc_el_py);
	h_rc_el_py.setLineColor(21);
	h_rc_el_py.setOptStat(1110);
 	c_mc_el.draw(h_rc_el_py,"same");

	c_mc_el.cd(2);
	h_mc_el_pz.setTitleX("GEN p_z [GeV]");
 	c_mc_el.draw(h_mc_el_pz);
	h_rc_el_pz.setLineColor(21);
 	c_mc_el.draw(h_rc_el_pz,"same");

	c_mc_pr.setSize(1600,400);
	c_mc_pr.divide(3,1);
	c_mc_pr.cd(0);
	h_mc_pr_px.setTitleX("GEN p_x [GeV]");
 	c_mc_pr.draw(h_mc_pr_px);
	h_rc_pr_px.setLineColor(21);
 	c_mc_pr.draw(h_rc_pr_px,"same");

	c_mc_pr.cd(1);
	h_mc_pr_py.setTitleX("GEN p_y [GeV]");
 	c_mc_pr.draw(h_mc_pr_py);
	h_rc_pr_py.setLineColor(21);
 	c_mc_pr.draw(h_rc_pr_py,"same");

	c_mc_pr.cd(2);
	h_mc_pr_pz.setTitleX("GEN p_z [GeV]");
 	c_mc_pr.draw(h_mc_pr_pz);
	h_rc_pr_pz.setLineColor(21);
 	c_mc_pr.draw(h_rc_pr_pz,"same");

	c_mc_kp.setSize(1600,400);
	c_mc_kp.divide(3,1);
	c_mc_kp.cd(0);
	h_mc_kp_px.setTitleX(" GEN p_x [GeV]");
 	c_mc_kp.draw(h_mc_kp_px);
	h_rc_kp_px.setLineColor(21);
 	c_mc_kp.draw(h_rc_kp_px,"same");

	c_mc_kp.cd(1);
	h_mc_kp_py.setTitleX(" GEN p_y [GeV]");
 	c_mc_kp.draw(h_mc_kp_py);
	h_rc_kp_py.setLineColor(21);
 	c_mc_kp.draw(h_rc_kp_py,"same");

	c_mc_kp.cd(2);
	h_mc_kp_pz.setTitleX(" GEN p_z [GeV]");
 	c_mc_kp.draw(h_mc_kp_pz);
	h_rc_kp_pz.setLineColor(21);
 	c_mc_kp.draw(h_rc_kp_pz,"same");

	c_mc_km.setSize(1600,800);
	c_mc_km.divide(3,1);
	c_mc_km.cd(0);
	h_mc_km_px.setTitleX(" GEN p_x [GeV]");
 	c_mc_km.draw(h_mc_km_px);
	h_rc_km_px.setLineColor(21);
 	c_mc_km.draw(h_rc_km_px,"same");

	c_mc_km.cd(1);
	h_mc_km_py.setTitleX(" GEN p_y [GeV]");
 	c_mc_km.draw(h_mc_km_py);
	h_rc_km_py.setLineColor(21);
 	c_mc_km.draw(h_rc_km_py,"same");

	c_mc_km.cd(2);
	h_mc_km_pz.setTitleX(" GEN p_z [GeV]");
 	c_mc_km.draw(h_mc_km_pz);
	h_rc_km_pz.setLineColor(21);
 	c_mc_km.draw(h_rc_km_pz,"same");
			
	c_rc_el.setSize(2000,800);
	c_rc_el.divide(4,2);
 	c_rc_el.cd(0);
 	h_rc_el_px.setTitleX("REC p_x [GeV]");
	h_rc_el_px.setOptStat(1110);
 	c_rc_el.draw(h_rc_el_px);
 	c_rc_el.cd(1);
 	h_rc_el_py.setTitleX("REC p_y [GeV]");
	h_rc_el_py.setOptStat(1110);
 	c_rc_el.draw(h_rc_el_py);
 	c_rc_el.cd(2);
	h_rc_el_pz.setOptStat(1110);
 	h_rc_el_pz.setTitleX("REC p_z [GeV]");
 	c_rc_el.draw(h_rc_el_pz);
 	c_rc_el.cd(3);
 	h_rc_el_e.setTitleX("REC E [GeV]");
	F1D f = new F1D("func","[y0]",0.1, 8.0);
	f.setParameters(new double[]{1.0});
	f.setLineColor(4);
	c_rc_el.draw(f);
 	c_rc_el.draw(h_rc_el_e,"same");
	c_rc_el.cd(4);
 	h2_rc_el_ptheta.setTitleX("Momentum [GeV]");
	h2_rc_el_ptheta.setTitleY("#theta [deg]");
	//h2_rc_el_ptheta.setOptStat(1110);
	c_rc_el.draw(h2_rc_el_ptheta,"colz");
	c_rc_el.cd(5);
	h2_rc_el_vzphi.setTitleX("Scattered Electron #phi [deg]");
	h2_rc_el_vzphi.setTitleY("Scattered Electron vertex position [cm]");
	//h2_rc_el_vzphi.setOptStat(1110);
	c_rc_el.draw(h2_rc_el_vzphi,"colz");
	c_rc_el.cd(6);
	h2_rc_el_thetaphi.setTitleX("Scattered Electron #phi [deg]");
	h2_rc_el_thetaphi.setTitleY("Scattered Electron #theta [deg]");
	//h2_rc_el_thetaphi.setOptStat(1110);
	c_rc_el.draw(h2_rc_el_thetaphi,"colz");

	c_rc_pr.setSize(2000,800);
	c_rc_pr.divide(4,2);
 	c_rc_pr.cd(0);
	h_rc_pr_px.setOptStat(1110);
 	h_rc_pr_px.setTitleX("REC p_x [GeV]");
 	c_rc_pr.draw(h_rc_pr_px);
 	c_rc_pr.cd(1);
 	h_rc_pr_py.setTitleX("REC p_y [GeV]");
	h_rc_pr_py.setOptStat(1110);
 	c_rc_pr.draw(h_rc_pr_py);
 	c_rc_pr.cd(2);
 	h_rc_pr_pz.setTitleX("REC p_z [GeV]");
	h_rc_pr_pz.setOptStat(1110);
 	c_rc_pr.draw(h_rc_pr_pz);
 	c_rc_pr.cd(3);
 	h_rc_pr_e.setTitleX("REC E [GeV]");
 	c_rc_pr.draw(h_rc_pr_e);
	c_rc_pr.cd(4);
 	h2_rc_pr_ptheta.setTitleX("Momentum [GeV]");
	h2_rc_pr_ptheta.setTitleY("#theta [deg]");
	//h2_rc_pr_ptheta.setOptStat(1110);
	c_rc_pr.draw(h2_rc_pr_ptheta,"colz");
	c_rc_pr.cd(5);
	h2_rc_pr_vzphi.setTitleX("Scattered Proton #phi [deg]");
	h2_rc_pr_vzphi.setTitleY("Scattered Proton vertex position [cm]");
	//h2_rc_pr_vzphi.setOptStat(1110);
	c_rc_pr.draw(h2_rc_pr_vzphi,"colz");
	c_rc_pr.cd(6);
	h2_rc_pr_thetaphi.setTitleX("Scattered Proton #phi [deg]");
	h2_rc_pr_thetaphi.setTitleY("Scattered Proton #theta [deg]");
	//h2_rc_pr_thetaphi.setOptStat(1110);
	c_rc_pr.draw(h2_rc_pr_thetaphi,"colz");

	c_rc_kp.setSize(2000,800);
	c_rc_kp.divide(4,2);
 	c_rc_kp.cd(0);
 	h_rc_kp_px.setTitleX("REC p_x [GeV]");
	h_rc_kp_px.setOptStat(1110);
 	c_rc_kp.draw(h_rc_kp_px);
 	c_rc_kp.cd(1);
 	h_rc_kp_py.setTitleX("REC p_y [GeV]");
	h_rc_kp_py.setOptStat(1110);
 	c_rc_kp.draw(h_rc_kp_py);
 	c_rc_kp.cd(2);
  	h_rc_kp_pz.setTitleX("REC p_z [GeV]");
	h_rc_kp_pz.setOptStat(1110);
 	c_rc_kp.draw(h_rc_kp_pz);
	c_rc_kp.cd(3);
  	h_rc_kp_e.setTitleX("REC E [GeV]");
	h_rc_kp_e.setOptStat(1110);
 	c_rc_kp.draw(h_rc_kp_e);
	c_rc_kp.cd(4);
 	h2_rc_kp_ptheta.setTitleX("Momentum [GeV]");
	h2_rc_kp_ptheta.setTitleY("#theta [deg]");
	c_rc_kp.draw(h2_rc_kp_ptheta,"colz");
	c_rc_kp.cd(5);
	h2_rc_kp_vzphi.setTitleX("Scattered Kaon Plus #phi [deg]");
	h2_rc_kp_vzphi.setTitleY("Scattered Kaon Plus vertex position [cm]");
	c_rc_kp.draw(h2_rc_kp_vzphi,"colz");
	c_rc_kp.cd(6);
	h2_rc_kp_thetaphi.setTitleX("Scattered Kaon Minus #phi [deg]");
	h2_rc_kp_thetaphi.setTitleY("Scattered Kaon Minus #theta [deg]");
	c_rc_kp.draw(h2_rc_kp_thetaphi,"colz");

	c_rc_km.setSize(2000,800);
	c_rc_km.divide(4,2);
 	c_rc_km.cd(0);
 	h_rc_km_px.setTitleX("REC p_x [GeV]");
	h_rc_km_px.setOptStat(1110);
 	c_rc_km.draw(h_rc_km_px);
 	c_rc_km.cd(1);
 	h_rc_km_py.setTitleX("REC p_y [GeV]");
	h_rc_km_py.setOptStat(1110);
  	c_rc_km.draw(h_rc_km_py);
 	c_rc_km.cd(2);
 	h_rc_km_pz.setTitleX("REC p_z [GeV]");
	h_rc_km_pz.setOptStat(1110);
 	c_rc_km.draw(h_rc_km_pz);
	c_rc_km.cd(3);
 	h_rc_km_e.setTitleX("REC E [GeV]");
	h_rc_km_e.setOptStat(1110);
 	c_rc_km.draw(h_rc_km_e);
	c_rc_km.cd(4);
 	h2_rc_km_ptheta.setTitleX("Momentum [GeV]");
	h2_rc_km_ptheta.setTitleY("#theta [deg]");
	c_rc_km.draw(h2_rc_km_ptheta,"colz");
	c_rc_km.cd(5);
	h2_rc_km_vzphi.setTitleX("Scattered Kaon Minus #phi [deg]");
	h2_rc_km_vzphi.setTitleY("Scattered Kaon Minus vertex position [cm]");
	c_rc_km.draw(h2_rc_km_vzphi,"colz");
	c_rc_km.cd(6);
	h2_rc_km_thetaphi.setTitleX("Scattered Kaon Minus #phi [deg]");
	h2_rc_km_thetaphi.setTitleY("Scattered Kaon Minus #theta [deg]");
	c_rc_km.draw(h2_rc_km_thetaphi,"colz");


    }


    public void SaveHistograms() {

	c_mc_el.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mc_el_mntm.png");
	c_mc_pr.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mc_pr_mntm.png");
	c_mc_kp.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mc_kp_mntm.png");
	c_mc_km.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_mc_km_mntm.png");

	c_rc_el.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_rc_el_mntm.png");
	c_rc_pr.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_rc_pr_mntm.png");
	c_rc_kp.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_rc_kp_mntm.png");
	c_rc_km.save("/u/home/bclary/CLAS12/phi_analysis/v1/pics/h_rc_km_mntm.png");

    }


}
