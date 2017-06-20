package de.uniba.dsg.jaxrs.get;

import com.wrapper.spotify.methods.NewReleasesRequest;
import com.wrapper.spotify.models.NewReleases;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.models.ErrorMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Created by rootX on 13/06/17.
 */

@Path("albums")
public class NewestMusic {
    @GET
    @Path("{country-id}/count/{count}")
    public NewReleases getNewReleases(@PathParam("country-id") String id, @PathParam("count") int count) {
        if (id == null || id.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        if (count == 0) {
            count = 10;
        }
        //Api api = Api.DEFAULT_API;

        NewReleasesRequest request = SpotifyApi.getInstance().getNewReleases().country(id).limit(count).build();

        try {
            NewReleases topTracks = request.get();

            return  topTracks;
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + id, 404);
        }
    }
}
