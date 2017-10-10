package src.com;

import src.com.PhysicalConstants;

import java.io.*;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.clas.physics.LorentzVector;


class MatchProtonMntm implements BICandidate2 {

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
	double gen_p = Math.sqrt( gen_px*gen_px + gen_py*gen_py + gen_pz*gen_pz );// + PhysicalConstants.mass_proton*PhysicalConstants.mass_proton );

	
	double p_accuracy = (rec_p - gen_p) / gen_p;
	//System.out.println( " >> MNTM " + rec_p + " " + gen_p );
	if(  Math.abs(p_accuracy) < 0.15 ){
	    return true;
	}
	return false;


    }
}