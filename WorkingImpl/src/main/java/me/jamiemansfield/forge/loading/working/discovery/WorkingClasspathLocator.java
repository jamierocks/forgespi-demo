package me.jamiemansfield.forge.loading.working.discovery;

import me.jamiemansfield.forge.loading.discovery.DemoClasspathLocator;
import me.jamiemansfield.forge.loading.working.DemoModFile;

public class WorkingClasspathLocator extends DemoClasspathLocator {

    public WorkingClasspathLocator() {
        super(DemoModFile::new);
    }

}
