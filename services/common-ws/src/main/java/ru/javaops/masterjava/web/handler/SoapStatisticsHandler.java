package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import ru.javaops.masterjava.web.Statistics;

import static ru.javaops.masterjava.web.handler.SoapLoggingHandlers.HANDLER.getMessageText;


public class SoapStatisticsHandler extends SoapBaseHandler{
    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        if (!isOutbound(context)){
          Statistics.count(getMessageText(context.getMessage().copy()), System.currentTimeMillis(), Statistics.RESULT.SUCCESS);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        if (!isOutbound(context)){
            Statistics.count(getMessageText(context.getMessage().copy()), System.currentTimeMillis(), Statistics.RESULT.FAIL);
        }
        return true;
    }
}
