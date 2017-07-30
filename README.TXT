
PROJECTS:

Java project created in Eclipse Mars 2 Release (4.5.2); Windows 10

Java jdk1.8.0_101 (64 bits) ==> Mandatory. 
Maven 3.3
JUnit 4.9
Jetty 9.2.11v2015
javax.servlet 3.0
Jersey-server 2.0

Two projects:

1)GameOfThreeServerJava:

Embedded Jetty Web Server: with two apps:

1.1)Server layer of JAX-RS 2.0 Asynchronous Layer (Server part of the Longpolling Strategy)
1.2)Web Client (html, jquery 1.9.1, ajax)

2)GameOfThreeClientJava:

Part of console testing (register an user and generate seed); just for testing funcionalities.

ARCHITECHTURE:

As I said before; this challenge can be separated in two parts:

1)Server part.

I have created an embedded jetty server project (executed as a java project) instead of war/ear project; 
with the objetive of portability specified in the assessment. For the same reason i don't use libraries
that aren't strictly necessary; so i don't use any logging library a part of the standar output channel.

I choose the option of JAX-RS Asynchronous/AJAX long polling rather than other possibilities such as
websockets; because this option is more mature and can fit with the relative small amount of messages
that generate the program.


2)Client part.

I have created two projects:

2.1)A webclient project (included in GameOfThreeServerJava project)
2.2)A console project (included in GameOfThreeClientJava project)

I created both; because i have considered that a console project is not enough for testing all the features
of this challengue.

Creating a webclient has helped me e.g for testing the concurrency of the calling of different clients

Keep in mind that is a "quick and dirty" client just for testing all the server features, so sorry if it's
a bit clumsy

Naturally in a production environment; i must implement the client part with other tools instead of html tags
and jquery.  (for example; angularJS)

With this documentation; i attach a pdf with instructions of how to use the web client
