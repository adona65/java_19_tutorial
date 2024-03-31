package tutorial_000.languageNewFeatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _001_PreallocatedHashMaps {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		/*
		 * To create a map of a given size (that will contain a known amount of elements),
		 * we might create an ArrayList and specify this size :
		 */
		List<String> list120 = new ArrayList<>(120); // Here ArrayList is allocated directly for 120 elements.
		
		/*
		 * We might thought the following would lead to same result for a HashMap :
		 */
		Map<String, Integer> map = new HashMap<>(120);
		
		/*
		 * In reality, the previous map is not allocated for 120 elements. This is because the HashMap is 
		 * initialized with a default load factor of 0.75. As soon as the HashMap is 75% full, it is rebuilt 
		 * ("rehashed") with double the size. This behavior ensures that the elements are distributed as evenly 
		 * as possible across the HashMap's buckets and that as few buckets as possible contain more than one 
		 * element.
		 * 
		 * Thus, the HashMap initialized with a capacity of 120 can only hold 120 x 0.75 = 90 mappings.
		 * 
		 * Previously to java 19, if we wanted to create a HashMap for 120 mappings, we had to calculate the 
		 * capacity ourselves : 120 x 0.75 = 160 :
		 */
		Map<String, Integer> map120 = new HashMap<>(160); // allocated directly for 120 elements (160*0.75 = 120)
		
		/*
		 * Java 19 introduce a new convenient method that allow creating directly map of wanted size :
		 */
		Map<String, Integer> anotherMap120 = HashMap.newHashMap(120); // allocated directly for 120 elements
		
		/*
		 * We will note that similar methods were added to LinkedHashMap and WeakHashMap.
		 */
	}
}
