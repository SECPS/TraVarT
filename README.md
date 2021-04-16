# TraVarT

Transforming Variability Artifacts (TraVart) is a tool meant to enable users to transfer commonly used variability models into one another. This should help users to gain understanding between the limitations of each modeling approach, experiment with new approaches, or maybe change existing models into a more desirable format. The tool currently features support for FeatureIDE feature models (which are currently used as a pivot model), OVM models and DOPLER decision models. The software features conversion from each of those models into one another, including a full roundtrip which should maintain all possible configurations.

## Transformation Algorithms
The main transformations performed by the transformation algorithms of TraVarT are outlined [here](https://github.com/SECPS/TraVarT/wiki/Algorithms).

## Transformation Examples
Find [here](https://github.com/SECPS/TraVarT/tree/develop/splc/at.jku.cps.vmt.evaluation) example models we transformed. Source models are in the folders "FM", "FMBigData", "DM" and "OVM". Transformed models can be found in the folder "Transformed".

# Using TraVarT
As we are still in development, there is currently no GUI available. We plan to include a GUI in the future to enable users to easily transform their models.

# Developing TraVarT ![Maven build](https://github.com/SECPS/TraVarT/workflows/Maven%20build/badge.svg)
TraVarT is built with Maven. If you don't have any experience with Maven, you can fork the project and use the GitHub Action that is set up for the project to build TraVart. If you want to build TraVarT locally, you should know, that we depend on a featureIDE library which is not available through the standard repositories. Therefore before you run the build for the first time, you need to install that dependency in your local maven repository. You can do that by either running the setup.sh for Linux systems or setup.bat for Windows. Alternatively head to the root folder of the project in a shell and run: 

``
mvn install:install-file -Dfile="travart/lib/de.ovgu.featureide.lib.fm-v3.7.0.jar" -DgroupId="de.ovgu.featureide" -DartifactId="lib.fm" -Dversion="3.7.0" -Dpackaging="jar"
``

``
mvn install:install-file -Dfile="travart/lib/de.ovgu.featureide.lib.fm-3.6.2.jar" -DgroupId="de.ovgu.featureide" -DartifactId="lib.fm" -Dversion="3.6.2" -Dpackaging="jar"
``

``
mvn install:install-file -Dfile="travart/lib/uvl-parser-0.1.0-SNAPSHOT-standalone.jar" -DgroupId="de.neominik" -DartifactId="uvl" -Dversion="0.1.0-SNAPSHOT" -Dpackaging="jar"
``

``
mvn install:install-file -Dfile="travart/lib/ppr-dsl-20210319.jar" -DgroupId="at.sqi.ppr" -DartifactId="ppr.dsl" -Dversion="0.0.2" -Dpackaging="jar"
``

``
mvn install:install-file -Dfile="travart/lib/ppr-model-20210319.jar" -DgroupId="at.sqi.ppr" -DartifactId="ppr.model" -Dversion="0.0.2" -Dpackaging="jar"
``

After that you should be able to build TraVarT in 2 different versions: 

For the version that supports ATL you have to run:

``
mvn clean package -P atl
``

For the version that supports UVL you have to run:
``
mvn clean package -P uvl
``

If the installation process fails due to failing tests, and you don't care, add the `-DskipTests` flag.

Mind though, that since we have not implemented a GUI yet, you will not receive a simple runnable from the build, but you will only find the binary as JAR files in the corresponding target folders of each project. Once we implement a GUI we are also planning to provide TraVarT as an executable.

# Contributors
[Kevin Feichtinger](https://github.com/coemgen1992) 

[Dario Romano](https://gist.github.com/DarioRomano) 

[joshi1993](https://github.com/joshi1993) 

[Rick Rabiser](https://github.com/RickRabiser) 
