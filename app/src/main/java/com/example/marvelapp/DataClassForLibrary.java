package com.example.marvelapp;

public class DataClassForLibrary {
private String name;
private String v_url;
DataClassForLibrary(String name,String url_link)
{
    this.name=name;
    this.v_url=url_link;
}

    public String getName() {
        return name;
    }

    public String getUrl() {
        return v_url;
    }
}
