package dev.xkmc.l2tabs.init.data;

import com.mojang.datafixers.util.Pair;
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
import java.util.stream.Collectors;

@SerialClass
public record AttrDispEntry(boolean usePercent, int order, double intrinsic) {

	public static List<Pair<Attribute, AttrDispEntry>> get(LivingEntity le) {
		var ans = L2Tabs.ATTRIBUTE_ENTRY.getAll()
				.filter(e -> le.getAttribute(e.getFirst()) != null)
				.sorted(Comparator.comparingInt(x -> x.getSecond().order))
				.toList();
		if (L2TabsConfig.CLIENT.generateAllAttributes.get()) {
			ans = new ArrayList<>(ans);
			ans.sort(Comparator.comparingInt(e -> e.getSecond().order));
			int latest = ans.isEmpty() ? 0 : ans.get(ans.size() - 1).getSecond().order();
			var set = ans.stream().map(Pair::getFirst).collect(Collectors.toSet());
			var all = new ArrayList<>(BuiltInRegistries.ATTRIBUTE.holders().map(e -> Pair.of(e.key().location(), e.value())).toList());
			all.sort(Comparator.<Pair<ResourceLocation, Attribute>, String>
							comparing(e -> e.getFirst().getNamespace())
					.thenComparing(e -> e.getFirst().getNamespace()));
			for (var e : all) {
				if (set.contains(e.getSecond())) continue;
				var ins = le.getAttribute(e.getSecond());
				if (ins == null) continue;
				if (ins.getModifiers().isEmpty()) {
					if (L2TabsConfig.CLIENT.generateAllAttributesHideUnchanged.get()) {
						continue;
					}
				}
				latest++;
				ans.add(Pair.of(e.getSecond(), new AttrDispEntry(false, latest, 0)));
			}
		}
		return ans;
	}

}
