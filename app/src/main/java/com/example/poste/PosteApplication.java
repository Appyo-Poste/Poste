package com.example.poste;

import android.app.Application;
import android.content.Context;

import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.Post;
import com.example.poste.api.poste.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A custom application class that extends the Android Application class
 * It is used to maintain a global application context and
 * store information about the currently logged-in user
 */
public class PosteApplication extends Application {
    private static Context context;
    private static User currentUser;
    private static Folder currentFolder;
    private static Post currentPost;

    /**
     * Called when the application is starting
     *
     * This is where we initialize any data
     * that will be used across the entire application
     */
    public void onCreate() {
        super.onCreate();
        // Initialize the application context
        PosteApplication.context = getApplicationContext();
    }

    /**
     * Debug code (remove this function in final build)
     */
    public void dataMockUp() {
        currentUser = new User(12, "e@mail.com", "ed", "123");
        Post post1 = new Post(1, "Look at these ones!", "somewhere.com/1", 12);
        Post post2 = new Post(2, "Look at these twos!", "somewhere.com/2", 12);
        Post post3 = new Post(3, "Wowza! cool threes!", "somewhere.com/3", 12);
        ArrayList<Post> mockList1 = new ArrayList<>();
        ArrayList<Post> mockList2 = new ArrayList<>();
        mockList1.add(post1);
        mockList1.add(post2);
        mockList2.add(post3);
        HashMap<Integer, FolderAccess> userMockAccess = new HashMap<>();
        userMockAccess.put(12, FolderAccess.valueOf(3));
        currentUser.addFolder(new Folder(1, "One n Two Stuff", 12, mockList1, userMockAccess));
        currentUser.addFolder(new Folder(1, "three Stuff", 12, mockList2, userMockAccess));
        currentFolder = currentUser.getFolders().get(0);
    }

    /**
     * Debug code (remove this function in final build)
     */
    public void printMockData(){
        List<Folder> usersMockFolders = currentUser.getFolders();
        System.out.println("The user # " + currentUser.getId() + " named " + currentUser.getUsername() + " at " + currentUser.getEmail() + " has " + usersMockFolders.size() + " folders shown below:");
        for (int i = 0; i < usersMockFolders.size(); i++) {
            Folder f = usersMockFolders.get(i);
            System.out.println("Folder Id = " + f.getId() + " --- Name = " + f.getName() + " --- Owned By = " + f.getOwnerId() + " And holds these posts:");
            List<Post> mps = f.getPosts();
            for (int k = 0; k < mps.size(); k++) {
                Post p = mps.get(k);
                System.out.println("     Post Id = " + p.getId() + " --- Name = " + p.getName() + " --- Link = " + p.getLink() +" --- User Id of Post Owner = " + p.getOwnerId());
            }
            System.out.println("     Folder visibility is as follows:");
            HashMap<Integer, FolderAccess> al = f.getUsers();
            Iterator<Map.Entry<Integer, FolderAccess>> iter = al.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, FolderAccess> ent = iter.next();
                System.out.println("          User # " + ent.getKey() + " has access level: " + ent.getValue());
            }
        }
    }

    /**
     * Get the currently logged-in user
     *
     * @return The User object representing the currently logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the currently logged-in user
     *
     * @param currentUser The User object representing the currently logged-in user
     */
    public static void setCurrentUser(User currentUser) {
        PosteApplication.currentUser = currentUser;
    }

    /**
     * Get currently selected folder
     *
     * @return the currently selected folder
     */
    public static Folder getCurrentFolder() {return currentFolder; }

    /**
     * Sets which folder is currently selected
     *
     * @param folder the folder that we want to select
     */
    public static void setCurrentFolder(Folder folder) {
        currentFolder = folder;
    }

    /**
     * Get currently selected post
     *
     * @return the currently selected post
     */
    public static Post getCurrentPost() {return currentPost; }

    /**
     * Sets which post is currently selected
     *
     * @param post the post that we want to select
     */
    public static void setCurrentPost(Post post) {
        currentPost = post;
    }

    /**
     * Get the application's context
     *
     * @return The application's context
     */
    public static Context getAppContext() {
        return PosteApplication.context;
    }

    /**
     * Get the instance of the application
     *
     * @return The application instance as an `Application` object.
     */
    public static Application getApp() {
        return (Application) PosteApplication.context;
    }
}
