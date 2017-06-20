package de.uniba.dsg.jaxrs.Helpers;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Artist;
import de.uniba.dsg.models.Interpret;

import java.util.List;

/**
 * Created by rootX on 18/06/17.
 */
public class ResponseDataHelper {
    public static String artistNameList(List<SimpleArtist> artists) {
        StringBuilder varString = new StringBuilder();
        for (SimpleArtist artist: artists) {
            if (varString.length() > 0) {
                varString.append(", ");
            }
            varString.append(artist.getName());
        }

        return varString.toString();
    }

    public static Interpret interpetFrom(Artist artist) {
        Interpret requestedArtist = new Interpret();
        requestedArtist.setName(artist.getName());
        requestedArtist.setId(artist.getId());
        requestedArtist.setPopularity(artist.getPopularity());
        requestedArtist.setGenres(artist.getGenres());

        return requestedArtist;
    }
}
