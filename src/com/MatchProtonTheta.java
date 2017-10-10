package src.com;

import src.com.PhysicalConstants;

import java.io.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.clas.physics.LorentzVector;

class MatchProtonTheta implements BICandidate2 {

    public boolean candidate2( DataEvent tempdevent, int index, int rec_i ){

	DataBank recbank = tempdevent.getBank("REC::Particle");
	DataBank genbank = tempdevent.getBank("MC::Particle");

	float gen_px = genbank.getFloat("px",index);
	float gen_py = genbank.getFloat("py",index);
	float gen_pz = genbank.getFloat("pz",index);
	float gen_vz = genbank.getFloat("vz",index);

	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz );// + PhysicalConstants.mass_proton*PhysicalConstants.mass_proton );
	double gen_p = Math.sqrt( gen_px*gen_px + gen_py*gen_py + gen_pz*gen_pz);// + PhysicalConstants.mass_proton*PhysicalConstants.mass_proton );
	
	LorentzVector gen = new LorentzVector(gen_px, gen_py, gen_pz, gen_p );
	LorentzVector rec = new LorentzVector(rec_px, rec_py, rec_pz, rec_p );

	double rec_theta = Math.toDegrees(Math.acos(rec_pz/rec_p));// * 180.0/Math.PI;
	double gen_theta = Math.toDegrees(Math.acos(gen_pz/gen_p));// * 180.0/Math.PI;

	//System.out.println(" >> THETA " + rec_theta + " " + gen_theta );
	//System.out.println(" >> LV THETA " + Math.toDegrees(gen.theta()) + " " + Math.toDegrees(rec.theta()) );
	
	double theta_accuracy = (rec_theta - gen_theta);// / gen_theta;
	
	if(  Math.abs(theta_accuracy) < 3 ){
	    return true;
	}
	return false;

    }
}
