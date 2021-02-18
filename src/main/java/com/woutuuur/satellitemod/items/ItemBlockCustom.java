package com.woutuuur.satellitemod.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCustom extends ItemBlock {
	private String[] tooltip;
	
	public ItemBlockCustom(Block block) {
		super(block);
	}
	
	public void addTooltip(String tooltip) { 
		this.tooltip = tooltip.split("\n");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		for (String row : this.tooltip) {
			tooltip.add(row);
		}
	}
}
