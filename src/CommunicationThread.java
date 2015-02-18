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
			String username = null;
			try
			{
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();
				
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);
				
				out.println("Hello! What is your username?");
				username = in.nextLine().trim();
				
				System.out.println(username);
				
				while(users.list.containsKey(username))
				{
					out.println("This username \"" + username + "\" already exists! Try again: ");
					username = in.nextLine().trim();
				}
				
				users.list.put(username, incoming.getRemoteSocketAddress());
				
				out.println("Welcome!");
				
				//printMenu(out);
				
				boolean done = false;
				while(!done && in.hasNextLine())
				{
					String input = in.nextLine().trim();
					
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
				System.out.println("BYE " + username + "!");
				users.list.remove(username);
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
		out.println("|'m'|  Message a user       |");
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
