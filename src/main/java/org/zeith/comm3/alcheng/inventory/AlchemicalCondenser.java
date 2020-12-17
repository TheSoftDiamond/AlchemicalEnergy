package org.zeith.comm3.alcheng.inventory;

import com.zeitheron.hammercore.client.gui.GuiFluidTank;
import com.zeitheron.hammercore.client.gui.GuiWTFMojang;
import com.zeitheron.hammercore.client.gui.GuiWidgets;
import com.zeitheron.hammercore.client.gui.impl.container.SlotScaled;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.zeith.comm3.alcheng.init.InfoAE;
import org.zeith.comm3.alcheng.tiles.TileAlchemicalCondenser;

public class AlchemicalCondenser
{
	@SideOnly(Side.CLIENT)
	public static GuiContainer createGUI(TileAlchemicalCondenser tile, EntityPlayer player)
	{
		return new Gui(tile, player);
	}

	public static Container createContainer(TileAlchemicalCondenser tile, EntityPlayer player)
	{
		return new Inventory(tile, player);
	}

	static class Gui
			extends GuiWTFMojang<Container>
	{
		static final ResourceLocation TEX = new ResourceLocation(InfoAE.MOD_ID, "textures/gui/alchemical_condenser.png");

		private final TileAlchemicalCondenser tile;
		private final EntityPlayer player;

		public GuiFluidTank tank;

		public Gui(TileAlchemicalCondenser tile, EntityPlayer player)
		{
			super(createContainer(tile, player));
			this.tile = tile;
			this.player = player;
			this.xSize = 176;
			this.ySize = 166;
		}

		@Override
		public void initGui()
		{
			super.initGui();
			this.tank = new GuiFluidTank(guiLeft + 120, guiTop + 11, 16, 64, tile.fluid);
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
		{
			UtilsFX.bindTexture(TEX);
			RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

			float h = (float) (tile.energy.getFilledProgress() * 64);

			GuiWidgets.drawFurnaceArrow(guiLeft + 90, guiTop + 35, tile.getProgress(partialTicks));
			GuiWidgets.drawEnergy(guiLeft + 41, guiTop + 11 + 64 - h, 8, h, GuiWidgets.EnumPowerAnimation.UP);
			tank.render(mouseX, mouseY);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			super.drawScreen(mouseX, mouseY, partialTicks);
			RenderHelper.enableGUIStandardItemLighting();
			if(tank.postRender(mouseX, mouseY))
				drawHoveringText(tank.getTooltip(mouseX, mouseY), mouseX, mouseY);
		}
	}

	static class Inventory
			extends Container
	{
		private final TileAlchemicalCondenser tile;

		public Inventory(TileAlchemicalCondenser tile, EntityPlayer player)
		{
			this.tile = tile;

			addSlotToContainer(new SlotScaled(tile.items, 0, 58, 31, 24, 24));
			addInventorySlots(player, 8, 84);
		}

		@Override
		public boolean canInteractWith(EntityPlayer playerIn)
		{
			return tile.items.isUsableByPlayer(playerIn, tile.getPos());
		}

		protected void addInventorySlots(EntityPlayer player, int x, int y)
		{
			for(int i = 0; i < 9; ++i)
				addSlotToContainer(new Slot(player.inventory, i, x + i * 18, 58 + y));

			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 9; ++j)
					addSlotToContainer(new Slot(player.inventory, 9 + j + i * 9, x + 18 * j, y + i * 18));
		}
	}
}