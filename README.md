Portugal - Building microservices so we can test the system, document specs and the exchange of messages between them.

# eHelp [![Build Status](https://travis-ci.org/EqualExperts/eHelp.svg?branch=master)](https://travis-ci.org/EqualExperts/eHelp) [![Apache 2.0 License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](https://github.com/EqualExperts/eHelp/blob/master/LICENSE.txt)

Provide Service so Someone Can Flag a Problem Somewhere

## Running the Application

Build with:

    sbt compile

Then starts eCore with:

    $ heroku local web [... are we really going to use Heroku? ...]

Check <a href="http://www.scala-sbt.org/release/tutorial/Running.html">this</a> out to see the most common sbt commands.

# Guidelines

## Spec tests 

Spec tests (which may include acceptance or smoke tests) should:
 * be written as documentation so developers can consult them to understand the API provided;
 * be automatically published after every successful CI build so a developer can easily consult the specs.
 
[This](http://rafaelfiume.github.io/tictactoe) and [this](http://rafaelfiume.github.io/basket-tax-and-price-calculator) are two examples of spec tests that follow these guidelines. 

# Stories 

eHelp stories are in [this](https://trello.com/b/1gkUc5Ma/ehelp) Trello board.

# About Monorepos

This project was first intended to be managed as a monorepos, that is, 
it would contain everything from the microservices that composes the stack till the testing tools in a single Git repository.

The idea is quite cool: everything necessary for a project in one place. Everything you need is there, just clone the project and you are in! (Much has been sad about how nice monorepos are, including [Facebook and Google](http://danluu.com/monorepo/) use it.)

Unfortunately, we've found difficult (not to say impractical) to embrace monorepos in our current context. 

### CI build and deploy configuration

We want to build apps so people can use it, which (for us it) means being able to deploy apps in a safe, cheap and easy way. 
That translates to build and deploy (if the build succeeds) with nothing more than a 'git push to the repository.
 
We are integrating Git, TravisCI and Heroku to achieve that, and there's where our problems with monorepos begin: 
it's hard to integrate everything when having a Git project with many apps, and very very hard when these app are developed for different plataforms (e.g. Android, JDK, Erlang OTP).

Here are a few links to articles and texts where people are trying to come across this sort of problems:

* http://larsen.io/blog/resonance/2013/11/03/sbt_multiprojects_and_heroku.html
* http://stackoverflow.com/questions/5977234/how-can-i-push-a-part-of-my-git-repo-to-heroku
* https://github.com/travis-ci/travis-ci/issues/3767
* http://stackoverflow.com/questions/26241683/heroku-deploy-a-sub-directory
* http://stackoverflow.com/questions/7539382/how-can-i-deploy-push-only-a-subdirectory-of-my-git-repo-to-heroku

Plus, there are well-know [conceptual and performance challenges](https://developer.atlassian.com/blog/2015/10/monorepos-in-git/) with Monorepos.

### So?

So we are backing away from monorepos for now. But we do have interesting questions here, and, maybe, we should come back to monorepos one day.


