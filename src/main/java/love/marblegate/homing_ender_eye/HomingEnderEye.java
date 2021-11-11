package love.marblegate.homing_ender_eye;

import love.marblegate.homing_ender_eye.misc.EyeThrowCache;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HomingEnderEye.MOD_ID)
public class HomingEnderEye {
    public static final String MOD_ID = "homing_ender_eye";

    public static EyeThrowCache EYE_THROW_CACHE;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

    }
}
