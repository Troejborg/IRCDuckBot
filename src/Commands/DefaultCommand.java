package commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 25-10-13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class DefaultCommand implements Command {
  private String superAuthedUser = "lite_";
  public static final String THE_TIME = "!time";
  public static final String LAST_MSG = "!lastmsg";
  private static String HELLO_STEAMDUCK = "hello steamduck";
  private static String LOVE_STEAMDUCK = "<3 steamduck";
  private static String ECHO = "!echo ";
  private final String sender;
  private String action;
  Response response;

  public DefaultCommand(String sender, String cmdString) {
    this.sender = sender;
    action = cmdString;
    response = new Response();
  }

  @Override
  public Response interpretCommand() {
    Random r = new Random();
    response.Messages.clear();
    if (action.equals(THE_TIME)) {
      String time = new Date().toString();
      response.Messages.add(sender + ": The time is now " + time);
    }
    if(action.equals(LOVE_STEAMDUCK)){
      response.Messages.add("aaw. I love you too, " + sender + " <3 !");
    }
    if(action.equals(HELLO_STEAMDUCK)){
      if(superAuthedUser.equals(sender)){
        response.Messages.add("Hey there, " + sender + ". youre my favourite person!");
      }else
        response.Messages.add("go away, peasant.");
    }
    else if(action.equalsIgnoreCase("!martin")){
      response.Messages.add("http://www.cupcakeserver.dk/hvordanharmartindet");
    }
    if(action.contains(" osse")){
      response.Messages.add("Message containing 'osse' recognized. Did you mean 'også', dumbass?");
    }

    if(action.startsWith("!burger") || action.startsWith("!pool")){
      Date burgerDate = null;
      try {
        burgerDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse("2014-04-02 19:00:00.000000");
      } catch (ParseException e) {
        e.printStackTrace();
      }
      long difference = burgerDate.getTime() - new Date().getTime();
      response.Messages.add("POOL & BURGER COUNTDOWN : " + difference / 1000 + " seconds!");
    }
    if(action.startsWith("!8ball")){
      int randomNum = r.nextInt(8);
      response.Messages.add(EightBall.AskHim(randomNum));
    }
    if(action.startsWith("!flip")){
      response.Messages.add("Flipping a coin...");
      response.Messages.add(r.nextBoolean() ? "HEADS!" : "TAILS!");
    }
    if(action.startsWith("!roll")){
      response.Messages.add("Rolling(rollin' rollin') a random number between 0 and 100...");
      response.Messages.add("..." + r.nextInt(101) + "!");
    }

    return response;
  }

  @Override
  public Response getAdditionalMessages() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  private static class EightBall {
    public static String AskHim(int r){
      String ans = null;
      switch(r)
      {
        case 1: ans = "It is decidedly so.";
          break;
        case 2: ans = "My reply is no";
          break;
        case 3: ans =  "It's possible.";
          break;
        case 4: ans =  "In your dreams...";
          break;
        case 5: ans =  "Reply hazy, try again.";
          break;
        case 6: ans =  "Most Likely";
          break;
        case 7: ans =  "Who knows?";
          break;
        case 8: ans =  "Go for it!";
          break;
      }
      return ans;
    }
  }
}
