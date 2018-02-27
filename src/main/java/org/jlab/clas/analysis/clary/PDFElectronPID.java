package org.jlab.clas.analysis.clary;

import org.jlab.clas.analysis.clary.PDFElectronBeta;

import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataBank;

import org.jlab.io.hipo.HipoDataEvent;

import java.util.*;
import java.io.*;

public class PDFElectronPID implements IParticleIdentifier{

    Vector<BIPDFCandidate> v_cuts = new Vector<BIPDFCandidate>();

    PDFElectronBeta pdf_el_beta = new PDFElectronBeta();

    public PDFElectronPID() {
    }

    public void initializeCuts(){

	v_cuts.add(pdf_el_beta);

    }

    public boolean processCuts( DataEvent tempevent, int rec_i ){

	boolean result = true;
	for( BIPDFCandidate cut : v_cuts ){
            /*
	    if( cut.candidate( tempevent, rec_i ) < 0.9 ){
		result = false;
		break;
	    }
            */
	}
	return result;
    }



    public void getResult( DataEvent event, int rec_i ){
	//System.out.println("overwriting ");
	//DataBank tempBank = event.createBank("PID::Electron",1);
	//tempBank.setInt("index",0,rec_i);
	//event.appendBank(tempBank);
	
	//System.out.println(" >> GETTING RESULT TO PUT INTO BANK ");
	if( event instanceof HipoDataEvent ){
	    //System.out.println(" >> 1 ");
	    /*
	    HipoDataEvent hipoEv = (HipoDataEvent) event;
	    //System.out.println(" >> 2 ");

	    //hipoEv.getHipoEvent().getSchemaFactory().addSchema("PID::electron","index/I");
	    hipoEv.getHipoEvent().getSchemaFactory().addSchema(new Schema("PDF::electron",300,"index/I"));
	    //System.out.println(" >> 3 ");

	    DataBank pidBank = event.createBank("PDF::electron",1);
	    //System.out.println(" >> 4 ");

	    pidBank.setInt("index",0,rec_i);
	    //System.out.println(" >> 5 ");

	    event.appendBank(pidBank);
	    ///System.out.println(" >> 6 ");
            */
	}
	//Systeam.out.println(" >> 7 ");

	//return event;      
    }

}
