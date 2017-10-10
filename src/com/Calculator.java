package src.com;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.clas.physics.LorentzVector;

public class Calculator{

    public static double lv_theta( DataEvent tempevent, int rec_i){       
	DataBank recbank = tempevent.getBank("REC::Particle");
	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);
	
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double theta = Math.toDegrees(Math.acos(rec_pz/rec_p));

	return theta;
    }

    public static double lv_phi( DataEvent tempevent, int rec_i){       
	DataBank recbank = tempevent.getBank("REC::Particle");
	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);
	
	double rec_p = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz);
	double phi = Math.toDegrees(Math.atan2(rec_py,rec_px));

	return phi;
    }


    public static LorentzVector lv_particle( DataEvent tempevent, int rec_i){

	LorentzVector lv_temp = new LorentzVector(0,0,0,0);

	DataBank recbank = tempevent.getBank("REC::Particle");
	float rec_px = recbank.getFloat("px",rec_i);
	float rec_py = recbank.getFloat("py",rec_i);
	float rec_pz = recbank.getFloat("pz",rec_i);
	float rec_vz = recbank.getFloat("vz",rec_i);

	double rec_e = Math.sqrt( rec_px*rec_px + rec_py*rec_py + rec_pz*rec_pz + PhysicalConstants.mass_electron*PhysicalConstants.mass_electron );
	lv_temp.setPxPyPzE(rec_px,rec_py,rec_pz,rec_e);
	return lv_temp;
    }


}
