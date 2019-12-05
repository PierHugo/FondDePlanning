package com.polytech.planning.view;

import com.polytech.planning.controller.ReadFile;
import com.polytech.planning.view.handler.MainViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

public class MainView
{

    private static final Logger LOG = LoggerFactory.getLogger(ReadFile.class);
    private String[] command;

    /**
     * @param command
     */
    public MainView(String[] command)
    {
        this.command = command;
    }

    public void createMainView() throws IOException, ParseException
    {
        System.out.println("**************** Planning Generator ****************");
        LOG.info("**************** Planning Generator ****************");
        MainViewHandler mainViewHandler = new MainViewHandler();
        System.out.println("Executing command...");
        LOG.info("Executing command...");
        mainViewHandler.excuteCommands(this.command);
    }
}
