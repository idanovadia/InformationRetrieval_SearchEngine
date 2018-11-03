
import javax.print.Doc;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;

public class Parse {
    private HashSet<String> stopWords;
    private HashMap<String,Term> terms;
//    private HashMap<Term,HashMap<Document,Integer>> termsInfo;
    private Document doc;
    private int lineNumber;
    private int wordPosition;
    private int index;
    private String[] docText;
    private enum wordType {NUMBER, SYMBOL, WORD,NULL};

    public Parse(Document doc){
        this.stopWords=new HashSet<String>();
//        this.termsInfo=new HashMap<Term,HashMap<Document,Integer>>();
        this.doc=doc;
        this.lineNumber=0;
        this.wordPosition=0;
        this.index=0;
        this.terms=new HashMap<String,Term>();

    }

    /**
     * Parsing A document and filling termsInfo HashMap
     */
    public void ParseDoc(){
        String text=doc.getTEXT();
        text=text.replace("."+"\n"," ");
//        text=text.replace(System.lineSeparator()," ");
//        text=text.replace("\n"," ").replace("\r"," ");
        text=text.replace(", "," ");
        text=text.replace("\t"," ");
        docText=text.split(" ");
        startParse();
    }

    private void startParse() {
        for (index = 0; index < docText.length; index++) {
            //if it's a line seperator. increase line number
            if (docText[index].equals(System.lineSeparator())) {
                lineNumber++;
                continue;
            }
            //else if its a space increase position
            else if( docText[index].equals(" ")) {
                wordPosition++;
                continue;
            }

            else {

                wordType type = identifyDoc(docText[index]); // identifying the word

                if (type == wordType.NUMBER) {
                    parseNumber(docText[index], index);
                } else if (type == wordType.SYMBOL) {

                } else if (type == wordType.WORD) {

                }
            }
        }
    }

    public wordType identifyDoc(String str) {
        for (int i = 0; i < docText.length; i++) {
            try {
                if (isSymbol(str)) {
                    System.out.println("Symbol : " + docText[i]);
                    return wordType.SYMBOL;

                } else if (isNumber(str)) {
                    System.out.println("Number : " + docText[i]);
                    return wordType.NUMBER;

                }
            } catch (ParseException e) {
                System.out.println("Word : " + docText[i]);
                return wordType.WORD;


            }
        }
        return wordType.NULL;
    }


    public boolean isSymbol (String str) throws ParseException {

        ArrayList<Character> ch = new ArrayList<>();
        ch.add('$');
        if(ch.contains(str.charAt(0))) {
            NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            String substring = str.substring(1, str.length());
            Number number = format.parse(substring);
            return true;
        }
        return false;
    }

    private boolean isNumber(String str) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

