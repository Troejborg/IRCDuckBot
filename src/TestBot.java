import Commands.Command;
import Commands.CommandFactory;
import Game.Player;
import Commands.Response;
import org.jibble.pircbot.PircBot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/11/13
 * Time: 7:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestBot extends PircBot{
  private static final String BATTLE_XP_LEVEL = "!battle nextlvl";
  private static final String CUPCAKE_BOT = "CupcakeStatbot";
  String lastMsg = "null", lastSender = "null";
  private String currentChannel = "#ithivemind";
  Player playerOne, playerTwo;
  List<String> superAuthedUsers = new ArrayList<String>();
  List<String> authedUsers = new ArrayList<String>();
  List<String> users = new ArrayList<String>();
  CommandFactory cmdFactory;

  public TestBot() {
    this.setName("steamduck_debug");
    superAuthedUsers.add("lite_");
    cmdFactory = new CommandFactory();
  }

  private String[] splitMsg(String msg){
    return msg.split(" ");
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
        sendMessage(channel, "Welcome to " + channel +", " + sender + "! Enjoy your stay.");
      else
        sendMessage(channel, "RAWR!");
      if(sender.equalsIgnoreCase(CUPCAKE_BOT))
        sendMessage(channel, "AIA(Artificial Intelligence Alert) - Let's give this one some power!");
        op(channel, CUPCAKE_BOT);

      users.add(sender);
    }

  }


  public void onNickChange(String s, String s2, String s3, String s4) {
    super.onNickChange(s, s2, s3, s4);    //To change body of overridden methods use File | Settings | File Templates.
  }

  public void onMessage(String channel, String sender,
                        String login, String hostname, String message) {
    Command newCmd = cmdFactory.getCommandType(sender, splitMsg(message), message);

    // Get main response
    Response response = newCmd.interpretCommand();
    String targetChannel = response.Channel != null ? response.Channel : channel;
    for(String msg : response.Messages){
      sendMessage(targetChannel, msg);
    }

    // Check for additional messages
    Response additionalResponse = newCmd.getAdditionalMessages();
    if(additionalResponse != null){
      targetChannel = additionalResponse.Channel != null ? additionalResponse.Channel : channel;
      for(String msg : additionalResponse.Messages){
        sendMessage(targetChannel, msg);
      }
    }
  }
}
