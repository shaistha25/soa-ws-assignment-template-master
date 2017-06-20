package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.NewReleasesRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.NewReleases;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleArtist;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxrs.Helpers.ResponseDataHelper;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rootX on 18/06/17.
 */
@Path("albums")
public class AlbumResource implements AlbumApi {

    @Override
    @GET
    @Path("new-releases")
    @Produces("application/json")
    public List<Release> getNewReleases(@QueryParam("country") String country, @QueryParam("size") int size) {
        if (country == null) {
            country = "de";
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist"));
        }
        if (size < 1) {
            size = 10;
        }

        NewReleasesRequest request = SpotifyApi.getInstance().getNewReleases().country(country).limit(size).build();

        try {
            NewReleases topTracks = request.get();
            List<SimpleAlbum> newAlbums = topTracks.getAlbums().getItems();

            if (newAlbums.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No new releases found for query: %s", country)));
            }

            List<Release> topReleases = new ArrayList<Release>();
            for (SimpleAlbum album : newAlbums) {
                AlbumRequest albumRequest = SpotifyApi.getInstance().getAlbum(album.getId()).build();
                Album completeAlbum = albumRequest.get();

                if (completeAlbum == null) {
                    throw new RemoteApiException(new ErrorMessage(String.format("Spotify did not return album for id:" + album.getId())));
                }

                List<SimpleArtist> artists = completeAlbum.getArtists();

                Release release = new Release(album.getName(), ResponseDataHelper.artistNameList(completeAlbum.getArtists()));
                topReleases.add(release);
            }

            return  topReleases;
        } catch (Exception e) {
            throw new WebApplicationException("No releases could be found for country-code: " + country, 404);
        }
    }
}
