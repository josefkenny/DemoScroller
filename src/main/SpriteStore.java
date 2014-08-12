package main;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import assets.SpriteRefs;

/**
 * A resource manager for sprites in the game. Its often quite important how and
 * where you get your game resources from. In most cases it makes sense to have
 * a central resource loader that goes away, gets your resources and caches them
 * for future use.
 * <p>
 * [singleton]
 * <p>
 * 
 * @author Kevin Glass
 */
public class SpriteStore
{
	/** The single instance of this class */
	private static SpriteStore	single	= new SpriteStore();

	/**
	 * Get the single instance of this class
	 * 
	 * @return The single instance of this class
	 */
	public static SpriteStore get()
	{
		return single;
	}

	/** The cached sprite map, from reference to sprite instance */
	private HashMap<String, Sprite>	sprites	= new HashMap<String, Sprite>();

	/**
	 * Retrieve a sprite from the store
	 * 
	 * @param ref
	 *            The reference to the image to use for the sprite
	 * @return A sprite instance containing an accelerate image of the request
	 *         reference
	 */
	public Sprite getSprite(String ref, char letter)
	{
		// if we've already got the sprite in the cache
		// then just return the existing version

		if (sprites.get( ref + "." + letter ) != null)
		{
			return (Sprite) sprites.get( ref + "." + letter );
		}

		// otherwise, go away and grab the sprite from the resource
		// loader

		BufferedImage sourceImage = null;

		try
		{		
			File fileToRead = new File(ref);

			// use ImageIO to read the image in
			
			sourceImage = ImageIO.read(fileToRead);
			
		} catch (IOException e)
		{
			fail("Failed to load: " + ref);
		}
		
		

		if( letter != 0 )
		{
			// crop to the specific letter we need
			
			int y = SpriteRefs.getFontCoords(letter)[0];
			int x = SpriteRefs.getFontCoords(letter)[1];
			
			System.out.println(x + ", " + y);
			
			sourceImage = cropImage(sourceImage, new Rectangle(x*64, y*64, 64, 64));
			
			ref = ref + "." + letter;
		}
		
		
		
		// create an accelerated image of the right size to store our sprite in

		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),
				sourceImage.getHeight(), Transparency.BITMASK);

		// draw our source image into the accelerated image

		image.getGraphics().drawImage(sourceImage, 0, 0, null);

		// create a sprite, add it the cache then return it

		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);
		
		
		
		return sprite;
	}

	/**
	 * Utility method to handle resource loading failure
	 * 
	 * @param message
	 *            The message to display on failure
	 */
	private void fail(String message)
	{
		// we'n't available
		// we dump the message and exit the game

		System.err.println(message);
		System.exit(0);
	}
	
	
	private BufferedImage cropImage(BufferedImage src, Rectangle rect) 
	{
	      BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	      return dest; 
	}
}