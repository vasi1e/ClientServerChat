# ClientServerChat

## What is it?

The project is for server-client chat application

## How is done?

We have Server part, which redirects messages from one user to another. The Client part is for reading and writing to the server

### Server: Keeps data for online users, chats and groups. Controlls one Thread (UserThread), which reads the text that the client has send and tell the server to send that text to another user.

### Client: Controlls two Threads for reading and writing

## How to start this project?

You need to run files server/src/Implimentations/ChatServer.java(with port as argument) and client/src/client/ChatClient.java(with host and port as argument)

## How to use it?

After starting as Client you will be asked for name and action you want to take. For now only "chat" mode is available. Now you will be asked with whom you want to start chat (only online people, the server will crash otherwise). You can chat with them! When you're in the chat the server will continue to send you messages from it or from another person, but to replay to them you will have to leave the current chat and start one with the person who texted you. To leave you need to text bye. To leave the server you need to write quit when asked for action.

## Future ideas

Make group chats 
Make GUI
