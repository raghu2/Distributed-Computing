import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class ExamServer
{
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final int maxClientsCount = 10;
	private static final clientThread[] threads = new clientThread[maxClientsCount];

	public static void main(String args[]) 
	{
		int portNumber = 8000;//port number
		System.out.println("java ExamServer started\n");
		try//starting the socket
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
				clientSocket = serverSocket.accept();//connecting the client socket (socket) to server socket (serversocket)
				int i = 0;
				for (i = 0; i < maxClientsCount; i++)//creating the threads and connecting them to the clients
				{
					if (threads[i] == null)
					{
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxClientsCount)//reaching max client count of limit 10
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
			//String examAccount="Examiner";//starting with Examiner
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			String name;
			while (true)
			{
				os.println("Enter your name: ");
				name = is.readLine().trim();
				break;
      			}//taking up the names of each of the clients
	      		os.println(name + " entered to the exam.\nTo leave enter /q in a new line.");
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
                 			threads[0].os.println(name+ " entered the exam ");
          			}
      			}
      			while (true)
			{
        			String line = is.readLine();
        			if (line.startsWith("/q"))
				{//for quitting the exam when client wishes to
          				break;
        			}
				//this is for printing the string text that examiner types to all the other students and then when the student types, it only should be displayed on the examiner account
				//rather than printing on every other's account.
				for (int i = 0; i < maxClientsCount; i++) 
				{
                  			if (threads[i] != null && threads[i] != this && threads[i].clientName != null ) 
					{
						if(threads[i].clientName.equals("@Examiner"))
						{
							threads[0].os.println(name + ": " + line);
							break;
                    				}	
						else
						{
							threads[i].os.println(name + ": " + line);
							
						}
                  			}
                		}
            		}//if somebody's quitting, text will be displayed on their account.
			if (threads[0] != null && threads[0] != this && threads[0].clientName != null)
			{
            			threads[0].os.println(name + " has finished the exam and quitting.");
        		}
      			os.println("Thank you " + name );
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
		catch (IOException e)
		{
    		}
	}
}
