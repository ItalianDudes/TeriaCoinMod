package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaBalance extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriabalance";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriabalance";
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

        if(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender) == null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.TERIA_BALANCE);

            double balance = Serializer.receiveDouble(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender));

            if(balance == 0){
                sender.sendMessage(new TextComponentString(TextFormatting.RED +"Account Balance: "+ balance + "TC"));
            }else{
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN +"Account Balance: "+ balance + "TC"));
            }

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer((EntityPlayerMP) sender);
        }

    }

}
