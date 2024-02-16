package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.util.Proxy;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;

public class SyncAttributeHandler {
	public static void handle(int id, ArrayList<SyncAttributeToClient.AttributeEntry> list) {
		var level = Proxy.getClientWorld();
		if (level == null) return;
		var entity = level.getEntity(id);
		if (!(entity instanceof LivingEntity le)) return;
		for (var attr : list) {
			var ins = le.getAttribute(attr.attr());
			if (ins == null) continue;
			for (var ent : attr.list()) {
				var mod = ins.getModifier(ent.id());
				if (mod == null) continue;
				mod.name = ent.name();
			}
		}
	}
}
