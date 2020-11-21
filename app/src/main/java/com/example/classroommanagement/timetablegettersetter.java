package com.example.classroommanagement;

public class timetablegettersetter
{
    public String name;
    public String url;

    public timetablegettersetter() {
    }

    public timetablegettersetter(String name, String url)
    {
        this.name = name;
        this.url = url;
    }

    public String getName()
    {
        return name;
    }
    public String getUrl()
    {
        return url;
    }
}


