package com.app.habit.logic.usage;

import java.io.Serializable;

public class UsageAnalytic implements Serializable {

    public static int APPLICATION = 0;
    public static int ACTION = 1;

    private final String _name;
    private final int _type;
    private final long _usageTime;
    private final long _lastTimeUsage;
    private final int _icon;
    private final long _date;

    public UsageAnalytic(String _name, int _type, long _usageTime, long _lastTimeUsage, int _icon, long _date) {
        this._name = _name;
        if(_type != APPLICATION && _type != ACTION)
            throw new IllegalArgumentException("Select a valid  type");
        this._type = _type;
        this._usageTime = _usageTime;
        this._lastTimeUsage = _lastTimeUsage;
        this._icon = _icon;
        this._date = _date;
    }

    public UsageAnalytic(String _name, int _type, long _usageTime, int _icon, long _date) {
        this(_name,  _type,_usageTime, 0, _icon, _date);
    }

    public String get_name() {
        return _name;
    }

    public int get_type() {
        return _type;
    }

    public long get_usageTime() {
        return _usageTime;
    }

    public long get_lastTimeUsage() {
        return _lastTimeUsage;
    }

    public int get_icon() {
        return _icon;
    }

    public long get_date() {return _date;}
}
