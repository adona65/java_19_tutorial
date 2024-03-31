package languageNewFeatures;

public class _003_RecordPatterns {

	@SuppressWarnings("preview")
	public static void main(String[] args) {
		/*
		 * Let say we create a "Position" record that take two parameters :
		 */
		record Position(int x, int y) {
		}
		Position position = new Position(3, 4);

		/*
		 * Introduced by Java 16, "Pattern Matching for instanceof" allow us to perform specific works
		 * and conditions for our record. For example :
		 */
		// Here we defined a condition based on our already existing "Position" record. This kind of condition
		// works because we have defined what a "Position" is previously, in line 9.
		if (position instanceof Position pos) {
			System.out.println("(Pattern Matching) object is a position, x = " + pos.x() + ", y = " + pos.y());
		} else {
			System.out.println("Not our record"); // Not reached in this example.
		}

		/*
		 * Java 19 released a preview feature called "record pattern" that, used alongside with instanceof, allow
		 * us to define the sort of record we want directly inside our conditions :
		 */
		// Instead of matching on "Position position" and accessing position in the following code, we now match
		// on "Position(int x, int y)" and can then access x and y directly.
		if (position instanceof Position(int x, int y)) {
			System.out.println("(record pattern) object is a position, x = " + x + ", y = " + y);
		} else {
			System.out.println("Still not our record"); // Not reached in this example.
		}

		System.out.println("--------------------");

		/*
		 * Thanks to java 17, we were allowed to rewrite our first condition as a switch statement.
		 */
		switch (position) {
			case Position pos -> System.out.println("(switch) object is a position, x = " + pos.x() + ", y = " + pos.y());
		}

		/*
		 * With Java 19's "record pattern" preview feature, we might rewrite the second example as well :
		 */
		switch (position) {
			case Position(int x, int y) -> System.out.println("(Record Pattern with switch) object is a position, x = " + x + ", y = " + y);
			default -> System.out.println("(Record Pattern with switch) not our record");
		}

		System.out.println("--------------------");

		/*
		 * We also may work with nested Records. Let say we create a Path record that use already defined Position
		 * record :
		 */
		record Path(Position from, Position to) {}
		Path path = new Path(new Position(5, 6), new Position(8, 9));

		/*
		 * We now may write conditions to access Position records from Path record, as in previous examples.
		 */
		if (path instanceof Path(Position(int x1, int y1), Position(int x2, int y2))) {
			System.out.println("(record pattern) object is a path with two nested positions, x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2);
		} else {
			System.out.println("Still not our record"); // Not reached in this example.
		}

		switch (path) {
			case Path(Position(int x1, int y1), Position(int x2, int y2)) -> System.out.println("(Record Pattern with switch) object is a position, x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2);
			default -> System.out.println("(Record Pattern with switch) not our record");
		}
	}
}
