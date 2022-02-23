package classLoader;

/**
 * @Author: pyhita
 * @Date: 2022/2/23
 * @Descrption: catalina.classLoader
 * @Version: 1.0
 */
public class CommonClassLoader extends ClassLoader {

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
