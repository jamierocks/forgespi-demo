package me.jamiemansfield.forge.loading;

import me.jamiemansfield.forge.Mod;
import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.fml.LifecycleEventProvider;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mod container for the Demo mod loader, must be created WITH THE CORRECT
 * CLASS LOADER.
 */
public class DemoModContainer extends ModContainer {

    public static final Logger log = LogManager.getLogger();

    private final ModMetadata metadata;
    private final ClassLoader classLoader;

    private Mod instance;

    public DemoModContainer(final IModInfo info, final ModMetadata metadata, final ClassLoader classLoader) {
        super(info);
        this.metadata = metadata;
        this.classLoader = classLoader;

        this.contextExtension = () -> null;
        this.triggerMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
    }

    private void constructMod(final LifecycleEventProvider.LifecycleEvent event) {
        log.debug("Loading {} of type {}", this.metadata.getId(), this.metadata.getMainClass());

        final Class<?> modClass;
        try {
             modClass = this.classLoader.loadClass(this.metadata.getMainClass());
        }
        catch (final ClassNotFoundException ex) {
            throw new ModLoadingException(this.modInfo, event.fromStage(), "demoloader.failed_to_find_mod_class", ex);
        }

        try {
            this.instance = (Mod) modClass.newInstance();
        }
        catch (final Throwable ex) {
            throw new ModLoadingException(this.modInfo, event.fromStage(), "demoloader.failed_to_create_instance", ex);
        }
    }

    @Override
    public boolean matches(final Object mod) {
        return this.instance == mod;
    }

    @Override
    public Mod getMod() {
        return this.instance;
    }

}
