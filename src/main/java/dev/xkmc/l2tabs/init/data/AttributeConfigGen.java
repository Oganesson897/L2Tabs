package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class AttributeConfigGen extends ConfigDataProvider {

	public AttributeConfigGen(DataGenerator generator) {
		super(generator, "L2Tabs Attribute Config Gen");
	}

	@Override
	public void add(Collector collector) {
		collector.add(L2Tabs.ATTRIBUTE_ENTRY,
				new ResourceLocation(L2Tabs.MODID, "vanilla"),
				new AttributeDisplayConfig()
						.add(Attributes.MAX_HEALTH, 1000)
						.add(Attributes.ARMOR, 2000)
						.add(Attributes.ARMOR_TOUGHNESS, 3000)
						.add(Attributes.KNOCKBACK_RESISTANCE, 4000)
						.add(Attributes.MOVEMENT_SPEED, 5000)
						.add(Attributes.ATTACK_DAMAGE, 6000)
						.add(Attributes.ATTACK_SPEED, 7000)
						.add(ForgeMod.BLOCK_REACH.get(), 8000)
						.add(ForgeMod.ENTITY_REACH.get(), 9000)
						.add(Attributes.LUCK, 10000)
		);
	}
}
