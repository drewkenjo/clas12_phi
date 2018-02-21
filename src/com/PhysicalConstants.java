package src.com;

import java.io.*;
import src.com.RunPropertiesLoader;

public class PhysicalConstants{

    //Values are in meters, seconds, and energy is GeV

    public static double eBeam =  RunPropertiesLoader.getBeamEnergy();
    //System.out.println(" >> eBEAM " + eBeam );

    public static double mass_phi = 1.01946;
    public static double mass_kaon = 0.49368;
    public static double mass_electron = 0.00051;
    public static double mass_proton = 0.93827;
    public static double mass_pion = 0.1395;


    //VALUES FOR THE POSITION OF VARIOUS PARTICLE TYPES IN THE MC::PARTICLE BANK
    public static int el_index = 0;
    public static int pr_index = 1;
    public static int kp_index = 2;    

    /////////////////////////////////
    //SPEED OF LIGHT IN m/ns
    public static double speedOfLight = 29.9792;

    /////////////////////////////////
    //PDG IDS
    public static int electronID = 11;
    public static int protonID = 2212;
    public static int kaonplusID = 411;
    public static int kaonminusID = -411;

}
