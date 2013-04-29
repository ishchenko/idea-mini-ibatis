Small IntelliJ IDEA iBatis/MyBatis plugin. http://plugins.intellij.net/plugin/?idea&id=6725

# Overview

Existing plugin won't work properly in recent IDEA versions, and I use iBATIS in my project. I solve my own
problems myself, thus this plugin includes a set of features I use on a daily basis. Feel free to post
feature requests: https://github.com/ishchenko/idea-mini-ibatis/issues

# Features

* iBATIS: Supports multiple sqlMap configurations per module. In fact it does not even bother with configurations (yet), only sqlMap files matter.
* iBATIS: "Go to Declaration" for literal expressions, e.g. inside spring's `SqlMapClientOperations#queryForObject` parameters etc.
* iBATIS: "Find Usages" for sqlMap statements
* iBATIS: Copy Reference (Ctrl+Alt+Shift+C) copies qualified statement name to clipboard
* iBATIS: Statement id completion inside any literal expressions
* iBATIS: Some basic sqlMap file navigation
* iBATIS: Quick Documentation (Ctrl+Q) on statement id in java code shows statement sql
* iBATIS: Concatenated literals supported as statement references 
* MyBatis: Proxy interfaces support, "Go to Implementaion" jumps right into mapper xml
* MyBatis: Proxy interface methods inspection, methods that have no mapper implementation marked as error
* MyBatis: Some basic mapper file navigation
