package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum TabType {
	ABOVE(0, 0, 26, 32);

	public static final int MAX_TABS = 7;
	private final int textureX;
	private final int textureY;
	private final int width;
	private final int height;

	TabType(int tx, int ty, int w, int h) {
		this.textureX = tx;
		this.textureY = ty;
		this.width = w;
		this.height = h;
	}

	public void draw(GuiGraphics g, ResourceLocation icon, int x, int y, boolean selected, int index) {
		index = index % MAX_TABS;
		int tx = this.textureX;
		if (index > 0)
			tx += this.width;


		if (index == MAX_TABS - 1) {
			tx += this.width;
		}

		int ty = selected ? this.textureY + this.height : this.textureY;
		g.blit(icon, x, y, tx, ty, this.width, this.height);
	}

	public void drawIcon(GuiGraphics g, int x, int y, ItemStack stack) {
		g.renderItem(stack, x + 5, y + 9);
	}

	public int getX(int index) {
		return (this.width + 4) * index;
	}

	public int getY(int index) {
		return -this.height + 4;
	}

	public boolean isMouseOver(int p_97214_, int p_97215_, int p_97216_, double p_97217_, double p_97218_) {
		int i = p_97214_ + this.getX(p_97216_);
		int j = p_97215_ + this.getY(p_97216_);
		return p_97217_ > (double) i && p_97217_ < (double) (i + this.width) && p_97218_ > (double) j && p_97218_ < (double) (j + this.height);
	}
}
