package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public class SyncAttributeToClient extends SerialPacketBase {

	@Deprecated
	public SyncAttributeToClient() {

	}

	@Override
	public void handle(NetworkEvent.Context context) {
		try {
			SyncAttributeHandler.handle(id, list);
		} catch (Exception e) {
			L2Library.LOGGER.throwing(e);
		}
	}

	@SerialClass.SerialField
	public int id;

	@SerialClass.SerialField
	public ArrayList<AttributeEntry> list = new ArrayList<>();

	public SyncAttributeToClient(LivingEntity le) {
		id = le.getId();
		for (var attr : le.getAttributes().getDirtyAttributes()) {
			ArrayList<ModifierEntry> modifiers = new ArrayList<>();
			for (var mod : attr.getModifiers()) {
				modifiers.add(new ModifierEntry(mod.getId(), mod.getName()));
			}
			if (modifiers.isEmpty()) continue;
			list.add(new AttributeEntry(attr.getAttribute(), modifiers));
		}
	}

	public record ModifierEntry(UUID id, String name) {

	}

	public record AttributeEntry(Attribute attr, ArrayList<ModifierEntry> list) {

	}

}
