package org.jlab.clas.analysis.clary;

import java.util.*; //ArrayList;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;
import org.jlab.clas.analysis.clary.Calculator;

public class Detectors {

    static public int pcal = 1;
    static public int ec_ei = 4;
    static public int ec_eo = 7;
    static public int ec_cal = 7;
    
    public static HashMap ECHit( DataEvent tempevent, int temp_pindex ){
	HashMap<Integer ,ArrayList<Double> > m_ec_hit = new HashMap<Integer, ArrayList<Double> >();
	
	if( tempevent.hasBank("REC::Calorimeter") ){
	    DataBank ecBank = tempevent.getBank("REC::Calorimeter");
	    for( int i = 0; i < tempevent.getBank("REC::Calorimeter").rows(); i++ ){
		ArrayList<Double> hit_pos = new ArrayList<Double>();

		int detector = ecBank.getInt("layer",i);
		double hit_x = ecBank.getFloat("x",i);
		double hit_y = ecBank.getFloat("y",i);
		double hit_z = ecBank.getFloat("z",i);
		hit_pos.add(hit_x);
		hit_pos.add(hit_y);
		hit_pos.add(hit_z);		
		//System.out.println(" >> " + detector );
		if( ecBank.getShort("pindex",i) == temp_pindex && (detector == ec_ei ) ){
		    m_ec_hit.put( detector, hit_pos );
		}
		if( ecBank.getShort("pindex",i) == temp_pindex && (detector == ec_eo ) ){
		    m_ec_hit.put( detector, hit_pos );
		}
	    }
	}
	
	return m_ec_hit;
    }
    
    public static ArrayList PCALHit( DataEvent tempevent, int temp_pindex ){
	ArrayList<Double> pcal_hit = new ArrayList<Double>();

	if( tempevent.hasBank("REC::Calorimeter") ){
	    //  System.out.println(" >> EVENT HAS CAL BANK " );
	    DataBank ecBank = tempevent.getBank("REC::Calorimeter");
	    for( int i = 0; i < tempevent.getBank("REC::Calorimeter").rows(); i++ ){
		//System.out.println(" >> LOOPING OVER REC::CAL index " + i );
		int layer = ecBank.getInt("layer",i);
		int pindex = ecBank.getShort("pindex",i);
		if( ecBank.getShort("pindex",i) == temp_pindex && layer == pcal ){
		    //System.out.println(" >> pindex " + pindex + " rec index " + temp_pindex );
		    double hit_x = ecBank.getFloat("x",i);
		    double hit_y = ecBank.getFloat("y",i);
		    double hit_z = ecBank.getFloat("z",i);
		    pcal_hit.add(hit_x);
		    pcal_hit.add(hit_y);
		    pcal_hit.add(hit_z);
		}
	    }
	}
	//System.out.println(" >> SIZE OF pcal_hit array " + pcal_hit.size() );
	return pcal_hit;
    }

    public static double ftofTiming( DataEvent tempevent, int temp_pindex, int layer ){
	//ArrayList<double> ftof_time = new ArrayList<double>();
	double ftof_hit_time = 0.0;
	//System.out.println(" >> GETTING FTOF TIME " );
	if( tempevent.hasBank("REC::Scintillator") ){
	    //System.out.println(" >> HAS SCINTILLATOR " );
	    DataBank scintBank = tempevent.getBank("REC::Scintillator");
	    for( int i = 0; i < scintBank.rows(); i++ ){
		if( scintBank.getShort("pindex",i) == temp_pindex ){
		    //System.out.println(" >> HAS PINDEX : index " + i + " " + scintBank.getShort("pindex",i) + " " + temp_pindex );
		    int scint_layer = scintBank.getInt("layer",i);
		    //System.out.println(" >> SCINT LAYER " + scint_layer + " for " + layer );
		    if( scint_layer == layer ){
			//System.out.println(" >> MATCHING SCINT LAYER " + scint_layer );
			float time = scintBank.getFloat("time",i);
			ftof_hit_time = time;
		    }
		}
	    }
	}
	//System.out.println(" >> TIME IS  " + ftof_hit_time );
	return ftof_hit_time;
    }


