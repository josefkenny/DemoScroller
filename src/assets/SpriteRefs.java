package assets;

public class SpriteRefs
{
	public static String greenText = "src/assets/green64.gif";
	
	public static String whiteText = "src/assets/white64.gif";

	public static String goldText = "src/assets/gold64.gif";
	
	public static String didYouKnow = "src/assets/didyouknow.gif";
	
	
	private static String[] fontMap = 
		{ 
			" !     '()",
			"  ,-. 0123",
			"456789:;  ",
			" ? ABCDEFG",
			"HIJKLMNOPQ",
			"RSTUVWXYZ "
		};
	
	
	/**
	 * Get the coordinates for the specified
	 * letter in the font sprite grid.
	 * 
	 * @param   letter  character to look for
	 * @return  the coordinates, or 0,0 if not found.
	 */
	public static int[] getFontCoords(char letter)
	{
		
		int[] coordinates = {0,0};
		
		for(int x=0; x<6; x++)
		{
			for(int y=0; y<10; y++)
			{
				if( fontMap[x].charAt(y) == letter)
				{
					coordinates[0] = x; coordinates[1] = y;
					
					return coordinates;
				}
			}
		}
			
		return coordinates;
	}
}
