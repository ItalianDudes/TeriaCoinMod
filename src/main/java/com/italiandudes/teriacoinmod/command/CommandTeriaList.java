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

public final class CommandTeriaList extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "terialist";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "terialist";
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

        try{

            Serializer.sendString(TeriaCoinMod.serverConnection, Defs.TeriaProtocols.TERIA_EXCHANGE_LIST);

            int valueAmount = Serializer.receiveInt(TeriaCoinMod.serverConnection);

            if(valueAmount <= 0){
                sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "Available Items: NONE"));
            }else{
                sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "Available Items:"));
                String itemName;
                int index;
                double itemValue;
                for(int i=0;i<valueAmount;i++){
                    index = Serializer.receiveInt(TeriaCoinMod.serverConnection);
                    itemName = Serializer.receiveString(TeriaCoinMod.serverConnection);
                    itemValue = Serializer.receiveDouble(TeriaCoinMod.serverConnection);
                    sender.sendMessage(new TextComponentString("["+index+"] "+itemName+": "+itemValue+"TC"));
                }
            }

        }catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer();
        }

    }

}
