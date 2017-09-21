package com.salesforce.freemind.salesforce101.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by anant on 2017-09-21.
 */

public class AllMoviesColumns {

    //Table structure of the recorded trip table
    @DataType(INTEGER)
    @PrimaryKey
    public static final String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    public static final String MOVIEID = "id";

    @DataType(TEXT)
    @NotNull
    public static final String MOVIETITLE = "title";

    @DataType(TEXT)
    @NotNull
    public static final String MOVIEPOSTERPATH = "poster_path";

    @DataType(INTEGER)
    public static final String MOVIESAVED = "saved"; //0  - no , 1 - yes


}
