package com.italiandudes.teriacoinmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class Teleport extends Teleporter {

    //Attributes
    private final double x, y, z;

    //Constructors
    public Teleport(WorldServer world, double x, double y, double z){
        super(world);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Methods
    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        this.world.getBlockState(new BlockPos((int) x, (int) y, (int) z));
        entityIn.setPosition(x,y,z);
        entityIn.motionX = 0f;
        entityIn.motionY = 0f;
        entityIn.motionZ = 0f;
    }
    public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z){
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        MinecraftServer server = player.getEntityWorld().getMinecraftServer();
        if(server == null){
            throw new IllegalArgumentException("Dimension: "+dimension+" doesn't exist");
        }
        WorldServer worldServer = server.getWorld(dimension);

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new Teleport(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y, z);
    }
}
