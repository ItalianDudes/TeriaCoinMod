package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaLogout extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "terialogout";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "terialogout";
    }
    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) {

        if(args.length != 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

        if(TeriaCoinMod.serverConnection == null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnection, Defs.TeriaProtocols.TERIA_LOGOUT);
            TeriaCoinMod.clearPeer();
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Logout completed"));

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer();
        }

    }

}
