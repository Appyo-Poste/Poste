package com.example.poste.callbacks;

/**
 * Interface for callbacks to be invoked after attempting to delete a post.
 * Implement this interface to enable callbacks triggered after deleting a post.
 * Callbacks handle the results of asynchronous operations, ensuring the application remains
 * responsive and dynamic -- we cannot use return values to handle the results of async operations.
 * Define the desired actions based on the deletion outcome by overriding these methods.
 *<p>
 * Always pass an instance of your implemented interface to the {@code deletePost} method to
 * activate this callback mechanism.
 * For example:
 *
 * <pre>
 * {@code
 * PostDeletionCallback callback = new PostDeletionCallback() {
 *     @Override
 *     public void onSuccess() {
 *         // Handle successful post deletion here.
 *     }
 *
 *     @Override
 *     public void onError(String errorMessage) {
 *         // Handle error in post deletion here.
 *     }
 * };
 *
 * User user = User.getUser();
 * user.deletePost(post, callback);
 * }
 * </pre>
 *
 * @see com.example.poste.models.User#deletePostFromServer
 */
public interface PostDeletionCallback {
    void onSuccess();
    void onError(String errorMessage);
}
