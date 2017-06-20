package de.uniba.dsg.jaxrs.resources;

import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Playlist;
import de.uniba.dsg.models.PlaylistRequest;
import de.uniba.dsg.models.Song;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rootX on 18/06/17.
 */
@Path("playlists")
public class PlaylistResource {

    @POST
    //@Consumes(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @Produces("application/json")
    public Response createPlaylist(PlaylistRequest request) {
        // Specify the name of a new playlist via the title attribute of a PlaylistRequest (mandatory)
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("The title for the playlist has to be provided"));
        }

        if (request.getArtistSeed() == null || request.getArtistSeed().size() == 0) {
            throw new ClientRequestException(new ErrorMessage("At least one artist-id has to be provided"));
        }

        List<String> artistIds = request.getArtistSeed();
        int size = request.getNumberOfSongs() > 0 ? request.getNumberOfSongs() : 10;

        ArtistResource resource = new ArtistResource();

        ArrayList<List<Song>> topSongsPerArtists = new ArrayList<List<Song>>();

        for (String artistId : artistIds) {
            if (artistId.isEmpty()) continue;

            topSongsPerArtists.add(resource.getTopTracks(artistId));
        }

        if (topSongsPerArtists.size() == 0) {
            throw new ClientRequestException(new ErrorMessage("At least one valid artist-id has to be provided"));
        }

        int numOfArtist = topSongsPerArtists.size();
        ArrayList<Song> songsForPlaylist = new ArrayList<Song>();
        for (int i = 0; i < size; i++) {
            int index = i % numOfArtist;

            songsForPlaylist.add(topSongsPerArtists.get(index).get(i / numOfArtist));
        }

        Playlist playlist = new Playlist(request.getTitle(), songsForPlaylist);

        return Response.ok(playlist).status(201).build();
    }
}
