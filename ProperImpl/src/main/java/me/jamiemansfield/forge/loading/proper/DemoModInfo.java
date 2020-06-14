package me.jamiemansfield.forge.loading.proper;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DemoModInfo implements IModInfo {

    private static final Config EMPTY_CONFIG = Config.inMemory();

    private final DemoModFileInfo fileInfo;
    private final ModMetadata metadata;

    public DemoModInfo(final DemoModFileInfo fileInfo, final ModMetadata metadata) {
        this.fileInfo = fileInfo;
        this.metadata = metadata;
    }

    @Override
    public IModFileInfo getOwningFile() {
        return this.fileInfo;
    }

    @Override
    public String getModId() {
        return this.metadata.getId();
    }

    @Override
    public String getDisplayName() {
        return this.metadata.getName();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ArtifactVersion getVersion() {
        return new DefaultArtifactVersion(this.metadata.getVersion());
    }

    @Override
    public List<ModVersion> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public UnmodifiableConfig getModConfig() {
        return EMPTY_CONFIG;
    }

    @Override
    public String getNamespace() {
        return this.metadata.getId();
    }

    @Override
    public Map<String, Object> getModProperties() {
        return Collections.singletonMap("metadata", this.metadata);
    }

    @Override
    public URL getUpdateURL() {
        return null;
    }

}
