# eHelp [![Build Status](https://travis-ci.org/EqualExperts/eHelp.svg?branch=master)](https://travis-ci.org/EqualExperts/eHelp) [![Apache 2.0 License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](https://github.com/EqualExperts/eHelp/blob/master/LICENSE.txt)

Building microservices so we can test the system, document specs and the exchange of messages between them.

## Guidelines

### Spec tests 

Spec tests (which may include acceptance or smoke tests) should:
 * be written as documentation so developers can consult them to understand the API provided;
 * be automatically published after every successful CI build so a developer can easily consult the specs.
 
[This](http://rafaelfiume.github.io/tictactoe) and [this](http://rafaelfiume.github.io/basket-tax-and-price-calculator) are two examples of spec tests that follow these guidelines. 

## Stories 

eHelp stories are in [this](https://trello.com/b/1gkUc5Ma/ehelp) Trello board.
