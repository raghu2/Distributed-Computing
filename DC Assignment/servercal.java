import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class servercal
{
    public static void main(String[] args) throws IOException
    {
	//creating a datagram socket
        DatagramSocket sock = new DatagramSocket(8888);

	//buffer for input
	byte[] buf = null;

	//intialising datagram packet for send and receive
        DatagramPacket rec = null;
        DatagramPacket sen = null;

	buf = new byte[100];

	//receiving datagram packet and initialissing the buffer for string
	rec = new DatagramPacket(buf, buf.length);
	sock.receive(rec);
	String str = new String(buf, 0, buf.length);

	System.out.println("Equation Received:- " +str);
	int result;

	//breaking the string into tokens for easy reading
	StringTokenizer st = new StringTokenizer(str);
	int oprnd1 = Integer.parseInt(st.nextToken());
	String operation = st.nextToken();
	int oprnd2 = Integer.parseInt(st.nextToken());

	//operation
	if (operation.equals(" + "))
		result = oprnd1 + oprnd2;
	else if (operation.equals(" - "))
		result = oprnd1 - oprnd2;
	else if (operation.equals(" * "))
		result = oprnd1 * oprnd2;
	else
		result = oprnd1 / oprnd2;

            System.out.println("Sending the result...");

	//string to buffer
	buf = str.getBytes();

	int port = rec.getPort();

	//sending the result to client
	sen = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), port);
	sock.send(sen);
    }
}
