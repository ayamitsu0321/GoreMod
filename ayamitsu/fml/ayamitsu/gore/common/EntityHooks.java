package ayamitsu.gore.common;

import ayamitsu.gore.client.EntityBloodFX;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public final class EntityHooks {

	public static void onUpdateBlood(EntityLiving living)
	{
		if (!living.isDead && living.isBleeding)
		{
			if (living.ticksNoBlood > GoreRegistry.TICKS_BLEED)
			{
				produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * 0.80000000000000004D), true, living);
				living.ticksActuallyBleeding += living.ticksNoBlood;
				living.ticksNoBlood = 0;
			}

			if (((living instanceof EntityPlayer) || (living instanceof EntityPlayerSP)) && (living.getHealth() < 4 && living.ticksNoBlood > GoreRegistry.TICKS_BLEED / 2.0F || living.getHealth() < 10 && living.ticksNoBlood > GoreRegistry.TICKS_BLEED))
            {
                produceBlood(GoreRegistry.AMOUT_PARTICLES / 3, true, living);
                living.ticksNoBlood = 0;
            }

            if (living.ticksActuallyBleeding >= GoreRegistry.TICKS_BLEEDING)
            {
            	living.isBleeding = false;
            }

            living.ticksNoBlood++;
		}
	}

	public static void onAttackEntityFrom(DamageSource damage, EntityLiving living)
	{
		if (living.getHealth() > 0 && living.hurtTime <= 0)
    	{
    		float f = 0.41F;

    		if (living.canBleed() && living.canBleedByDamage(damage))
        	{
        		Entity entity = damage.getEntity();

        		if (GoreRegistry.containsFarDamage(damage.damageType))
        		{
        			produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
        		}
        		else if (entity instanceof EntityPlayer)
        		{
        			ItemStack is = ((EntityPlayer)entity).getCurrentEquippedItem();

        			if (living.canHurtByItem(is))
        			{
        				f = 1.0F;
        				living.isBleeding = true;
        				living.ticksActuallyBleeding = 0;
        				living.ticksNoBlood = 0;
        				produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
        			}
        		}
        		else
        		{
        			produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
        		}
        	}
    	}
	}

	public static void onDeath(DamageSource damage, EntityLiving living)
	{
		float f = 0.41F;

    	if (living.canBleed() && living.canBleedByDamage(damage))
    	{
    		Entity entity = damage.getEntity();

    		if (GoreRegistry.containsFarDamage(damage.damageType))
    		{
    			produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
    		}
    		else if (entity instanceof EntityPlayer)
    		{
    			ItemStack is = ((EntityPlayer)entity).getCurrentEquippedItem();

    			if (living.canHurtByItem(is))
    			{
    				f = 1.0F;
    				living.isBleeding = true;
					living.ticksActuallyBleeding = 0;
					living.ticksNoBlood = 0;
    				produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
    			}
    		}
    		else
    		{
    			produceBlood(MathHelper.floor_double(GoreRegistry.AMOUT_PARTICLES * f), false, living);
    		}
    	}
	}

	public static void produceBlood(int i, boolean flag, EntityLiving living)
	{
		if (flag)
		{
			i /= 3;
		}

		if (living.worldObj.isRemote)
		{
			for (int j = 0; j < i; j++)
			{
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityBloodFX(living.worldObj, living.posX + (living.width / 2.0F), living.posY + (living.height / 2.0F), living.posZ + (living.width / 2.0F), flag, living));
			}
		}
		else
		{
			spawnBloodStain(living);
		}
	}

	public static boolean canSpawnTrace(World world, int i, int j, int k)
    {
        int l = Block.pressurePlatePlanks.blockID;
        return world.getBlockId(i - 1, j, k) != l;
    }

	public static void spawnBloodStain(EntityLiving living)
	{
		if (!canSpawnTrace(living.worldObj, MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY), MathHelper.floor_double(living.posZ)))
		{
			return;
		}

		double d = (living.posY - living.yOffset) + 0.0D;
        EntityBloodStain bloodstain = new EntityBloodStain(living.worldObj, living);
        bloodstain.setPosition(living.posX, d, living.posZ);
        living.worldObj.spawnEntityInWorld(bloodstain);
        bloodstain.setStrength(70);
        bloodstain.type = 0;
	}

	public static boolean defaultCanBleedByDamage(DamageSource damagesource)
	{
		String damageType = damagesource.damageType;
		return GoreRegistry.containsNearDamage(damageType) || GoreRegistry.containsFarDamage(damageType);
	}

	public static boolean defaultCanBleed(EntityLiving living)
	{
		if (living instanceof EntitySkeleton || living instanceof EntityGolem || living instanceof EntitySlime)
		{
			return false;
		}

		return true;
	}

	public static boolean defaultCanHurtByItem(ItemStack is)
	{
		return is != null && GoreRegistry.containsItemDamage(is.itemID);
	}

	public static String defaultGetBloodTexture(EntityLiving living)
	{
		return living instanceof EntitySquid ? "/ayamitsu/gore/misc/ink.png" : living.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD ? "/ayamitsu/gore/misc/blood_blue.png" : "/ayamitsu/gore/misc/blood_red.png";
	}

	public static int defaultGetBloodColor(EntityLiving living)
	{
		return living instanceof EntitySquid ? 0x101010 : living.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD ? 0x004c26 : 0x800000;
	}

}
