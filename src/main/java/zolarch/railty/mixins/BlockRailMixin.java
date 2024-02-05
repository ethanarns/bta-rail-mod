package zolarch.railty.mixins;

import com.google.common.base.Strings;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
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
		int blockId = world.getBlockId(i, j, k);
		if (blockId == Railty.JUMP_RAIL_ID || blockId == Railty.BASIC_RAIL_ID) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isRailBlock",  at = @At("HEAD"), cancellable = true)
	private static void isRailBlockFix(int i, CallbackInfoReturnable<Boolean> cir) {
		if (i == Railty.JUMP_RAIL_ID || i == Railty.BASIC_RAIL_ID) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "getBlockTextureFromSideAndMetadata", at = @At("HEAD"))
	public void getBlockTextureFromSideAndMetadataFix(Side side, int data, CallbackInfoReturnable<Integer> cir) {
		Railty.LOGGER.warn("getBlockTextureFromSideAndMetadata: " + Strings.padStart(Integer.toBinaryString(data),4,'0'));
	}
	// Rail block data values:
	// Flat, straight, north-south: 0
	// Flat, straight, east-west: 1
	// Sloped, east-west: 3
	// Sloped, north-south: 4
	// The curved blocks, flat: 6, 7, 8, 9

}
