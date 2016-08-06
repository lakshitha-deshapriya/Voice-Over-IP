/***
Sender should address packages to an IP number in the range
 between 224.0.0.1 and 239.255.255.254.
 Please see the full range of Multicast IP Address

***/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.net.* ;
import java.util.Date;

public class PlayAudio extends AudioOut{
	
	int port = 20000;
	ByteArrayOutputStream byteArrayOutputStream;
	boolean stopPlay;
	SourceDataLine sourceDataLine;
	byte tempBuffer[] = new byte[432];
	int recivePort;
    MulticastSocket socket ;
	DatagramPacket packet;
	private final static int packetsize = 864 ;
	InetAddress address;
	/**
	**get pear address, create socket and packet
	**multi-casting goes here
	**/

	public PlayAudio(String ip){
		
		this.stopPlay=false;
		recivePort = port;
		
		try{
			
			 address = InetAddress.getByName(ip);
		
			
		}catch(IOException  e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	//set system volume setting and audio format	
	public void configePlay(){
		try{
		DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();        
        //Setting the maximum volume
        FloatControl control = (FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(control.getMaximum());
		}catch(LineUnavailableException e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	//threading
	@Override
	public void run(){
		//time counting to get the packet loss
		long startTime,endTime,diff;

		byteArrayOutputStream = new ByteArrayOutputStream();
	    Record rec = new Record();//creates an object from Record class
	    startTime = new Date().getTime();
		
		try {
			socket = new MulticastSocket(recivePort) ;
			packet = new DatagramPacket( new byte[packetsize], packetsize ) ;		
			socket.joinGroup(address);
			
			int readCount = 432;
			
			while (!stopPlay) {				
				socket.receive( packet );
			
				//count lost packet in 10 second, de-serialize receive serialized byte stream  
				if(packet.getData()!=null){		
					PacketFormat receivepac = (PacketFormat)PacketFormat.deserialize(packet.getData());				
					rec.addPktNo(receivepac.getSeqNo());
					
					endTime = new Date().getTime();
					
					//reset log every 10 sec                
					if((endTime - startTime)> 10000){
						System.out.println("No of lost packets in 10s - "+ rec.getLsCount());
						startTime = new Date().getTime();//reset timer
						rec = new Record();
					}
					
					//System.out.println(receivepac.getSeqNo() + "\n");
					byteArrayOutputStream.write(packet.getData(), 0, readCount);
					sourceDataLine.write(receivepac.getTempBuffer(), 0, 432);   //playing audio available in tempBuffer
			  
					/* System.out.println(packet.getData());
						byteArrayOutputStream.write(packet.getData(), 0, readCount);
						sourceDataLine.write(packet.getData(), 0, 500);   //playing audio available in tempBuffer
					*/
				}
			}
            //byteArrayOutputStream.close();
         }catch (Exception e) {
            System.out.println(e);
            System.exit(0);
		}
	}
	
	//to start and stop capturing
	public void startPlay(){
		this.stopPlay=false;
	}	
	public void stopPlay(){
		this.stopPlay=true;
	}	
}
