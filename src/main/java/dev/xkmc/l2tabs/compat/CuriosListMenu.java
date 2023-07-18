package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class CuriosListMenu extends BaseContainerMenu<CuriosListMenu> {

	public static final SpriteManager[] MANAGER;

	static {
		MANAGER = new SpriteManager[4];
		for (int i = 0; i < 4; i++) {
			MANAGER[i] = new SpriteManager(L2Tabs.MODID, "curios_" + (i + 3));
		}
	}

	private static SpriteManager getManager(int size) {
		int n = (size + 8) / 9;
		return MANAGER[Math.min(Math.max(n - 3, 0), 3)];
	}

	public static CuriosListMenu fromNetwork(MenuType<CuriosListMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int page = buf.readInt();
		return new CuriosListMenu(type, wid, plInv, new CuriosWrapper(plInv.player, page));
	}

	protected CuriosWrapper curios;

	protected CuriosListMenu(MenuType<?> type, int wid, Inventory plInv, CuriosWrapper curios) {
		super(type, wid, plInv, getManager(curios.getSize()), e -> new BaseContainer<>(curios.getSize(), e), false);
		addCurioSlot("grid", curios);
		this.curios = curios;
	}

	protected void addCurioSlot(String name, CuriosWrapper curios) {
		int current = added;
		sprite.get().getSlot(name, (x, y) -> {
			int i = added - current;
			if (i >= curios.getSize()) return null;
			var ans = curios.get(i).toSlot(x, y);
			added++;
			return ans;
		}, this::addSlot);
	}

	private boolean checkSwitch(Player player, int page) {
		if (page >= 0 && page < curios.total) {
			if (player instanceof ServerPlayer sp) {
				ItemStack carry = getCarried();
				setCarried(ItemStack.EMPTY);
				new CuriosMenuPvd(CuriosScreenCompatImpl.get().menuType.get(), page).open(sp);
				sp.containerMenu.setCarried(carry);
			} else {
				slots.clear();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean clickMenuButton(Player player, int btn) {
		if (btn == 1) {
			return checkSwitch(player, curios.page - 1);
		} else if (btn == 2) {
			return checkSwitch(player, curios.page + 1);
		}
		return super.clickMenuButton(player, btn);
	}
}
