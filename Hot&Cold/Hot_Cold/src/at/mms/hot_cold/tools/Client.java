package at.mms.hot_cold.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class Client {

	private final String address = "192.168.178.42";
	private final int port = 9998;
	private Socket s;
	private BufferedReader input;
	private Bitmap image;
	private BufferedInputStream bufferedInputStream;
	private OutputStream out;
	
	public Client() {
	}
	
	public void connect() throws UnknownHostException, IOException, InterruptedException {
		s = new Socket(address, port);
		System.out.println("Connected to " + address + ":" + port);
	}
	
	public String receiveData() throws IOException {
		return input.readLine();
	}
	
	public void sendImage(String filePath) throws UnknownHostException, IOException, InterruptedException {
		
		image = BitmapFactory.decodeFile(filePath);
		s.getOutputStream().write("newPlayer".getBytes());
		Thread.sleep(1000);
		s.getOutputStream().write("img".getBytes());
		Thread.sleep(1000);
		
		boolean ok;
		ok = image.compress(Bitmap.CompressFormat.JPEG, 100, s.getOutputStream());
		if(!ok)
			System.out.println("Error");
	}
	
	public void cancel() throws UnknownHostException, IOException, InterruptedException {
		s.getOutputStream().write("cancel".getBytes());
		Thread.sleep(100);
		closeConnection();
	}
	
	public void done(String str) throws UnknownHostException, IOException, InterruptedException {
		if (str.compareTo("hint") == 0) {
			s.getOutputStream().write("done".getBytes());
		} else {
			s.getOutputStream().write("n".getBytes());
			closeConnection();
		}
	}
	
	
	public void closeConnection() throws IOException {
		System.out.println("Closing connection!");
		s.close();
	}
	
	public void sendText(String text)throws UnknownHostException, IOException, InterruptedException  {
		String[] hints = text.split(";");
		s.getOutputStream().write("newHint".getBytes());
		Thread.sleep(100);
		s.getOutputStream().write("txt".getBytes());
		Thread.sleep(100);
		
		if(hints.length == 1){
			s.getOutputStream().write(text.getBytes());
			Thread.sleep(100);	
		} else {
			sendCoord(hints[0], hints[1]);
		}
	}
	
	public void createPlayer(String text, String text2)throws UnknownHostException, IOException, InterruptedException  {
		s.getOutputStream().write("newPlayer".getBytes());
		Thread.sleep(100);
		s.getOutputStream().write(text.getBytes());
		Thread.sleep(100);
		s.getOutputStream().write(text2.getBytes());
		closeConnection();
	}
	
	public void sendAudio(String filePath) throws IOException {
		
		s.getOutputStream().write("3gp".getBytes());
		File file = new File(filePath);
		byte[] byteArray = new byte[(int)file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		
		bufferedInputStream = new BufferedInputStream(fileInputStream);
		bufferedInputStream.read(byteArray,0, byteArray.length);
		
		out = s.getOutputStream();
		out.write(byteArray,0, byteArray.length);
		
	}
	
	public void newGame(String gameName) throws UnknownHostException, IOException, InterruptedException {
		s.getOutputStream().write("newGame".getBytes());
		Thread.sleep(100);
		s.getOutputStream().write(gameName.getBytes());
		Thread.sleep(1000);
		s.getOutputStream().write("descr".getBytes());
		Thread.sleep(100);
	}
	
	public void sendCoord(String latit, String longit) throws UnknownHostException, IOException, InterruptedException {
		
		s.getOutputStream().write("latitude".getBytes());
		Thread.sleep(100);
		s.getOutputStream().write(latit.getBytes());
		Thread.sleep(100);
		s.getOutputStream().write("longitude".getBytes());
		Thread.sleep(100);
		s.getOutputStream().write(longit.getBytes());
		Thread.sleep(100);
	}
	
	//Methods to get back these elements
	
	
}