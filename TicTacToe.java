import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a very
 * simple GUI window.
 * 
 * @author Sashank Bandemegala
 * @version June 10, 2017
 */
public class TicTacToe implements ActionListener
{
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String EMPTY = "";  // empty cell
 
   private String player;   // current player (PLAYER_X or PLAYER_O)
   private String winner;   // winner: PLAYER_X, PLAYER_O, EMPTY = in progress
   
   private JMenuItem newItem;     
   private JMenuItem quitItem;      
   private JButton[][] buttons; // buttons for the 3 x 3 grid
   private JLabel label;    
   private int xScore, oScore, tie;
   
   int i, j;    
   
   /** 
    * Constructs a new Tic-Tac-Toe board.
    */
   public TicTacToe()
   {
      winner = EMPTY;
      player = PLAYER_X;     
      xScore = 0;
      oScore = 0;
      tie = 0;
      
      JFrame frame = new JFrame("Tic-Tac-Toe");
      frame.setPreferredSize(new Dimension(750, 750));
      
      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(3, 3));
      contentPane.add(buttonPanel, BorderLayout.CENTER);
      
      buttons = new JButton[3][3];
      for(i = 0; i < 3; i++)
      {
          for(j = 0; j < 3; j++)
          {
              buttons[i][j] = new JButton();
              buttons[i][j].setText("");
              buttonPanel.add(buttons[i][j]);
              buttons[i][j].addActionListener(this);
          }
      }
      
      label = new JLabel(" Its X's Turn   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
      contentPane.add(label, BorderLayout.SOUTH);
      
      
      JMenuBar menuBar = new JMenuBar();
      frame.setJMenuBar(menuBar); 
      JMenu gameMenu = new JMenu("Game");
      menuBar.add(gameMenu);
      
      newItem = new JMenuItem("New");
      gameMenu.add(newItem);      
      quitItem = new JMenuItem("Quit");
      gameMenu.add(quitItem);
      
      final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
      newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));//new game
      quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));//quit game

      
      newItem.addActionListener(this); 
      quitItem.addActionListener(new ActionListener()   
        {  
             //anonymous subclass
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0); 
            }
        }   
      );    
      
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
   }

   /**
    * Returns the player if we have a winner, EMPTY otherwise
    *
    * @param int row on the board
    * @param int col on the board
    * 
    * @return Player if theres a, Empty otherwise
    */
   private String haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).
       if (freeButtons() > 4) 
            return EMPTY;
            
       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       // check row "row"
       if ( buttons[row][0].getText().equals(buttons[row][1].getText()) &&
            buttons[row][0].getText().equals(buttons[row][2].getText()) ) return player;
       
       // check column "col"
       if ( buttons[0][col].getText().equals(buttons[1][col].getText()) &&
            buttons[0][col].getText().equals(buttons[2][col].getText()) ) return player;

       // if row=col check one diagonal
       if (row == col)
          if ( buttons[0][0].getText().equals(buttons[1][1].getText()) &&
               buttons[0][0].getText().equals(buttons[2][2].getText()) ) return player;

       // if row=2-col check other diagonal
       if (row == 2-col)
          if ( buttons[0][2].getText().equals(buttons[1][1].getText()) &&
               buttons[0][2].getText().equals(buttons[2][0].getText()) ) return player;

       // no winner yet
       return EMPTY;
   }
 
   /** 
   * Action Listener for when button is clicked in GUI
   */
   public void actionPerformed(ActionEvent e)
   {
      Object o = e.getSource();     
      
      if(winner.equals(EMPTY) && o instanceof JButton)
      {
          JButton button = (JButton)o;
          
          for(i = 0; i < 3; i++)
          {
              for(j = 0; j < 3; j++)
              {
                  if(button == buttons[i][j]) 
                  {
                      if(player.equals(PLAYER_X)){
                         
                         buttons[i][j].setText("X");
                         buttons[i][j].setFont(new Font("Serif", Font.BOLD, 200));
                        }
                      else{
                         buttons[i][j].setText("O");
                         buttons[i][j].setFont(new Font("Serif", Font.BOLD, 200));
                        }
                      buttons[i][j].setEnabled(false);
                      winner = haveWinner(i, j);
                  }
              }
          }    
          
          if(winner.equals(PLAYER_X)) 
          {
              disableAll();
              xScore = xScore + 1;
              label.setText(" Game Over! X Wins!   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
          }else if(winner.equals(PLAYER_O)) 
          {
              disableAll();
              oScore = oScore + 1;
              label.setText(" Game Over! X Wins!   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
          }
          else 
          {
              if(freeButtons() == 0) 
              {
              disableAll();
              tie = tie + 1;
              label.setText(" Game Over! X Wins!   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
              }else
              {
                  if (player==PLAYER_X)
                  { 
                     player = PLAYER_O;
                     label.setText(" Game Is In Progress! O's Turn!   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
                  }
                  else 
                  {
                     player = PLAYER_X;
                     label.setText(" Game Is In Progress! X's Turn!   " + " -   X's score: " + xScore + " -   O's score: " + oScore + " -   Ties: " + tie);
                  }
              }
          }
      }else 
      {   
           JMenuItem item = (JMenuItem)o;
           if (item == newItem)   
               newGame();
           else if (item == quitItem)   
               System.exit(0);
      }
   }    
   
   /**
    * Disables all the buttons when the game ends
    */
   private void disableAll()
   {
       for(i = 0; i < 3; i++)
       {
           for(j = 0; j < 3; j++)
           {
               buttons[i][j].setEnabled(false);
           }
       }
   }
   
   /**
    * Resets the board to make a new board when game is over
    */
   private void newGame()
   {
      for(i=0; i<3; i++) 
      {
          for(j=0; j<3; j++)
          {
              buttons[i][j].setEnabled(true);
              buttons[i][j].setText("");  
          }   
      }     
      winner = EMPTY;
      player = PLAYER_X;    
   }
   
   /**
    * returns the number of free buttons in the board
    * 
    * @return the free buttons on the board
    */
   private int freeButtons()
   {
       int freeCounter = 0;
       
       for(i = 0; i < 3; i++)
       {
           for(j = 0; j < 3; j++)
           {
               if(buttons[i][j].getText().equals(EMPTY))
                   freeCounter = freeCounter + 1;
           }
       }
       return freeCounter;
   }
}