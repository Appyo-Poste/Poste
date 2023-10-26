package com.example.poste;

import android.app.Application;
import android.content.Context;

import com.example.poste.models.Folder;
import com.example.poste.models.Post;
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
    // This needs to get refactored out of the code
    private static User currentUser;
    private static Folder selectedFolder;
    private static Post selectedPost;

    /**
     * Called when the application is starting
     *
     * This is where we initialize any data
     * that will be used across the entire application
     */
    public void onCreate() {
        super.onCreate();
        // Initialize the application context
    }

    /**
     * Debug code (remove this function in final build)
     */
    public void dataMockUp() {
        Post.Builder builder1 = new Post.Builder();
        Post.Builder builder2 = new Post.Builder();
        Post.Builder builder3 = new Post.Builder();
        builder1.setTitle("Look at these ones!");
        builder2.setTitle("Look at these twos!");
        builder3.setTitle("Wowza! cool threes!");
        builder1.setDescription("Desc 1");
        builder2.setDescription("Desc 2");
        builder3.setDescription("Desc 3");
        builder1.setUrl("www.youtube.com");
        builder2.setUrl("www.facebook.com");
        builder3.setUrl("www.https://github.com/.com");
        builder1.setId("a");
        builder2.setId("b");
        builder3.setId("c");
        Post post1 = builder1.build();
        Post post2 = builder2.build();
        Post post3 = builder3.build();
        Folder.Builder fBuild1 = new Folder.Builder();
        Folder.Builder fBuild2 = new Folder.Builder();
        fBuild1.setId("fa");
        fBuild2.setId("fb");
        fBuild1.setTitle("Folder 1");
        fBuild2.setTitle("Folder 2");
        fBuild1.setUserPermission("full_access");
        fBuild2.setUserPermission("viewer");
        ArrayList<Post> mockList1 = new ArrayList<>();
        ArrayList<Post> mockList2 = new ArrayList<>();
        mockList1.add(post1);
        mockList1.add(post2);
        mockList2.add(post3);
        fBuild1.setPosts(mockList1);
        fBuild2.setPosts(mockList2);
        Folder folder1 = fBuild1.build();
        Folder folder2 = fBuild1.build();
        com.example.poste.models.User.getUser().addFolder(folder1);
        com.example.poste.models.User.getUser().addFolder(folder2);
        selectedFolder = com.example.poste.models.User.getUser().getFolders().get(0);
    }

    /**
     * Debug code (remove this function in final build)
     */
    public void printMockData(){
        List<Folder> usersMockFolders = com.example.poste.models.User.getUser().getFolders();
        System.out.println("The user has the folders shown below:");
        for (int i = 0; i < usersMockFolders.size(); i++) {
            Folder f = usersMockFolders.get(i);
            System.out.println("Folder Id = " + f.getId() + " --- Title = " + f.getTitle() + " --- Access Level = " + f.getUserPermission() + " And holds these posts:");
            List<Post> mps = f.getPosts();
            for (int k = 0; k < mps.size(); k++) {
                Post p = mps.get(k);
                System.out.println("     Post Id = " + p.getId() + " --- Title = " + p.getTitle() + " --- Description = " + p.getDescription() + " --- Link = " + p.getUrl());
            }
        }
    }

    /**
     * Get the currently logged-in user
     *
     * @return The User object representing the currently logged-in user
     */
    public static com.example.poste.api.poste.models.User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the currently logged-in user
     *
     * @param currentUser The User object representing the currently logged-in user
     */
    public static void setCurrentUser(com.example.poste.api.poste.models.User currentUser) {
        PosteApplication.currentUser = currentUser;
    }

    /**
     * Get currently selected folder
     *
     * @return the currently selected folder
     */
    public static Folder getSelectedFolder() {return selectedFolder; }

    /**
     * Sets which folder is currently selected
     *
     * @param folder the folder that we want to select
     */
    public static void setSelectedFolder(Folder folder) {
        selectedFolder = folder;
    }

    /**
     * Get currently selected post
     *
     * @return the currently selected post
     */
    public static Post getSelectedPost() {return selectedPost; }

    /**
     * Sets which post is currently selected
     *
     * @param post the post that we want to select
     */
    public static void setSelectedPost(Post post) {
        selectedPost = post;
    }
}
