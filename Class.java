import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Variable{
    static ArrayList<String> var = new ArrayList<>();
}

class Output{
    static ArrayList<String> result = new ArrayList<>();
}

class Label{
    static ArrayList<Integer> labelRecord = new ArrayList<>();
}

class AssignRegisters{

    static String assignReg(String s) {
        if(s.equals("R0")) return "000";
        else if(s.equals("R1")) return "001";
        else if(s.equals("R2")) return "010";
        else if(s.equals("R3")) return "011";
        else if(s.equals("R4")) return "100";
        else if(s.equals("R5")) return "101";
        else if(s.equals("R6")) return "110";
        else if(s.equals("FLAGS")) return "111";
        else return "";
    }
}

class Binary{

    static String binary(int n) {
        String s = "";
        if(n<0) return "";
        else if(n==0) return "0";
        else if(n==1) return "1";
        else {
            while(n>0) {
                String tem = ""+n%2;
                s = tem+s;
                n = n/2;
            }
            return s;
        }
    }

    static String complete(String s) {
        if(s.length()==8) return s;
        else if(s.length() < 8) {
            while(s.length()!=8) {
                s = "0"+s;
            }
            return s;
        }
        else {
            System.out.println("OVERFLOW. ");
            return "";
        }
    }
}

class Assign{

    static String opCode;
    static String unusedBits;

    static void assignA(String[] arr){
        switch (arr[0]){
            case "add":
                opCode = "00000";
                break;
            case "sub":
                opCode = "00001";
                break;
            case "mul":
                opCode = "00110";
                break;
            case "ls" :
                opCode = "01010";
                break;
            case "or" :
                opCode = "01011";
                break;
            case "and":
                opCode = "01100";
                break;
            default:
                opCode = "";
        }
        unusedBits = "00";

        String s = opCode+unusedBits;

        for(int i=1; i<arr.length; i++) {
            s+=AssignRegisters.assignReg(arr[i]);
        }

        Output.result.add(s);
    }

    static void assignB(String[] arr){
        switch (arr[0]) {
            case "mov" :
                opCode = "00010";
                break;
            case "rs" :
                opCode = "01000";
                break;
            case "ls" :
                opCode = "01001";
                break;
            default:
                opCode = "";
        }
        unusedBits = "";

        String s = opCode+unusedBits;

        s+=AssignRegisters.assignReg(arr[1])+Binary.complete(Binary.binary(Integer.parseInt(arr[2].substring(1))));

        Output.result.add(s);
    }

    static void assignC(String[] arr){

        switch(arr[0]) {
            case "mov" :
                opCode = "00011";
                break;
            case "not" :
                opCode = "01101";
                break;
            case "cmp" :
                opCode = "01110";
                break;
            default:
                opCode = "";
        }
        unusedBits = "00000";

        String s = opCode+unusedBits;

        s+=AssignRegisters.assignReg(arr[1])+AssignRegisters.assignReg(arr[2]);

        Output.result.add(s);
    }

    static void assignD(String[] arr){

        switch (arr[0]) {
            case "ld" :
                opCode = "00100";
                break;
            case "st" :
                opCode = "00101";
                break;
            default:
                opCode = "";
        }
        unusedBits = "";

        String s = opCode+unusedBits;
        s+=AssignRegisters.assignReg(arr[1]);

        int i = 0;
        while(i<Class.check.size() && Class.check.get(i)!=arr){
            i++;
        }

        String tem = Binary.complete(Binary.binary(i+1));
        s+=tem;

        Output.result.add(s);
    }

    static void assignE(String[] arr){

        switch (arr[0]) {
            case "jmp" :
                opCode = "01111";
                break;
            case "jlt" :
                opCode = "10000";
                break;
            case "jgt" :
                opCode = "10001";
                break;
            case "je" :
                opCode = "10010";
                break;
            default:
                opCode = "";
        }
        unusedBits = "000";

        String s = opCode+unusedBits;

        int c = 0;
        for(int i=0; i<Class.check.size(); i++){
            if(Class.check.get(i) == arr){
                c = i+1;
                break;
            }
        }

        s+=Binary.complete(Binary.binary(c));
        Output.result.add(s);
    }

