package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// REFACTOR ME
public class GameBetter implements IGame {
   List<Player> players = new ArrayList();

   //Why a LinkedList instead of ArrayList?
   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   Player currentPlayer;

   public GameBetter() {
      for (int i = 0; i < 50; i++) {
         //Why use a method for rock questions but not the others?
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   private String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   @Override
   public boolean add(String playerName) {
      Player newPlayer = new Player(playerName, players.size() + 1);
      if (currentPlayer == null) {
         currentPlayer = newPlayer;
      }

      System.out.println(newPlayer.name + " was added");
      System.out.println("They are player number " + newPlayer.number);

      return players.add(newPlayer);
   }

   @Override
   public void roll(int roll) {
      System.out.println(currentPlayer.name + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer.inPenaltyBox) {
         if (roll % 2 != 0) {
            currentPlayer.inPenaltyBox = false;

            System.out.println(currentPlayer.name + " is getting out of the penalty box");
            currentPlayer.move(roll);

            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(currentPlayer.name + " is not getting out of the penalty box");
         }

      } else {

         currentPlayer.move(roll);

         System.out.println("The category is " + currentCategory());
         askQuestion();
      }
   }

   @Override
   public boolean handleCorrectAnswer() {
      if (currentPlayer.inPenaltyBox) {
         moveToNextPlayer();
         return true;
      } else {
         return rewardAndMoveToNextPlayer();
      }
   }

   @Override
   public boolean handleWrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer.name + " was sent to the penalty box");
      currentPlayer.inPenaltyBox = true;

      moveToNextPlayer();
      return true;
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
      if (currentPlayer.positionOnBoard == 0) return "Pop";
      if (currentPlayer.positionOnBoard == 4) return "Pop";
      if (currentPlayer.positionOnBoard == 8) return "Pop";
      if (currentPlayer.positionOnBoard== 1) return "Science";
      if (currentPlayer.positionOnBoard == 5) return "Science";
      if (currentPlayer.positionOnBoard == 9) return "Science";
      if (currentPlayer.positionOnBoard == 2) return "Sports";
      if (currentPlayer.positionOnBoard == 6) return "Sports";
      if (currentPlayer.positionOnBoard == 10) return "Sports";
      return "Rock";
   }

   private boolean rewardAndMoveToNextPlayer() {
      rewardCurrentPlayerCoin();

      boolean notAWinner = currentPlayer.numCoins != 6;
      moveToNextPlayer();

      return notAWinner;
   }

   private void rewardCurrentPlayerCoin() {
      currentPlayer.numCoins++;
      System.out.println("Answer was correct!!!!");
      System.out.println(currentPlayer.name
                         + " now has "
                         + currentPlayer.numCoins
                         + " Gold Coins.");
   }

   private void moveToNextPlayer() {
      if (currentPlayer.number == players.size()) {
         currentPlayer = players.get(0);
      } else {
         currentPlayer = players.get(currentPlayer.number);
      }
   }
}
