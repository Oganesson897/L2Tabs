package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2tabs.compat.api.IAccessoriesSlotWrapper;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.slot.SlotGroup;
import io.wispforest.accessories.api.slot.SlotType;
import io.wispforest.accessories.data.SlotGroupLoader;
import io.wispforest.accessories.data.SlotTypeLoader;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

		List<AccessoriesContainer> containers = new ArrayList<>();
		var groups = SlotGroupLoader.getGroups(player.level(), true);
		var map = cap.getContainers();
		for (var group : groups.stream().sorted(Comparator.comparingInt(SlotGroup::order).reversed()).toList()) {
			var slotNames = group.slots();
			var slotTypes = slotNames.stream()
					.map(s -> SlotTypeLoader.getSlotType(player.level(), s))
					.filter(Objects::nonNull)
					.sorted(Comparator.comparingInt(SlotType::order).reversed())
					.toList();
			for (var slot : slotTypes) {
				var cont = map.get(slot.name());
				if (cont == null || cont.slotType() == null) continue;
				containers.add(cont);
			}
		}
		for (var cont : containers) {
			for (int i = 0; i < cont.getSize(); i++) {
				count++;
				if (offset > 0) {
					offset--;
				} else {
					if (list.size() < max * 9) {
						list.add(new AccessoriesSlotWrapper(player, cont, i, cont.getSlotName()));
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
