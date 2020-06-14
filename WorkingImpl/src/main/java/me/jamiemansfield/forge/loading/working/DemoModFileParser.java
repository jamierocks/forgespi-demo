package me.jamiemansfield.forge.loading.working;

import static me.jamiemansfield.forge.DemoLoaderConstants.LOADER;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DemoModFileParser {

	private static final Logger log = LogManager.getLogger();

	public static ModFileInfo readModList(final DemoModFile modFile) {
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

		return create(modFile, ModMetadata.from(props));
	}

	public static ModFileInfo create(final DemoModFile file, final ModMetadata metadata) {
		try {
			final Constructor<ModFileInfo> cstr = ModFileInfo.class.getDeclaredConstructor(ModFile.class, UnmodifiableConfig.class);
			cstr.setAccessible(true);
			return cstr.newInstance(file, createConfig(metadata));
		}
		catch (final Throwable ignored) {
			return null;
		}
	}

	public static UnmodifiableConfig createConfig(final ModMetadata metadata) {
		final Config config = Config.inMemory();
		config.set("modLoader", LOADER);
		config.set("loaderVersion", "[31,)");

		final List<UnmodifiableConfig> mods = new ArrayList<>();
		final Config modConfig = Config.inMemory();
		modConfig.set("modId", metadata.getId());
		modConfig.set("displayName", metadata.getName());
		modConfig.set("version", metadata.getVersion());
		mods.add(modConfig);

		final Config properties = Config.inMemory();
		properties.set("metadata", metadata);

		final Config modProperties = Config.inMemory();
		modProperties.set(metadata.getId(), properties);

		config.set("mods", mods);
		config.set("modproperties", modProperties);
		return config;
	}

}