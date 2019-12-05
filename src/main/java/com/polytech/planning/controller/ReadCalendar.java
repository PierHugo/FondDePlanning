package com.polytech.planning.controller;

import com.bluecast.io.FileFormatException;
import com.polytech.planning.model.OriginalCalendar;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.util.Date;

public class ReadCalendar extends ReadFile
{

    private OriginalCalendar calendar;
    private String filePath;

    /**
     * @param filePath The path of the excel file
     */
    public ReadCalendar(String filePath) throws IOException
    {
        super(filePath);
        this.filePath = filePath;
        calendar = new OriginalCalendar();
    }

    /**
     * @param sheetNum The number of the selected sheet
     * @param name     Name of the search semester
     * @return A HashMap<String, Date[]> of a Semester
     * @throws NameNotFoundException
     */
    private void readSemester(int sheetNum, String name) throws NameNotFoundException
    {
        String nameSemester;
        Date[] dates = new Date[2];
        int[] coordinate = searchContent(sheetNum, name, false);

        if (coordinate[0] != -1 || coordinate[1] != -1)
        {
            // Name
            nameSemester = readCell(coordinate[0], coordinate[1], sheetNum);

            // Date de début
            dates[0] = readCellDate(coordinate[0], coordinate[1] + 1, sheetNum);

            // Date de fin
            dates[1] = readCellDate(coordinate[0], coordinate[1] + 2, sheetNum);

            calendar.addSemesters(nameSemester, dates[0], dates[1]);

        } else
        {
            throw new NameNotFoundException(name + " : not found");
        }
    }

    /**
     * @param sheetNum The number of the selected sheet
     */
    public void readSemesters(int sheetNum)
    {
        String[] semesterNames = {"S5", "S6", "S7", "S8", "S9", "S10"};
        this.calendar.clearSemesters();

        for (String name : semesterNames)
        {
            try
            {
                readSemester(sheetNum, name);
            } catch (NameNotFoundException e)
            {
                e.printStackTrace();
                e.getMessage();
                break;
            }
        }
    }

    /**
     * @param sheetNum The number of the selected sheet
     * @return A HashMap<String, Date[]> of a Semester
     * @throws NameNotFoundException
     */
    private void readHoliday(int sheetNum, int rowNum, int colNum)
    {
        String nameHoliday;
        Date[] dates = new Date[2];

        nameHoliday = readCell(rowNum, colNum, sheetNum);
        dates[0] = readCellDate(rowNum, colNum + 1, sheetNum);
        dates[1] = readCellDate(rowNum, colNum + 2, sheetNum);

        this.calendar.addHolidays(nameHoliday, dates[0], dates[1]);
    }

    /**
     * @param sheetNum The number of the selected sheet
     */
    public void readHolidays(int sheetNum)
    {
        String searchString = "Nom";
        boolean emptyRow = false;
        this.calendar.clearHolidays();

        int[] coordonates = searchContent(sheetNum, searchString, false);

        if (coordonates[0] == -1 || coordonates[1] == -1)
        {
            try
            {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide au niveau des vacances.");
            } catch (FileFormatException e)
            {
                e.printStackTrace();
            }
        }

        int rowNum = 1 + coordonates[0];
        int colNum = coordonates[1];

        while (!emptyRow)
        {
            if (!rowIsEmpty(rowNum, sheetNum))
            {
                readHoliday(sheetNum, rowNum, colNum);
            } else
            {
                emptyRow = true;
            }
            rowNum++;
        }
    }

    private void readFreeDay(int sheetNum, int rowNum, int colNum)
    {
        String[] nameHoliday = new String[2];
        Date date;

        nameHoliday[0] = readCell(rowNum, colNum, sheetNum);
        date = readCellDate(rowNum, colNum + 1, sheetNum);
        nameHoliday[1] = readCell(rowNum, colNum + 2, sheetNum);

        this.calendar.addFreeDays(nameHoliday[0], date, nameHoliday[1]);
    }

    /**
     * @param sheetNum The number of the selected sheet
     */
    public void readFreeDays(int sheetNum)
    {
        String searchString = "Nom";
        boolean emptyRow = false;
        this.calendar.clearFreeDays();

        int[] coordonates = searchContent(sheetNum, searchString, false);

        if (coordonates[0] == -1 || coordonates[1] == -1)
        {
            try
            {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide au niveau des jours libres.");
            } catch (FileFormatException e)
            {
                e.printStackTrace();
            }
        }

        int rowNum = 1 + coordonates[0];
        int colNum = coordonates[1];

        while (!emptyRow)
        {
            if (!rowIsEmpty(rowNum, sheetNum))
            {
                readFreeDay(sheetNum, rowNum, colNum);
            } else
            {
                emptyRow = true;
            }
            rowNum++;
        }
    }

    /**
     * @return the calendar object
     */
    public OriginalCalendar getCalendar()
    {
        return calendar;
    }

}
