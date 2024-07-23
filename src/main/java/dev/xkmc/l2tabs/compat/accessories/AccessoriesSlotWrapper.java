package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2tabs.compat.api.IAccessoriesSlotWrapper;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

record AccessoriesSlotWrapper(LivingEntity player, AccessoriesContainer cap, int index, String identifier)
		implements IAccessoriesSlotWrapper {

	public Slot toSlot(int x, int y) {
		return new TabAccessoriesSlot(player, cap, index, identifier, x, y);
	}

}
