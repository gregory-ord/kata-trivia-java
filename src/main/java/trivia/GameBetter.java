package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

// REFACTOR ME
public class GameBetter implements IGame {
   ArrayList players = new ArrayList();

   //Should there be 12 places instead of 6?
   int[] places = new int[6];

   //Does 6 purses make sense? -> Probably is okay for when we change the max number of players to 6.
   //We could probably put purses and inPenaltyBox into a Player class. Also store the position of the player in the Player class.
   int[] purses = new int[6];
   boolean[] inPenaltyBox = new boolean[6];


   //Why a LinkedList instead of ArrayList?
   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   //Maybe these two variables could be in a GameState class?
   //Does isGettingOutOfPenaltyBox even need to exist?
   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public GameBetter() {
      for (int i = 0; i < 50; i++) {
         //Why use a method for rock questions but not the others?
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   //This method is not used.
   //Seems like it might be related to how many players you enter as in input at the start.
   public boolean isPlayable() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      //Can definitely put all of these things into a player class?
      players.add(playerName);
      places[howManyPlayers()] = 0;
      purses[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      System.out.println(players.get(currentPlayer) + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (inPenaltyBox[currentPlayer]) {
         if (roll % 2 != 0) { //Need to roll odd number to get out of the penalty box.
            isGettingOutOfPenaltyBox = true; //This might fit in the player class as well.

            System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            System.out.println(players.get(currentPlayer)
                               + "'s new location is "
                               + places[currentPlayer]);
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         //Duplicate code of above
         places[currentPlayer] = places[currentPlayer] + roll;
         if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

         System.out.println(players.get(currentPlayer)
                            + "'s new location is "
                            + places[currentPlayer]);
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory() == "Pop")
         System.out.println(popQuestions.removeFirst());
      if (currentCategory() == "Science")
         System.out.println(scienceQuestions.removeFirst());
      if (currentCategory() == "Sports")
         System.out.println(sportsQuestions.removeFirst());
      if (currentCategory() == "Rock")
         System.out.println(rockQuestions.removeFirst());
   }


   private String currentCategory() {
      //Maybe this could be a static map.
      //Place could be a class that stores an integer position and a Category
      //Maybe a Category class that has a String name and a List<String> questions. A Stack would actually be better than a list.
      if (places[currentPlayer] == 0) return "Pop";
      if (places[currentPlayer] == 4) return "Pop";
      if (places[currentPlayer] == 8) return "Pop";
      if (places[currentPlayer] == 1) return "Science";
      if (places[currentPlayer] == 5) return "Science";
      if (places[currentPlayer] == 9) return "Science";
      if (places[currentPlayer] == 2) return "Sports";
      if (places[currentPlayer] == 6) return "Sports";
      if (places[currentPlayer] == 10) return "Sports";
      return "Rock";
   }

   //Move a player and check if they won the game.
   //The boolean that is returned indicates if the current player has won the game.
   public boolean wasCorrectlyAnswered() {
      if (inPenaltyBox[currentPlayer]) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            purses[currentPlayer]++;
            System.out.println(players.get(currentPlayer)
                               + " now has "
                               + purses[currentPlayer]
                               + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;
            return true; //Probably don't mean to always return true.
         }


      } else {

         //This is duplicate of the code above.
         System.out.println("Answer was corrent!!!!");
         purses[currentPlayer]++;
         System.out.println(players.get(currentPlayer)
                            + " now has "
                            + purses[currentPlayer]
                            + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   //The return value of this method means nothing as it is right now.
   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
      inPenaltyBox[currentPlayer] = true;

      //These lines to increment the currentPlayer are duplicates of other code in this file.
      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }

   //This says that you win if you don't have six coins. That doesn't seem right.
   //Think we can just get rid of the '!' in this logic.
   //Which would mean the current player wins if they get to 6 coins.
   private boolean didPlayerWin() {
      return !(purses[currentPlayer] == 6);
   }
}
