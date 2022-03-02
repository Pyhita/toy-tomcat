package util;


import lombok.extern.slf4j.Slf4j;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/22 - 12 - 22 - 22:16
 * @Description: catalina.util
 * @Version: 1.0
 */
@Slf4j
public class Handler {
    public static void main(String[] args) {

    }

//    public static void handleResource(Request request, Response response) {
//        String url = request.getUrl();
//        String docBase = request.getContext().getDocBase();
//        try {
//            File file = new File(docBase, url);
//            if (!file.exists() || file.isDirectory()) {
//                throw new ResourceNotFoundException("资源不存在-" + url);
//            }
//            byte[] bytes = IOUtil.getBytesFromStream(new FileInputStream(file));
//
//
//            String encoding = request.getHeaderMap().get("Accept-Encoding");
//            if (StrUtil.isNotEmpty(encoding) && StrUtil.containsIgnoreCase(encoding, "gzip")) {
//                response.setGzip(true);
//            }
//            //TODO:实现简单的模板引擎
////            response.setBody(TemplateResolver.resolve(new String(bytes), request).getBytes(StandardCharsets.UTF_8));
//            response.setBody(bytes);
//            response.setContentType(util.MimeTypeUtil.getType(file));
//        } catch (ResourceNotFoundException e) {
//            handleException(e, response);
//        } catch (IOException e) {
//            handleException(new ServerErrorException("无法读取静态资源"), response);
//        }
//    }
//
//    public static void handleException(Exception e, Response response) {
//        response.setStatus(util.HttpStatus.INTERNAL_SERVER_ERROR);
//        response.addHeader("Connection", "close");
//        try {
//            log.error(e.toString());
//            for (StackTraceElement element : e.getStackTrace()) {
//                log.error(element.toString());
//            }
//            if (e instanceof ServletException) {
//                ServletException se = (ServletException) e;
//                response.setStatus(se.getStatus());
//                response.setBody(IOUtil.getBytesFromStream(new FileInputStream(new File(CATALINA_BASE, ERROR_PAGE))));
//            }
//        } catch (IOException ignored) {
//        }
//    }
}
