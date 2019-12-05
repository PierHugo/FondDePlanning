package com.polytech.planning.controller;

import com.polytech.planning.model.Planning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GeneratePlanning
{
    private String filePathMockUp;
    private String filePathCalender;
    private String year;

    public GeneratePlanning()
    {

    }

    /**
     * @param year
     * @param filePathMockUp
     * @param filePathCalender
     */
    public GeneratePlanning(String year, String filePathMockUp, String filePathCalender)
    {
        this.filePathMockUp = filePathMockUp;
        this.filePathCalender = filePathCalender;
        this.year = year;
    }

    /**
     * read data for planning by year.
     *
     * @return list of planning named by year
     */
    private LinkedHashMap<String, List<Planning>> setPlannings(String year) throws IOException
    {
        LinkedHashMap<String, List<Planning>> plannings = new LinkedHashMap<String, List<Planning>>();
        System.out.println("Reading Calendar...");
        ReadCalendar readCalendar = new ReadCalendar(this.filePathCalender);
        ReadMockUp readMockUp = null;
        ParserMockUp pMockUp;
        ParserCalendar pCalendar;
        Planning planning;
        List<Planning> planningDI3 = new ArrayList<Planning>();
        List<Planning> planningDI4 = new ArrayList<Planning>();
        List<Planning> planningDI5 = new ArrayList<Planning>();

        readCalendar.readSemesters(0);
        readCalendar.readHolidays(1);
        readCalendar.readFreeDays(2);
        pCalendar = new ParserCalendar(readCalendar.getCalendar());
        System.out.println("Reading MockUp...");
        if (year.equals("DI3"))
        {
            // DI3 S5
            readMockUp = new ReadMockUp(this.filePathMockUp, 1);
            readMockUp.readTeachingUnits();


            System.out.println("Parsing data...");
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S5");
            planningDI3.add(planning);

            // DI3 S6
            readMockUp = new ReadMockUp(this.filePathMockUp, 2);
            readMockUp.readTeachingUnits();
            // TODO : add method call to check all lines
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S6");
            // TODO : Check si l'oubli de PlanningDI3.add(planning) est normal
            //planningDI3.add(planning);

            planningDI3.add(planning);
            plannings.put("DI3", planningDI3);
        } else if (year.equals("DI4"))
        {
            // DI4 S7
            readMockUp = new ReadMockUp(this.filePathMockUp, 3);
            readMockUp.readTeachingUnits();
            // TODO : add method call to check all lines
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S7");
            planningDI4.add(planning);

            // DI4 S8
            readMockUp = new ReadMockUp(this.filePathMockUp, 4);
            readMockUp.readTeachingUnits();
            // TODO : add method call to check all lines
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S8");
            planningDI4.add(planning);

            plannings.put("DI4", planningDI4);
        } else if (year.equals("DI5"))
        {
            // DI5 S9
            readMockUp = new ReadMockUp(this.filePathMockUp, 5);
            readMockUp.readTeachingUnits();
            // TODO : add method call to check all lines
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S9");
            planningDI5.add(planning);

            // DI5 S10
            readMockUp = new ReadMockUp(this.filePathMockUp, 6);
            readMockUp.readTeachingUnits();
            // TODO : add method call to check all lines
            pMockUp = new ParserMockUp(readMockUp.getTeachingUnits());
            planning = new Planning(this.year, pMockUp.createTeachingUnits(), pCalendar.createCalendar());
            planning.getCalendar().setName("S10");
            planningDI5.add(planning);

            plannings.put("DI5", planningDI5);
        }

        return plannings;
    }

    /**
     * offer list of planning that you want to generate
     *
     * @param year
     * @return list of planning
     */
    public List<Planning> getPlanningByYear(String year) throws IOException
    {
        LinkedHashMap<String, List<Planning>> plannings = this.setPlannings(year);
        return plannings.get(year);
    }

}
