package at.jku.cps.travart.ppr.dsl.io.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.core.io.FeatureModelXMLWriter;
import at.jku.cps.travart.ppr.dsl.io.PprDslReader;
import at.jku.cps.travart.ppr.dsl.io.PprDslWriter;
import at.jku.cps.travart.ppr.dsl.transformation.FeatureModelToPprDslTransformer;
import at.jku.cps.travart.ppr.dsl.transformation.PprDslToFeatureModelTransformer;
import at.sqi.ppr.model.AssemblySequence;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

public final class TestTransformToFeatureModel {

	public static void main(final String[] args) {
		try {
			Path dslPath = Paths.get(new TestTransformToFeatureModel().getClass().getClassLoader()
					.getResource("rocker-switches.dsl").toURI());
			Path output = Paths.get("fm_rocker_switches_intermediate.xml");
			Path roundtripModelPath = Paths.get("dsl_rocker_switches_roundtrip.dsl");

			PprDslReader reader = new PprDslReader();
			AssemblySequence asq = reader.read(dslPath);

			PprDslToFeatureModelTransformer pprToFm = new PprDslToFeatureModelTransformer();
			IFeatureModel fm = pprToFm.transform(asq);

			FeatureModelXMLWriter xmlWriter = new FeatureModelXMLWriter();
			xmlWriter.write(fm, output);

			FeatureModelToPprDslTransformer fmToPpr = new FeatureModelToPprDslTransformer();
			AssemblySequence roundtripAsq = fmToPpr.transform(fm);

			PprDslWriter dslWriter = new PprDslWriter();
			dslWriter.write(roundtripAsq, roundtripModelPath);
		} catch (URISyntaxException | IOException | NotSupportedVariablityTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