        Number number = format.parse(str);
        return true;
    }
    private boolean isFraction(String str) {

        if (str.contains("/")) {
            String separator = "/";
            String[] new_str = str.split(Pattern.quote(separator));
            NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            try {
                Number first = format.parse(new_str[0]);
                Number second = format.parse(new_str[1]);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }


    /*
                     Starting Parsing functions
     */

    /**
     * Handles terms of type Number
     * @param str given String for term
     */
    public void parseNumber(String str, int index){
        Term tempTerm = new Term();
        ArrayList<String> first_keywords=getFirstKeyWords(); // An Array of listed keywords that can appear after a term
//        Term
        HashSet<String> toReturn = new HashSet<>();
        boolean fraction = str.contains("/");
        boolean fractionAndText = fraction && index + 1 < docText.length && first_keywords.contains(docText[index + 1]);  //???
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        Number number = null;
            try {
                number = format.parse(docText[index]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double number_term = number.doubleValue();
            //has a keyword after the word
            if (!fraction && index + 1 < docText.length && first_keywords.contains(docText[index + 1])) {
                //number + value + U.S + Dollar
                if (index + 2 < docText.length && docText[index + 2].equals("U.S.") && index + 3 < docText.length && docText[index + 3].equals("Dollars")) {
                    switch (docText[index + 1]) {
                        case "million":
                            break;
                        case "billion":
                            number_term = number_term * 1000;
                            break;
                        case "trillion":
                            number_term = number_term * 1000000;
                            break;
                    }
                    tempTerm.setName(convertDouble(number_term) + " " + "M" + " " + "Dollars");
//                    toReturn.add(convertDouble(number_term) + " " + "M" + " " + "Dollars");
                } else {
                    if (!fraction) {
                        switch (docText[index + 1]) {
                            case "Thousand":
                                number_term = number_term;
                                tempTerm.setName(convertDouble(number_term) + "K");
//                                toReturn.add(convertDouble(number_term) + "K");
                                break;
                            case "Million":
                                number_term = number_term;
                                tempTerm.setName(convertDouble(number_term) + "M");
//                                toReturn.add(convertDouble(number_term) + "M");
                                break;
                            case "Trillion":
                            case "Billion":
                                number_term = number_term;
                                tempTerm.setName(convertDouble(number_term) + "B");
//                                toReturn.add(convertDouble(number_term) + "B");
                                break;
                            case "percent":
                            case "percentage":
                                tempTerm.setName(convertDouble(number_term) + "%");
//                                toReturn.add(convertDouble(number_term) + "%");
                                break;
                            case "Dollars":
                                tempTerm.setName(transformNumber(number_term));
//                                toReturn.add(transformNumber(number_term));
                                break;

                        }
                    }


                }
            }
            //Fraction
            else if (index + 1 < docText.length && isFraction(docText[index + 1])) {
                if (number_term < 1000000 && index + 2 < docText.length && docText[index + 2].equals("Dollars")) {
                    tempTerm.setName(convertDouble(number_term) + " " + docText[index + 1] + " " + "Dollars");
//                    toReturn.add(convertDouble(number_term) + " " + docText[index + 1] + " " + "Dollars");
                    index = index++; //skip the next word in the document :O
//                    Parser.setIndex(index + 1);
                } else {
                    tempTerm.setName(convertDouble(number_term) + " " + docText[index + 1]);
//                    toReturn.add(convertDouble(number_term) + " " + docText[index + 1]);
                }

//                return toReturn; // ??? check why //TODO : Check why !

            } else if (fraction) {
                if (index + 1 < docText.length && first_keywords.contains(docText[index + 1])) {
                    tempTerm.setName(docText[index] + " " + docText[index + 1]);
//                    toReturn.add(docText[index] + " " + docText[index + 1]);
                } else {
                    tempTerm.setName(docText[index]);
//                    toReturn.add(docText[index]);
                }

            } else if (index + 1 < docText.length && months().containsKey(docText[index + 1])) {
                String month = "" + months().get(docText[index + 1]);
                if (months().get(docText[index + 1]) < 10) {
                    month = "0" + months().get(docText[index + 1]);
                }
                if (number_term < 10) {
                    tempTerm.setName(month + "-" + "0" + convertDouble(number_term));
//                    toReturn.add(month + "-" + "0" + convertDouble(number_term));
                } else {
                    tempTerm.setName(month + "-" + convertDouble(number_term));
//                    toReturn.add(month + "-" + convertDouble(number_term));
                }
            } else {
                if (!fraction) {
                    if (number_term >= 1000 && number_term < 1000000) {
                        tempTerm.setName(convertDouble(number_term / 1000) + "K");
//                        toReturn.add(convertDouble(number_term / 1000) + "K");
                    } else if (number_term >= 1000000 && number_term < 1000000000) {
                        tempTerm.setName(convertDouble(number_term / 1000000) + "M");
//                        toReturn.add(convertDouble(number_term / 1000000) + "M");
                    } else if (number_term >= 1000000000) {
                        tempTerm.setName(convertDouble(number_term / 1000000000) + "B");
//                        toReturn.add(convertDouble(number_term / 1000000000) + "B");
                    } else {
                        tempTerm.setName(convertDouble(number_term));
//                        toReturn.add(convertDouble(number_term));
                    }
                }

            }

            handleTerm(tempTerm); //
            int i =5;
        }

//    public HashMap<Term, HashMap<Document, Integer>> getTermsInfo() {
//        return termsInfo;
//    }

    private void handleTerm(Term toCheck) {
        /*
            if found term not avilable in the term list
                set doc frequency of the term to 1
                increase doc distnict term by 1
                add to terms
                set corpus frequency to 1
                set df to 1
                insert it to the terms with the document found.
                set doc frequency
                set term location in doc
         */
        if (terms.get(toCheck)==null) {
            toCheck.getDocFrequency().put(doc,1);
            doc.setDistinctwords(doc.getDistinctwords()+1);
            toCheck.setCorpusFrequency(toCheck.getCorpusFrequency()+1);
            terms.put(toCheck.getName(),toCheck);



        }
        // if found term already exists in the term list:
            /*
                Get the term from the list
                increase it's corpus frequency
                see if the term has current doc in it's docfreq hashmap
                    if not - add it, increase df,
                    if it does - increase the freq by 1, see if freq > max tf
                add the term back to the hashmap and overwrite the old term
             */
        else{
            Term UsedTerm = terms.get(toCheck.getName());
            UsedTerm.setCorpusFrequency(UsedTerm.getCorpusFrequency()+1);

            if(UsedTerm.getDocFrequency().get(doc)==null){
                UsedTerm.setDf(UsedTerm.getDf()+1);
                UsedTerm.getDocFrequency().put(doc,1);

            }
            else{
                UsedTerm.getDocFrequency().put(doc,UsedTerm.getDocFrequency().get(doc)+1);
                updateDocMaxTf(UsedTerm.getDocFrequency().get(doc));

            }


        }
    }





/*
                Begining of Utilities functions
 */
    public ArrayList<String> getFirstKeyWords(){
        ArrayList<String> keywords=new ArrayList<String>();
        keywords.add("Thousand");
        keywords.add("Million");
        keywords.add("Trillion");
        keywords.add("Billion");
        keywords.add("percent");
        keywords.add("percentage");
        keywords.add("Dollars");
        keywords.add("billion");
        keywords.add("million");
        keywords.add("trillion");
        return keywords;
    }
    public void updateDocMaxTf(int term_tf){
        if(doc.getMaxtf() < term_tf){
            doc.setMaxtf(term_tf);
        }
    }

    /**
     * Convert Double to String without leaving Zero Trails behind
     * @param d given double
     * @return formatted string
     */
    public String convertDouble(double d){
        String result=""+d;
        return result=result.indexOf(".") < 0 ? result : result.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    /**
     * Helper function to convert double to string with additional parameters
     * @param number given double
     * @return
     */
    public String transformNumber(double number) {
        if(number>=1000000){
            number=number/1000000;
            return convertDouble(number)+" "+"M"+" "+"Dollars";
        }
        return number+" "+"Dollars";
    }

    /**
     * List of all months by names and order
     * @return
     */
    public HashMap<String, Integer> months(){

        HashMap<String,Integer> parse_months = new HashMap<String,Integer>();
        parse_months.put("JAN",1);
        parse_months.put("Jan",1);
        parse_months.put("JANUARY",1);
        parse_months.put("January",1);
        parse_months.put("FEB",2);
        parse_months.put("Feb",2);
        parse_months.put("February",2);
        parse_months.put("FEBRUARY",2);
        parse_months.put("Mar",3);
        parse_months.put("MAR",3);
        parse_months.put("March",3);
        parse_months.put("MARCH",3);
        parse_months.put("Apr",4);
        parse_months.put("APR",4);
        parse_months.put("April",4);
        parse_months.put("APRIL",4);
        parse_months.put("May",5);
        parse_months.put("MAY",5);
        parse_months.put("June",6);
        parse_months.put("JUNE",6);
        parse_months.put("July",7);
        parse_months.put("JULY",7);
        parse_months.put("Aug",8);
        parse_months.put("AUG",8);
        parse_months.put("August",8);
        parse_months.put("AUGUST",8);
        parse_months.put("Sept",9);
        parse_months.put("SEPT",9);
        parse_months.put("September",9);
        parse_months.put("SEPTEMBER",9);
        parse_months.put("Oct",10);
        parse_months.put("OCT",10);
        parse_months.put("October",10);
        parse_months.put("OCTOBER",10);
        parse_months.put("Nov",11);
        parse_months.put("NOV",11);
        parse_months.put("November",11);
        parse_months.put("NOVEMBER",11);
        parse_months.put("Dec",12);
        parse_months.put("DEC",12);
        parse_months.put("December",12);
        parse_months.put("DECEMBER",12);

        return parse_months;
    }

}