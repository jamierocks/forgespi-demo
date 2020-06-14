package me.jamiemansfield.forge.demo;

import me.jamiemansfield.forge.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DemoMod implements Mod {

    public static final Logger log = LogManager.getLogger("Demo Mod");

    @Override
    public void setup() {
        log.info("Hello from Demo Mod!");
    }

}