    static void assignF(String[] arr){

        if(arr[0].equals("hlt")) opCode = "10011";
        else opCode = "";

        unusedBits = "00000000000";

        String s = opCode+unusedBits;

        Output.result.add(s);
    }
}

class CheckIf {
    static void ifA(String[] arr) {
        if (arr[1].length() == 2 && arr[2].length() == 2 && arr[3].length() == 2) {
            if (arr[1].charAt(0) == 'R' && arr[2].charAt(0) == 'R' && arr[3].charAt(0) == 'R') {
                if ((arr[1].charAt(1) >= '0' && arr[1].charAt(1) <= '6') && (arr[2].charAt(1) >= '0' && arr[2].charAt(1) <= '6') && (arr[3].charAt(1) >= '0' && arr[3].charAt(1) <= '6')) {
                    switch (arr[0]) {
                        case "add":
                        case "sub":
                        case "mul":
                        case "xor":
                        case "or":
                        case "and":
                            Assign.assignA(arr);
                            break;

                        default:
                            Output.result.add("Error1.");
                            return;
                    }
                }
                else{
                    Output.result.add("Error2.");
                    return;
                }
            }
            else{
                Output.result.add("Error2.");
                return;
            }
        }
    }

    static boolean isNumeric(String s) {
        boolean b = false;

        if (s.length() >= 1 && s.length() <= 3) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) >= '0' && s.charAt(i) <= '9') b = true;
                else b = false;
            }
        }
        return b;
    }

    static void ifB(String[] arr) {
        if (arr[1].length() == 2 && arr[1].charAt(0) == 'R' && (arr[1].charAt(1)>='0' && arr[1].charAt(1)<='6') && arr[2].charAt(0) == '$') {
            if (isNumeric(arr[2].substring(1))) {
                switch (arr[0]) {
                    case "mov":
                    case "rs" :
                    case "ls" :
                    Assign.assignB(arr);
                        break;

                    default:
                        Output.result.add("Error4.");
                        return;
                }
            } else {
                Output.result.add("Error5."); return;
            }

        } else {
            Output.result.add("Error6."); return;
        }
    }

    static void ifC(String[] arr) {
        if (arr[1].length() == 2 && arr[2].length() == 2) {
            if (arr[1].charAt(0) == 'R' && arr[2].charAt(0) == 'R' && (arr[1].charAt(1) >= 0 && arr[1].charAt(1) <= 0) && (arr[2].charAt(1) >= 0 && arr[2].charAt(1) <= 0)) {
                switch (arr[0]) {
                    case "mov":
                    case "div":
                    case "not":
                    case "cmp":
                        Assign.assignC(arr);
                        break;

                    default:
                        Output.result.add("Error7.");
                        return;
                }
            } else {
                Output.result.add("Error8.");return;
            }
        } else {
            Output.result.add("Error9."); return;
        }
    }

    static void ifD(String[] arr){
        if(arr[1].length()==2 && arr[1].charAt(0)=='R' && (arr[1].charAt(1))>='0' && arr[1].charAt(1)<='6'){
            if(Variable.var.contains(arr[2])){
                switch (arr[0]){
                    case "ld" :
                    case "st" :
                        int c = 0;
                        Assign.assignD(arr);
                        break;

                    default:
                        Output.result.add("Error10.");
                        return;
                }
            }
        }
        else {
            Output.result.add("Error11."); return;
        }
    }

    static void ifE(String[] arr){
        if(arr[0].equals("jmp") || arr[0].equals("jgt") || arr[0].equals("je") || arr[0].equals("jlt")) Assign.assignE(arr);
        else Output.result.add("Error.");
    }

    static void ifF(String[] arr){
        if(arr[0].equals("hlt")) Assign.assignF(arr);
        else {
            Output.result.add("Error12."); return;
        }
    }

}

