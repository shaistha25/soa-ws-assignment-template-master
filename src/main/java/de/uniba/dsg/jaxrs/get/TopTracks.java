package de.uniba.dsg.jaxrs.get;

import com.wrapper.spotify.methods.TopTracksRequest;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Song;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rootX on 13/06/17.
 */
@Path("artists")
public class TopTracks {

    @GET
    @Path("topTracks/{artist-id}")
    public List<Song> getTopTracks(@PathParam("artist-id") String id) {
        if (id == null || id.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //Api api = Api.DEFAULT_API;

        TopTracksRequest request = SpotifyApi.getInstance().getTopTracksForArtist(id, "de").build();

        try {
            List<Track> topTracks = request.get();

            if (topTracks.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", id)));
            }

            ArrayList<Song> topSongs = new ArrayList<Song>();

            for (Track track : topTracks) {
                List<SimpleArtist> artists = track.getArtists();
                StringBuilder varString = new StringBuilder();
                for (SimpleArtist artist: artists) {
                    if (varString.length() > 0) {
                        varString.append(", ");
                    }
                    varString.append(artist.getName());
                }
                Song newSong = new Song(track.getName(), varString.toString(), track.getDuration());
                topSongs.add(newSong);
            }

            return  topSongs;
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + id, 404);
        }
    }
}
