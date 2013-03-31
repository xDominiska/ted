package codilime.ted.example;

import java.util.*;

public class Itemize implements Runnable {

	private static int ONE_SECOND = 1000;
	private static List<String> LANGUAGES = Arrays.asList("Clojure", "Groovy", "Java", "JRuby", "Scala");

	@Override
	public void run() {
		for (String language : LANGUAGES) {
			wait(ONE_SECOND);
			System.out.println(String.format("JVM language: %s", language));
		}
	}

	private void wait(int timeInMillis) {
		try {
			Thread.sleep(timeInMillis);
		} catch (InterruptedException e) {
			throw new RuntimeException("Thread interrupted.", e);
		}
	}
}
