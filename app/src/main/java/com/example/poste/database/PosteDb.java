package com.example.poste.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.poste.database.dao.FolderDao;
import com.example.poste.database.dao.ItemFolderDao;
import com.example.poste.database.dao.PosteItemDao;
import com.example.poste.database.dao.TwitterTokenDao;
import com.example.poste.database.dao.UserDao;
import com.example.poste.database.dao.UserFolderDao;
import com.example.poste.database.entity.Folder;
import com.example.poste.database.entity.ItemFolder;
import com.example.poste.database.entity.PosteItem;
import com.example.poste.database.entity.TwitterToken;
import com.example.poste.database.entity.User;
import com.example.poste.database.entity.UserFolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, TwitterToken.class, PosteItem.class, Folder.class, UserFolder.class, ItemFolder.class}, version = 10)
public abstract class PosteDb extends RoomDatabase {

    private static volatile PosteDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UserDao userDao();
    public abstract TwitterTokenDao tokenDao();
    public abstract PosteItemDao posteTweetDao();
    public abstract UserFolderDao userFolderDao();
    public abstract ItemFolderDao itemFolderDao();
    public abstract FolderDao folderDao();

    public static PosteDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PosteDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PosteDb.class, "poste")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        //activityData = new PopulateDatabase(context).getActivityData();
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
