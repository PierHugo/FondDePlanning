package com.polytech.planning.controller;

import com.polytech.planning.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WritePlanning extends WriteFile
{

    private List<Planning> plannings;
    private Workbook workbook;
    private String year;
    private HashMap<String, Sheet> sheets;
    private int lastWritenRow; // derniere ligne ecrite
    private int[] lastTURow;// 0=> last row of teaching unit in sheet1, 1=> ...
    private nameYear nameYear;
    private int[] numCol;
    private String numSemester;

    /**
     * Constructor
     *
     * @param filePath
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public WritePlanning(List<Planning> plannings, String year, String filePath)
    {
        super(filePath);
        this.lastWritenRow = 0;
        if (plannings == null || plannings.isEmpty())
            throw new IllegalStateException("The planning is empty. Please check it.");
        this.plannings = plannings;
        this.year = year;
        this.workbook = super.getWorkbook();
        this.sheets = new HashMap<String, Sheet>();
        this.lastTURow = new int[2];
        this.numCol = new int[2];
        this.nameYear = nameYear.valueOf(year);
    }

    /**
     * Create Planning in to excel file.
     */
    public void createFile() throws IOException, ParseException
    {
        System.out.println("Writing data into file...");
        Sheet para = this.workbook.createSheet("Paramétrage");
        sheets.put("Paramétrage", para);
        this.writeParametrage();
        int i = 0;
        for (Planning planning : this.plannings)
        {
            numSemester = planning.getCalendar().getName();
            Sheet sheet = this.workbook.createSheet("Planning " + planning.getCalendar().getName());
            sheets.put(planning.getCalendar().getName(), sheet);

            this.writeIntroPart(planning);
            this.lastTURow[i] = this.writeTeachingUnits(planning);
            this.writeWeeks(planning, i);
            this.writeEmptyCells(sheet, i);
            i++;

            sheet.createFreezePane(9,17); //fige des volets
            for (int iCol = 0; iCol <= 5; iCol++)
            {
                sheet.setColumnHidden(iCol, true); //cache les colonnes
            }
            sheet.setColumnHidden(1, false); //réaffiche celle des cours
        }
        FileOutputStream output;
        output = new FileOutputStream(super.getFilePath());
        this.workbook.write(output);
        output.close();

    }

    /**
     * write the first sheet "Paramétrage" of planning
     */
    private void writeParametrage()
    {
        Sheet sheet = sheets.get("Paramétrage");
        float numDays = 0, numSlots = 0, numGroupTD = 0, numGroupTP = 0, totalCM = 0, totalTD = 0, totalTP = 0;
        // first table
        Cell cell = super.writeStringCell(0, 1, sheet, "Semestre");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 2, sheet, "Nombre de jours disponibles");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 3, sheet, "Nombre de créneaux  par jour");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 4, sheet, "Nombre de groupes de TD");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 5, sheet, "Nombre de groupes de TP");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 6, sheet, "Volume horaire max / étudiant / semaine");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(0, 7, sheet, "Nombre de créneaux disponibles / semaine");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        StylesLib.setCellMerge(sheet, 1, 2, 0, 0);
        int yearNum = 3;
        switch (nameYear)
        {
            case DI3:
                yearNum = 3;
                numDays = 4.5f;
                numSlots = 4f;
                numGroupTD = 3;
                numGroupTP = 3;
                break;
            case DI4:
                yearNum = 4;
                numDays = 4.5f;
                numSlots = 4f;
                numGroupTD = 3;
                numGroupTP = 3;
                break;
            case DI5:
                yearNum = 5;
                numDays = 3;
                numSlots = 4;
                numGroupTD = 2;
                numGroupTP = 2;
                break;
        }
        cell = super.writeStringCell(1, 0, sheet, "Année " + yearNum);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(1, 1, sheet, plannings.get(0).getCalendar().getName());
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(1, 2, sheet, numDays);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(1, 3, sheet, numSlots);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(1, 4, sheet, numGroupTD);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(1, 5, sheet, numGroupTP);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeFormula("C2*D2*C5", 1, 6, sheet);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeFormula("C2*D2", 1, 7, sheet);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeStringCell(2, 1, sheet, plannings.get(1).getCalendar().getName());
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(2, 2, sheet, numDays);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(2, 3, sheet, numSlots);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(2, 4, sheet, numGroupTD);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeNumberCell(2, 5, sheet, numGroupTP);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeFormula("C3*D3*C5", 2, 6, sheet);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell = super.writeFormula("C3*D3", 2, 7, sheet);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        // second table
        StylesLib.setCellMerge(sheet, 4, 4, 0, 1);
        cell = super.writeStringCell(4, 0, sheet, "Durée d'un créneau (h)");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeNumberCell(4, 2, sheet, 2f);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        // third table
        if (this.year.equals("DI5"))
        {
            return;
        } else
        {
            cell = super.writeStringCell(6, 0, sheet, "Volume maquettes / étudiant (heures à planifier)");

            cell = super.writeStringCell(7, 3, sheet, "CM");
            cell.setCellStyle(StylesLib.cmStyle((XSSFWorkbook) workbook));
            cell = super.writeStringCell(7, 4, sheet, "TD");
            cell.setCellStyle(StylesLib.tdStyle((XSSFWorkbook) workbook));
            cell = super.writeStringCell(7, 5, sheet, "TP");
            cell.setCellStyle(StylesLib.tpStyle((XSSFWorkbook) workbook));
            cell = super.writeStringCell(7, 6, sheet, "Projet");
            cell.setCellStyle(StylesLib.ccStyle((XSSFWorkbook) workbook));
            cell = super.writeStringCell(7, 7, sheet, "Cumul heures / étudiant");

            StylesLib.setCellMerge(sheet, 8, 9, 0, 0);
            cell = super.writeStringCell(8, 0, sheet, "Année " + yearNum);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

            StylesLib.setCellMerge(sheet, 8, 8, 1, 2);
            cell = super.writeStringCell(8, 1, sheet, plannings.get(0).getCalendar().getName());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

            StylesLib.setCellMerge(sheet, 9, 9, 1, 2);
            cell = super.writeStringCell(9, 1, sheet, plannings.get(1).getCalendar().getName());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

            //if (this.year.equals("DI3") || this.year.equals("DI4")) { //Inutile ?
            // s5
            cell = super.writeNumberCell(8, 3, sheet, plannings.get(0).getTotalCM());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(8, 4, sheet, plannings.get(0).getTotalTD());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(8, 5, sheet, plannings.get(0).getTotalTP());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(8, 6, sheet, plannings.get(0).getTotalProject());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeFormula("SUM(D9:G9)", 8, 7, sheet);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            // s6
            cell = super.writeNumberCell(9, 3, sheet, plannings.get(1).getTotalCM());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(9, 4, sheet, plannings.get(1).getTotalTD());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(9, 5, sheet, plannings.get(1).getTotalTP());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(9, 6, sheet, plannings.get(1).getTotalProject());
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeFormula("SUM(D10:G10)", 9, 7, sheet);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            //}
        }
    }

    /**
     * write all teaching units into sheet
     *
     * @param planning
     * @return
     */
    public int writeTeachingUnits(Planning planning)
    {
        List<TeachingUnit> teachingUnits = planning.getTeachingUnits();
        ToolBox.checkCourseType(teachingUnits);
        int courseStartRow = lastWritenRow, teachingUnitStartRow = lastWritenRow;
        int lastRow;

        for (TeachingUnit teachingUnit : teachingUnits)
        {
            if (!teachingUnit.getName().matches("Stage.*") && !teachingUnit.getName().equals("0"))
            {
                lastRow = this.writeCourses(courseStartRow, sheets.get(planning.getCalendar().getName()), teachingUnit) - 1;
                // Row row = sheet.createRow(courseEndRow);
                if (teachingUnitStartRow < lastRow)
                {
                    StylesLib.setCellMerge(sheets.get(planning.getCalendar().getName()), teachingUnitStartRow, lastRow, 0, 0);
                }

                this.writeTeachingUnit(teachingUnitStartRow, sheets.get(planning.getCalendar().getName()),
                        teachingUnit.getName());
                teachingUnitStartRow = lastRow + 2;
                courseStartRow = teachingUnitStartRow;
                StylesLib.setCellMerge(sheets.get(planning.getCalendar().getName()), teachingUnitStartRow - 1,
                        teachingUnitStartRow - 1, 0, 9);
            }

        }

        return teachingUnitStartRow - 1;
    }

    /**
     * write courses of one teaching unit into a sheet
     *
     * @param courseStartRow
     * @param sheet
     * @param teachingUnit
     * @return
     */
    public int writeCourses(int courseStartRow, Sheet sheet, TeachingUnit teachingUnit)
    {
        int courseEndRow = courseStartRow;
        int nowRow, lastRow = courseStartRow;
        for (Course course : teachingUnit.getListCourses())
        {

            nowRow = this.writeTeachers(courseStartRow, sheet, course);

            courseEndRow = nowRow - 1;
            if (courseStartRow < courseEndRow)
            {
                StylesLib.setCellMerge(sheet, courseStartRow, courseEndRow, 1, 1);
            }

            this.writeCourse(courseStartRow, sheet, course.getName());
            courseStartRow = courseEndRow + 1;

            // test mundus
            if (course.isMundus())
            {
                nowRow = this.writeMundusTeachers(courseStartRow, sheet, course);
                courseEndRow = nowRow - 1;
                if (courseStartRow < courseEndRow)
                {
                    StylesLib.setCellMerge(sheet, courseStartRow, courseEndRow, 1, 1);
                }

                this.writeCourse(courseStartRow, sheet, course.getName() + "_mundus");
                courseStartRow = courseEndRow + 1;
            }

            lastRow = nowRow;
        }
        return lastRow;
    }

    /**
     * write MUNDUS course's teachers
     *
     * @param teacherStartRow
     * @param sheet
     * @param course
     * @return
     */
    private int writeMundusTeachers(int teacherStartRow, Sheet sheet, Course course)
    {
        int teacherEndRow = teacherStartRow;
        int lastRow = 0;
        int colCM = 10;
        if (this.numSemester.equals("S10"))
        {
            colCM = 7;
        }
        for (Teacher teacher : course.getListTeachers())
        {

            if (teacher.getTDMundus() != 0)
            {
                // Row row = sheet.createRow(teacherEndRow);
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "TD");
                cell.setCellStyle(StylesLib.tdStyle((XSSFWorkbook) workbook));

                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                this.writeHoursPut(teacherEndRow, sheet, teacher.getTDMundus());
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 0);
                teacherEndRow++;
                lastRow = teacherEndRow;

            }
            if (teacher.getTPMundus() != 0)
            {

                // Row row = sheet.createRow(teacherEndRow);
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "TP");
                cell.setCellStyle(StylesLib.tpStyle((XSSFWorkbook) workbook));

                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                this.writeHoursPut(teacherEndRow, sheet, teacher.getTPMundus());
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 0);
                teacherEndRow++;
                lastRow = teacherEndRow;

            }

            if (teacherStartRow < teacherEndRow - 1)
            {
                StylesLib.setCellMerge(sheet, teacherStartRow, teacherEndRow - 1, 2, 2);
                // StylesLib.setCellMerge(sheet, teacherStartRow, teacherEndRow
                // - 1, 3, 3);
                // StylesLib.setCellMerge(sheet, teacherStartRow, teacherEndRow
                // - 1, 4, 4);
            }

            teacherStartRow = teacherEndRow;
        }
        return lastRow;
    }

    /**
     * write teachers for all courses (except for MUNDUS course)
     *
     * @param teacherStartRow
     * @param sheet
     * @param course
     * @return
     */
    private int writeTeachers(int teacherStartRow, Sheet sheet, Course course)
    {
        int teacherEndRow = teacherStartRow;
        int lastRow = 0;
        int colCM = 10;
        if (this.numSemester.equals("S10"))
        {
            colCM = 7;
        }
        for (Teacher teacher : course.getListTeachers())
        {

            if (teacher.getHoursCM() != 0)
            {
                // Row row = sheet.createRow(teacherEndRow);
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "CM");
                cell.setCellStyle(StylesLib.cmStyle((XSSFWorkbook) workbook));
                this.writeHoursPut(teacherEndRow, sheet, teacher.getHoursCM());
                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                if (this.year.equalsIgnoreCase("DI3"))
                {
                    if (course.isMundus())
                    {
                        this.writeBooleanDI3(teacherEndRow, sheet, 2);
                    } else
                    {
                        this.writeBooleanDI3(teacherEndRow, sheet, 1);
                    }
                }
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
                lastRow = teacherEndRow;

            }

            if (teacher.getHoursTD() != 0)
            {

                // Row row = sheet.createRow(teacherEndRow);
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "TD");
                cell.setCellStyle(StylesLib.tdStyle((XSSFWorkbook) workbook));
                this.writeHoursPut(teacherEndRow, sheet, teacher.getHoursTD());
                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
                lastRow = teacherEndRow;

            }
            if (teacher.getHoursTP() != 0)
            {

                // Row row = sheet.createRow(teacherEndRow);
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "TP");

                cell.setCellStyle(StylesLib.tpStyle((XSSFWorkbook) workbook));

                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                this.writeHoursPut(teacherEndRow, sheet, teacher.getHoursTP());
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
                lastRow = teacherEndRow;

            }
            // il reste mundus

            if (teacher.getHoursCM() == 0 && teacher.getHoursTD() == 0 && teacher.getHoursTP() == 0
                    && teacher.getTDMundus() == 0 && teacher.getTPMundus() == 0)
            {

                // Row row = sheet.createRow(teacherEndRow);

                this.writeTeacher(teacherEndRow, sheet, teacher.getName());
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
                lastRow = teacherEndRow;
            }

            if (teacherStartRow < teacherEndRow - 1)
            {
                StylesLib.setCellMerge(sheet, teacherStartRow, teacherEndRow - 1, 2, 2);
            }
            teacherStartRow = teacherEndRow;
        }
        if (course.hasCt() || course.hasCc())
        {
            if (course.hasCt() && course.hasCc())
            {
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "CC/CT");
                cell.setCellStyle(StylesLib.ccStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 2, sheet, 2);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 3, sheet, 2);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;

            } else if (course.hasCt())
            {
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "CT");
                cell.setCellStyle(StylesLib.ccStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 2, sheet, 2);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 3, sheet, 0);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
            } else
            {
                Cell cell = super.writeStringCell(teacherEndRow, colCM, sheet, "CC");
                cell.setCellStyle(StylesLib.ccStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 2, sheet, 2);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                cell = super.writeNumberCell(teacherEndRow, colCM - 3, sheet, 0);
                cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                if (this.year.equalsIgnoreCase("DI3"))
                    this.writeBooleanDI3(teacherEndRow, sheet, 1);
                if (this.year.equalsIgnoreCase("DI4")
                        || this.year.equalsIgnoreCase("DI5") && !this.numSemester.equals("S10"))
                    this.writeBooleanDI4(teacherEndRow, sheet, course.getType());
                teacherEndRow++;
            }

            if (!course.getListTeachers().isEmpty())
            {
                this.writeTeacher(teacherEndRow - 1, sheet, course.getListTeachers().get(0).getName());
            }

            lastRow = teacherEndRow;
        }
        return lastRow;
    }

    /**
     * write column DI3 for planning of DI3
     *
     * @param row
     * @param sheet
     * @param type  0=>MUNDUS, 1=>DI3, 2=> ALL
     */
    private void writeBooleanDI3(int row, Sheet sheet, int type)
    {
        if (type == 0)
        {
            Cell cell = super.writeNumberCell(row, 3, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        } else if (type == 1)
        {
            Cell cell = super.writeNumberCell(row, 3, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        } else
        {
            Cell cell = super.writeNumberCell(row, 3, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        }

    }

    /**
     * write column ASR or SI or IA for DI4&DI5
     *
     * @param row
     * @param sheet
     * @param type
     */
    private void writeBooleanDI4(int row, Sheet sheet, String type)
    {
        if (type.equalsIgnoreCase("ASR"))
        {// middle ASR
            Cell cell = super.writeNumberCell(row, 3, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 5, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        } else if (type.equalsIgnoreCase("SI"))
        {// left SI
            Cell cell = super.writeNumberCell(row, 3, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 5, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        } else if (type.equalsIgnoreCase("IA"))
        {// right IA
            Cell cell = super.writeNumberCell(row, 3, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 0);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 5, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        } else
        {// all
            Cell cell = super.writeNumberCell(row, 3, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 4, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
            cell = super.writeNumberCell(row, 5, sheet, 1);
            cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        }

    }

    /**
     * write the introduction part of planning
     *
     * @param planning
     */
    private void writeIntroPart(Planning planning)
    {
        lastWritenRow = 2;
        String calName = planning.getCalendar().getName();
        Sheet sheet = sheets.get(calName);
        Cell cell = super.writeStringCell(lastWritenRow, 0, sheet, this.year + " " + calName);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        cell = super.writeFormula("TODAY()", lastWritenRow, 1, sheet);
        cell.setCellStyle(StylesLib.dateFormatStyle((XSSFWorkbook) workbook));

        lastWritenRow++;
        cell = super.writeStringCell(lastWritenRow, 0, sheet, planning.getYear());
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        lastWritenRow++;
        if (!this.numSemester.equals("S10"))
        {
            cell = super.writeStringCell(lastWritenRow, 2, sheet, "Disponibilité / étudiant (h)");
            cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

            lastWritenRow++;

            cell = super.writeStringCell(lastWritenRow, 2, sheet, "Créneaux disponibles");
            cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

            lastWritenRow++;

            cell = super.writeStringCell(lastWritenRow, 2, sheet, "Créneaux utilisés");
            cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

            lastWritenRow += 4;

            StylesLib.setCellMerge(sheet, lastWritenRow - 2, lastWritenRow + 3, 2, 2);

            cell = super.writeStringCell(lastWritenRow - 2, 2, sheet, "Synthèse volume travail / étudiant (h)");
            cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
            StylesLib.addBorderForMergedCell(sheet, lastWritenRow - 2, lastWritenRow + 3, 2, 2);

            // write two different total
            switch (nameYear)
            {

                case DI3:
                    // Mundus
                    StylesLib.setCellMerge(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow - 2, 6, sheet, "Total Tr. Com. + MUNDUS");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);

                    // DI3
                    StylesLib.setCellMerge(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow, 6, sheet, "Total Tr. Com. + DI3");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);
                    break;

                case DI4:
                    // SI
                    StylesLib.setCellMerge(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow - 2, 6, sheet, "Total Tr. Com. + ASR");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);

                    // ASR
                    StylesLib.setCellMerge(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow, 6, sheet, "Total Tr. Com. + SI");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);

                    // IA
                    StylesLib.setCellMerge(sheet, lastWritenRow + 2, lastWritenRow + 3, 6, 8);
                    cell = super.writeStringCell(lastWritenRow + 2, 6, sheet, "Total Tr. Com. + IA");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow + 2, lastWritenRow + 3, 6, 8);

                    break;

                case DI5:
                    // SI
                    StylesLib.setCellMerge(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow - 2, 6, sheet, "Total Tr. Com. + ASR");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow - 2, lastWritenRow - 1, 6, 8);

                    // ASR
                    StylesLib.setCellMerge(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);
                    cell = super.writeStringCell(lastWritenRow, 6, sheet, "Total Tr. Com. + SI");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow, lastWritenRow + 1, 6, 8);

                    // IA
                    StylesLib.setCellMerge(sheet, lastWritenRow + 2, lastWritenRow + 3, 6, 8);
                    cell = super.writeStringCell(lastWritenRow + 2, 6, sheet, "Total Tr. Com. + IA");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    StylesLib.addBorderForMergedCell(sheet, lastWritenRow + 2, lastWritenRow + 3, 6, 8);

                    break;

                default:
                    break;
            }
        }

        lastWritenRow += 5;

        cell = super.writeStringCell(lastWritenRow, 2, sheet, "N° semaine");
        cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

        lastWritenRow++;

        cell = super.writeStringCell(lastWritenRow, 2, sheet, "Date semaine");
        cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

        lastWritenRow += 2;

        StylesLib.setCellMerge(sheet, lastWritenRow, lastWritenRow, 6, 8);
        cell = super.writeStringCell(lastWritenRow, 6, sheet, "Heures à placer");
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        StylesLib.addBorderForMergedCell(sheet, lastWritenRow, lastWritenRow, 6, 8);
        lastWritenRow++;
        cell = super.writeStringCell(lastWritenRow, 7, sheet, "CC");
        cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
        cell = super.writeStringCell(lastWritenRow, 8, sheet, "CT");
        cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

        switch (nameYear)
        {

            case DI3:
                cell = super.writeStringCell(lastWritenRow, 3, sheet, "DI3");
                cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                cell = super.writeStringCell(lastWritenRow, 4, sheet, "MUNDUS");
                cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                break;

            case DI4:

                cell = super.writeStringCell(lastWritenRow, 3, sheet, "SI");
                cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                cell = super.writeStringCell(lastWritenRow, 4, sheet, "ASR");
                cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                cell = super.writeStringCell(lastWritenRow, 5, sheet, "IA");
                cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                break;

            case DI5:
                if (planning.getCalendar().getName().equals("S9"))
                {
                    cell = super.writeStringCell(lastWritenRow, 3, sheet, "SI");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                    cell = super.writeStringCell(lastWritenRow, 4, sheet, "ASR");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                    cell = super.writeStringCell(lastWritenRow, 5, sheet, "IA");
                    cell.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));

                    break;
                }
            default:
                break;
        }

        lastWritenRow++;
    }

    /**
     * write one teaching unit
     *
     * @param row
     * @param sheet
     * @param content
     */
    private void writeTeachingUnit(int row, Sheet sheet, String content)
    {
        Cell cell = super.writeStringCell(row, 0, sheet, content);
        StylesLib.columTitleWidth(sheet, 0);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
    }

    /**
     * write one course
     *
     * @param row
     * @param sheet
     * @param content
     */
    private void writeCourse(int row, Sheet sheet, String content)
    {
        Cell cell = super.writeStringCell(row, 1, sheet, content);
        StylesLib.columTitleWidth(sheet, 1);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
    }

    /**
     * write one teacher
     *
     * @param row
     * @param sheet
     * @param content
     */
    private void writeTeacher(int row, Sheet sheet, String content)
    {
        Cell cell = super.writeStringCell(row, 2, sheet, content);
        StylesLib.columTitleWidth(sheet, 2);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
    }

    /**
     * write hours to be put
     *
     * @param row
     * @param sheet
     * @param content
     */
    private void writeHoursPut(int row, Sheet sheet, double content)
    {
        int colHp = 6; //C'était ici pour décaler les heures à placer
        if (this.numSemester.equals("S10"))
        {
            colHp = 3;
        }
        Cell cell = super.writeNumberCell(row, colHp, sheet, (float) content);
        cell.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
    }

    /**
     * write weeks
     *
     * @param planning
     * @param numSemester
     */
    private void writeWeeks(Planning planning, int numSemester) throws ParseException
    {
        Calendar calendar = planning.getCalendar();
        Semester semester = calendar.getListSemester().get(numSemester);
        Date startDate = semester.getStartDate();
        Date endDate = semester.getEndDate();
        LinkedHashMap<Integer, String> dates = ToolBox.getBetweenDates(startDate, endDate);
        String calName = planning.getCalendar().getName();
        // no asr/si in s10
        int i = 11;
        if (this.numSemester.equals("S10"))
        {
            i = 9;
        }

        for (Integer key : dates.keySet())
        {

            StylesLib.setCellMerge(sheets.get(calName), lastWritenRow - 5, lastWritenRow - 5, i, i + 2);
            StylesLib.setCellMerge(sheets.get(calName), lastWritenRow - 4, lastWritenRow - 4, i, i + 2);
            // write weeks
            Cell cell1 = super.writeStringCell(lastWritenRow - 4, i, sheets.get(calName), dates.get(key));
            // write num of week
            Cell cell2 = super.writeNumberCell(lastWritenRow - 5, i, sheets.get(calName), key);

            if (!ToolBox.isHoliday(dates.get(key), semester.getListHoliday()))
            {
                // if not holiday
                if (!this.numSemester.equals("S10"))
                {
                    this.writeAvailable(calendar, i, sheets.get(calName), numSemester, key);
                    this.writeSummaryTitles(i, sheets.get(calName), numSemester);
                    // write summary MUNDUS/SI
                    this.writeSummary(i, sheets.get(calName), numSemester, 0);
                    // write summary DI3/ASR
                    this.writeSummary(i, sheets.get(calName), numSemester, 1);
                    // write summary IA
                    if (year.equals("DI4") || year.equals("DI5"))
                    {
                        this.writeSummary(i, sheets.get(calName), numSemester, 2);
                    }
                }
                // write cm,td,tp
                Cell cell3 = super.writeStringCell(lastWritenRow - 2, i, sheets.get(calName), "CM");
                Cell cell4 = super.writeStringCell(lastWritenRow - 2, i + 1, sheets.get(calName), "TD");
                Cell cell5 = super.writeStringCell(lastWritenRow - 2, i + 2, sheets.get(calName), "TP");
                cell3.setCellStyle(StylesLib.cmStyle((XSSFWorkbook) workbook));
                cell4.setCellStyle(StylesLib.tdStyle((XSSFWorkbook) workbook));
                cell5.setCellStyle(StylesLib.tpStyle((XSSFWorkbook) workbook));
                cell1.setCellStyle(StylesLib.dateStyle((XSSFWorkbook) workbook));
                cell2.setCellStyle(StylesLib.weekNumStyle((XSSFWorkbook) workbook));
            } else
            {
                cell1.setCellStyle(StylesLib.holidayStyle((XSSFWorkbook) workbook));
                cell2.setCellStyle(StylesLib.holidayStyle((XSSFWorkbook) workbook));
            }
            // set style
            sheets.get(calName).setColumnWidth(i, 60 * 20);
            sheets.get(calName).setColumnWidth(i + 1, 60 * 20);
            sheets.get(calName).setColumnWidth(i + 2, 60 * 20);
            i += 3;

            //
        }
        this.numCol[numSemester] = dates.size() * 3 + 11;

    }

    /**
     * write summary
     *
     * @param col
     * @param sheet
     * @param numSemester
     */
    private void writeSummaryTitles(int col, Sheet sheet, int numSemester)
    {
        String rowStrStart, rowStrEnd;
        String totalColStart, totalColEnd;
        int startRow = 0, line = 4;
        startRow = this.lastWritenRow - 4;
        // each part CM
        Cell cell1 = super.writeStringCell(startRow - 9, col, sheet, "CM");

        // each part TD
        Cell cell2 = super.writeStringCell(startRow - 9, col + 1, sheet, "TD");

        // each part TP
        Cell cell3 = super.writeStringCell(startRow - 9, col + 2, sheet, "TP");

        cell1.setCellStyle(StylesLib.cmStyle((XSSFWorkbook) workbook));
        cell2.setCellStyle(StylesLib.tdStyle((XSSFWorkbook) workbook));
        cell3.setCellStyle(StylesLib.tpStyle((XSSFWorkbook) workbook));

    }

    /**
     * write summary
     *
     * @param col
     * @param sheet
     * @param numSemester
     * @param type        0 => SI/MUNDUS, 1=>ASR/DI3 2=>IA
     */
    private void writeSummary(int col, Sheet sheet, int numSemester, int type)
    {
        String rowStrStart, rowStrEnd;
        String totalColStart, totalColEnd;
        int startRow = 0, line = 4;
        switch (type)
        {
            case 0:
                startRow = this.lastWritenRow - 4;
                line = 5;
                break;
            case 1:
                startRow = this.lastWritenRow - 2;
                line = 4;
                break;
            case 2:
                startRow = this.lastWritenRow;
                line = 6;
                break;
        }
        // total
        totalColStart = ToolBox.excelColIndexToStr(col + 1) + (startRow - 7);
        totalColEnd = ToolBox.excelColIndexToStr(col + 3) + (startRow - 7);
        StylesLib.setCellMerge(sheet, startRow - 7, startRow - 7, col, col + 2);
        Cell cell4_1 = super.writeFormula("SUM(" + totalColStart + ":" + totalColEnd + ")", startRow - 7, col, sheet);

        // generate variable CM
        rowStrStart = ToolBox.excelColIndexToStr(col + 1) + (lastWritenRow + 1);
        rowStrEnd = ToolBox.excelColIndexToStr(col + 1) + (this.lastTURow[numSemester] + 1);
        String array1 = rowStrStart + ":" + rowStrEnd;
        String array2_1 = ToolBox.excelColIndexToStr(line) + (lastWritenRow + 1) + ":"
                + ToolBox.excelColIndexToStr(line) + (this.lastTURow[numSemester] + 1);
        // write SUMPRODUCT()
        Cell cell1_1 = super.writeFormula("SUMPRODUCT(" + array1 + "," + array2_1 + ")", startRow - 8, col++, sheet);

        // generate variable TD
        rowStrStart = ToolBox.excelColIndexToStr(col + 1) + (lastWritenRow + 1);
        rowStrEnd = ToolBox.excelColIndexToStr(col + 1) + (this.lastTURow[numSemester] + 1);
        array1 = rowStrStart + ":" + rowStrEnd;
        array2_1 = ToolBox.excelColIndexToStr(line) + (lastWritenRow + 1) + ":" + ToolBox.excelColIndexToStr(line)
                + (this.lastTURow[numSemester] + 1);
        // write SUMPRODUCT()
        Cell cell2_1 = super.writeFormula("SUMPRODUCT(" + array1 + "," + array2_1 + ")", startRow - 8, col++, sheet);

        // generate variable TP
        rowStrStart = ToolBox.excelColIndexToStr(col + 1) + (lastWritenRow + 1);
        rowStrEnd = ToolBox.excelColIndexToStr(col + 1) + (this.lastTURow[numSemester] + 1);
        array1 = rowStrStart + ":" + rowStrEnd;
        array2_1 = ToolBox.excelColIndexToStr(line) + (lastWritenRow + 1) + ":" + ToolBox.excelColIndexToStr(line)
                + (this.lastTURow[numSemester] + 1);
        // write SUMPRODUCT()
        Cell cell3_1 = super.writeFormula("SUMPRODUCT(" + array1 + "," + array2_1 + ")", startRow - 8, col++, sheet);

        cell1_1.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell2_1.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell3_1.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell4_1.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));

        /*
         * "("+c1+"/Paramétrage!$C$5)+(IF(MOD(K10,Paramétrage!$C$5*Paramétrage!
         * $E$2 ),
         * (ROUNDDOWN(K10/(Paramétrage!$C$5*Paramétrage!$E$2),0)+1)*Paramétrage!
         * $E$2,
         * ROUNDDOWN(K10/(Paramétrage!$C$5*Paramétrage!$E$2)*Paramétrage!$E$2,0)
         * )) +(IF(MOD(L10,Paramétrage!$C$5*Paramétrage!$F$2),
         * (ROUNDDOWN(L10/(Paramétrage!$C$5*Paramétrage!$F$2),0)+1)*Paramétrage!
         * $E$2,
         * ROUNDDOWN(L10/(Paramétrage!$C$5*Paramétrage!$F$2)*Paramétrage!$F$2,0)
         * ))"
         */
    }

    /**
     * write available time
     *
     * @param numSemester
     */
    private void writeAvailable(Calendar calendar, int col, Sheet sheet, int numSemester, int weekNum)
    {
        Semester semester = calendar.getListSemester().get(numSemester);
        String rowStrStart, rowStrEnd;
        String freeDayName = "";
        int startRow = this.lastWritenRow - 16;
        String totalSlot = "Paramétrage!$H$2", avaiableSlot = "Paramétrage!$H$2";

        for (FreeDay fd : semester.getListFreeDays())
        {
            if (ToolBox.freeDayInWeek(weekNum, fd.getDate()))
            {
                avaiableSlot = avaiableSlot + "-" + fd.getTimeslot();
                freeDayName += fd.getName();
                StylesLib.setCellMerge(sheet, startRow - 2, startRow - 1, col, col + 2);
                Cell cellName = super.writeStringCell(startRow - 2, col, sheet, freeDayName);
                cellName.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
                StylesLib.addBorderForMergedCell(sheet, startRow - 2, startRow - 1, col, col + 2);
            }
        }

        StylesLib.setCellMerge(sheet, startRow, startRow, col, col + 2);
        StylesLib.setCellMerge(sheet, startRow + 1, startRow + 1, col, col + 2);
        StylesLib.setCellMerge(sheet, startRow + 2, startRow + 2, col, col + 2);

        rowStrStart = ToolBox.excelColIndexToStr(col + 1) + (startRow + 2);
        // line 3
        String c1, c2, c3;
        c1 = ToolBox.excelColIndexToStr(col + 1) + (startRow + 9);
        c2 = ToolBox.excelColIndexToStr(col + 2) + (startRow + 9);
        c3 = ToolBox.excelColIndexToStr(col + 3) + (startRow + 9);
        String forlume = "(" + c1 + "/Paramétrage!$C$5)+(IF(MOD(" + c2
                + ",Paramétrage!$C$5*Paramétrage!$E$2),(ROUNDDOWN(" + c2
                + "/(Paramétrage!$C$5*Paramétrage!$E$2),0)+1)*Paramétrage!$E$2,ROUNDDOWN(" + c2
                + "/(Paramétrage!$C$5*Paramétrage!$E$2)*Paramétrage!$E$2,0))) +(IF(MOD(" + c3
                + ",Paramétrage!$C$5*Paramétrage!$F$2),(ROUNDDOWN(" + c3
                + "/(Paramétrage!$C$5*Paramétrage!$F$2),0)+1)*Paramétrage!$E$2,ROUNDDOWN(" + c3
                + "/(Paramétrage!$C$5*Paramétrage!$F$2)*Paramétrage!$F$2,0)))";

        Cell cell1 = super.writeFormula(rowStrStart + "*Paramétrage!$C$5", startRow, col, sheet);
        Cell cell2 = super.writeFormula(avaiableSlot, startRow + 1, col, sheet);
        Cell cell3 = super.writeFormula(forlume, startRow + 2, col, sheet);

        cell1.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell2.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        cell3.setCellStyle(StylesLib.baseStyle((XSSFWorkbook) workbook));
        StylesLib.addBorderForMergedCell(sheet, startRow, startRow, col, col + 2);
        StylesLib.addBorderForMergedCell(sheet, startRow + 1, startRow + 1, col, col + 2);
        StylesLib.addBorderForMergedCell(sheet, startRow + 2, startRow + 2, col, col + 2);

    }

    /**
     * set style for cell that will be put hour
     *
     * @param sheet
     * @param numSemester
     */
    private void writeEmptyCells(Sheet sheet, int numSemester)
    {
        int startRow, endRow, startCol = 10, endCol;
        if (this.numSemester.equals("S10"))
        {
            startCol = 7;
        }
        String rowStr;
        startRow = this.lastWritenRow;
        endRow = this.lastTURow[numSemester] - 1;
        endCol = this.numCol[numSemester] - 1;
        Cell cellRow = null, cellCol = null;
        for (int i = startRow; i <= endRow; i++)
        {
            if (sheet.getRow(i) == null)
                continue;
            cellRow = sheet.getRow(i).getCell(startCol);
            if (cellRow == null)
            {
                continue;
            }
            if (cellRow.getCellTypeEnum() == CellType.STRING)
            {
                rowStr = cellRow.getRichStringCellValue().getString().trim().toUpperCase();
                for (int j = startCol + 1; j <= endCol; j++)
                {
                    Cell cellEmpty = sheet.getRow(i).createCell(j);
                    cellEmpty.setCellStyle(StylesLib.baseBorderStyle((XSSFWorkbook) workbook));
                    cellCol = sheet.getRow(startRow - 2).getCell(j);
                    if (cellCol == null)
                    {
                        cellEmpty.setCellStyle(StylesLib.holidayBorderStyle((XSSFWorkbook) workbook));
                    } else
                    {
                        String colStr = cellCol.getRichStringCellValue().getString().trim().toUpperCase();

                        if (rowStr.equals(colStr) && rowStr.equals("CM"))
                        {
                            cellEmpty.setCellStyle(StylesLib.cmBorderStyle((XSSFWorkbook) workbook));
                        }
                        if (rowStr.equals(colStr) && rowStr.equals("TD"))
                        {
                            cellEmpty.setCellStyle(StylesLib.tdBorderStyle((XSSFWorkbook) workbook));
                        }
                        if (rowStr.equals(colStr) && rowStr.equals("TP"))
                        {
                            cellEmpty.setCellStyle(StylesLib.tpBorderStyle((XSSFWorkbook) workbook));
                        }
                    }
                }
            }
        }
    }

    private enum nameYear
    {
        DI3, DI4, DI5;
    }
}
