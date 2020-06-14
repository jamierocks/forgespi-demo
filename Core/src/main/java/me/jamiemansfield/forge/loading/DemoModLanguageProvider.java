package me.jamiemansfield.forge.loading;

import static me.jamiemansfield.forge.DemoLoaderConstants.LOADER;
import static net.minecraftforge.fml.Logging.LOADING;

import me.jamiemansfield.forge.meta.ModMetadata;
import net.minecraftforge.forgespi.language.ILifecycleEvent;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DemoModLanguageProvider implements IModLanguageProvider {

    private static final Logger log = LogManager.getLogger();

    @Override
    public String name() {
        return LOADER;
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        return sd -> {
            final Map<String, IModLanguageLoader> mods = new HashMap<>();

            for (final IModFileInfo info : sd.getIModInfoData()) {
                for (final IModInfo mod : info.getMods()) {
                    mods.put(mod.getModId(), new Loader());
                }
            }

            sd.addLanguageLoader(mods);
        };
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(final Supplier<R> consumeEvent) {
    }

    private static class Loader implements IModLanguageLoader {

        @Override
        public <T> T loadMod(final IModInfo info, final ClassLoader modClassLoader, final ModFileScanData modFileScanResults) {
            final ModMetadata metadata = (ModMetadata) info.getModProperties().get("metadata");

            try {
                final Class<?> modClass = Class.forName("me.jamiemansfield.forge.loading.DemoModContainer", true, modClassLoader);
                return (T) modClass.getConstructor(IModInfo.class, ModMetadata.class, ClassLoader.class)
                        .newInstance(info, metadata, modClassLoader);
            }
            catch (final Throwable ex) {
                log.fatal(LOADING,"Unable to load DemoModContainer", ex);
                throw new RuntimeException(ex);
            }
        }

    }

}
