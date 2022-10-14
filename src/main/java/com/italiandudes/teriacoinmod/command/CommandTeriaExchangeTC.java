package com.italiandudes.teriacoinmod.command;

import com.italiandudes.teriacoinmod.common.Serializer;
import com.italiandudes.teriacoinmod.TeriaCoinMod;
import com.italiandudes.teriacoinmod.common.exceptions.IO.socket.InvalidProtocolException;
import com.italiandudes.teriacoinmod.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CommandTeriaExchangeTC extends CommandBase {

    //Methods
    @Override @NotNull
    public String getName() {
        return "teriaexchangetc";
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "teriaexchangetc <index> <amount>";
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

        if(TeriaCoinMod.serverConnection == null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "There isn't a connection with the server, you have to login first"));
            return;
        }

        int index;
        int amount;
        try {
            index = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: \""+args[0]+"\" must be an integer > 0"));
            return;
        }
        try {
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: \""+args[1]+"\" must be an integer > 0"));
            return;
        }

        if(index<=0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: \""+index+"\" must be > 0"));
            return;
        }

        if(amount <= 0 || amount>64){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: \""+amount+"\" must be > 0 and <=64"));
            return;
        }

        try {

            Serializer.sendString(TeriaCoinMod.serverConnection, Defs.TeriaProtocols.TERIA_EXCHANGE_TC);
            Serializer.sendInt(TeriaCoinMod.serverConnection, index);
            Serializer.sendInt(TeriaCoinMod.serverConnection, amount);

            int result = Serializer.receiveInt(TeriaCoinMod.serverConnection);

            switch (result){

                case Defs.TeriaProtocols.OK:
                    try {
                        EntityPlayer entityplayer = getPlayer(server, sender, sender.getName());
                        String itemID = Serializer.receiveString(TeriaCoinMod.serverConnection);
                        int obtainableAmount = Serializer.receiveInt(TeriaCoinMod.serverConnection);
                        Item item = getItemByText(sender, itemID);
                        ItemStack itemstack = new ItemStack(item, obtainableAmount, 0);
                        boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

                        if (flag){
                            entityplayer.world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            entityplayer.inventoryContainer.detectAndSendChanges();
                        }

                        if (flag && itemstack.isEmpty()){
                            itemstack.setCount(1);
                            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, obtainableAmount);
                            EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);

                            if (entityitem1 != null){
                                entityitem1.makeFakeItem();
                            }
                        }else{
                            EntityItem entityitem = entityplayer.dropItem(itemstack, false);

                            if(entityitem != null){
                                entityitem.setNoPickupDelay();
                                entityitem.setOwner(entityplayer.getName());
                            }
                        }

                    } catch (CommandException e){
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "An unknown error has occurred on item giving"));
                        return;
                    }
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Exchange completed!"));
                    break;

                case Defs.TeriaProtocols.TeriaExchangeTCCodes.INSUFFICIENT_TC_AVAILABLE:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: Insufficient amount of TC into the wallet"));
                    break;

                case Defs.TeriaProtocols.TeriaExchangeTCCodes.ITEM_INDEX_NOT_FOUND:
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "TC to Item Exchange blocked: Item Index not found in exchange items index list"));
                    break;

                default:
                    throw new InvalidProtocolException("Protocol not respected!");

            }

        } catch (IOException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occurred while communicating with server"));
            TeriaCoinMod.clearPeer();
        }

    }

}
