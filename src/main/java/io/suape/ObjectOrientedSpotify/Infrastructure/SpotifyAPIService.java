package io.suape.ObjectOrientedSpotify.Infrastructure;

import io.suape.ObjectOrientedSpotify.Domain.Playlist;
import io.suape.ObjectOrientedSpotify.Domain.Track;
import io.suape.ObjectOrientedSpotify.Domain.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface SpotifyAPIService {
    String getAuthorizationCode(String state);

    String getAccessToken(String code, String redirectUri);

    String refreshAccessToken(String refreshToken);

    List<Playlist> getCurrentUserPlaylists(String accessToken, int limit, int offset);

    //Playlist Related API Calls
    Playlist getPlaylist(String accessToken, Playlist playlist, List<String> fields);

    Playlist getPlaylistById(String accessToken, String playlistId, List<String> fields);

    boolean setPlaylistName(String accessToken, Playlist playlist, String newName);

    boolean setPlaylistPrivate(String accessToken, Playlist playlist);

    boolean setPlaylistPublic(String accessToken, Playlist playlist);

    String getPlaylistCoverURL(String accessToken, Playlist playlist);

    Playlist createPlaylist(String accessToken, String playlistName, boolean isPrivate);

    boolean deletePlaylist(String accessToken, Playlist playlist);

    boolean addTracksToPlaylist(String accessToken, List<Track> tracks, Playlist playlist);

    //Playlist Related API Calls

    //Track Related API Calls
    List<Track> getPlaylistTracks(String accessToken, Playlist playlist);

    Track getTrack(String accessToken, String track_id);

    List<Track> getTracksByTracks(String accessToken, List<Track> track_ids);

    List<Track> getTracksByIds(String accessToken, String track_ids);

    Map<String, Object> getTrackAudioFeatures();

    List<Map<String, Object>> getTracksAudioFeatures();

    String getTrackImage();
    //Track Related API Calls

    //User Related API Calls
    String getSpotifyUserId(User user);
    //User Related API Calls

}
