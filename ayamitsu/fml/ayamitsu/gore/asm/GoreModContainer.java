package ayamitsu.gore.asm;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class GoreModContainer extends DummyModContainer {

	public GoreModContainer() {
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId       = "goremodplugin";
		meta.name        = "GoreModPlugin";
		meta.version     = "1.1.0";
		meta.authorList  = Arrays.asList("ayamitsu");
		meta.description = "";
		meta.url         = "";
		meta.credits     = "";
	}

}
