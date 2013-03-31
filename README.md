TED
===

Description
-----------

TED is a Task Executing Distributor. A task is a class implementing [java.lang.Runnable](http://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html) interface.

Building and running the project
--------------------------------
To build the project you will need [Leiningen 2](https://github.com/technomancy/leiningen/tree/stable).
After cloning the repository go to its directory and run TED:

    $ cd ted
    $ lein deps
    $ lein install
    $ lein run

To run the tests, open a new console, make sure you are in the project's directory, and enter the following task:

    $ lein midje

Interface
---------

TED comunicates through the HTTP/JSON interface listening on the 8330 port.
Available queries:

* `GET /tasks/schedule?class=fully.qualified.class.Name` schedules a task implemeted by the class *fully.qualified.class.Name*
* `GET /tasks/count-scheduled` returns a number of scheduled tasks
* `GET /tasks/count-running` returns a number of running tasks

Example
-------

In `/src/codilime/ted/example` there is a class `Itemize.java` which was used in the tests. This task can be scheduled with:

    $ wget http://localhost:8330/tasks/schedule?class=codilime.ted.example.Itemize

After that, every second one of 5 most popular JVM programming languages will be printed out.

It is best to add new classes representing tasks which could be scheduled in the `/src` directory.
