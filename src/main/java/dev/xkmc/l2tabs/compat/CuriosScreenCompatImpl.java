package dev.xkmc.l2tabs.compat;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;

import java.util.function.Predicate;

class CuriosScreenCompatImpl {

	private static CuriosScreenCompatImpl INSTANCE;

	public static CuriosScreenCompatImpl get() {
		if (INSTANCE == null) {
			INSTANCE = new CuriosScreenCompatImpl();
		}
		return INSTANCE;
	}

	MenuEntry<CuriosListMenu> menuType;

	void onStartUp() {
		menuType = L2Tabs.REGISTRATE.menu("curios", CuriosListMenu::fromNetwork, () -> CuriosListScreen::new).register();
	}

	void onClientInit() {
		Predicate<Screen> old = TabInventory.inventoryTest;
		TabInventory.inventoryTest = screen -> old.test(screen) || screen instanceof CuriosScreen;
		TabInventory.openInventory = this::openInventory;

		TabCurios.tab = TabRegistry.registerTab(2000, TabCurios::new, () -> Items.AIR, L2TabsLangData.CURIOS.get());
	}

	void openScreen(ServerPlayer player) {
		NetworkHooks.openScreen(player, new CuriosMenuPvd(menuType.get()));
	}

	private void openInventory() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null) {
			ItemStack stack = mc.player.containerMenu.getCarried();
			Screen scr = mc.screen;
			mc.player.containerMenu.setCarried(ItemStack.EMPTY);
			if (scr instanceof InventoryScreen inventory) {
				RecipeBookComponent recipeBookGui = inventory.getRecipeBookComponent();
				if (recipeBookGui.isVisible()) {
					recipeBookGui.toggleVisibility();
				}
			}
			NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
					new CPacketOpenCurios(stack));
		}
	}

}
