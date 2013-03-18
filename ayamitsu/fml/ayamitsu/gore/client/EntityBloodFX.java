package ayamitsu.gore.client;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ayamitsu.gore.common.GoreRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityBloodFX extends EntityFX
{
	public EntityBloodFX(World world, double d, double d1, double d2, float f, float f1, float f2, EntityLiving living)
	{
		this(world, d, d1, d2, true, living);
	}

	public EntityBloodFX(World world, double d, double d1, double d2, boolean flag, EntityLiving living)
	{
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);

		if (flag)
		{
			this.motionX *= 0.60000002384185791D;
			this.motionY = -0.10000000149011612D;
			this.motionZ *= 0.60000002384185791D;
			this.moveEntity(motionX, motionY, motionZ);
		}
		else
		{
			Random random = new Random();

			if (random.nextInt() % 100 > 5)
			{
				this.motionX = (random.nextFloat() - 0.5F) * 0.25F;
				this.motionZ = (random.nextFloat() - 0.5F) * 0.25F;
				this.motionY = random.nextFloat() * 0.4F;
			}
			else
			{
				this.motionX = (random.nextFloat() - 0.5F) * 0.29F;
				this.motionZ = (random.nextFloat() - 0.5F) * 0.29F;
				this.motionY = random.nextFloat() * 0.4F;
			}
		}

		float[] colors = this.getColor(living.getBloodColor());
		this.particleRed = colors[0];
		this.particleGreen = colors[1];
		this.particleBlue = colors[2];
		this.particleScale *= GoreRegistry.BLOOD_SIZE;
		this.particleMaxAge = rand.nextInt() % 280 + 1;

		if (this.worldObj.isRaining())
		{
			this.particleMaxAge -= 150;
		}

		this.noClip = false;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public final float[] getColor(int hex)
	{
		return new float[] {
				((hex >> 0x10) & 0xFF) / 255.0F,
				((hex >> 0x08) & 0xFF) / 255.0F,
				((hex >> 0x00) & 0xFF) / 255.0F,
		};
	}

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge || this.posY < 0.01D)
		{
			this.setDead();
		}

		Material material = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

		if (material == Material.water)
		{
			this.setDead();
		}

		if (this.motionY > 0.0D)
		{
			this.motionY *= 0.55000000000000004D;

			if (this.motionY <= 0.01D)
			{
				this.motionY = -0.10000000000000001D;
			}
		}
		else
		{
			this.motionY *= 1.2690000534057617D;
		}

		if (this.motionY == 0.0D)
		{
			this.motionX = 0.0D;
			this.motionZ = 0.0D;
		}

		if (this.onGround)
		{
			this.motionX = 0.0D;
			this.motionY = 9.9999997473787516E-005D;
			this.motionZ = 0.0D;
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.prevPosY == this.posY)
		{
			this.posY -= 0.01D;
		}
	}
}