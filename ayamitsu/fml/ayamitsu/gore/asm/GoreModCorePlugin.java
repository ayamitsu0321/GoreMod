package ayamitsu.gore.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions("ayamitsu.gore.asm")
public class GoreModCorePlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"ayamitsu.gore.asm.TransformerEntityLiving"
		};
	}

	@Override
	public String getModContainerClass() {
		return "ayamitsu.gore.asm.GoreModContainer";
	}

	@Override
	public String getSetupClass() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
