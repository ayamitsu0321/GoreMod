package net.minecraft.src;

import ayamitsu.gore.*;

import java.util.Map;

public class mod_GoreMod extends BaseMod
{
	@MLProp(info="blood color-red")
	public static int bloodRed = 0x800000;
	
	@MLProp(info="blood color-blue")
	public static int bloodBlue = 0x004c26;
	
	@MLProp(info="blood color-black")
	public static int bloodBlack = 0x101010;
	
	@MLProp(info = "blood staic entity id")
	public static int stainId = 102;
	
	@MLProp(info="near damage source")
	public static String nearDamage = "mob, player, cactus";
	
	@MLProp(info="far damage source")
	public static String farDamage = "arrow";
	
	@MLProp(info="item damage ids")
	public static String itemDamage = "256, 257, 258, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 283, 284, 285, 286";

	@Override
	public String getVersion()
	{
		return "1.5.0-v1.1.0";
	}
	
	@Override
	public void load()
	{
		ModLoader.registerEntityID(EntityBloodStain.class, "bloodstain", this.stainId);
		ModLoader.addLocalization("entity.bloodstain.name", "en_US", "BloodStain");
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
	
	@Override
	public void addRenderer(Map map)
	{
		map.put(EntityBloodStain.class, new RenderBloodStain());
	}
}