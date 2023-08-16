/**
 * TODO: explanation what the class does
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
 */
package at.jku.cps.travart.core.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public final class FileUtils {

	private FileUtils() {

	}

	public static Set<Path> getPathSet(final Path path, final String extension) throws IOException {
		return getPathSetForLevel(path, extension, 1);
	}

	public static Set<Path> getPathSetForLevel(final Path path, final String extension, final int level)
			throws IOException {
		return Files.walk(path, level).filter(Files::isRegularFile)
				.filter(f -> f.getFileName().toString().endsWith(extension)).collect(Collectors.toSet());
	}
}
