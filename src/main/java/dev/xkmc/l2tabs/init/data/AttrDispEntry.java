package dev.xkmc.l2tabs.init.data;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record AttrDispEntry(boolean usePercent, int order, double intrinsic) {

	public static List<Pair<Holder<Attribute>, AttrDispEntry>> get(LivingEntity le) {
		var ans = L2Tabs.ATTRIBUTE_ENTRY.getAll(le.level().registryAccess())
				.filter(e -> le.getAttribute(e.getFirst()) != null)
				.sorted(Comparator.comparingInt(x -> x.getSecond().order))
				.toList();
		if (L2TabsConfig.CLIENT.generateAllAttributes.get()) {
			ans = new ArrayList<>(ans);
			ans.sort(Comparator.comparingInt(e -> e.getSecond().order));
			int latest = ans.isEmpty() ? 0 : ans.getLast().getSecond().order();
			var set = ans.stream().map(Pair::getFirst).collect(Collectors.toSet());
			var all = new ArrayList<>(BuiltInRegistries.ATTRIBUTE.holders().toList());
			all.sort(Comparator.comparing(e -> e.key().location().toString()));
			for (var e : all) {
				if (set.contains(e)) continue;
				var ins = le.getAttribute(e);
				if (ins == null) continue;
				if (ins.getModifiers().isEmpty()) {
					if (L2TabsConfig.CLIENT.generateAllAttributesHideUnchanged.get()) {
						continue;
					}
				}
				latest++;
				ans.add(Pair.of(e, new AttrDispEntry(false, latest, 0)));
			}
		}
		return ans;
	}

}
