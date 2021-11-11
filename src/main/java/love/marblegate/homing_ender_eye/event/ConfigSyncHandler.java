package love.marblegate.homing_ender_eye.event;

import love.marblegate.homing_ender_eye.HomingEnderEye;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HomingEnderEye.MOD_ID)
public class ConfigSyncHandler {
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(HomingEnderEye.MOD_ID)) {
            ConfigManager.sync(HomingEnderEye.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
