package tam.summer.restdoc.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by tanqimin on 2015/11/14.
 */
public class ResourceUtil {

    public static void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException,
            IOException {

        String filePath = getFilePath(fileName);
        if (fileName.endsWith(".jpg")) {
            byte[] bytes = readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = readFromResource(filePath);
        if (text == null) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);
    }

    private static String getFilePath(String fileName) {
        String name;
        if (fileName.startsWith("/")) {
            name = fileName;
        } else {
            name = "/".concat(fileName);
        }
        return "/html" + name;
    }

    /**
     * 把页面资源转换为字符串
     *
     * @param resource
     * @return
     * @throws IOException
     */
    public static String readFromResource(String resource) throws IOException {
        InputStream in = null;

        String text;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = ResourceUtil.class.getResourceAsStream(resource);
            }

            if (in != null) {
                text = read(in);
                String var3 = text;
                return var3;
            }

            text = null;
        } finally {
//            JdbcUtils.close(in);
        }

        return text;
    }

    public static String read(InputStream in) {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalStateException(var3.getMessage(), var3);
        }

        return read(reader);
    }

    public static String read(Reader reader) {
        try {
            StringWriter ex = new StringWriter();
            char[] buffer = new char[4096];
            boolean n = false;

            int n1;
            while (-1 != (n1 = reader.read(buffer))) {
                ex.write(buffer, 0, n1);
            }

            return ex.toString();
        } catch (IOException var4) {
            throw new IllegalStateException("read error", var4);
        }
    }

    public static byte[] readByteArrayFromResource(String resource) throws IOException {
        InputStream in = null;

        byte[] var2;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                Object var6 = null;
                return (byte[]) var6;
            }

            var2 = readByteArray(in);
        } finally {
//            JdbcUtils.close(in);
        }

        return var2;
    }

    public static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        boolean EOF = true;
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n1;
        for (boolean n = false; -1 != (n1 = input.read(buffer)); count += (long) n1) {
            output.write(buffer, 0, n1);
        }

        return count;
    }
}
