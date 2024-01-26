package ttv.alanorMiga.jeg.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import ttv.alanorMiga.jeg.blockentity.GunniteWorkbenchBlockEntity;
import ttv.alanorMiga.jeg.crafting.GunniteWorkbenchRecipes;
import ttv.alanorMiga.jeg.init.ModContainers;

/**
 * Author: MrCrayfish
 */
public class GunniteWorkbenchContainer extends AbstractContainerMenu
{
    private GunniteWorkbenchBlockEntity workbench;
    private BlockPos pos;

    public GunniteWorkbenchContainer(int windowId, Container playerInventory, GunniteWorkbenchBlockEntity workbench)
    {
        super(ModContainers.GUNNITE_WORKBENCH.get(), windowId);
        this.workbench = workbench;
        this.pos = workbench.getBlockPos();

        int offset = GunniteWorkbenchRecipes.isEmpty(workbench.getLevel()) ? 0 : 28;

        this.addSlot(new Slot(workbench, 0, 174, 18)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getItem() instanceof DyeItem;
            }

            @Override
            public int getMaxStackSize()
            {
                return 1;
            }
        });

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 102 + y * 18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 160));
        }
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return workbench.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            if(index == 0)
            {
                if(!this.moveItemStackTo(slotStack, 1, 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if(slotStack.getItem() instanceof DyeItem)
                {
                    if(!this.moveItemStackTo(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 28)
                {
                    if(!this.moveItemStackTo(slotStack, 28, 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index <= 36 && !this.moveItemStackTo(slotStack, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if(slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if(slotStack.getCount() == stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stack;
    }

    public GunniteWorkbenchBlockEntity getWorkbench()
    {
        return workbench;
    }

    public BlockPos getPos()
    {
        return pos;
    }

}
