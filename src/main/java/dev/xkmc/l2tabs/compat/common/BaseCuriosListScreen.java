package dev.xkmc.l2tabs.compat.common;

import dev.xkmc.l2core.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2tabs.compat.api.INamedSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BaseCuriosListScreen<T extends BaseCuriosListMenu<T>> extends BaseContainerScreen<T> {

	public BaseCuriosListScreen(T cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void init() {
		super.init();
		if (topPos < 28) topPos = 28;
		int w = 10;
		int h = 11;
		int x = getGuiLeft() + titleLabelX + font.width(getTitle()) + 14,
				y = getGuiTop() + 4;
		if (menu.curios.page > 0) {
			addRenderableWidget(Button.builder(Component.literal("<"), e -> click(1))
					.pos(x - w - 1, y).size(w, h).build());
		}
		if (menu.curios.page < menu.curios.total - 1) {
			addRenderableWidget(Button.builder(Component.literal(">"), e -> click(2))
					.pos(x, y).size(w, h).build());
		}
	}

	@Override
	protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
		var sr = getRenderer();
		sr.start(g);
		for (int i = 0; i < menu.curios.getRows() * 9; i++) {
			if (menu.curios.getSlotAtPosition(i) != null)
				sr.draw(g, "grid", "slot", i % 9 * 18 - 1, i / 9 * 18 - 1);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
		super.renderLabels(p_281635_, p_282681_, p_283686_);
	}

	@Override
	protected void renderTooltip(GuiGraphics g, int mx, int my) {
		LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer != null && clientPlayer.inventoryMenu
				.getCarried().isEmpty() && this.getSlotUnderMouse() != null) {
			Slot slot = this.getSlotUnderMouse();

			if (slot instanceof INamedSlot slotCurio && !slot.hasItem()) {
				g.renderTooltip(font, slotCurio.getName(), mx, my);
			}
		}
		super.renderTooltip(g, mx, my);
	}

}