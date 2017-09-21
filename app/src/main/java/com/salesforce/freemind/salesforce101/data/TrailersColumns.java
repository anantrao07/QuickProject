package com.salesforce.freemind.salesforce101.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by anant on 2017-09-21.
 */

public class TrailersColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";
    @DataType(DataType.Type.INTEGER)@NotNull
    public static final String COLUMN_MOVIE_ID_trailer = "movie_id_trailer";

    @DataType(DataType.Type.INTEGER)@NotNull
    public static final String COLUMN_TMOVIE_ID = "tmovie_id";


    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_MOVIE_TRAILER_NAME = "trailer_name";


    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_MOVIE_TRAILER_KEY = "trailer_key";
}
