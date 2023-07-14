package com.example.legacy.poste.database;

import android.app.Application;

import com.example.legacy.poste.database.dao.FolderDao;
import com.example.legacy.poste.database.dao.ItemFolderDao;
import com.example.legacy.poste.database.dao.PosteItemDao;
import com.example.legacy.poste.database.dao.TwitterTokenDao;
import com.example.legacy.poste.database.dao.UserDao;
import com.example.legacy.poste.database.dao.UserFolderDao;
import com.example.legacy.poste.database.entity.Folder;
import com.example.legacy.poste.database.entity.ItemFolder;
import com.example.legacy.poste.database.entity.PosteItem;
import com.example.legacy.poste.database.entity.TwitterToken;
import com.example.legacy.poste.database.entity.User;
import com.example.legacy.poste.database.entity.UserFolder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {

    private UserDao mUserDao;
    private TwitterTokenDao mTwitterTokenDao;
    private PosteItemDao mPosteItemDao;
    private ItemFolderDao mItemFolderDao;
    private UserFolderDao mUserFolderDao;
    private FolderDao mFolderDao;

    public AppRepository(Application application) {
        PosteDb db = PosteDb.getDatabase(application);
        mUserDao = db.userDao();
        mTwitterTokenDao = db.tokenDao();
        mPosteItemDao = db.posteTweetDao();
        mItemFolderDao = db.itemFolderDao();
        mUserFolderDao = db.userFolderDao();
        mFolderDao = db.folderDao();

    }

    public void insertUser(User user) {
        User existingUser = null;
        Future<User> future = PosteDb.databaseExecutor.submit(() -> mUserDao.getUser(user.email));
        try{
            existingUser = future.get();
            if (existingUser == null)
                PosteDb.databaseExecutor.execute(() -> {
                    mUserDao.insert(user);
                });
        }catch(Exception e){}
    }

    public long insertFolder(Folder folder) {
        long rowId = 0;
        Future<Long> future = PosteDb.databaseExecutor.submit(() -> mFolderDao.insert(folder));
            try {
            rowId = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void insertUserFolder(UserFolder userFolder) {
        PosteDb.databaseExecutor.execute(() -> {
            mUserFolderDao.insert(userFolder);
        });
    }

    public void insertPosteItem(PosteItem tweet) {
        PosteItem existingTweet = mPosteItemDao.getTweet(tweet.poste_item_id);
        if (mPosteItemDao.getTweet(tweet.poste_item_id) == null) {
            PosteDb.databaseExecutor.execute(() -> {
                mPosteItemDao.insert(tweet);
            });
        } else {
            PosteDb.databaseExecutor.execute(() -> {
                mPosteItemDao.update(tweet);
            });
        }
    }

    public void updateTwitterToken(String access_token, String refresh_token, String folder_id) {
        TwitterToken existingToken = mTwitterTokenDao.getToken();

        TwitterToken token = new TwitterToken();
        token.refresh_token = refresh_token;
        token.access_token = access_token;
        token.twitter_folder_id = folder_id;

        if (existingToken == null)
            PosteDb.databaseExecutor.execute(() -> {
                mTwitterTokenDao.insert(token);
            });
        else
            PosteDb.databaseExecutor.execute(() -> {
                mTwitterTokenDao.update(existingToken.access_token, token.access_token, token.refresh_token, token.twitter_folder_id);
            });
    }

    public void setTwitterId(String id) {
        PosteDb.databaseExecutor.execute(() -> {
            mTwitterTokenDao.setId(id);
        });
    }

    public void updateFolder(Folder folder) {
        PosteDb.databaseExecutor.execute(() -> {
            mFolderDao.update(folder);
        });
    }

    public User getUser(String email) {
        User user = null;
        Future<User> future = PosteDb.databaseExecutor.submit(() -> mUserDao.getUser(email));
        try {
            user = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }
    public String getFirstUsername(){
        String username = null;
        Future<String> future = PosteDb.databaseExecutor.submit(() -> mUserDao.getFirstUsername());
        try {
            username = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return username;
    }

    public TwitterToken getTwitterToken() {
        return mTwitterTokenDao.getToken();
    }

    public List<PosteItem> getTweets() {
        return mPosteItemDao.getTweets();
    }

    public List<PosteItem> getPostByFolder(String folderId){
        return mPosteItemDao.postsByFolder(folderId);
    }
    public List<Folder> getFolders(String username) {
        List<Folder> folderList = null;
        Future<List<Folder>> future = PosteDb.databaseExecutor.submit(() -> mFolderDao.foldersByUserName(username));
        try {
            folderList = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return folderList;
    }

    public UserFolder getUserFolderByUser(String id) {
        return mUserFolderDao.getUserFolderByUser(id);
    }

    public UserFolder getUserFolderByFolder(String id) {
        return mUserFolderDao.getUserFolderByFolder(id);
    }

    public ItemFolder getItemFolderByFolder(String id) {
        return mItemFolderDao.getItemFolderByFolder(id);
    }

    public ItemFolder getItemFolderByItem(String id) {
        return mItemFolderDao.getItemFolderByItem(id);
    }

    public void deleteFolder(Folder folder) {
        mFolderDao.delete(folder);
    }

    public Folder getFolder(String folderId) {
        return mFolderDao.getFolder(folderId);
    }
    public ItemFolder getItemFolder(String itemId, String folderId){
        ItemFolder itemFolder = null;
        Future<ItemFolder> future = PosteDb.databaseExecutor.submit(() -> mItemFolderDao.getItemFolder(itemId, folderId));
        try {
            itemFolder = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return itemFolder;
    }

    public void insertItemFolder(ItemFolder itemFolder) {
        ItemFolder newItemFolder = getItemFolder(itemFolder.poste_item_id, itemFolder.folder_id);
        if(getItemFolder(itemFolder.poste_item_id, itemFolder.folder_id) == null){
            mItemFolderDao.insert(itemFolder);
        }
    }
}
