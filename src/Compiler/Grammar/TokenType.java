package Compiler.Grammar;


      public enum TokenType {
            //Single-character Tokens
            BLOCK_START, STATEMENT_START, LISTING, COMMA,
            OPEN_BRACKET, CLOSE_BRACKET, OPEN_PAIRING, CLOSE_PAIRING, OPEN_CARTESIAN, CLOSE_CARTESIAN,
            MINUS, PLUS, DIVIDE, TIMES, MODULUS, EQUAL,
            
            //One or two character tokens
            BANG, BANG_EQUAL,
            GREATER, GREATER_EQUAL,
            LESS, LESS_EQUAL,
            
            //Literals
            IDENTIFIER, STRING, NUMBER, 
            
            //Keywords
            TRUE, FALSE, 
            PLAYING, PROP, GAME, PLAYER, EVENT, THIS,
            PLAY,CREATE, TRANSFER, UPDATE, DESTROY, SWITCH, 
            LOCKED, UNLOCKED, 
            MOVE, CONDITION, DO,
            
            EOF
      }
      
