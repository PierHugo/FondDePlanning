package com.polytech.planning.controller;

import com.polytech.planning.model.Course;
import com.polytech.planning.model.Holiday;
import com.polytech.planning.model.TeachingUnit;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public final class ToolBox
{

    public static String listToString(List<?> list)
    {
        String result = "";
        for (int i = 0; i < list.size(); i++)
        {
            result += "" + list.get(i);
        }
        return result;
    }

    public static String capitalize(String input)
    {
        input = input.toLowerCase();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }

    public static String getColLetter(int num) throws InvalidValue
    {
        String result = "";

        if (num == 0)
        {
            return "A";
        } else if (num > 0)
        {
            while (num > 0)
            {
                int remainder = num % 26;
                System.out.println(remainder);
                char digit = (char) (remainder + 65);
                result = digit + result;
                num = (num - remainder) / 26;
            }
        } else
        {
            throw new InvalidValue();
        }

        return result;
    }

    /**
     * get list of date between begin and end
     *
     * @param begin
     * @param end
     * @return list of date
     */
    public static LinkedHashMap<Integer, String> getBetweenDates(Date begin, Date end)
    {
        LinkedHashMap<Integer, String> result = new LinkedHashMap<Integer, String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy");
        while (begin.getTime() <= end.getTime())
        {
            result.put(tempStart.get(Calendar.WEEK_OF_YEAR), sdf.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
            begin = tempStart.getTime();
        }
        return result;
    }

    /**
     * test if freeday is in this week
     */
    public static boolean freeDayInWeek(int weekNum, Date date)
    {
        LinkedHashMap<Integer, String> result = new LinkedHashMap<Integer, String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(date);
        if (tempStart.get(Calendar.WEEK_OF_YEAR) == weekNum)
        {
            return true;
        }
        return false;
    }

    /**
     * test a date is a holiday or not
     *
     * @param string
     * @param holiday
     * @return boolean
     */
    public static boolean isHoliday(String string, List<Holiday> holiday) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy");
        Date date;
        date = sdf.parse(string);

        for (Holiday h : holiday)
        {
            // System.out.println("date " + date);
            // System.out.println("date " + date.getTime());
            // System.out.println("holiday " + h.getStartDate().getTime());
            if (date.getTime() >= h.getStartDate().getTime() && date.getTime() < h.getEndDate().getTime())
            {
                return true;
            }
        }
        return false;

    }

    /**
     * convert from numberIndex to excel format index like AC...
     *
     * @param columnIndex
     * @return string of column in Excel
     */
    public static String excelColIndexToStr(int columnIndex)
    {
        if (columnIndex <= 0)
        {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do
        {
            if (columnStr.length() > 0)
            {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }

    /**
     * check a course is ASR or SI
     *
     * @param teachingUnits
     */
    public static void checkCourseType(List<TeachingUnit> teachingUnits)
    {
        for (TeachingUnit tu : teachingUnits)
        {
            if (tu.getName().toUpperCase().matches(".*PARCOURS ASR.*"))
            {
                for (Course c : tu.getListCourses())
                {
                    c.setType("ASR");
                }
            } else if (tu.getName().toUpperCase().matches(".*PARCOURS SI.*"))
            {
                for (Course c : tu.getListCourses())
                {
                    c.setType("SI");
                }
            } else if (tu.getName().toUpperCase().matches(".*PARCOURS IA.*"))
            {
                for (Course c : tu.getListCourses())
                {
                    c.setType("IA");
                }
            } else
            {
                for (Course c : tu.getListCourses())
                {
                    c.setType("ALL");
                }
            }
        }
    }
}
