# eCore [![Apache 2.0 License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](https://github.com/EqualExperts/eHelp/blob/master/eCore/LICENSE)

Provide Service so Someone Can Flag a Problem Somewhere

## Guidelines

### Spec tests 

Spec tests (which may include acceptance or smoke tests) should:
 * be written as documentation so developers can consult them to understand the API provided;
 * be automatically published after every successful CI build so a developer can easily consult the specs.
 
[This](http://rafaelfiume.github.io/tictactoe) and [this](http://rafaelfiume.github.io/basket-tax-and-price-calculator) are two examples of spec tests that follow these guidelines. 

## Running the Application

Build with:

    sbt compile

Then starts Cheese-Web with:

    $ heroku local web [... are we really going to use Heroku? ...]

Check <a href="http://www.scala-sbt.org/release/tutorial/Running.html">this</a> out to see the most common sbt commands.

## Stories 

eCore stories are in [this](https://trello.com/b/1gkUc5Ma/ehelp) Trello board.