package com.leokayasen.sentinel;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sentinel implements ModInitializer {
    public static final String MOD_ID = "sentinel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Sentinel mod initialized!");
    }
}