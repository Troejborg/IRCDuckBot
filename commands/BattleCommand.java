package commands;

import game.BattleGame;
import game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 20-10-13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class BattleCommand implements Command {
  private static int PLAYER_NAME_INDEX = 0;
  private static int HOST_NAME_INDEX = 1;
  private static String BATTLE_CHAN = "#ithivemind-game";
  private static final String BATTLE_XP_LEVEL = "nextlvl";
  private static String BATTLE_START = "start";
  private static String BATTLE_CREATE = "create";
  private static String BATTLE_STATS = "stats";
  private static String BATTLE_NEXT_ROUND = "next";
  String targetPlayer, action;
  String sender;
  Player playerOne, playerTwo;
  BattleGame game;
  Response response, additionalResponse;
  List<String> gameMessages;

  public BattleCommand(String sender, String[] cmdStrings, BattleGame game) {
    this.game = game;
    this.sender = sender;
    action = cmdStrings[1];
    targetPlayer = cmdStrings.length > 2 ? cmdStrings[2] : null;
    response = new Response();
    additionalResponse = new Response();
  }

  @Override
  public Response interpretCommand() {
    response.Messages.clear();
    if(action.equals(BATTLE_CREATE)){
      if(game.getPlayerList().get(sender) == null){
        game.getPlayerList().put(sender, new Player(sender));
        response.Messages.add(sender + " has been added to the player database");
      }else
        response.Messages.add(sender + " already exists in my database");
    }
    else if(action.equals(BATTLE_START)){
      if(initGame(sender, targetPlayer)){
        response.Channel = BATTLE_CHAN;
        startGame();
        while(!game.evalIsGameOver()){
          playNextRound();
          response.Messages.add("END OF ROUND " + game.RoundNumber + "!");
        };
        determineWinner();
      }
    }
    else if(action.equals(BATTLE_XP_LEVEL)){
      Player player = game.getPlayerList().get(sender);
      int nextLvl = player.Level +1;
      String res =  player != null ? player.PlayerName + " needs another " + player.getExpForNextLevel() + "xp to reach level " + nextLvl : sender + " not found in database";
      response.Messages.add(res);
    }
    else if(action.equals(BATTLE_STATS)){
      Player player = game.getPlayerList().get(targetPlayer);
      if(player != null)
        response.Messages.add(player.PlayerName + " is level " + player.Level + ", has " + player.Wins + " wins and " + player.Losses + " losses.");
      else
        response.Messages.add("Could not find player in database");
    }
    return response;
  }

  @Override
  public Response getAdditionalMessages() {
    return additionalResponse;
  }

  private void playNextRound() {
    game.startNextRound();
    response.Messages.add(game.getAttackingPlayer().PlayerName + " charges forward! He launches towards his target and...");
    response.Messages.add("...does " + game.getLastResultingDamage() + " damage to " + game.getDefendingPlayer().PlayerName);
    response.Messages.add(game.getDefendingPlayer().PlayerName + " has " + game.getDefendingPlayer().getCurrentHealth() + " health remaining.");
    game.switchTurns();
  }

  private boolean initGame(String playerOneName, String playerTwoName) {
    playerOne = game.getPlayerList().get(playerOneName);
    playerTwo = game.getPlayerList().get(playerTwoName);
    if(playerOne == null || playerTwo == null ){
      response.Messages.add("No player named " + (playerOne == null ? playerOneName : playerTwoName) + " found in database. To create yourself type '!battle create'");
      return false;
    }else{
      playerOne.revitalize();
      playerTwo.revitalize();
      game.rollStartingPositions(playerOne, playerTwo);
      return true;
    }
  }
  private void startGame() {
    response.Messages.clear();
    if(game.StartMatch()){
      response.Messages.add("!!!MATCH STARTED!!!");
      response.Messages.add("!!!MATCH STARTED!!! Follow it over at #ithivemind-game");
      response.Messages.add("Rolling the dice to see who gets to strike first...");
      response.Messages.add("Aaaaand " + game.getAttackingPlayer().PlayerName + " won the dice roll!");
      response.Messages.add(game.getDefendingPlayer().PlayerName + " is getting ready to defend...");
    }
  }

  private void determineWinner() {
    boolean levelUpWinner = false, levelUpLoser = false;
    game.setMatchExp();
    levelUpWinner = game.grantWinnngExp();
    levelUpLoser = game.grantLosingExp();

    game.save();

    response.Messages.addAll(announcePostGame(levelUpWinner, levelUpLoser));
    additionalResponse.Messages.addAll(announcePostGame(levelUpWinner, levelUpLoser));
  }

  private List<String> announcePostGame(boolean levelUpWinner, boolean levelUpLoser) {
    List<String> result = new ArrayList<String>();
    result.add(game.getLoser().PlayerName + " has suffered a gruesome death :( Rest in Pieces.");
    result.add("CONGRATUALATIONS " + game.getWinner().PlayerName + "! Youve won this game!");

    result.add("!!!POST GAME!!!");
    result.add(game.getWinner().PlayerName + " gained " + game.getWinningExp() + "xp and " + game.getLoser().PlayerName + " gained " + game.getLosingExp() + "xp for fighting in this match");
    if(levelUpWinner)
      result.add("What's this? " + game.getWinner().PlayerName + " is evolving! He is now level " + game.getWinner().Level + "!");
    if(levelUpLoser)
      result.add("What's this? " + game.getLoser().PlayerName + " is evolving! He is now level " + game.getLoser().Level + "!");

    return result;
  }
}
