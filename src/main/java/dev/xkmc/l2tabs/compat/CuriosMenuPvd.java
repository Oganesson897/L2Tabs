package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record CuriosMenuPvd(MenuType<CuriosListMenu> type, int page) implements MenuProvider {

	public CuriosMenuPvd(MenuType<CuriosListMenu> type) {
		this(type, 0);
	}

	@Override
	public Component getDisplayName() {
		return L2TabsLangData.CURIOS.get();
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new CuriosListMenu(type, wid, inv, new CuriosWrapper(player, page));
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeInt(page);
	}

	public void open(ServerPlayer player) {
		player.openMenu(this, this::writeBuf);
	}

}
