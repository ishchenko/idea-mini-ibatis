Small IntelliJ IDEA iBatis/MyBatis plugin. http://plugins.intellij.net/plugin/?idea&id=6725

# Overview

Existing plugin won't work properly in recent IDEA versions, and I need some of them in a project I work on.
This plugin is not in official repo yet, has to be tested in large project first.

# Features

* Supports multiple sqlmap configurations per module. In fact it does not even bother with configurations (yet), only sqlmap files matter.
* "Go to Declaration" for literal expressions inside any method parameters, e.g. inside spring's SqlMapClientOperations#queryForObject parameters etc.
* Autocompletion inside any literal expressions