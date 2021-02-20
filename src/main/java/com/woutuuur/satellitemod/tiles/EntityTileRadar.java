package com.woutuuur.satellitemod.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntityTileRadar extends TileEntity implements ITickable{
	List<UUID> whitelist = new ArrayList<UUID>();
	World test;
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
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}
	
	public boolean getPlayerNearby() {
		return playerNearby;
	}
	
	public void addToWhiteList(UUID uuid) {
		whitelist.add(uuid);
	}
	
	public void removeFromWhitelist(UUID uuid) {
		if (whitelist.contains(uuid)) {
			whitelist.remove(uuid);
		}
	}
	
	public List<UUID> getWhitelist() {
		System.out.println(whitelist.toString());
		return whitelist;
	}
	
	
	
//	@Override
//	public SPacketUpdateTileEntity getUpdatePacket() {
//		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
//	}
//	
//	@Override
//	public NBTTagCompound getUpdateTag() {
//		return this.writeToNBT(new NBTTagCompound());
//	}
//	
//	@Override
//	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//		super.onDataPacket(net, pkt);
//		handleUpdateTag(pkt.getNbtCompound());
//	}
//	
//	public void sendUpdates() {
//		getWorld().markBlockRangeForRenderUpdate(pos, pos);
//		getWorld().notifyBlockUpdate(pos, getState(), getState(), 3);
//		getWorld().scheduleBlockUpdate(pos,this.getBlockType(),0,0);
//		markDirty();
//	}
//	
//	private IBlockState getState() {
//		return getWorld().getBlockState(pos);
//	}
}
