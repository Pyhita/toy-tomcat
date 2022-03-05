package util;

import java.io.*;
import java.util.Objects;

/**
 * @Author: pyhita
 * @Date: 2022/3/5
 * @Descrption: util
 * @Version: 1.0
 */
public class IOUtil {
    public static byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        int size = 1024;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[size];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }
    public static byte[] getBytesFromFile(String fileName) throws IOException {
        String path = getAbsolutePath(fileName);
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException(path);
        }
        return getBytesFromStream(new FileInputStream(file));
    }

    public static String getAbsolutePath(String path) {
        String absolutePath = Objects.requireNonNull(IOUtil.class.getResource("/")).getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }
}
