package com.italiandudes.teriacoinmod.command;

import com.google.common.collect.Lists;
import com.italiandudes.teriacoinmod.util.Defs;
import com.italiandudes.teriacoinmod.util.Teleport;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandDimensionTeleport extends CommandBase {

    //Attributes
    private final ArrayList<String> aliases = Lists.newArrayList(Defs.MOD_ID, "tpdimension", "teleportdimension");

    @Override @NotNull
    public String getName() {
        return "tpdimension";
    }

    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "tpdimension <id>";
    }

    @Override @NotNull
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) {

        if(args.length < 1){
            return;
        }

        String s = args[0];
        int dimensionID;

        try{
            dimensionID = Integer.parseInt(s);
        }catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString(TextFormatting.RED+ "Dimension ID Invalid"));
            return;
        }

        if(sender instanceof EntityPlayer){
            Teleport.teleportToDimension((EntityPlayer) sender, dimensionID, sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
        }

    }
}
