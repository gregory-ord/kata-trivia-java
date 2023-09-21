package trivia;

import java.util.*;

// REFACTOR ME
public class GameBetter implements IGame {
   List<Player> players = new ArrayList();

   List<BoardPosition> boardPositions = new ArrayList<>();

   List<Category> categories = new ArrayList<>();

   Player currentPlayer;


   public GameBetter() {
      Category popCategory = new Category( Category.CategoryName.POP );
      Category scienceCategory = new Category( Category.CategoryName.SCIENCE );
      Category sportsCategory = new Category( Category.CategoryName.SPORTS );
      Category rockCategory = new Category( Category.CategoryName.ROCK );

      for (int i = 49; i >= 0; i--) {
         popCategory.addQuestion(i);
         scienceCategory.addQuestion(i);
         sportsCategory.addQuestion(i);
         rockCategory.addQuestion(i);
      }

      for(int i = 0; i < 12; i+=4){
         boardPositions.add( new BoardPosition( i, popCategory.name ) );
         boardPositions.add( new BoardPosition( i+1, scienceCategory.name ) );
         boardPositions.add( new BoardPosition( i+2, sportsCategory.name ) );
         boardPositions.add( new BoardPosition( i+3, rockCategory.name ) );
      }

      categories.add(popCategory);
      categories.add(scienceCategory);
      categories.add(sportsCategory);
      categories.add(rockCategory);
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
         if ( rollIsOdd(roll)) {
            currentPlayer.inPenaltyBox = false;
            System.out.println(currentPlayer.name + " is getting out of the penalty box");
            moveAndAskQuestion( roll );
         } else {
            System.out.println(currentPlayer.name + " is not getting out of the penalty box");
         }

      } else {
         moveAndAskQuestion( roll );
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

   private boolean rollIsOdd(int roll){
      return roll % 2 != 0;
   }

   private void moveAndAskQuestion( int roll ) {
      currentPlayer.move( roll );

      System.out.println("The category is " + currentCategory().name.desc);
      askQuestion();
   }

   private void askQuestion() {
      Category.CategoryName categoryName = currentCategory().name;
      Optional<Category> categoryOpt = categories.stream().filter( c -> c.name.equals( categoryName ) ).findFirst();

      if(categoryOpt.isEmpty()){
         throw new IllegalArgumentException("Invalid category name");
      }

      System.out.println(categoryOpt.get().getNextQuestion());
   }


   private Category currentCategory() {
      BoardPosition currentPlayerBoardPosition = boardPositions.get( currentPlayer.positionOnBoard );
      Category.CategoryName categoryName = currentPlayerBoardPosition.categoryName;
      Optional<Category> categoryOpt = categories.stream().filter( c -> c.name.equals( categoryName ) ).findFirst();

      if(categoryOpt.isEmpty()){
         throw new IllegalArgumentException("Invalid category name");
      }

      return categoryOpt.get();
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
