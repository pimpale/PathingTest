package com.search;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;



@SuppressWarnings("serial")
public class PathingTest extends JPanel  implements  MouseListener, MouseMotionListener
{	
	public PathingTest()
	{
		addMouseListener(this);
		addMouseMotionListener(this);	 
	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {


	}

	@Override
	public void mouseClicked(MouseEvent arg0) {


	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {


	}


	int xsize = 50;
	int ysize = 50;

	int[][] MAP = new int[xsize][ysize];
	Node[][] NODEMAP = new Node[xsize][ysize];



	int startx = 25;
	int starty = 25;

	int endx = 30;
	int endy = 45;



	public static boolean[][] travelable(boolean[][] map, int startx, int starty)
	{
		int xsize = map.length;
		int ysize = map[0].length;
		ArrayList<Node> nodelist = new ArrayList<Node>();
		Node[][] NODEMAP = new Node[map.length][map[0].length];
		NODEMAP[startx][starty] = new Node("pending", startx,starty, startx,starty);
		while(true)
		{
			boolean finished = true;
			for(int y = 0; y < ysize; y++)
			{
				for(int x = 0; x < xsize; x++)
				{
					if(NODEMAP[x][y] != null && NODEMAP[x][y].Type == "pending")
					{
						Node node = NODEMAP[x][y];
						node.Weight = 1;
						nodelist.add(node);
						node.Type = "visited";
						finished = false;
					}
				}
			}

			if(finished == true)
			{
				boolean[][] available = new boolean[xsize][ysize];
				for(int y = 0; y < ysize; y++)
				{
					for(int x = 0; x < xsize; x++)
					{
						if(NODEMAP[x][y] != null)
						{
							available[x][y] = true;
						}
						else
						{
							available[x][y] = false;
						}
					}
				}
				return available;
			}
			nodelist = sortArrayList(nodelist);

			for(int i = 0; i < (int)nodelist.size(); i+=1)
			{
				Node node = nodelist.get(i);

				int x = node.X;
				int y = node.Y;

				for(int x1 = -1; x1 <= 1; x1++)
				{
					for(int y1 = -1; y1 <= 1; y1++)
					{
						if(x+x1 > 0 && x+x1 < xsize && 
								y+y1 > 0 && y+y1 < ysize &&
								(x1 == 0 || y1 == 0))
						{
							if(map[x+x1][y+y1] == true && NODEMAP[x+x1][y+y1] == null)
							{
								NODEMAP[x][y] = new Node("pending", x+x1,y+y1, x, y);
							}
						}
					}
				}
			}
		}
	}

	Random seed = new Random(2);

	void initialize()
	{

		for(int i = 0; i < 1000; i++)
		{
			MAP[(int)(seed.nextDouble()*xsize)][(int)(seed.nextDouble()*ysize)] = 1;
		}


		MAP[startx][starty] = 2;//start
		MAP[endx][endy] = 3;//finish
		NODEMAP[startx][starty] = new Node("pending", startx,starty, startx,starty);


	}




	Node[][] copyArray(Node[][] copied)
	{
		Node[][] newArray = new Node[copied.length][copied[0].length];
		for(int x = 0; x < copied.length; x++)
		{
			for(int y = 0; y < copied[0].length; y++)
			{
				newArray[x][y] = copied[x][y];
			}
		}
		return newArray;
	}

	ArrayList<Node> copyArrayList(ArrayList<Node> copied)
	{
		ArrayList<Node> copy = new ArrayList<Node>();
		for(int i = 0; i < copied.size(); i++)
		{
			copy.add(copied.get(i));
		}
		return copy;
	}

	static ArrayList<Node> sortArrayList(ArrayList<Node> sortable)
	{

		TreeMap<Double, Node> map = new TreeMap<Double, Node>();
		if(sortable.size() > -50)
		{
			for(int i = 0; i < sortable.size(); i+=1)
			{
				double random = Math.random()/100000;//the random is because it will replace if it is the same value
				map.put(sortable.get(i).Weight+ random, sortable.get(i));
			}
		}


		//Getting Collection of values from HashMap

		Collection<Node> values = map.values();

		//Creating an ArrayList of values

		ArrayList<Node> sorted = new ArrayList<Node>(values);

		return sorted;
	}


	void UpdatePending(int beginx, int beginy, int x, int y)
	{
		if(x >= 0 && y >= 0 && x < xsize && y < ysize)
		{
			if(MAP[x][y] != 1 && NODEMAP[x][y] == null)
			{
				NODEMAP[x][y] = new Node("pending", x,y, beginx, beginy);
			}
			if(MAP[x][y] == 3)
			{
				tracepath(x,y);
			}
		}
	}

	void tracepath(int x, int y)
	{
		boolean notreachedstart = true;
		while(notreachedstart)
		{
			NODEMAP[x][y].Type = "ispath";
			int newx = NODEMAP[x][y].PreviousX;
			int newy = NODEMAP[x][y].PreviousY;
			x = newx;
			y = newy; 
			if(x == startx && y == starty)
			{
				notreachedstart = false;
			}
		}
	}

	private void functions()
	{
		Update();

	}

	void Update()
	{
		ArrayList<Node> nodelist = new ArrayList<Node>();
		for(int y = 0; y < ysize; y++)
		{
			for(int x = 0; x < xsize; x++)
			{
				if(NODEMAP[x][y] != null && NODEMAP[x][y].Type == "pending")
				{
					Node node = NODEMAP[x][y];
					double distancefromstart = Math.sqrt(Math.pow(startx-node.X,2) + Math.pow(starty-node.Y,2));
					double distancetodestination = Math.sqrt(Math.pow(endx-node.X,2) + Math.pow(endy-node.Y,2));
					node.Weight = 1*distancefromstart + 1*distancetodestination;
					nodelist.add(node);
					node.Type = "visited";
				}
			}
		}

		nodelist = sortArrayList(nodelist);

		for(int i = 0; i < (int)nodelist.size(); i+=1)
		{
			Node node = nodelist.get(i);
			int x = node.X;
			int y = node.Y;
			if(node.Type.equals("visited"))
			{
				UpdatePending(x,y,x,y-1);
				UpdatePending(x,y,x,y+1);

				UpdatePending(x,y,x-1,y);
				//		UpdatePending(x,y,x-1,y-1);
				//		UpdatePending(x,y,x-1,y+1);

				UpdatePending(x,y,x,y);

				UpdatePending(x,y,x+1,y);
				//		UpdatePending(x,y,x+1,y-1);
				//		UpdatePending(x,y,x+1,y+1);
			}


		}
	}

	
	
	





	int size = 10;
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		setBackground(Color.black);
		for(int x = 0; x < MAP.length; x++)
		{
			for(int y = 0; y < MAP[0].length; y++)
			{
				if(MAP[x][y] == 1)//obstruction
				{
					g2d.setPaint(Color.white);
				}
				else if(MAP[x][y] == 2)//start
				{
					g2d.setPaint(Color.red);
				}
				else if(MAP[x][y] == 3)//end
				{
					g2d.setPaint(Color.green);
				}
				else
				{
					g2d.setPaint(Color.black);
				}
				g2d.fillRect(x*size, y*size, size,size);
			}
		}

		for(int x = 0; x < NODEMAP.length; x++)
		{
			for(int y = 0; y < NODEMAP[0].length; y++)
			{
				Node node = NODEMAP[x][y];
				if(node != null)
				{
					if(NODEMAP[x][y].Type == "pending")//obstruction
					{		
						g2d.setPaint(Color.blue);
						g2d.drawLine(size*x+size/2, size*y+size/2,
								size*node.PreviousX+size/2, size*node.PreviousY+size/2);
					}
					else if(NODEMAP[x][y].Type == "visited")//start
					{
						g2d.setPaint(Color.gray);
						g2d.drawLine(size*x+size/2, size*y+size/2,
								size*node.PreviousX+size/2, size*node.PreviousY+size/2);
					}
					else if(NODEMAP[x][y].Type == "ispath")//start
					{
						g2d.setPaint(Color.green);
						g2d.drawLine(size*x+size/2, size*y+size/2,
								size*node.PreviousX+size/2, size*node.PreviousY+size/2);
					}
					else
					{
						g2d.setPaint(Color.black);
					}
					//g2d.drawOval(x*10, y*10, 9,9);
				}
			}
		}//*/
	}


	public static void main(String[] args) throws InterruptedException 
	{
		JFrame frame = new JFrame("Pathfinder");
		PathingTest game = new PathingTest();
		frame.add(game);
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.initialize();
		for(int i = 0; i < 30; i++)
		{
			game.functions();
			game.repaint(10);
			Thread.sleep(500);
		}
	}
}

class Node {
	String Type;
	int PreviousX;
	int PreviousY;
	int X;
	int Y;
	double Weight = 0;
	public Node(String type, int x, int y, int previousX, int previousY)
	{
		Type = type;
		X = x;
		Y = y;
		PreviousX = previousX;
		PreviousY = previousY;
	}
}
