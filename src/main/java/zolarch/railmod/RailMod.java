package zolarch.railmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class RailMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "railmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("RailMod (Temporary name) initialized.");
    }

	@Override
	public void beforeGameStart() {

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
	}
}
