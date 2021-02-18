package com.woutuuur.satellitemod.init;

import com.woutuuur.satellitemod.Reference;
import com.woutuuur.satellitemod.SatelliteMod;
import com.woutuuur.satellitemod.items.ItemBlockCustom;
import com.woutuuur.satellitemod.tiles.EntityTileRadar;
import com.woutuuur.satellitemod.tiles.TileRadar;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ModBlocks {
	static Block basic_radar;
	static Block advanced_radar;
	
	public static void init() {
		basic_radar = new TileRadar("basic_radar", 3);
		advanced_radar = new TileRadar("advanced_radar", 10);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(basic_radar);
		event.getRegistry().registerAll(advanced_radar);
		
		GameRegistry.registerTileEntity(EntityTileRadar.class, new ResourceLocation(Reference.MODID + ":basic_radar"));
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		String radarTooltip = "Range: %d chunks\nEmits a redstone signal when players are nearby\nRight click to add yourself to the whitelist";
		
		ItemBlockCustom _basic_radar = new ItemBlockCustom(basic_radar);
		_basic_radar.setCreativeTab(SatelliteMod.tabRadarMod);
		_basic_radar.addTooltip(String.format(radarTooltip, ((TileRadar) basic_radar).range));
		
		ItemBlockCustom _advanced_radar = new ItemBlockCustom(advanced_radar);
		_advanced_radar.setCreativeTab(SatelliteMod.tabRadarMod);
		_advanced_radar.addTooltip(String.format(radarTooltip, ((TileRadar) advanced_radar).range));

		event.getRegistry().registerAll(_basic_radar.setRegistryName(basic_radar.getRegistryName()));
		event.getRegistry().registerAll(_advanced_radar.setRegistryName(advanced_radar.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(Item.getItemFromBlock(basic_radar));
		registerRender(Item.getItemFromBlock(advanced_radar));
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
