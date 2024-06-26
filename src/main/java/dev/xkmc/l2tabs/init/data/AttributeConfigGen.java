package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class AttributeConfigGen extends DataMapProvider {

	public AttributeConfigGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
		super(output, lookup);
	}

	@Override
	protected void gather() {
		var b = builder(L2Tabs.ATTRIBUTE_ENTRY.reg());
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
	}

	public static void add(Builder<AttrDispEntry, Attribute> b, Holder<Attribute> attr, int order) {
		var rl = attr.unwrapKey();
		assert rl.isPresent();
		b.add(rl.get(), new AttrDispEntry(false, order, 0), false);
	}
}
