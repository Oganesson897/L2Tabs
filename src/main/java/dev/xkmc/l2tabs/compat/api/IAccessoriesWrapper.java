package dev.xkmc.l2tabs.compat.api;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public abstract class IAccessoriesWrapper {

	public final LivingEntity entity;
	public int total, page;

	public IAccessoriesWrapper(LivingEntity entity) {
		this.entity = entity;
	}

	public abstract int getSize();

	public abstract int getRows();

	@Nullable
	public abstract IAccessoriesSlotWrapper getSlotAtPosition(int i);

}
