package com.woutuuur.satellitemod.init;

import com.woutuuur.satellitemod.Reference;
import com.woutuuur.satellitemod.SatelliteMod;
import com.woutuuur.satellitemod.items.ItemBasic;
import com.woutuuur.satellitemod.items.ItemHandheldJammer;
import com.woutuuur.satellitemod.items.ItemHandheldScanner;
import com.woutuuur.satellitemod.items.ItemPlayerTracker;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ModItems {
	static Item handheld_scanner;
	public static Item tracking_core;
	static Item player_tracker;
	static Item handheld_jammer;
	
	static public void init() {
		tracking_core = new ItemBasic("tracking_core").setCreativeTab(SatelliteMod.tabRadarMod).setMaxStackSize(64);
		
		handheld_scanner = new ItemHandheldScanner().setCreativeTab(SatelliteMod.tabRadarMod).setMaxStackSize(1);
		player_tracker = new ItemPlayerTracker().setCreativeTab(SatelliteMod.tabRadarMod).setMaxStackSize(1);
		handheld_jammer = new ItemHandheldJammer().setCreativeTab(SatelliteMod.tabRadarMod).setMaxStackSize(1);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(handheld_scanner);
		event.getRegistry().registerAll(tracking_core);
		event.getRegistry().registerAll(player_tracker);
		event.getRegistry().registerAll(handheld_jammer);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(handheld_scanner);
		registerRender(tracking_core);
		registerRender(player_tracker);
		registerRender(handheld_jammer);
	}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
