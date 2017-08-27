# KnockKnockApplicationAssignment3
Client Server Socket Threaded KnockKnock Application


Using the multi-threaded version of the Knock Knock example as the basis for this assignment:

Create a simple Swing GUI to start and stop the clients and server and display the jokes.  All of the interaction does not have
to occur in this one GUI.  You may open other simple GUIs to replicate the behavior of the client cmd window. 
I expect you will need one application GUI that starts and stops the server.  It should not be possible to start a client unless 
the server has been started.  When the server is stopped, it is not necessary or even desirable to try to stop the clients.  
The application UI should be capable of starting multiple clients and displaying them simultaneously.

Handle the exceptions thrown by the stream and socket classes.

Read the jokes from a resource such as a file

Successive clients should get different initial knock-knock jokes.  You do not have to guarantee the difference, just the randomness 
of the initial joke.  Consider a load time solution that populates the clues and answers arrays randomly.  Also consider a run-time 
solution that has a random start location and operates on the lists as circular lists.

Add appropriate Javadoc comments to the source code.

Make any other changes you think should be made to improve the "OO-ness" of the design (at least one).  The example code cries 
out for moving code out of the main methods and into appropriate class methods. Please explain what changes you made in the comments 
while submitting your assignment.
