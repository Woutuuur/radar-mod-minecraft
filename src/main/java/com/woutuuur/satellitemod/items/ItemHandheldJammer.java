package com.woutuuur.satellitemod.items;

import java.util.List;

import com.woutuuur.satellitemod.Reference;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ItemHandheldJammer extends ItemBasic {
	public ItemHandheldJammer() {
		super("handheld_jammer");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Jams players attempts at tracking you using a player tracker");
		tooltip.add("Simply keep it in your inventory to block tracking attempts");
	}
}
