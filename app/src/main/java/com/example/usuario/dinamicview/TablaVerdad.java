package com.example.usuario.dinamicview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import static java.lang.Math.pow;

/**
 * Created by José Luis García on 26/09/16.
 */

public class TablaVerdad extends TableLayout implements Serializable {
    private int noVariables;
    private int noFunciones;
    private ArrayList<String> variables;
    private ArrayList<String> funciones;
    private ArrayList<ArrayList<Integer>> miniterminos;
    private ArrayList<ArrayList<Integer>> noImportas;
    private ArrayList<Arbol> bosque;
    private TabSim tablaSimbolos;
    private transient Context contexto;
    private transient boolean finTabla;
    private transient int indexTabla;
    private transient int columnTabla;
    private String textMiniterm;
    private transient int colRespaldo;

    public TablaVerdad(Context unContexto) {
        super(unContexto);
        this.contexto = unContexto;
        this.noVariables = 0;
        this.noFunciones = 0;
        this.bosque = null;
    }

    public TablaVerdad(Context unContexto, ArrayList<Arbol> unBosque, TabSim tablaSimbolos)  {
        super(unContexto);
        this.contexto = unContexto;
        this.configuraTabla();
        this.bosque = unBosque;
        this.tablaSimbolos = tablaSimbolos;
        this.calculaNoVariables();
        this.calculaNoFunciones();
        this.variables = new ArrayList<String>();
        this.funciones = new ArrayList<String>();
        this.construyeTablaArbol();
    }

    public TablaVerdad(Context unContexto, int numFunciones, int numVariables) {
        super(unContexto);
        this.contexto = unContexto;
        this.bosque = null;
        configuraTabla();
        this.tablaSimbolos = new TabSim();
        this.noFunciones = numFunciones;
        this.noVariables = numVariables;
        this.variables = new ArrayList<String>();
        this.funciones = new ArrayList<String>();
        this.tablaSimbolos.generaSimbolos(this.noVariables);
        this.construyeTablaDirecta();
    }

