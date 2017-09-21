package com.salesforce.freemind.salesforce101.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by anant on 2017-09-21.
 */

public class ReviewsColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String COLUMN_MOVIE_ID_REVIEW = "movie_id_review";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String COLUMN_RMOVIE_ID = "rmovie_id";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_AUTHOR = "author";


    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_CONTENT = "content";
}
