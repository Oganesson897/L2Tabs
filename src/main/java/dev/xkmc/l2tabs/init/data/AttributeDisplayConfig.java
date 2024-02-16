package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.serial.config.ConfigLoadOnStart;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.contents.AttributeEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerialClass
@ConfigLoadOnStart
public class AttributeDisplayConfig extends BaseConfig {

	public static List<AttributeEntry> get() {
		return L2Tabs.ATTRIBUTE_ENTRY.getMerged().cache;
	}

	public static List<AttributeEntry> get(LivingEntity le) {
		return get().stream().filter(e -> le.getAttribute(e.attr()) != null).toList();
	}

	@ConfigCollect(CollectType.COLLECT)
	@SerialClass.SerialField
	protected final ArrayList<AttributeDataEntry> list = new ArrayList<>();

	private final ArrayList<AttributeEntry> cache = new ArrayList<>();

	@Override
	protected void postMerge() {
		for (var e : list) {
			if (!BuiltInRegistries.ATTRIBUTE.containsKey(e.id()))
				continue;
			Attribute attr = BuiltInRegistries.ATTRIBUTE.get(e.id());
			if (attr == null)
				continue;
			cache.add(new AttributeEntry(attr.setSyncable(true), e.usePercent(), e.order(), e.intrinsic()));
		}
		cache.sort(Comparator.comparingInt(AttributeEntry::order));
	}

	public AttributeDisplayConfig add(Attribute attr, boolean usePercent, int order, double intrinsic) {
		list.add(new AttributeDataEntry(BuiltInRegistries.ATTRIBUTE.getKey(attr), usePercent, order, intrinsic));
		return this;
	}

	public AttributeDisplayConfig add(Attribute attr, int order) {
		return add(attr, false, order, 0);
	}

	public record AttributeDataEntry(ResourceLocation id, boolean usePercent, int order, double intrinsic) {

	}

}
