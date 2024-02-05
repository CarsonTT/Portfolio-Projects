"""
A module that sets up a client socket to play Tic-Tac-Toe while also providing a UI.

This module contains functions related to the Tic-Tac-Toe game client, including
functions for obtaining host information, updating player moves, and checking win conditions
which it sends/receives to/from the server side through a socket. Uses Tkinter for the UI.
"""

import tkinter as tk
from tkinter import messagebox
import socket
import threading
from gameboard import board_class, spot_taken

class TicTacToeClient:
    """
    Class representing the Tic Tac Toe client GUI application.

    Attributes:
        client_socket (socket.socket): The client socket used for communication.
        board (board_class): An instance of the board_class representing the game board.
        root (tk.Tk): The main Tkinter window.
        hostname_label (tk.Label): Label prompting the user to input the server's hostname/IP address.
        hostname_entry (tk.Entry): Entry widget for the user to input the server's hostname/IP address.
        port_label (tk.Label): Label prompting the user to input the server's port.
        port_entry (tk.Entry): Entry widget for the user to input the server's port.
        username_prompt (tk.Label): Label prompting the user to enter their username.
        username_entry (tk.Entry): Entry widget for the user to enter their username.
        submit_connection_button (tk.Button): Button to submit the connection details.
        connection_error_response (bool): Flag indicating the user's response to a connection error prompt.
        player_turn_label (tk.Label): Label displaying the current player's turn.
        buttons (List[List[tk.Button]]): 2D list representing the game board buttons.
        play_again_popup (bool): Flag indicating the user's response to a play again prompt.
        game_stats_label (tk.Label): Label displaying "Game Stats."
        username_label (tk.Label): Label displaying the player's username.
        last_user_label (tk.Label): Label displaying the last player who made a move.
        games_played_label (tk.Label): Label displaying the total number of games played.
        wins_label (tk.Label): Label displaying the total number of games won.
        losses_label (tk.Label): Label displaying the total number of games lost.
        ties_label (tk.Label): Label displaying the total number of tied games.
    """
    
    def __init__(self):
        """
        Initializes an instance of the TicTacToeClient class.
        """
        self.client_socket = None
        self.board = board_class()
        self.canvas_setup()
        self.create_hostname_entry()
        self.create_port_entry()
        self.create_username_entry()
        self.create_submit_connection_button()
        self.create_player_turn_label()
        self.create_board_buttons()
        self.create_stats_labels()

    def canvas_setup(self) -> None:
        """
        Set up the main Tkinter window.
        """
        self.root = tk.Tk()
        self.root.title("Tic Tac Toe - Client")
        self.root.configure(background='grey')

    def create_hostname_entry(self) -> None:
        """
        Create and grid the hostname-related labels and entry widget.
        """
        self.hostname_label = tk.Label(self.root, text="Please input Server's Hostname/IP Address:")
        self.hostname_label.grid()
        self.hostname_entry = tk.Entry(self.root)
        self.hostname_entry.grid()

    def create_port_entry(self) -> None:
        """
        Create and grid the port-related labels and entry widget.
        """
        self.port_label = tk.Label(self.root, text="Please input Server's Port:")
        self.port_label.grid()
        self.port_entry = tk.Entry(self.root)
        self.port_entry.grid()

    def create_username_entry(self) -> None:
        """
        Create and grid the username-related labels and entry widget.
        """
        self.username_prompt = tk.Label(self.root, text="Please enter your username:")
        self.username_prompt.grid()
        self.username_entry = tk.Entry(self.root)
        self.username_entry.grid()

    def create_submit_connection_button(self) -> None:
        """
        Create and grid the button to submit connection details.
        """
        self.submit_connection_button = tk.Button(self.root, text='Submit', command=self.create_client)
        self.submit_connection_button.grid()

    def create_client(self) -> None:
        """
        Attempt to create a connection to the server with the provided details.
        """
        try:
            host = self.hostname_entry.get()
            port = int(self.port_entry.get())

            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.client_socket.connect((host, port))
            self.update_player_name()
            self.switch_board_page()

            receive_thread = threading.Thread(target=self.receive_data)
            receive_thread.start()

        except(ConnectionRefusedError, ConnectionError, tk.TclError, OverflowError, socket.gaierror, ValueError):
            self.create_connection_retry_button()
            if self.connection_error_response == True:
                pass
            elif self.connection_error_response == False:
                self.root.destroy()

    def create_connection_retry_button(self) -> None:
        """
        Display a connection error prompt when needed.
        """
        self.connection_error_response = tk.messagebox.askyesno(title="Connection Error",
                                                                message='Connection failed, Would you like to try again?')

    def update_player_name(self) -> None:
        """
        Update the player's name in the game board.
        """
        self.board.player_names.append(self.username_entry.get())
        encoded_name =f'username,{self.board.player_names[0]}'
        self.send_data(encoded_name)

    def switch_board_page(self) -> None:
        """
        Switch the GUI to the game board page, enabling game-related widgets.
        """
        self.hostname_label.destroy()
        self.hostname_entry.destroy()
        self.port_label.destroy()
        self.port_entry.destroy()
        self.username_prompt.destroy()
        self.username_entry.destroy()
        self.submit_connection_button.destroy()


        self.player_turn_label.grid(row = 0, column = 1)
        self.enable_buttons()  # Enable buttons when it's the client-side player's turn
        self.show_gameboard_buttons()

    def show_gameboard_buttons(self) -> None:
        """
        Grid the game board buttons on the GUI.
        """
        for i in range(3):
            for j in range(3):
                self.buttons[i][j].grid(row=i + 1, column=j)

    def create_player_turn_label(self) -> None:
        """
        Create and grid the label indicating the current player's turn.
        """
        self.player_turn_label = tk.Label(self.root, text="",
                                          font=("Helvetica", 14), width=20, height=3)

    def create_board_buttons(self) -> None:
        """
        Create the game board buttons on the GUI.
        """
        self.buttons = [[None] * 3 for _ in range(3)]
        for i in range(3):
            for j in range(3):
                self.buttons[i][j] = tk.Button(
                    text="", font=("Helvetica", 35), width=8, height=4,
                    command=lambda row=i, col=j: self.make_p1_move(row, col),
                    state='disabled'  # Set buttons initially disabled
                )

    def make_p1_move(self, row: int, col: int) -> None:
        """
        Handle the user's move, update the game board, and check for win conditions.
        """
        try:
            self.disable_buttons()
            self.board.update_game_board(row, col, "X")
            self.buttons[row][col].config(text='X', state='disable')
            self.board.last_player = self.board.player_names[0]
            self.player_turn_label.config(text=f"{self.board.player_names[1]}'s Turn.")
            self.board.turn_count += 1
            move = f'{row},{col}'
            self.send_data(move)
            self.check_all_win_con()

        except spot_taken:
            messagebox.showwarning("Invalid Move", "This spot is already taken. Choose another.")

    def check_x_win_con(self) -> bool:
        """
        Check if Player 1 (X) has won the game.

        Returns:
            bool: True if Player 1 has won, False otherwise.
        """
        if self.board.is_winner('X'):
            self.board.wins += 1
            self.board.update_games_played()
            tk.messagebox.showinfo(title="Info Box", message='You Won!')
            self.create_play_again_popup()
            return True
        else:
            return False

    def check_o_win_con(self) -> bool:
        """
        Check if Player 2 has won and handle accordingly.
        """
        if self.board.is_winner('O'):
            self.board.losses += 1
            self.board.update_games_played()
            tk.messagebox.showinfo(title="Info Box", message='You Lost!')
            self.create_play_again_popup()
            return True
        else:
            return False

    def check_tie_con(self) -> bool:
        """
        Check if Player 2 (O) has won the game.

        Returns:
            bool: True if Player 2 has won, False otherwise.
        """
        if self.board.board_is_full():
            self.board.update_games_played()
            tk.messagebox.showinfo(title="Info Box", message='You Tied!')
            self.create_play_again_popup()
            return True
        else:
            return False

    def check_all_win_con(self) -> bool:
        """
        Check if the game is a tie.

        Returns:
            bool: True if the game is a tie, False otherwise.
        """
        return self.check_x_win_con() or self.check_o_win_con() or self.check_tie_con()

    def create_play_again_popup(self) -> bool:
        """
        Check if any win or tie condition is met.

        Returns:
            bool: True if any win or tie condition is met, False otherwise.
        """
        self.play_again_popup = tk.messagebox.askyesno(title="Rematch Prompt.", message='Do you want to play again?')
        if self.play_again_popup == True:
            self.board.reset_game_board()
            self.board.last_player = None
            self.player_turn_label.config(text=f"{self.board.player_names[0]}'s Turn.")
            self.board.turn_count = 0
            self.reset_board()
            self.send_data("yes")
        else:
           self.switch_to_stats_page()
           self.send_data('no')
           self.switch_to_stats_page()
               
    def reset_board(self) -> None:
        """
        Reset the game board buttons for a new game.
        """
        for i in range(3):
            for j in range(3):
                self.buttons[i][j].config(text="", state="active")

    def send_data(self, data: str) -> None:
        """
        Send data to the connected client.

        Args:
            data (str): The data to be sent.
        """
        self.client_socket.send(data.encode())

    def receive_data(self) -> None:
        """
        Receive and process data from the server.
        """
        while True:
            try:
                data = self.client_socket.recv(1024)
                if not data:
                    break
                data = data.decode()
                if data.startswith('username'):
                    self.update_p2_name(data)
                else:
                    self.update_p2_move(data)

            except ConnectionResetError:
                break

    def update_p2_name(self, data: str) -> None:
        """
        Update Player 2's name on the game board.

        Args:
            data (str): Data containing the new username for Player 2.
        """
        p2_name = data.split(',')[1]
        self.board.player_names.append(p2_name)
        self.player_turn_label.config(text=f"{self.board.player_names[0]}'s Turn.")

    def update_p2_move(self, data: str) -> None:
        """
        Update Player 2's move on the game board.

        Args:
            data (str): Data containing the move made by Player 2.
        """
        try:
            row = int(data.split(',')[0])
            col = int(data.split(',')[1])

            self.buttons[row][col].config(text="O", state="disabled")
            self.board.update_game_board(row, col, "O")
            self.board.last_player = self.board.player_names[1]
            self.player_turn_label.config(text=f"{self.board.player_names[0]}'s Turn.")
            self.board.turn_count += 1
            self.enable_buttons()
            self.check_all_win_con()

        except spot_taken:
            messagebox.showwarning("Invalid Move", "This spot is already taken. Choose another.")

    def disable_buttons(self) -> None:
        """
        Disable the game board buttons.
        """
        for i in range(3):
            for j in range(3):
                self.buttons[i][j].config(state='disabled')

    def enable_buttons(self) -> None:
        """
        Enable the game board buttons for the client's turn.
        """
        for i in range(3):
            for j in range(3):
                move = f'{self.board.board[i][j]}'
                if move == '':
                    self.buttons[i][j].config(state='active')

    def create_stats_labels(self) -> None:
        """
        Create labels for displaying game statistics.
        """
        self.game_stats_label = tk.Label(self.root, text="Game Stats")
        self.username_label = tk.Label(self.root, text="t")
        self.last_user_label = tk.Label(self.root, text='t')
        self.games_played_label = tk.Label(self.root, text='t')
        self.wins_label = tk.Label(self.root, text='t')
        self.losses_label = tk.Label(self.root, text='t')
        self.ties_label = tk.Label(self.root, text='t')

    def destroy_board(self) -> None:
        """
        Destroy the game board buttons.
        """
        for i in range(3):
            for j in range(3):
                self.buttons[i][j].destroy()
    
    def switch_to_stats_page(self) -> None:
        """
        Switch the GUI to the game statistics page.
        """
        self.player_turn_label.destroy()
        self.destroy_board()
        self.game_stats_label.grid()
        self.username_label.config(text=f'Player: {self.board.player_names[0]}')
        self.username_label.grid()
        self.last_user_label.config(text=f'Last User: {self.board.last_player}')
        self.last_user_label.grid()
        self.games_played_label.config(text=f"Games Played: {self.board.games_played}")
        self.games_played_label.grid()
        self.wins_label.config(text=f"# of Wins: {self.board.wins}")
        self.wins_label.grid()
        self.losses_label.config(text=f"# of Losses: {self.board.losses}")
        self.losses_label.grid()
        self.ties_label.config(text=f"# of Ties: {self.board.ties}")
        self.ties_label.grid()
    
    def runUI(self) -> None:
        """
        Run the main GUI loop.
        """
        self.root.mainloop()

if __name__ == "__main__":
    game = TicTacToeClient()
    game.runUI()
