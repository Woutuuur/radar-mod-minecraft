package com.woutuuur.satellitemod.client.gui;

import java.util.List;
import java.util.UUID;

import com.woutuuur.satellitemod.Reference;
import com.woutuuur.satellitemod.tiles.EntityTileRadar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiRadar extends GuiScreen {
	EntityTileRadar TE;
	private int buttonID = 0;
	private final int GUI_WIDTH = 176;
	private final int GUI_HEIGHT = 239;
	private final int BUTTON_COUNT = 10;
	private final int BUTTON_HEIGHT = (GUI_HEIGHT - 30) / BUTTON_COUNT;
	private final int BUTTON_WIDTH = GUI_WIDTH - 40;
	ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/default.png");
	
	public GuiRadar(World worldIn, BlockPos pos) {
		TE = (EntityTileRadar) worldIn.getTileEntity(pos);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void updateScreen() {

	}
	
	@Override
	public void initGui() {
		List<UUID> whitelist = TE.getWhitelist();
		int centerX = width / 2 - GUI_WIDTH / 2;
		int centerY = height / 2 - GUI_HEIGHT / 2;
		for (int i = 0; i < whitelist.size(); i++) {
			this.buttonList.add(new GuiButton(buttonID + i, centerX + 10, centerY + (BUTTON_HEIGHT + 1)  * i + 10, BUTTON_WIDTH, BUTTON_HEIGHT, whitelist.get(i).toString()));
			this.buttonList.add(new GuiButton(buttonID + 100 + i, (centerX + 10) + BUTTON_WIDTH + 5, centerY + (BUTTON_HEIGHT + 1) * i + 10, 15, BUTTON_HEIGHT, "X"));
		}
		for (int i = whitelist.size(); i < BUTTON_COUNT; i++) {
			this.buttonList.add(new GuiButton(buttonID + i, centerX + 10, centerY + (BUTTON_HEIGHT + 1) * i + 10, BUTTON_WIDTH, BUTTON_HEIGHT, ""));
			this.buttonList.add(new GuiButton(buttonID + 100 + i, (centerX + 10) + BUTTON_WIDTH + 5, centerY + (BUTTON_HEIGHT + 1) * i + 10, 15, BUTTON_HEIGHT, "X"));
		}
		
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		List<UUID> whitelist = TE.getWhitelist();
		if (button.id - 100 < whitelist.size() && button.id - 100 >= 0) {
			UUID uuid = whitelist.get(button.id - 100);
			if (uuid != null) {
				TE.removeFromWhitelist(uuid);;
			}
			this.buttonList.clear();
			initGui();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		int centerX = width / 2 - GUI_WIDTH / 2;
		int centerY = height / 2 - GUI_HEIGHT / 2;
		
		drawTexturedModalRect(centerX, centerY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
