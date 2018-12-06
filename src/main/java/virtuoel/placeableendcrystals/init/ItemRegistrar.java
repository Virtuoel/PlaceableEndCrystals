package virtuoel.placeableendcrystals.init;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.placeableendcrystals.PlaceableEndCrystals;
import virtuoel.placeableendcrystals.reference.PlaceableEndCrystalsConfig;

@EventBusSubscriber(modid = PlaceableEndCrystals.MOD_ID)
public class ItemRegistrar
{
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemEndCrystal()
		{
			@Override
			public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
			{
				return !PlaceableEndCrystalsConfig.allowSettingBeamTarget;
			}
			
			@Override
			public float getDestroySpeed(ItemStack stack, IBlockState state)
			{
				return PlaceableEndCrystalsConfig.allowSettingBeamTarget ? 0.0F : super.getDestroySpeed(stack, state);
			}
			
			@Override
			public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
			{
				if(PlaceableEndCrystalsConfig.allowSettingBeamTarget && !entityLiving.world.isRemote && stack == entityLiving.getHeldItemMainhand() && entityLiving instanceof EntityPlayer)
				{
					if(stack.hasTagCompound())
					{
						NBTTagCompound compound = stack.getTagCompound();
						if(compound.hasKey("Placing"))
						{
							compound.removeTag("Placing");
							if(compound.isEmpty())
							{
								stack.setTagCompound(null);
							}
							return super.onEntitySwing(entityLiving, stack);
						}
					}
					
					EntityPlayer player = (EntityPlayer) entityLiving;
					if(player.isSneaking())
					{
						double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
						reach = player.isCreative() ? reach : reach - 0.5F;
						Vec3d eyes = player.getPositionEyes(1);
						Vec3d look = player.getLook(1);
						RayTraceResult trace = player.world.rayTraceBlocks(eyes, eyes.add(look.x * reach, look.y * reach, look.z * reach), false, false, true);
						
						if(trace.typeOfHit == Type.BLOCK && trace.getBlockPos() != null)
						{
							BlockPos pos = trace.getBlockPos();
							if(!(stack.hasTagCompound() && stack.getTagCompound().hasKey("BeamPos") && pos.equals(NBTUtil.getPosFromTag(stack.getTagCompound().getCompoundTag("BeamPos")))))
							{
								stack.setTagInfo("BeamPos", NBTUtil.createPosTag(pos));
								player.sendStatusMessage(new TextComponentTranslation("placeableendcrystals.set_target", pos.getX(), pos.getY(), pos.getZ()), true);
							}
						}
						else if(stack.hasTagCompound())
						{
							NBTTagCompound compound = stack.getTagCompound();
							if(compound.hasKey("BeamPos"))
							{
								compound.removeTag("BeamPos");
								player.sendStatusMessage(new TextComponentTranslation("placeableendcrystals.reset_target"), true);
							}
							
							if(compound.isEmpty())
							{
								stack.setTagCompound(null);
							}
						}
						
						player.inventory.mainInventory.set(player.inventory.currentItem, stack);
					}
				}
				return super.onEntitySwing(entityLiving, stack);
			}
			
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
			{
				if(stack.hasTagCompound())
				{
					if(PlaceableEndCrystalsConfig.allowSettingBeamTarget && stack.getTagCompound().hasKey("BeamPos"))
					{
						BlockPos target = NBTUtil.getPosFromTag(stack.getTagCompound().getCompoundTag("BeamPos"));
						tooltip.add(I18n.format("placeableendcrystals.tooltip.position", target.getX(), target.getY(), target.getZ()));
					}
					
					if(PlaceableEndCrystalsConfig.allowBasePlateToggle && stack.getTagCompound().hasKey("HasBasePlate"))
					{
						tooltip.add(I18n.format("placeableendcrystals.tooltip.base_plate"));
					}
				}
			}
			
			@Override
			public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
			{
				if(PlaceableEndCrystalsConfig.allowBasePlateToggle && !worldIn.isRemote && handIn == EnumHand.MAIN_HAND && playerIn.isSneaking())
				{
					ItemStack stack = playerIn.getHeldItemMainhand().copy();
					if(stack.hasTagCompound())
					{
						NBTTagCompound compound = stack.getTagCompound();
						if(compound.hasKey("Placing"))
						{
							compound.removeTag("Placing");
							if(compound.isEmpty())
							{
								stack.setTagCompound(null);
							}
							return super.onItemRightClick(worldIn, playerIn, handIn);
						}
						
						if(compound.hasKey("HasBasePlate"))
						{
							compound.removeTag("HasBasePlate");
							if(compound.isEmpty())
							{
								stack.setTagCompound(null);
							}
							playerIn.sendStatusMessage(new TextComponentTranslation("placeableendcrystals.disabled_base_plate"), true);
						}
						else
						{
							stack.setTagInfo("HasBasePlate", new NBTTagByte((byte) 1));
							playerIn.sendStatusMessage(new TextComponentTranslation("placeableendcrystals.enabled_base_plate"), true);
						}
					}
					else
					{
						stack.setTagInfo("HasBasePlate", new NBTTagByte((byte) 1));
						playerIn.sendStatusMessage(new TextComponentTranslation("placeableendcrystals.enabled_base_plate"), true);
					}
					
					playerIn.inventory.mainInventory.set(playerIn.inventory.currentItem, stack);
				}
				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
			}
			
			@Override
			public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
			{
				if(!PlaceableEndCrystalsConfig.canPlaceOnState(worldIn.getBlockState(pos)))
				{
					return EnumActionResult.FAIL;
				}
				
				BlockPos blockpos = pos.up();
				ItemStack itemstack = player.getHeldItem(hand);
				
				if(player.canPlayerEdit(blockpos, facing, itemstack))
				{
					BlockPos blockpos1 = blockpos.up();
					boolean flag = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
					flag = flag | (!worldIn.isAirBlock(blockpos1) && !worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1));
					
					if(flag)
					{
						return EnumActionResult.FAIL;
					}
					else
					{
						List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockpos, blockpos.add(1, 2, 1)));
						
						if(list.isEmpty())
						{
							if(PlaceableEndCrystalsConfig.allowSettingBeamTarget || PlaceableEndCrystalsConfig.allowBasePlateToggle)
							{
								itemstack.setTagInfo("Placing", new NBTTagByte((byte) 1));
							}
							else if(itemstack.hasTagCompound())
							{
								NBTTagCompound compound = itemstack.getTagCompound();
								compound.removeTag("Placing");
								if(compound.isEmpty())
								{
									itemstack.setTagCompound(null);
								}
							}
							
							if(!worldIn.isRemote)
							{
								EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn, pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F);
								
								entityendercrystal.setShowBottom(false);
								if(itemstack.hasTagCompound())
								{
									if(PlaceableEndCrystalsConfig.allowSettingBeamTarget && itemstack.getTagCompound().hasKey("BeamPos", Constants.NBT.TAG_COMPOUND))
									{
										BlockPos beamPos = NBTUtil.getPosFromTag(itemstack.getTagCompound().getCompoundTag("BeamPos"));
										if(!pos.equals(beamPos))
										{
											entityendercrystal.setBeamTarget(beamPos);
										}
									}
									
									if(PlaceableEndCrystalsConfig.allowBasePlateToggle && itemstack.getTagCompound().hasKey("HasBasePlate"))
									{
										entityendercrystal.setShowBottom(true);
									}
								}
								
								worldIn.playSound(null, pos, SoundType.GLASS.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.GLASS.getVolume() + 1.0F) / 2.0F, SoundType.GLASS.getPitch() * 0.8F);
								worldIn.spawnEntity(entityendercrystal);
								
								if(worldIn.provider instanceof WorldProviderEnd)
								{
									DragonFightManager dragonfightmanager = ((WorldProviderEnd) worldIn.provider).getDragonFightManager();
									if(dragonfightmanager != null)
									{
										dragonfightmanager.respawnDragon();
									}
								}
							}
							
							if(!player.capabilities.isCreativeMode)
							{
								itemstack.shrink(1);
							}
							return EnumActionResult.SUCCESS;
						}
					}
				}
				return EnumActionResult.FAIL;
			}
		}.setRegistryName(Items.END_CRYSTAL.getRegistryName()));
	}
}
