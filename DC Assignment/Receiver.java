import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.StringWriter;
/** Rakesh Kumar 
    18MCMT04
    M.Tech. C.S.
*/
 
public class Receiver
{
 
    private static Socket socket;
 
    public static void main(String[] args)
    {
        try
        {
 
            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Receiver Started and listening to the port 25000");
 
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
                //Reading the message from the Sender
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String number = br.readLine();
                System.out.println("Message received from Sender is "+number);
 
                //Calculating and forming the return message
                String[] splitArray= number.split("\\s+");
                int num1=Integer.parseInt(splitArray[0]);
                int num2=Integer.parseInt(splitArray[1]);
                String opcode=splitArray[2].toString().trim();
                int res=0;
                String returnMessage;
                try
                {
                    switch(opcode)
                    {
                          case "+":
                	   res=(num1+num2);
                	   break;
                          case "-":
                	   res= (num1-num2);
                	   break;
                          case "*":
                	   res= (num1*num2);
                	   break;
                          case "/":
                	   if(num2!=0)
                	       res=(num1/num2);
                	   else
                	       System.out.println("Zero division error..");
                	   break;
                          case "%":
                	   res=(num1%num2);
                	   break;
                          default:
                           res=-1;
                	   System.out.println("Invalid Opcode..");
                	   break;
                     }
                }
                catch(NumberFormatException e)
                {
                    //Input was not a number. Sending proper message back to Sender.
                    returnMessage = "Please send a proper number\n";
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    System.out.println(exceptionAsString);
                }
                if(res<0)
                   returnMessage="Please enter a correct opcode..\n";  
                else
                   returnMessage=String.valueOf(res)+"\n";
                   
                //Sending the response back to the Sender.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage);
                System.out.println("Message Result sent to the Sender is "+returnMessage);
                bw.flush();
            }
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.out.println(exceptionAsString);
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){
                  StringWriter sw = new StringWriter();
                  e.printStackTrace(new PrintWriter(sw));
                  String exceptionAsString = sw.toString();
                  System.out.println(exceptionAsString);
              }
        }
    }
}
