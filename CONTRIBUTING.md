# How to contribute to Eclipse Milo

First of all, thank you for considering a contribution to Eclipse Milo. We really appreciate the time and effort you want to
spend improving the project, and we can use the help :-)

Here is a (non-exclusive, non-prioritized) list of things you might be able to help us with:

* documentation (Getting Started guide, Examples, etc...)
* bug reports
* bug fixes
* code quality improvements
* features (both ideas and code are welcome)

## Legal Requirements

Before you can get started an annoying but necessary bureaucratic requirement must be satisfied.


Milo is an [Eclipse IoT](https://iot.eclipse.org) project and as such is governed by the Eclipse Development process.
This process helps us in creating great open source software within a safe legal framework.

As a contributor the following preliminary steps are required in order for us to be able to accept your contribution:

* Sign the [Eclipse Contributor Agreement](https://www.eclipse.org/legal/ECA.php).
In order to do so:

  * Obtain an Eclipse Foundation user ID. Anyone who currently uses Eclipse Bugzilla or Gerrit systems already has one of those.
If you don't already have an account simply [register on the Eclipse web site](https://accounts.eclipse.org/user/register).
  * Once you have your account, log in to the [accounts portal](https://accounts.eclipse.org/), select *Eclipse Contributor Agreement* from the box on the right.

* Add your GitHub username to your Eclipse Foundation account. Log in to Eclipse and go to [my account](https://accounts.eclipse.org/), select *Edit Profile*.

The easiest way to contribute code/patches/whatever is by creating a GitHub pull request (PR).
When you do make sure that you *Sign-off* your commit records using the same email address used for your Eclipse account.

You do this by adding the `-s` flag when you make the commit(s), e.g.

    $> git commit -s -m "Shave the yak some more"

You can find all the details in the [Contributing via Git](http://wiki.eclipse.org/Development_Resources/Contributing_via_Git) document on the Eclipse web site.

## Making your Changes

* Fork the repository on GitHub
* Create a new branch for your changes
* Make your changes
* When you create new files make sure you include a proper license header at the top of the file (see License Header section below).
* Make sure you include test cases for non-trivial features
* Make sure the test suite passes after your changes
* Make sure checkstyle passes after your changes
* Apply `google-java-format` (see https://github.com/google/google-java-format) 
* Commit your changes into that branch
* Use descriptive and meaningful commit messages
* If you have a lot of commits squash them into a single commit
* Make sure you use the `-s` flag when committing as explained above
* Push your changes to your branch in your forked repository

## Submitting the Changes

Submit a pull request via the normal GitHub UI.

## After Submitting

* Do not use your branch for any other development, otherwise further changes that you make will be visible in the PR.

## License Header

Please make sure new source files have license header like this:

````
/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
````
