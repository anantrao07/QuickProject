package com.salesforce.freemind.salesforce101.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by anant on 2017-09-21.
 */

@ContentProvider(authority = AllMoviesProvider.AUTHORITY , database = AllMoviesDataBase.class)

public final class AllMoviesProvider {
    private AllMoviesProvider() {
    }


    public static final String AUTHORITY = "com.salesforce.freemind.salesforce101.data.AllMoviesProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIESPATH = "allmovies";
        String MOVIES_TRAILERS = "movies_trailers";
        String MOVIES_REVIEWS = "movies_reviews";


    }

    private static Uri uriBuilder(String... paths) {

        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths) {
            builder.appendPath(path);
        }

        return builder.build();

    }

    @TableEndpoint(table = AllMoviesDataBase.allmovies)
    public static class AllMovies {
        @ContentUri(
                path = Path.MOVIESPATH,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = AllMoviesColumns._ID + " ASC"
        )
        public static final Uri CONTENT_URI = uriBuilder(Path.MOVIESPATH);

        public static Uri getUri() {
            return CONTENT_URI;

        }

        @InexactContentUri(
                name = "_id",
                path = Path.MOVIESPATH + "/#",
                type = "vnd.android.cursor.item/allmovies",
                whereColumn = AllMoviesColumns.MOVIESAVED,
                pathSegment = 1)
        public static Uri withId(long id) {
            return uriBuilder(Path.MOVIESPATH, String.valueOf(id));
        }
    }

    @TableEndpoint(table = AllMoviesDataBase.trailers)
    public static class MovieTrailers {
        @ContentUri(
                path = Path.MOVIES_TRAILERS,
                type = "vnd.android.cursor.dir/movies_trailers",
                defaultSort = TrailersColumns.COLUMN_TMOVIE_ID + " ASC"
        )
        public static final Uri CONTENT_URI = uriBuilder(Path.MOVIES_TRAILERS);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES_TRAILERS + "/#",
                type = "vnd.android.cursor.item/movies_trailers",
                whereColumn = TrailersColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return uriBuilder(Path.MOVIES_TRAILERS, String.valueOf(id));

        }
    }

    @TableEndpoint(table = AllMoviesDataBase.reviews)
    public static class MovieReviews {
        @ContentUri(
                path = Path.MOVIES_REVIEWS,
                type = "vnd.android.cursor.dir/movies_reviews",
                defaultSort = ReviewsColumns.COLUMN_RMOVIE_ID + " ASC"
        )
        public static final Uri CONTENT_URI = uriBuilder(Path.MOVIES_REVIEWS);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES_REVIEWS + "/#",
                type = "vnd.android.cursor.item/movies_reviews",
                whereColumn = ReviewsColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return uriBuilder(Path.MOVIES_REVIEWS, String.valueOf(id));
        }


    }


}

