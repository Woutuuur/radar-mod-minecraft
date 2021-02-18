package com.woutuuur.satellitemod.init;

import com.woutuuur.satellitemod.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class ModEvents {
	public static SoundEvent PLING;
	public static SoundEvent JAMMED;
	public static SoundEvent ENDER;
	public static SoundEvent ERROR;
	public static SoundEvent RADAR_PING;
	
	public static void init() {
		registerSounds();
	}
	
	private static void registerSounds() {
		JAMMED = registerSound("minecraft", "block.dispenser.dispense");
		PLING = registerSound("minecraft", "entity.arrow.hit_player");
		ENDER = registerSound("minecraft", "entity.endermen.teleport");
		RADAR_PING = registerSound(Reference.MODID, "radar");
		ERROR = registerSound(Reference.MODID, "error");
	}
	
	private static SoundEvent registerSound(String domain, String name) {
		ResourceLocation location = new ResourceLocation(domain, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}
