package love.marblegate.homing_ender_eye.event;

import love.marblegate.homing_ender_eye.HomingEnderEye;
import love.marblegate.homing_ender_eye.misc.EyeThrowCache;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HomingEnderEye.MOD_ID)
public class CreateCacheHandler {
    @SubscribeEvent
    public static void load(WorldEvent.Load event){
        if(HomingEnderEye.EYE_THROW_CACHE == null){
            HomingEnderEye.EYE_THROW_CACHE = new EyeThrowCache();
        }
    }
}
