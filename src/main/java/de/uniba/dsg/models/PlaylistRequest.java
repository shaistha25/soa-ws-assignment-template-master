package de.uniba.dsg.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * TODO:
 * PlaylistRequest attributes should be
 * - title:String
 * - artistSeeds:List<String>, must be serialized as 'seeds'
 * - numberOfSongs:int, must be serialized as 'size'
 */
@XmlType(propOrder={"title", "artistSeed", "numberOfSongs" })
public class PlaylistRequest {
    @XmlElement(required=true)
    private String title;
    private List<String> artistSeed;
    private int numberOfSongs;

    public PlaylistRequest() {}

    public PlaylistRequest(String title, List<String> artistSeed, int numberOfSongs) {
        this.title = title;
        this.artistSeed = artistSeed;
        this.numberOfSongs = numberOfSongs;
    }

    public String getTitle() {
        return title;
    }

    //@XmlElement(name="seeds")
    public List<String> getArtistSeed() {
        return artistSeed;
    }

    //@XmlElement(name="size")
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtistSeed(List<String> artistSeed) {
        this.artistSeed = artistSeed;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        if (numberOfSongs < 1) {
            numberOfSongs = 10;
        }
        this.numberOfSongs = numberOfSongs;
    }
}
