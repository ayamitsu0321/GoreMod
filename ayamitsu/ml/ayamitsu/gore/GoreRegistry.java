package ayamitsu.gore;

import net.minecraft.src.*;
import java.util.List;
import java.util.ArrayList;

public final class GoreRegistry
{
	private static boolean hasInit = false;
	
	public static float amountParticles = 0.1F;
	public static float ticksBleed = 0.1F;
	public static float ticksBleeding = 0.5F;
	public static float bloodSize = 0.5F;
	
	private static List<String> nearDamageList;
	private static List<String> farDamageList;
	private static List<Integer> itemDamageList;
	
	private static void init()
	{
		if (hasInit)
		{
			return;
		}
		
		hasInit = true;
		nearDamageList = new ArrayList<String>();
		farDamageList = new ArrayList<String>();
		itemDamageList = new ArrayList<Integer>();
	}
	
	public static List<String> getNearDamages()
	{
		return nearDamageList;
	}
	
	public static List<String> getFarDamage()
	{
		return farDamageList;
	}
	
	public static List<Integer> getItemDamage()
	{
		return itemDamageList;
	}
	
	public static boolean containsNearDamage(String damage)
	{
		return nearDamageList != null && nearDamageList.contains(damage);
	}
	
	public static boolean containsFarDamage(String damage)
	{
		return farDamageList != null && farDamageList.contains(damage);
	}
	
	public static boolean containsItemDamage(int id)
	{
		return itemDamageList != null && itemDamageList.contains(Integer.valueOf(id));
	}
	
	public static void addNearDamage(String[] damages)
	{
		if (damages == null)
		{
			return;
		}
		
		if (nearDamageList == null)
		{
			nearDamageList = new ArrayList<String>();
		}
		
		for (int i = 0; i < damages.length; i++)
		{
			String damageSource = damages[i];
			
			if (damageSource != null && damageSource != "")
			{
				nearDamageList.add(damageSource);
			}
		}
	}
	
	public static void addNearDamage(String damage)
	{
		if (damage != null && !damage.equals(""))
		{
			nearDamageList.add(damage);
		}
	}
	
	public static void addFarDamage(String[] damages)
	{
		if (damages == null)
		{
			return;
		}
		
		for (int i = 0; i < damages.length; i++)
		{
			String damageSource = damages[i];
			
			if (damageSource != null && damageSource != "")
			{
				farDamageList.add(damageSource);
			}
		}
	}
	
	public static void addFarDamage(String damage)
	{
		if (damage != null && !damage.equals(""))
		{
			farDamageList.add(damage);
		}
	}
	
	public static void addItemDamage(Integer[] ids)
	{
		if (ids == null)
		{
			return;
		}
		
		for (int i = 0; i < ids.length; i++)
		{
			Integer id = ids[i];
			
			if (id != null && id.intValue() < Item.itemsList.length)
			{
				itemDamageList.add(id);
			}
		}
	}
	
	public static void addItemDamage(int id)
	{
		if (id < Item.itemsList.length)
		{
			itemDamageList.add(Integer.valueOf(id));
		}
	}
	
	public static int nTransformVariable(String s)
    {
        if (s.equals("amountParticles"))
        {
            return (int)(amountParticles * 1000F);
        }

        if (s.equals("ticksBleed"))
        {
            return (int)(ticksBleed * 100F);
        }

        if (s.equals("ticksBleeding"))
        {
            return (int)(ticksBleeding * 100F);
        }
        else
        {
            return 0;
        }
    }
	
	public static float fTransformVariable(String s)
    {
        if (s.equals("bloodSize"))
        {
            return bloodSize * 3F + 1.0F;
        }
        else
        {
            return 0.0F;
        }
    }
	
	static
	{
		init();
	}
}