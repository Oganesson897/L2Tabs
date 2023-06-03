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
						.add(Attributes.MAX_HEALTH, false, 1000)
						.add(Attributes.ARMOR, false, 2000)
						.add(Attributes.ARMOR_TOUGHNESS, false, 3000)
						.add(Attributes.KNOCKBACK_RESISTANCE, false, 4000)
						.add(Attributes.MOVEMENT_SPEED, false, 5000)
						.add(Attributes.ATTACK_DAMAGE, false, 6000)
						.add(Attributes.ATTACK_SPEED, false, 7000)
						.add(ForgeMod.BLOCK_REACH.get(), false, 8000)
						.add(ForgeMod.ENTITY_REACH.get(), false, 9000)
						.add(Attributes.LUCK, false, 10000)
		);
	}
}
