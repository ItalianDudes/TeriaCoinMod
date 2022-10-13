package com.italiandudes.teriacoinmod.command;

import com.italianDudes.idl.common.Credential;
import com.italianDudes.idl.common.Peer;
import com.italianDudes.idl.common.Serializer;
import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.exception.socket.InvalidProtocolException;
import com.italiandudes.teriacoinmod.handler.ConfigHandler;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public final class CommandTeriaLogin extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "terialogin";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "terialogin <username> <password>";
    }
    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) {

        if(args.length != 2){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

        String username = args[0];
        String password = args[1];

        if(TeriaCoinMod.serverConnection != null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There is already an open connection with the server, disconnect first"));
            return;
        }

        try {

            Socket serverConnection = new Socket(ConfigHandler.serverAddress, ConfigHandler.serverPort);

            Credential userCredential = new Credential(username, password);

            TeriaCoinMod.serverConnection = new Peer(serverConnection, userCredential);

            Serializer.sendString(TeriaCoinMod.serverConnection, Defs.TeriaProtocols.TERIA_LOGIN);
            Serializer.sendString(TeriaCoinMod.serverConnection, userCredential.getUsername());
            Serializer.sendString(TeriaCoinMod.serverConnection, userCredential.getPassword());

            int result = Serializer.receiveInt(TeriaCoinMod.serverConnection);

            switch (result){

                case Defs.TeriaProtocols.OK:
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Login completed, welcome "+userCredential.getUsername()+"!"));
                    break;

                case Defs.TeriaProtocols.TeriaLoginCodes.INVALID_CREDENTIALS:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Login failed: invalid combination username/password"));
                    TeriaCoinMod.clearPeer();
                    break;

                default:
                    throw new InvalidProtocolException("Protocol not respected!");
            }

        } catch (UnknownHostException e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Server \""+ConfigHandler.serverAddress+":"+ConfigHandler.serverPort+"\" unreachable"));
            TeriaCoinMod.clearPeer();
        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer();
        }

    }

}
