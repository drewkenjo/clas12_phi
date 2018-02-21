import java.io.*;
import java.util.*;
import com.google.gson.*;
import org.json.*;
import org.jlab.jnp.utils.json.*;

import src.com.FileManager;
import src.com.EventProcessor;
import src.com.BEvent;
import src.com.BHistoPlotter;
import src.com.BHistoKinPlotter;
import src.com.BHistoPhysPlotter;
import src.com.BHistoPhysKinPlotter;
import src.com.BHistoDetectorInfo;
import src.com.PhysicsEvent;
import src.com.PhysicsBuilder;
import src.com.PhysicsEventWriter;
import src.com.Calculator;
import src.com.Detectors;
import src.com.CoolText;
import src.com.GetBanks;
import src.com.RunInformation;
import src.com.CutLoader;
import src.com.RunPropertiesLoader;

import src.com.MatchingElectronPID;
import src.com.MatchingProtonPID;
import src.com.MatchingKaonPlusPID;
import src.com.MatchingProcessor;
import src.com.ElectronPID;
import src.com.ProtonPID;
import src.com.KaonPlusPID;

import src.com.BHistoPIDLevel;
import src.com.BHistoCLAS12PID;

import src.com.CLAS12ElectronPID;
import src.com.CLAS12ProtonPID;
import src.com.CLAS12KaonMinusPID;
import src.com.CLAS12KaonPlusPID;

import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo.data.HipoEvent;
import org.jlab.jnp.hipo.data.HipoGroup;
import org.jlab.jnp.hipo.io.HipoReader;
import org.jlab.jnp.hipo.io.HipoWriter;
import org.jlab.jnp.hipo.schema.SchemaFactory;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.groot.data.H1F;

public class phiAnalysis{

