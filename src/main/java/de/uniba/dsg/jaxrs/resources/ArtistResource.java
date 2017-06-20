package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.methods.ArtistRequest;
import com.wrapper.spotify.methods.RelatedArtistsRequest;
import com.wrapper.spotify.methods.TopTracksRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxrs.Helpers.ResponseDataHelper;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rootX on 18/06/17.
 */
@Path("artists")
public class ArtistResource implements ArtistApi {
    @GET
    @Path("{artist-id}")
    public Interpret getArtist(@PathParam("artist-id") String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //Api api = Api.DEFAULT_API;

        ArtistRequest request = SpotifyApi.getInstance().getArtist(artistId).build();

        try {
            Artist artist = request.get();

            return ResponseDataHelper.interpetFrom(artist);
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + artistId, 404);
        }
    }

    @GET
    @Path("{artist-id}/top-tracks")
    @Produces("application/json")
    public List<Song> getTopTracks(@PathParam("artist-id") String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //Api api = Api.DEFAULT_API;

        TopTracksRequest request = SpotifyApi.getInstance().getTopTracksForArtist(artistId, "de").build();

        try {
            List<Track> topTracks = request.get();

            if (topTracks.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching top-tracks found for artist-id: %s", artistId)));
            }

            // Size of the track list must be <= 5
            if (topTracks.size() > 5) {
                topTracks = topTracks.subList(0, 5);
            }

            List<Song> topSongs = new ArrayList<Song>();

            for (Track track : topTracks) {
                Song newSong = new Song(track.getName(), ResponseDataHelper.artistNameList(track.getArtists()), track.getDuration() / 1000.0);
                topSongs.add(newSong);
            }

            return topSongs;
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + artistId, 404);
        }
    }

    @GET
    @Path("{artist-id}/top-track")
    public Song getTopTrack(@PathParam("artist-id") String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //Api api = Api.DEFAULT_API;

        TopTracksRequest request = SpotifyApi.getInstance().getTopTracksForArtist(artistId, "de").build();

        try {
            List<Track> topTracks = request.get();

            if (topTracks.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching top-tracks found for artist-id: %s", artistId)));
            }

            // Size of the track list must be <= 5
            if (topTracks.size() > 5) {
                topTracks = topTracks.subList(0, 5);
            }

            ArrayList<Song> topSongs = new ArrayList<Song>();

            for (Track track : topTracks) {
                Song newSong = new Song(track.getName(), ResponseDataHelper.artistNameList(track.getArtists()), track.getDuration() / 1000.0);
                topSongs.add(newSong);
            }

            return  topSongs.get(0);
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + artistId, 404);
        }
    }

    @GET
    @Path("{artist-id}/similar-artist")
    public Interpret getSimilarArtist(@PathParam("artist-id") String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-name"));
        }

        RelatedArtistsRequest request = SpotifyApi.getInstance().getArtistRelatedArtists(artistId).build();

        try {
            List<Artist> relatedArtists = request.get();

            if (relatedArtists.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No artists similar to %s were found", artistId)));
            }

            Artist similarArtist = relatedArtists.get(0);

            return ResponseDataHelper.interpetFrom(similarArtist);
        } catch (Exception e) {
            throw new WebApplicationException("No artist found with id: " + artistId, 404);
        }
    }
}
