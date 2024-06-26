//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.l2tabs.tabs.contents;

import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.L2TabsClient;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import dev.xkmc.l2tabs.tabs.inventory.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.function.Predicate;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = EventBusSubscriber.Bus.GAME)
public class TabInventory extends TabBase<InvTabData, TabInventory> {

	public static Predicate<Screen> inventoryTest = e -> e instanceof InventoryScreen;

	public static Runnable openInventory = () -> {
		var player = Minecraft.getInstance().player;
		if (player != null) Minecraft.getInstance().setScreen(new InventoryScreen(player));
	};

	@SubscribeEvent
	public static void guiPostInit(ScreenEvent.Init.Post event) {
		if (inventoryTest.test(event.getScreen())) {
			var manager = new TabManager<>(ScreenWrapper.of(event.getScreen()), new InvTabData());
			manager.init(event::addListener, L2TabsClient.TAB_INVENTORY);
		}
	}

	private static void renderTabs(GuiGraphics g, Screen screen) {
		for (var e : screen.children()) {
			if (e instanceof TabBase<?, ?> tab) {
				if (tab.manager.selected != tab.token)
					tab.renderBackground(g);
			}
		}
	}

	@SubscribeEvent
	public static void guiPostRenderBG(ScreenEvent.BackgroundRendered event) {
		renderTabs(event.getGuiGraphics(), event.getScreen());
	}

	@SubscribeEvent
	public static void guiPostRenderBG(ContainerScreenEvent.Render.Background event) {
		renderTabs(event.getGuiGraphics(), event.getContainerScreen());
	}

	public TabInventory(int index, TabToken<InvTabData, TabInventory> token,
						TabManager<InvTabData> manager, ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	public void onTabClicked() {
		openInventory.run();
	}

}
