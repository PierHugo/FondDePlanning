package com.polytech.planning.view.handler;

import com.polytech.planning.controller.GeneratePlanning;
import com.polytech.planning.controller.WritePlanning;

import java.io.IOException;
import java.text.ParseException;

/**
 * This class is used for handle the actions of MainView
 */
public class MainViewHandler
{
    private String[] command;

    /**
     * excute commands
     *
     * @param command
     * @throws Exception
     */
    public void excuteCommands(String[] command) throws IOException, ParseException
    {
        this.command = command;
        this.parseCommand();
    }

    /**
     * planning-generator <school_year> <-di3|-di4|-di5|-all> <path_of_maquette>
     * <path_of_calendar>
     *
     * @throws Exception
     */
    private void parseCommand() throws IllegalArgumentException, NullPointerException, IOException, ParseException
    {
        String file_name;
        String year;
        String maquettePath;
        String calendarPath;
        String schoolYear;
        String[] schoolYearTable;
        if (command.length < 4)
        {    // Not adapted to future adds but work correctly now
            if (command.length == 1 && command[0].equals("-h"))
            {
                this.help();
            } else
            {
                throw new IllegalArgumentException("Command error! Please check. You can enter 'java -jar planning-generator.jar -h' to get help.");
            }
        } else
        {
            schoolYear = command[0];
            year = command[1];
            maquettePath = command[2];
            calendarPath = command[3];
            schoolYearTable = schoolYear.split("/");
            switch (year)
            {
                case ("-di3"):
                    file_name = "Planning Année " + "3 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI3", maquettePath, calendarPath, file_name);
                    break;
                case ("-di3m"):
                    file_name = "Planning Année " + "3 DI Mundus " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI3M", maquettePath, calendarPath, file_name);
                    break;
                case ("-di4"):
                    file_name = "Planning Année " + "4 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI4", maquettePath, calendarPath, file_name);
                    break;
                case ("-di5"):
                    file_name = "Planning Année " + "5 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI5", maquettePath, calendarPath, file_name);
                    break;
                case ("-all"):
                    file_name = "Planning Année " + "3 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI3", maquettePath, calendarPath, file_name);
                    file_name = "Planning Année " + "4 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI4", maquettePath, calendarPath, file_name);
                    file_name = "Planning Année " + "5 DI " + schoolYearTable[0] + " - " + schoolYearTable[1] + ".xlsx";
                    this.excuteGenerateCommand(schoolYear, "DI5", maquettePath, calendarPath, file_name);
                    break;
                default:
                    throw new IllegalArgumentException("Command error! Please check");
            }
        }
    }

    /**
     * excute the command for generating the planning
     *
     * @param school_year
     * @param annee
     * @param maquette_path
     * @param calendar_path
     * @param file_name
     */

    private void excuteGenerateCommand(String school_year, String annee, String maquette_path, String calendar_path,
                                       String file_name) throws NullPointerException, IOException, ParseException
    {
        WritePlanning wp = null;
        GeneratePlanning gp = null;

        gp = new GeneratePlanning(school_year, maquette_path, calendar_path);
        wp = new WritePlanning(gp.getPlanningByYear(annee), annee, file_name);

        wp.createFile();
        System.out.println("Creating <" + file_name + "> finished");
    }

    /**
     * offer help information for user
     */
    private void help()
    {
        System.out.println("------------------------------------------------------------");
        System.out.println("The format of command is:");
        System.out.println(
                "java -jar planning-generator.jar <school_year> <-di3|-di3m|-di4|-di5|-all> <path_of_maquette> <path_of_calendar>");
        System.out.println("------------------------------------------------------------");
        System.out.println("Explication:");
        System.out.println("\t <school_year> : format-> yearStart/yearEnd");
        System.out.println("\t\t Obligatory, the school year of the planning. eg. 2017/2018");
        System.out.println("\t");
        System.out.println("\t <-di3|-di4|-di5|-all> :");
        System.out.println("\t\t Obligatory, the year of the planning you want to generate.");
        System.out.println("\t\t\t -di3: for DI3");
        System.out.println("\t\t\t -di3m: for DI3 Mundus");
        System.out.println("\t\t\t -di4: for DI4");
        System.out.println("\t\t\t -di5: for DI5");
        System.out.println("\t\t\t -all: for all three years");
        System.out.println("\t");
        System.out.println("\t <path_of_maquette> :");
        System.out.println("\t\t Obligatory, the file path of the maquette");
        System.out.println("\t");
        System.out.println("\t <path_of_calendar> :");
        System.out.println("\t\t Obligatory, the file path of the calendar");
        System.out.println("\t");
        System.out.println("Note: the planning will be genereted at the same place with planning-generator.jar\n"
                + "The name of the file will be 'Planning Année <year> DI <school_year_start> - <school_year_end>.xlsx'");
        System.out.println("\t");
        System.out.println("\t");
    }
}
