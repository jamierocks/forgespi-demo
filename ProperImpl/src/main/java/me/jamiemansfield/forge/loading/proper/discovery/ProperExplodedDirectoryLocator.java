package me.jamiemansfield.forge.loading.proper.discovery;

import me.jamiemansfield.forge.loading.discovery.DemoExplodedDirectoryLocator;
import me.jamiemansfield.forge.loading.proper.DemoModFile;

public class ProperExplodedDirectoryLocator extends DemoExplodedDirectoryLocator {

    public ProperExplodedDirectoryLocator() {
        super(DemoModFile::new);
    }

}
