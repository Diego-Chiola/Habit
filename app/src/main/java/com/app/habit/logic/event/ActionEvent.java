package com.app.habit.logic.event;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ActionEvent {

    public interface IActionListener {
        void execute(String currentState, Date currentStateSetDate, String oldState, Date oldStateSetDate);
    }

    private List<IActionListener> _eventList = null;

    public ActionEvent() {
        _eventList = new LinkedList<>();
    }

    public void subscribe(IActionListener listener) {
        _eventList.add(listener);
    }

    public void execute(String currentState, Date currentStateSetDate, String oldState, Date oldStateSetDate){
        for (var event : _eventList)
            event.execute(currentState, currentStateSetDate, oldState, oldStateSetDate);
    }
}
