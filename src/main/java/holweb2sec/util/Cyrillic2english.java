package holweb2sec.util;

public class Cyrillic2english {
	final static String cyrillic = 
		"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЄІЇҐ абвгдеёжзийклмнопрстуфхцчшщъыьэюяєіїґ";
	final static String [] english = 
		{"A","B","V","H","D","E","Jo","Zh","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","Kh"
		,"Ts","Ch","Sh","Shch","","I","","E","Yu","Ya","Ye","I","Ji","G"," ",
		"a","b","v","h","d","e","jo","zh","z","i","j","k","l","m","n","o","p","r","s","t","u","f","kh"
		,"ts","ch","sh","shch","","i","","e","yu","ya","ye","i","ji","g"};
	public static void main(String[] args) {
		System.out.println(cyrillic);
		final Cyrillic2english cyrillic2english = new Cyrillic2english();
		String[] samples = {"співробітник","Їжак","Варгатая","Міщенко","Клічко"};
		for (String s : samples) {
			final String convert = cyrillic2english.convert(s);
			System.out.println(s+" > "+convert);
		}
	}
	public String convert(String s) {
		String e = "";
//		System.out.print(s);
//		System.out.print(" > ");
		for (char c : s.toCharArray()) {
//		System.out.print(c);
			final int indexOf = cyrillic.indexOf(c);
			e+=indexOf<0?c:english[indexOf];
		}
//		System.out.println(e);
		return e;
	}
}
