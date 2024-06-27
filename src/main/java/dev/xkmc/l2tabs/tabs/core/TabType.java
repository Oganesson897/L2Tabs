package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum TabType {
	ABOVE(26, 32, 8),
	BELOW(26, 32, 8),
	LEFT(32, 26, 5),
	RIGHT(32, 26, 5);

	public static final int MAX_TABS = 7;
	public final int width;
	public final int height;
	public final int max;

	TabType(int w, int h, int max) {
		this.width = w;
		this.height = h;
		this.max = max;
	}

	public void draw(TabSprites tex, GuiGraphics g, int x, int y, boolean selected, int index) {
		index = index % max;
		int tx = 0;
		if (index > 0) {
			tx++;
		}
		g.blitSprite(tex.get(tx, selected), x, y, tex.w(), tex.h());
	}

	public void drawIcon(GuiGraphics g, int x, int y, ItemStack stack) {
		int i = x;
		int j = y;
		switch (this) {
			case ABOVE -> {
				i += 6;
				j += 9;
			}
			case BELOW -> {
				i += 6;
				j += 6;
			}
			case LEFT -> {
				i += 10;
				j += 5;
			}
			case RIGHT -> {
				i += 6;
				j += 5;
			}
		}
		g.renderFakeItem(stack, i, j);
		g.renderItemDecorations(Minecraft.getInstance().font, stack, i, j);
	}

	public int getX(int pIndex) {
		return switch (this) {
			case ABOVE, BELOW -> this.width * pIndex;
			case LEFT -> -this.width + 4;
			case RIGHT -> -4;
		};
	}

	public int getY(int pIndex) {
		return switch (this) {
			case ABOVE -> -this.height + 4;
			case BELOW -> -4;
			case LEFT, RIGHT -> this.height * pIndex;
		};
	}

	public boolean isMouseOver(int p_97214_, int p_97215_, int p_97216_, double p_97217_, double p_97218_) {
		int i = p_97214_ + this.getX(p_97216_);
		int j = p_97215_ + this.getY(p_97216_);
		return p_97217_ > (double) i && p_97217_ < (double) (i + this.width) && p_97218_ > (double) j && p_97218_ < (double) (j + this.height);
	}

	public int getTabX(int imgWidth, int index) {
		return (this == RIGHT ? imgWidth : 0) + getX(index);
	}

	public int getTabY(int imgHeight, int index) {
		return (this == BELOW ? imgHeight : 0) + getY(index);
	}

	public Button getLeftButton(ITabScreen screen, Button.OnPress o) {
		int radius = 3;
		return new FloatingButton(screen::getGuiLeft, screen::getGuiTop,
				radius, -26 + radius,
				26 - radius * 2, 26 - radius * 2,
				Component.literal("<"), o);
	}

	public Button getRightButton(ITabScreen screen, Button.OnPress o) {
		int radius = 3;
		return new FloatingButton(screen::getGuiLeft, screen::getGuiTop,
				(TabType.MAX_TABS - 1) * 26 + radius, -26 + radius,
				26 - radius * 2, 26 - radius * 2,
				Component.literal(">"), o);
	}

}
