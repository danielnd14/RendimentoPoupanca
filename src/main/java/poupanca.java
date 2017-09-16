/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sun.org.apache.bcel.internal.generic.SWAP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * @author daniel
 */
public class poupanca {
    private static final double taxa = 1+(0.5/100.00);
    private static int aniversarioDepositos;
    private static GregorianCalendar data;
    public poupanca() {
        data = new GregorianCalendar();
        aniversarioDepositos = data.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public static void simularDepositos(double saldoI, double deposito, int periodo,
                                        boolean ComReajuste, javax.swing.JTable tabela, javax.swing.JLabel jlMsgBotton, double reajuste,
                                        javax.swing.JProgressBar barra) {

        long tempo = System.currentTimeMillis();
        barra.setMaximum(periodo);
        double depositoSemReajuste =deposito;
        double mediaDepositos = 0.0;
        double somaDepositos = 0.0;
        reajuste = 1+(reajuste/100.0);
        double Vfinal = saldoI;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat nbf = NumberFormat.getInstance();
        try {

            DefaultTableModel modelSimulacao = (DefaultTableModel) tabela.getModel();
            Rectangle rect;
            barra.setVisible(true);
            for(int i = 0; i<=periodo;i++){

                if(i!=0){

                    data = corrigirData(data);


                    Vfinal = Vfinal*(taxa);
                    Vfinal = Vfinal+deposito;
                    somaDepositos = somaDepositos+deposito;

                    modelSimulacao.addRow(new Object[]{i+"º - "+sdf.format(data.getTime()),
                            "R$ "+nbf.format(Vfinal),
                            "R$ "+nbf.format(deposito),
                            "R$ "+nbf.format(Vfinal*(taxa-1))});

                    if(i%12 == 0 && ComReajuste){

                        deposito = deposito*reajuste;
                    }
                    data.add(Calendar.MONTH,1);

                }else {
                    modelSimulacao.addRow(new Object[]{"Quanto você tem: ",
                            "R$ "+nbf.format(Vfinal),
                            "R$ "+"0.0","R$ "+nbf.format(Vfinal*(taxa-1))});
                }
                rect = tabela.getCellRect(i, 0, true);
                tabela.scrollRectToVisible(rect);
                final int iAux=i;
                javax.swing.SwingUtilities.invokeLater(() -> barra.setValue(iAux));
            }
            barra.setVisible(false);
            mediaDepositos = somaDepositos/periodo;

            modelSimulacao.addRow(new Object[]{"-----------------------------------",
                    "-----------------------------------",
                    "-----------------------------------",
                    "-----------------------------------"});

            modelSimulacao.addRow(new Object[]{"Total Depositado ---->>>",
                    "R$ " + nbf.format(somaDepositos),
                    "Juros Adquiridos ------>>>",
                    "R$ " + nbf.format(Vfinal-somaDepositos-saldoI)});

            modelSimulacao.addRow(new Object[]{"Media de Depositos-------",
                    "R$ "+nbf.format(mediaDepositos),
                    "Calculo efetuado em >--->",
                    ((System.currentTimeMillis() - tempo) / 1000.0) + " Segundos"});

            modelSimulacao.addRow(new Object[]{"-----------------------------------",
                    "-----------------------------------",
                    "-----------------------------------",
                    "-----------------------------------"});

            jlMsgBotton.setText("Você terá: R$ " + nbf.format(Vfinal));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Erro ao Calcular", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } finally {
            nbf = null;
            sdf = null;
            data = null;
            System.gc();
        }
    }/******************
        Esse metodo verifica se a data informada e sabado ou domingo.
        Caso seja Sabado ele antecipa o deposito para Sexta feira, e
        se for em um Domingo ele adia o deposito para Segunda Feira
     ******************/
    private static GregorianCalendar corrigirData (GregorianCalendar dataDeposito){
        switch(dataDeposito.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SATURDAY:dataDeposito.add(Calendar.DAY_OF_MONTH , -1);break;
            case Calendar.SUNDAY:dataDeposito.add(Calendar.DAY_OF_MONTH , 1);break;
            default:{
                dataDeposito.set(GregorianCalendar.DAY_OF_MONTH,aniversarioDepositos);

            }
        }
        return dataDeposito;
    }

    public static void calcularTempo(double valor, javax.swing.JTable tabela,
                                     javax.swing.JLabel jlMsgBotton,
                                     javax.swing.JProgressBar barra,int prazo ) {

        long timeMillis = System.currentTimeMillis();
         java.util.Map linha = new HashMap<Integer,Object[]>();

        barra.setMaximum(prazo);
        jlMsgBotton.setVisible(false);
        NumberFormat nbf = NumberFormat.getInstance();
        double depositos = 0.0;
        final int ano = 12;
        DefaultTableModel modelCalcTempo = (DefaultTableModel) tabela.getModel();
        Thread work = null;

        for (int i = 1; i <= prazo; i++) {
            final int tempo = ano * i;
            final int key = i;

            work = new Thread(()->{
                linha.put(key,calcularDepositos(tempo , valor));
                SwingUtilities.invokeLater(()->{
                    barra.setValue(barra.getValue()+1);
                });
            });

            work.start();
            if(i%10 == 0){// a cada 10 threads ele espera ela terminar
                try {
                    work.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        while(linha.size()<prazo){// isso e uma especie de listener
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        linha.put(prazo+1,new Object[]{"Tempo Gasto",
                "-----------------------------------",
                "-----------------------------------",
                ((System.currentTimeMillis() - timeMillis) / 1000.0) + " Segundos"});


        barra.setVisible(false);


        SwingUtilities.invokeLater(()->{
            linha.forEach((k,v)->{

                modelCalcTempo.addRow((Object[]) v);

            });
        });


        Object[] linhaMeta = (Object[])linha.get(prazo);

        jlMsgBotton.setText("Junte "+linhaMeta[2] +
                " ao mês e em "+prazo+" ano (s) você baterá sua meta!");
        jlMsgBotton.setVisible(true);
    }

    /**
     * Esse metodo basea se na formula do capital que e:
     * C=m/taxa^tempo, sendo assim ele pega o valor desejado
     * divide pelo tempo, assume que ele e o montante e joga
     * na formula citada e usa o capital como candidato a deposito
     * simula depositos, caso ache um valor menor que o desejado
     * ele soma mais 5 centavos no candidato a deposito e refaz a
     * simulaçao, ele repete isso ate encontrar um valor que melhor
     * aproxima. Ao encontrar um valor aproximado ele retorna.
     */

    private static Object calcularDepositos(int tempo, double valor) {
        String periodo = "";
        NumberFormat nbf = NumberFormat.getInstance();
        double deposito = valor / tempo;
        deposito = deposito / Math.pow(taxa, tempo-2);
        double acrescimo = 0.05;//5 centavos.
        double valorFinal;
        do {

            valorFinal = 0.0;
            deposito = deposito + acrescimo;
            for (int i = 0; i < tempo; i++) {
                valorFinal = valorFinal * taxa;
                valorFinal = valorFinal + deposito;
            }

            /**
             * Se entrar em algum desses if e porque o codigo esta longe de convergir
             * para o valor desejado, entao o if da um empurrao no valor, para
             * andar mais rapido
             * */
            if((valor-valorFinal)>2000){
                deposito++;
                deposito++;
            }else if((valor-valorFinal)>1000){
                deposito++;
            }

        } while (valorFinal <= valor);
        if ((tempo / 12) == 1) {
            periodo = "1 ano";
        } else {
            periodo = (tempo / 12) + " ano";
        }

        Object[] retorno = new Object[]{periodo,
                "R$ " + nbf.format(valor),
                "R$ " + nbf.format(deposito),
                "R$ " + nbf.format(valor * (taxa - 1))};
        return retorno;
    }
}
