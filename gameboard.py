class spot_taken(Exception):
    """
    A simple class exception that catches when a player picks a spot that has been already been taken.
    """

class board_class:
    """
    A class that initialize the board with the given player's name and initialize game statistics.

    Attributes:
        player_names (List[str]): A list containing the player's username.
        turn_count (int): An integer representing the number of turns played.
        last_player (str): A string representing the username of the last player who made a move.
        games_played (int): An integer representing the total number of games played.
        wins (int): An integer representing the total number of games won.
        losses (int): An integer representing the total number of games lost.
        ties (int): An integer representing the total number of tied games.
        board (List[List[str]]): A 2D list representing the game board, initialized with underscores ('_').
    """
    
    def __init__(self):
        """
        Makes a board_class.
        """
        self.player_names = []
        self.turn_count = 0
        self.last_player = None
        self.games_played = 0
        self.wins = 0
        self.losses = 0
        self.ties = 0
        self.board = [['' for x in range(3)] for x in range(3)]

    def update_games_played(self) -> None:
        self.games_played += 1
        
    def reset_game_board(self) -> None:
        self.board = [['' for x in range(3)] for x in range(3)]
        
    def update_game_board(self, row_value: int, col_value: int, player: str) -> None:
        """
        Update the game board with the player's move.
        Args:
            row_value (int): Row value for the move.
            col_value (int): Column value for the move.
            player (str): The player making the move.
        Raises:
            spot_taken: If the spot on the board is already taken.
        """
        if self.board[row_value][col_value] == '':
            self.board[row_value][col_value] = player.upper()
        else:
            raise spot_taken

    def checkwc1(self, player: str) -> bool:
        """
        Check for a win condition in rows.
        Args:
            player (str): The player to check for.
        Returns:
            bool: True if there is a win condition, False otherwise.
        """
        checked = False
        for row in self.board:
            if row == [player, player, player]:
                checked = True
        return checked

    def checkwc2(self, player: str) -> bool:
        """
        Check for a win condition in columns.
        Args:
            player (str): The player to check for.
        Returns:
            bool: True if there is a win condition, False otherwise.
        """
        checked = False
        for i in range(3):
            check = []
            for row in self.board:
                check.append(row[i])
            if check == [player, player, player]:
                checked = True
        return checked
            
    def checkwc3(self, player: str) -> bool:
        """
        Check for a win condition in diagonals.
        Args:
            player (str): The player to check for.
        Returns:
            bool: True if there is a win condition, False otherwise.
        """
        cross = [self.board[0][0], self.board[1][1], self.board[2][2]]
        cross_2 = [self.board[0][2], self.board[1][1], self.board[2][0]]
        
        if cross == [player, player, player]:
            return True
        elif cross_2 == [player, player, player]:
            return True
        else:
            return False
        
    def is_winner(self, player: str) -> bool:
        """
        Check if the given player has won.
        Args:
            player (str): The player to check for.
        Returns:
            bool: True if the player has won, False otherwise.
        """
        if self.checkwc1(player) or self.checkwc2(player) or self.checkwc3(player):
            return True
        else:
            return False
        
    def board_is_full(self) -> bool:
        """
        Check if the game board is full.
        Returns:
            bool: True if the board is full, False otherwise.
        """
        if self.turn_count == 9:
            self.ties += 1
            return True
        else:
            return False

    def print_stats(self) -> None:
        """
        Print game statistics.
        """
        print('Game Stats\n')
        print('---------------------\n')
        print(f"Player: {self.player_names[0]}\n")
        print(f'Last User: {self.last_player}\n')
        print(f'Games Played: {self.games_played}\n')
        print(f'# of Wins: {self.wins}\n')
        print(f'# of Losses: {self.losses}\n')
        print(f'# of Ties: {self.ties}\n')

    def print_board(self) -> None:
        """
        Print the current state of the game board.
        """
        print(f'{self.board[0][0]} {self.board[0][1]} {self.board[0][2]}\n')
        print(f'{self.board[1][0]} {self.board[1][1]} {self.board[1][2]}\n')
        print(f'{self.board[2][0]} {self.board[2][1]} {self.board[2][2]}\n')

