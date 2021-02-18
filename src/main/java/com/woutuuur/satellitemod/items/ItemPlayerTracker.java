package com.woutuuur.satellitemod.items;

import java.util.List;

import com.woutuuur.satellitemod.Reference;
import com.woutuuur.satellitemod.init.ModEvents;
import com.woutuuur.satellitemod.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ItemPlayerTracker extends ItemBasic {
	public ItemPlayerTracker() {
		super("player_tracker");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String name = ((ItemPlayerTracker) itemStack.getItem()).getTrackedName(itemStack);
		if (name.isEmpty() ) {
			tooltip.add("Right click on a player to activate");
			tooltip.add("Right click again to get their distance away from you");
			tooltip.add("Uses one tracking core per use");
		}
		else {
			tooltip.add(TextFormatting.WHITE + "Tracking " + TextFormatting.GOLD + name);
			tooltip.add("Right click to get their distance away from you");
			tooltip.add("Uses one tracking core per use");
		}
	}
	
	public String getTrackedName(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (!stack.hasTagCompound()) {
			NBTTagCompound newNBT = new NBTTagCompound();
			newNBT.setString("trackedName", "");
			newNBT.setBoolean("firstUse", true);
			stack.setTagCompound(newNBT);
	    }
		nbt = stack.getTagCompound();
	    return nbt.getString("trackedName");
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack) {
		String name = ((ItemPlayerTracker) itemStack.getItem()).getTrackedName(itemStack);
		return !name.isEmpty();
	}
	
	@SubscribeEvent
	public static void playerInteract(EntityInteract event) {
		try {
			if (!event.getWorld().isRemote) {
				EntityPlayer p = event.getEntityPlayer();
				ItemStack itemStack = p.getHeldItemMainhand();
				// Cast to ItemPlayerTracker. If it cannot cast, it will cause a ClassCastException, which will be caught below
				String name = ((ItemPlayerTracker) itemStack.getItem()).getTrackedName(itemStack);
				NBTTagCompound nbt = itemStack.getTagCompound();
				if (nbt.getBoolean("firstUse") == true) {
					NBTTagCompound newNBT = new NBTTagCompound();
					newNBT.setBoolean("firstUse", false);
					itemStack.setTagCompound(newNBT);
				}
				else if (name.isEmpty() && event.getTarget() instanceof EntityPlayer)
				{
					EntityPlayer target = (EntityPlayer) event.getTarget();
					String targetName = target.getName();
					nbt.setString("trackedName", targetName.toString());
					itemStack.setTagCompound(nbt);
					p.sendMessage(new TextComponentString(TextFormatting.WHITE + "Tracker activated. Tracking " + TextFormatting.GOLD + targetName));
					p.playSound(ModEvents.PLING, 1.0f, 1.0f);
				}
			}
		}
		// Player is not holding a PlayerTracker
		catch (ClassCastException e) {
			return;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		EntityPlayerSP client = Minecraft.getMinecraft().player;
		
		if (worldIn.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);

		String name = ((ItemPlayerTracker) itemStack.getItem()).getTrackedName(itemStack);
		NBTTagCompound nbt = itemStack.getTagCompound();
		List<ItemStack> itemStacksIn = playerIn.inventoryContainer.getInventory();
		
		if (!name.isEmpty() && !nbt.getBoolean("firstUse")) {
			int index = -1;
			ItemStack cores = null;
			index = playerIn.inventory.getSlotFor(new ItemStack(ModItems.tracking_core));
			if (index < 0) {
				playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "No tracking cores!"));
				client.playSound(ModEvents.JAMMED, 1.0f, 1.0f);
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
			}
			cores = playerIn.inventory.getStackInSlot(index);
			try {
				boolean isJammed = false;
				EntityPlayer trackedPlayer = worldIn.getPlayerEntityByName(name);
				List<ItemStack> itemStacks = trackedPlayer.inventoryContainer.inventoryItemStacks;
				for (ItemStack item : itemStacks) {
					// If the tracked player has a jammer in their inventory, the tracking attempt is jammed
					if (item.getUnlocalizedName().contains("item.handheld_jammer"))
						isJammed = true;
				}
				if (isJammed) {
					playerIn.sendMessage(new TextComponentString(TextFormatting.GOLD + name + TextFormatting.WHITE + " is jamming your tracking attempt!"));
					client.playSound(ModEvents.JAMMED, 1.0f, 1.0f);
				} else {
					playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Distance to " + TextFormatting.GOLD + name + TextFormatting.WHITE + ": " + (int) playerIn.getDistance(trackedPlayer) + " blocks"));
					playerIn.inventory.setInventorySlotContents(index, new ItemStack(ModItems.tracking_core, cores.getCount() - 1));
					client.playSound(ModEvents.RADAR_PING, 1.0f, 1.0f);
				}

			}
			catch (Exception e) {
				playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Unable to locate player"));
				client.playSound(ModEvents.ERROR, 1.0f, 1.0f);
			}

		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
	}
}
