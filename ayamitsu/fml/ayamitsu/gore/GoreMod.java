package ayamitsu.gore;

import ayamitsu.gore.common.CommonProxy;
import ayamitsu.gore.common.EntityBloodStain;
import ayamitsu.gore.common.GoreRegistry;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
	modid = "GoreMod",
	name = "GoreMod",
	version = "1.1.0"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class GoreMod {

	@Mod.Instance("GoreMod")
	public static GoreMod instance;

	@SidedProxy(clientSide = "ayamitsu.gore.client.ClientProxy", serverSide = "ayamitsu.gore.common.CommonProxy")
	public static CommonProxy proxy;

	public static int bloodRed = 0x800000;
	public static int bloodBlue = 0x004c26;
	public static int bloodBlack = 0x101010;
	public static int stainId;

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
		conf.load();

		this.stainId = conf.get("entity.id.bloodstain", Configuration.CATEGORY_GENERAL, 102).getInt();
		String damage = "";
		damage = conf.get("entity.damage.near", Configuration.CATEGORY_GENERAL, "mob, player, cactus").getString();
		GoreRegistry.addNearDamage(this.splitToString(damage));
		damage = conf.get("entity.damage.far", Configuration.CATEGORY_GENERAL, "arrow").getString();
		GoreRegistry.addFarDamage(this.splitToString(damage));
		damage = conf.get("entity.damage.item", Configuration.CATEGORY_GENERAL, "256, 257, 258, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 283, 284, 285, 286").getString();
		GoreRegistry.addItemDamage(this.splitToInt(damage));

		conf.save();
	}

	@Mod.Init
	public void init(FMLInitializationEvent event)
	{
		this.proxy.load();
		EntityRegistry.registerGlobalEntityID(EntityBloodStain.class, "bloodstain", this.stainId);
		EntityRegistry.registerModEntity(EntityBloodStain.class, "bloodstain", this.stainId, this, 250, 6, false);
		proxy.load();
		LanguageRegistry.instance().addStringLocalization("entity.bloodstain.name", "en_US", "BloodStain");
	}

	private static String[] splitToString(String str)
	{
		String[] var1 = str.trim().split(",");

		for (int i = 0; i < var1.length; i++)
		{
			var1[i] = var1[i].trim();
		}

		return var1;
	}

	private static int[] splitToInt(String str)
	{
		String[] var1 = str.trim().split(",");
		int[] var2 = new int[var1.length];

		for (int i = 0; i < var1.length; i++)
		{
			try
			{
				var2[i] = Integer.parseInt(var1[i].trim());
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		return var2;
	}

}
