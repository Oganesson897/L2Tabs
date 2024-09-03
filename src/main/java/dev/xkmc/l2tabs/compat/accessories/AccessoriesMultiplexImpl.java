package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2menustacker.screen.source.MenuSourceRegistry;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import dev.xkmc.l2menustacker.screen.track.MenuTraceRegistry;
import dev.xkmc.l2menustacker.screen.track.TrackedEntry;
import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import dev.xkmc.l2tabs.compat.api.INamedSlot;
import dev.xkmc.l2tabs.compat.common.CuriosListMenu;
import dev.xkmc.l2tabs.compat.track.CurioSlotData;
import dev.xkmc.l2tabs.compat.track.CurioTraceData;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import io.wispforest.accessories.Accessories;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.client.AccessoriesMenu;
import io.wispforest.accessories.client.gui.AccessoriesInternalSlot;
import io.wispforest.accessories.networking.server.ScreenOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Predicate;

public class AccessoriesMultiplexImpl extends AccessoriesMultiplex {

	@Override
	public IAccessoriesWrapper wrap(LivingEntity player, int page) {
		return new AccessoriesWrapper(player, page);
	}

	public void onClientInit() {
		Predicate<Screen> old = TabInventory.inventoryTest;
		TabInventory.inventoryTest = screen -> {
			String name = screen.getClass().getName();
			boolean isCurio = screen instanceof EffectRenderingInventoryScreen<?> &&
					name.startsWith("top.theillusivec4.curios") ||
					name.startsWith("io.wispforest.accessories");
			boolean onlyCurio = L2TabsConfig.CLIENT.showTabsOnlyCurio.get();
			return onlyCurio ? isCurio : old.test(screen) || isCurio;
		};
		var prev = TabInventory.openInventory;
		TabInventory.openInventory = () -> {
			if (L2TabsConfig.CLIENT.redirectInventoryTabToCuriosInventory.get())
				clientOpenInventory();
			else prev.run();
		};
	}

	@Override
	public void commonSetup() {
		MenuSourceRegistry.register(Accessories.ACCESSORIES_MENU_TYPE, (menu, slot, index, wid) ->
				get().getPlayerSlotImpl(slot, index, wid, menu));

		MenuTraceRegistry.register(Accessories.ACCESSORIES_MENU_TYPE, menu ->
				Optional.of(TrackedEntry.of(TE_CURIO_INV.get(), new CurioTraceData(0))));
	}

	@Override
	public void openCuriosInv(ServerPlayer player, CurioTraceData data) {
		Accessories.openAccessoriesMenu(player, null);
	}

	private void clientOpenInventory() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null) {
			PacketDistributor.sendToServer(ScreenOpen.of(false));
		}
	}

	public Optional<PlayerSlot<?>> getPlayerSlotImpl(int slot, int index, int wid, AbstractContainerMenu menu) {
		if (menu.containerId != wid) return Optional.empty();
		var pl = getPlayerFromCurioCont(menu);
		if (pl.isPresent()) {
			Slot s = menu.getSlot(index);
			var source = AccessoriesMultiplex.IS_CURIOS.get();
			if (s instanceof AccessoriesInternalSlot curioSlot) {
				if (curioSlot.isCosmetic) return Optional.empty();
				return Optional.of(new PlayerSlot<>(source, new CurioSlotData(
						curioSlot.accessoriesContainer.getSlotName(),
						curioSlot.getSlotIndex()
				)));
			}
			if (s instanceof INamedSlot curioSlot) {
				return Optional.of(new PlayerSlot<>(source, curioSlot.toSlotData()));
			}
		}
		return Optional.empty();
	}

	private Optional<Player> getPlayerFromCurioCont(AbstractContainerMenu menu) {
		if (menu instanceof AccessoriesMenu cont) {
			if (cont.targetEntity() != null && cont.targetEntity() != cont.owner())
				return Optional.empty();
			return Optional.of(cont.owner());
		}
		if (menu instanceof CuriosListMenu cont) {
			return Optional.of(cont.inventory.player);
		}
		return Optional.empty();
	}

	@Override
	public ItemStack getItemFromSlot(Player player, CurioSlotData slot) {
		AccessoriesCapability cap = AccessoriesCapability.get(player);
		if (cap == null) return ItemStack.EMPTY;
		var handler = cap.getContainers().get(slot.id());
		if (handler == null || handler.getSize() <= slot.slot()) return ItemStack.EMPTY;
		return handler.getAccessories().getItem(slot.slot());
	}

}
