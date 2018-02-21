package src.com;
import com.google.gson.*;

import src.com.RunParameters;
import java.io.*;
import java.util.*;

public class RunInformation{

    public Map<String,RunParameters> run_info;
    

    public RunInformation(){
    }

    public Map<String,RunParameters> getRunParameters(){
	return run_info;
    }

}
