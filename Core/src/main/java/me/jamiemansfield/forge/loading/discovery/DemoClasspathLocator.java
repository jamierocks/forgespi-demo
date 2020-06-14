/*
 * Copyright 2020 Jamie Mansfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.jamiemansfield.forge.loading.discovery;

import static me.jamiemansfield.forge.DemoLoaderConstants.METADATA_PATH;
import static net.minecraftforge.fml.loading.LogMarkers.CORE;

import net.minecraftforge.fml.loading.LibraryFinder;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.IModLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Locates Demo mods on the classpath, based on {@link net.minecraftforge.userdev.ClasspathLocator}.
 */
public abstract class DemoClasspathLocator extends AbstractJarFileLocator {

	private static final Logger log = LogManager.getLogger();

	private final BiFunction<Path, IModLocator, IModFile> factory;

	private Set<Path> modCoords;

	public DemoClasspathLocator(final BiFunction<Path, IModLocator, IModFile> factory) {
		this.factory = factory;
	}

	@Override
	public List<IModFile> scanMods() {
		final List<IModFile> mods = new ArrayList<>();
		for (final Path modPath : this.modCoords) {
			final IModFile modFile = this.factory.apply(modPath, this);
			this.modJars.compute(modFile, (mf, fs) -> createFileSystem(mf));
			mods.add(modFile);
		}
		return mods;
	}

	@Override
	public String name() {
		return "demo userdev classpath";
	}

	@Override
	public void initArguments(final Map<String, ?> arguments) {
		try {
			this.modCoords = new LinkedHashSet<>();
			this.locateMods(METADATA_PATH, "classpath_mod");
		} catch (IOException e) {
			log.fatal(CORE,"Error trying to find resources", e);
			throw new RuntimeException("wha?", e);
		}
	}

	private void locateMods(final String resource, final String name) throws IOException {
		final Enumeration<URL> modJsons = ClassLoader.getSystemClassLoader().getResources(resource);
		while (modJsons.hasMoreElements()) {
			final URL url = modJsons.nextElement();
			final Path path = LibraryFinder.findJarPathFor(resource, name, url);
			if (Files.isDirectory(path)) continue;

			log.debug(CORE, "Found classpath mod: {}", path);
			this.modCoords.add(path);
		}
	}

}
