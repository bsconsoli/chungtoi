/**
 * @file ChungToi.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * @modified November 2017 
 *
 * @section DESCRIPTION
 * 
 * Implementation of the game Chung Toi, as specified in TRABALHO DA AREA 1: JOGO CHUNG TOI DISTRIBUIDO
 * EM JAVA RMI by Roland Teodorowitsch. Updated to conform to TRABALHO 2: JOGO CHUNG TOI AUTOMATIZADO
 * EM JAVA WEB SERVICES by Roland Teodorowitsch
 * 
 */

package chungtoi;

public class ChungToi {

	private StringBuilder board;
	private boolean whiteTurn;  // Saves whose turn it is currently; true for white player, false for black player
    private int blkPcs = 0;
    private int whtPcs = 0;

	public ChungToi(){
		whiteTurn = true;
		board = new StringBuilder(".........");
	}

	/**
	 * @brief Set new piece on board.
	 * 
	 * One of two ways to affect the board. It checks the validity of the arguments 
	 * provided and, if valid, it attempts to set a new piece for the current player on the board.
	 * If successful, it then cycles the turn to the next player.
	 *
	 * The checks performed are the following:
	 *	- Checks if the position provided by argument is a valid position in the board;
	 *  - Checks if the orientation provided by argument is a valid orientation for a piece;
	 *  - Checks if the position provided by argument is not already occupied by anothe piece;
	 *	
	 */

	public int setPiece(int position, int orientation){

        if (whiteTurn && whtPcs == 3 || !whiteTurn && blkPcs == 3) return -5;
		if (!validPosition(position)) return -3;
		if (!validOrientation(orientation)) return -3;
		if (!positionEmpty(position)) return 0;
		board.setCharAt(position, getPiece(orientation));
        if (whiteTurn) whtPcs++;
        else blkPcs++;
        nextPlayer();
		return 1;
	}

	/**
	 * @brief Move an existing piece to another position.
	 * 
	 * One of two ways to affect the board. It checks the validity of the arguments 
	 * provided and, if valid, it attempts to move a piece from one position to another
	 * on the board. If successful, the inicial position is emptied and the new position is filled with
	 * the appropriate piece. It then cycles the turn to the next player.
	 *
	 * The checks performed are the following:
	 *	- Checks if the position provided by argument is a valid position in the board;
	 *  - Checks if the position provided by argument holds a piece owned by the current player;
	 *  - Checks if the orientation provided by argument is a valid orientation for a piece;
	 *  - Checks if the direction provided by argument is a valid direction for a piece to move;
	 *  - Checks if the distance provided by argument is a valid distance fir a piece to move;
	 *  - Checks if the combination of arguments provided result in a valid move, returning the
	 *      new position after the move
	 *	
	 */

	public int movePiece(int initialPosition, int moveDirection, int moveDistance, int newOrientation){
        if (whiteTurn && whtPcs != 3 || !whiteTurn && blkPcs != 3) return -5;
		if (!validPosition(initialPosition)) return -1;
		if (!currentPlayerPiece(initialPosition)) return -3;
		if (!validOrientation(newOrientation)) return -3;
		if (!validDirection(moveDirection)) return -3;
		if (!validDistance(moveDistance)) return -3;
		int newPosition = findNewPosition(initialPosition, findOrientation(initialPosition), newOrientation, moveDirection, moveDistance);
		if (newPosition == -1) return 0;
		board.setCharAt(initialPosition, '.');
		board.setCharAt(newPosition, getPiece(newOrientation));
		nextPlayer();
		return 1;
	}

	/**
	 * @brief Check for a victory condition having been reached.
	 * 
	 * Takes each separate character from the String that composes the board and checks the game for
	 * the achievement of a victory condition, either by the white player or the black player.
	 *
	 */

