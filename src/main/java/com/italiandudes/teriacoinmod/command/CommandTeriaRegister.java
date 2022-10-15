package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.common.Credential;
import com.italiandudes.teriacoinmod.common.Peer;
import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.common.exceptions.IO.socket.InvalidProtocolException;
import com.italiandudes.teriacoinmod.handler.ConfigHandler;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public final class CommandTeriaRegister extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriaregister";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriaregister <username> <password> <confirmPassword>";
    }
    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) {

        if(args.length != 3){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

        String username = args[0];
        String password = args[1];
        String confirmPassword = args[2];

        if(!password.equals(confirmPassword)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Password mismatch!"));
        }

        if(TeriaCoinMod.serverConnections.containsKey((EntityPlayerMP) sender)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There is already an open connection with the server, disconnect first"));
            return;
        }

        Socket serverConnection = null;

        try {

            serverConnection = new Socket(ConfigHandler.serverAddress, ConfigHandler.serverPort);

            Credential userCredential = new Credential(username, password);

            Peer authConnection = new Peer(serverConnection, userCredential);

            Serializer.sendString(authConnection, Defs.TeriaProtocols.TERIA_REGISTER);
            Serializer.sendString(authConnection, userCredential.getUsername());
            Serializer.sendString(authConnection, userCredential.getPassword());

            int result = Serializer.receiveInt(authConnection);

            switch (result){

                case Defs.TeriaProtocols.OK:
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Registration completed!"));
                    break;

                case Defs.TeriaProtocols.TeriaRegisterCodes.ALREADY_EXIST:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Registration rejected: username \""+username+"\" already exist"));
                    break;

                case Defs.TeriaProtocols.TeriaRegisterCodes.UNSAFE_PASSWORD:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Registration rejected: password unsafe"));
                    break;

                case Defs.TeriaProtocols.TeriaRegisterCodes.INVALID_USER:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Registration rejected: invalid username"));
                    break;

                case Defs.TeriaProtocols.TeriaRegisterCodes.INVALID_PASSWORD:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Registration rejected: invalid password"));
                    break;

                default:
                    throw new InvalidProtocolException("Protocol not respected!");
            }

            serverConnection.close();

        } catch (UnknownHostException e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Server \""+ConfigHandler.serverAddress+":"+ConfigHandler.serverPort+"\" unreachable"));
            try {
                if (serverConnection != null) {
                    serverConnection.close();
                }
            }catch (Exception ignored){}

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            try {
                if (serverConnection != null) {
                    serverConnection.close();
                }
            }catch (IOException ignored){}
        }

    }
}
