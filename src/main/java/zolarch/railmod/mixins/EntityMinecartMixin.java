package zolarch.railmod.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity {
	@Shadow
	public abstract void remove();

	@Shadow
	public int minecartType;

	public EntityMinecartMixin(World world) {
		super(world);
	}

	@Inject(method = "tick",at = @At("TAIL"))
	private void chestMinecartPickupItem(CallbackInfo ci) {
		// Ensure it's a chest minecart
		if (this.minecartType == 1) {
			List<Entity> nearby = this.world.getEntitiesWithinAABBExcludingEntity(this,this.bb.expand(1.0,1.0,1.0));
			if (nearby != null) {
                for (Entity near : nearby) {
					if (!near.removed && near instanceof EntityItem) {
						near.remove();
					}
                }
			}
		}
	}
}