    private void configuraTabla() {
        this.setLayoutParams(new LayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, (LayoutParams.MATCH_PARENT) - 50)));
        this.setStretchAllColumns(true);
    }

    private void calculaNoVariables() {
        this.noVariables = this.tablaSimbolos.numeroSimbolos();
    }

    private void calculaNoFunciones() {
        this.noFunciones = this.bosque.size();
    }

    private void construyeTablaArbol(){
        int combinaciones = this.obtenNumeroCombinaciones();

        this.agregaEncabezados();
        this.creaEstructuraMiniterminos(false);
        for (int i = 0; i < combinaciones; i++) {
            String numBinario = String.format("%" + noVariables + "s", Integer.toBinaryString(i)).replace(' ', '0');
            this.variables.add(numBinario);
            this.obtenValorFunciones(numBinario);
            this.agregaValores(i);
        }
    }

    private void construyeTablaDirecta(){
        int combinaciones = this.obtenNumeroCombinaciones();

        this.agregaEncabezados();
        this.creaEstructuraMiniterminos(true);
        for (int i = 0; i < combinaciones; i++){
            String numBinario = String.format("%" + noVariables + "s", Integer.toBinaryString(i)).replace(' ', '0');
            this.variables.add(numBinario);
            this.obtenValorFunciones();
            this.agregaValores(i);
        }
    }

    private int obtenNumeroCombinaciones(){
        return (int)pow(2, this.noVariables);
    }

    private void agregaEncabezados() {
        TableRow row = new TableRow(this.contexto);
        row.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        row.setGravity(Gravity.CENTER);
        agregaEncabezadosVariables(row);
        agregaEncabezadosFunciones(row);
        this.addView(row);
    }

    private void obtenValorFunciones(String valoresVariables) {
        this.tablaSimbolos.asignaValoresSimbolos(valoresVariables);
        String fila = "";
        for(Arbol a : this.bosque)
        {
            boolean valorFunc = a.evaluaArbol(this.tablaSimbolos);
            fila += valorFunc ? "1" : "0";
            if(valorFunc)
                this.miniterminos.get(this.bosque.indexOf(a)).add(this.funciones.size());
        }
        this.funciones.add(fila);
    }

    private void agregaValores(int i)
    {
        TableRow row = new TableRow(this.contexto);
        row.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER);
        agregaValoresVariables(row, i);
        agregaValoresFunciones(row, i);
        this.addView(row);
    }

    private void agregaValoresVariables(TableRow row, int i)
    {
        String valor = this.variables.get(i);

        for(int pos = 0; pos < valor.length(); pos++)
        {
            TextView cell = new TextView(this.contexto);
            this.configuraCeldaValor(Character.toString(valor.charAt(pos)), cell);
            row.addView(cell);
        }
    }

    private void agregaValoresFunciones(TableRow row, int i)
    {
        String valor = this.funciones.get(i);

        for(int pos = 0; pos < valor.length(); pos++)
        {
            TextView cell = new TextView(this.contexto);
            cell.setTag(pos + (this.noFunciones * i));
            if(this.bosque != null)
                this.configuraCeldaValor(Character.toString(valor.charAt(pos)), cell);
            else
                this.configuraCeldaAccion(Character.toString(valor.charAt(pos)), cell);
            row.addView(cell);
        }
    }

    private void obtenValorFunciones() {
        String fila = "";
        for (int i = 0; i < this.noFunciones; i++) {
            fila += "0";
        }
        this.funciones.add(fila);
    }

    private void agregaEncabezadosVariables(TableRow row) {
        String vars = this.tablaSimbolos.obtenSimbolos();

        for (int i = 0; i < vars.length(); i++) {
            TextView cell = new TextView(this.contexto);
            this.configuraEncabezado(Character.toString(vars.charAt(i)), cell);
            row.addView(cell);
        }
    }

    private void agregaEncabezadosFunciones(TableRow row) {
        for (int i = 1; i <= this.noFunciones; i++) {
            TextView cell = new TextView(this.contexto);
            cell.setTag(i-1);
            this.configuraEncabezado("F" + i, cell);
            cell.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            int pos = (int)v.getTag();
                            indexTabla = 0;
                            finTabla = false;
                            textMiniterm = "";
                            limpiaMiniterminos();
                            for(int i = 0; i < miniterminos.get(pos).size(); i++)
                            {
                                String func = funciones.get(i);
                                TableRow fila = (TableRow)TablaVerdad.super.getChildAt(miniterminos.get(pos).get(i) + 1);
                                    TextView cell = (TextView)fila.getChildAt(pos + noVariables);
                                    cell.setBackgroundColor(Color.YELLOW);
                                    textMiniterm += "m" + miniterminos.get(pos).get(i);
                                    if(i < miniterminos.get(pos).size() - 1)
                                        textMiniterm += " + ";
                            }
                            LinearLayout layoutPrincipal = (LinearLayout)TablaVerdad.super.getParent();
                            TextView viewMiniterm = (TextView)layoutPrincipal.getChildAt(1);
                            viewMiniterm.setTextSize(18);
                            viewMiniterm.setHeight(100);
                            viewMiniterm.setText(textMiniterm);
                        }
                    }
            );
            row.addView(cell);
        }
    }

    private void configuraEncabezado(String texto, TextView celda)
    {
        celda.setText(texto);
        celda.setHeight(120);
        celda.setTextSize(20);
        celda.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        celda.setGravity(Gravity.CENTER);
        celda.setBackgroundResource(R.color.colorPrimary);
        celda.setTextColor(Color.WHITE);
    }

    private void configuraCeldaValor(String texto, TextView celda)
    {
        celda.setText(texto);
        celda.setHeight(120);
        celda.setTextSize(18);
        celda.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        celda.setGravity(Gravity.CENTER);
        celda.setBackgroundColor(Color.LTGRAY);
        colRespaldo = Color.LTGRAY;
        celda.setTextColor(Color.BLACK);
    }

    private void configuraCeldaAccion(String texto, TextView celda)
    {
        celda.setText(texto);
        celda.setHeight(120);
        celda.setTextSize(18);
        celda.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        celda.setGravity(Gravity.CENTER);
        celda.setBackgroundColor(Color.WHITE);
        colRespaldo = Color.WHITE;
        celda.setTextColor(Color.BLACK);
        celda.setClickable(true);
        celda.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = (String)((TextView)v).getText();
                int noCell = (int)v.getTag();
                int noRow = noCell / noFunciones;
                int noCol = noCell % noFunciones;
                if(valor.equals("0")) {
                    ((TextView) v).setText("1");
                    funciones.set(noRow, funciones.get(noRow).substring(0, noCol) + "1" + funciones.get(noRow).substring(noCol + 1));
                    miniterminos.get(noCol).add(noRow);
                    Collections.sort(miniterminos.get(noCol));
                }
                else if(valor.equals("1")) {
                    ((TextView) v).setText("X");
                    funciones.set(noRow, funciones.get(noRow).substring(0, noCol) + "X" + funciones.get(noRow).substring(noCol + 1));
                    miniterminos.get(noCol).remove((Integer)noRow);
                    noImportas.get(noCol).add(noRow);
                }
                else if(valor.equals("X")) {
                    ((TextView) v).setText("0");
                    funciones.set(noRow, funciones.get(noRow).substring(0, noCol) + "0" + funciones.get(noRow).substring(noCol + 1));
                    noImportas.get(noCol).remove((Integer)noRow);
                }
            }
        });
    }

    public  int getNoVariables(){return noVariables;}

    public ArrayList<String> getFunciones(){return this.funciones;}

    public  int getNoFunciones(){return  this.noFunciones;}

    public ArrayList<ArrayList<Integer>> getMiniterminos(){ return this.miniterminos; }

    public ArrayList<ArrayList<Integer>> getNoImportas(){ return this.noImportas; }

    private void creaEstructuraMiniterminos(boolean aceptaNoImporta) {
        this.miniterminos = new ArrayList<>();
        if (aceptaNoImporta)
            this.noImportas = new ArrayList<>();
        for (int i = 0; i < this.noFunciones; i++) {
            this.miniterminos.add(new ArrayList<Integer>());
            if (aceptaNoImporta)
                this.noImportas.add(new ArrayList<Integer>());
        }
    }

    private void limpiaMiniterminos() {
        for (int i = 0; i < miniterminos.size(); i++) {
            for (int j = 0; j < miniterminos.get(i).size(); j++) {
                TableRow fila = (TableRow) TablaVerdad.super.getChildAt(miniterminos.get(i).get(j) + 1);
                TextView cell = (TextView) fila.getChildAt(i + noVariables);
                cell.setBackgroundColor(colRespaldo);
            }
        }
    }

    /**
     * Algoritmo de dos niveles de Quine-McCluskey para la reducción de la tabla de verdad
     * @return      una lista de funciones con grupos validos para las reducciones de los miniterminos
     */
    public ArrayList<ArrayList<ParEnlazado>> quineMcCluskey(){
        ArrayList<Integer> terminosPosibles;
        ArrayList<ArrayList<ParEnlazado>> grupo;
        ArrayList<ParEnlazado> terminosReducidos;
        ArrayList<ArrayList<ParEnlazado>> enlacesFunciones;

        enlacesFunciones = new ArrayList<ArrayList<ParEnlazado>>();
        for (int func = 0; func < this.noFunciones; func++){
            //Paso 0: combinacion de no importas y miniterminos ordenadamente
            terminosPosibles = new ArrayList<Integer>(this.miniterminos.get(func));
            if (this.noImportas != null)
                terminosPosibles.addAll(this.noImportas.get(func));
            Collections.sort(terminosPosibles);
            //Paso 1: generacion de la inicializacion de grupos por numero de bits en 1
            grupo = new ArrayList<ArrayList<ParEnlazado>>(this.noVariables + 1);
            for(int i = 0; i < this.noVariables + 1; i++)
                grupo.add(new ArrayList<ParEnlazado>());
            for(Integer termino : terminosPosibles){
                ParEnlazado par = new ParEnlazado(termino);
                par.setTablaSimbolos(this.tablaSimbolos);
                int noBits = Integer.bitCount(termino);
                grupo.get(noBits).add(par);
            }
            //Paso 2: agrupamiento de enlaces, implicantes de orden i
            terminosReducidos = this.encuentraImplicantes(this.miniterminos.get(func), grupo);
            //Paso 3: busqueda de las implicantes esenciales por el metodo de Petrick
            terminosReducidos = this.petricksMethod(this.miniterminos.get(func), terminosReducidos);
            enlacesFunciones.add(terminosReducidos);
        }
        return enlacesFunciones;
    }

    /**
     * Genera todos los posibles enlaces para una tabla de verdad con miniterminos
     * @param unGrupo   una tabla con agrupaciones de posiciones de una tabla de verdad por su cantidad de bits en "1"
     * @return      los terminos implicantes primas que podrían ser implicantes primas esenciales de los miniterminos especificados
     */
    private ArrayList<ParEnlazado> encuentraImplicantes(ArrayList<Integer> miniterminos, ArrayList<ArrayList<ParEnlazado>> unGrupo){
        boolean hayReducciones;
        ArrayList<ArrayList<ParEnlazado>> tablaParesAnterior;
        ArrayList<ArrayList<ParEnlazado>> tablaParesPresente;
        ListIterator<ArrayList<ParEnlazado>> itActual;
        ListIterator<ArrayList<ParEnlazado>> itSig;
        ArrayList<ParEnlazado> terminosReducidos = new ArrayList<ParEnlazado>();
        ArrayList<ParEnlazado> enlaces = new ArrayList<ParEnlazado>();

        tablaParesAnterior = new ArrayList<ArrayList<ParEnlazado>>(unGrupo);            //copiado de la tabla de llegada
        do {
            hayReducciones = false;
            tablaParesAnterior.removeAll(Arrays.asList(new ArrayList<ParEnlazado>()));      //eliminado de los subgrupos vacios
            tablaParesPresente = new ArrayList<ArrayList<ParEnlazado>>();                   //creacion de la tabla nueva
            if (tablaParesAnterior.size() > 0) {
                for (int i = 0; i < tablaParesAnterior.size() - 1; i++)
                    tablaParesPresente.add(new ArrayList<ParEnlazado>());
                itActual = tablaParesAnterior.listIterator();
                itSig = tablaParesAnterior.listIterator(1);
                while (itSig.hasNext()) {     //recorrido de la tabla
                    int actual = itActual.nextIndex();
                    ArrayList<ParEnlazado> grupoActual = itActual.next();
                    ArrayList<ParEnlazado> grupoSig = itSig.next();
                    for (ParEnlazado elemActual : grupoActual) {     //recorriendo elementos del grupo i
                        for (ParEnlazado elemSig : grupoSig) {       //recorriendo elementos del grupo i + 1
                            if (elemActual.getMascaraBitsValidos().intValue() != elemSig.getMascaraBitsValidos().intValue())
                                continue;
                            int mascaraXOR = (elemActual.getPrimerElemento() & elemActual.getMascaraBitsValidos()) ^ (elemSig.getPrimerElemento() & elemSig.getMascaraBitsValidos());
                            if (mascaraXOR == 0x01 || mascaraXOR == 0x02 || mascaraXOR == 0x04 || mascaraXOR == 0x08 || mascaraXOR == 0x10) {
                                elemActual.setEsImplicante(false);
                                elemSig.setEsImplicante(false);
                                ParEnlazado enlace = new ParEnlazado();
                                enlace.setTablaSimbolos(this.tablaSimbolos);
                                enlace.agregaParEnlazado(elemActual);
                                enlace.agregaParEnlazado(elemSig);
                                enlace.setMascaraBitsValidos(elemActual.getMascaraBitsValidos() ^ mascaraXOR);
                                ArrayList<ParEnlazado> grupo = tablaParesPresente.get(actual);
                                if (!grupo.contains(enlace))
                                    grupo.add(enlace);
                                hayReducciones = true;
                            }
                        }
                    }
                }
                for (ArrayList<ParEnlazado> grupo : tablaParesAnterior)      //se extraen todas las implicantes resultantes de la reduccion
                    for (ParEnlazado elemento : grupo)
                        if (elemento.getEsImplicante())         //si el par ya no pudo producir otro conjunto
                            terminosReducidos.add(elemento);
            }
            tablaParesAnterior = tablaParesPresente;        //se elimina el conjunto anterior para no ocupar recursos
        }
        while (hayReducciones);      //mientras queden reducciones disponibles
        for (ParEnlazado par : terminosReducidos) {       //revisar que los terminos reducidos coincidan con los miniterminos especificados
            boolean contenido = false;
            Iterator<Integer> itMinitermino = miniterminos.iterator();
            while (itMinitermino.hasNext() && !contenido) {
                if (par.contiene(itMinitermino.next())) {
                    contenido = true;
                    enlaces.add(par);
                }
            }
        }
        return enlaces;
    }

    /**
     * Metodo de Petrick
     * @param miniterminos      la lista de miniterminos a procesar
     * @param implicantesPrimas los enlaces marcados como implicantes primas
     * @return      una lista de enlaces con las implicantes escenciales o más optimos
     */
    private ArrayList<ParEnlazado> petricksMethod(ArrayList<Integer> miniterminos, ArrayList<ParEnlazado> implicantesPrimas){
        ArrayList<ParEnlazado> implicantesEscenciales;
        ArrayList<Producto> implicantesPrimasSumas;
        ArrayList<ProductosSuma> productosSumas;
        ProductosSuma factorizado;
        Producto sumaOptima;
        ArrayList<ParEnlazado> sumaEscencial = new ArrayList<ParEnlazado>();

        if (implicantesPrimas.size() > 0) {
            //Paso 1: buscar implicantes escenciales y removerlas de la tabla
            implicantesEscenciales = new ArrayList<ParEnlazado>();
            for (Integer minitermino : miniterminos) {
                ArrayList<ParEnlazado> coincidencias = new ArrayList<ParEnlazado>();
                for (ParEnlazado enlace : implicantesPrimas)
                    if (enlace.contiene(minitermino))
                        coincidencias.add(enlace);
                if (coincidencias.size() == 1 && !implicantesEscenciales.contains(coincidencias.get(0)))      //implicante escencial, remover de las implicantes primas
                    implicantesEscenciales.add(coincidencias.remove(0));
            }
            if (!implicantesEscenciales.isEmpty()) {
                implicantesPrimas.removeAll(implicantesEscenciales);
                sumaEscencial = implicantesEscenciales;
            }
            //Paso 2: formar los productos de sumas para terminos no escenciales
            if (implicantesPrimas.size() > 1) {
                implicantesPrimasSumas = new ArrayList<Producto>();
                for (ParEnlazado enlacePrimo : implicantesPrimas)
                    implicantesPrimasSumas.add(new Producto(enlacePrimo));
                productosSumas = new ArrayList<ProductosSuma>();
                for (Integer minitermino : miniterminos) {
                    ProductosSuma suma = new ProductosSuma();
                    for (Producto prod : implicantesPrimasSumas)
                        if (prod.getPrimerElemento().contiene(minitermino))
                            suma.agregaProducto(prod);
                    if (suma.obtieneTamano() > 1 && suma.obtieneTamano() < miniterminos.size() && !productosSumas.contains(suma))  //evita productos con un termino y productos con todos los miniterminos
                        productosSumas.add(suma);
                }
                //Paso 3: factorizar terminos (no necesariamente comunes) para generar sumas de productos en pares hasta que quede un solo conjunto
                factorizado = this.factoriza(productosSumas);
                //Paso 4: escoger del conjunto el termino con menor numero de literales, si no, escoger arbitrariamente (suma más positiva)
                sumaOptima = factorizado.productoOptima();
                for (ParEnlazado enlace : sumaOptima.getProductosEnlaces())
                    sumaEscencial.add(enlace);
            }
        }
        return sumaEscencial;
    }

    /**
     * Ayuda con la distribución realizada en el Método de Petrick
     * @param productosSumas    la suma de productos inicial formulada por el Metodo de Petrick
     * @return      un producto de sumas completamente distribuido y/o factorizado
     */
    private ProductosSuma factoriza(ArrayList<ProductosSuma> productosSumas){
        ArrayList<ProductosSuma> productosSumasAnterior;
        ArrayList<ProductosSuma> productosSumasPresente;
        boolean buscarFactoresComunes = true;

        productosSumasAnterior = new ArrayList<ProductosSuma>(productosSumas);      //copia de la colección de llegada
        do {
            productosSumasPresente = new ArrayList<ProductosSuma>();        //creación de la colección presente
            while (!productosSumasAnterior.isEmpty()) {         //mientras el conjunto antiguo no se vacie
                ProductosSuma productoFactor = productosSumasAnterior.get(0);
                ProductosSuma factorizado = null;
                for (ListIterator<ProductosSuma> itProductosSuma = productosSumasAnterior.listIterator(1); itProductosSuma.hasNext() && factorizado == null; ) {
                    ProductosSuma productoOtro = itProductosSuma.next();
                    if (buscarFactoresComunes) {        //en la primera iteración, buscar tantos terminos comunes como sea posible
                        Producto factor = null;
                        factor = productoFactor.encuentraFactor(productoOtro);
                        if (factor != null)
                            factorizado = productoFactor.factoriza(factor, productoOtro);
                    }
                    else        //iteraciones posteriores no necesitan factorizado
                        factorizado = productoFactor.factoriza(null, productoOtro);
                    if (factorizado != null) {      //agregar el resultado de la factorización a la lista de sumas
                        productosSumasPresente.add(factorizado);
                        productosSumasAnterior.remove(productoFactor);
                        productosSumasAnterior.remove(productoOtro);
                    }
                }
                if (factorizado == null) {     //producto sin terminos comunes, no factorizable
                    ProductosSuma terminoNoComun = productosSumasAnterior.remove(0);
                    productosSumasPresente.add(terminoNoComun);
                }
            }
            buscarFactoresComunes = false;
            productosSumasAnterior = productosSumasPresente;    //se elimina la colección anterior para no consumir recursos
        }
        while (productosSumasPresente.size() > 1);
        return productosSumasPresente.get(0);
    }

    /**
     * Obtiene la tabla de simbolos utilizada en la tabla de verdad
     * @return      un objeto TabSim valido
     */
    public TabSim getTablaSimbolos(){
        return this.tablaSimbolos;
    }
}
