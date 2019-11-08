package com.polytech.planning.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.bluecast.io.FileFormatException;
import com.polytech.planning.model.AutomateUtil;
import com.polytech.planning.model.exception.AutomateException;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import com.polytech.planning.model.OriginalCourse;

public class ReadMockUp extends ReadFile {

    private LinkedHashMap<String, List<OriginalCourse>> teachingUnits;
    private int sheetNum;
    private int rowNum;

    private int colTitleTU;
    private int colCourseName;
    private int colCM;
    private int colTD;
    private int colTP;
    private int colProject;
    private int colMundus;
    private int colCC;
    private int colCT;
    private int colTeachers;

    /**
     * Constructor
     *
     * @param filePath
     * @param sheetNum
     */
    public ReadMockUp(String filePath, int sheetNum) throws IOException {
        super(filePath);
        String searchString = "Unité d'enseignement";

        try {

            int[] coordinates = searchContent(sheetNum, searchString, false);

            if (coordinates[0] == -1 || coordinates[1] == -1) {
                try {
                    throw new FileFormatException("Le formats du fichier " + filePath
                            + " n'est pas valide. Il manque le terme 'Unité d'enseignement'.");
                } catch (FileFormatException e) {
                    e.printStackTrace();
                }
            }

            this.sheetNum = sheetNum;
            this.rowNum = coordinates[0];

            this.colCourseName = coordinates[1];
            this.colTitleTU = coordinates[1] - 1;

            getColNumbers(filePath, sheetNum);

            teachingUnits = new LinkedHashMap<String, List<OriginalCourse>>();
        } catch (Exception e1) {
            //e1.printStackTrace(); //dev
            System.out.println(e1.getMessage());
        }
    }

    /**
     * @param filePath
     * @param sheetNum
     * @throws FileFormatException
     */
    private void getColNumbers(String filePath, int sheetNum) throws FileFormatException, Exception {
        String sheetName = null;
        try {
            sheetName = getSheetName(sheetNum);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sheetName != null) {
            this.colCM = searchContent(sheetNum, "Cours", false)[1];
            if (this.colCM == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'Cours'.");
            }

            this.colTD = searchContent(sheetNum, "TD", false)[1];
            if (this.colTD == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'TD'.");
            }

            this.colTP = searchContent(sheetNum, "TP", false)[1];
            if (this.colTP == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'TP'.");
            }

            this.colCC = searchContent(sheetNum, "CC", false)[1];
            if (this.colCC == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'CC'.");
            }

            this.colCT = searchContent(sheetNum, "CT", false)[1];
            if (this.colCT == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'CT'.");
            }

            this.colTeachers = searchContent(sheetNum, "Affectation enseignement et responsabilité UE", true)[1];
            if (this.colTeachers == -1) {
                throw new FileFormatException("Le formats du fichier " + filePath + " n'est pas valide sur l'onglet "
                        + sheetName + ". Il manque le terme 'Affectation enseignement et responsabilité UE'.");
            }

            if (sheetNum == 1 || sheetNum == 2) { // Mundus uniquement pour 3A
                this.colMundus = searchContent(sheetNum, "Mundus", false)[1];
                /* if (this.colMundus == -1) {
                    throw new FileFormatException("Le formats du fichier " + filePath
                            + " n'est pas valide sur l'onglet " + sheetName + ". Il manque le terme 'Mundus'.");
                }*/
            }

            if (sheetNum != 5 && sheetNum != 6) { // Pas de projet pour la 5A
                this.colProject = searchContent(sheetNum, "Projet", false)[1];
                if (this.colProject == -1) {
                    System.out.println(sheetNum);
                    throw new FileFormatException("Le formats du fichier " + filePath
                            + " n'est pas valide sur l'onglet " + sheetName + ". Il manque le terme 'Projet'.");
                }
            }
        }
    }

    /**
     * Method to read all teaching units
     */
    public void readTeachingUnits() {
        boolean notFinish = true;
        int nbLoop = 0;

        while (notFinish) {

            if ((cellIsEmpty(rowNum, colCourseName, sheetNum) || cellIsNumeric(rowNum, colCourseName, sheetNum))
                    && (cellIsEmpty(rowNum, colTitleTU, sheetNum) || cellIsNumeric(rowNum, colTitleTU, sheetNum))) {

                nbLoop++;
                rowNum++;

                if (nbLoop > 1)
                    notFinish = false;
            } else {
                nbLoop = 0;
                readTeachingUnit();
            }
        }
    }

