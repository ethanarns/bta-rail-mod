package zolarch.railty;

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
import zolarch.railty.block.JumpRail;


public class Railty implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "railty";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Block jumpRailBlock;
	public static final String JUMP_RAIL_NAME = "jump_rail";
	public static final int JUMP_RAIL_ID = 6421;

    @Override
    public void onInitialize() {
        LOGGER.info("Railty initialized.");
    }

	@Override
	public void beforeGameStart() {
		jumpRailBlock = new BlockBuilder(MOD_ID)
			.setTextures("jumprail.png")
			.setBlockModel(new BlockModelRenderBlocks(9))
			.build(new JumpRail(JUMP_RAIL_NAME,JUMP_RAIL_ID,false));
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {
		// Increase the output of normal rail recipe
		RecipeBuilder.ModifyWorkbench("minecraft").removeRecipe("rail");
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("I I","ISI","I I")
			.addInput('I', Item.ingotIron)
			.addInput('S', Item.stick)
			.create("rail", new ItemStack(Block.rail,32));
		// Increase the output of powered rail recipe
		RecipeBuilder.ModifyWorkbench("minecraft").removeRecipe("powered_rail");
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("I I","ISI","IRI")
			.addInput('I', Item.ingotGold)
			.addInput('S', Item.stick)
			.addInput('R', Item.dustRedstone)
			.create("powered_rail", new ItemStack(Block.railPowered,16));
		// Make a more efficient recipe for powered rails (normal rails with redstone dust in the spaces)
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("IRI","ISI","IRI")
			.addInput('I', Item.ingotIron)
			.addInput('S', Item.stick)
			.addInput('R', Item.dustRedstone)
			.create("powered_rail_compact", new ItemStack(Block.railPowered,6));
		// Add Jump rails
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("I I","ISI","IPI")
			.addInput('I',Item.ingotIron)
			.addInput('S', Item.stick)
			.addInput('P', Block.railPowered)
			.create("jump_rail", new ItemStack(jumpRailBlock,4));
	}
}
