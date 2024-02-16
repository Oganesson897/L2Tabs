package dev.xkmc.l2tabs.init.data;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Comparator;
import java.util.List;

@SerialClass
public record AttrDispEntry(boolean usePercent, int order, double intrinsic) {

	public static List<Pair<Attribute, AttrDispEntry>> get(LivingEntity le) {
		return L2Tabs.ATTRIBUTE_ENTRY.getAll()
				.filter(e -> le.getAttribute(e.getFirst()) != null)
				.sorted(Comparator.comparingInt(x -> x.getSecond().order))
				.toList();
	}

}
