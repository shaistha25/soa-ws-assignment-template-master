package de.uniba.dsg.jaxrs.get;

import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.methods.ArtistRequest;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.models.ErrorMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

/**
 * Created by rootX on 12/06/17.
 */

@Path("artists")
public class SingleArtist {
    @GET
    @Path("{artist-id}")
    public Artist getArtist(@PathParam("artist-id") String id) {
        if (id == null || id.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //Api api = Api.DEFAULT_API;

        ArtistRequest request = SpotifyApi.getInstance().getArtist(id).build();

        try {
            Artist artist = request.get();

            return  artist;
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + id, 404);
        }
    }
}

