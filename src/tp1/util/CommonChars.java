package tp1.util;

public class CommonChars {
	
	private CommonChars() {}
	
	public static boolean contains(String b, char[] a) {
		for (int i = 0; i < a.length; i++) {
			if (b.contains( Character.toString(a[i]) )) 
				return true;
		}
		return false;
	}
}
