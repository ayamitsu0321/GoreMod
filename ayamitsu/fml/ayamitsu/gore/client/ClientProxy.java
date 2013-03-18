package ayamitsu.gore.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import ayamitsu.gore.common.CommonProxy;
import ayamitsu.gore.common.EntityBloodStain;

public class ClientProxy extends CommonProxy
{
	@Override
	public void load()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBloodStain.class, new RenderBloodStain());
	}
}
