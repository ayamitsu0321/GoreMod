package ayamitsu.gore;

import net.minecraft.src.*;
/*import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.ByteArrayOutputStream;*/

public class EntityBloodStain extends Entity// implements IEntityAdditionalSpawnData
{
	public int type;
	public boolean isUsed;
    public int strength;
    public int age;
	private boolean touchGround = false;
	private EntityLiving targetEntity;
	
	public EntityBloodStain(World world)
	{
		this(world, (EntityLiving)null);
	}
	
    public EntityBloodStain(World world, EntityLiving entity)
    {
        super(world);
        this.type = 0;
        this.isUsed = false;
        this.isImmuneToFire = true;
        this.setSize(1.1F, 1.1F);
    	//this.setSize(1.0F, 1.0F);
    	this.strength = 100;
        this.age = 0;
    	this.targetEntity = entity;
    }
	
	@Override
	public void setDead()
    {
        super.setDead();
    }
	
	// add
	public EntityLiving getEntity()
	{
		return this.targetEntity;
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
	
	@Override
	public void entityInit() {}
	
	public float getRange()
    {
        return ((float)this.strength / 100F) * 64F;
    }
	
	public void setStrength(int i)
    {
        this.age = (100 - i) * 10;
        this.strength = 100 - this.age / 10;
    }
	
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
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("age", (short)this.age);
        nbttagcompound.setShort("type", (short)this.type);
    	NBTTagCompound entityNBT = new NBTTagCompound();
    	
    	if (this.targetEntity != null)
    	{
    		this.targetEntity.writeToNBT(entityNBT);
    		entityNBT.setString("id", EntityList.getEntityString(this.targetEntity));
    	}
    	
    	nbttagcompound.setCompoundTag("Target", entityNBT);
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.age = nbttagcompound.getShort("age");
        this.type = nbttagcompound.getShort("type");
    	NBTTagCompound entityNBT = nbttagcompound.getCompoundTag("Target");
    	
    	if (entityNBT != null)
    	{
    		this.targetEntity = (EntityLiving)EntityList.createEntityFromNBT(entityNBT, this.worldObj);
    	}
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
	
// IEntityAdditionalSpawnData
	
	/**
	 * パケットに書き込み、たぶんServerSide
	 */
	/*@Override
	public void writeSpawnData(ByteArrayDataOutput bado)
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		
		try
		{
			byte[] data = CompressedStreamTools.compress(nbttagcompound);
			bado.write(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}*/
	
	/**
	 * パケットの読み込み、たぶんClientSide
	 */
	/*@Override
	public void readSpawnData(ByteArrayDataInput badi)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try
		{
			try
			{
				while (true)
				{
					baos.write(badi.readByte());
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			catch (IllegalStateException e1) {}
			
			byte[] data = baos.toByteArray();
			NBTTagCompound nbttagcompound = CompressedStreamTools.decompress(data);
			this.readFromNBT(nbttagcompound);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}*/
}