package life.automata.simple;

import java.awt.Color;
import java.awt.Point;

public final class cell {

	public boolean alive = false;
	public int neighbors = 0;
	public Point ID=null;
	public int x = 0;
	public int y = 0;
	public Color[] colours=null;
	//Default Color is green
	public Color myColour=Color.green;
	
	public static boolean[] NumBirth   = {false,false,false,true ,false,false,false,false,false};
	public static boolean[] NumSurvive = {false,false,true ,true ,false,false,false,false,false};
	
	public cell(int x,int y)
	{
		alive = false;
		neighbors = 0;
		ID = new Point(x,y);
		this.x = x ;
		this.y = y;
		//the colors of our neighbors
		colours = new Color[8];
		for(int i=0 ; i < colours.length;i++)
		{
			colours[i] = null;
		}
	}
	
	public void dispose()
	{
		/*throw away references*/
		ID = null;
		for(int i=0 ; null != colours && i < colours.length;i++)
		{
			colours[i] = null;
		}
		colours = null;
	}
	
	public static void addNeighbor(cell c)
	{
		c.neighbors++;
	}
	
	public static void setMyColour(cell c,Color colour)
	{
		c.myColour = colour;
	}
	
	public static void addNeighbor(cell c, Color colour)
	{
		if(c.neighbors < c.colours.length)
		{
			//keep a reference to this neighbors color
			c.colours[c.neighbors] = colour; 
		}
		c.neighbors++;
	}
	
	public static boolean pushToStack(cell c)
	{
		if(c.alive)
		{
			/*by default if less than 2 or more than 3 neighbors we die*/
			if(c.neighbors >= NumSurvive.length || !NumSurvive[c.neighbors])
			{
				c.alive=false;
				return false;
			}
			
			c.neighbors = 0;
			return true;
		}
		
		
		/*by default, if dead and have 3 neighbors then we live.*/
		if(c.neighbors < NumBirth.length && NumBirth[c.neighbors])
		{
			c.alive=true;
			c.neighbors = 0;
			//if(null != colours[0])
				//{
					mixColour(c);
				//}
			return true;
		}
		
		return false;
	}
	
	private static void mixColour(cell c)
	{
		//to do make it loop thru colors
		try{
			/*
			double dblRed = (colours[0].getRed() + colours[1].getRed() + colours[2].getRed())/3;
			double dblGreen = (colours[0].getGreen() + colours[1].getGreen() + colours[2].getGreen())/3;
			double dblBlue = (colours[0].getBlue() + colours[1].getBlue() + colours[2].getBlue())/3;
			int red = (int) Math.min(Math.round(dblRed), 255);
			int green = (int) Math.min(Math.round(dblGreen), 255);
			int blue = (int) Math.min(Math.round(dblBlue), 255);
			if((red + blue + green) < 170)
			{
				int diff = 170 - (red + blue + green) / 3;
				red = red + diff;
				green = green + diff;
				blue = blue + diff;
			}
			*/
			
			int red = 0;
			int blue = 0;
			int green = 0;
			
			int numcols = 0;
			for(int i = 0 ; i < c.colours.length ; i++)
			{
				if(null == c.colours[i])
				{
					break;
				}
				red += c.colours[i].getRed();
				blue += c.colours[i].getBlue();
				green += c.colours[i].getGreen();
				numcols++;
				//Remove Colour Reference
				c.colours[i] = null;
			}
			
			if((red + blue + green) < 1)
			{
				//some weird issue
				System.out.println("!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!! COLOR ISSUE  " + c.neighbors);
				c.myColour=Color.green;
				return;
			}
			
			red = Math.round(red/numcols);
			blue = Math.round(blue/numcols);
			green = Math.round(green/numcols);
			
			if(red > 0)
			{
				red = Math.max(red, 127);
			}
			if(blue > 0)
			{
				blue = Math.max(blue, 127);
			}
			if(green > 0)
			{
				green = Math.max(green, 127);
			}
			
			red = Math.min(red, 255);
			blue = Math.min(blue, 255);
			green = Math.min(green, 255);
			
			c.myColour=new Color(red,green,blue);
		}
		catch(Exception e)
		{
			System.out.println("Color Mixing Exception");
			c.myColour=Color.green;
			for(int i = 0 ; i < c.colours.length;i++)
			{
				//remove any references
				c.colours[i]=null;
			}
		}
	}
}
