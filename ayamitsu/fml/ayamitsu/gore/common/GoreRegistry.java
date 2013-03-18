package ayamitsu.gore.common;

import java.util.ArrayList;
import java.util.List;

public final class GoreRegistry
{
	public static final int AMOUT_PARTICLES = 100;
	public static final int TICKS_BLEED = 10;
	public static final int TICKS_BLEEDING = 50;
	public static final float BLOOD_SIZE = 2.5F;

	private static List<String> nearDamageList = new ArrayList<String>();
	private static List<String> farDamageList = new ArrayList<String>();
	private static List<Integer> itemDamageList = new ArrayList<Integer>();

	public static void addNearDamage(String ... arrayOfString)
	{
		for (String str : arrayOfString)
		{
			nearDamageList.add(str);
		}
	}

	public static void addFarDamage(String ... arrayOfString)
	{
		for (String str : arrayOfString)
		{
			farDamageList.add(str);
		}
	}

	public static void addItemDamage(int ... ids)
	{
		for (int id : ids)
		{
			itemDamageList.add(id);
		}
	}

	public static boolean containsNearDamage(String damage)
	{
		return damage != null && nearDamageList.contains(damage);
	}

	public static boolean containsFarDamage(String damage)
	{
		return damage != null && farDamageList.contains(damage);
	}

	public static boolean containsItemDamage(int id)
	{
		return itemDamageList.contains(Integer.valueOf(id));
	}
}
