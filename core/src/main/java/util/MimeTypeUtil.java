package util;

import eu.medsea.mimeutil.MimeUtil;

import java.io.File;
import java.util.Collection;

/**
 * @Author: pyhita
 * @Date: 2022/3/5
 * @Descrption: util
 * @Version: 1.0
 */
public class MimeTypeUtil {
    public static String getType(File file) {
        if (file.getName().endsWith(".html")) {
            return Constant.DEFAULT_CONTENT_TYPE;
        }
        if (file.getName().endsWith(".ico")) {
            return "image/x-icon";
        }
        Collection mimeTypes = MimeUtil.getMimeTypes(file);
        return mimeTypes.toArray()[0].toString();
    }
}

