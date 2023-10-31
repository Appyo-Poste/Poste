package com.example.poste.tests;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.poste.models.Folder;
import com.example.poste.models.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataModelUnitTest {
    String postID = "1";
    String postURL = "www.example.com";
    String postTitle = "EX 1";
    String postDescription = "example post";
    Post testPost = new Post.Builder()
            .setId(postID)
            .setUrl(postURL)
            .setDescription(postDescription)
            .setTitle(postTitle)
            .build();

    @Test
    public void postModelGetIDTest(){
        assertEquals(postID, testPost.getId());
    }

    @Test
    public void postModelGetURLTest(){
        assertEquals(postURL, testPost.getUrl());
    }

    @Test
    public void postModelGetTitleTest(){
        assertEquals(postTitle, testPost.getTitle());
    }

    @Test
    public void postModelGetDescriptionTest(){
        assertEquals(postDescription, testPost.getDescription());
    }

    @Test
    public void postModelSetTitleTest(){
        String updatedTitle = "SET EX 1";
        testPost.setTitle(updatedTitle);
        assertEquals(updatedTitle, testPost.getTitle());
    }

    @Test
    public void postModelSetDescriptionTest(){
        String updatedDescription = "SET example post";
        testPost.setDescription(updatedDescription);
        assertEquals(updatedDescription, testPost.getDescription());
    }

    @Test
    public void postModelSetURLTest(){
        String updatedURL = "www.setexample.com";
        testPost.setUrl(updatedURL);
        assertEquals(updatedURL, testPost.getUrl());
    }

    String folderID = "1";
    String folderTitle = "Test Folder";
    String folderUserPermission = "full_access";
    List<Post> postsInFolder = new ArrayList<>(Arrays.asList(testPost));
    Folder testFolder = new Folder.Builder()
            .setId(folderID)
            .setTitle(folderTitle)
            .setUserPermission(folderUserPermission)
            .setPosts(postsInFolder)
            .build();

    @Test
    public void folderModelGetIDTest(){
        assertEquals(folderID, testFolder.getId());
    }

    @Test
    public void folderModelGetTitleTest(){
        assertEquals(folderTitle,testFolder.getTitle());
    }

    @Test
    public void folderModelGetUserPermissionTest(){
        assertEquals(folderUserPermission, testFolder.getUserPermission());
    }

    @Test
    public void folderModelGetPostsTest(){
        assertEquals(postsInFolder, testFolder.getPosts());
    }

}