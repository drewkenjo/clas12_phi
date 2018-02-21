#!/bin/bash

#javac -cp "src/com/FileManager.class:/lustre/expphy/work/halla/sbs/bclary/extras/coatjava_4a.8.1/lib/clas/coat-libs-3.0-SNAPSHOT.jar" phiAnalysis.java

#############
##
##  $2 is the analysis type and $3 is the run number
##

if [ "$1" = "PID"  ] || [ "$1" = "CLAS12" ]
then
 	#javac -cp /lustre/expphy/work/halla/sbs/bclary/extras/coatjava_4a.8.2/lib/clas/coat-libs-4.0-SNAPSHOT.jar -d . -sourcepath . phiAnalysis.java
	#java -cp ".:src/com/*.class:/lustre/expphy/work/halla/sbs/bclary/extras/coatjava_4a.8.2/lib/clas/coat-libs-4.0-SNAPSHOT.jar" phiAnalysis $2
 	#javac -cp /lustre/expphy/work/halla/sbs/bclary/extras/myClara/plugins/clas12/lib/clas/coat-libs-5.0-SNAPSHOT.jar -d . -sourcepath . phiAnalysis.java
	javac -cp /w/halla-scifs17exp/sbs/bclary/extras/myClara/plugins/clas12/lib/clas/coat-libs-5.0-SNAPSHOT.jar -d . -sourcepath . phiAnalysis.java
	java -cp ".:src/com/*.class:/w/halla-scifs17exp/sbs/bclary/extras/myClara/plugins/clas12/lib/clas/coat-libs-5.0-SNAPSHOT.jar" phiAnalysis $2 $3
#	java -cp ".:src/com/*.class:/lustre/expphy/work/halla/sbs/bclary/extras/myClara/plugins/clas12/lib/clas/coat-libs-5.0-SNAPSHOT.jar" phiAnalysis $2 $3

elif [ "$1" = "PHYS" ]
then
	javac -cp /lustre/expphy/work/halla/sbs/bclary/extras/coatjava_4a.8.2/lib/clas/coat-libs-4.0-SNAPSHOT.jar -d . -sourcepath . physicsAnalysis.java
	java -cp ".:src/com/*.class:/lustre/expphy/work/halla/sbs/bclary/extras/coatjava_4a.8.2/lib/clas/coat-libs-4.0-SNAPSHOT.jar" physicsAnalysis $2 $3
fi
