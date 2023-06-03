package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.contents.AttributeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerialClass
public class AttributeDisplayConfig extends BaseConfig {

	public static List<AttributeEntry> get() {
		return L2Tabs.ATTRIBUTE_ENTRY.getMerged().cache;
	}

	@ConfigCollect(CollectType.COLLECT)
	@SerialClass.SerialField
	protected final ArrayList<AttributeDataEntry> list = new ArrayList<>();

	private final ArrayList<AttributeEntry> cache = new ArrayList<>();

	@Override
	protected void postMerge() {
		for (var e : list) {
			if (!ForgeRegistries.ATTRIBUTES.containsKey(e.id()))
				continue;
			Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(e.id());
			if (attr == null)
				continue;
			cache.add(new AttributeEntry(attr.setSyncable(true), e.usePercent(), e.order()));
		}
		cache.sort(Comparator.comparingInt(AttributeEntry::order));
	}

	public AttributeDisplayConfig add(Attribute attr, boolean usePercent, int order) {
		list.add(new AttributeDataEntry(ForgeRegistries.ATTRIBUTES.getKey(attr), usePercent, order));
		return this;
	}

	public record AttributeDataEntry(ResourceLocation id, boolean usePercent, int order) {

	}

}
