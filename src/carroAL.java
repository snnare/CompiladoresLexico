
import java.io.*;

public class carroAL {
    static int a_avance = 0;
    static int a_inicio =0;

    static  int filesize =0;
    static  boolean fin_archivo = false;

    public static char [] linea;
    static int DIAG;
    static int ESTADO;
    static int c;

    static String LEXEMA = "";
    static String entrada = "";
    static String salida = "";
    static String MyToken = "";
    static int renglon = 1;

    // Palabras reservadas
    static String [] pReservadas = new String[28];


    public static void creaEscribeArchivo(File xFile, String msg){
        try{
            PrintWriter fileOut = new PrintWriter(new FileWriter(xFile, true));
            fileOut.println(msg);
            fileOut.close();
        } catch (IOException ignored){
        }
    }
    // Todo excepto \r, \n , \t, ", eof
    public static boolean esAny1(int x){
        return x != 10 && x != 13 && x != 9 && x != 34 && x != 255;    }

    // Todo excepto \r, \n, eof
    public static boolean esAny2(int x){
        return x != 13 && x != 10 && x != 255;
    }
    // Todo excepto eof
    public static boolean esAny3(int x){
        return  x!=255;
    }

    // acepta cualquier carácter \r, \n, \t, o espacio:
    public static boolean esDelim(int x){
        return x == 13 || x == 10 || x == 9 || x == 32;
    }

    public static String obtenerLexema(){
        StringBuilder x = new StringBuilder();
        for (int i = a_inicio; i < a_avance; i++) {
            x.append(linea[i]);
        }
        return (x.toString());
    }


    public static File xArchivo(String xName){
        return new File(xName);
    }

    public static int leerCar(){
        if(a_avance <= filesize - 1){
            if(linea[a_avance] == 10){
                renglon ++;
            }
            return (linea[a_avance++]);
        } else {
            fin_archivo = true;
            return  255;
        }
    }

    public static char [] abreLeeCierra(String xName){
        File xFile = new File(xName);
        char [] data;

        try{
            FileReader fin = new FileReader(xFile);
            filesize = (int) xFile.length();
            data = new char[filesize + 1];
            fin.read(data, 0, filesize);
            data[filesize] = ' ';
            filesize++;
            return  data;

        } catch (IOException ignored){

        }
        return null;
    }

