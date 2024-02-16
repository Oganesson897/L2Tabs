package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public record SyncAttributeToClient(int id, ArrayList<AttributeEntry> list)
		implements SerialPacketBase<SyncAttributeToClient> {

	@Override
	public void handle(@Nullable Player player) {
		try {
			SyncAttributeHandler.handle(id, list);
		} catch (Exception e) {
			L2Library.LOGGER.throwing(e);
		}
	}

	public static SyncAttributeToClient of(LivingEntity le) {
		int id = le.getId();
		ArrayList<AttributeEntry> list = new ArrayList<>();
		for (var attr : le.getAttributes().getDirtyAttributes()) {
			ArrayList<ModifierEntry> modifiers = new ArrayList<>();
			for (var mod : attr.getModifiers()) {
				modifiers.add(new ModifierEntry(mod.getId(), mod.name));
			}
			if (modifiers.isEmpty()) continue;
			list.add(new AttributeEntry(attr.getAttribute(), modifiers));
		}
		return new SyncAttributeToClient(id, list);
	}

	public record ModifierEntry(UUID id, String name) {

	}

	public record AttributeEntry(Attribute attr, ArrayList<ModifierEntry> list) {

	}

}
