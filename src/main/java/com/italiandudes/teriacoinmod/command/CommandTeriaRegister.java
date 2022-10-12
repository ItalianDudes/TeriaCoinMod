package com.italiandudes.teriacoinmod.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public class CommandTeriaRegister extends CommandBase {

    //Methods
    @Override
    public String getName() {
        return "teriaregister";
    }
    @Override
    public String getUsage(ICommandSender sender) {
        return "teriaregister <username> <password> <confirmPassword>";
    }
    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length != 4){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid command structure!"));
            return;
        }

    }
}
