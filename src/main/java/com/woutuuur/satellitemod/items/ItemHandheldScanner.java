package com.woutuuur.satellitemod.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.woutuuur.satellitemod.Reference;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ItemHandheldScanner extends ItemBasic{
	public ItemHandheldScanner() {
		super("handheld_scanner");
		this.maxStackSize = 1;
		this.setMaxDamage(256);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
	
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Find players in the current chunk");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);
		
		if (worldIn.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, item);
		
		BlockPos playerInPos = playerIn.getPosition();
		
		Chunk chunk = worldIn.getChunkFromBlockCoords(playerInPos);
		
		// Only check current chunk
		int minX = chunk.x * 16;
		int minZ = chunk.z * 16;
		int maxX = minX + 16;
		int maxZ = minZ + 16;

		List<EntityPlayer> players = worldIn.playerEntities;
		Set<EntityPlayer> playersInRange = new HashSet<EntityPlayer>();
		
		// Checks all players to see if they are in the chunk
		for (EntityPlayer player : players) {
			BlockPos playerPos = player.getPosition();
			if (player.getUniqueID() != playerIn.getUniqueID() && player.getClass() == EntityPlayerMP.class) {
				if (playerPos.getX() >= minX && playerPos.getX() <= maxX && playerPos.getZ() >= minZ && playerPos.getZ() <= maxZ) {
					playersInRange.add(player);
				}
			}
		}
		
		// Send chat message for every player found (no message if none are found)
		for (EntityPlayer playerFound : playersInRange) {
			BlockPos playerFoundPos = playerFound.getPosition();
			playerIn.sendMessage(new TextComponentString("§fFound player §6"+ playerFound.getName() + "§f at §6" + playerFoundPos.getX() + ", " + playerFoundPos.getY() + ", " + playerFoundPos.getZ()));
		}
		
		// Reduce durability by 1
		item.damageItem(1, playerIn);
		
		//TO-DO: RF power usage (?)
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
	}
}
