package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2tabs.compat.api.IAccessoriesSlotWrapper;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;

class AccessoriesWrapper extends IAccessoriesWrapper {

	private final ArrayList<AccessoriesSlotWrapper> list = new ArrayList<>();

	public AccessoriesWrapper(LivingEntity player, int page) {
		super(player);
		int max = 6;
		AccessoriesCapability cap = AccessoriesCapability.get(player);
		this.page = page;
		if (cap == null) {
			total = 0;
			return;
		}
		int offset = page * max * 9;
		int count = 0;
		for (var ent : cap.getContainers().entrySet()) {
			AccessoriesContainer cont = ent.getValue();
			// TODO isVisible
			for (int i = 0; i < cont.getSize(); i++) {
				count++;
				if (offset > 0) {
					offset--;
				} else {
					if (list.size() < max * 9) {
						list.add(new AccessoriesSlotWrapper(player, cont, i, ent.getKey()));
					}
				}
			}
		}
		this.total = (count - 1) / (max * 9) + 1;
	}

	public int getSize() {
		return list.size();
	}

	@Override
	public int getRows() {
		return list.isEmpty() ? 0 : (list.size() - 1) / 9 + 1;
	}

	@Nullable
	public IAccessoriesSlotWrapper getSlotAtPosition(int i) {
		if (i < 0 || i >= list.size()) return null;
		return list.get(i);
	}

}
