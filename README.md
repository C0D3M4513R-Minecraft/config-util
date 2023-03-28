# config-qual
## What is this?

This projects holds annotations and relevant classes for those classes, so that checkers for the correct use of the annotations can be written.

## About Semver:

I generally strive to use [semantic versioning](https://semver.org/) for all my projects.
But there are a few exceptions I have to communicate here (mainly because I have seen enough stuff in minecraft projects):
- The implementation of methods and Classes can change, even in Patch releases.
- Private methods and variables are considered implementation details.
- non-public methods and variables on final classes are considered implementation details.
- Annotations may be added or removed in Patch releases, if the method call is still [binary compatible](https://docs.oracle.com/javase/specs/jls/se17/html/jls-13.html#jls-13.2).

This means, that any java bytecode manipulation or java reflection may break in Patch releases.

These rules are only in place, because I publish as java-version 8. If I had java modules, I would probably not have to add these notes.

# About the license:

I do not want this project to be copy-pasted into other projects.
I have thus not yet decided on a license.
General rules are:
- If you want to use this project, please contact me as I have not yet decided on a License yet. And yes, this includes ANY USE.
    - The [Pixel-gaming](https://pixelgaming.co) group, under which this project's source is published under shall be free to use any and all projects contained in this repository.
- Redistributing the source of this project is not permitted.
- Forks shall be only allowed with the intent of contributing to this project.
  All changes in Forks must not be on the default branch.
  If you fork this repository, and end up changing your mind about contributing to this project I would kindly ask you to delete the repository.
  If you end up forking this repository, and accidentally push to the default branch, just reset the branch to before that commit, and push again.
- If you want to use this project in closed-source projects, or open-source for-profit projects you

# Contributing guidelines and Code ownership:

Whilst no rigid license is in place (no License file in the modules and no license information available in the maven projects) code ownership of anything in this repository shall automatically change to C0D3 M4513R.
This includes any contributions to this Project, including PR's.
This will allow for specifying a license later on.
Also in general code ownership shall be settled on a per-module basis.
