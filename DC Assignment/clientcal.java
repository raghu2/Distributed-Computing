import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class clientcal
{
	public static void main(String args[]) throws IOException
	{
		//reading the input
		Scanner sc = new Scanner(System.in);

		//creating a data socket
		DatagramSocket sock = new DatagramSocket();

		//ip address
		InetAddress add = InetAddress.getLocalHost();

		//buffer for reading input
		byte buf[] = null;
		System.out.println("'operand1 operator operand2'");

		//reading the input
		String str = sc.nextLine();
		buf = new byte[100];

		//converting string to byte format
		buf = str.getBytes();

		//datagram packet initialisation
		DatagramPacket sen = new DatagramPacket(buf, buf.length, add, 8888);

		//sending to the socket datagram packet
		sock.send(sen);
		buf = new byte[100];

		//receiving the datagram packet for message
		DatagramPacket rec = new DatagramPacket(buf, buf.length);
		sock.receive(rec);
		System.out.println("Answer = " +new String(buf,0,buf.length));
	}
}

