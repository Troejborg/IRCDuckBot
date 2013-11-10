package main;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import commands.Command;
import commands.CommandFactory;
import commands.Response;
import org.jibble.pircbot.PircBot;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rt
 * Date: 10/11/13
 * Time: 7:26 AM
 * Steamduck Apps | 2013 | All rights reserved
 */
public class SteamduckBot extends PircBot{
  private static final String BATTLE_XP_LEVEL = "!battle nextlvl";
  private static final long MESSAGE_DELAY = 1500l;
  private String CUPCAKE_BOT;
  String lastMsg = "null", lastSender = "null";
  private String currentChannel = "#ithivemind";
  List<String> users = new ArrayList<String>();
  CommandFactory cmdFactory;
  Firebase ref;
  private String ident;

  public SteamduckBot() {
    this.setName("steamduck");
    ref = new Firebase("https://steamduck.firebaseIO.com");
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        CUPCAKE_BOT = dataSnapshot.child("friend").getValue().toString();
        ident = dataSnapshot.child("ident").getValue().toString();
      }

      @Override
      public void onCancelled() {

      }
    });
    cmdFactory = new CommandFactory();
  }

  private String[] splitMsg(String msg){
    return msg.split(" ");
  }

  public static void main(String[] args) throws Exception {
    // Now start our bot up.
    SteamduckBot bot = new SteamduckBot();

    // Enable debugging output.
    bot.setVerbose(true);

    // Connect to the IRC server.
    bot.connect("irc.freenode.net");

    // Join the channels.
    bot.joinChannel("#ithivemind");
    bot.joinChannel("#ithivemind-game");


  }
  protected void onConnect(){
    sendMessage("NickServ", "identify " + ident);
    setLogin("steamduck");
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
    if(message.equals("!debug")){
      message = "!battle create";
      sender = "steamduck_debug";
    }

      Command newCmd = cmdFactory.getCommandType(sender, splitMsg(message), message);

    // Get main response
    Response response = newCmd.interpretCommand();
    String targetChannel = response.Channel != null ? response.Channel : channel;
    if(response.Messages.size() > 5)
      setMessageDelay(MESSAGE_DELAY);
    for(String msg : response.Messages){
      sendMessage(targetChannel, msg);
    }

    // Check for additional messages
    Response additionalResponse = newCmd.getAdditionalMessages();
    if(additionalResponse != null){
      if(additionalResponse.Messages.size() > 5)
        setMessageDelay(MESSAGE_DELAY);
      targetChannel = additionalResponse.Channel != null ? additionalResponse.Channel : channel;
      for(String msg : additionalResponse.Messages){
        sendMessage(targetChannel, msg);
      }
    }
  }
}