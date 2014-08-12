package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import assets.SpriteRefs;
import assets.Text;

public class Scroller extends Canvas
{

	private BufferStrategy		strategy;

	private ArrayList<Entity>	entities;

	private boolean				isRunning		= true;

	private int					textCounter		= 0;
	private int					textIndex		= 0;
	private final int			textCounterMax	= 15;

	private int					scrollSpeed		= 200;

	public Scroller()
	{

		// create a frame to contain our game

		JFrame theFrame = new JFrame("Demotext Scroller");

		// get hold the content of the frame and set up the resolution of the
		// game

		JPanel panel = (JPanel) theFrame.getContentPane();

		panel.setPreferredSize(new Dimension(1920, 1080));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame

		setBounds(0, 0, 1920, 1080);
		panel.add(this);

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode

		setIgnoreRepaint(true);

		// finally make the window visible

		theFrame.pack();
		theFrame.setResizable(false);
		theFrame.setVisible(true);

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game

		theFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		entities = new ArrayList<Entity>();

		try
		{
			// Wait two seconds on startup.
			// This is useful as it allows a video
			// capture application (e.g. FRAPS)
			// to hook into the program before it
			// starts displaying the scroller.
			Thread.sleep(2000);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		// run the game
		mainLoop();
	}

	public void mainLoop()
	{
		long lastTimeMillis = System.currentTimeMillis();

		// keep looping round til the game ends

		while (isRunning)
		{
			long delta = System.currentTimeMillis() - lastTimeMillis;

			lastTimeMillis = System.currentTimeMillis();

			// Paint the background for chroma-key
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.green);
			g.fillRect(0, 0, 1920, 1080);

			textCounter++;
			if (textCounter > textCounterMax)
			{
				textCounter = 0;
				textIndex++;
				addTextEntities(textIndex);
			}

			// each entity will move itself
			// or delete itself if its x is < -64
			if (entities.size() > 0)
			{
				for (int i = 0; i < entities.size(); i++)
				{
					Entity entity = (Entity) entities.get(i);

					if (entity.getX() < -640)
					{
						entities.remove(i);
					} else
					{
						entity.move(delta);
					}

				}

			}

			// cycle round drawing all the entities we have in the game

			for (int i = 0; i < entities.size(); i++)
			{
				Entity entity = (Entity) entities.get(i);

				entity.draw(g);
			}

			// remove any entity that has been marked for clear up

			// entities.removeAll(removeList);
			// removeList.clear();

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			// if we're waiting for an "any key" press then draw the
			// current message
			//
			// if (waitingForKeyPress) {
			// g.setColor(Color.white);
			// g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
			// g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
			// }

			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over

			g.dispose();
			strategy.show();

			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer

			try
			{
				Thread.sleep(20);

			} catch (Exception e)
			{
			}
		}
	}

	private void addTextEntities(int index)
	{
		char nextChar = Text.scrollerText.charAt(index
				% Text.scrollerText.length());
		LetterEntity newEntity = new LetterEntity(SpriteRefs.whiteText,
				nextChar, 1920 + 64, 540 - 32);
		newEntity.setHorizontalMovement(-scrollSpeed);
		entities.add(newEntity);

		nextChar = Text.weed.charAt(index % Text.weed.length());
		newEntity = new LetterEntity(SpriteRefs.goldText, nextChar, 1920 + 64,
				270 - 32);
		newEntity.setHorizontalMovement(-scrollSpeed * 1.5);
		entities.add(newEntity);

		nextChar = Text.fourTwentyOne.charAt(index
				% Text.fourTwentyOne.length());
		newEntity = new LetterEntity(SpriteRefs.greenText, nextChar, 1920 + 64,
				810 - 32);
		newEntity.setHorizontalMovement(-scrollSpeed * 1.2);
		entities.add(newEntity);

		// LetterEntity newEntity = new LetterEntity(SpriteRefs.didYouKnow,
		// (char) 0, 1920 + 64, 540-128);
		// newEntity.setHorizontalMovement(-scrollSpeed*5);
		// entities.add(newEntity);

	}

}