	public int checkVictory(){
		char board0 = board.charAt(0);
		char board1 = board.charAt(1);
		char board2 = board.charAt(2);
		char board3 = board.charAt(3);
		char board4 = board.charAt(4);
		char board5 = board.charAt(5);
		char board6 = board.charAt(6);
		char board7 = board.charAt(7);
		char board8 = board.charAt(8);
		if(whitePiece(board0) && whitePiece(board1) && whitePiece(board2)) return 1;
		if(whitePiece(board3) && whitePiece(board4) && whitePiece(board5)) return 1;
		if(whitePiece(board6) && whitePiece(board7) && whitePiece(board8)) return 1;
		if(whitePiece(board0) && whitePiece(board3) && whitePiece(board6)) return 1;
		if(whitePiece(board1) && whitePiece(board4) && whitePiece(board7)) return 1;
		if(whitePiece(board2) && whitePiece(board5) && whitePiece(board8)) return 1;
		if(whitePiece(board0) && whitePiece(board4) && whitePiece(board8)) return 1;
		if(whitePiece(board2) && whitePiece(board4) && whitePiece(board6)) return 1;
		if(blackPiece(board0) && blackPiece(board1) && blackPiece(board2)) return 0;
		if(blackPiece(board3) && blackPiece(board4) && blackPiece(board5)) return 0;
		if(blackPiece(board6) && blackPiece(board7) && blackPiece(board8)) return 0;
		if(blackPiece(board0) && blackPiece(board3) && blackPiece(board6)) return 0;
		if(blackPiece(board1) && blackPiece(board4) && blackPiece(board7)) return 0;
		if(blackPiece(board2) && blackPiece(board5) && blackPiece(board8)) return 0;
		if(blackPiece(board0) && blackPiece(board4) && blackPiece(board8)) return 0;
		if(blackPiece(board2) && blackPiece(board4) && blackPiece(board6)) return 0;
		return -1;
	}

	public String getBoard(){
		return board.toString();
	}

	public int getPlayerTurn(){
		if (whiteTurn) return 1;
		return 0;
	}

	public void wipeBoard(){
		whiteTurn = true;
		board.replace(0, 9, ".........");
	}

	private boolean validPosition(int position){
		if (position >= 0 && position <=8) return true;
		return false;
	}

	private boolean validOrientation(int orientation){
		if (orientation >= 0 && orientation <=1) return true;
		return false;
	}

	private boolean validDistance(int distance){
		if (distance >= 0 && distance <=2) return true;
		return false;
	}

	private boolean validDirection(int direction){
		if (direction >= 0 && direction <=8) return true;
		return false;
	}

	private boolean whitePiece(char piece){
		if (piece == 'C' || piece == 'c'){
			return true;
		}
		return false;
	}

	private boolean blackPiece(char piece){
		if (piece == 'E' || piece == 'e'){
			return true;
		}
		return false;
	}

	private boolean positionEmpty(int position){
		if (board.charAt(position) == '.') return true;
		return false;
	}

	private void nextPlayer(){
		whiteTurn = !whiteTurn;
	}

	/**
	 * @brief Find the orientation of a piece
	 * 
	 * Finds, regardless of color, which orientation a certain piece on a position provided by argument
	 * is. Returns 0 for perpendicular and 1 for diagonal.
	 *
	 */

	private int findOrientation(int initialPosition){
		char piece = board.charAt(initialPosition);
		if (piece == 'C' || piece == 'E'){
			return 0;
		} else if (piece == 'c' || piece == 'e'){
			return 1;
		}
		return -1;
	}

	/**
	 * @brief Return the piece which should be placed on the board given paraeters
	 * 
	 * Given an orientation by parameter, this method discerns what kind of piece should be
	 * placed on the board and returns it so that it may be used by setPiece and movePiece.
	 *
	 */

	private char getPiece(int orientation){
		char piece;
		boolean upperCase;

		if (orientation == 0){
			upperCase = true;
		} else{
			upperCase = false;
		}

		if (whiteTurn){
			if(upperCase){
				piece = 'C';
			} else{
				piece = 'c';
			}
		} else{
			if(upperCase){
				piece = 'E';
			} else{
				piece = 'e';
			}
		}
		return piece;
	}

