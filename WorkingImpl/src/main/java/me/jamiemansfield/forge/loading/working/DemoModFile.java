package me.jamiemansfield.forge.loading.working;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.moddiscovery.CoreModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.locating.IModLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DemoModFile extends ModFile {

    private static final Logger log = LogManager.getLogger();

    private IModLanguageProvider loader;
    private ModFileInfo modFileInfo;

    public DemoModFile(final Path path, final IModLocator locator) {
        super(path, locator);
    }

    @Override
    public Supplier<Map<String, Object>> getSubstitutionMap() {
        return () -> ImmutableMap.<String, Object>builder()
                .build();
    }

    @Override
    public Type getType() {
        return Type.MOD;
    }

    @Override
    public void identifyLanguage() {
        this.loader = FMLLoader.getLanguageLoadingProvider().findLanguage(this, this.modFileInfo.getModLoader(), this.modFileInfo.getModLoaderVersion());
    }

    @Override
    public IModLanguageProvider getLoader() {
        return this.loader;
    }

    @Override
    public List<IModInfo> getModInfos() {
        return this.modFileInfo.getMods();
    }

    @Override
    public boolean identifyMods() {
        this.modFileInfo = DemoModFileParser.readModList(this);
        if (this.modFileInfo == null) return false;

        log.debug(LogMarkers.LOADING, "Loading mod file {} with language {}", this.getFilePath(), this.modFileInfo.getModLoader());
        return true;
    }

    @Override
    public IModFileInfo getModFileInfo() {
        return this.modFileInfo;
    }

    @Override
    public List<CoreModFile> getCoreMods() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Path> getAccessTransformer() {
        return Optional.empty();
    }

}
