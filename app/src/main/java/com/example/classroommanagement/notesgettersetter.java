package com.example.classroommanagement;

public class notesgettersetter
{
    public String name;
    public String url;

    public notesgettersetter() {}

    public notesgettersetter(String name, String url)
    {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
}
