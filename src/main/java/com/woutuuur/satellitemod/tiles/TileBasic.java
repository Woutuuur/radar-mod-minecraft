package com.woutuuur.satellitemod.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class TileBasic extends Block{
	protected TileBasic(Material materialIn, String name) {
		super(materialIn);
		setRegistryName(name);
		setUnlocalizedName(name);
	}
}
