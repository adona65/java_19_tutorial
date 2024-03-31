package tutorial_000.languageNewFeatures;
public class _000_MiscellaneousChanges {

	/*
	 * Java 19 delivered various changes concerning Java. Here are some.
	 */
	
	/*
	 * New System Properties for System.out and System.err
	 * 
	 * As of Java 19, the operating system's default encoding is used for printing to System.out and System.err, 
	 * for example, "Cp1252" on Windows. To change the output to UTF-8, we may either :
	 * - add the following VM options when calling the application : -Dstdout.encoding=utf8 -Dstderr.encoding=utf8
	 * - set these settings globally by defining the following environment variable : _JAVA_OPTIONS="-Dstdout.encoding=utf8 -Dstderr.encoding=utf8"
	 */
	
	/*
	 * Structured Concurrency
	 * 
	 * Java 19 is released with an incubator geature called Structured Concurrency. When a task consists of several 
	 * subtasks that can be processed in parallel, Structured Concurrency allows us to implement this in a 
	 * particularly readable and maintainable way.
	 * 
	 * We won't see more of it now, and will just note to stay alert about it. We will review this feature in a futur
	 * version of java, waiting at least it reach the preview feature state.
	 */
	
	/*
	 * Foreign Function & Memory API
	 * 
	 * First, a little reminder. The "Foreign Memory Access API" and the "Foreign Linker API" were already introduced 
	 * in Java 14 and Java 16, both initially individually in the incubator stage. In Java 17, these APIs were combined 
	 * to form the "Foreign Function & Memory API" (FFM API), which remained in the incubator stage until Java 18.
	 * 
	 * Now with Java 19, this API finally reached the preview feature state. This API enables access to native memory 
	 * (memory outside the Java heap) and access to native code (C libraries for example) directly from Java.
	 * 
	 * We won't see code example here as Java developers rarely need access to native memory and code.
	 */
	
	/*
	 * Vector API
	 * 
	 * The new Vector API is different from the java.util.Vector class. It is a new API for mathematical vector 
	 * computation and its mapping to modern SIMD (Single-Instruction-Multiple-Data) CPUs.
	 * 
	 * The Vector API has been part of the JDK since Java 16 as an incubator and was further developed in Java 17 and 
	 * Java 18. Java 19 delivers the fourth iteration in which the API has been extended to include new vector 
	 * operations, as well as the ability to store vectors in and read them from memory segments (a feature of the
	 * previously described "Foreign Function & Memory API"). It is still an incubator feature.
	 */
}
