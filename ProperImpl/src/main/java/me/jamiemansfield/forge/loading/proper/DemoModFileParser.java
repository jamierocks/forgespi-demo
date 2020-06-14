package me.jamiemansfield.forge.loading.proper;

import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.fml.loading.LogMarkers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DemoModFileParser {

	private static final Logger log = LogManager.getLogger();

	public static DemoModFileInfo readModList(final DemoModFile modFile) {
		log.debug(LogMarkers.LOADING, "Considering mod file candidate {}", modFile.getFilePath());

		final Path metaPath = modFile.getLocator().findPath(modFile, "META-INF", "mod.properties");
		if (!Files.exists(metaPath)) {
			log.warn(LogMarkers.LOADING, "Mod file {} is missing mod.properties file", modFile);
			return null;
		}

		final Properties props = new Properties();

		try (final InputStream is = Files.newInputStream(metaPath)) {
			props.load(is);
		}
		catch (final IOException ex) {
			log.warn(LogMarkers.LOADING, "Failed to load mod.properties", ex);
			return null;
		}

		return new DemoModFileInfo(modFile, ModMetadata.from(props));
	}

}