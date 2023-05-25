package com.example.poste.api.twitter;

import android.app.Application;

import com.example.PosteApplication;
import com.example.poste.BuildConfig;
import com.example.poste.database.AppRepository;
import com.example.poste.database.entity.ItemFolder;
import com.example.poste.database.entity.PosteItem;
import com.example.poste.database.entity.TwitterToken;
import com.example.poste.database.entity.UserFolder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20Service;
import com.twitter.clientlib.model.Get2UsersIdBookmarksResponse;
import com.twitter.clientlib.model.Get2UsersMeResponse;
import com.twitter.clientlib.model.Media;
import com.twitter.clientlib.model.Photo;
import com.twitter.clientlib.model.Tweet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This is an example of getting an OAuth2 access token and using it to call an API.
 * It's expected to set TWITTER_OAUTH2_CLIENT_ID & TWITTER_OAUTH2_CLIENT_SECRET in TwitterCredentialsOAuth2
 *
 * Example steps:
 * 1. Getting the App Authorization URL.
 * 2. User should click the URL and authorize it.
 * 3. After receiving the access token, setting the values into TwitterCredentialsOAuth2.
 * 4. Call the API.
 */

public class OAuth20GetAccessToken {

    public static String apiCall(Application app) {

        AppRepository appRepository = new AppRepository(app);
        TwitterToken twitterToken = appRepository.getTwitterToken();

        TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(BuildConfig.TWITTER_CLIENT_KEY,
                BuildConfig.TWITTER_CLIENT_SECRET,
                twitterToken.access_token,
                twitterToken.refresh_token);

        return callApi(credentials, app);
    }

