package love.marblegate.homing_ender_eye.misc;

import love.marblegate.homing_ender_eye.HomingEnderEye;
import net.minecraftforge.common.config.Config;

@Config(modid = HomingEnderEye.MOD_ID)
@Config.LangKey("config.homing_ender_eye.general")
public class Configuration {

    @Config.Comment("Should Ender Eye Destroy Event Only Belongs to Specific Player?")
    @Config.LangKey("config.homing_ender_eye.individual_mode")
    @Config.RequiresWorldRestart
    @Config.RequiresMcRestart
    public static boolean INDIVIDUAL_MODE = false;

    @Config.Comment("Scanning interval for nearby End Portal Frame. The unit is tick.")
    @Config.LangKey("config.homing_ender_eye.scanning.rate")
    @Config.RangeInt(min = 1, max = 1200)
    @Config.RequiresWorldRestart
    @Config.RequiresMcRestart
    public static int SCANNING_RATE = 300;

    @Config.Comment("Scanning Radius for nearby End Portal Frame.")
    @Config.LangKey("config.homing_ender_eye.scanning.radius")
    @Config.RequiresWorldRestart
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 160)
    public static int SCANNING_RADIUS = 30;

}
