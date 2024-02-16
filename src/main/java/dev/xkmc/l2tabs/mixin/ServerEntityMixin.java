package dev.xkmc.l2tabs.mixin;

import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(at = @At("HEAD"), method = "sendDirtyEntityData")
	public void l2tabs$sendDirtyData(CallbackInfo ci) {
		if (this.entity instanceof LivingEntity le) {
			L2Tabs.onAttributeUpdate(le);
		}
	}

}
