package com.example.poste.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a Post of the Poste app. Should closely mirror the Backend Post model.
 */
public class Post {

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The description of the post.
     */
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The url of the post.
     */
    private String url;

    /**
     * The id of the post, as identified by the backend.
     */
    private final String id;

    /**
     * The tags this post has
     */
    private ArrayList<String> tags;

    /**
     * Private constructor for the Post class. Post uses the Builder design pattern, so this should
     * not be called directly -- instead, use the {@link Builder} class to build a Post.
     * @param builder the Builder to build the Post from.
     */
    private Post(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.url = builder.url;
        this.id = builder.id;
        this.tags = builder.parseTags();
    }

    /**
     * Returns the title of the post.
     * @return the title of the post.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the post.
     * @return the description of the post.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the url of the post.
     * @return the url of the post.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the id of the post, as a String.
     * @return the id of the post, as a String.
     */
    public String getId() { return id; }

    public ArrayList<String> getTags() {return tags; }
    /**
     * Static builder class for the Post class. This is used to build a Post object.
     */
    public static class Builder {

        /**
         * The title of the post.
         */
        private String title;

        /**
         * The description of the post.
         */
        private String description;

        /**
         * The url of the post.
         */
        private String url;

        /**
         * The id of the post, as identified by the backend.
         */
        private String id;

        /**
         * The list of tags as one comma separated string.
         */
        private String tags;

        /**
         * Default constructor for the Builder class.
         */
        public Builder() {
        }

        /**
         * Sets the title of the post.
         * @param title the title of the post.
         * @return the Builder.
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the description of the post.
         * @param description the description of the post.
         * @return the Builder.
         */
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the url of the post.
         * @param url the url of the post.
         * @return the Builder.
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Sets the id of the post, as identified by the backend.
         * @param id the id of the post, as identified by the backend.
         * @return the Builder.
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the tags of the post, as identified by the backend.
         * @param tags the tags of the post, as identified by the backend.
         * @return the Builder.
         */
        public Builder setTags (String tags) {
            this.tags = tags;
            return this;
        }

        private ArrayList<String> parseTags() {
            ArrayList<String> ret = null;
            if (tags.length() > 0) {
                ret = new ArrayList<String>(Arrays.asList(tags.split(",")));
            }
            else {
                ret = new ArrayList<>();
            }
            return ret;
        }

        /**
         * Builds a Post object from the Builder.
         * @return the Post object.
         */
        public Post build() {
            return new Post(this);
        }
    }
}
