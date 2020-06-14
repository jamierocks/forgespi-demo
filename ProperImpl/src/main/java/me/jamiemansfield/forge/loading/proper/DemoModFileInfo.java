package me.jamiemansfield.forge.loading.proper;

import static me.jamiemansfield.forge.DemoLoaderConstants.LOADER;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DemoModFileInfo implements IModFileInfo {

    private static final Config EMPTY_CONFIG = Config.inMemory();
    private static final VersionRange FORGE_RANGE = VersionRange.createFromVersion("[31,)");

    private final DemoModInfo modInfo;

    public DemoModFileInfo(final DemoModFile modFile, final ModMetadata metadata) {
        this.modInfo = new DemoModInfo(this, metadata);
    }

    @Override
    public List<IModInfo> getMods() {
        return Collections.singletonList(this.modInfo);
    }

    @Override
    public UnmodifiableConfig getConfig() {
        return EMPTY_CONFIG;
    }

    @Override
    public String getModLoader() {
        return LOADER;
    }

    @Override
    public VersionRange getModLoaderVersion() {
        return FORGE_RANGE;
    }

    @Override
    public boolean showAsResourcePack() {
        return false;
    }

    @Override
    public Map<String, Object> getFileProperties() {
        return Collections.emptyMap();
    }

}
