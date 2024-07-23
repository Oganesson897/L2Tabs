package dev.xkmc.l2tabs.compat.accessories;

import dev.xkmc.l2tabs.compat.api.INamedSlot;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.client.gui.AccessoriesInternalSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

class TabAccessoriesSlot extends AccessoriesInternalSlot implements INamedSlot {

	private final String identifier;
	private final LivingEntity entity;

	private final AccessoriesContainer handler;
	private final int index;

	public TabAccessoriesSlot(LivingEntity entity, AccessoriesContainer handler, int index, String identifier,
							  int xPosition, int yPosition) {
		super(0, handler, false, index, xPosition, yPosition);
		this.identifier = identifier;
		this.entity = entity;
		this.handler = handler;
		this.index = index;
	}

	public boolean isValid() {
		return handler.getSize() > index;
	}

	@Override
	public void set(@Nonnull ItemStack stack) {
		if (!isValid()) return;
		super.set(stack);
	}

	@Override
	public @NotNull ItemStack getItem() {
		if (!isValid()) return ItemStack.EMPTY;
		return super.getItem();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean mayPlace(@Nonnull ItemStack stack) {
		if (!isValid()) return false;
		return super.mayPlace(stack);
	}

	@Override
	public boolean mayPickup(Player playerIn) {
		if (!isValid()) return false;
		return super.mayPickup(playerIn);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		//set(getItem());
	}

	@Override
	public Component getName() {
		return getTooltipData().getFirst();
	}

}
