package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.common.exceptions.IO.socket.InvalidProtocolException;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaChangePassword extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriachangepassword";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriachangepassword <password> <newPassword> <confirmNewPassword>";
    }
    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) {

        if (args.length == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + getUsage(sender)));
            return;
        }

        if(args.length != 3){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

        String password = DigestUtils.sha512Hex(args[0]);
        String newPassword = DigestUtils.sha512Hex(args[1]);
        String confirmNewPassword = DigestUtils.sha512Hex(args[2]);

        if(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender) == null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        if(!password.equals(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender).getCredential().getPassword())){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Password mismatch!"));
            return;
        }

        if(!newPassword.equals(confirmNewPassword)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "The new passwords are not equals!"));
            return;
        }

        if(!password.equals(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender).getCredential().getPassword())){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "The old password and the new password are the same!"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.TERIA_CHANGE_PASSWORD);
            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), password);
            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), newPassword);
            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), confirmNewPassword);

            int result = Serializer.receiveInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender));

            switch (result){

                case Defs.TeriaProtocols.OK:
                    TeriaCoinMod.clearPeer((EntityPlayerMP) sender);
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Password change completed! Disconnecting from server..."));
                    break;

                case Defs.TeriaProtocols.TeriaChangePasswordCodes.OLD_AND_NEW_PASSWORD_ARE_EQUALS:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "The old password and the new password are the same!"));
                    break;

                case Defs.TeriaProtocols.TeriaChangePasswordCodes.NEW_PASSWORD_AND_CONFIRM_MISMATCH:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "The new passwords are not equals!"));
                    break;

                case Defs.TeriaProtocols.TeriaChangePasswordCodes.PASSWORD_MISMATCH:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Password mismatch!"));
                    break;

                default:
                    throw new InvalidProtocolException("Protocol not respected!");

            }

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer((EntityPlayerMP) sender);
        }

    }

}
