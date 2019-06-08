package relicsorter.util;

public class MathHelper {
	public static boolean fuzzyEquals(float a, float b, float threshold) {
		return (Math.abs(a - b)) <= threshold;
	}
}
