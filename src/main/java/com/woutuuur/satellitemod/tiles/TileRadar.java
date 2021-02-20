package com.woutuuur.satellitemod.tiles;

import java.util.List;
import java.util.UUID;

import com.woutuuur.satellitemod.Reference;
import com.woutuuur.satellitemod.blocks.BlockBasic;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class TileRadar extends BlockBasic implements ITileEntityProvider {
	public int range = 3;
	public TileRadar(String name, int range) {
		super(name, Material.ROCK);
		this.range = range;
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setHardness(1.5f);
		this.setHarvestLevel("pickaxe", 1);
		this.setTickRandomly(true);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// New radar entity with range 3
		return new EntityTileRadar(this.range);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
		this.getWeakPower(state, worldIn, pos, EnumFacing.NORTH);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!worldIn.isRemote)
			return;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		EntityTileRadar TE = (EntityTileRadar) worldIn.getTileEntity(pos);
		if (worldIn.isRemote) return true;
		if (playerIn.isSneaking()) {
			List<UUID> whitelist = TE.getWhitelist();
			if (whitelist.size() == 0) {
				playerIn.sendMessage(new TextComponentString("Whitelist is empty"));
			} else {
				playerIn.sendMessage(new TextComponentString("Currently on the whitelist:"));
				for (UUID uuid : TE.getWhitelist()) {	
					playerIn.sendMessage(new TextComponentString(worldIn.getPlayerEntityByUUID(uuid).getName()));
				}
			}
			
		}
		else {
			if (!(TE.isInWhitelist(playerIn.getUniqueID())))
			{
				TE.addToWhiteList(playerIn.getUniqueID());
				playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Player " + TextFormatting.GOLD + playerIn.getName() + TextFormatting.WHITE + " added to whitelist"));
			} else {
				playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Already whitelisted"));
			}
		}
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity TE =  blockAccess.getTileEntity(pos);
		if (TE instanceof EntityTileRadar)
			return ((EntityTileRadar) TE).getPlayerNearby() ? 15 : 0;
		else {
			return 0;
		}
	}
}
