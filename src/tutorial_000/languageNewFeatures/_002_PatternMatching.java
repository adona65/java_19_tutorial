package tutorial_000.languageNewFeatures;

public class _002_PatternMatching {

	public static void main(String[] args) {
		/*
		 * Introduced as preview feature with Java 17, Pattern Matching for switch are still in preview
		 * state with Java 19. This feature allow us to write code like this :
		 * 
		 	switch (obj) {
			  case String s && s.length() > 5 -> System.out.println(s.toUpperCase());
			  case String s                   -> System.out.println(s.toLowerCase());
			  case Integer i                  -> System.out.println(i * i);
			  default -> {}
			}
		 *
		 * With this feature, we can check within a switch statement if an object is of a particular class and if 
		 * it has additional characteristics (in the example : longer than five characters).	
		 * 
		 * Java 19 changed the syntax of the so-called "Guarded Pattern" (in the example above 
		 * "String s && s.length() > 5"). Instead of &&, we now have to use the new keyword when :
		 * 
			switch (obj) {
			  case String s when s.length() > 5 -> System.out.println(s.toUpperCase());
			  case String s                     -> System.out.println(s.toLowerCase());
			  case Integer i                    -> System.out.println(i * i);
			  default -> {}
			}
		 * 
		 * "when" is a "contextual keyword" and has a meaning only within a case label. If we have variables or 
		 * methods with the name "when" in our code, we don't need to change them.
		 */
	
	}
}
