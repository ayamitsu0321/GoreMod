package ayamitsu.gore;

import net.minecraft.src.*;

public class EntityBloodStain extends Entity
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
    		String name = EntityList.getEntityString(this.targetEntity);
    		
    		if (name != null && !name.equals(""))
    		{
    			entityNBT.setString("id", name);
    		}
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
}