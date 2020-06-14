package me.jamiemansfield.forge.loading.proper;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.IModLocator;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DemoModFile implements IModFile {

    private final Path path;
    private final IModLocator locator;
    private final IModFileInfo modFileInfo;

    private IModLanguageProvider loader;

    public DemoModFile(final Path path, final IModLocator locator) {
        this.path = path;
        this.locator = locator;
        this.modFileInfo = DemoModFileParser.readModList(this);
    }

    @Override
    public IModLanguageProvider getLoader() {
        if (this.loader == null) {
            // TODO: Forge limitation, unable to get language provider
            this.loader = FMLLoader.getLanguageLoadingProvider().findLanguage(null, this.modFileInfo.getModLoader(), this.modFileInfo.getModLoaderVersion());
        }
        return this.loader;
    }

    @Override
    public Path findResource(final String className) {
        return this.locator.findPath(this, className);
    }

    @Override
    public Supplier<Map<String, Object>> getSubstitutionMap() {
        return Collections::emptyMap;
    }

    @Override
    public Type getType() {
        return Type.MOD;
    }

    @Override
    public Path getFilePath() {
        return this.path;
    }

    @Override
    public List<IModInfo> getModInfos() {
        return this.modFileInfo.getMods();
    }

    @Override
    public ModFileScanData getScanResult() {
        return null;
    }

    @Override
    public String getFileName() {
        return this.path.getFileName().toString();
    }

    @Override
    public IModLocator getLocator() {
        return this.locator;
    }

    @Override
    public IModFileInfo getModFileInfo() {
        return this.modFileInfo;
    }

}
