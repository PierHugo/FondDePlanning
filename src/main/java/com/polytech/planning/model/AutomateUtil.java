package com.polytech.planning.model;

import com.polytech.planning.model.exception.AutomateException;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

/**
 * This class analyze a given chain according to a define syntax.
 */
public class AutomateUtil {

    /**
     * The chain to check
     */
    private String chain;
    /**
     * The sheet where is store the chain
     */
    private int sheet;
    /**
     * The row of the chain
     */
    private int row;
    /**
     * The school year
     */
    private int year;
    /**
     * Contains the chain split by ";" (correspcnd to the key)
     * then by "," (correspond to the entry list)
     */
    private Map<Integer, String[]> teacherCourse;
    /**
     * The current index of the verification for the map's key
     */
    private int mapKeyIndex;
    /**
     * The current index of the verification for the map's entries
     */
    private int mapEntryIndex;

    /**
     * The constructor.
     * @param chain chain to check
     * @param sheet the number of the current sheet
     * @param row the number of the current row
     */
    public AutomateUtil(String chain, int sheet, int row) {
        // si la chaîne est null, on la considère vide
        if(chain == null){
            chain = "";
        }
        // delete all the non usefull characters
        chain = chain.replace("\n", "");
        chain = chain.replace(" ", "");

        this.chain = chain;
        teacherCourse = chainToMap(chain);

        this.sheet = sheet;
        this.row = row;

        // start at the first substring
        mapKeyIndex = 0;
        mapEntryIndex = 0;

        // Base on the programme, each year have dedicaded sheets number
        switch (sheet) {
            case 1:
            case 2:
                year = 3;
                break;
            case 3:
            case 4:
                year = 4;
                break;
            case 5:
            case 6:
                year = 5;
                break;
        }
    }

    /**
     * Put the chain into the map teacherCourses
     * @param chain the chain to split
     * @return Map<Integer, String[]> the map get from the chain
     */
    private Map<Integer, String[]> chainToMap(String chain) {

        String teachersRow[] = chain.split(";");
        Map<Integer, String[]> map = new HashMap<Integer, String[]>();

        for (int i = 0; i < teachersRow.length; i++) {
            map.put(i, teachersRow[i].split(","));
        }

        return map;
    }

    /**
     * Check if the chain pass to the objet match the pattern
     * @return true if the chain match the pattern
     * @throws IllegalFormatException if the chain doesn't match the pattern
     */
    public boolean isCorrect() throws AutomateException {
        try {
            // On considère les chaines vides comme valides
            if(chain.equals(""))
                return true;
            state0();
            return true;
        } catch (AutomateException e) {
            System.err.println("La chaine ligne " + row + " de la feuille " + sheet +
                    " n'est pas conforme : " + chain);
            throw e;
        }
    }

