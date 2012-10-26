/*
 * The MIT License
 *
 * Original work sponsored and donated by Lakeside A/S (http://www.lakeside.dk)
 *
 * Copyright (c) to all contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.lakeside.foosballranker.event;

import java.util.*;

/**
 * simple eventbus framework, heavily inspired by GWT
 */
public class EventBus {
    private Map<EventType<?>, List<? extends EventHandler>> handlerMap = new HashMap<EventType<?>, List<? extends EventHandler>>();

    public <H extends EventHandler> HandlerRegistration addHandler(final EventType<H> type, final H handler) {
        List<H> handlers = getHandlers(type);
        if (handlers == null) {
            handlers = new ArrayList<H>();
            handlerMap.put(type, handlers);
        }
        return new HandlerRegistration() {
            public void removeHandler() {
                List<H> l = getHandlers(type);
                if (l != null && l.remove(handler) && l.isEmpty()) {
                    handlerMap.remove(type);
                }
            }
        };
    }


    public <H extends EventHandler> void fireEvent(Event<H> event) {
        List<H> handlers = getHandlerList(event.getAssociatedType());
        for (H handler : handlers) {
            event.dispatch(handler);
        }
    }
    

    private <H extends EventHandler> List<H> getHandlerList(EventType<H> type) {
        List<H> handlers = getHandlers(type);
        if (handlers == null) {
            return Collections.emptyList();
        } else {
            return handlers;
        }
    }

    private <H extends EventHandler> List<H> getHandlers(EventType<H> type) {
        // safe, we control the puts.
        //@SuppressWarnings("unchecked")
        return (List<H>) this.handlerMap.get(type);
    }

}
