package ru.evernight.ui.util;

import lombok.extern.slf4j.Slf4j;
import ru.evernight.exception.EvernightException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

@Slf4j
public class EvernightExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler wrapped;

    EvernightExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
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

            EvernightException realCause = unwrap(t);
            if (realCause != null) {
                //here you do what ever you want with exception
                try {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    String detail = realCause.getCause() == null ? "" : realCause.getCause().getMessage();
                    ctx.addMessage(null, new FacesMessage(realCause.getMessage(), detail));
                    t.printStackTrace();
                } finally {
                    log.error("Got error", realCause);
                    i.remove();
                }
            }
        }
        getWrapped().handle();
    }

    private EvernightException unwrap(Throwable th) {
        while (th instanceof ELException && th.getCause() != null) {
            th = th.getCause();
        }
        if (th instanceof EvernightException) {
            return (EvernightException) th;
        } else {
            return null;
        }
    }
}