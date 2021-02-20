package com.woutuuur.satellitemod;

import org.apache.logging.log4j.Logger;

import com.woutuuur.satellitemod.client.gui.GuiHandler;
import com.woutuuur.satellitemod.init.ModBlocks;
import com.woutuuur.satellitemod.init.ModEvents;
import com.woutuuur.satellitemod.init.ModItems;
import com.woutuuur.satellitemod.init.ModRecipes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class SatelliteMod
{
	@Instance
	public  static SatelliteMod instance;

	public  static Entity storedEntity;
    private static Logger logger;
    
    static public final CreativeTabs tabRadarMod = (new CreativeTabs("tabRadarMod") {
    	@Override
    	public ItemStack getTabIconItem() {
    		return new ItemStack(ModItems.tracking_core);
    	}
    });

    ModEvents events = new ModEvents();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ModItems.init();
        ModBlocks.init();
        ModRecipes.init(); //  << Add smelting recipes here
        MinecraftForge.EVENT_BUS.register(events);

        ModEvents.init();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }
}
