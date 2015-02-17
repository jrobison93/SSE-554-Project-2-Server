import java.net.SocketAddress;
import java.util.HashMap;

public class UsersList 
{
	public HashMap <String, SocketAddress> list;
	
	private static UsersList instance = null;
	private UsersList()
	{
		list = new HashMap<String, SocketAddress>();	
	}
	
	public static synchronized UsersList getInstance()
	{
		if(instance == null)
		{
			instance = new UsersList();
		}
		
		return instance;
	}
	

}
