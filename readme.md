About
-----

`jira-browser` displays the tickets that are stored in a directory using the 
[jira-sync](https://github.com/programmiersportgruppe/jira-sync) tool. 

It looks for `.json` files in the current directory. 


Getting Started
---------------

* Make sure you have the JDK **1.8** on your machine

* `mvn package`
* `sudo cp target/jira-browser-1-capsule-thin.x /usr/local/bin/jira-browser` 

    (this only works on unix systems. on windows you need to take the jar
    file and do a `java -jar jira-browser-1-capsule-thin.jar` or create 
    a batch script that does this.
* `cd mytickets`
* `jira-browser` will open the UI
