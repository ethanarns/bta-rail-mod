package zolarch.railty.block;

import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.logic.RailLogic;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import turniplabs.halplibe.helper.TextureHelper;
import zolarch.railty.Railty;

public class BlockRailtyBase extends BlockRail {
	public BlockRailtyBase(String key, int id, boolean isPowered) {
		super(key, id, isPowered);
	}

	// These are fixed with mixins
	// public static final boolean isRailBlockAt(World world, int i, int j, int k)
	// public static boolean isRailBlock(int i)

	public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
		return TextureHelper.getOrCreateBlockTextureIndex(Railty.MOD_ID,"basicrail.png");
	}

	public int getBlockTextureFromSideAndMetadata(Side side, int data) {
		// Curved data IDs are 6, 7, 8, and 9
		if (data >= 6 && data <= 9) {
			return TextureHelper.getOrCreateBlockTextureIndex(Railty.MOD_ID,"basicrail_turn.png");
		}
		return TextureHelper.getOrCreateBlockTextureIndex(Railty.MOD_ID,"basicrail.png");
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		this.updateRail(world, x, y, z);
	}

	// Was func_4031_h
	private void updateRail(World world, int i, int j, int k) {
		if (!world.isClientSide) {
			(new RailLogic(this, world, i, j, k)).func_792_a(world.isBlockIndirectlyGettingPowered(i, j, k), true);
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		// Don't do anything client side
		if (world.isClientSide) {
			return;
		}
		// Was the block this was placed on destroyed?
		if (!world.canPlaceOnSurfaceOfBlock(x, y - 1, z)) {
			this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), null);
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

}
