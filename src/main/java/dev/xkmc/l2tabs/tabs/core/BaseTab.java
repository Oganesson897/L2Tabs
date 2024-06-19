package dev.xkmc.l2tabs.tabs.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class BaseTab<T extends BaseTab<T>> extends FloatingButton {

	protected final static ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

	public final ItemStack stack;
	public final TabToken<T> token;
	public final TabManager manager;

	public int page;

	@SuppressWarnings("unchecked")
	public BaseTab(TabToken<T> token, TabManager manager, ItemStack stack, Component title) {
		super(26, 32, title, b -> ((T) b).onTabClicked());
		this.stack = stack;
		this.token = token;
		this.manager = manager;
	}

	public abstract void onTabClicked();

	public void onTooltip(GuiGraphics g, int x, int y) {
		g.renderTooltip(Minecraft.getInstance().font, getMessage(), x, y);
	}

	public void renderBackground(GuiGraphics g) {
		if (getX() == 0 && getY() == 0) return;
		if (this.visible) {
			token.type.draw(g, TEXTURE, getX(), getY(), manager.selected == token, token.getIndex());
			RenderSystem.defaultBlendFunc();
			if (!this.stack.isEmpty())
				token.type.drawIcon(g, getX(), getY(), this.stack);
		}
	}

	@Override
	public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
		if (getX() == 0 && getY() == 0) return;
		if (manager.selected == token) {
			renderBackground(g);
		}
		if (this.token.getIndex() == TabRegistry.getTabs().size() - 1) { // draw on last
			manager.onToolTipRender(g, mouseX, mouseY);
		}
	}

}
