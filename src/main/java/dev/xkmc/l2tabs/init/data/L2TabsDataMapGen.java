package dev.xkmc.l2tabs.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.CuriosApi;

public class L2TabsDataMapGen {

	public static void onDataMapGen(RegistrateDataMapProvider pvd) {
		var b = pvd.builder(L2Tabs.ATTRIBUTE_ENTRY.reg());
		add(b, Attributes.MAX_HEALTH, 1000);
		add(b, Attributes.ARMOR, 2000);
		add(b, Attributes.ARMOR_TOUGHNESS, 3000);
		add(b, Attributes.KNOCKBACK_RESISTANCE, 4000);
		add(b, Attributes.MOVEMENT_SPEED, 5000);
		add(b, Attributes.ATTACK_DAMAGE, 6000);
		add(b, Attributes.ATTACK_SPEED, 7000);
		add(b, Attributes.BLOCK_INTERACTION_RANGE, 8000);
		add(b, Attributes.ENTITY_INTERACTION_RANGE, 9000);
		add(b, Attributes.LUCK, 10000);

		pvd.builder(L2Tabs.ICON.reg())
				.add(L2Tabs.TAB_INVENTORY.id(), Items.CRAFTING_TABLE, false)
				.add(L2Tabs.TAB_ATTRIBUTE.id(), Items.IRON_SWORD, false);

		pvd.builder(L2Tabs.ORDER.reg())
				.add(L2Tabs.TAB_INVENTORY.id(), 0, false)
				.add(L2Tabs.TAB_ATTRIBUTE.id(), 1000, false)
				.add(AccessoriesMultiplex.TAB_CURIOS.id(), 2000, false,
						new ModLoadedCondition(CuriosApi.MODID));
	}

	public static void add(DataMapProvider.Builder<AttrDispEntry, Attribute> b, Holder<Attribute> attr, int order) {
		var rl = attr.unwrapKey();
		assert rl.isPresent();
		b.add(rl.get(), new AttrDispEntry(false, order, 0), false);
	}

}
