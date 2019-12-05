package com.polytech.planning;

import com.polytech.planning.controller.ReadMockUp;
import com.polytech.planning.model.OriginalCourse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadMockUpTest
{
    private static ReadMockUp readMockUp;
    private static List<OriginalCourse> listCoursesExpected;

    @BeforeClass
    public static void init() throws IOException
    {
        readMockUp = new ReadMockUp("Maquette.xlsx", 1);
        OriginalCourse course = new OriginalCourse("Conception et utilisation de bases de donnée", 16.0, 20.0, 28.0,
                0.0,
                "  C.Tacquard 16hCM, 20hTD x3gr + Mundus, 16hTP x3gr + Mundus  ; M. Martineau 12hTP x3gr + Mundus  ",
                true, true, false);
        listCoursesExpected = new ArrayList<OriginalCourse>();
        listCoursesExpected.add(course);
    }

    @Test
    public void readTeachingUnits()
    {
        readMockUp.readTeachingUnits();
        List<OriginalCourse> listCourses = readMockUp.getTeachingUnits()
                .get("Conception et utilisation de bases de donnees");

        System.out.println(readMockUp.getTeachingUnits().toString());
        Assert.assertTrue(readMockUp.getTeachingUnits().size() == 7);

        System.out.println(listCoursesExpected.get(0).equals(listCourses.get(0)));
    }
}
