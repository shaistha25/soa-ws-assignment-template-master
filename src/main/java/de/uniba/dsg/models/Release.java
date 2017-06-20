package de.uniba.dsg.models;

import javax.xml.bind.annotation.XmlType;

/**
 * TODO:
 * Release attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 */
@XmlType(propOrder={"title", "artist" })
public class Release {
    private String title;
    private String artist;

    public Release() {}

    public Release(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
