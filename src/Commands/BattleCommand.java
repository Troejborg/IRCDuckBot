package Commands;

import Commands.Command;
import Game.BattleGame;
import Game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 20-10-13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class BattleCommand implements Command {
  private static String BATTLE_CHAN = "#ithivemind-game";
  private static final String BATTLE_XP_LEVEL = "nextlvl";
  private static String BATTLE_START = "start";
  private static String BATTLE_CREATE = "create";
  private static String BATTLE_STATS = "stats ";
  private static String BATTLE_NEXT_ROUND = "next";
  String sender, targetPlayer, action;
  Map<String, Player> players;
  Player playerOne, playerTwo;
  BattleGame activeGame;
  Response response, additionalResponse;
  List<String> gameMessages;

  public BattleCommand(String sender, String[] cmdStrings, Map<String, Player> players) {
    this.players = players;
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
      if(players.get(sender) == null){
        players.put(sender, new Player(sender));
        response.Messages.add(sender + " has been added to the player database");
      }else
        response.Messages.add(sender + " already exists in my database");
    }
    else if(action.equals(BATTLE_START)){
      if(initGame(sender, targetPlayer)){
        response.Channel = BATTLE_CHAN;
        startGame();
        while(!activeGame.evalIsGameOver()){
          playNextRound();
          response.Messages.add("END OF ROUND " + activeGame.RoundNumber + "!");
        };
        determineWinner();
      }
    }
    else if(action.equals(BATTLE_XP_LEVEL)){
      Player player = players.get(sender);
      String res =  player != null ? sender + " needs another " + player.getExpForNextLevel() + "xp to reach level " + (player.getLevel()+1) : sender + " not found in database";
      response.Messages.add(res);
    }
    else if(action.equals(BATTLE_STATS)){
      Player player = players.get(targetPlayer);
      if(player != null)
        response.Messages.add(player.getPlayerName() + " is level " + player.getLevel() + ", has " + player.getWins() + " wins and " + player.getLosses() + " losses.");
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
    activeGame.startNextRound();
    response.Messages.add(activeGame.getAttackingPlayer().getPlayerName() + " charges forward! He launches towards his targetPlayer and...");
    response.Messages.add("...does " + activeGame.getLastResultingDamage() + " damage to " + activeGame.getDefendingPlayer().getPlayerName());
    response.Messages.add(activeGame.getDefendingPlayer().getPlayerName() + " has " + activeGame.getDefendingPlayer().getCurrentHealth() + " health remaining.");
    activeGame.switchTurns();
  }

  private boolean initGame(String playerOneName, String playerTwoName) {
    playerOne = players.get(playerOneName);
    playerTwo = players.get(playerTwoName);
    if(playerOne == null || playerTwo == null ){
      response.Messages.add("No player named " + (playerOne == null ? playerOneName : playerTwoName) + " found in database. To create yourself type '!battle create'");
      return false;
    }else{
      activeGame = new BattleGame(playerOne, playerTwo);
      return true;
    }
  }
  private void startGame() {
    response.Messages.clear();
    if(activeGame.StartMatch()){
      response.Messages.add("!!!MATCH STARTED!!!");
      response.Messages.add("!!!MATCH STARTED!!! Follow it over at #ithivemind-game");
      response.Messages.add("Rolling the dice to see who gets to strike first...");
      response.Messages.add("Aaaaand " + activeGame.getAttackingPlayer().getPlayerName() + " won the dice roll!");
      response.Messages.add(activeGame.getDefendingPlayer().getPlayerName() + " is getting ready to defend...");
    }
  }

  private void determineWinner() {
    boolean levelUpWinner = false, levelUpLoser = false;
    activeGame.setMatchExp();
    levelUpWinner = activeGame.grantWinnngExp();
    levelUpLoser = activeGame.grantLosingExp();
    activeGame.clearPlayers();

    players.put(activeGame.getWinner().getPlayerName(), activeGame.getWinner());
    players.put(activeGame.getLoser().getPlayerName(), activeGame.getLoser());

    response.Messages.addAll(announcePostGame(levelUpWinner, levelUpLoser));
    additionalResponse.Messages.addAll(announcePostGame(levelUpWinner, levelUpLoser));
  }

  private List<String> announcePostGame(boolean levelUpWinner, boolean levelUpLoser) {
    List<String> result = new ArrayList<String>();
    result.add(activeGame.getLoser().getPlayerName() + " has suffered a gruesome death :( Rest in Pieces.");
    result.add("CONGRATUALATIONS " + activeGame.getWinner().getPlayerName() + "! Youve won this game!");

    result.add("!!!POST GAME!!!");
    result.add(activeGame.getWinner().getPlayerName() + " gained " + activeGame.getWinningExp() + "xp and " + activeGame.getLoser().getPlayerName() + " gained " + activeGame.getLosingExp() + "xp for fighting in this match");
    if(levelUpWinner)
      result.add("What's this? " + activeGame.getWinner().getPlayerName() + " is evolving! He is now level " + activeGame.getWinner().getLevel() + "!");
    if(levelUpLoser)
      result.add("What's this? " + activeGame.getLoser().getPlayerName() + " is evolving! He is now level " + activeGame.getLoser().getLevel() + "!");

    return result;
  }
}
