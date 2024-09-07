#JAF
A compiler (technically, interpreter) I developed for my University Project. JAF allows you to write game logic for turn-based games by first defining the game rules and then "querying" the runtime engine through game queries, in the same way you might set up a database by creating its tables and then writing queries to interact with it.

JAF is still in its early stages of development, but it is functional with a Tic-Tac-Toe (XO) example. You can try building it if you'd like. I plan to heavily update JAF in the future by adding more features and possibly generating documentation automatically from the source code itself. Future updates may range from simply adding features to a complete overhaul of the programming language and approach.

Currently JAF is written with Java so you can simply try it by:
```
1. cloning the project
2. Downloading JavaFX and adding it to your project(to try the XO test)
3. Compiling and running TicTacToe.java
```
If you look closely at the source code, you will find that the XO game is just a UI interface and the actual game logic to check for a win is done on the compiler side by querying it. The queries are being logged to the console, and the win checking is happening at every move.

Hopefully, I can make this more robust and usable in future iterations.
