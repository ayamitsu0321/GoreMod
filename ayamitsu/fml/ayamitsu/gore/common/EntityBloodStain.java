package ayamitsu.gore.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityBloodStain extends Entity implements IEntityAdditionalSpawnData
{
	public int age;
	public int strength;
	public int type;
	private String texturePath = "/ayamitsu/gore/misc/blood_red.png";
	private boolean touchGround;

	public EntityBloodStain(World world)
	{
		this(world, (EntityLiving)null);
	}

	public EntityBloodStain(World world, EntityLiving living)
	{
		super(world);
		this.isImmuneToFire = true;
		this.setSize(1.1F, 1.1F);
		type = 0;
		this.strength = 100;
		this.age = 0;

		if (living != null)
		{
			this.texturePath = living.getBloodTexture();
		}
	}

	@Override
	protected void entityInit() {}

	@Override
	public void onUpdate()
	{
		this.age++;
		this.strength = 100 - this.age / 10;

		if (this.strength <= 0)
		{
			this.setDead();
		}

		if (this.onGround)
		{
			this.touchGround = true;
		}

		if (this.touchGround && !this.onValidSurface())
		{
			this.setDead();
		}
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public boolean isInRangeToRenderDist(double d)
	{
		return true;
	}

	public String getBloodTexture()
	{
		return this.texturePath;
	}

	public float getRange()
	{
		return (this.strength / 100F) * 64F;
	}

	public void setStrength(int i)
	{
		this.age = (100 - i) * 10;
		this.strength = 100 - this.age / 10;
	}

	public boolean onValidSurface()
	{
		if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0)
		{
			return false;
		}

		int tileX = MathHelper.floor_double(this.posX);
		int tileY = MathHelper.floor_double(this.posY);
		int tileZ = MathHelper.floor_double(this.posZ);

		if (!this.worldObj.getBlockMaterial(tileX, tileY - 1, tileZ).isSolid())
		{
			return false;
		}

		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		this.age = nbttagcompound.getShort("stain.Age");
		this.type = nbttagcompound.getShort("stain.Type");
		this.texturePath = nbttagcompound.getString("stain.TexturePath");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setShort("stain.Age", (short)this.age);
		nbttagcompound.setShort("stain.Type", (short)this.type);
		nbttagcompound.setString("stain.TexturePath", this.texturePath);
	}

// IEntityAdditionalSpawnData

	// server side
	@Override
	public void writeSpawnData(ByteArrayDataOutput bado) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeEntityToNBT(nbttagcompound);

		try
		{
			byte[] data = CompressedStreamTools.compress(nbttagcompound);
			bado.writeInt(data.length);
			bado.write(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// client side どうにかしたい
	@Override
	public void readSpawnData(ByteArrayDataInput badi) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try
		{
			int length = badi.readInt();
			byte[] data = new byte[length];
			badi.readFully(data);
			NBTTagCompound nbttagcompound = CompressedStreamTools.decompress(data);
			this.readEntityFromNBT(nbttagcompound);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
