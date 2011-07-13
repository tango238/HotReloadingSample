package hello.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CustomClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		if(className.startsWith("java")){
			return Class.forName(className);
		}
		int idx = className.lastIndexOf('.');
		if (idx >= 0) {
			// Defines the package
			String packageName = className.substring(0, idx);
			if (getPackage(packageName) == null) {
				try {
					definePackage(packageName, null, null, null, null, null,
							null, null);
				} catch (IllegalArgumentException ignore) {
				}
			}
			Class<?> clazz = defineClass(className);
			if (clazz != null) {
				return clazz;
			}
		}

		return super.loadClass(className);
	}

	protected Class<?> defineClass(String className) {
		Class<?> clazz;
		String path = className.replace('.', '/') + ".class";
		InputStream is = getInputStream(path);
		if (is != null) {
			byte[] bytes = getBytes(is);
			clazz = defineClass(className, bytes, 0, bytes.length);
			resolveClass(clazz);
			return clazz;
		}
		return null;
	}
	
	protected InputStream getInputStream(String path) {
        try {
            URL url = getResource(path);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ignore) {
            return null;
        }
    }
	
	protected byte[] getBytes(InputStream is) {
        byte[] bytes = null;
        byte[] buf = new byte[8192];
        try {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int n = 0;
                while ((n = is.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, n);
                }
                bytes = baos.toByteArray();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ignore) {
        }
        return bytes;
    }
}
