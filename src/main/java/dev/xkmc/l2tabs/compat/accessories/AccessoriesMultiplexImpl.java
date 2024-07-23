package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import io.wispforest.accessories.networking.server.ScreenOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

public class AccessoriesMultiplexImpl extends AccessoriesMultiplex {

	@Override
	public IAccessoriesWrapper wrap(Player player, int page) {
		return new AccessoriesWrapper(player, page);
	}

	public void onClientInit() {
		Predicate<Screen> old = TabInventory.inventoryTest;
		TabInventory.inventoryTest = screen -> {
			String name = screen.getClass().getName();
			boolean isCurio = screen instanceof EffectRenderingInventoryScreen<?> && (
					name.startsWith("top.theillusivec4.curios") ||
							name.startsWith("io.wispforest.accessories"));
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
			PacketDistributor.sendToServer(ScreenOpen.of(false));
		}
	}

}
