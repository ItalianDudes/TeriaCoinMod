package com.italiandudes.teriacoinmod;

import com.italianDudes.idl.common.Peer;
import com.italiandudes.teriacoinmod.handler.RegistryHandler;
import com.italiandudes.teriacoinmod.proxy.CommonProxy;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;

@Mod(modid = Defs.MOD_ID, name = Defs.MOD_NAME, version = Defs.VERSION)
public final class TeriaCoinMod {

    //Attributes
    public static File configs;
    public static Peer serverConnection = null;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(Defs.MOD_ID) @SuppressWarnings("unused")
    public static TeriaCoinMod INSTANCE;

    @SidedProxy(clientSide = Defs.CLIENT, serverSide = Defs.COMMON) @SuppressWarnings("unused")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        RegistryHandler.preInitRegistries(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RegistryHandler.initRegistries();
    }
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        RegistryHandler.postInitRegistries();
    }
    @Mod.EventHandler
    public static void serverinit(FMLServerStartingEvent event) {
        RegistryHandler.serverRegistries(event);
    }

    public static void clearPeer(){
        try{
            serverConnection.getPeerSocket().close();
        }catch (IOException ignored){}
        serverConnection = null;
    }
}
