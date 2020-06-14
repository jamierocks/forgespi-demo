package me.jamiemansfield.forge.loading.discovery;

import static me.jamiemansfield.forge.DemoLoaderConstants.METADATA_PATH;
import static net.minecraftforge.fml.loading.LogMarkers.LOADING;
import static net.minecraftforge.fml.loading.LogMarkers.SCAN;

import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.IModLocator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.jar.Manifest;
import java.util.stream.Stream;

/**
 * Locates Demo mods in a directory, based on {@link net.minecraftforge.fml.loading.moddiscovery.ExplodedDirectoryLocator}.
 */
public abstract class DemoExplodedDirectoryLocator implements IModLocator {

    private static final Logger log = LogManager.getLogger();

    private final BiFunction<Path, IModLocator, IModFile> factory;
    private final List<Pair<Path, List<Path>>> rootDirs = new ArrayList<>();
    private final Map<IModFile, Pair<Path,List<Path>>> mods = new HashMap<>();

    public DemoExplodedDirectoryLocator(final BiFunction<Path, IModLocator, IModFile> factory) {
        this.factory = factory;
    }

    @Override
    public List<IModFile> scanMods() {
        // Collect all the mod.properties files used
        this.rootDirs.forEach(pathPathPair -> {
            final Path resources = pathPathPair.getLeft();
            final Path modJson = resources.resolve(METADATA_PATH);

            if (Files.exists(modJson)) {
                log.debug(LOADING, "Found exploded directory mod manifest at {}", modJson.toString());

                final IModFile modFile = this.factory.apply(pathPathPair.getLeft(), this);
                this.mods.put(modFile, pathPathPair);
            }
            else {
                log.warn(LOADING, "Failed to find exploded resource mod.properties in directory {}", resources.toString());
            }
        });
        return new ArrayList<>(this.mods.keySet());
    }

    @Override
    public String name() {
        return "demo exploded directory";
    }

    @Override
    public Path findPath(final IModFile modFile, final String... path) {
        if (path.length < 1) {
            throw new IllegalArgumentException("Missing path");
        }
        final Path target = Paths.get(path[0], Arrays.copyOfRange(path, 1, path.length));
        // try right path first (resources)
        final Path found = this.mods.get(modFile).getLeft().resolve(target);
        if (Files.exists(found)) return found;
        // then try left path (classes)
        return this.mods.get(modFile).getRight().stream()
                .map(p->p.resolve(target))
                .filter(Files::exists)
                .findFirst().orElse(found.resolve(target));
    }

    @Override
    public void scanFile(final IModFile modFile, final Consumer<Path> pathConsumer) {
        log.debug(SCAN,"Scanning exploded directory {}", modFile.getFilePath().toString());
        final Pair<Path, List<Path>> pathPathPair = this.mods.get(modFile);
        // classes are in the right branch of the pair
        pathPathPair.getRight().forEach(path -> scanIndividualPath(path, pathConsumer));
        log.debug(SCAN,"Exploded directory scan complete {}", pathPathPair.getLeft().toString());
    }

    private void scanIndividualPath(final Path path, Consumer<Path> pathConsumer) {
        log.debug(SCAN, "Scanning exploded target {}", path.toString());
        try (final Stream<Path> files = Files.find(path, Integer.MAX_VALUE, (p, a) -> p.getNameCount() > 0 && p.getFileName().toString().endsWith(".class"))) {
            files.forEach(pathConsumer);
        }
        catch (final IOException ex) {
            log.error(SCAN,"Exception scanning {}", path, ex);
        }
    }

    @Override
    public Optional<Manifest> findManifest(final Path file) {
        return Optional.empty();
    }

    @Override
    public void initArguments(final Map<String, ?> arguments) {
        final List<Pair<Path, List<Path>>> explodedTargets = ((Map<String, List<Pair<Path, List<Path>>>>) arguments).get("explodedTargets");
        if (explodedTargets != null && !explodedTargets.isEmpty()) {
            this.rootDirs.addAll(explodedTargets);
        }
    }

    @Override
    public boolean isValid(final IModFile modFile) {
        return this.mods.get(modFile) != null;
    }

}
