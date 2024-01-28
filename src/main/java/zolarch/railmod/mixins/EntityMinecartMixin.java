package zolarch.railmod.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zolarch.railmod.RailMod;

import java.util.List;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity {
	@Shadow
	public abstract void remove();

	@Shadow
	public int minecartType;

	@Shadow
	private ItemStack[] cargoItems;

	public EntityMinecartMixin(World world) {
		super(world);
	}

	@Inject(method = "tick",at = @At("TAIL"))
	private void chestMinecartPickupItem(CallbackInfo ci) {
		// Ensure it's a chest minecart (and handle server side)
		if (this.minecartType == 1 && !this.world.isClientSide) {
			List<Entity> nearby = this.world.getEntitiesWithinAABBExcludingEntity(this,this.bb.expand(0.1,0.2,0.1));
			if (nearby != null && !nearby.isEmpty()) {
				RailMod.LOGGER.debug("Nearby ChestCart entity count: " + nearby.size());
                for (int j = 0; j < nearby.size(); j++) {
					if (!nearby.get(j).removed && nearby.get(j) instanceof EntityItem) {
						EntityItem item = (EntityItem) nearby.get(j);
                        for (int i = 0; i < this.cargoItems.length; i++) {
							if (this.cargoItems[i] != null && this.cargoItems[i].canStackWith(item.item)) {
								// Found a slot of the same item
								if (this.cargoItems[i].stackSize < this.cargoItems[i].getMaxStackSize()) {
									// It *can* be incremented, do so
									this.cargoItems[i].stackSize++;
									item.remove();
									this.world.playSoundAtEntity(this, "random.pop", 0.2F,
										((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
									break;
								}
								// Otherwise, go to the next slot in line, this one is full
							} else if (this.cargoItems[i] == null) {
								// Found an empty slot, and there was not a duplicate earlier
								this.cargoItems[i] = item.item.copy();
								item.remove();
								this.world.playSoundAtEntity(this, "random.pop", 0.2F,
									((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                break;
                            }
                        }
					}
                }
			}
		}
	}
}