    public static void main( String[] analysisInfo ){
	EventProcessor process = new EventProcessor();

	CoolText coolprint = new CoolText();
	coolprint.printToScreen(">> STARTING ANALYSIS FOR RUN " + analysisInfo[1] + " TYPE "  + analysisInfo[0], "red" );

	String inName = analysisInfo[0];
	int run_number = Integer.valueOf(analysisInfo[1]);

       	String file_location = "/lustre/expphy/volatile/halla/sbs/bclary/clas12Analysis/RECclas12/rec_clary_norad_phi/";
	String file_location2 = "/lustre/expphy/volatile/halla/sbs/bclary/clas12Analysis/RECclas12/rec_fx_norad_phi/";///volatile/clas12/fxgirod/phi/prod_phi_tp1_s1/";
	String file_location3 = "/volatile/halla/sbs/bclary/clas12Analysis/RECclas12/rec_markov_flat_accp/";
	String file_location4 = "/lustre/expphy/volatile/clas12/data/pass0_5/cooked/";
	String file_location5 = "/volatile/clas12/data/TorusFieldTest/";
	String file_location6 = "/volatile/clas12/data/online/cooked/";
	String file_location7 = "/volatile/clas12/data/pass0/cooked/";
	String file_loc = ""; 
	String fieldconf = "";
	String file_name = "";
	String file_ext = "";
	String f_type = "";
		
	if( inName.equals("Clary") ){
	    file_loc = file_location;
	    fieldconf = "/u/home/bclary/CLAS12/phi_analysis/PIX_clary/";
	    file_name = "out_phi_lund_";
	    file_ext = ".hipo";
	    f_type = "clary_pid_phi_";
	}
	else if( inName.equals("FX") ){
	    file_loc = file_location2;
	    fieldconf = "/u/home/bclary/CLAS12/phi_analysis/PIX_fx/";
	    file_name = "out_phi_lund_";
	    file_ext = ".hipo";
	    f_type = "pid_phi_";
	}
	else if( inName.equals("AC") ){
	    file_loc = file_location3;
	    file_name = "out_flatLund";//flatCJ4.8.5";
	    file_ext = ".hipo";
	    f_type = "accp_phi_";				
	}
	else if( inName.equals("CLAS12") ){
	    file_loc = file_location6;
	    file_name = "out_clas_00"+Integer.toString(run_number)+".evio.";
	    file_ext = ".hipo";
	    f_type = "clas12_";
	}

	int max_files = 1;
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

	double fc_integratedcharge = 0.0;

	////////////////////////////
	//
	//   INIT CLASSES & CUTS
	//
	////////////////////////////

	RunPropertiesLoader run_properties = new RunPropertiesLoader();
	run_properties.loadRunProperties(run_number);
	run_properties.setRunProperties();

	HipoDataSource reader = new HipoDataSource();
	BHistoPlotter h_plot = new BHistoPlotter();
	BHistoKinPlotter h_kin = new BHistoKinPlotter();
	BHistoPhysPlotter h_physplot = new BHistoPhysPlotter();
	BHistoPhysKinPlotter h_mc_el_phy = new BHistoPhysKinPlotter("mc","kp");
	BHistoPhysKinPlotter h_mc_pr_phy = new BHistoPhysKinPlotter("mc","km");
	BHistoPhysKinPlotter h_mc_kp_phy = new BHistoPhysKinPlotter("mc","el");
	BHistoPhysKinPlotter h_mc_km_phy = new BHistoPhysKinPlotter("mc","pr");
	BHistoPhysKinPlotter h_mc_physics = new BHistoPhysKinPlotter("mc","phy");
	BHistoDetectorInfo h_detectors = new BHistoDetectorInfo();
	BHistoPIDLevel h_pid_cutlvls = new BHistoPIDLevel(run_number);
	BHistoCLAS12PID h_clas12_pid = new BHistoCLAS12PID(run_number);
	
	MatchingElectronPID match_el = new MatchingElectronPID();
	MatchingProtonPID match_pr = new MatchingProtonPID();
	MatchingKaonPlusPID match_kp = new MatchingKaonPlusPID();

	ElectronPID find_el = new ElectronPID();
	ProtonPID find_pr = new ProtonPID();
	KaonPlusPID find_kp = new KaonPlusPID();

	CLAS12ElectronPID clas12_el = new CLAS12ElectronPID();
	CLAS12ProtonPID clas12_pr = new CLAS12ProtonPID();
	CLAS12KaonPlusPID clas12_kp = new CLAS12KaonPlusPID();
	CLAS12KaonMinusPID clas12_km = new CLAS12KaonMinusPID();

	//match_el.initializeCuts();
	//match_pr.initializeCuts();
	//match_kp.initializeCuts();

	CutLoader cut_loader = new CutLoader();
	cut_loader.loadRunCuts(run_number,"cut_nom");
	cut_loader.setRunCuts();
	cut_loader.printRunCuts();

	
	find_el.initializeCuts();
	find_pr.initializeCuts();
	find_kp.initializeCuts();

	clas12_el.initializeCuts();
	clas12_pr.initializeCuts();
	clas12_kp.initializeCuts();
	clas12_km.initializeCuts();
	
	h_plot.CreateHistograms();
	h_kin.CreateHistograms();
	h_physplot.CreateHistograms();
	h_physplot.CreateMCHistograms();
	h_mc_el_phy.CreateH1Kin();
	h_mc_pr_phy.CreateH1Kin();
	h_mc_kp_phy.CreateH1Kin();
	h_mc_km_phy.CreateH1Kin();
	h_mc_physics.CreateH1Kin();

	h_detectors.CreateHistograms();
	h_pid_cutlvls.CreateHistograms();
	h_clas12_pid.createHistograms();

	//h_pid_cutlvls.createElectronHistoToHipoOut(0);

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
	    System.out.println(" >> FILE TO ANALYZE " + in_file);

	    if( file_to_analyze.exists() ){//  && file_to_analyze.length() >= max_file_size ){
		System.out.println("File Exists");
		f_counter+=1;
	    }
	    if( !file_to_analyze.exists() ){// || file_to_analyze.length() <= max_file_size){
		System.out.println("File DOES NOT Exists");
		continue;
	    }

	    HipoWriter output_writer = new HipoWriter();
	    System.out.println(String.format(">> CREATING OUTPUT FILE pid_phi_%d.hipo",i));
	    String f_output = f_type +i+".hipo";
	    PhysicsEventWriter output = new PhysicsEventWriter(output_writer,"pid",i);
	    output.writeTxtHeader();

	    reader.open(in_file);
	    int counter = 0;
	    DataEvent nullevent = reader.getNextEvent();
	    while( reader.hasEvent() ){
		DataEvent event = reader.getNextEvent();
		
		BEvent bevent = new BEvent();
		//bevent.setEvent(event);

		boolean recBank_present = event.hasBank("REC::Particle");
		boolean genBank_present = event.hasBank("MC::Particle");
		boolean eventBank_present = event.hasBank("REC::Event");

		//System.out.println(" >> EVENT " + counter );

		float start_time = -1000;
		if( eventBank_present && recBank_present ) {
		    DataBank eventBank = event.getBank("REC::Event");
		    start_time = eventBank.getFloat("STTime",0);
		    
		}

		PhysicsBuilder physicsbuild = new PhysicsBuilder();

		double fc_bcurrent = Calculator.faradayCupCurrent(event);
		double fc_event_integratedcharge = Calculator.faradayCupIntegratedCharge(event);
		fc_integratedcharge = fc_integratedcharge + fc_event_integratedcharge; 
		//System.out.println(" >> " + fc_bcurrent + " " + fc_integratedcharge);

		////////////////////////////////////////
		//TESTING HERE 
		//


		if( genBank_present && event.getBank("MC::Particle").rows() == 4 ){
		    DataBank genBank = event.getBank("MC::Particle");
		    //System.out.println(" GEN ROWS " + genBank.rows() );
		    tot_gen_events+=1;

		    //System.out.println(" GEN " );
		   }
		
		boolean rec_time_based = false;
		if( recBank_present && start_time > 0 && event.getBank("REC::Particle").rows() >= 1){

		    DataBank recBank = event.getBank("REC::Particle");
		      if( event.hasBank("TimeBasedTrkg::TBTracks") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("REC::Track") ){
			  //System.out.println(" >> AND TBTRACKS ");
			DataBank tbtrack = event.getBank("TimeBasedTrkg::TBTracks");
			DataBank tbcross = event.getBank("TimeBasedTrkg::TBCrosses");
			DataBank rectrack = event.getBank("REC::Track");
			
			//recBank.show();
			//rectrack.show();						
			//tbtrack.show();
			//tbcross.show();
			
			rec_time_based  = true;

			//	System.out.println(" counter " + counter );
		    }
		        

		    //tot_rec_events+=1;
		    
		    //System.out.println(" REC ROWS with " + recBank.rows() + " rows" );
		    
			//System.out.println(" REC " );
		    
		    boolean el_test = false;
		    boolean pr_test = false;
		    boolean kp_test = false;
		    boolean km_test = false;
		    boolean event_candidate = false;
		    
		    boolean clas12_el_test = false;
		    boolean clas12_pr_test = false;
		    boolean clas12_kp_test = false;
		    
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
		    
		    Vector<Boolean> v_el_tests = new Vector<Boolean>();
		    Vector<Boolean> v_pr_tests = new Vector<Boolean>();
		    Vector<Boolean> v_kp_tests = new Vector<Boolean>();
		    
		    for( int k = 0; k < recBank.rows(); k++ ){
			
			//System.out.println(">> j " + j + ", k " + k ); 
			//LOOK AT EFFECT OF EACH CUT ON DATA			
			
			//v_el_tests = find_el.processCutsVector(event, k);
			//h_pid_cutlvls.FillElectronPID( v_el_tests, event, k );
			
			
			int clas12pid = event.getBank("REC::Particle").getInt("pid",k);
			int clas12charge = event.getBank("REC::Particle").getInt("charge",k);
			if( clas12charge > 0 || clas12charge < 0 ){
			//if( clas12pid == 11 || clas12pid == -11 || clas12pid == 2212 || clas12pid == -2212 || clas12pid == 211 || clas12pid == -211 ){
			    h_pid_cutlvls.fillPNDriftChamber(event, k);
			}


			if ( !el_test ){
			    
			    //el_test = find_el.processCutsVectorResults(v_el_tests);
			    //System.out.println(" >> checking " );
			    if( el_test ){
				//System.out.println(" >> passed " );
				

				tot_el_pass+=1;
				n_el+=1;
				golden_el_index = k;
				//find_pid.processResult(event,k);
				//find_el.getResult(event,k);
				//System.out.println(" >> INDEX IN REC::PARTICLE FOR ELECTRON IS " + k );
				//h_kin.FillNegativesDetHist(event, golden_el_index);
				//System.out.println(" >> passed electron cuts " );
				//Calculator.eventStartTime( event, golden_el_index );
				//h_plot.FillElectronHistograms(event,golden_el_index);
				//h_detectors.FillECHist( event, golden_el_index );
				//h_detectors.FillCherenkovHist( event, golden_el_index );
			    }			    
			    //	}
			}
			    
			if( !clas12_el_test ){

			    //clas12_el_test = clas12_el.processCuts(event, 0);
			    if( clas12_el_test ){
				//System.out.println(" >> clas12 el pass " );
			    	//h_clas12_pid.fillElectronCLAS12PID( event, 0 );
			    }
			    
			    
			}

			if( !clas12_pr_test ){
			    //clas12_pr_test = clas12_pr.processCuts( event, k);
			    if( clas12_pr_test ){
				//h_clas12_pid.fillProtonCLAS12PID(event,k);
			    }
			}
			

			//if( !clas12_kp_test ){
			//  clas12_kp_test = clas12_kp.processCuts(event,k);
			//  h_clas12_pid.fillKaonPCLAS12PID(event,k);
			//}
			    

			//v_pr_tests = find_pr.processCutsVector(event, k);
			//h_pid_cutlvls.fillProtonPID( v_pr_tests, event, k ); 

		       //v_kp_tests = find_kp.processCutsVector(event, k);
		       //	h_pid_cutlvls.fillKaonPPID( v_kp_tests, event, k ); 

			/*if( el_test ){
			    for( int j =0; j < recBank.rows(); j++){
				if( j == k ) continue;
				v_pr_tests = find_pr.processCutsVector(event, k);
				h_pid_cutlvls.fillProtonPID( v_pr_tests, event, k ); 
			    }
			    }*/
			    
			
				
				//}
				//}
			    
			if( !pr_test ){
			    //System.out.println(" TESTING PROTON " + k );
			    //pr_test = cutmanager.passCut("proton",event, 1, k);
			    //pr_test = cutmanager.passCut("proton",event, k);
			    //pr_test = match_pr.processCuts(event,k);
			    //pr_test = find_pr.processCuts(event,k);
				
			    if( pr_test ){
				tot_pr_pass+=1;
				n_pr+=1;
				golden_pr_index = k;
				//find_pr.getResult(event,k);
				//System.out.println("EVENT " + tot_rec_events + " " + k);
				//h_plot.FillProtonHistograms(event,golden_pr_index);
				//System.out.println(" >> INDEX IN REC::PARTICLE FOR PROTON IS " + k );
				/////h_kin.FillProtonDetHist(event, golden_pr_index);
				//////h_kin.FillPositivesDetHist(event, golden_pr_index);
				//	h_detectors.FillFTOFHist( event, golden_pr_index );
				//	h_detectors.FillECHist( event, golden_pr_index );
			    }
			       
				
			    if( !kp_test ){
				//System.out.println(" TESTING KAON PLUS " + k );
				//kp_test = cutmanager.passCut("kaon_plus",event, 2, k);
				//kp_test = find_kp.processCuts(event,k);
				    
				if( kp_test ){
				    tot_kp_pass+=1;
				    n_kp+=1;
				    golden_kp_index = k;
				    //find_kp.getResult(event,k);
				    //h_kin.FillKaonPlusDetHist(event, golden_kp_index);
				    //h_plot.FillKaonPlusHistograms(event,golden_kp_index);
				}
			    }
			    
			    //if( !km_test ){
			    // System.out.println(" TESTING KAON MINUS " + k );
			    //km_test = cutmanager.passCut("kaon_minus",event, 3, k);
			    //if( km_test ){
			    //tot_km_pass+=1;
			    //n_km+=1;
			    //golden_km_index = k;
			    //	h_kin.FillKaonMinusDetHist(event, golden_km_index);										
			    //h_plot.FillKaonMinusHistograms(event,golden_km_index);
			    //}
			}
		    }
		    //}
			
		    // System.out.println(">> POST TEST GOLDEN VALUES " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index);
		    if( golden_el_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 1"); }
		    //	    if( golden_kp_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 2"); }
		    //if( golden_km_index == golden_pr_index && golden_pr_index != -1 ){ System.out.println("OH NO 3"); }
		    //if( golden_kp_index == golden_km_index && golden_kp_index != -1  ){ System.out.println("OH NO 4"); }
			
		    /*		    if( n_el == 1 && n_pr == 1 && (n_kp == 0 && n_km == 0 ) ){
				    tot_ep_pass+=1;
				    eventtopology=1;
				    //System.out.println("ep : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );
				    //			event_candidate = true;
				    } */
			
		    boolean real_event = false;			    
		    if( n_el == 1 && n_pr == 1 && (n_kp == 1)){// && n_km == 0 ) ){
			tot_epkp_pass+=1;
			eventtopology = 2;
			event_candidate = true;
			real_event = true;
			/*System.out.println(" >> EVENT " + counter );
			  double betamntm = Calculator.betaMntm( event, golden_pr_index, 0.938);
			  double betatime =  Calculator.betaTime( event, golden_pr_index, 2);
			  System.out.println(" >> BETA MNTM IS " + " " + betamntm);
			  System.out.println(" >> BETA TIME IS " + " " + betatime);
			      
			  double beta_diff = betamntm - betatime ;
			  System.out.println(" >> DIFFERENCE " + beta_diff);
			*/
			    
			bevent.SetGoldenIndices( golden_el_index, golden_pr_index, golden_kp_index, golden_km_index );
			//System.out.println("epkp : " + golden_el_index + " " + golden_pr_index + " " + golden_kp_index + " " + golden_km_index );
		    }
		    /*
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
		    */
		    boolean toHipo = false;
		    boolean toTxt = false;
		    if(real_event){
			PhysicsEvent final_phy_event = physicsbuild.setPhysicsEvent( real_event, event, golden_el_index, golden_pr_index, golden_kp_index, golden_km_index, eventtopology );
			//PhysicsEvent mc_final_phyev = physicsbuild.setMCPhysicsEvent( event );
				
			//h_physplot.FillPhysicsHistos(final_phy_event);				
			//h_plot.FillElectronHistograms(event,golden_el_index);
			//h_plot.FillProtonHistograms(event,golden_pr_index);
			//h_plot.FillKaonPlusHistograms(event,golden_kp_index);

			//System.out.println(">> golden kp "  + golden_kp_index );

				
			//				System.out.println("event " + counter );
			//output.writePhysicsEvent(toHipo, final_phy_event);
		    }
			    
		    //}
		    //}
		    //if( !real_event ){ System.out.println(" >> NOT REAL EVENT " ); }
		    tot_rec_events+=1;

		    /////PhysicsEvent final_phy_event = physicsbuild.setPhysicsEvent( real_event, event, golden_el_index, golden_pr_index, golden_kp_index, golden_km_index, eventtopology );
		    //PhysicsEvent mc_final_phyev = physicsbuild.setMCPhysicsEvent(event);
		    /////output.writeToTxt(final_phy_event);

		    //h_plot.FillMCHistograms(event);
			    
		    //h_mc_pr_phy.FillH1Kin(mc_final_phyev);
		    //h_mc_kp_phy.FillH1Kin(mc_final_phyev);
		    //h_mc_km_phy.FillH1Kin(mc_final_phyev);
		    //h_mc_el_phy.FillH1Kin(mc_final_phyev);
		    //h_mc_physics.FillH1Kin(mc_final_phyev);
			    
		    //h_physplot.FillMCPhysicsHistograms(mc_final_phyev);
		    count++;
		}
		counter = counter + 1;
			    
	    }
	 
	    //RECORD RUN PROPERTIES:
	    //bevent.setAccumulatedCharge(fc_integratedcharge);
   
	    //START NEXT FILE
	    count = 0;
	    System.out.println(">> CLOSING OUTPUT FILE ");
	    output_writer.close();
	    output.closeTxtFile();
	    
	}
	System.out.println("------------- RESULTS ------------- ");
	System.out.println("TOTAL FILES ANALYZED : " + f_counter);
	System.out.println(">> TOTAL GEN : " + tot_gen_events );
	System.out.println(">> TOTAL REC : " + tot_rec_events );
	System.out.println(">> TOTAL INTEGRATED CHARGE: " + fc_integratedcharge);
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


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// SET RUN PROPERTIES INFORMATION HERE POST RUN SUCH AS INTEGRATED CHARGE, ETC.
	//
	run_properties.setTotalAccumulatedCharge(fc_integratedcharge);
	

  	boolean view_phys = true;
	System.out.println(" >> VIEWING HISTOGRAMS " );
	//h_plot.ViewHistograms();
	//h_kin.ViewHistograms();
	//h_physplot.CompareRECMCHist(false);
	//h_physplot.ViewHistograms(view_phys);
	//h_physplot.viewMCHistograms(view_phys);
	//h_detectors.ViewHistograms();
	//h_physplot.viewAcceptance();
	h_pid_cutlvls.savePIDHistograms(view_phys);
	h_clas12_pid.saveCLAS12PIDHistograms(view_phys);


	System.out.println(">> SAVING HISTOGRAMS");
	//h_plot.SaveHistograms();
	//h_kin.SaveHistograms();
	//h_physplot.SavePhysHistograms();
	//h_physplot.SaveMCPhysHistograms();
	//h_detectors.SaveHistograms();
	//h_mc_el_phy.SaveH1Kin();
	//h_mc_pr_phy.SaveH1Kin();
	//h_mc_kp_phy.SaveH1Kin();
	//h_mc_km_phy.SaveH1Kin();
	//h_mc_physics.SaveH1Kin();
	//h_clas12_pid.printHistograms();
	//h_pid_cutlvls.printHistograms();


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



    public void ProcessData(){
	//DO SOMETHING HERE FOR HANDLING DATA VS MC/REC

    }

}
