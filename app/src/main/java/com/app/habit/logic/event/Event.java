package com.app.habit.logic.event;

import java.util.LinkedList;
import java.util.List;

public class Event {

    public interface IListener {
        void execute();
    }

    private List<IListener> _eventList = null;

    public Event() {
        _eventList = new LinkedList<>();
    }

    public void subscribe(IListener listener) {
        _eventList.add(listener);
    }

    public void execute(){
        for (IListener event : _eventList)
            event.execute();
    }
}
