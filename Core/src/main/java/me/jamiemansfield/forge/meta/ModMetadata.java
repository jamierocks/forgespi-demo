package me.jamiemansfield.forge.meta;

import java.util.Properties;

public class ModMetadata {

    public static ModMetadata from(final Properties props) {
        final String mainClass = props.getProperty("main-class");
        if (mainClass == null) {
            throw new IllegalArgumentException("main-class not specified by mod!");
        }

        final String id = props.getProperty("id");
        if (id == null) {
            throw new IllegalArgumentException("id not specified by mod!");
        }

        return new ModMetadata(
                mainClass,
                id,
                props.getProperty("name", id),
                props.getProperty("version", "unversioned")
        );
    }

    private final String mainClass;
    private final String id;
    private final String name;
    private final String version;

    public ModMetadata(final String mainClass, final String id, final String name, final String version) {
        this.mainClass = mainClass;
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

}
