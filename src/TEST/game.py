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
~ cell (rowNumber = 0 , content = "Y")
~ cell (rowNumber = 1 , content = "Y")
~ cell (rowNumber = 2 , content = "Y")
Condition
~ cell (rowNumber = 3 , content = "Y")
~ cell (rowNumber = 4 , content = "Y")
~ cell (rowNumber = 5 , content = "Y")
Condition
~ cell (rowNumber = 6 , content = "Y")
~ cell (rowNumber = 7 , content = "Y")
~ cell (rowNumber = 8 , content = "Y")
Conditions
~ cell (rowNumber = 0 , content = "Y")
~ cell (rowNumber = 4 , content = "Y")
~ cell (rowNumber = 8 , content = "Y")
Condition
~ cell (rowNumber = 2 , content = "Y")
~ cell (rowNumber = 4 , content = "Y")
~ cell (rowNumber = 6 , content = "Y")
Do
~ update GameState (winner = "") (winner = "player2")

#Game
~ create cell (rowNumber = 1, content = "")
~ create cell (rowNumber = 2, content = "")
~ create cell (rowNumber = 3, content = "")
~ create GameState (winner = "")

