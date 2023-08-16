/**
 * The command transforms a given variability artifact into a different one.
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
*/
package at.jku.cps.travart.core.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.cps.travart.core.common.IModelTransformer;
import at.jku.cps.travart.core.common.IPlugin;
import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.common.IWriter;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.exception.TransformationException;
import at.jku.cps.travart.core.helpers.TraVarTPluginManager;
import at.jku.cps.travart.core.io.FileUtils;
import at.jku.cps.travart.core.io.UVLReader;
import at.jku.cps.travart.core.io.UVLWriter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@SuppressWarnings("rawtypes")
@Command(name = "transform", version = "0.0.1", description = "Transforms the given variability artifacts into another type.")
public class TransformCommand implements Callable<Integer> {

	private static final Logger LOGGER = LogManager.getLogger(TransformCommand.class);

	private static final String CORE_MODEL_UVL = "UVL";
	private static final String CORE_MODEL_FEATURE_IDE = "FeatureIDE";

	@Parameters(index = "0", description = "The source path to the variability artifact to transform. If the path is a folder, each variability artifact of the given type (-soureType) is transformed.")
	private Path sourcePath;

	@Parameters(index = "1", description = "The output path to which the variability artifact is transformed. If the source is given as a folder, this parameter must be a folder too.")
	private Path targetPath;

	@Option(names = { "-st", "-sourceType", "--st",
			"--sourceType" }, required = true, description = "The mandatory type of the source variability artifacts, as listed in the plugin command.")
	private String sourceType;

	@Option(names = { "-tt", "-targetType", "--tt",
			"--targetType" }, required = true, description = "The mandatory target type of the transformed variability artifacts, as listed in the plugin command.")
	private String targetType;

	@Option(names = {
			"--validate" }, description = "Validate the resulting variability artifact as with the validate command.")
	private boolean validate;

	private IReader reader;
	private IWriter writer;
	private final Queue<IModelTransformer> transformers = new LinkedList<>();

	@Override
	public Integer call() throws Exception {
		assert sourcePath != null;
		assert sourceType != null;
		assert targetPath != null;
		assert targetType != null;
		LOGGER.debug("Verify parameters...");
		if (sourceType.equalsIgnoreCase(targetType)) {
			LOGGER.error("Source and Target type are equal, no transformation needed");
			return -1;
		}
		LOGGER.debug("Verify the given folders");
		if (!(Files.isRegularFile(sourcePath) || Files.isDirectory(sourcePath))) {
			LOGGER.error("Given source path is not a valid one!");
			return -2;
		}
		if (!(Files.isRegularFile(targetPath) || Files.isDirectory(targetPath))) {
			LOGGER.error("Given target path is not a valid one!");
			return -3;
		}
		// start collecting necessary information for transformations
		LOGGER.debug("Initialize transformations...");
		initializeTransformations();
		// do the transformations
		LOGGER.debug("Starting trasnforming variability artifacts...");
		try {
			if (Files.isRegularFile(sourcePath)) {
				return transformSingleFile(sourcePath);
			}
			return transformDirectory();
		} catch (IOException | NotSupportedVariabilityTypeException ex) {
			LOGGER.debug("Error while handling files...");
			throw new TransformationException(ex);
		}
	}

	private void initializeTransformations() {
		if (CORE_MODEL_UVL.equalsIgnoreCase(sourceType) || CORE_MODEL_FEATURE_IDE.equalsIgnoreCase(sourceType)) {
			reader = new UVLReader();
		} else {
			String pluginKey = TraVarTCommand.nameToKeyMap().get(sourceType);
			IPlugin plugin = TraVarTPluginManager.getAvailablePlugins().get(pluginKey);
			reader = plugin.getReader();
			transformers.add(plugin.getTransformer());
		}
		if (CORE_MODEL_UVL.equalsIgnoreCase(targetType) || CORE_MODEL_FEATURE_IDE.equalsIgnoreCase(targetType)) {
			writer = new UVLWriter();
		} else {
			String pluginKey = TraVarTCommand.nameToKeyMap().get(targetType);
			IPlugin plugin = TraVarTPluginManager.getAvailablePlugins().get(pluginKey);
			writer = plugin.getWriter();
			transformers.add(plugin.getTransformer());
		}
	}

	private Integer transformDirectory() throws IOException, NotSupportedVariabilityTypeException {
		Set<Path> files = new HashSet<>();
		for (Object elem : reader.fileExtensions()) {
			String extension = (String) elem;
			files.addAll(FileUtils.getPathSet(sourcePath, extension));
		}
		for (Path file : files) {
			int result = transformSingleFile(file);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	private Integer transformSingleFile(final Path file) throws IOException, NotSupportedVariabilityTypeException {
		Object model = reader.read(file);
		Object newModel = model;
		for (IModelTransformer transformer : transformers) {
			newModel = transformer.transform(newModel);
		}
		Path newPath = targetPath.resolve(file.getFileName() + writer.getFileExtension());
		writer.write(newModel, newPath);
		return 0;
	}
}
