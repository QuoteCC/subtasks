package io.github.quotecc.subtasks;

/**
 * Created by cCorliss on 5/1/17.
 */

public class SQLContract {
    private SQLContract(){};

    public static class FeedEntry{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DUE = "due";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_PARENT = "parent";

        /*

        ID|Content|Due|Note|Parent

         */


    }

}