    public static HashMap<Integer, Double> getEDepCal( DataEvent tempevent, int temp_pindex ){

	HashMap m_edep = new HashMap();
	if( tempevent.hasBank("REC::Calorimeter") ){

	    DataBank calBank = tempevent.getBank("REC::Calorimeter");
	    for( int i = 0; i < calBank.rows(); i++ ){
		if( calBank.getShort("pindex", i) == temp_pindex ){
		    int cal_detector = calBank.getInt("layer", i);
		    double edep = calBank.getFloat("energy",i );		    		    
		    //System.out.println(" >> CAL HIT IN " + cal_detector + " ENERGY " + edep );
		    m_edep.put(cal_detector, edep);
		}
	    }
	}
	return m_edep;
    }


    public static int getSectorDC( DataEvent tempevent, int temp_pindex ){ //WTF DID I TYPE HERE CHANGE NAME TO SCINT
	int sector = -1;
	if( tempevent.hasBank("REC::Scintillator") ){
	    DataBank  scintBank = tempevent.getBank("REC::Scintillator");
	    for( int i = 0; i < scintBank.rows(); i++ ){
		if( scintBank.getShort("pindex",i) == temp_pindex ){
		    sector = scintBank.getInt("sector",i);
		}
	    }
	}
	return sector;
    }

    public static int getSectorECAL( DataEvent tempevent, int temp_pindex ){
	int sector = -1;
	if( tempevent.hasBank("REC::Calorimeter") ){
	    DataBank  scintBank = tempevent.getBank("REC::Calorimeter");
	    for( int i = 0; i < scintBank.rows(); i++ ){
		if( scintBank.getShort("pindex",i) == temp_pindex ){
		    sector = scintBank.getInt("sector",i);
		}
	    }
	}
	return sector;
    }

    public static int getSectorPCAL( DataEvent tempevent, int temp_pindex ){
	int sector = -1;
	if( tempevent.hasBank("REC::Calorimeter") ){
	    DataBank calBank = tempevent.getBank("REC::Calorimeter");
	    for( int i = 0; i < calBank.rows(); i++ ){
		if( calBank.getShort("pindex",i) == temp_pindex ){
		    int layer = calBank.getInt("layer",i);
		    if( layer == pcal ){
			sector = calBank.getInt("sector",i);
		    }
		}
	    }
	}
	return sector;
    }


    
    
    public static double getCherenkovNPHE( DataEvent event, int rec_i ){

	double nphe = -1.0;

	/*if( event.hasBank("REC::Cherenkov") ){    	
	    DataBank chkovBank = tempevent.getBank("REC::Cherenkov");
	    for( int i = 0; i < chkovBank.rows(); i++ ){
		int pindex =  chkovBank.getShort("pindex",i);                                                                                                                                                              int detector = chkov.getByte("detector",i);                                                
		if( pindex == temp_pindex && detector == 15  ){                                                                                                                                                                                                                  
		    nphe = chkovBank.getInt("nphe",i);                                                                                                                                                                                                                      
		    System.out.println(" >> NPHE " + nphe );                                                                                                                                                                                                                
		}                                           
	    }
	}
	*/


	if( event.hasBank("REC::Cherenkov") ){
	    //System.out.println(" >> HAS CHERENKOV " );
	    DataBank chkovBank = event.getBank("REC::Cherenkov");
	    for( int i = 0; i < chkovBank.rows(); i++ ){
		int pindex =  chkovBank.getShort("pindex",i); 
		// 15 - HTCC
		//16 - LTCC
		int detector =  chkovBank.getByte("detector",i);  
		
		//System.out.println(" >>  REC::CHERENKOV PINDEX " + pindex );
		if( pindex == rec_i && detector == 15 ){		    
		    nphe = chkovBank.getInt("nphe",i);
		    //System.out.println(" >> NPHE " + nphe );		    
		}
	    }
	}
	return (double)nphe;
       
    }

    public static int getDCSectorR1( DataEvent event, int rec_i ){
 
	int dc_sector_r1 = -1;
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    int sector_r1 = tbtracks.getByte("sector",i);
			    dc_sector_r1 = sector_r1;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_sector_r1;
    
    }

    public static int getDCSectorR2( DataEvent event, int rec_i ){
 
	int dc_sector_r2 = -1;
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    int sector_r2 = tbtracks.getByte("sector",i);
			    dc_sector_r2 = sector_r2;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_sector_r2;
    
    }

   public static int getDCSectorR3( DataEvent event, int rec_i ){
 
	int dc_sector_r3 = -1;
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    int sector_r3 = tbtracks.getByte("sector",i);
			    dc_sector_r3 = sector_r3;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_sector_r3;
    
    }


