package com.woutuuur.satellitemod.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntityTileRadar extends TileEntity implements ITickable{
	
	List<UUID> whitelist = new ArrayList<UUID>();
	boolean playerNearby = true;
	int range = 3;
	
	public EntityTileRadar(int range) {
		this.range = range;
	}
	
	@Override
	public void update() {
		if (!getWorld().isRemote) {
			// Every tick, check if a player is in range
			findPlayerNearby();
			// Update the block to emit redstone
			getWorld().scheduleBlockUpdate(getPos(), getBlockType(), 0, 1);
			// Tell connected blocks/redstone that this block has a new update
			getWorld().notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
		}
	}
	
	public boolean isInWhitelist(UUID uuid) {
		return (whitelist.contains(uuid));
			
	}
	
	public void findPlayerNearby() {
		World world = getWorld();
		BlockPos pos = getPos();
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		
		// Get range in coordinates
		int minX = (chunk.x - (range - 1) / 2) 		* 16;
		int minZ = (chunk.z - (range - 1) / 2) 		* 16;
		int maxX = (chunk.x + (range - 1) / 2 + 1) 	* 16;
		int maxZ = (chunk.z + (range - 1) / 2 + 1) 	* 16;
		
		List<EntityPlayer> players = world.playerEntities;

		// Check if a player (that is not on the whitelist) is in range
		for (EntityPlayer player : players) {
			BlockPos playerPos = player.getPosition();
			if (!(isInWhitelist(player.getUniqueID()))) {
				if (playerPos.getX() >= minX && playerPos.getX() <= maxX && playerPos.getZ() >= minZ && playerPos.getZ() <= maxZ) {
					playerNearby = true;
					return;
				}
			}
		}
		playerNearby = false;
	}
	
	public boolean getPlayerNearby() {
		return playerNearby;
	}
	
	public void addToWhiteList(UUID uuid) {
		whitelist.add(uuid);
	}
}
