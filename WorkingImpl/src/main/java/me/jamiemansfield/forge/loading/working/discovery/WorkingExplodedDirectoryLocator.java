package me.jamiemansfield.forge.loading.working.discovery;

import me.jamiemansfield.forge.loading.discovery.DemoExplodedDirectoryLocator;
import me.jamiemansfield.forge.loading.working.DemoModFile;

public class WorkingExplodedDirectoryLocator extends DemoExplodedDirectoryLocator {

    public WorkingExplodedDirectoryLocator() {
        super(DemoModFile::new);
    }

}
