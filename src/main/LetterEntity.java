package main;


public class LetterEntity extends Entity
{
	public double frameCounter = 0;
	
	private int originY;
	
	public LetterEntity(String ref, char letter, int x, int y)
	{
		super(ref, x, y, letter);
		
		originY = y;
	}
	
	public void move(long delta)
	{
		super.move(delta);
		
		super.y = (originY) + Math.sin(frameCounter/100) * 30;
		
		frameCounter += 8;
	}
	
}
