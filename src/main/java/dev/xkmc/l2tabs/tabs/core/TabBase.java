package dev.xkmc.l2tabs.tabs.core;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public abstract class TabBase<G extends TabGroupData<G>, T extends TabBase<G, T>> extends FloatingButton {

	public final int index;
	public final TabToken<G, T> token;
	public final TabManager<G> manager;

	public int page;

	@SuppressWarnings("unchecked")
	public TabBase(int index, TabToken<G, T> token, TabManager<G> manager, Component title) {
		super(token.getType().width, token.getType().height,
				title, b -> ((T) b).onTabClicked());
		this.index = index;
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
			token.draw(g, getX(), getY(), manager.selected == token, index);
			renderIcon(g);
		}
	}

	@Override
	public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
		if (getX() == 0 && getY() == 0) return;
		if (manager.selected == token) {
			renderBackground(g);
		}
		if (this == manager.list.getLast()) { // draw on last
			manager.onToolTipRender(g, mouseX, mouseY);
		}
	}

	protected void renderIcon(GuiGraphics g) {
		ItemStack stack = get(L2Tabs.TABS.reg().wrapAsHolder(token)).getDefaultInstance();
		if (!stack.isEmpty())
			token.getType().drawIcon(g, getX(), getY(), stack);
	}

	private static Item get(Holder<TabToken<?, ?>> holder) {
		var level = Minecraft.getInstance().level;
		if (level == null) return Items.AIR;
		var ans = L2Tabs.ICON.get(level.registryAccess(), holder);
		return ans == null ? Items.AIR : ans;
	}

}