    /**
     * Method to read one Teaching Unit and add it in teachingUnits LinkedHashMap
     */
    private void readTeachingUnit() {
        String name = null;
        List<OriginalCourse> listCourses;

        rowNum++;

        if (cellIsEmpty(rowNum, colTitleTU, sheetNum) || cellIsNumeric(rowNum, colTitleTU, sheetNum)) {
            while (cellIsEmpty(rowNum, colCourseName, sheetNum) || cellIsNumeric(rowNum, colCourseName, sheetNum)) {
                rowNum++;
            }
        }

        name = readCell(rowNum - 1, colTitleTU, sheetNum);
        name = ToolBox.capitalize(name);

        try {
            listCourses = readCourses();
            teachingUnits.put(name, listCourses);
        } catch (InvalidValue e) {
            e.printStackTrace();
        } catch (AutomateException e) {
            System.err.println(e.getMessage());
        }

    }

    private void checkTeachingUnits(String teachers, int i) throws AutomateException {
        if (!teachers.equals("")) {
            AutomateUtil automateUtil = new AutomateUtil(teachers, sheetNum, rowNum);
            automateUtil.isCorrect();
        }
    }

    /**
     * Method to read one course
     *
     * @return one OriginalCourse object
     */
    private OriginalCourse readCourse() throws AutomateException {
        OriginalCourse buffer = new OriginalCourse();
        String mundus = "Mundus";
        String readMundus;

        mundus = normalizeText(mundus);

        buffer.setCourseName(readCell(rowNum, colCourseName, sheetNum)); // C -> name

        buffer.setHoursCM(readNumericCell(rowNum, colCM, sheetNum)); // D -> CM
        buffer.setHoursTD(readNumericCell(rowNum, colTD, sheetNum)); // E -> TD
        buffer.setHoursTP(readNumericCell(rowNum, colTP, sheetNum)); // F -> TP

        if (colProject != -1) {
            buffer.setHoursProject(readNumericCell(rowNum, colProject, sheetNum)); // G -> Project
        }

        if (colCC != -1) {
            if ((cellIsNumeric(rowNum, colCC, sheetNum) && readNumericCell(rowNum, colCC, sheetNum) != 0)
                    || cellIsString(rowNum, colCC, sheetNum) || cellIsEmpty(rowNum, colCC, sheetNum)) {
                buffer.setCc(true);
            }
        }

        if (colCT != -1) {
            if ((cellIsNumeric(rowNum, colCT, sheetNum) && readNumericCell(rowNum, colCT, sheetNum) != 0)
                    || cellIsString(rowNum, colCT, sheetNum) || cellIsEmpty(rowNum, colCT, sheetNum)) {
                buffer.setCt(true);
            }
        }

        if (colMundus != -1) {
            readMundus = readCell(rowNum, colMundus, sheetNum); // M -> Mundus

            readMundus = normalizeText(readMundus);
            if (readMundus != null) {
                if (readMundus.equals(mundus))
                    buffer.setMundus(true);
            }
        }

        buffer.setTeachers(readCell(rowNum, colTeachers, sheetNum)); // N -> Teachers

        try {
            checkTeachingUnits(buffer.getTeachers(), 1);
        } catch (AutomateException e) {
            throw e;
        }
        return buffer;
    }

    /**
     * Method to read all courses in a Teaching Unit
     *
     * @return a list of courses
     * @throws InvalidValue
     */
    public List<OriginalCourse> readCourses() throws InvalidValue, AutomateException {
        int blankLines = 0;
        List<OriginalCourse> courses = new ArrayList<OriginalCourse>();

        if (rowNum >= 0 && colCourseName >= 0) {
            while (blankLines < 1) {
                if (cellIsEmpty(rowNum, colCourseName, sheetNum)) {
                    blankLines++;
                } else if (cellIsNumeric(rowNum, colCourseName, sheetNum)) {
                    blankLines++;
                } else {
                    blankLines = 0;
                    courses.add(readCourse());
                }
                rowNum++;
            }
        } else {
            throw new InvalidValue("Les coordonnées ne peuvent pas être inferieurs à 0");
        }
        return courses;
    }

    /**
     * Method to get the list of teaching units
     *
     * @return the list of teaching units in a LinkedHashMap with the name of the
     * teaching units and the list of courses present in this teaching unit.
     */
    public LinkedHashMap<String, List<OriginalCourse>> getTeachingUnits() {
        return this.teachingUnits;
    }
}
