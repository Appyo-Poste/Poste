package com.example.poste.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Folder of the Poste app. Should closely mirror the Backend Folder model.
 */
public class Folder {

    /**
     * The title of the folder.
     */
    private final String title;

    /**
     * The user permission of the folder. Should mirror the FolderPermission class of the backend.
     * May be:
     * - "viewer": a viewer can only view posts within a folder
     * - "editor": an editor can add posts to a folder, edit existing posts, and delete posts
     * - "full_access": a full access user is an editor who can share the folder with other users
     */
    private final String userPermission;

    /**
     * The posts within the folder.
     */
    private final List<Post> posts;

    /**
     * The id of the folder, as identified by the backend.
     */
    private final String id;

    /**
     * Private constructor for the Folder class. Folder uses the Builder design pattern, so this
     * should not be called directly -- instead, use the {@link Builder} class to build a Folder.
     * @param builder the Builder to build the Folder from.
     */
    private Folder(Builder builder) {
        this.title = builder.title;
        this.userPermission = builder.userPermission;
        this.posts = builder.posts;
        this.id = builder.id;
    }


    /**
     * Returns the title of the folder.
     * @return the title of the folder.
     */
    public String getTitle() {
        return title;
    }

    public Post getPostFromFolder(String postId){
        for (Post post:
                posts) {
            if (post.getId().equals(postId)){
                return post;
            }
        }
        return null;
    }

    /**
     * Returns the user permission of the folder, as a String.
     * @return the user permission of the folder, as a String.
     */
    public String getUserPermission() {
        return userPermission;
    }

    /**
     * Returns a copy of the list of posts in the folder. This is a copy, so modifying the returned
     * list will not modify the folder's posts.
     * @return a copy of the list of posts in the folder.
     */
    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    /**
     * Returns the id of the folder, as a String.
     * @return the id of the folder, as a String.
     */
    public String getId() {
        return id;
    }

    /**
     * Static builder class for the Folder class. This is used to build a Folder object.
     */
    public static class Builder {

        /**
         * The title of the folder.
         */
        private String title;

        /**
         * The user permission of the folder. Should mirror the FolderPermission class of the
         * backend.
         * May be:
         * - "viewer": a viewer can only view posts within a folder
         * - "editor": an editor can add posts to a folder, edit existing posts, and delete posts
         * - "full_access": a full access user is an editor who can share the folder with others
         */
        private String userPermission;

        /**
         * The posts within the folder.
         */
        private List<Post> posts;

        /**
         * The id of the folder, as identified by the backend.
         */
        private String id;


        /**
         * Default constructor for the Builder class.
         */
        public Builder() {
        }

        /**
         * Sets the title of the folder.
         * @param title the title of the folder.
         * @return the Builder.
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the user permission of the folder. Should mirror the FolderPermission class of the
         * backend.
         * May be:
         * - "viewer": a viewer can only view posts within a folder
         * - "editor": an editor can add posts to a folder, edit existing posts, and delete posts
         * - "full_access": a full access user is an editor who can share the folder with others
         *
         * @param userPermission the user permission of the folder.
         * @return the Builder.
         */
        public Builder setUserPermission(String userPermission) {
            this.userPermission = userPermission;
            return this;
        }

        /**
         * Sets the posts within the folder.
         * @param posts the posts within the folder.
         * @return the Builder.
         */
        public Builder setPosts(List<Post> posts) {
            this.posts = posts;
            return this;
        }

        /**
         * Sets the id of the folder, as identified by the backend.
         * @param id the id of the folder, as identified by the backend.
         * @return the Builder.
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Builds a Folder object from the Builder.
         * @return a Folder object built from the Builder.
         */
        public Folder build() {
            return new Folder(this);
        }
    }
}
