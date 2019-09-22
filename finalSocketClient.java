import java.io.IOException;
import java.net.*;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.time.LocalTime;
import java.util.*;

public class finalSocketClient
{
    HashMap<String,String> hm=new HashMap<String,String>();
    public void createSocket()
    {
        createReadThread();
        createWriteThread();
    }
    public void createWriteThread()
    {
        Thread writeThread = new Thread()
        {
            public void run()
            {
                try
                {
                    int m=500000;
                    DatagramSocket ss = new DatagramSocket();
                    while(m>0)
                    {
                        String sentence = "This is a message from client";
                        byte[] data = sentence.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 12345);
                        ss.send(sendPacket);
                        System.out.println("Message sent from client");
                        Thread.sleep(5000);
                        m-=1;
                    }

                    ss.close();

                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (SocketException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        writeThread.setPriority(Thread.MAX_PRIORITY);
        writeThread.start();
    }
    public void createReadThread()
    {
        Thread readThread = new Thread()
        {
            public void run()
            {
                try
                {
                    DatagramSocket ss = new DatagramSocket(null);
                    ss.setReuseAddress(true);
                    ss.bind(new InetSocketAddress(12345));
                    int k=5000000;
                    while(k>0)
                    {
                        Date date = new Date();
                        byte[] incomingData = new byte[1024];
                        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                        ss.receive(incomingPacket);
                        String response = new String(incomingPacket.getData());
                        System.out.println("Response from server: ");
                        System.out.println("TIME: "+date.toString() +" IP: "+ incomingPacket.getAddress());
                        hm.put(incomingPacket.getAddress().toString(),date.toString());
                        Thread.sleep(5000);
                        k-=1;
                    }
                    ss.close();
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (SocketException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        readThread.start();

        Thread print = new Thread()
        {
            @Override
            public void run()
            {
                int n=999999;
                while(n>0)
                {
                    Scanner input = new Scanner(System.in);
                    String com = input.nextLine();
                    if(Objects.equals("list",com))
                    {
                        System.out.println(" Printing the  I.P. list:- ");
                        for(Map.Entry m:hm.entrySet())
                        {
                            System.out.println(m.getKey()+" "+m.getValue());
                        }
                    }
                    n--;
                }
            }
        };
        print.start();
    }
    public static void main(String[] args) throws Exception
    {
        finalSocketClient myChatClient = new finalSocketClient();
        myChatClient.createSocket();

        String str="";
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements())
        {
            NetworkInterface ni = en.nextElement();
            List<InterfaceAddress> list = ni.getInterfaceAddresses();
            Iterator<InterfaceAddress> it = list.iterator();

            while (it.hasNext())
            {
                InterfaceAddress ia = it.next();
                if(ia.getBroadcast()!=null)
                {
                    str=ia.getBroadcast().toString();
                    break;
                }
            }
        }
        str=str.substring(1,str.length());
        System.out.println("Broadcast = " + str);
    }
}