	/**
	 * @brief Discern if a piece belongs to current player
	 * 
	 * Given an orientation by parameter, this method discerns what kind of piece should be
	 * placed on the board and returns it so that it may be used by setPiece and movePiece.
	 *
	 */

	private boolean currentPlayerPiece(int initialPosition){
		char piece = board.charAt(initialPosition);
		if (whiteTurn){
			if (whitePiece(piece)){
				return true;
			} else {
				return false;
			}
		} else {
			if (blackPiece(piece)){
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @brief Find position to which piece will move
	 * 
	 * From the position provided by initialPosition, the piece will be moved in the direction
     * provided by moveDirection a distance specified by moveDistance. The direction is 
     * validated by the orientation provided by orientation. If the combined arguments result
     * in a valid position on the board, that position is returned, else -1 (error) is returned.
	 *
	 */

	private int findNewPosition(int initialPosition, int orientation, int newOrientation, int moveDirection, int moveDistance){
		switch (initialPosition) {
			case 0: 
				switch (moveDirection){
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(1)) return -1;
								return 1;
							case 2:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(3)) return -1;
								return 3;
							case 2:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 8:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 1:
				switch (moveDirection){
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 6:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(3)) return -1;
								return 3;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(7)) return -1;
								return 7;
							default:
								return -1;
						}
					case 8:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(5)) return -1;
								return 5;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 2:
				switch (moveDirection){
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(1)) return -1;
								return 1;
							case 2:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 6:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(5)) return -1;
								return 5;
							case 2:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 3:
				switch (moveDirection){
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 2:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(1)) return -1;
								return 1;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(5)) return -1;
								return 5;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 8:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(7)) return -1;
								return 7;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 4:
				switch (moveDirection){
					case 0:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(1)) return -1;
								return 1;
							default:
								return -1;
						}
					case 2:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(3)) return -1;
								return 3;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(5)) return -1;
								return 5;
							default:
								return -1;
						}
					case 6:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(7)) return -1;
								return 7;
							default:
								return -1;
						}
					case 8:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 5:
				switch (moveDirection){
					case 0:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(1)) return -1;
								return 1;
							default:
								return -1;
						}
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(3)) return -1;
								return 3;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 6:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(7)) return -1;
								return 7;
							default:
								return -1;
						}
					case 7:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 6:
				switch (moveDirection){
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(3)) return -1;
								return 3;
							case 2:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 2:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(7)) return -1;
								return 7;
							case 2:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 7:
				switch (moveDirection){
					case 0:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(3)) return -1;
								return 3;
							default:
								return -1;
						}
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(2)) return -1;
								return 2;
							default:
								return -1;
						}
					case 2:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(5)) return -1;
								return 5;
							default:
								return -1;
						}
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					case 5:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(8)) return -1;
								return 8;
							default:
								return -1;
						}
					default:
						return -1;
				}
			case 8:
				switch (moveDirection){
					case 0:
						if (orientation == 0) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(4)) return -1;
								return 4;
							case 2:
								if(!positionEmpty(0)) return -1;
								return 0;
							default:
								return -1;
						}
					case 1:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(5)) return -1;
								return 5;
							case 2:
								if(!positionEmpty(3)) return -1;
								return 3;
							default:
								return -1;
						}
					case 3:
						if (orientation == 1) return -1;
						switch(moveDistance){
							case 1:
								if(!positionEmpty(7)) return -1;
								return 7;
							case 2:
								if(!positionEmpty(6)) return -1;
								return 6;
							default:
								return -1;
						}
					case 4:
						if (moveDistance > 0 || orientation == newOrientation) return -1;
						return initialPosition;
					default:
						return -1;
				}
			default:
				return -1;
		}
	}
}
