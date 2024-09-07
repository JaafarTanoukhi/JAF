#Prop cell
rowNumber, content

Move fillX
Do 
~ update this (content = "X")

Move fillO
Do
~ update this (content = "O")


#Player player1
#Player player2



#Prop GameState
winner


#Event checkXWin
Condition
~ cell (rowNumber = 0 , content = "X")
~ cell (rowNumber = 1 , content = "X")
~ cell (rowNumber = 2 , content = "X")
Condition
~ cell (rowNumber = 3 , content = "X")
~ cell (rowNumber = 4 , content = "X")
~ cell (rowNumber = 5 , content = "X")
Condition
~ cell (rowNumber = 6 , content = "X")
~ cell (rowNumber = 7 , content = "X")
~ cell (rowNumber = 8 , content = "X")
Condition
~ cell (rowNumber = 0 , content = "X")
~ cell (rowNumber = 4 , content = "X")
~ cell (rowNumber = 8 , content = "X")
Condition
~ cell (rowNumber = 2 , content = "X")
~ cell (rowNumber = 4 , content = "X")
~ cell (rowNumber = 6 , content = "X")

Do
~ update GameState (winner = "") (winner = "player1")

#Event checkYWin
Condition
~ cell (rowNumber = 0 , content = "O")
~ cell (rowNumber = 1 , content = "O")
~ cell (rowNumber = 2 , content = "O")
Condition
~ cell (rowNumber = 3 , content = "O")
~ cell (rowNumber = 4 , content = "O")
~ cell (rowNumber = 5 , content = "O")
Condition
~ cell (rowNumber = 6 , content = "O")
~ cell (rowNumber = 7 , content = "O")
~ cell (rowNumber = 8 , content = "O")
Condition
~ cell (rowNumber = 0 , content = "O")
~ cell (rowNumber = 4 , content = "O")
~ cell (rowNumber = 8 , content = "O")
Condition
~ cell (rowNumber = 2 , content = "O")
~ cell (rowNumber = 4 , content = "O")
~ cell (rowNumber = 6 , content = "O")
Do
~ update GameState (winner = "") (winner = "player2")

#Game
~ create cell (rowNumber = 0, content = "")
~ create cell (rowNumber = 1, content = "")
~ create cell (rowNumber = 2, content = "")
~ create cell (rowNumber = 3, content = "")
~ create cell (rowNumber = 4, content = "")
~ create cell (rowNumber = 5, content = "")
~ create cell (rowNumber = 6, content = "")
~ create cell (rowNumber = 7, content = "")
~ create cell (rowNumber = 8, content = "")
~ create cell (rowNumber = 9, content = "")

~ create GameState (winner = "")

