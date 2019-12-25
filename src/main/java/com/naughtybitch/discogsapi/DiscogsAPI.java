package com.naughtybitch.discogsapi;

import com.google.gson.JsonObject;
import com.naughtybitch.POJO.AddToCollectionResponse;
import com.naughtybitch.POJO.AddToWantlistResponse;
import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.ArtistResponse;
import com.naughtybitch.POJO.CollectionResponse;
import com.naughtybitch.POJO.CollectionValueResponse;
import com.naughtybitch.POJO.IdentityResponse;
import com.naughtybitch.POJO.LabelReleasesResponse;
import com.naughtybitch.POJO.LabelResponse;
import com.naughtybitch.POJO.MasterReleaseResponse;
import com.naughtybitch.POJO.MasterReleaseVersionsResponse;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.POJO.ReleaseResponse;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.POJO.WantlistResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiscogsAPI {

    /*
    Get request to issue a search query to the Discogs database. This
    endpoint accepts pagination parameters
     */
    @GET("database/search")
    Call<SearchResponse> getSearchResult(@Query("q") String query, @Query("per_page") int per_page, @Query("page") int page);

    @GET("database/search")
    Call<SearchResponse> getSearchResult(@Query("per_page") int per_page, @Query("page") int page, @Query("genre") String genre);

    /*
    The Master resource represents a set of similar Releases.
    Masters (also known as “master releases”) have a “main release” which
    is often the chronologically earliest.
     */
    @GET("masters/{master_id}")
    Call<MasterReleaseResponse> fetchMasterData(@Path("master_id") int master_id);

    /*
    Returns a list of Releases and Masters associated with the Artist.
    Accepts Pagination.
     */
    @GET("artists/{artist_id}/releases")
    Call<ArtistReleasesResponse> fetchArtistReleases(@Path("artist_id") int artist_id, @Query("sort") String sort, @Query("sort_order") String sort_order
            , @Query("per_page") int per_page, @Query("page") int page);

    /*
    Retrieves a list of all Releases that are versions of this master. Accepts Pagination parameters.
     */
    @GET("masters/{master_id}/versions")
    Call<MasterReleaseVersionsResponse> fetchMasterReleaseVersions(@Path("master_id") int master_id, @Query("per_page") int per_page, @Query("page") int page);

    /*
    The Release resource represents a particular physical or digital object released by one or more Artists.
     */
    @GET("releases/{release_id}")
    Call<ReleaseResponse> fetchRelease(@Path("release_id") int release_id);

    /*
    The Artist resource represents a person in the Discogs
    database who contributed to a Release in some capacity.
     */
    @GET("artists/{artist_id}")
    Call<ArtistResponse> fetchArtist(@Path("artist_id") int artist_id);

    /*
    The Label resource represents a label, company, recording studio,
    location, or other entity involved with Artists and Releases.
    Labels were recently expanded in scope to include things that aren’t
    labels – the name is an artifact of this history.
     */
    @GET("labels/{label_id}")
    Call<LabelResponse> fetchLabel(@Path("label_id") int label_id);

    /*
    Returns a list of Releases associated with the label. Accepts Pagination parameters.
     */
    @GET("labels/{label_id}/releases")
    Call<LabelReleasesResponse> fetchLabelReleases(@Path("label_id") int label_id, @Query("per_page") int per_page, @Query("page") int page);

    /*
    Retrieve a user by username.
    If authenticated as the requested user, the email key will be visible, and the num_list count will include
    the user’s private lists. If authenticated as the requested user or the user’s collection/wantlist is public,
    the num_collection / num_wantlist keys will be visible.
     */
    @GET("users/{username}")
    Call<ProfileResponse> fetchProfile(@Path("username") String username);

    /*
    Retrieve basic information about the authenticated user.
    You can use this resource to find out who you’re authenticated as,
    and it also doubles as a good sanity check to ensure that you’re using OAuth correctly.
    For more detailed information, make another request for the user’s Profile.
     */
    @GET("oauth/identity")
    Call<IdentityResponse> getIdentity();

    /*
    Returns the list of item in a folder in a user’s collection. Accepts Pagination parameters.
    Basic information about each release is provided, suitable for display in a list. For detailed information, make another API call to fetch the corresponding release.
    If folder_id is not 0, or the collection has been made private by its owner, authentication as the collection owner is required.
    If you are not authenticated as the collection owner, only public notes fields will be visible.

    Valid sort keys are:
    label
    artist
    title
    catno
    format
    rating
    added
    year
     */
    @GET("users/{username}/collection/folders/{folder_id}/releases")
    Call<CollectionResponse> fetchCollection(@Path("username") String username, @Path("folder_id") int folder_id, @Query("per_page") int per_page, @Query("page") int page);

    /*
    Returns the list of releases in a user’s wantlist. Accepts Pagination parameters.
    Basic information about each release is provided, suitable for display in a list. For detailed information,
    make another API call to fetch the corresponding release.
    If the wantlist has been made private by its owner, you must be authenticated as the owner to view it.
    The notes field will be visible if you are authenticated as the wantlist owner.
     */
    @GET("users/{username}/wants")
    Call<WantlistResponse> fetchWishlist(@Path("username") String username, @Query("per_page") int per_page, @Query("page") int page);

    /*
    Returns the minimum, median, and maximum value of a user’s collection.
    Authentication as the collection owner is required.
     */
    @GET("users/{username}/collection/value")
    Call<CollectionValueResponse> fetchCollectionValue(@Path("username") String username);

    /*
    Add a release to a folder in a user’s collection.
    The folder_id must be non-zero – you can use 1 for “Uncategorized”.
    Authentication as the collection owner is required.
     */
    @POST("users/{username}/collection/folders/{folder_id}/releases/{release_id}")
    Call<AddToCollectionResponse> addToCollectionFolder(@Path("username") String username, @Path("folder_id") int folder_id, @Path("release_id") int release_id);

    /*
    Add a release to a user’s wantlist.
    Authentication as the wantlist owner is required.
     */
    @PUT("users/{username}/wants/{release_id}")
    Call<AddToWantlistResponse> addToWantlist(@Path("username") String username, @Path("release_id") int release_id);

    /*
    Remove a release from a user's wantlist.
    Authentication as the wantlist owner is required.
     */
    @DELETE("users/{username}/wants/{release_id}")
    Call<JsonObject> removeFromWantlist(@Path("username") String username, @Path("release_id") int release_id);

}
