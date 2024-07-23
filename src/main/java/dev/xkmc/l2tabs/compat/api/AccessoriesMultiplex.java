package dev.xkmc.l2tabs.compat.api;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.compat.accessories.AccessoriesMultiplexImpl;
import dev.xkmc.l2tabs.compat.common.*;
import dev.xkmc.l2tabs.compat.curios.CuriosMultiplexImpl;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

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

	static MenuEntry<CuriosListMenu> menuType;
	public static Val<TabToken<InvTabData, TabCurios>> tab;

	public static void onStartUp() {
		menuType = L2Tabs.REGISTRATE.menu("curios", CuriosListMenu::fromNetwork, () -> CuriosListScreen::new).register();
		tab = L2Tabs.TAB_REG.reg("curios", () -> L2Tabs.GROUP.registerTab(2000, () -> TabCurios::new,
				() -> Items.AIR, L2TabsLangData.CURIOS.get()));
	}

	public static void openScreen(ServerPlayer player) {
		CuriosEventHandler.openMenuWrapped(player, () -> new CuriosMenuPvd(menuType.get()).open(player));
	}

	public abstract void onClientInit();

	public abstract IAccessoriesWrapper wrap(Player player, int page);

}