    /**
     * Automate : 0->1->2
     * Check the teacher name of the first teacher
     * @throws AutomateException if the subchain doesn't match the pattern
     */
    private void state0() throws AutomateException {

        // We accept all symbol except numbers
        String teacherReg = "[^0-9]+";

        if (teacherCourse.get(0)[0].matches(teacherReg)) {
            if(mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                if(teacherCourse.size() != 1)
                {
                    throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Il manque des informations après le nom" +
                            " de ce professeur (il faut renseigner les cours s'il y a plus d'un professeur) : " + teacherCourse.get(0)[0]);
                }
                isEndofCourse();
            }else{
                mapEntryIndex++;
                state1();
            }
        } else {
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Il manque une virgule entre le nom du " +
                    "professeur et les heures de cours.\n Partie de la chaîne concernée : " + teacherCourse.get(0)[0]);
        }
    }

    /**
     * Search the pattern "x[0-9}+gr" or a number
     * @throws AutomateException if the subchain doesn't match the patterns or we don't reach the end after have found "x[0-9]+gr" pattern
     */
    private void state1() throws AutomateException {
        // We search for numbers
        String groupReg = "^x\\dgr$";
        // The subchain to check
        String chainToCheck = teacherCourse.get(mapKeyIndex)[mapEntryIndex];

        if (chainToCheck.matches(groupReg)) {
            // we found the pattern "x[0-9]+gr" so make sure this is the end of the chain
            // but first we make sure there's only one teacher in the chain
            if(teacherCourse.size() > 1){
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Seul un seul professeur doit être " +
                        "mentionné pour pouvoir utiliser la mention \"x<number>gr\":  " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
            }

            // Look if we reach the end of the chain
            if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                // End of the teacher's course
                if (mapKeyIndex == teacherCourse.size() - 1) {
                    // End of the chain
                    return;
                } else {
                    throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Ce doit être la fin de la chaîne ! " +
                            "Or d'autres éléments ont étés trouvés :  " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
                }
            } else {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Ce doit être la fin de la chaîne ! " +
                        "Or d'autres éléments ont étés trouvés :  " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
            }
        } else {
            // We may have a number

            String[] chainSplited = chainToCheck.split("h");

            if (chainSplited.length == 2) {
                // we have an h in the chain

                try {
                    // Get the integer which must be stored in the first subString
                    Integer.parseInt(chainSplited[0]);
                    // The "h" caracter as been remove so we need to put it back
                    teacherCourse.get(mapKeyIndex)[mapEntryIndex] = "h" + chainSplited[1];

                    state2();
                } catch (NumberFormatException e) {
                    throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre ou \"x[0-9]+gr\" sont attendus après le nom du " +
                            "professeur : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex], e);
                }
            } else {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre ou \"x[0-9]+3gr\" sont attendus après le nom du " +
                        "professeur : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
            }

        }
    }

    /**
     * Automate : 15->5 | 15->6->9[->25->22] | 15->7->8[->23->24]
     * Search the pattern hCM |hTDx[0-9]+gr(+Mundus) | hTPx[0-9]+gr(+Mundus)
     * @throws AutomateException if the sub-chain doesn't match one of the pattern
     */
    private void state2() throws AutomateException {
        // The subchain to check
        String chainToCheck = teacherCourse.get(mapKeyIndex)[mapEntryIndex];

        // the chain must be one of the 3 research or there's an error
        // search if its CMs
        if (chainToCheck.equals("hCM")) {
            // Look if we reach the end of the chain
            if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                // End of the teacher's course
                isEndofCourse();
            } else {
                // TD or TP are mentionned after
                mapEntryIndex++;
                state3();

            }
        } else {
            String tdReg = "^hTDx[0-9]+gr";
            String tpReg = "^hTPx[0-9]+gr";

            String[] splitChain = chainToCheck.split("\\+");

            // We make sure the Mundus are only mention in the 3rd Year and not in 4th and 5th
            if(splitChain.length >= 2 && year != 3){
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Les années 4 et 5 n'ont pas de Mundus dans leur promotion. :" +
                        chainToCheck);
            }
            else if(year == 3 && splitChain.length >= 2){
                // If we have Mundus, we add the pattern to the one to check
                tdReg += "\\+Mundus";
                tpReg += "\\+Mundus";
            }

            // Search the pattern for TD or TP
            if (chainToCheck.matches(tdReg)) {
                // we have TD hours
                //search if it's the last element
                // Look if we reach the end of the chain
                if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                    // End of the teacher's course
                    isEndofCourse();
                } else {
                    // Not the end continue
                    mapEntryIndex++;
                    state4();
                }
            } else isTpSyntax(chainToCheck, tpReg);
        }
    }

    /**
     * Automate : 5->12->13
     * Search of a number after the CM hours
     * @throws AutomateException if there isn't a number at the start of the subString
     */
    private void state3() throws AutomateException {

        String[] chainSplited = teacherCourse.get(mapKeyIndex)[mapEntryIndex].split("h");
        if (chainSplited.length == 2) {
            // We must have a number in the first subString
            try {
                Integer.parseInt(chainSplited[0]);
                teacherCourse.get(mapKeyIndex)[mapEntryIndex] = "h" + chainSplited[1];
                state5();
            } catch (NumberFormatException e) {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre est attendus après la mention du " +
                        "CM : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex], e);
            }
        } else {
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre est attendus après la mention du " +
                    "CM : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
        }
    }

    /**
     * Automate : 20->16
     * Search a number after TD
     * @throws AutomateException if there isn't a number at the start of the subchain
     */
    private void state4() throws AutomateException {
        String[] chainSplited = teacherCourse.get(mapKeyIndex)[mapEntryIndex].split("h");
        if (chainSplited.length == 2) {
            // We must have a number in the first subString
            try {
                Integer.parseInt(chainSplited[0]);
                teacherCourse.get(mapKeyIndex)[mapEntryIndex] = "h" + chainSplited[1];
                state6();
            } catch (NumberFormatException e) {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre est attendus après la mention du " +
                        "TD : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex], e);
            }
        } else {
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Un nombre est attendus après la mention du " +
                    "TD : " + teacherCourse.get(mapKeyIndex)[mapEntryIndex]);
        }
    }

    /**
     * Automate: 13->14->6 | 13->14->7
     * Search hTD | hTP after have found a CM
     * @throws AutomateException if the subChain doesn't match on the the patterns
     */
    private void state5() throws AutomateException {
        String chainToCheck = teacherCourse.get(mapKeyIndex)[mapEntryIndex];
        String tdReg = "^hTDx[0-9]+gr";
        String tpReg = "^hTPx[0-9]+gr";

        String[] splitChain = chainToCheck.split("\\+");

        // We make sure Mundus are only mentionned in 3rd Year and not 4th or 5th
        if(splitChain.length >= 2 && year != 3){
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Les années 4 et 5 n'ont pas de Mundus dans leur promotion. :" +
                    chainToCheck);
        }
        else if(year == 3 && splitChain.length >=2){
            // We add the Mundus mention to the patterns
            tdReg += "\\+Mundus";
            tpReg += "\\+Mundus";
        }

        if (chainToCheck.matches(tdReg)) {
            // we have TD hours
            //search if it's the last element
            // Look if we reach the end of the chain
            if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                // End of the teacher's course
                isEndofCourse();
            } else {
                // Not the end continue
                mapEntryIndex++;
                state4();
            }
        } else {
            isTpSyntax(chainToCheck, tpReg);
        }
    }

    /**
     * Automate : 17->6->9
     * Search for a TP after have found a TD
     */
    private void state6() throws AutomateException {
        String chainToCheck = teacherCourse.get(mapKeyIndex)[mapEntryIndex];
        String tpReg = "^hTPx[0-9]+gr";

        String[] splitChain = chainToCheck.split("\\+");

        if(splitChain.length >= 2 && year != 3){
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Les années 4 et 5 n'ont pas de Mundus dans leur promotion. :" +
                    chainToCheck);
        }
        else if (year == 3 && splitChain.length >= 2){
            tpReg += "\\+Mundus";
        }

        if(chainToCheck.matches(tpReg)){
            // we have TP hours
            // it's the end of the subchain
            // check if it's the end of the chain or a new teacher
            if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                // End of the teacher's course
                isEndofCourse();
            } else {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") " +
                        "Les TP doivent être le dernier élément renseigné dans cette partie de la chaîne : " + chainToCheck);
            }
        }
        else{
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Les horaires de TPs n'ont pas été trouvé, manque-t-il un ';' ?" +
                    chainToCheck);
        }
    }

    /**
     * Automate : 10->11->21
     * Search another teacher name
     * @throws AutomateException if the chain doesn't match the pattern
     */
    private void state7() throws AutomateException {
        // We accept all symbol except numbers
        String teacherReg = "[^0-9]+";

        if (teacherCourse.get(mapKeyIndex)[mapEntryIndex].matches(teacherReg)) {
            if(mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Il manque des informations après le nom" +
                        " de ce professeur (il faut renseigner les cours s'il y a plus d'un professeur) : " + teacherCourse.get(0)[0]);
            }else{
                mapEntryIndex++;
                state1();
            }
        } else {
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Il manque une virgule entre le nom du " +
                    "professeur et les heures de cours.\n Partie de la chaîne concernée : " + teacherCourse.get(0)[0]);
        }
    }

    /**
     * Search if there's a TP in the chain
     * @param chainToCheck chain where we search the TP
     * @param tpReg the pattern of a TP definition
     * @throws AutomateException if the pattern doesn't match the chain
     */
    private void isTpSyntax(String chainToCheck, String tpReg) throws AutomateException {
        if (chainToCheck.matches(tpReg)) {
            // we have TP hours
            // it's the end of the subchain
            // check if it's the end of the chain or a new teacher
            if (mapEntryIndex == teacherCourse.get(mapKeyIndex).length - 1) {
                // End of the teacher's course
                isEndofCourse();
            } else {
                throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") " +
                        "Les TP doivent être le dernier élément renseigné dans cette partie de la chaîne : " + chainToCheck);
            }
        } else {
            throw new AutomateException("[Error] (F:" + sheet + "r:" + row + ") Une \",\" est attendu entre les heures de CM, TD et TP et" +
                    " un \";\" entre les informations de deux professeurs ! : " + chainToCheck);
        }
    }

    /**
     * Search if it's the end of the course
     * @throws AutomateException
     */
    private void isEndofCourse() throws AutomateException {
        if (mapKeyIndex == teacherCourse.size() - 1) {
            // End of the chain
            return;
        } else {
            // Go to the next teacher
            mapKeyIndex++;
            mapEntryIndex = 0;
            state7();
        }
    }










}
