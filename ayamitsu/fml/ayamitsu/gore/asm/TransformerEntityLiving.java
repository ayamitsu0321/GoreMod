package ayamitsu.gore.asm;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerEntityLiving implements IClassTransformer, Opcodes
{
	// for 1.5.0
	private static final String ENTITYLIVING_CLASS_NAME_PERIOD = "net.minecraft.entity.EntityLiving";
	private static final String ENTITYLIVING_CLASS_NAME = "net/minecraft/entity/EntityLiving";// ng
	private static final String DAMAGESOURCE_CLASS_NAME = "net/minecraft/util/DamageSource";// mg
	private static final String ITEMSTACK_CLASS_NAME = "net/minecraft/item/ItemStack";// wq

	private boolean hasInit = false;

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (hasInit || !ENTITYLIVING_CLASS_NAME_PERIOD.equals(transformedName))
		{
			return bytes;
		}

		System.out.println("Found EntityLiving");
		this.hasInit = true;

		try
		{
			return this.transformEntityLiving(bytes);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to transform EntityLiving", e);
		}
	}

	private byte[] transformEntityLiving(byte[] arrayOfByte)
	{
		ClassNode cNode = this.encode(arrayOfByte);

		for (MethodNode mNode : cNode.methods)
		{
			String mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(cNode.name, mNode.name, mNode.desc);
			String mappedMethodDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(mNode.desc);

			// attackEntityFrom(DamageSource, int)
			if ("func_70097_a".equals(mappedMethodName) && ("(L" + DAMAGESOURCE_CLASS_NAME + ";I)Z").equals(mappedMethodDesc))
			{
				InsnList insnList = new InsnList();
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "onAttackEntityFrom", "(L" + DAMAGESOURCE_CLASS_NAME + ";L" + ENTITYLIVING_CLASS_NAME + ";)V"));
				mNode.instructions.insertBefore(mNode.instructions.get(0), insnList);
				ASMDebugUtils.log("Override attackEntityFrom");
			}

			// onDeath(DamageSource)
			if ("func_70645_a".equals(mappedMethodName) && ("(L" + DAMAGESOURCE_CLASS_NAME + ";)V").equals(mappedMethodDesc))
			{
				InsnList insnList = new InsnList();
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "onDeath", "(L" + DAMAGESOURCE_CLASS_NAME + ";L" + ENTITYLIVING_CLASS_NAME + ";)V"));
				mNode.instructions.insertBefore(mNode.instructions.get(0), insnList);
				ASMDebugUtils.log("Override onDeath");
			}

			// onUpdate()
			if ("func_70071_h_".equals(mappedMethodName) && ("()V").equals(mappedMethodDesc))
			{
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++)
				{
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof MethodInsnNode)
					{
						MethodInsnNode miNode = (MethodInsnNode)aiNode;
						String mappedMethodNameInsn = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(cNode.name, miNode.name, miNode.desc);

						// onLivingUpdate
						if ("func_70636_d".equals(mappedMethodNameInsn) && ("()V").equals(miNode.desc))
						{
							InsnList overrideList = new InsnList();
							overrideList.add(new VarInsnNode(ALOAD, 0));// this
							overrideList.add(new MethodInsnNode(INVOKEVIRTUAL, cNode.name, "updateBlood", "()V"));
							mNode.instructions.insert(aiNode, overrideList);
							ASMDebugUtils.log("Override onUpdate");
							break;
						}
					}
				}
			}
		}

		cNode.fields.add(new FieldNode(ACC_PUBLIC, "isBleeding", "Z", null, null));
		cNode.fields.add(new FieldNode(ACC_PUBLIC, "ticksNoBlood", "I", null, null));
		cNode.fields.add(new FieldNode(ACC_PUBLIC, "ticksActuallyBleeding", "I", null, null));

		/**Add
		 * 		public void updateBlood()
		 * 		{
		 * 			ayamitsu.gore.common.EntityHooks.onUpdateBlood(this);
		 * 		}
		 */
		MethodNode mNode = new MethodNode(ASM4, ACC_PUBLIC, "updateBlood", "()V", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 0));// this
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "onUpdateBlood", "(L" + ENTITYLIVING_CLASS_NAME + ";)V"));
		mNode.instructions.add(new InsnNode(RETURN));
		cNode.methods.add(mNode);

		/**Add
		 * 		public boolean canBleed()
		 * 		{
		 * 			ayamitsu.gore.common.EntityHooks.defaultCanBleed(this);
		 * 		}
		 */
		mNode = new MethodNode(ASM4, ACC_PUBLIC, "canBleed", "()Z", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 0));// this
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "defaultCanBleed", "(L" + ENTITYLIVING_CLASS_NAME + ";)Z"));
		mNode.instructions.add(new InsnNode(IRETURN));
		cNode.methods.add(mNode);

		/**Add
		 * 		public boolean canBleedByDamage(DamageSource damage)
		 * 		{
		 * 			return ayamitsu.gore.common.EntityHooks.defaultCanBleedByDamage(damage);
		 * 		}
		 */
		mNode = new MethodNode(ASM4, ACC_PUBLIC, "canBleedByDamage", "(L" + DAMAGESOURCE_CLASS_NAME + ";)Z", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 1));// par1
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "defaultCanBleedByDamage", "(L" + DAMAGESOURCE_CLASS_NAME + ";)Z"));
		mNode.instructions.add(new InsnNode(IRETURN));
		cNode.methods.add(mNode);

		/**ADD:
		 * 		public boolean canHurtByItem(ItemStack itemStack)
		 * 		{
		 * 			return ayamitsu.gore.common.EntityHooks.defaultCanHurtByItem(itemStack);
		 * 		}
		 */
		mNode = new MethodNode(ASM4, ACC_PUBLIC, "canHurtByItem", "(L" + ITEMSTACK_CLASS_NAME + ";)Z", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 1));// par1
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "defaultCanHurtByItem", "(L" + ITEMSTACK_CLASS_NAME + ";)Z"));
		mNode.instructions.add(new InsnNode(IRETURN));
		cNode.methods.add(mNode);

		/**Add:
		 * 		public String getBloodTexture()
		 * 		{
		 * 			return ayamitsu.gore.common.EntityHooks.defaultGetBloodTexture(this);
		 * 		}
		 */
		mNode = new MethodNode(ASM4, ACC_PUBLIC, "getBloodTexture", "()Ljava/lang/String;", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 0));// this
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "defaultGetBloodTexture", "(L" + ENTITYLIVING_CLASS_NAME + ";)Ljava/lang/String;"));
		mNode.instructions.add(new InsnNode(ARETURN));
		cNode.methods.add(mNode);

		/**Add:
		 * 		public int getBloodColor()
		 * 		{
		 * 			return ayamitsu.gore.common.EntityHooks.defaultGetBloodColor(this);
		 * 		}
		 */
		mNode = new MethodNode(ASM4, ACC_PUBLIC, "getBloodColor", "()I", null, null);
		mNode.instructions.add(new VarInsnNode(ALOAD, 0));// this
		mNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/gore/common/EntityHooks", "defaultGetBloodColor", "(L" + ENTITYLIVING_CLASS_NAME + ";)I"));
		mNode.instructions.add(new InsnNode(IRETURN));
		cNode.methods.add(mNode);

		return this.decode(cNode);
	}

	protected final ClassNode encode(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);
		return cNode;
	}

	protected final byte[] decode(ClassNode cNode) {
		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
		{
			@Override
			public String getCommonSuperClass(String type1, String type2)
			{
				return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important
			}
		};
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}
}
