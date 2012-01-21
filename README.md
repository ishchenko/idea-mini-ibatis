Small IntelliJ IDEA iBatis/MyBatis plugin. http://plugins.intellij.net/plugin/?idea&id=6725

# Overview

Existing plugin won't work properly in recent IDEA versions, and I use iBATIS in my project. I solve my own
problems myself, thus this plugin includes a set of features I use on a daily basis. Feel free to post
feature requests: https://github.com/ishchenko/idea-mini-ibatis/issues

# Features

* Supports multiple sqlmap configurations per module. In fact it does not even bother with configurations (yet), only sqlmap files matter.
* "Go to Declaration" for literal expressions inside any method parameters, e.g. inside spring's SqlMapClientOperations#queryForObject parameters etc.
* "Find Usages" for statements
* Autocompletion inside any literal expressions
* MyBatis 3 proxy methods - "Go to Implementaion" jumps right into xml
* Some basic sqlMap file navigation