    public static List<Tweet> getTweets() {

        AppRepository appRepository = new AppRepository(PosteApplication.getApp());
        TwitterToken twitterToken = appRepository.getTwitterToken();

        TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(BuildConfig.TWITTER_CLIENT_KEY,
                BuildConfig.TWITTER_CLIENT_SECRET,
                twitterToken.access_token,
                twitterToken.refresh_token);

        TwitterApi apiInstance = new TwitterApi(credentials);

        // Set the params values
        String id = twitterToken.twitter_id; // String | The ID of the authenticated source User for whom to return results.
        Integer maxResults = 56; // Integer | The maximum number of results.
        //String paginationToken = "paginationToken_example"; // String | This parameter is used to get the next 'page' of results.
        String tweetFieldsString = "attachments, author_id, context_annotations, conversation_id, created_at, entities, edit_controls, geo, id, in_reply_to_user_id, lang, possibly_sensitive, referenced_tweets, reply_settings, source, text, withheld";
        String expansionsString = "attachments.media_keys, attachments.poll_ids, author_id, edit_history_tweet_ids, entities.mentions.username, geo.place_id, in_reply_to_user_id, referenced_tweets.id, referenced_tweets.id.author_id";
        String mediaFieldsString = "alt_text, duration_ms, height, media_key, non_public_metrics, organic_metrics, preview_image_url, promoted_metrics, public_metrics, type, url, variants, width";
        String pollFieldsString = "duration_minutes, end_datetime, id, options, voting_status";
        String userFieldsString = "created_at, description, entities, id, location, name, pinned_tweet_id, profile_image_url, protected, public_metrics, url, username, verified, withheld";
        String placeFieldsString = "contained_within, country, country_code, full_name, geo, id, name, place_type";

        Set<String> tweetFields = new HashSet<>(Arrays.asList(tweetFieldsString.split(", "))); // Set<String> | A comma separated list of Tweet fields to display.
        Set<String> expansions = new HashSet<>(Arrays.asList(expansionsString.split(", "))); // Set<String> | A comma separated list of fields to expand.
        Set<String> mediaFields = new HashSet<>(Arrays.asList(mediaFieldsString.split(", "))); // Set<String> | A comma separated list of Media fields to display.
        Set<String> pollFields = new HashSet<>(Arrays.asList(pollFieldsString.split(", "))); // Set<String> | A comma separated list of Poll fields to display.
        Set<String> userFields = new HashSet<>(Arrays.asList(userFieldsString.split(", "))); // Set<String> | A comma separated list of User fields to display.
        Set<String> placeFields = new HashSet<>(Arrays.asList(placeFieldsString.split(", "))); // Set<String> | A comma separated list of Place fields to display.
        try {
            Get2UsersIdBookmarksResponse result = apiInstance.bookmarks().getUsersIdBookmarks(id)
                    .tweetFields(tweetFields)
                    .expansions(expansions)
                    .mediaFields(mediaFields)
                    .pollFields(pollFields)
                    .userFields(userFields)
                    .placeFields(placeFields)
                    .execute();
            System.out.println(result.getData());
            List<Tweet> tweetList = result.getData();

            List<Media> mediaList = result.getIncludes().getMedia();
            HashMap<String, String> mediaHash = new HashMap<String, String>();
            Photo mediaPhoto = null;
            for (Media media: mediaList){
                if(media.getType().equals("photo")) {
                    mediaPhoto = (Photo) media;
                    mediaHash.put(media.getMediaKey(), mediaPhoto.getUrl().toString());
                }
            }

            for (Tweet tweet: tweetList
                 ) {
                PosteItem newTweet = new PosteItem();
                newTweet.poste_item_id = tweet.getId();
                newTweet.text = tweet.getText();
                if(tweet.getAttachments() != null)
                    newTweet.media_key = tweet.getAttachments().getMediaKeys().get(0);
                newTweet.media_url = mediaHash.get(newTweet.media_key);
                appRepository.insertPosteItem(newTweet);
                ItemFolder itemFolder = new ItemFolder();
                itemFolder.folder_id = appRepository.getTwitterToken().twitter_folder_id;
                itemFolder.poste_item_id = newTweet.poste_item_id;
                appRepository.insertItemFolder(itemFolder);
            }

            return result.getData();
        } catch (ApiException e) {
            System.err.println("Exception when calling BookmarksApi#getUsersIdBookmarks");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAuthUrl() {
        TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(BuildConfig.TWITTER_CLIENT_KEY,
                BuildConfig.TWITTER_CLIENT_SECRET,
                null,
                null,
                true);

        TwitterOAuth20Service service = new TwitterOAuth20Service(
                credentials.getTwitterOauth2ClientId(),
                credentials.getTwitterOAuth2ClientSecret(),
                "https://test-project-379806.wl.r.appspot.com/twitter/auth",
                "offline.access tweet.read users.read bookmark.read like.read");
        try {
            System.out.println("Fetching the Authorization URL...");

            final String secretState = "cannunziello@hotmail.com";
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            String authorizationUrl = service.getAuthorizationUrl(pkce, secretState);
            return authorizationUrl;
        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
            e.printStackTrace();
        }
        return null;
    }

    public static OAuth2AccessToken getAccessToken(String code) {
        TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(BuildConfig.TWITTER_CLIENT_KEY,
                BuildConfig.TWITTER_CLIENT_SECRET,
                null,
                null,
                true);

        TwitterOAuth20Service service = new TwitterOAuth20Service(
                credentials.getTwitterOauth2ClientId(),
                credentials.getTwitterOAuth2ClientSecret(),
                "https://test-project-379806.wl.r.appspot.com/twitter/auth",
                "offline.access tweet.read users.read");
        try {
            OAuth2AccessToken accessToken = null;
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            accessToken = service.getAccessToken(pkce, code);
            return accessToken;

        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
            e.printStackTrace();
        }
        return null;
    }



    public static OAuth2AccessToken getAccessToken(TwitterCredentialsOAuth2 credentials) {
        TwitterOAuth20Service service = new TwitterOAuth20Service(
                credentials.getTwitterOauth2ClientId(),
                credentials.getTwitterOAuth2ClientSecret(),
                "https://test-project-379806.wl.r.appspot.com/twitter/auth",
                "offline.access tweet.read users.read");

        OAuth2AccessToken accessToken = null;
        try {
            final Scanner in = new Scanner(System.in, "UTF-8");
            System.out.println("Fetching the Authorization URL...");

            final String secretState = "state";
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            String authorizationUrl = service.getAuthorizationUrl(pkce, secretState);

            System.out.println("Go to the Authorization URL and authorize your App:\n" +
                    authorizationUrl + "\nAfter that paste the authorization code here\n>>");
            final String code = in.nextLine();
            System.out.println("\nTrading the Authorization Code for an Access Token...");
            accessToken = service.getAccessToken(pkce, code);

            System.out.println("Access token: " + accessToken.getAccessToken());
            System.out.println("Refresh token: " + accessToken.getRefreshToken());
        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
            e.printStackTrace();
        }
        return accessToken;
    }

    public static String callApi(TwitterCredentialsOAuth2 credentials, Application app) {
        TwitterApi apiInstance = new TwitterApi(credentials);
        /*
        OAuth2AccessToken token = null;
        try {
            token = apiInstance.refreshToken();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        if(token == null)
            return null;
        credentials.setTwitterOauth2AccessToken(token.getAccessToken());
        credentials.setTwitterOauth2RefreshToken(token.getRefreshToken());
        */

        Set<String> userFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of User fields to display.
        Set<String> expansions = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of fields to expand.
        Set<String> tweetFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of Tweet fields to display.

        tweetFields.add("author_id");
        tweetFields.add("id");
        tweetFields.add("created_at");

        userFields.add("id");

        try {
            Get2UsersMeResponse result = apiInstance.users().findMyUser()
                    .userFields(userFields)
                    .expansions(expansions)
                    .tweetFields(tweetFields)
                    .execute();
            System.out.println(result);
            return result.getData().getId();
        } catch (ApiException e) {
            System.err.println("Exception when calling UsersApi#findMyUser");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return null;
    }
}