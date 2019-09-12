import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class MultiThreadChatServer
{
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final int maxClientsCount = 10;
	private static final clientThread[] threads = new clientThread[maxClientsCount];

	public static void main(String args[]) 
	{
		int portNumber = 8000;
		if (args.length < 1)
		{
			System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n"+ "Now using port number=" + portNumber);
		}
		else
		{
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		try
		{
			serverSocket = new ServerSocket(portNumber);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		while (true)
		{
			try
			{
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++)
				{
					if (threads[i] == null)
					{
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxClientsCount)
				{
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}
	}
}
class clientThread extends Thread 
{  
	private String clientName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClientsCount;

	public clientThread(Socket clientSocket, clientThread[] threads)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
	}
	public void run()
	{
		int maxClientsCount = this.maxClientsCount;
		clientThread[] threads = this.threads;
		try
		{
			String examAccount="Examiner";
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			String name;
			while (true)
			{
				os.println("Enter your name: ");
				name = is.readLine().trim();
				if (name.indexOf('@') == -1)
				{
					break;
        			}
				else
				{
          				os.println("The name should not contain '@' character.");
        			}
      			}
	      		os.println("Welcome " + name + " to our exam.\nTo leave enter /quit in a new line.");
      			synchronized (this)
			{
        			for (int i = 0; i < maxClientsCount; i++)
				{
          				if (threads[i] != null && threads[i] == this)
					{
            					clientName = "@" + name;
            					break;
        				}
        			}
				if (threads[0] != null && threads[0] != this)
				{
                 			threads[0].os.println("*** A new user " + name+ " entered the exam !!! ***");
          			}
      			}
      			while (true)
			{
        			String line = is.readLine();
        			if (line.startsWith("/quit"))
				{
          				break;
        			}
				for (int i = 0; i < maxClientsCount; i++) 
				{
                  			if (threads[i] != null && threads[i] != this && threads[i].clientName != null ) 
					{
						if(threads[i].clientName.equals("@Examiner"))
						{
							threads[0].os.println("<" + name + "> " + line);
							break;
                    				}	
						else
						{
							threads[i].os.println("<" + name + "> " + line);
							
						}
                  			}
                		}
            		}
			if (threads[0] != null && threads[0] != this && threads[0].clientName != null)
			{
            			threads[0].os.println("*** The user " + name + " is leaving the exam !!! ***");
        		}
      			os.println("*** Bye " + name + " ***");
      			synchronized (this)
			{
        			for (int i = 0; i < maxClientsCount; i++)
				{
        				if (threads[i] == this)
					{
        					threads[i] = null;
        		  		}
        			}
			}
      			is.close();
      			os.close();
      			clientSocket.close();
		}
		catch (IOException e) {
    		}
	}
}
