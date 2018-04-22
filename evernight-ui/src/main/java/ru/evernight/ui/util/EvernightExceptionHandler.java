package ru.evernight.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.omnifaces.exceptionhandler.FullAjaxExceptionHandler;
import ru.evernight.exception.EvernightException;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

@Slf4j
public class EvernightExceptionHandler extends FullAjaxExceptionHandler {

    EvernightExceptionHandler(ExceptionHandler exception) {
        super(exception);
    }

    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            // get the exception from context
            Throwable t = context.getException();
            try {
                Throwable realCause = findExceptionRootCause(FacesContext.getCurrentInstance(), t);
                if (realCause instanceof EvernightException) {
                    //here you do what ever you want with exception
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    String detail = realCause.getCause() == null ? "" : realCause.getCause().getMessage();
                    ctx.addMessage(null, new FacesMessage(realCause.getMessage(), detail));
                    t.printStackTrace();
                    i.remove();
                }
            } finally {
                log.error("Got error", t);
            }
        }
        super.handle();
    }

}