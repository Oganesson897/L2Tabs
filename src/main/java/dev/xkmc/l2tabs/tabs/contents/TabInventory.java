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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TabInventory extends TabBase<InvTabData, TabInventory> {

	public static Predicate<Screen> inventoryTest = e -> e instanceof InventoryScreen;
	public static Runnable openInventory = () -> Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));

	@SubscribeEvent
	public static void guiPostInit(ScreenEvent.Init.Post event) {
		if (inventoryTest.test(event.getScreen())) {
			var manager = new TabManager<>(ScreenWrapper.of(event.getScreen()), new InvTabData());
			manager.init(event::addListener, L2TabsClient.TAB_INVENTORY);
		}
	}

	@SubscribeEvent
	public static void guiPostRenderBG(ScreenEvent.BackgroundRendered event) {
		Screen screen = event.getScreen();
		for (var e : screen.children()) {
			if (e instanceof TabBase<?, ?> tab) {
				if (tab.manager.selected != tab.token)
					tab.renderBackground(event.getGuiGraphics());
			}
		}
	}

	public TabInventory(int index, TabToken<InvTabData, TabInventory> token,
						TabManager<InvTabData> manager, ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	public void onTabClicked() {
		openInventory.run();
	}

}
