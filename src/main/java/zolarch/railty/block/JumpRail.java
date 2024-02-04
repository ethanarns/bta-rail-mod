package zolarch.railty.block;

import net.minecraft.core.block.BlockRail;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import turniplabs.halplibe.helper.TextureHelper;
import zolarch.railty.Railty;

public class JumpRail extends BlockRail {

	public JumpRail(String key, int id, boolean isPowered) {
		super(key, id, isPowered);
	}

	@Override
	public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
		return TextureHelper.getOrCreateBlockTextureIndex(Railty.MOD_ID,"jumprail.png");
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(Side side, int data) {
		return TextureHelper.getOrCreateBlockTextureIndex(Railty.MOD_ID,"jumprail.png");
	}
}
