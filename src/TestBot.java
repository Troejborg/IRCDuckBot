import org.jibble.pircbot.PircBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/11/13
 * Time: 7:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestBot extends PircBot{
  String lastMsg = "null", lastSender = "null";
  List<String> superAuthedUsers = new ArrayList<String>();
  List<String> authedUsers = new ArrayList<String>();
  List<String> users = new ArrayList<String>();
  public TestBot() {
    this.setName("steamduck");

    superAuthedUsers.add("ThomasCle");
    superAuthedUsers.add("lite_");
  }
  public static void main(String[] args) throws Exception {
    // Now start our bot up.
    TestBot bot = new TestBot();

    // Enable debugging output.
    bot.setVerbose(true);

    // Connect to the IRC server.
    bot.connect("irc.freenode.net");

    // Join the #pircbot channel.
    bot.joinChannel("#ithivemind");

  }
  protected void onConnect(){
    sendMessage("NickServ", "identify trolol");
  }
  protected void onJoin(String channel, String sender, String login, String hostname) {
    if(superAuthedUsers.contains(sender))
      op(channel, sender);
    else if(authedUsers.contains(sender))
      voice(channel, sender);

    if(!users.contains(sender)){
      if(sender != getName())
        sendMessage(channel, "Welcome to #ithivemind, " + sender + "! Enjoy your stay.");
      else
        sendMessage(channel, "RAWR!");

      users.add(sender);
    }

  }

  public void onMessage(String channel, String sender,
                        String login, String hostname, String message) {
    String response;
    if (message.equalsIgnoreCase("!time")) {
      String time = new java.util.Date().toString();
      sendMessage(channel, sender + ": The time is now " + time);
    }
    else if(message.equalsIgnoreCase("!martin")){
      response = "http://www.cupcakeserver.dk/hvordanharmartindet";
      sendMessage(channel, response);
    }
    else if(message.equalsIgnoreCase("!lastmsg")){
      sendMessage(channel, sender + ": This was the last message I recorded:");
      sendMessage(channel, lastSender + ": " + lastMsg);
    }
    if(sender != getNick()){
      lastMsg = message;
      lastSender = sender;
    }
  }

  private void adminCmd(String channel, String sender,String cmd){
    if(cmd.equalsIgnoreCase("!op me")){
      if(superAuthedUsers.contains(sender))
        op(channel, sender);
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
