package Other;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    public static final int STATUS_OK = 200;
    public static final int STATUS_ERR = 405;

    public static final String HTTP_OK = "HTTP" + STATUS_OK + "OK";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public static final int UNAUTHORISED_USER = 401;

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final int DEFAULT_MSG_ID = -1;
    public static final int DEFAULT_AUTHOR_ID = -1;
    public static final int DEFAULT_ID = -1;
    public static final String DEFAULT_MSG = "";

}
