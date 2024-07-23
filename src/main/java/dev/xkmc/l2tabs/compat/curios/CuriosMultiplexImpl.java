package dev.xkmc.l2tabs.compat.curios;

import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

public class CuriosMultiplexImpl extends AccessoriesMultiplex {

	@Override
	public IAccessoriesWrapper wrap(Player player, int page) {
		return new CuriosWrapper(player, page);
	}

	public void onClientInit() {
		Predicate<Screen> old = TabInventory.inventoryTest;
		TabInventory.inventoryTest = screen -> {
			boolean isCurio = screen instanceof EffectRenderingInventoryScreen<?> &&
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
			curiosOpenInventory(stack);
		}
	}

	private static void curiosOpenInventory(ItemStack stack) {
		try {
			var cls = Class.forName("top.theillusivec4.curios.common.network.client.CPacketOpenCurios");
			var con = cls.getConstructor(ItemStack.class);
			CustomPacketPayload packet = (CustomPacketPayload) con.newInstance(stack);
			PacketDistributor.sendToServer(packet);
		} catch (Exception ignored) {
		}
	}


}
