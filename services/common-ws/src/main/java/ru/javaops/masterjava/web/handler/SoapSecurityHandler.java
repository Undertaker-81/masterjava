package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import com.typesafe.config.Config;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.AuthUtil;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

public class SoapSecurityHandler extends SoapBaseHandler{
    private static final Config config =
           Configs.getConfig("hosts.conf", "hosts");

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
       String user = config.getConfig("mail").getString("user");
       String password = config.getConfig("mail").getString("password");
          Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);

        int code = AuthUtil.checkBasicAuth(headers, AuthUtil.encodeBasicAuthHeader(user, password));
        if (code != 0) {
            context.put(MessageContext.HTTP_RESPONSE_CODE, code);
            throw new SecurityException();
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {

        throw new SecurityException();
    }
}
