package ayamitsu.gore.client;

import ayamitsu.gore.common.CommonProxy;
import ayamitsu.gore.common.EntityBloodStain;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void load()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBloodStain.class, new RenderBloodStain());
	}
}
