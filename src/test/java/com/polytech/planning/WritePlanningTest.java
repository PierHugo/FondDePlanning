package com.polytech.planning;

import com.polytech.planning.controller.GeneratePlanning;
import com.polytech.planning.controller.WritePlanning;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class WritePlanningTest
{
    private static WritePlanning wp;
    private static GeneratePlanning gp;

    @BeforeClass
    public static void init()
    {
        gp = new GeneratePlanning("2017/2018", "Maquette.xlsx", "Calendar.xlsx");
        try
        {
            wp = new WritePlanning(gp.getPlanningByYear("DI3"), "DI3", "TestWriteTeachingUnits.xlsx");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void testWriteTeachingUnit() throws IOException, ParseException
    {
        wp.createFile();
    }
}