class CheckAndAdd{
    static void addVar(){
        for(int i=0; i<Class.check.size(); i++){
            if(Class.check.get(i)[0].equals("var")) Variable.var.add(Class.check.get(i)[1]);
        }
    }

    static int find(String s){                  // FIND OUT THE POSITION OF MEM ADDRESS OF VAR.
        int c = -1;
        for(int i=0; i<Class.check.size(); i++){
            if(Class.check.size()==3 && (Class.check.get(i)[0]=="st" || Class.check.get(i)[0]=="ld")) {
                c = i+1;
                break;
            }
        }
        return c;
    }

    static void label(){
        for(int i=0; i<Class.check.size(); i++){
            if(Class.check.get(i)[0].contains("label")) Label.labelRecord.add(i+1);
        }
    }

    static int countVar(){
        int count = 0;
        for(int i=0; i<Class.check.size(); i++){
            if(Class.check.get(i)[0].equals("var")) count++;
        }

        return count;
    }
}

public class Class {

    static ArrayList<String[]> check = new ArrayList<>();  // ArrayList of String [] arr

    static void input(){
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\t");
        String s;
        while(true){
            s = sc.next();
            break;
        }
        sc.close();

        String[] arr = s.split("\\n"); //

        for(int i=0; i<arr.length; i++){
            String[] tem = arr[i].split(" ");
            check.add(tem);
        }

        check1(check);
    }

    static boolean isValid(String s){
        boolean b = false;
        for(int i=0; i<s.length(); i++){
            if((s.charAt(i)>='0' && s.charAt(i)<=9) || s.charAt(i)=='_' || (s.charAt(i)>='a' && s.charAt(i)<='z') || (s.charAt(i)>='A' && s.charAt(i)<='Z')){
                b = true;
            }
            else{
                b = false;
                break;
            }
        }
        return b;
    }

    static void check1(ArrayList<String[]> arr){
        if(!arr.get(arr.size()-1)[0].equals("hlt")) Output.result.add("Error.");
        boolean b = true; int i = 0;
        while(i<arr.size()){
            if(b && arr.get(i)[0].equals("var") && isValid(arr.get(i)[1])) {
                Variable.var.add(arr.get(i)[1]);
            }
            else if(!b && arr.get(i)[0].equals("var")){
                Output.result.add("Error13.");
                break;
            }
            else if(b && !arr.get(i)[0].equals("var")){
                b = false;
            }
            i++;
        }

        for(int a=0; a<arr.size()-1; a++){
            if(arr.get(a)[0].equals("hlt")){
                Output.result.add("Error14.");
                break;
            }
        }

      for(int j=0; j<check.size(); j++){
          check2(check.get(j));
      }
    }

    static void check2(String[] arr){
        if(arr.length == 4) {
            CheckIf.ifA(arr);
        }

        else if(arr.length == 3){
            if(arr[2].charAt(0)=='$') CheckIf.ifB(arr);
            else if(arr[2].charAt(0)=='R') CheckIf.ifC(arr);
            else CheckIf.ifD(arr);
        }

        else if(arr.length == 2){
            CheckIf.ifE(arr);
        }

        else if(arr.length == 1){
             CheckIf.ifF(arr);
        }

        else {
            Output.result.add("Error15.");
            return;
        }
    }

    static void output(){

        boolean b = true;
        for(int i=0; i<Output.result.size(); i++){
            if(Output.result.get(i).charAt(0)=='1' || Output.result.get(i).charAt(0)=='0') {
                b = true;
            }
            else {
                b = false;
                break;
            }
        }

        if(b == true){
            for(int i=0; i<Output.result.size(); i++){
                System.out.println(Output.result.get(i));
            }
        }

        else System.out.println("Error.");
    }

    public static void main(String[] args){
        input();
        output();
    }
}
