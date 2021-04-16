#!/bin/bash

mvn install:install-file -Dfile="travart/lib/de.ovgu.featureide.lib.fm-v3.7.0.jar" -DgroupId="de.ovgu.featureide" -DartifactId="lib.fm" -Dversion="3.7.0" -Dpackaging="jar" &
mvn install:install-file -Dfile="travart/lib/de.ovgu.featureide.lib.fm-3.6.2.jar" -DgroupId="de.ovgu.featureide" -DartifactId="lib.fm" -Dversion="3.6.2" -Dpackaging="jar" &
mvn install:install-file -Dfile="travart/lib/uvl-parser-0.1.0-SNAPSHOT-standalone.jar" -DgroupId="de.neominik" -DartifactId="uvl" -Dversion="0.1.0-SNAPSHOT" -Dpackaging="jar" &
mvn install:install-file -Dfile="travart/lib/ppr-dsl-20210319.jar" -DgroupId="at.sqi.ppr" -DartifactId="ppr.dsl" -Dversion="0.0.2" -Dpackaging="jar" &
mvn install:install-file -Dfile="travart/lib/ppr-model-20210319.jar" -DgroupId="at.sqi.ppr" -DartifactId="ppr.model" -Dversion="0.0.2" -Dpackaging="jar"