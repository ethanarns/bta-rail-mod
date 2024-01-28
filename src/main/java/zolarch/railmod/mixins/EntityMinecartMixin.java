package zolarch.railmod.mixins;

import net.minecraft.core.block.BlockRail;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zolarch.railmod.RailMod;

import java.util.List;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity {
	@Shadow
	public abstract void remove();

	@Unique
	public int fastCountDown = 0;

	@Unique
	public double lastAbsVelX = 0;

	@Unique
	public double lastAbsVelZ = 0;

	@Shadow
	public int minecartType;

	@Shadow
	private ItemStack[] cargoItems;

	@Shadow
	public abstract Entity ejectRider();

	public EntityMinecartMixin(World world) {
		super(world);
	}

	@ModifyConstant(method = "onUpdate2", constant = @Constant(doubleValue = 0.4))
	private double speedMod(double input) {
		int i = MathHelper.floor_double(this.x);
		int j = MathHelper.floor_double(this.y);
		int k = MathHelper.floor_double(this.z);
		if (BlockRail.isRailBlockAt(this.world, i, j - 1, k)) {
			--j;
		}
		int l = this.world.getBlockId(i, j, k);
		if (l == RailMod.FAST_BLOCK_ID) {
			this.fastCountDown = 2;
			return 3.6;
		}
		return 0.4;
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
		// Passenger minecart on a fast block
		double xv = Math.abs(this.xd);
		double zv = Math.abs(this.zd);
		if (this.fastCountDown > 0) {
			this.fastCountDown--;
		}
		if (this.minecartType == 0 && this.yd < 0.01 && this.fastCountDown > 0) {
			RailMod.LOGGER.debug("On fast block " + xv + ", " + zv + " (lasts: " + this.lastAbsVelX + ", " + lastAbsVelZ);
			if (this.lastAbsVelX < 0.001 && this.lastAbsVelZ < 0.001) {
				// Just started, do nothing
				RailMod.LOGGER.debug("Just started");
			} else {
				if (xv > 0.001 && this.lastAbsVelX < 0.001) {
					RailMod.LOGGER.debug("Too fast a turn X!");
					this.ejectRider();
				}
				if (zv > 0.001 && this.lastAbsVelZ < 0.001) {
					RailMod.LOGGER.debug("Too fast a turn Z!");
					this.ejectRider();
				}
			}
		}
		this.lastAbsVelX = xv;
		this.lastAbsVelZ = zv;
	}
}
