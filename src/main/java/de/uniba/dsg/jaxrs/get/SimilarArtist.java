package de.uniba.dsg.jaxrs.get;

import com.wrapper.spotify.methods.ArtistSearchRequest;
import com.wrapper.spotify.methods.RelatedArtistsRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Page;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Created by rootX on 13/06/17.
 */

@Path("artists")
public class SimilarArtist {

    @GET
    @Path("similarArtist/{artist-name}")
    public Artist getSimilarArtist(@PathParam("artist-name") String name) {
        if (name == null || name.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-name"));
        }
        //Api api = Api.DEFAULT_API;

        ArtistSearchRequest artistRequest = SpotifyApi.getInstance().searchArtists(name).limit(1).build();

        try {
            Page<Artist> artistSearchResult = artistRequest.get();
            List<Artist> artists = artistSearchResult.getItems();

            // no artist found
            if (artists.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", name)));
            }

            Artist namedArtist = artists.get(0);

            RelatedArtistsRequest request = SpotifyApi.getInstance().getArtistRelatedArtists(namedArtist.getId()).build();

            List<Artist> relatedArtists = request.get();

            if (relatedArtists.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No artists similar to %s were found", name)));
            }


            Artist similarArtist = relatedArtists.get(0);

            return  similarArtist;
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with name: " + name, 404);
        }
    }
}
