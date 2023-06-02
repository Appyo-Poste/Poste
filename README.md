# Poste
You can find the project documentation [here](./docs/home.md)


# LEGACY DOCUMENTATION (OLD PLEASE REFERENCE [NEW DOCUMENTATION](./docs/home.md))

"getting-started.zip" contains the backend server files.

Create a Twitter API Account at: https://developer.twitter.com/en/portal/dashboard      "Recently changed there basic API Tier so in future might not be free"

Android Studio:
  - Download Android Studio. 
  - Download and extract the project. 
  - Open android studio and from there open the project.

Twitter
Step 1:

  - Create/Own a twitter account.
  - Create application and generate tokens from https://developer.twitter.com/en/portal/dashboard.
  - apikey.properties contains the keys you will need for the project. Place Tokens into the "" in apikey.
  - Projec should now work and you should be able to link your twitter account.
  
Backend & Database
Step 2:

- Create a server such as google cloud. "You dont need to use google cloud"
  - Heres a link that can help you with setting up your backend service https://cloud.google.com/appengine/docs/standard/php-gen2/building-app.
  - getting-started.zip will have a similar copy to the repository that you would grab in the totorial link above. You can essentialy overwrite the files.
  - As per the totorial you will need to overwrite the files in app.yaml with the correct datebase credentials.
