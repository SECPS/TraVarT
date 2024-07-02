/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Command line tool command transform a variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.cps.travart.core.common.IModelTransformer;
import at.jku.cps.travart.core.common.IModelTransformer.STRATEGY;
import at.jku.cps.travart.core.common.IPlugin;
import at.jku.cps.travart.core.common.IDeserializer;
import at.jku.cps.travart.core.common.ISerializer;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.exception.TransformationException;
import at.jku.cps.travart.core.helpers.TraVarTPluginManager;
import at.jku.cps.travart.core.io.FileUtils;
import at.jku.cps.travart.core.io.UVLDeserializer;
import at.jku.cps.travart.core.io.UVLSerializer;
import de.vill.model.FeatureModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@SuppressWarnings("rawtypes")
@Command(name = "transform", version = "0.0.1", description = "Transforms the given variability artifacts into another type.")
public class TransformCommand implements Callable<Integer> {

	private static final Logger LOGGER = LogManager.getLogger(TransformCommand.class);

	private static final String CORE_MODEL_UVL = "UVL";

	private static String toStringList(final Iterable<String> fileExtensions) {
		StringBuilder builder = new StringBuilder();
		builder.append("{ ");
		for (String extension : fileExtensions) {
			builder.append(extension).append(",");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append(" }");
		return builder.toString();
	}

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

//	@Option(names = { "-validate",
//			"--validate" }, description = "Validate the resulting variability artifact as with the validate command.")
//	private boolean validate;

	private IDeserializer deserializer;
	private ISerializer serializer;
	private final Queue<IModelTransformer> transformers = new LinkedList<>();

	private boolean startUVL = false;

	@Override
	public Integer call() throws Exception {
		assert sourcePath != null;
		assert sourceType != null;
		assert targetPath != null;
		assert targetType != null;
		LOGGER.debug("Verify parameters...");
		if (sourceType.equalsIgnoreCase(targetType)) {
			LOGGER.error("Source and Target type are equal, no transformation needed");
			return 1;
		}
		LOGGER.debug("Verify the given paths...");
		if (!(Files.isRegularFile(sourcePath) || Files.isDirectory(sourcePath))) {
			LOGGER.error("Given source path is not a valid one!");
			return 2;
		}
		if (!(Files.isRegularFile(targetPath) || Files.isDirectory(targetPath))) {
			if (Files.exists(targetPath)) {
				LOGGER.error("Given target path is not a valid one!");
				return 3;
			}
			targetPath.toFile().mkdirs();
		}
		// start collecting necessary information for transformations
		LOGGER.debug("Initialize transformations...");
		int init = initializeTransformations();
		if (init != 0) {
			LOGGER.error("Unable to initialize plugins! Check installed plugins using command \"plugin\".");
			return 4;
		}
		// do the transformations
		LOGGER.debug("Starting trasnforming variability artifacts...");
		try {
			if (Files.isRegularFile(sourcePath)) {
				return transformSingleFile(sourcePath);
			}
			return transformDirectory();
		} catch (IOException | NotSupportedVariabilityTypeException ex) {
			LOGGER.error("Error while handling files...");
			LOGGER.error(ex.toString());
			throw new TransformationException(ex);
		}
	}

	private int initializeTransformations() {
		if (CORE_MODEL_UVL.equalsIgnoreCase(sourceType)) {
			LOGGER.debug("Deteced source type UVL...");
			deserializer = new UVLDeserializer();
			startUVL = true;
		} else {
			IPlugin plugin = findPlugin(sourceType);
			if (plugin == null) {
				LOGGER.error("Could not find plugin for given source type!");
				return 1;
			}
			LOGGER.debug(String.format("Deteced source type %s...", plugin.getName()));
			deserializer = plugin.getDeserializer();
			transformers.add(plugin.getTransformer());
		}
		if (CORE_MODEL_UVL.equalsIgnoreCase(targetType)) {
			LOGGER.debug("Deteced target type UVL...");
			serializer = new UVLSerializer();
		} else {
			IPlugin plugin = findPlugin(targetType);
			if (plugin == null) {
				LOGGER.error("Could not find plugin for given target type!");
				return 2;
			}
			LOGGER.debug(String.format("Deteced target type %s...", plugin.getName()));
			serializer = plugin.getSerializer();
			transformers.add(plugin.getTransformer());
		}
		return 0;
	}

	private static IPlugin findPlugin(final String type) {
		LOGGER.debug(String.format("Try to find plugin for type %s...", type));
		Optional<IPlugin> plugin = TraVarTPluginManager.getAvailablePlugins().values().stream()
				.filter(v -> v.getName().equalsIgnoreCase(type)).findFirst();
		if (plugin.isPresent()) {
			return plugin.get();
		}
		return null;
	}

	private Integer transformDirectory() throws IOException, NotSupportedVariabilityTypeException {
		Set<Path> files = new HashSet<>();
		LOGGER.debug(String.format("Collect files of type %s...", toStringList(deserializer.fileExtensions())));
		for (Object elem : deserializer.fileExtensions()) {
			String extension = (String) elem;
			Set<Path> filesFound = FileUtils.getPathSet(sourcePath, extension);
			files.addAll(filesFound);
			LOGGER.debug(String.format("%d files with extension %s found...", filesFound.size(), extension));
		}
		LOGGER.debug(String.format("%d files to transform...", files.size()));
		for (Path file : files) {
			int result = transformSingleFile(file);
			if (result != 0) {
				LOGGER.error(String.format("Error during transfomration of file %s...", file.getFileName()));
				return result;
			}
		}
		return 0;
	}

	private Integer transformSingleFile(final Path file) throws IOException, NotSupportedVariabilityTypeException {
		LOGGER.debug(String.format("Start transforming file %s...", file.getFileName()));
		Object model = deserializer.deserializeFromFile(file);
		Object newModel = model;
		boolean intermediate = false;
		for (IModelTransformer transformer : transformers) {
			if (startUVL || intermediate) {
				FeatureModel fm = (FeatureModel) newModel;
				newModel = transformer.transform(fm, file.getFileName().toString(), STRATEGY.ROUNDTRIP);
				intermediate = false;
			} else {
				newModel = transformer.transform(model, file.getFileName().toString(), STRATEGY.ROUNDTRIP);
				intermediate = true;
			}
		}
		Path newPath = targetPath.resolve(file.getFileName() + serializer.getFileExtension());
		LOGGER.debug(String.format("Write transformed file to %s...", newPath.toAbsolutePath()));
		serializer.serializeToFile(newModel, newPath);
//		if (validate) {
//			LOGGER.debug("Validate the transformed model...");
//			// TODO validate newModel with model
//		}
		return 0;
	}
}
