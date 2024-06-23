package dev.xkmc.l2tabs.compat;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.L2TabsClient;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.client.gui.CuriosScreen;
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
		TabInventory.inventoryTest = screen -> {
			boolean isCurio = screen instanceof CuriosScreen ||
					screen instanceof EffectRenderingInventoryScreen<?> &&
							screen.getClass().getName().startsWith("top.theillusivec4.curios");
			boolean onlyCurio = L2TabsConfig.CLIENT.showTabsOnlyCurio.get();
			return onlyCurio ? isCurio : old.test(screen) || isCurio;
		};
		var prev = TabInventory.openInventory;
		TabInventory.openInventory = () -> {
			if (L2TabsConfig.CLIENT.redirectInventoryTabToCuriosInventory.get())
				openInventory();
			else prev.run();
		};

		TabCurios.tab = L2TabsClient.GROUP.registerTab(2000, TabCurios::new, () -> Items.AIR, L2TabsLangData.CURIOS.get());
	}

	void openScreen(ServerPlayer player) {
		CuriosEventHandler.openMenuWrapped(player, () -> new CuriosMenuPvd(menuType.get()).open(player));
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
			PacketDistributor.SERVER.noArg().send(new CPacketOpenCurios(stack));
		}
	}

}
