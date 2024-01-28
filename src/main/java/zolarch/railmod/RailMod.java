package zolarch.railmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import zolarch.railmod.block.FastRail;


public class RailMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "railmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Block fastRailBlock;
	public static final String FAST_BLOCK_NAME = "rail_fast";
	public static final int FAST_BLOCK_ID = 6420;

    @Override
    public void onInitialize() {
        LOGGER.info("Railty initialized.");
    }

	@Override
	public void beforeGameStart() {
		fastRailBlock = new BlockBuilder(MOD_ID)
			.setTextures("fastrail.png")
			.setBlockModel(new BlockModelRenderBlocks(9))
			.build(new FastRail(FAST_BLOCK_NAME, FAST_BLOCK_ID, false));
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {
		// Increase the output of normal rails
		RecipeBuilder.ModifyWorkbench("minecraft").removeRecipe("rail");
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("I I","ISI","I I")
			.addInput('I', Item.ingotIron)
			.addInput('S', Item.stick)
			.create("rail", new ItemStack(Block.rail,32));
		// Increase the output of normal powered rail recipe
		RecipeBuilder.ModifyWorkbench("minecraft").removeRecipe("powered_rail");
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("I I","ISI","IRI")
			.addInput('I', Item.ingotGold)
			.addInput('S', Item.stick)
			.addInput('R', Item.dustRedstone)
			.create("powered_rail", new ItemStack(Block.railPowered,16));
		// Make a more efficient recipe for powered rails
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("IRI","ISI","IRI")
			.addInput('I', Item.ingotIron)
			.addInput('S', Item.stick)
			.addInput('R', Item.dustRedstone)
			.create("powered_rail_compact", new ItemStack(Block.railPowered,6));
		// Make fast rails
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("IRI","ISI","IRI")
			.addInput('I', Item.ingotIron)
			.addInput('S', Item.stick)
			.addInput('R', Block.glass)
			.create("rail_fast", new ItemStack(fastRailBlock,32));
	}
}
