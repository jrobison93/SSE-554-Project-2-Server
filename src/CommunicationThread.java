import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class CommunicationThread implements Runnable
{
	private Socket incoming;
	private UsersList users;
	
	public CommunicationThread(Socket i)
	{
		incoming = i;
		users = UsersList.getInstance();
	}
	
	public void run()
	{
		try
		{
			try
			{
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();
				
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);
				
				out.println("Hello! What is your username?");
				String username = in.nextLine();
				
				while(users.list.containsKey(username))
				{
					out.println("This username \"" + username + "\" already exists! Try again: ");
					username = in.nextLine();
				}
				
				users.list.put(username, incoming.getRemoteSocketAddress());
				
				printMenu(out);
				
				boolean done = false;
				while(!done)
				{
					String input = in.next();
					
					switch(input)
					{
						case "p":
							printUsers(out);
							break;
						case "m":
							out.println("What user would you like to message? ");
							String user = in.nextLine();
							
							if(users.list.containsKey(user))
							{
								
							}
							else
							{
								out.println("The username \"" + user + "\" does not exist!");
							}
							
							break;
						case "d":
							done = true;
							out.println("Bye!");
							users.list.remove(username);
							break;
						case "h":
							printMenu(out);
							break;
						default:
							out.println("Invalid command! Type 'h' to reprint the menu.");
							break;
					}
					
				}
				
			}
			finally
			{
				incoming.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void printMenu(PrintWriter out)
	{
		out.println("|            Menu           |");
		out.println("|---------------------------|");
		out.println("|'p'|  Print users list     |");
		out.println("|'m'|  Message a users      |");
		out.println("|'d'|  Disconnect           |");
		out.println("|'h'|  Print menu           |");
		out.println("|---------------------------|\n");
	}
	
	private void printUsers(PrintWriter out)
	{
		if(users.list.size() == 0)
		{
			out.println("You are the only user logged in!");
		}
		else
		{
			out.println("These are the available users: ");
			for(String key : users.list.keySet())
			{
				out.println("\t" + key);
			}
		}
	}

}
