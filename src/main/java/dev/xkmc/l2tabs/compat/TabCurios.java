//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.OpenCurioHandler;
import dev.xkmc.l2tabs.init.data.OpenCuriosPacket;
import dev.xkmc.l2tabs.tabs.core.BaseTab;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.Curios;

public class TabCurios extends BaseTab<TabCurios> {

	public static TabToken<TabCurios> tab;

	public TabCurios(TabToken<TabCurios> token, TabManager manager, ItemStack stack, Component title) {
		super(token, manager, stack, title);
	}

	public void onTabClicked() {
		L2Tabs.PACKET_HANDLER.toServer(new OpenCuriosPacket(OpenCurioHandler.CURIO_OPEN));
	}

	@Override
	public void renderBackground(GuiGraphics g) {
		if (getX() == 0 && getY() == 0) return;
		if (this.visible) {
			token.type.draw(g, TEXTURE, getX(), getY(), manager.selected == token, token.getIndex());
			g.blit(new ResourceLocation(Curios.MODID, "textures/gui/inventory.png"),
					getX() + 6, getY() + 10, 50, 14, 14, 14);
		}
	}

}
