package de.uniba.dsg.models;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * TODO:
 * Playlist attributes should be
 * - title:String
 * - size:int
 * - tracks:List<Song>
 */
@XmlType(propOrder={"title", "size", "tracks" })
public class Playlist {
    private String title;
    private int size;
    private List<Song> tracks;

    public Playlist() {}

    public Playlist(String title, List<Song> tracks) {
        this.title = title;
        this.tracks = tracks;
        this.size = tracks.size();
    }

    public String getTitle() { return title; }

    public int getSize() { return size; }

    public List<Song> getTracks() { return tracks; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(int size) {
        if (size < 1) {
            size = 10;
        }
        this.size = size;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }
}