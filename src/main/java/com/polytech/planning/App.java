package com.polytech.planning;

import com.polytech.planning.controller.ReadFile;
import com.polytech.planning.view.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger LOG = LoggerFactory.getLogger(ReadFile.class);

	public static void main(String[] args) {
//		for(int i = 0;i<args.length;i++){
//			System.out.println(args[i]);
//		}
		
		
		try {
			MainView mainView= new MainView(args);
			mainView.createMainView();
		} catch (Exception e) {
			System.out.println("<!> ERR : " + e.getMessage());
			LOG.error(e.getMessage());
			for(StackTraceElement stackTrace : e.getStackTrace()) {
				LOG.error(stackTrace.toString());
			}
		}
		
	}
}
