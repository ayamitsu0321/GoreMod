package ayamitsu.gore;

import ayamitsu.gore.common.*;

import net.minecraft.src.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import java.util.logging.Level;
import java.util.BitSet;

@Mod(
	modid = "GoreMod",
	name = "GoreMod",
	version = "1.0.0"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class GoreMod
{
	@Mod.Instance("GoreMod")
	public static GoreMod instance;
	
	@SidedProxy(clientSide = "ayamitsu.gore.client.ClientProxy", serverSide = "ayamitsu.gore.common.CommonProxy")
	public static CommonProxy proxy;
	
	public static int stainId;
	public static int bloodRed = 0x800000;
	public static int bloodBlue = 0x004c26;
	public static int bloodBlack = 0x101010;
	public static String nearDamage = "mob, player, cactus";
	public static String farDamage = "arrow";
	public static String itemDamage = "256, 257, 258, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 283, 284, 285, 286";
	
	
	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		
		try
		{
			cfg.load();
			this.stainId = cfg.get("entity.id.bloodstain", Configuration.CATEGORY_GENERAL, 102).getInt();
			this.nearDamage = cfg.get("entity.damage.near", Configuration.CATEGORY_GENERAL, "mob, player, cactus").value;
			this.farDamage = cfg.get("entity.damage.far", Configuration.CATEGORY_GENERAL, "arrow").value;
			this.itemDamage = cfg.get("entity.damage.item", Configuration.CATEGORY_GENERAL, "256, 257, 258, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 283, 284, 285, 286").value;
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}
	}
	
	@Mod.Init
	public void init(FMLInitializationEvent event)
	{
		EntityRegistry.registerGlobalEntityID(EntityBloodStain.class, "bloodstain", this.stainId);
		EntityRegistry.registerModEntity(EntityBloodStain.class, "bloodstain", this.stainId, this, 250, 6, false);
		proxy.load();
		LanguageRegistry.instance().addStringLocalization("entity.bloodstain.name", "en_US", "BloodStain");
		
		String[] near = this.nearDamage.split(",");
		
		for (int i = 0; i < near.length; i++)
		{
			near[i] = near[i].trim();
		}
		
		String[] far = this.farDamage.split(",");
		
		for (int i = 0; i < far.length; i++)
		{
			far[i] = far[i].trim();
		}
		
		String[] idsString = this.itemDamage.split(",");
		Integer[] ids = new Integer[idsString.length];
		
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = Integer.decode(idsString[i].trim());
		}
		
		GoreRegistry.addNearDamage(near);
		GoreRegistry.addFarDamage(far);
		GoreRegistry.addItemDamage(ids);
	}
}