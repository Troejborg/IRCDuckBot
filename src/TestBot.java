import org.jibble.pircbot.PircBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/11/13
 * Time: 7:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestBot extends PircBot{
  private static final String OP_OWNER = "!op me";
  public static final String THE_TIME = "!time";
  public static final String LAST_MSG = "!lastmsg";
  String lastMsg = "null", lastSender = "null";
  private String currentChannel = "#ithivemind";
  private static String BATTLE_CHAN = "#ithivemind-game";
  private static String BATTLE_START = "!battle start ";
  private static String BATTLE_CREATE = "!battle create";
  private static String BATTLE_STATS = "!battle stats ";
  private static String BATTLE_NEXT_ROUND = "!battle next";
  private static String HELLO_STEAMDUCK = "hello steamduck";
  private static String ECHO = "!echo ";
  Player playerOne, playerTwo;
  List<String> superAuthedUsers = new ArrayList<String>();
  List<String> authedUsers = new ArrayList<String>();
  List<String> users = new ArrayList<String>();
  Map<String,Player> players = new HashMap<String, Player>();
  BattleGame activeGame;

  public TestBot() {
    this.setName("steamduck");
    superAuthedUsers.add("lite_");
  }
  public static void main(String[] args) throws Exception {
    // Now start our bot up.
    TestBot bot = new TestBot();

    // Enable debugging output.
    bot.setVerbose(true);

    // Connect to the IRC server.
    bot.connect("irc.freenode.net");

    // Join the channels.
    bot.joinChannel("#ithivemind");
    bot.joinChannel("#ithivemind-game");

  }
  protected void onConnect(){
    sendMessage("NickServ", "identify 200687");
  }
  protected void onJoin(String channel, String sender, String login, String hostname) {

    if(!users.contains(sender)){
      if(sender != getName())
        sendMessage(channel, "Welcome to #ithivemind, " + sender + "! Enjoy your stay.");
      else
        sendMessage(channel, "RAWR!");

      users.add(sender);
      Player steamduck = new Player("steamduck");
      steamduck.setLevel(1);
      players.put("steamduck", steamduck);

    }

  }


  public void onNickChange(String s, String s2, String s3, String s4) {
    super.onNickChange(s, s2, s3, s4);    //To change body of overridden methods use File | Settings | File Templates.
  }

  public void onMessage(String channel, String sender,
                        String login, String hostname, String message) {
    String response;
    if (message.equalsIgnoreCase(THE_TIME)) {
      String time = new java.util.Date().toString();
      sendMessage(channel, sender + ": The time is now " + time);
    }
    if(message.equalsIgnoreCase(OP_OWNER) && superAuthedUsers.contains(sender)){
      op(channel, sender);
    }
    if(message.equalsIgnoreCase(HELLO_STEAMDUCK)){
      if(superAuthedUsers.contains(sender)){
        sendMessage(channel, "Hey there, " + sender + ". youre my favourite person!");
      }else
        sendMessage(channel, "go away, peasant.");
    }
    else if(message.equalsIgnoreCase("!martin")){
      response = "http://www.cupcakeserver.dk/hvordanharmartindet";
      sendMessage(channel, response);
    }
    else if(message.startsWith(ECHO)){
      sendMessage(channel, message.substring(ECHO.length()));
    }
    else if(message.equalsIgnoreCase(LAST_MSG)){
      sendMessage(channel, sender + ": This was the last message I recorded:");
      sendMessage(channel, lastSender + ": " + lastMsg);
    }
    else if(message.contentEquals(BATTLE_CREATE)){
      if(players.get(sender) == null){
        players.put(sender, new Player(sender));
        sendMessage(channel, sender + " has been added to the player database");
      }else
        sendMessage(channel, sender + " already exists in my database");
    }
    else if(message.startsWith(BATTLE_START)){
      if(initGame(sender, message.substring(BATTLE_START.length()))){
        startGame();
        setMessageDelay(1000l);
        while(!activeGame.evalIsGameOver()){
          playNextRound();
          sendMessage(currentChannel, "END OF ROUND " + activeGame.RoundNumber + "!");
        };
        determineWinner();
      }

    }
    else if(message.startsWith(BATTLE_NEXT_ROUND)){
      playNextRound();
    }
    else if(message.startsWith(BATTLE_STATS)){
      Player player = players.get(message.substring(BATTLE_STATS.length()));
      if(player != null)
        sendMessage(channel, player.getPlayerName() + " is level " + player.getLevel() + ", has " + player.getWins() + " wins and " + player.getLosses() + " losses.");
      else
        sendMessage(channel, "Could not find player in database");
    }
    if(sender != getNick()){
      lastMsg = message;
      lastSender = sender;
    }
  }

  private void determineWinner() {
    activeGame.setMatchExp();
    sendMessage(currentChannel, activeGame.getLoser().getPlayerName() + " has suffered a gruesome death :( Rest in Pieces.");
    sendMessage(currentChannel, "CONGRATUALATIONS " + activeGame.getWinner().getPlayerName() + "! Youve won this game!");

    sendMessage(currentChannel, "!!!POST GAME!!!");
    sendMessage(currentChannel, activeGame.getWinner().getPlayerName() + " gained " + activeGame.getWinningExp() + "xp and "+activeGame.getLoser().getPlayerName() + " gained " + activeGame.getLosingExp() + "xp for fighting in this match");
    if(activeGame.grantWinnngExp())
      sendMessage(currentChannel, "What's this? " + activeGame.getWinner().getPlayerName() + " is evolving! He is now level " + activeGame.getWinner().getLevel() + "!");
    if(activeGame.grantLosingExp())
      sendMessage(currentChannel, "What's this? " + activeGame.getLoser().getPlayerName() + " is evolving! He is now level " + activeGame.getLoser().getLevel() + "!");
    activeGame.clearPlayers();

    players.put(activeGame.getWinner().getPlayerName(), activeGame.getWinner());
    players.put(activeGame.getLoser().getPlayerName(), activeGame.getLoser());


  }

  private void playNextRound() {
    activeGame.startNextRound();
    sendMessage(currentChannel, activeGame.getAttackingPlayer().getPlayerName() + " charges forward! He launches towards his opponent and...");
    sendMessage(currentChannel, "...does " + activeGame.getLastResultingDamage() + " damage to " + activeGame.getDefendingPlayer().getPlayerName());
    sendMessage(currentChannel, activeGame.getDefendingPlayer().getPlayerName() + " has " + activeGame.getDefendingPlayer().getCurrentHealth() + " health remaining.");
    activeGame.switchTurns();
  }

  private void startGame() {

    if(activeGame.StartMatch()){
      sendMessage(currentChannel, "!!!MATCH STARTED!!!");
      sendMessage(currentChannel, "Rolling the dice to see who gets to strike first...");
      sendMessage(currentChannel, "Aaaaand " + activeGame.getAttackingPlayer().getPlayerName() + " won the dice roll!");
      sendMessage(currentChannel, activeGame.getDefendingPlayer().getPlayerName() + " is getting ready to defend...");
    }

  }

  private boolean initGame(String playerOneName, String playerTwoName) {
    playerOne = players.get(playerOneName);
    playerTwo = players.get(playerTwoName);
    if(playerOne == null || playerTwo == null ){
      sendMessage(currentChannel, "No player named " + (playerOne == null ? playerOneName : playerTwoName) + " found in database. To create yourself type '!battle create'");
      return false;
    }else{
      activeGame = new BattleGame(playerOne, playerTwo);
      return true;
    }

  }

  private void adminCmd(String channel, String sender,String cmd){
    if(cmd.equalsIgnoreCase("!op me")){
      if(superAuthedUsers.contains(sender))
        op(channel, sender);
      else
        sendMessage(channel, "You're not my real dad!");
    }
    else if(cmd.equalsIgnoreCase("!voice me")){
      if(superAuthedUsers.contains(sender))
        voice(channel, sender);
    }
    else if(cmd.equalsIgnoreCase("!unvoice me")){
      if(superAuthedUsers.contains(sender))
        deVoice(channel, sender);
    }
    else if(cmd.equalsIgnoreCase("!down me")){
      deOp(channel, sender);
    }
    else if(cmd.startsWith("!addauth")){
      if(superAuthedUsers.contains(sender)){
        String newUser = cmd.substring("!addauth ".length());
        authedUsers.add(newUser);
        sendMessage(channel, sender + ": gave auth to user " + newUser);
      }
    }
    else if(cmd.startsWith("!remauth")){
      String newUser = cmd.substring("!remauth ".length());
      if(superAuthedUsers.contains(sender)){
        if(authedUsers.contains(newUser)){
          authedUsers.remove(newUser);
          sendMessage(channel, sender + ": removed auth from user " + newUser);
        }else
          sendMessage(channel, "Sorry, " + sender + ". I couldn't find " + newUser + " :(");
      }
    }
  }
}
