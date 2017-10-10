package src.com;

import src.com.PhysicsEvent;

import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoGroup;
import org.jlab.hipo.io.HipoReader;
import org.jlab.hipo.io.HipoWriter;
import org.jlab.hipo.schema.SchemaFactory;

public class PhysicsEventWriter{

    HipoWriter writer;
    String output_location = "/volatile/halla/sbs/bclary/clas12Analysis/clary_phiPID";
    
    //Constructor 
    public PhysicsEventWriter( HipoWriter tempwriter, String tempanalysistype, int tempfile_num ){	
	//HipoWriter tempwriter = new HipoWriter(tempinname);
	writer = tempwriter;

	/// ADD TARGET POLARIZATION OF BEAM POLARIZATION TO RUNPROP BANK FOR REAL DATA LATER
	writer.defineSchema("RunProperties",00,"beam_energy/D");

	writer.defineSchema("final_momenta",1,"el_p/D:pr_p/D:kp_p/D:km_p/D");
	writer.defineSchema("el_properties",2,"el_px/D:el_py/D:el_pz/D:el_E/D");
	writer.defineSchema("pr_properties",3,"pr_px/D:pr_py/D:pr_pz/D:pr_E/D");
	writer.defineSchema("kp_properties",4,"kp_px/D:kp_py/D:kp_pz/D:kp_E/D");
	writer.defineSchema("km_properties",5,"km_px/D:km_py/D:km_pz/D:km_E/D");
	writer.defineSchema("PhysicsEvent",6,"topology/I:q2/D:xB/D:t/D:w2/D:cm_theta/D:cm_phi/D");
	writer.open(String.format("%s/%s_phi_%d.hipo",output_location,tempanalysistype, tempfile_num));
	writer.setCompressionType(2);

    }

    public void outputFileProperties(int temp_nfile){

	

    }

    public void writePhysicsEvent(PhysicsEvent phyev){
	//System.out.println(">> WRITING EVENT TO OUTPUT FORMAT");
	
	HipoGroup bank = writer.getSchemaFactory().getSchema("PhysicsEvent").createGroup(1);
	HipoGroup bank2 = writer.getSchemaFactory().getSchema("RunProperties").createGroup(1);
	HipoGroup final_state = writer.getSchemaFactory().getSchema("final_momenta").createGroup(1);
	HipoGroup el_bank = writer.getSchemaFactory().getSchema("el_properties").createGroup(1);
	HipoGroup pr_bank = writer.getSchemaFactory().getSchema("pr_properties").createGroup(1);
	HipoGroup kp_bank = writer.getSchemaFactory().getSchema("kp_properties").createGroup(1);
	HipoGroup km_bank = writer.getSchemaFactory().getSchema("km_properties").createGroup(1);
	

	//System.out.println("GETTING NODES");
	//final_state.getNode("el_p").setDouble(0,lv_el.vect().mag());

	el_bank.getNode("el_px").setDouble(0,phyev.lv_el.px());
 	el_bank.getNode("el_py").setDouble(0,phyev.lv_el.py());
 	el_bank.getNode("el_pz").setDouble(0,phyev.lv_el.pz());
 	el_bank.getNode("el_E").setDouble(0,phyev.lv_el.e());

 	pr_bank.getNode("pr_px").setDouble(0,phyev.lv_pr.px());
 	pr_bank.getNode("pr_py").setDouble(0,phyev.lv_pr.py());
 	pr_bank.getNode("pr_pz").setDouble(0,phyev.lv_pr.pz());
 	pr_bank.getNode("pr_E").setDouble(0,phyev.lv_pr.e());

 	kp_bank.getNode("kp_px").setDouble(0,phyev.lv_kp.px());
 	kp_bank.getNode("kp_py").setDouble(0,phyev.lv_kp.py());
 	kp_bank.getNode("kp_pz").setDouble(0,phyev.lv_kp.pz());
 	kp_bank.getNode("kp_E").setDouble(0,phyev.lv_kp.e());

 	km_bank.getNode("km_px").setDouble(0,phyev.lv_km.px());
 	km_bank.getNode("km_py").setDouble(0,phyev.lv_km.py());
 	km_bank.getNode("km_pz").setDouble(0,phyev.lv_km.pz());
 	km_bank.getNode("km_E").setDouble(0,phyev.lv_km.e());
	
	bank.getNode("topology").setInt(0,phyev.topology);
	bank.getNode("q2").setDouble(0,phyev.q2);
	bank.getNode("xB").setDouble(0,phyev.xB);
	bank.getNode("t").setDouble(0,phyev.t);
	bank.getNode("w2").setDouble(0,phyev.w2);
	bank.getNode("cm_theta").setDouble(0,phyev.cm_theta);
	bank.getNode("cm_phi").setDouble(0,phyev.cm_phi);
       	
	//System.out.println("CREATING EVENT");
	HipoEvent event = writer.createEvent();
	//System.out.println("WRITING GROUP");
	event.writeGroup(bank);
	event.writeGroup(el_bank);
	event.writeGroup(pr_bank);
	event.writeGroup(kp_bank);
	event.writeGroup(km_bank);
	//System.out.println("WRITING EVENT");
	writer.writeEvent(event);
	//System.out.println("WRITTEN");
    }


}

