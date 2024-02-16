//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.OpenCurioHandler;
import dev.xkmc.l2tabs.init.data.OpenCuriosPacket;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.Curios;

public class TabCurios extends TabBase<InvTabData, TabCurios> {

	public static TabToken<InvTabData, TabCurios> tab;

	public TabCurios(int index, TabToken<InvTabData, TabCurios> token,
					 TabManager<InvTabData> manager, ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	public void onTabClicked() {
		L2Tabs.PACKET_HANDLER.toServer(new OpenCuriosPacket(OpenCurioHandler.CURIO_OPEN));
	}

	@Override
	public void renderBackground(GuiGraphics g) {
		if (this.visible) {
			token.getType().draw(g, getX(), getY(), manager.selected == token, index);
			g.blit(new ResourceLocation("curios", "textures/gui/inventory.png"),
					getX() + 6, getY() + 10, 50, 14, 14, 14);
		}
	}

}
