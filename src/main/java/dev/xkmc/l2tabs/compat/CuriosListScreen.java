package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import top.theillusivec4.curios.common.inventory.CurioSlot;

public class CuriosListScreen extends BaseContainerScreen<CuriosListMenu> {

	public CuriosListScreen(CuriosListMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	public void init() {
		super.init();
		if (topPos < 28) topPos = 28;
		int w = 10;
		int h = 11;
		int x = getGuiLeft() + getXSize() - w - 7, y = getGuiTop() + 4;
		new TabManager(this).init(this::addRenderableWidget, TabCurios.tab);
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
		var sr = menu.sprite.get().getRenderer(this);
		sr.start(g);
		for (int i = 0; i < menu.curios.getSize(); i++) {
			sr.draw(g, "grid", "slot", i % 9 * 18 - 1, i / 9 * 18 - 1);
		}
	}

	@Override
	protected void renderTooltip(GuiGraphics g, int mx, int my) {
		LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer != null && clientPlayer.inventoryMenu
				.getCarried().isEmpty() && this.getSlotUnderMouse() != null) {
			Slot slot = this.getSlotUnderMouse();

			if (slot instanceof CurioSlot slotCurio && !slot.hasItem()) {
				g.renderTooltip(font, Component.translatable(slotCurio.getSlotName()), mx, my);
			}
		}
		super.renderTooltip(g, mx, my);
	}
}
