package zolarch.railty.mixins;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zolarch.railty.Railty;

@Mixin(value = BlockRail.class, remap = false)
public abstract class BlockRailMixin extends Block {

	public BlockRailMixin(String key, int id, Material material) {
		super(key, id, material);
	}

	@Inject(method = "isRailBlockAt", at = @At("HEAD"), cancellable = true)
	private static void isRailBlockAtFix(World world, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockId(i, j, k) == Railty.JUMP_RAIL_ID) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isRailBlock",  at = @At("HEAD"), cancellable = true)
	private static void isRailBlockFix(int i, CallbackInfoReturnable<Boolean> cir) {
		if (i == Railty.JUMP_RAIL_ID) {
			cir.setReturnValue(true);
		}
	}
}
