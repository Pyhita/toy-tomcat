package classLoader;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: pyhita
 * @Date: 2022/2/23
 * @Descrption: catalina.classLoader
 * @Version: 1.0
 */
public class WebappClassLoader extends URLClassLoader {

    private static final String CLASS_FILE_SUFFIX = ".class";

    private final Map<String, Class<?>> classMap;

    private final ClassLoader j2seClassLoader;

    private final ClassLoader parent;

    public WebappClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.classMap = new ConcurrentHashMap<>();
        this.j2seClassLoader = getSystemClassLoader();
        this.parent = parent;
    }

    /**
     * 查找并且加载类
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        URL[] urls = super.getURLs();
        String path = binaryNameToPath(name);
        File classFile = null;

        for (URL url : urls) {
            File base = null;
            try {
                base = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            classFile = new File(base, path);
            if (classFile.exists()) break;
            classFile = null;
        }

        if (classFile == null) {
            throw new ClassNotFoundException(name);
        }
        byte[] bytes = loadClassBytes(classFile);
        return this.defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 将class文件读到字节流
     * @param classFile
     * @return
     */
    private byte[] loadClassBytes(File classFile) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(classFile)) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();

            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    /**
     * 查找当前是否已加载该类
     *
     * @param name 类名
     * @return Class or NULL
     */
    protected Class<?> findLoadedClass0(String name) {
        return classMap.get(name);
    }

    /**
     * 将类名转换成 .class 文件路径
     *
     * @param binaryName 类名
     * @return .class 文件路径
     */
    private String binaryNameToPath(String binaryName) {
        StringBuilder path = new StringBuilder(7 + binaryName.length());
        path.append('/');
        path.append(binaryName.replace('.', '/'));
        path.append(CLASS_FILE_SUFFIX);
        return path.toString();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 1.检查该类是否被当前的WebappClassLoader加载
            Class<?> clazz = findLoadedClass0(name);
            if (clazz != null) return clazz;

            // 2.检查JVM缓存是否加载该类
            clazz = findLoadedClass(name);
            if (clazz != null) return clazz;

            // 3.尝试通过系统类加载器APPClassLoader加载该类，防止Webapploader重写JDK中的类
            try {
                clazz = j2seClassLoader.loadClass(name);
                if (clazz != null) return clazz;
            } catch (ClassNotFoundException exception) {

            }

            // 4.使用WebAppClassLoader加载
            clazz = findClass(name);
            if (clazz != null) {
                classMap.put(name, clazz);
                return clazz;
            }

            // 5.如果WebappClassLoader没有加载到，那么无条件委托给父类进行加载
            try {
                clazz = Class.forName(name, false, parent);
                return clazz;
            } catch (Exception e) {

            }
        }

        throw new ClassNotFoundException(name);
    }
}
