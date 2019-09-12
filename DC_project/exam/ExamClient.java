import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExamClient implements Runnable
{
	private static Socket clientSocket = null;
	private static PrintStream os = null;
	private static DataInputStream is = null;
	private static BufferedReader inputLine = null;
	private static boolean closed = false;
  
	public static void main(String[] args)
	{
		int portNumber = 8000;
		String host = "localhost";
		System.out.println("ExamClient\n");
    		try
		{
      			clientSocket = new Socket(host, portNumber);
      			inputLine = new BufferedReader(new InputStreamReader(System.in));
      			os = new PrintStream(clientSocket.getOutputStream());
      			is = new DataInputStream(clientSocket.getInputStream());
    		}
		catch (UnknownHostException e)
		{
      			System.err.println("Server Not Yet Started\n");
    		}
 		catch (IOException e)
		{
      			System.err.println("Couldn't get I/O for the connection to the host " + host);
    		}
    		if (clientSocket != null && os != null && is != null)
		{
      			try
			{
        			new Thread(new ExamClient()).start();
        			while (!closed)
				{
          				os.println(inputLine.readLine().trim());
        			}

        			os.close();
        			is.close();
        			clientSocket.close();
      			}
			catch (IOException e)
			{
        			System.err.println("IOException:  " + e);
      			}
    		}
  	}
  	public void run()
	{
    		String responseLine;
    		try
		{
     	 		while ((responseLine = is.readLine()) != null)
			{
        			System.out.println(responseLine);
			}
      			closed = true;
    		}
		catch (IOException e)
		{
      			System.err.println("IOException:  " + e);
    		}
  	}
}
