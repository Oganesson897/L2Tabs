package dev.xkmc.l2tabs.compat.curios;

import dev.xkmc.l2tabs.compat.api.INamedSlot;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nonnull;

class TabCurioSlot extends SlotItemHandler implements INamedSlot {

	private final String identifier;
	private final LivingEntity entity;
	private final SlotContext slotContext;

	private final IDynamicStackHandler handler;
	private final int index;

	public TabCurioSlot(LivingEntity entity, IDynamicStackHandler handler, int index, String identifier,
						int xPosition, int yPosition, NonNullList<Boolean> renders) {
		super(handler, index, xPosition, yPosition);
		this.identifier = identifier;
		this.entity = entity;
		this.slotContext = new SlotContext(identifier, entity, index, false, renders.get(index));
		CuriosApi.getSlot(identifier, entity.level()).ifPresent((slotType) ->
				this.setBackground(InventoryMenu.BLOCK_ATLAS, slotType.getIcon()));
		this.handler = handler;
		this.index = index;
	}

	public boolean isValid() {
		return handler.getSlots() > index;
	}

	@Override
	public void set(@Nonnull ItemStack stack) {
		if (!isValid()) return;
		ItemStack current = this.getItem();
		boolean flag = current.isEmpty() && stack.isEmpty();
		((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
		this.setChanged();
		if (!flag && !ItemStack.matches(current, stack)) {
			CuriosApi.getCurio(stack)
					.ifPresent(curio -> curio.onEquipFromUse(this.slotContext));
		}
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
		((IItemHandlerModifiable) getItemHandler()).setStackInSlot(index, getItem());
	}

	@Override
	public Component getName() {
		return Component.translatable("curios.identifier." + this.identifier);
	}

}
