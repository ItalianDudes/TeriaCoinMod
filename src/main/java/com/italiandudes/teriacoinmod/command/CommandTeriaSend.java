package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaSend extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriasend";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriasend <destinationUser> <TCamount>";
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

        String destinationUser = args[0];
        double amountTC;
        try {
            amountTC = Double.parseDouble(args[1]);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC Transfer Blocked: \""+args[1]+"\" is not a valid amount"));
            return;
        }

        if(!TeriaCoinMod.serverConnections.containsKey((EntityPlayerMP) sender)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.TERIA_SEND);
            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), destinationUser);
            Serializer.sendDouble(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), amountTC);

            int result = Serializer.receiveInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender));

            switch (result){

                case Defs.TeriaProtocols.OK:
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + String.valueOf(amountTC)+"TC sent to "+destinationUser+"!"));
                    break;

                case Defs.TeriaProtocols.TeriaSendCodes.INSUFFICIENT_TC_AVAILABLE:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC Transfer Blocked: Insufficient amount of TC into the wallet"));
                    break;

                case Defs.TeriaProtocols.TeriaSendCodes.USERNAME_DOES_NOT_EXIST:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC Transfer Blocked: username \""+destinationUser+"\" does not exist"));
                    break;

                case Defs.TeriaProtocols.TeriaSendCodes.INVALID_TC_AMOUNT:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC Transfer Blocked: \""+amountTC+"\" is not a valid amount (min 0)"));
                    break;

                default:
                    throw new IOException();

            }

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer((EntityPlayerMP) sender);
        }

    }

}
