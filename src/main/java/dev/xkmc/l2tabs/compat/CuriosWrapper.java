package dev.xkmc.l2tabs.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.inventory.CurioSlot;

import java.util.ArrayList;

public class CuriosWrapper {//TODO make living entity compatible

	private final ArrayList<CuriosSlotWrapper> list = new ArrayList<>();

	public final int total, page;

	public CuriosWrapper(Player player, int page) {
		int max = 6;//TODO
		var opt = player.getCapability(CuriosCapability.INVENTORY).resolve();
		this.page = page;
		if (opt.isEmpty()) {
			total = 0;
			return;
		}
		var cap = opt.get();
		int offset = page * max * 9;
		int count = 0;
		for (var ent : cap.getCurios().entrySet()) {
			var stack = ent.getValue();
			for (int i = 0; i < stack.getSlots(); i++) {
				count++;
				if (offset > 0) {
					offset--;
				} else {
					if (list.size() < max * 9) {
						list.add(new CuriosSlotWrapper(player, stack, i, ent.getKey()));
					}
				}
			}
		}
		this.total = (count - 1) / (max * 9) + 1;
	}

	public int getSize() {
		return list.size();
	}

	public CuriosSlotWrapper get(int i) {
		return list.get(i);
	}

	public record CuriosSlotWrapper(Player player, ICurioStacksHandler cap, int index, String identifier) {

		public Slot toSlot(int x, int y) {
			return new CurioSlot(player, cap.getStacks(), index, identifier, x, y, cap.getRenders(), false);
		}

	}

}
