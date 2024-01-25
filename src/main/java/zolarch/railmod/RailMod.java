package zolarch.railmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class RailMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "railmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("RailMod initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}
}
