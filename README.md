# Poste

# Poste

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



# Branching Guidelines

Using GitHubFlow branching strategey.

You start off with the master branch then developers create branches, feature branches that stem directly from the master, to isolate their work which are then merged back into master. The feature branch is then deleted.

The main idea behind this model is keeping the master code in a constant deployable state and hence can support continuous integration and continuous delivery processes.

All feature branches are to be made from the `master` branch

Feature branches should be in camel case and be prefixed with `feature/` (example: `feature/addNewFolders`)

All PR (pull requests) will be reviewed by at least 2 other team members

![githubflow branching](https://www.flagship.io/wp-content/uploads/github-flow-branching-model.jpeg)
