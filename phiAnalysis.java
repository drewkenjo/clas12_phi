import java.io.*;

import src.com.FileManager;
import src.com.EventProcessor;
import src.com.BEvent;
import src.com.BHistoPlotter;
import src.com.BHistoPhysPlotter;
import src.com.BCutManager;
import src.com.PhysicsEvent;
import src.com.PhysicsBuilder;
import src.com.PhysicsEventWriter;
import src.com.GetBanks;

import org.jlab.io.hipo.HipoDataSource;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoGroup;
import org.jlab.hipo.io.HipoReader;
import org.jlab.hipo.io.HipoWriter;
import org.jlab.hipo.schema.SchemaFactory;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.groot.data.H1F;

public class phiAnalysis{

    public static void main( String[] analysisName ){
	EventProcessor process = new EventProcessor();

	System.out.println(" >> STARTING ANALYSIS FOR " + analysisName[0]);
	String inName = analysisName[0];

       	String file_location = "/volatile/halla/sbs/bclary/clas12Analysis/RECclas12/rec_fx_norad_phi/";
	String file_location2 = "/volatile/clas12/fxgirod/phi/prod_phi_tp1_s1/";
	String file_loc = ""; 
	String fieldconf = "";
	String file_name = "";
	String file_ext = "";

	if( inName.equals("Clary") ){
	    file_loc = file_location;
	    fieldconf = "/u/home/bclary/CLAS12/phi_analysis/PIX_clary/";
	    file_name = "phi_lund_";
	    file_ext = ".txt.evio.hipo.hipo";
	}
	else if( inName.equals("FX") ){
	    file_loc = file_location2;
	    fieldconf = "/u/home/bclary/CLAS12/phi_analysis/PIX_fx/";
	    file_name = "out_phi_gemc_";
	    file_ext = ".hipo";
	}

	int max_files = 20;
	int count = 0;

	/////////////////////////////
	//
	//   RATE COUNTERS
	//
	////////////////////////////
 	int tot_el_pass = 0;
 	int tot_pr_pass = 0;
 	int tot_kp_pass = 0;
 	int tot_km_pass = 0;

	int tot_epkpkm_pass = 0;
	int tot_ep_pass = 0;
	int tot_epkp_pass = 0;
	int tot_epkm_pass = 0;
	int tot_ekpkm_pass = 0;
	int tot_all_pass = 0;

	int tot_gen_events = 0;
	int tot_rec_events = 0;

	////////////////////////////
	//
	//   INIT CLASSES & CUTS
	//
	////////////////////////////

	HipoDataSource reader = new HipoDataSource();
	BHistoPlotter h_plot = new BHistoPlotter();
	BHistoPhysPlotter h_physplot = new BHistoPhysPlotter();
	BCutManager cutmanager = new BCutManager();
	//GetBanks get = new GetBanks();

	cutmanager.InitializeCutsFor("electron");
	cutmanager.InitializeCutsFor("proton");
	cutmanager.InitializeCutsFor("kaon_plus");
	cutmanager.InitializeCutsFor("kaon_minus");

	h_plot.CreateHistograms();
	h_physplot.CreateHistograms();
	h_physplot.CreateMCHistograms();
	cutmanager.ElectronCuts();

	////////////////////////////
	//
	// START PID ANALYSIS 
	//
	////////////////////////////
	int f_counter = 0;

	for( int i = 1; i <= max_files; i++ ){
	    
	    String in_file = file_loc + file_name + Integer.toString(i) + file_ext ;
	    File file_to_analyze = new File(in_file);
	    double max_file_size = 200000000;
	    System.out.println(in_file);

	    if( file_to_analyze.exists() && file_to_analyze.length() >= max_file_size ){
		System.out.println("File Exists");
		f_counter+=1;
	    }
	    if( !file_to_analyze.exists() || file_to_analyze.length() <= max_file_size){
		System.out.println("File DOES NOT Exists");
		continue;
	    }

	    HipoWriter output_writer = new HipoWriter();
	    System.out.println(String.format(">> CREATING OUTPUT FILE pid_phi_%d.hipo",i));
	    String f_output = "pid_phi_"+i+".hipo";
	    PhysicsEventWriter output = new PhysicsEventWriter(output_writer,"pid",i);
	    
	    reader.open(in_file);
	    int counter = 0;
	    DataEvent nullevent = reader.getNextEvent();
	    while( reader.hasEvent() ){
		DataEvent event = reader.getNextEvent();
		
		//get.particleIndices(event);

		BEvent bevent = new BEvent();
		bevent.setEvent(event);

		boolean recBank_present = event.hasBank("REC::Particle");
		boolean genBank_present = event.hasBank("MC::Particle");
		PhysicsBuilder physicsbuild = new PhysicsBuilder();
		
		if( genBank_present && event.getBank("MC::Particle").rows() == 4 ){
		    DataBank genBank = event.getBank("MC::Particle");
		    //System.out.println(" GEN ROWS " + genBank.rows() );
		    tot_gen_events+=1;

		    //System.out.println(" GEN " );
		   }
		
		    if( recBank_present ){
			DataBank recBank = event.getBank("REC::Particle");
			tot_rec_events+=1;
			
			//System.out.println(" REC ROWS " + recBank.rows() );
			
			//System.out.println(" REC " );
			
			boolean el_test = false;
			boolean pr_test = false;
			boolean kp_test = false;
			boolean km_test = false;
			boolean event_candidate = false;

			//System.out.println(">> BOOLEAN STATUS OF PARTICLES " + el_test + " " + pr_test + " " + kp_test + " " + km_test + " " + event_candidate);
			int n_el = 0;
			int n_pr = 0;
			int n_kp = 0;
			int n_km = 0;

			int golden_el_index = -1;
			int golden_pr_index = -1;
			int golden_kp_index = -1;
			int golden_km_index = -1;
			//System.out.println(">> GOLDEN INDEX OF PARTICLES " +  golden_el_index + " " +  golden_pr_index + " " +  golden_kp_index + " " + golden_km_index);

			//VALUES FOR EVENT TOPOLOGY : -1 null, 1 = e+p, 2 = e+p+kp, 3 = e+p+km, 4 = e+p+kp+km, 5 = e+kp+km
			int eventtopology = -1;
			//for( int j = 0; j < genBank.rows(); j++ ){
			//  int e_index = -1;
			for( int k = 0; k < recBank.rows(); k++ ){
		
				//System.out.println(">> j " + j + ", k " + k ); 
				if ( !el_test ){
				    //	    System.out.println(" TESTING ELECTRON " + k );
				    el_test = cutmanager.passCut("electron", event, 0, k);
				    if( el_test ){
					tot_el_pass+=1;
					n_el+=1;
					golden_el_index = k;
					h_plot.FillElectronHistograms(event,golden_el_index);
				    }			    
				}
				if( !pr_test ){
				    //System.out.println(" TESTING PROTON " + k );
				    pr_test = cutmanager.passCut("proton",event, 1, k);
				    if( pr_test ){
					tot_pr_pass+=1;
					n_pr+=1;
					golden_pr_index = k;
					//System.out.println("EVENT " + tot_rec_events + " " + k);
					h_plot.FillProtonHistograms(event,golden_pr_index);
				    }
				}
				if( !kp_test ){
				    //System.out.println(" TESTING KAON PLUS " + k );
				    kp_test = cutmanager.passCut("kaon_plus",event, 2, k);
				    if( kp_test ){
					tot_kp_pass+=1;
					n_kp+=1;
					golden_kp_index = k;
					h_plot.FillKaonPlusHistograms(event,golden_kp_index);
				    }
				   }
				   if( !km_test ){
				       // System.out.println(" TESTING KAON MINUS " + k );
				    km_test = cutmanager.passCut("kaon_minus",event, 3, k);
				    if( km_test ){
					tot_km_pass+=1;
					n_km+=1;
					golden_km_index = k;
					h_plot.FillKaonMinusHistograms(event,golden_km_index);
				    }
				   }
			    }

			// System.out.println(">> POST TEST GOLDEN VALUES " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index);
			    if( golden_el_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 1"); }
			    if( golden_kp_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 2"); }
			    if( golden_km_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 3"); }
			    if( golden_kp_index == golden_km_index && golden_kp_index != -1  ){ System.out.println("OH NO 4"); }

			    if( n_el == 1 && n_pr == 1 && (n_kp == 0 && n_km == 0 ) ){
				tot_ep_pass+=1;
 				eventtopology=1;
 				//System.out.println("ep : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );
				//			event_candidate = true;
			    } 

			    boolean real_event = false;			    
			    if( n_el == 1 && n_pr == 1 && (n_kp == 1 && n_km == 0 ) ){
				tot_epkp_pass+=1;
				eventtopology = 2;
				event_candidate = true;
				real_event = true;
				//System.out.println("epkp : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );
			    } 
			    
			    if( n_el == 1 && n_pr == 1 && (n_kp == 0 && n_km == 1 ) ){
				tot_epkm_pass+=1;
				eventtopology = 3;
				event_candidate = true;
				real_event = true;
				//System.out.println("epkm : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );

			    } 
			    if( n_el == 1 && n_pr == 1 && (n_kp == 1 && n_km == 1 ) ){						       
				tot_all_pass+=1;
				eventtopology = 4;
				event_candidate = true;
				//real_event = true;
				//System.out.println("epkpkm : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );

			    }
			    if( n_el == 1 && n_pr == 0 && (n_kp == 1 && n_km == 1 ) ){
				tot_ekpkm_pass+=1;
				eventtopology = 5;
				//event_candidate = true;
				real_event = true;
				//System.out.println("ekpkm : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );
			    } 

			    if(real_event){
				counter = counter + 1;
				PhysicsEvent final_phy_event = physicsbuild.setPhysicsEvent( event, golden_el_index, golden_pr_index, golden_kp_index, golden_km_index, eventtopology );
				h_physplot.FillPhysicsHistos(final_phy_event);

				//				System.out.println("event " + counter );
				output.writePhysicsEvent(final_phy_event);
			    }
			    
		    }
		    //}
		
		    PhysicsEvent mc_final_phyev = physicsbuild.setMCPhysicsEvent(event);
		    h_physplot.FillMCPhysicsHistograms(mc_final_phyev);
		    count++;
	    }
	    
	    
	    //START NEXT FILE
	    count = 0;
	    System.out.println(">> CLOSING OUTPUT FILE ");
	    output_writer.close();
	}
	
	
	boolean view_phys = false;
	h_plot.ViewHistograms();
	h_physplot.ViewHistograms(view_phys);
	h_physplot.viewMCHistograms(view_phys);
	h_plot.SaveHistograms();
	h_physplot.SavePhysHistograms();
	h_physplot.SaveMCPhysHistograms();

	System.out.println("------------- RESULTS ------------- ");
	System.out.println("TOTAL FILES ANALYZED : " + f_counter);
	System.out.println(">> TOTAL GEN : " + tot_gen_events );
	System.out.println(">> TOTAL REC : " + tot_rec_events );
	System.out.println("---- PARTICLE RATES ---- ");
	System.out.println(">> TOTAL PASSING ELECTRONS: " + tot_el_pass);
	System.out.println(">> TOTAL PASSING PROTONS: " + tot_pr_pass);
	System.out.println(">> TOTAL PASSING K PLUS: " + tot_kp_pass);
	System.out.println(">> TOTAL PASSING K MINUS: " + tot_km_pass);
	System.out.println("---- EVENT RATES ---- ");
	System.out.println(">> TOTAL e+p EVENTS : " + tot_ep_pass );
	System.out.println(">> TOTAL e+p+kp EVENTS : " + tot_epkp_pass );
	System.out.println(">> TOTAL e+p+km EVENTS : " + tot_epkm_pass );
	System.out.println(">> TOTAL e+kp+km EVENTS : " + tot_ekpkm_pass );	
	System.out.println(">> TOTAL e+p+kp+km EVENTS : " + tot_all_pass );
	int tot_events = tot_ep_pass + tot_epkp_pass + tot_epkm_pass + tot_ekpkm_pass + tot_all_pass;
	System.out.println(">> TOTAL EVENTS PASSING : " + tot_events );

	BufferedWriter writer = null;
	try
	    {
		String particle_multiplicity_results = "partMultiplicity.txt";
		writer = new BufferedWriter( new FileWriter(particle_multiplicity_results));
		writer.write(Integer.toString(tot_gen_events) + "\n");
		writer.write(Integer.toString(tot_rec_events) + "\n");
		writer.write(Integer.toString(tot_el_pass) + "\n");
		writer.write(Integer.toString(tot_pr_pass) + "\n");
		writer.write(Integer.toString(tot_kp_pass) + "\n");
		writer.write(Integer.toString(tot_km_pass) + "\n");
		writer.write(Integer.toString(tot_ep_pass) + "\n");
		writer.write(Integer.toString(tot_epkp_pass) + "\n");
		writer.write(Integer.toString(tot_epkm_pass) + "\n");
		writer.write(Integer.toString(tot_ekpkm_pass) + "\n");
		writer.write(Integer.toString(tot_all_pass) + "\n");
		writer.write(Integer.toString(tot_events) + "\n");
	    }
	catch ( IOException e)
	    {
		System.out.println(">> ERROR WRITING TO FILE ");
	    }	
	finally
	    {
		try
		    {
			if ( writer != null)
			    writer.close( );
		    }
		catch ( IOException e)
		    {
			System.out.println(">> ANOTHER ERROR");
		    }
	    }    
    }
}
