package hello;

import hello.classloader.CustomClassLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class Main {

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.run();
	}

	public void run() throws Exception {
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			// Initialize ClassLoader
			CustomClassLoader loader = new CustomClassLoader();
			
			System.out.println(">>> Please hit the Enter key.");
			reader.readLine();
			String input = "hello.Sample execute"; 
			String[] str = input.split(" ");
			String className = str[0];
			String methodName = str[1];
			
			System.out.println("===== " + input + " =====");
			
			Class<?> clazz = loader.loadClass(className);
			Object newInstance = clazz.newInstance();
			Method method = clazz.getDeclaredMethod(methodName);
			method.invoke(newInstance);
			
			System.out.println("<<<");
			System.out.println();
		}
	}
}
