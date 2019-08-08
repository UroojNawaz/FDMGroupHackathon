package com.fdmgroup.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.Instant;
import java.util.Scanner;

public class TimeLogger implements Runnable {

	@Override
	public void run() {
		try (FileOutputStream is = new FileOutputStream(new File("timeStamps.txt"));
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
				Scanner scan = new Scanner(System.in);){
			
			String time = Instant.now().toString();
			System.out.println(time);
			w.append(time + "\n");
			
			@SuppressWarnings("unused")
			String input = scan.nextLine();
			time = Instant.now().toString();
			System.out.println(time);
			w.append(time);
			w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
