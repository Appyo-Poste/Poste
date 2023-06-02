# Developer Documentation - GitHub Branching
*[Click here to return to table of contents](../home.md)*

Using GitHubFlow branching strategey.

You start off with the master branch then developers create branches, feature branches that stem directly from the master, to isolate their work which are then merged back into master. The feature branch is then deleted.

The main idea behind this model is keeping the master code in a constant deployable state and hence can support continuous integration and continuous delivery processes.

All feature branches are to be made from the `master` branch

Feature branches should be in camel case and be prefixed with `feature/` (example: `feature/addNewFolders`)

All PR (pull requests) will be reviewed by at least 2 other team members

![githubflow branching](https://www.flagship.io/wp-content/uploads/github-flow-branching-model.jpeg)