package com.italiandudes.teriacoinmod.handler;

import com.italiandudes.teriacoinmod.command.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public final class RegistryHandler {

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    public static void preInitRegistries(FMLPreInitializationEvent event){
        ConfigHandler.registerConfig(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    public static void initRegistries(){

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    public static void postInitRegistries(){

    }

    public static void serverRegistries(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandTeriaRegister());
        event.registerServerCommand(new CommandTeriaLogin());
        event.registerServerCommand(new CommandTeriaLogout());
        event.registerServerCommand(new CommandTeriaBalance());
        event.registerServerCommand(new CommandTeriaSend());
        event.registerServerCommand(new CommandTeriaExchangeTC());
        event.registerServerCommand(new CommandTeriaExchangeItem());
        event.registerServerCommand(new CommandTeriaList());
    }
}
