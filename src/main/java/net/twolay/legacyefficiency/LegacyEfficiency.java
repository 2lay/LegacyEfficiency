package net.twolay.legacyefficiency;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import java.util.logging.Logger;

public class LegacyEfficiency implements ModInitializer {
    Logger logger = Logger.getLogger(LegacyEfficiency.class.getName());
    public void onInitialize() {
        logger.info("[LF] LegacyEfficiency " + FabricLoader.getInstance().getModContainer("legacyefficiency").get().getMetadata().getVersion() + " is starting.");
    }
}
