package ayamitsu.gore.client;

import ayamitsu.gore.common.*;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void load()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBloodStain.class, new RenderBloodStain());
	}
}
