/*
https://www.baeldung.com/udp-in-java
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];


    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public static void main(String[] args) throws SocketException {
        EchoServer server = new EchoServer();
        server.run();
    }

    public void run() {
        System.out.println("Running server on port " + this.socket.getLocalPort());
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
                received = received.trim();
                int num = Integer.parseInt(received);
                String contents = num * num + "";
                DatagramPacket p = new DatagramPacket(contents.getBytes(), contents.length(), address, port);
                socket.send(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}