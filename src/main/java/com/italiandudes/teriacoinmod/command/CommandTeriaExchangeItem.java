package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaExchangeItem extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriaexchangeitem";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriaexchangeitem <index> <amount>";
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

        if(args.length != 2){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

        if(!TeriaCoinMod.serverConnections.containsKey((EntityPlayerMP) sender)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        int index;
        int amount;
        try {
            index = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: \""+args[0]+"\" must be an integer > 0"));
            return;
        }
        try {
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: \""+args[1]+"\" must be an integer > 0"));
            return;
        }

        if(index<=0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: \""+index+"\" must be > 0"));
            return;
        }

        if(amount <= 0 || amount>64){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: \""+amount+"\" must be > 0 and <=64"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.TERIA_EXCHANGE_ITEM);
            Serializer.sendInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), index);
            boolean itemExist = Serializer.receiveBoolean(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender));
            if(!itemExist){
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: Item Index not found in exchange items index list"));
                return;
            }
            String itemID = Serializer.receiveString(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender));

            try {
                Item item = getItemByText(sender, itemID);
                EntityPlayerMP playerMP = getPlayer(server, sender, sender.getName());
                if(!playerMP.inventory.hasItemStack(new ItemStack(item, amount))){
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Item to TC Exchange blocked: player doesn't have the declared item amount"));
                    Serializer.sendInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.TeriaExchangeItemCodes.MISSING_REQUESTED_ITEM_AMOUT);
                    return;
                }
                amount = playerMP.inventory.clearMatchingItems(item, 0, amount, null);
                playerMP.inventoryContainer.detectAndSendChanges();

                if(!playerMP.capabilities.isCreativeMode){
                    playerMP.updateHeldItem();
                }

            } catch (CommandException e){
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "An unknown error has occurred on item exchanging"));
                return;
            }
            Serializer.sendInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), Defs.TeriaProtocols.OK);
            Serializer.sendInt(TeriaCoinMod.serverConnections.get((EntityPlayerMP) sender), amount);
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Exchange completed!"));

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer((EntityPlayerMP) sender);
        }

    }

}
