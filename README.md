# Poste
You can find the project documentation [here](./docs/home.md)

To get started you will need to [download and setup Android Studio](./docs/devDocs/projectConfig/androidStudio.md) and once that is completed you will need to [host the API](./docs/devDocs/projectConfig/apiConfig.md).

All the API endpoints are documented in the [apiDocs.md](./docs/devDocs/apiDocs.md) file.



# Application Structure

## `Poste/app/src/main/java/com/example/poste/activities`
Contains all the activities in the application. Each activity has its own package. For more 
information, see: https://developer.android.com/guide/components/activities/intro-activities

Each activity corresponds to a screen in the application, represented by an XML file in
`Poste/app/src/main/res/layout/`.

The following activities are present in the application:

| Activity               | XML                                | Description                                                                                |
|------------------------|------------------------------------|--------------------------------------------------------------------------------------------|
| `IntroActivity`        | `activity_intro.xml`               | The first activity that is launched when the application is opened                         |
| `LoginActivity`        | `activity_login.xml`               | The activity that is launched when a user clicks the "Login" button on the intro screen    |
| `RegisterActivity`     | `activity_register.xml`            | The activity that is launched when a user clicks the "Register" button on the intro screen |
| `AccountActivity`      | `activity_account.xml`             | Edit account screen. Currently, only allows the user to change their password              |
| `DashboardActivity`    | `activity_dashboard.xml`           | Appears to be the folder dashboard (viewing folders)                                       |
| `EditFolderActivity`   | `activity_edit_folder.xml`         | Allows the user to edit a folder                                                           |
| `EditPostActivity`     | `activity_edit_post.xml`           | Allows the user to edit a post                                                             |
| `FolderViewActivity`   | `activity_folder_view.xml`         | Appears to be the folder view (inside a folder)                                            |
| `LinkAccounts`         | `activity_link_account.xml`        | Appears to allow a user to link an account to Twitter (may be deprecated)                  |
| `MainActivity`         | `activity_main.xml`                | Appears to be a base activity that other activities extend                                 |
| `OptionsActivity`      | `activity_options.xml`             | Appears to give options to Link Accounts or Signout                                        |
| `Shared_Folder`        | `activity_shared_folder.xml`       | Appears to allow a user to share a folder                                                  |
| `PActivity`            | -                                  |                                                                                            |
| `redditInstructions`   | `activity_reddit_instructions.xml` | (May be deprecated)                                                                        |
| `TwitterTokenActivity` | `activity_twitter_token.xml`       | (May be deprecated)                                                                        |


## `Poste/app/src/main/java/com/example/poste/api/poste/`
Contains `poste`, `reddit` and `twitter` packages. Ostensibly, `reddit` and `twitter` may end up
being pruned due to disuse of those APIs.

## `Poste/app/src/main/java/com/example/poste/api/poste/API.java`
Stores the base API URL and functions to call endpoints. Utilized throughout the application.

## `Poste/app/src/main/java/com/example/poste/api/poste/models`
Contains the models used in the application: `Folder`, `FolderAccess`, `Post` and `User`

## `Poste/app/src/main/java/com/example/poste/api/poste/exceptions`
Contains custom exceptions for this application: 
- `APIException`
- `EmailAlreadyUsedException`
- `IncompleteRequestException`
- `MalformedResponseException`
- `NoUserFoundException`

 

## `Poste/app/src/main/java/com/example/poste/PosteApplication.java`
Stores the global application context and information about the currently logged-in user

## `Poste/app/src/main/java/com/example/poste/utils/HTTPRequest.java` 

Handles HTTP requests to the API. Based on okhttp3 and performs all requests asynchronously. 

 
# Debugging Application 
To run spotbug use the command gradle spotbugsDebug. For checkstlye use gradle checkstyle or run from file in android studio. For PMD use gradle pmd or run from file in android studio. 

Spotbugs: default rule set with the addition of https://find-sec-bugs.github.io/ 

Checkstyle: Uses a modified version of checkstlye_hard from https://github.com/noveogroup/android-check. Need to update to match changes in checkstyle formatting.

pmd: Uses the pmd_hard rule set from https://github.com/noveogroup/android-check.