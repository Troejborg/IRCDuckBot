package Commands;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 20-10-13
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
public interface Command {
  public Response interpretCommand();
  public Response getAdditionalMessages();
}
