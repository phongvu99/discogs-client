package com.naughtybitch.discogsapi;

import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleaseResponse;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.POJO.ShowAllResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiscogsAPI {

    /*
    Get request to issue a search query to the Discogs database. This
    endpoint accepts pagination parameters
     */
    @GET("database/search")
    Call<SearchResponse> getSearchResult(@Query("q") String query, @Query("per_page") int per_page, @Query("page") int page);

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
    Call<ShowAllResponse> fetchMasterReleaseVersions(@Path("master_id") int master_id, @Query("per_page") int per_page, @Query("page") int page);

}
