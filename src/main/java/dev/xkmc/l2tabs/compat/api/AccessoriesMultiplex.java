package dev.xkmc.l2tabs.compat.api;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2menustacker.screen.base.L2MSReg;
import dev.xkmc.l2menustacker.screen.source.MenuSourceRegistry;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import dev.xkmc.l2menustacker.screen.track.MenuTraceRegistry;
import dev.xkmc.l2menustacker.screen.track.TrackedEntry;
import dev.xkmc.l2tabs.compat.accessories.AccessoriesMultiplexImpl;
import dev.xkmc.l2tabs.compat.common.*;
import dev.xkmc.l2tabs.compat.curios.CuriosMultiplexImpl;
import dev.xkmc.l2tabs.compat.track.*;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

import java.util.Optional;

public abstract class AccessoriesMultiplex {

	private static AccessoriesMultiplex INSTANCE;

	public static AccessoriesMultiplex get() {
		if (INSTANCE == null) {
			if (ModList.get().isLoaded("curios")) {
				if (ModList.get().isLoaded("accessories"))
					INSTANCE = new AccessoriesMultiplexImpl();
				else INSTANCE = new CuriosMultiplexImpl();
			}
		}
		return INSTANCE;
	}

	public static MenuEntry<CuriosListMenu> MT_CURIOS;
	public static Val<TabToken<InvTabData, TabCurios>> TAB_CURIOS;
	public static Val<CurioSource> IS_CURIOS;
	public static Val<CurioInvTrace> TE_CURIO_INV;
	public static Val<CurioTabTrace> TE_CURIO_TAB;

	public static void onStartUp() {
		MT_CURIOS = L2Tabs.REGISTRATE.menu("curios", CuriosListMenu::fromNetwork, () -> CuriosListScreen::new).register();
		TAB_CURIOS = L2Tabs.TAB_REG.reg("curios", () -> L2Tabs.GROUP.registerTab(2000, () -> TabCurios::new,
				() -> Items.AIR, L2TabsLangData.CURIOS.get()));

		IS_CURIOS = L2MSReg.SOURCES.reg("curios", CurioSource::new);
		TE_CURIO_INV = L2MSReg.TRACKED.reg("curios_inv", CurioInvTrace::new);
		TE_CURIO_TAB = L2MSReg.TRACKED.reg("curios_tab", CurioTabTrace::new);
	}

	public static void onCommonSetup() {
		MenuSourceRegistry.register(MT_CURIOS.get(), (menu, slot, index, wid) -> get().getPlayerSlotImpl(slot, index, wid, menu));
		MenuTraceRegistry.register(MT_CURIOS.get(), menu -> Optional.of(TrackedEntry.of(TE_CURIO_TAB.get(), new CurioTraceData(menu.curios.page))));
		get().commonSetup();


	}

	public static void openScreen(ServerPlayer player, CurioTraceData data) {
		CuriosEventHandler.openMenuWrapped(player, () -> new CuriosMenuPvd(MT_CURIOS.get(), data.page()).open(player));
	}

	public abstract void onClientInit();

	public abstract void commonSetup();

	public abstract IAccessoriesWrapper wrap(Player player, int page);

	public abstract Optional<PlayerSlot<?>> getPlayerSlotImpl(int slot, int index, int wid, AbstractContainerMenu menu);

	public abstract ItemStack getItemFromSlot(Player player, CurioSlotData slot);

	public abstract void openCuriosInv(ServerPlayer player, CurioTraceData data);

}
