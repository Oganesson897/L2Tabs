package dev.xkmc.l2tabs.compat.curios;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.CuriosRegistry;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

import java.util.Optional;
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

	@Override
	public void commonSetup() {
		MenuSourceRegistry.register(CuriosRegistry.CURIO_MENU.get(), (menu, slot, index, wid) ->
				get().getPlayerSlotImpl(slot, index, wid, menu));

		MenuTraceRegistry.register(CuriosRegistry.CURIO_MENU.get(), menu ->
				Optional.of(TrackedEntry.of(TE_CURIO_INV.get(), new CurioTraceData(0))));
	}

	@Override
	public void openCuriosInv(ServerPlayer player, CurioTraceData data) {
		serverOpenInventory(player);
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
			clientOpenInventory(stack);
		}
	}

	private static void serverOpenInventory(ServerPlayer player) {
		try {
			var cls = Class.forName("top.theillusivec4.curios.common.inventory.container.CuriosContainerProvider");
			player.openMenu((MenuProvider) cls.getConstructor().newInstance());
		} catch (Exception ignored) {
		}
	}

	private static void clientOpenInventory(ItemStack stack) {
		try {
			var cls = Class.forName("top.theillusivec4.curios.common.network.client.CPacketOpenCurios");
			var con = cls.getConstructor(ItemStack.class);
			CustomPacketPayload packet = (CustomPacketPayload) con.newInstance(stack);
			PacketDistributor.sendToServer(packet);
		} catch (Exception ignored) {
		}
	}

	public Optional<PlayerSlot<?>> getPlayerSlotImpl(int slot, int index, int wid, AbstractContainerMenu menu) {
		if (menu.containerId != wid) return Optional.empty();
		var pl = getPlayerFromCurioCont(menu);
		if (pl.isPresent()) {
			Slot s = menu.getSlot(index);
			var source = AccessoriesMultiplex.IS_CURIOS.get();
			if (s instanceof CurioSlot curioSlot) {
				if (curioSlot.isCosmetic()) return Optional.empty();
				return Optional.of(new PlayerSlot<>(source, new CurioSlotData(curioSlot.getIdentifier(), curioSlot.getSlotIndex())));
			}
			if (s instanceof INamedSlot curioSlot) {
				return Optional.of(new PlayerSlot<>(source, curioSlot.toSlotData()));
			}
		}
		return Optional.empty();
	}

	private Optional<Player> getPlayerFromCurioCont(AbstractContainerMenu menu) {
		if (menu instanceof CuriosContainer cont) {
			return Optional.of(cont.player);
		}
		if (menu instanceof CuriosListMenu cont) {
			return Optional.of(cont.inventory.player);
		}
		return Optional.empty();
	}

	@Override
	public ItemStack getItemFromSlot(Player player, CurioSlotData slot) {
		var cap = CuriosApi.getCuriosInventory(player);
		if (cap.isEmpty()) return ItemStack.EMPTY;
		var handler = cap.get().getCurios().get(slot.id());
		if (handler == null || handler.getSlots() <= slot.slot()) return ItemStack.EMPTY;
		return handler.getStacks().getStackInSlot(slot.slot());
	}

}
