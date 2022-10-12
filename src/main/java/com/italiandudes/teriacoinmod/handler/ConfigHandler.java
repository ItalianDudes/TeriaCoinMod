package com.italiandudes.teriacoinmod.handler;

import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public final class ConfigHandler {

    //Attributes
    public static Configuration config;

    //Default Values
    private static final String DEFAULT_DOMAIN = "127.0.0.1";
    private static final int DEFAULT_PORT = 25000;

    //Values
    public static String serverAddress = DEFAULT_DOMAIN;
    public static int serverPort = DEFAULT_PORT;

    public static void init(File file){
        config = new Configuration(file);

        String category;
        category = "Connection";
        config.addCustomCategoryComment(category, "Set the info needed to connect to the external server");
        serverAddress = config.getString("serverDomain", category, DEFAULT_DOMAIN, "Domain of the server");
        serverPort = config.getInt("serverPort", category, DEFAULT_PORT, 0, 65535, "Port of the server");

        config.save();
    }

    public static void registerConfig(FMLPreInitializationEvent event){
        TeriaCoinMod.configs = new File(event.getModConfigurationDirectory() + "/" + Defs.MOD_ID);
        //noinspection ResultOfMethodCallIgnored
        TeriaCoinMod.configs.mkdirs();
        init(new File(TeriaCoinMod.configs.getPath(), Defs.MOD_ID+".cfg"));
    }

}
