package Compiler;

public enum TokenType {
      //Single-character Tokens
      BLOCK_START, STATEMENT_START, STATEMENT_LISTING, VARIABLE_LISTING, COMMA,
      CLOSE_BRACKET, OPEN_BRACKET, CLOSE_PAIRING, OPEN_PAIRING, CLOSE_CARTESIAN, OPEN_CARTESIAN,
      MINUS, PLUS, DIVIDE, TIMES, MODULUS, EQUAL,
  
      //One or two character tokens
      BANG, BANG_EQUAL,
      GREATER, GREATER_EQUAL,
      LESS, LESS_EQUAL,
  
      //Literals
      IDENTIFIER, STRING, NUMBER, 
  
      //Keywords
      TRUE, FALSE, 
      PROP, GAME, PLAYER, EVENT, 
      CREATE, TRANSFER, UPDATE, DESTROY, 
      LOCKED, UNLOCKED, 
      CONDITION, DO,
  
      EOF
}