    public static double getDCCrossX1( DataEvent event, int rec_i ){

	double dc_cross_1x = -1000;
	

	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    double c1_x = tbtracks.getFloat("c1_x",i);
			    dc_cross_1x = c1_x;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_cross_1x;
    
    }

    public static double getDCCrossY1( DataEvent event, int rec_i ){

	double dc_cross_1y = -1000;
	

	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    double c1_y = tbtracks.getFloat("c1_y",i);
			    dc_cross_1y = c1_y;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_cross_1y;
    
    }


    public static double getDCCrossX3( DataEvent event, int rec_i ){

	double dc_cross_3x = -1000;
	

	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    double c3_x = tbtracks.getFloat("c3_x",i);
			    dc_cross_3x = c3_x;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_cross_3x;
    
    }

    public static double getDCCrossY3( DataEvent event, int rec_i ){

	double dc_cross_3y = -1000;
	

	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i );
		//System.out.println(" >> PINDEX " + pindex  + " REC " + rec_i + " INDEX " +  index );
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++){
			if( j == index ){
			    double c3_y = tbtracks.getFloat("c3_y",i);
			    dc_cross_3y = c3_y;
			    break;
			}
		    }
		}
		
	    }

	}
	return dc_cross_3y;
    
    }


    public static Vector<Double> getDCCrossLocalR1( DataEvent event, int rec_i ){
	Vector<Double> v_temp_local_coord = new Vector<Double>();
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i);
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++ ){
			if( j == index ){
			    int cross1_id = tbtracks.getShort("Cross1_ID",j);
			    for( int k = 0; k < tbcrosses.rows(); k++ ){
				int cross_id = tbcrosses.getShort("id",k);
				if( cross_id == cross1_id ){
				    double cross1_hit_x = tbcrosses.getFloat("x",k);
				    double cross1_hit_y = tbcrosses.getFloat("y",k);				   
				    v_temp_local_coord.add(cross1_hit_x);
				    v_temp_local_coord.add(cross1_hit_y);
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
	return v_temp_local_coord;
    }
    
    public static Vector<Double> getDCCrossLocalR2( DataEvent event, int rec_i ){
	Vector<Double> v_temp_local_coord = new Vector<Double>();
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i);
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++ ){
			if( j == index ){
			    int cross2_id = tbtracks.getShort("Cross2_ID",j);
			    for( int k = 0; k < tbcrosses.rows(); k++ ){
				int cross_id = tbcrosses.getShort("id",k);
				if( cross_id == cross2_id ){
				    double cross2_hit_x = tbcrosses.getFloat("x",k);
				    double cross2_hit_y = tbcrosses.getFloat("y",k);				   
				    v_temp_local_coord.add(cross2_hit_x);
				    v_temp_local_coord.add(cross2_hit_y);
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
	return v_temp_local_coord;
    }

    public static Vector<Double> getDCCrossLocalR3( DataEvent event, int rec_i ){
	Vector<Double> v_temp_local_coord = new Vector<Double>();
	
	if( event.hasBank("REC::Track") && event.hasBank("TimeBasedTrkg::TBCrosses") && event.hasBank("TimeBasedTrkg::TBTracks") ){
	    
	    DataBank recTrack = event.getBank("REC::Track");
 	    DataBank tbcrosses = event.getBank("TimeBasedTrkg::TBCrosses");
	    DataBank tbtracks = event.getBank("TimeBasedTrkg::TBTracks");

	    for( int i = 0; i < recTrack.rows(); i++ ){
		int pindex = recTrack.getShort("pindex",i);
		int detector = recTrack.getByte("detector",i);
		int index = recTrack.getShort("index",i);
		if( pindex == rec_i && detector == 6 ){
		    for( int j = 0; j < tbtracks.rows(); j++ ){
			if( j == index ){
			    int cross3_id = tbtracks.getShort("Cross3_ID",j);
			    for( int k = 0; k < tbcrosses.rows(); k++ ){
				int cross_id = tbcrosses.getShort("id",k);
				if( cross_id == cross3_id ){
				    double cross3_hit_x = tbcrosses.getFloat("x",k);
				    double cross3_hit_y = tbcrosses.getFloat("y",k);
				    //System.out.println(" >> LOCAL HIT COORDINATE IS " + cross3_hit_x );
				    v_temp_local_coord.add(cross3_hit_x);
				    v_temp_local_coord.add(cross3_hit_y);
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
	return v_temp_local_coord;
    }

       
}
    
