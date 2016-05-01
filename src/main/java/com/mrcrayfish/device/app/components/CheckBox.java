package com.mrcrayfish.device.app.components;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import com.mrcrayfish.device.app.Application;
import com.mrcrayfish.device.app.Component;
import com.mrcrayfish.device.app.Laptop;
import com.mrcrayfish.device.app.Layout;
import com.mrcrayfish.device.app.listener.ClickListener;
import com.mrcrayfish.device.util.GuiHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class CheckBox extends Component implements IRadioGroupItem
{
	public final String name;
	private boolean checked = false;
	private RadioGroup group = null;
	
	private ClickListener listener = null;
	
	public CheckBox(String name, int x, int y, int left, int top) 
	{
		super(x, y, left, top);
		this.name = name;
	}
	
	public void setRadioGroup(RadioGroup group)
	{
		this.group = group;
		this.group.add(this);
	}
	
	public void setClickListener(ClickListener listener) 
	{
		this.listener = listener;
	}
	
	@Override
	public void render(Laptop laptop, Minecraft mc, int mouseX, int mouseY, boolean windowActive)
	{
		if (this.visible)
        {
			drawRect(xPosition, yPosition, xPosition + 10, yPosition + 10, Color.BLACK.getRGB());
			drawRect(xPosition + 1, yPosition + 1, xPosition + 9, yPosition + 9, Color.GRAY.getRGB());
			if(checked)
			{
				drawRect(xPosition + 2, yPosition + 2, xPosition + 8, yPosition + 8, Color.DARK_GRAY.getRGB());
			}
			drawString(mc.fontRendererObj, name, xPosition + 12, yPosition + 1, Color.WHITE.getRGB());
        }
	}
	
	@Override
	public void handleClick(Application app, int mouseX, int mouseY, int mouseButton)
	{
		if(!this.visible || !this.enabled)
			return;
		
		if(GuiHelper.isMouseInside(mouseX, mouseY, xPosition, yPosition, xPosition + 10, yPosition + 10))
		{
			if(group != null)
			{
				group.unselect();
			}
			this.checked = !checked;
			if(listener != null)
			{
				listener.onClick(this, mouseButton);
			}
		}
	}
	
	@Override
	public boolean isSelected() 
	{
		return checked;
	}
	
	@Override
	public void setSelected(boolean enabled) 
	{
		this.checked = enabled;
	}
}
