package dev.xkmc.l2tabs.tabs.contents;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class BaseTextScreen extends Screen {

	private final ResourceLocation texture;

	public int imageWidth, imageHeight, leftPos, topPos;

	protected BaseTextScreen(Component title, ResourceLocation texture) {
		super(title);
		this.texture = texture;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	public void init() {
		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		renderBackground(g);
		int i = this.leftPos;
		int j = this.topPos;
		g.blit(texture, i, j, 0, 0, this.imageWidth, this.imageHeight);
		super.render(g, mx, my, ptick);
	}

	public boolean keyPressed(int a, int b, int c) {
		InputConstants.Key mouseKey = InputConstants.getKey(a, b);
		if (Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey)) {
			this.onClose();
			return true;
		}
		return super.keyPressed(a, b, c);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
