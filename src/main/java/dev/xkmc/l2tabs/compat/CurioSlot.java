package dev.xkmc.l2tabs.compat;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.mixin.core.AccessorEntity;

import javax.annotation.Nonnull;

public class CurioSlot extends SlotItemHandler {

	private final String identifier;
	private final Player player;
	private final SlotContext slotContext;

	private NonNullList<Boolean> renderStatuses;
	private boolean canToggleRender;

	public CurioSlot(Player player, IDynamicStackHandler handler, int index, String identifier,
					 int xPosition, int yPosition, NonNullList<Boolean> renders,
					 boolean canToggleRender) {
		super(handler, index, xPosition, yPosition);
		this.identifier = identifier;
		this.renderStatuses = renders;
		this.player = player;
		this.canToggleRender = canToggleRender;
		this.slotContext = new SlotContext(identifier, player, index, false, renders.get(index));
		this.setBackground(InventoryMenu.BLOCK_ATLAS, CuriosApi.getSlotIcon(identifier));
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public boolean canToggleRender() {
		return this.canToggleRender;
	}

	public boolean getRenderStatus() {

		if (!this.canToggleRender) {
			return true;
		}
		return this.renderStatuses.size() > this.getSlotIndex() &&
				this.renderStatuses.get(this.getSlotIndex());
	}

	@OnlyIn(Dist.CLIENT)
	public String getSlotName() {
		return I18n.get("curios.identifier." + this.identifier);
	}

	@Override
	public void set(@Nonnull ItemStack stack) {
		ItemStack current = this.getItem();
		boolean flag = current.isEmpty() && stack.isEmpty();
		super.set(stack);

		if (!flag && !ItemStack.matches(current, stack) &&
				!((AccessorEntity) this.player).getFirstTick()) {
			CuriosApi.getCurio(stack)
					.ifPresent(curio -> curio.onEquipFromUse(this.slotContext));
		}
	}

	@Override
	public boolean allowModification(@Nonnull Player pPlayer) {
		return true;
	}
}