    public static String pausa(){
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String nada = null;

        try {
            nada = entrada.readLine();
            return nada;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "";
    }

    public static void error(){
        System.out.println("ERROR: en el caracter: ["+ (char) c +"]");
    }

    public static boolean esLetra() {
        return ((c >= 65 && c <= 90) || (c >= 97 && c <= 122));
    }

    public static boolean esDigito() {
        return ((c >= 48 && c <= 57));
    }

    public static boolean esReservada(String x){
        for(int i = 0; i < pReservadas.length; i++){
            if(pReservadas[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    public static  int DIAGRAMA(){
        a_avance = a_inicio;
        switch (DIAG){
            case 0 -> DIAG = 4;
            case 4 -> DIAG = 8;
            case 8 -> DIAG = 13;
            case 13 -> DIAG = 16;
            case 16 -> DIAG = 27;
            case 27 -> DIAG = 30;
            case 30 -> error();
        }
        return  DIAG;
    }

    public static String TOKEN(){
        do{
            switch (ESTADO){
                case 0 -> {
                    c = leerCar();
                    if(esLetra()){
                        ESTADO = 1;
                    } else {
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 1 -> {
                    c = leerCar();
                    if(esLetra() || esDigito()){
                        ESTADO = 1;
                    } else if (c == '_') {
                        ESTADO = 2;
                    } else {
                        ESTADO = 3;
                    }
                    break;
                }
                case 2 -> {
                    c = leerCar();
                    if(esLetra() || esDigito()){
                        ESTADO = 1;
                    }
                    break;
                }
                case 3 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ("id");
                }
                case 4 -> {
                    c = leerCar();
                    if(c == '"'){
                        ESTADO = 5;
                    } else {
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 5 -> {
                    c = leerCar();
                    if(esAny1(c)){
                        ESTADO = 6;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 6 -> {
                    c = leerCar();
                    if(esAny1(c)){
                        ESTADO = 6;
                    }
                    if (c == '"'){
                        ESTADO = 7;
                    }
                }
                case 7 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ("cad");
                }
                case 8 -> {
                    c = leerCar();
                    if(esDigito()){
                        ESTADO = 9;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 9 -> {
                    c = leerCar();
                    if(esDigito()){
                        ESTADO = 9;
                    }
                    if (c == '.'){
                        ESTADO = 10;
                    }
                    break;
                }
                case 10 -> {
                    c = leerCar();

                    if(esDigito()){
                        ESTADO = 11;
                    } else {
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 11 ->{
                    c = leerCar();
                    if(esDigito()){
                        ESTADO = 11;
                    } else{
                        ESTADO = 12;
                    }
                    break;
                }
                case 12 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio= a_avance;
                    return "num";
                }
                case 13 -> {
                    c = leerCar();

                    if(esDigito()){
                        ESTADO = 14;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 14 -> {
                    c = leerCar();
                    if(esDigito()){
                        ESTADO = 14;
                    } else {
                        ESTADO = 15;
                    }
                    break;
                }
                case 15 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "num";
                }
                case 16 ->{
                    c = leerCar();

                    if (c == '>'){
                        ESTADO = 17;
                    } else if (c == '<') {
                        ESTADO = 20;
                    } else if (c == '=') {
                        ESTADO = 25;
                    } else {
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 17 -> {
                    c = leerCar();
                    if( c == '='){
                        ESTADO = 18;
                    } else{
                        ESTADO =19;
                    }
                    break;
                }
                case 18 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "mai";
                }
                case 19 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ">";
                }
                case 20 -> {
                    c = leerCar();

                    if( c == '='){
                        ESTADO = 21;
                    } else if (c == '-') {
                        ESTADO = 22;
                    } else if (c == '>') {
                        ESTADO = 23;
                    } else {
                        ESTADO = 24;
                    }
                    break;
                }
                case 21 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio =a_avance;
                    return "mei";
                }
                case 22 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "asig";
                }
                case  23 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "dif";
                }
                case 24 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "<";
                }
                case 25 -> {
                    c = leerCar();

                    if (c == '<'){
                        ESTADO =21;
                    } else if (c == '>'){
                        ESTADO =18;
                    } else {
                        ESTADO =26;
                    }
                    break;
                }
                case 26 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "=";
            }
                case 27 -> {
                    c = leerCar();
                    if(esDelim(c)){
                        ESTADO = 28;
                    } else {
                        ESTADO = DIAGRAMA();
                    }
                    break;
                }
                case 28 -> {
                    c = leerCar();

                    if(esDelim(c)){
                        ESTADO = 28;
                    } else {
                        ESTADO =29;
                    }
                    break;
                }
                case 29 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "omite";
                }
                case 30 -> {
                    c = leerCar();
                    if( c == '+'){
                        ESTADO = 31;
                    } else if (c == '-') {
                        ESTADO =32;
                    } else if (c == '*') {
                        ESTADO = 35;
                    } else if(c == '/'){
                        ESTADO =36;
                    } else if(c == ';'){
                        ESTADO = 43;
                    } else if(c== '('){
                        ESTADO = 44;
                    } else if(c == ')'){
                        ESTADO = 45;
                    } else if(c==','){
                        ESTADO = 46;
                    } else if(c =='['){
                        ESTADO = 47;
                    } else if(c == ']'){
                        ESTADO = 48;
                    } else {
                        ESTADO = 49;
                    }
                    break;
                }
                case 31 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "+";
                }
                case 32 -> {
                    c = leerCar();
                    if(c == '>'){
                        ESTADO = 33;
                    } else{
                        ESTADO = 34;
                    }
                    break;
                }
                case 33 -> {
                    LEXEMA =obtenerLexema();
                    a_inicio = a_avance;
                    return "opdec";
                }
                case 34 -> {
                    a_avance--;
                    LEXEMA =obtenerLexema();
                    a_inicio = a_avance;
                    return "-";
                }
                case 35 -> {
                    LEXEMA =obtenerLexema();
                    a_inicio = a_avance;
                    return "*";
                }
                case 36 -> {
                    c = leerCar();
                    if (c == '/'){
                        ESTADO = 37;
                    } else if (c == '*'){
                        ESTADO = 39;
                    } else{
                        ESTADO = 42;
                    }
                    break;
                }
                case 37 -> {
                    // Programar Any 2
                    c = leerCar();
                    if(esAny2(c)){
                        ESTADO =37;
                    } else{
                        ESTADO =38;
                    }
                    break;
                }
                case 38 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "omite";
                }
                case 39 -> {
                    // Programar Any3
                    c = leerCar();
                    if(esAny3(c)){
                        ESTADO = 39;
                    }
                    if(c == '*'){
                        ESTADO =40;
                    }
                    break;
                }
                case 40 -> {
                    c = leerCar();
                    if(c == '*'){
                        ESTADO =40;
                    } else if(esAny3(c)){
                        ESTADO =39;
                    } else if(c == '/'){
                        ESTADO =41;
                    }
                    break;
                }
                case 41 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "omite";
                }
                case 42 -> {
                    a_avance--;
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "/";
                }
                case 43 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ";";
                }
                case 44 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "(";
                }
                case 45 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ")";
                }
                case 46 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return ",";
                }
                case 47 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "[";
                }
                case 48 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "]";
                }
                case 49 -> {
                    LEXEMA = obtenerLexema();
                    a_inicio = a_avance;
                    return "omite";
                }

            } // Fin Switch
        } while (true);
    }

    public static void main(String[] args) {
        entrada = args[0] ;
        salida = args[0] + ".sal";

        pReservadas[0]="declara";
        pReservadas[1]="fin_declara";
        pReservadas[2]="dato";
        pReservadas[3]="numerico";
        pReservadas[4]="cadena";
        pReservadas[5]="comienza";
        pReservadas[6]="termina";
        pReservadas[7]="funciones";
        pReservadas[8]="fin_funciones";
        pReservadas[9]="funcion";
        pReservadas[10]="fin_funcion";
        pReservadas[11]="mientras";
        pReservadas[12]="fin_mientras";
        pReservadas[13]="si";
        pReservadas[14]="entonces";
        pReservadas[15]="otro_caso";
        pReservadas[16]="fin_si";
        pReservadas[17]="repite";
        pReservadas[18]="hasta";
        pReservadas[19]="lee";
        pReservadas[20]="escribe";
        pReservadas[21]="escribe_ret";
        pReservadas[22]="o";
        pReservadas[23]="y";
        pReservadas[24]="no";
        pReservadas[25]="div";
        pReservadas[26]="mod";
        pReservadas[27]="abs";

        if(!xArchivo(entrada).exists()){
            System.out.println("\n\nERROR: Archivo no encontrado");
            System.exit(4);
        }

        linea = abreLeeCierra(entrada);

        do {
            ESTADO = 0;
            DIAG = 0;
            MyToken = TOKEN();
            if(!MyToken.equals("omite")){
                creaEscribeArchivo(xArchivo(salida), MyToken);
                creaEscribeArchivo(xArchivo(salida), LEXEMA);
                creaEscribeArchivo(xArchivo(salida), renglon+"");
            }

            System.out.println("Token\tLexema\tLinea");
            System.out.println("["+MyToken+"]\t["+LEXEMA+"]\t["+renglon+"]");
            pausa();

            System.out.println(fin_archivo);
            a_inicio = a_avance;
        }while (!fin_archivo);
        creaEscribeArchivo(xArchivo(salida), "eof");
        creaEscribeArchivo(xArchivo(salida), "eof");
        creaEscribeArchivo(xArchivo(salida), "666");
        System.out.println("\nTodo facherito :))");

    }



}
