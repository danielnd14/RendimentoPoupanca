/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author daniel
 * https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
 */

/**
 */
public class GUI extends javax.swing.JFrame {
    private JTable tabela;
    private JScrollPane sp;
    private JPanel jpSul;
    private JTabbedPane abas;
    private JLabel jlMsgBotton;
    static boolean reajuste = false;

    @SuppressWarnings("unchecked")
    public GUI() {
        super("Simulador de investimento em poupança |By: Danielnd14|");

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName()) || "GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        NumberFormat nbf = NumberFormat.getInstance();
        this.setSize(900, 590);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        abas = new JTabbedPane();

        SwingUtilities.invokeLater(()->{
            tabela = new JTable();
            sp = new JScrollPane();
            sp.setViewportView(tabela);
            jpSul = new JPanel();
            jpSul.setLayout(new BorderLayout());
            jpSul.setVisible(false);
            Font padrao = new Font("Open Sans", Font.BOLD, 22);
            jlMsgBotton = new JLabel();
            jlMsgBotton.setFont(padrao);
        });

        SwingUtilities.invokeLater(()->{
            abas.add("Simular Depósitos", getJPanelAba1(nbf));
        });
        SwingUtilities.invokeLater(()->{
            abas.add("Previsão", getJPanelAba2(nbf));
        });
        SwingUtilities.invokeLater(()->{
            abas.add("Resultado", getJPanelAba3());
        });

        this.add(abas);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                Aviso a = new Aviso();
                a.setVisible(true);
                System.gc();
            }

        });

    }

    private JPanel getJPanelAba1(NumberFormat nbf) {

        String[] sOption = {"Não", "Sim"};
        JComboBox jcbAba1Reajuste = new JComboBox(sOption);

        JPanel jpBaseAba1 = new JPanel();

        JPanel jpAba1 = new JPanel();
        JPanel jpAba1Todos = new JPanel();
        JPanel jpAba1Centro = new JPanel();
        JPanel jpAba1Text = new JPanel();
        JPanel jpAba1Bcalcular = new JPanel();
        JPanel jpAba1TextField = new JPanel();
        JPanel jpAba1Esquerda = new JPanel();
        JButton jbAba1Calcular = new JButton("Calcular!");
        JFormattedTextField jtfAba1ValorInicial = new JFormattedTextField(nbf);
        JFormattedTextField jtfAba1Deposito = new JFormattedTextField(nbf);
        JFormattedTextField jtfAba1Periodo = new JFormattedTextField(nbf);
        JFormattedTextField jtfAba1ValorReajuste = new JFormattedTextField(nbf);
        jtfAba1ValorReajuste.setEditable(reajuste);
        jpBaseAba1.setLayout(new BorderLayout());
        jpAba1Todos.setLayout(new BorderLayout());
        jpAba1Esquerda.setLayout(new GridLayout(30, 1));
        jpAba1.setLayout(new BoxLayout(jpAba1, BoxLayout.Y_AXIS));
        jpAba1Bcalcular.setLayout(new BoxLayout(jpAba1Bcalcular, BoxLayout.X_AXIS));
        jpAba1Text.setLayout(new GridLayout(1, 5));
        jpAba1TextField.setLayout(new GridLayout(1, 5));
        jpAba1Centro.setLayout(new GridLayout(30, 1));
        jpAba1Bcalcular.add(Box.createHorizontalGlue());
        jpAba1Bcalcular.add(jbAba1Calcular);
        jpAba1Bcalcular.add(Box.createHorizontalGlue());
        jpAba1Text.add(new JLabel("Quanto você tem?"));
        jpAba1Text.add(new JLabel("Quanto vai depositar?"));
        jpAba1Text.add(new JLabel("Período? (mêses)"));
        jpAba1Text.add(new JLabel("Quer reajuste anual?"));
        jpAba1Text.add(new JLabel("Qual o valor do reajuste (%)"));
        jpAba1TextField.add(jtfAba1ValorInicial);
        jpAba1TextField.add(jtfAba1Deposito);
        jpAba1TextField.add(jtfAba1Periodo);
        jpAba1TextField.add(jcbAba1Reajuste);
        jpAba1TextField.add(jtfAba1ValorReajuste);
        jpAba1Centro.add(jpAba1Text);
        jpAba1Centro.add(jpAba1TextField);
        jpAba1Centro.add(new JPanel());
        jpAba1Centro.add(jpAba1Bcalcular);
        jpAba1Todos.add(jpAba1Esquerda, BorderLayout.WEST);
        jpAba1Todos.add(jpAba1Centro, BorderLayout.CENTER);
        jpAba1.add(jpAba1Todos);
        jpAba1.add(Box.createHorizontalBox());
        jpAba1.add(Box.createHorizontalBox());
        JProgressBar jpbBarra = new JProgressBar();
        jpbBarra.setVisible(false);

        jcbAba1Reajuste.addActionListener((ActionEvent e) -> {
            if (jcbAba1Reajuste.getSelectedItem().equals("Sim")) {

                this.reajuste = true;
                jtfAba1ValorReajuste.setEditable(reajuste);
            } else {
                this.reajuste = false;
                jtfAba1ValorReajuste.setEditable(reajuste);
            }
        });

        jbAba1Calcular.addActionListener((ActionEvent e) -> {

            poupanca a = new poupanca();
            tabela.removeAll();
            {
                tabela.setModel(new DefaultTableModel(new Object[][]{
                                }, new String[]{
                                        "Nº - Data", "Capital", "Depositos", "Juros ao mês"}

                                ) {
                                    boolean[] canEdit = new boolean[]{false, false, false, false};

                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return canEdit[columnIndex];
                                    }
                                }

                );
            }
            if (jtfAba1ValorInicial.getText().equalsIgnoreCase("") ||
                    jtfAba1Deposito.getText().equalsIgnoreCase("") ||
                    jtfAba1Periodo.getText().equalsIgnoreCase("") ||
                    jtfAba1ValorReajuste.getText().equalsIgnoreCase("")) {

                if (jtfAba1ValorInicial.getText().equalsIgnoreCase("")) {
                    jtfAba1ValorInicial.setText("0.0");
                }
                if (jtfAba1Deposito.getText().equalsIgnoreCase("")) {
                    jtfAba1Deposito.setText("0.0");
                }
                if (jtfAba1Periodo.getText().equalsIgnoreCase("")) {
                    jtfAba1Periodo.setText("0");
                }
                if (jtfAba1ValorReajuste.getText().equalsIgnoreCase("")) {
                    jtfAba1ValorReajuste.setText("0.0");

                }
            }

            if (Integer.parseInt(jtfAba1Periodo.getText()) > 0) {
                Thread calculo;
                calculo = new Thread(() -> {
                    double valorInicial = 0;
                    double valorReajuste = 0;
                    double valorDeposito = 0;
                    int periodo = 0;
                    try {
                        valorInicial = nbf.parse(jtfAba1ValorInicial.getText()).doubleValue();
                        valorReajuste = nbf.parse(jtfAba1ValorReajuste.getText()).doubleValue();
                        valorDeposito = nbf.parse(jtfAba1Deposito.getText()).doubleValue();
                        periodo = nbf.parse(jtfAba1Periodo.getText()).intValue();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    SwingUtilities.invokeLater(() -> {
                        jpbBarra.setVisible(true);
                    });

                    poupanca.simularDepositos(valorInicial, valorDeposito,
                            periodo, reajuste,
                            tabela, jlMsgBotton,
                            valorReajuste, jpbBarra);

                    SwingUtilities.invokeLater(() -> {
                        tabela.setRowSelectionInterval(Integer.parseInt(jtfAba1Periodo.getText()) + 4,
                                Integer.parseInt(jtfAba1Periodo.getText()) + 4);
                    });

                    SwingUtilities.invokeLater(() -> {
                        abas.setSelectedIndex(2);
                        jpbBarra.setValue(0);
                        jpbBarra.setVisible(false);
                    });

                });
                calculo.setPriority(Thread.MAX_PRIORITY);
                calculo.start();
                SwingUtilities.invokeLater(() -> jpSul.setVisible(true));
            }

        });
        jpBaseAba1.add(jpAba1, BorderLayout.CENTER);
        jpBaseAba1.add(jpbBarra,BorderLayout.SOUTH);
        return jpBaseAba1;
    }

    private JPanel getJPanelAba2(NumberFormat nbf) {

        JPanel jpBaseAba2 = new JPanel();
        JPanel jpAba2 = new JPanel();
        JPanel jpAba2TextQuanto = new JPanel();
        JPanel jpAba2BaseQuanto = new JPanel();
        JPanel jpAba2Bcalcular = new JPanel();
        JPanel jpAba2Dados = new JPanel();
        JPanel jpAba2HorizontalGlue = new JPanel();

        JProgressBar jpbBarra = new JProgressBar();
        String anos[] = new String[31];
        anos[0] = new String("Em quantos Anos?");
        
        for(int i = 1; i<=30 ; i++){
            if(i!= 1){
                anos[i] = new String(""+(i)+" - Anos");
            }else{
                anos[i] = new String(""+(i)+" - Ano");
            }

        }

        JComboBox jcAba2Ano = new JComboBox(anos);
        JButton jbAba2Calcular = new JButton("Calcular!");
        JFormattedTextField jtfAba2QuantoQuer = new JFormattedTextField(nbf);
        jpBaseAba2.setLayout(new BorderLayout());

        jpAba2Bcalcular.setLayout(new BoxLayout(jpAba2Bcalcular, BoxLayout.X_AXIS));
        jpAba2Bcalcular.add(Box.createHorizontalGlue());
        jpAba2Bcalcular.add(jbAba2Calcular);
        jpAba2Bcalcular.add(Box.createHorizontalGlue());

        jpAba2TextQuanto.setLayout(new GridLayout(1,2));
        jpAba2TextQuanto.add(new JLabel("Quanto você quer?"));
        jpAba2TextQuanto.add(jtfAba2QuantoQuer);


        jpAba2HorizontalGlue.setLayout(new BoxLayout(jpAba2HorizontalGlue, BoxLayout.X_AXIS));
        jpAba2HorizontalGlue.add(Box.createHorizontalGlue());
        jpAba2HorizontalGlue.add(jpAba2Dados);
        jpAba2HorizontalGlue.add(Box.createHorizontalGlue());


        jpAba2Dados.setLayout(new GridLayout(3,1));
        jpAba2Dados.add(jpAba2TextQuanto);
        jpAba2Dados.add(jcAba2Ano);
        jpAba2Dados.add(jbAba2Calcular);

        jpAba2BaseQuanto.setLayout(new BoxLayout(jpAba2BaseQuanto,BoxLayout.Y_AXIS));
        jpAba2BaseQuanto.add(Box.createVerticalGlue());
        jpAba2BaseQuanto.add(jpAba2HorizontalGlue);
        jpAba2BaseQuanto.add(Box.createVerticalGlue());

        jpAba2.add(jpAba2BaseQuanto);
        jpbBarra.setVisible(false);
        
        jbAba2Calcular.addActionListener((ActionEvent e) -> {
            if(jcAba2Ano.getSelectedIndex() != 0){
                tabela.removeAll();
                {
                    tabela.setModel(new DefaultTableModel(new Object[][]{
                                    }, new String[]{
                                            "Período", "Meta", "Depositos ao mês", "Juros ao mês"}

                                    ) {
                                        boolean[] canEdit = new boolean[]{false, false, false, false};

                                        @Override
                                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                            return canEdit[columnIndex];
                                        }
                                    }

                    );
                }
                if (jtfAba2QuantoQuer.getText().equalsIgnoreCase("")) {

                    if (jtfAba2QuantoQuer.getText().equalsIgnoreCase("")) {
                        jtfAba2QuantoQuer.setText("0.0");
                    }

                }

                try {
                    double valor = Math.abs( nbf.parse(jtfAba2QuantoQuer.getText()).doubleValue());
                    if (valor > 0) {

                        Thread calculo = new Thread(()-> {
                            SwingUtilities.invokeLater(()->{
                                jpbBarra.setVisible(true);
                            });

                            poupanca.calcularTempo(valor, tabela, jlMsgBotton,jpbBarra,jcAba2Ano.getSelectedIndex());

                            SwingUtilities.invokeLater(()->{
                                jpSul.setVisible(true);
                                abas.setSelectedIndex(2);
                                jpbBarra.setValue(0);
                                jpbBarra.setVisible(false);
                            });

                        });
                        calculo.setPriority(Thread.MAX_PRIORITY);
                        calculo.start();
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(null,"Ops, Parece que você não selecionou a quantidade de anos!");
            }


        });
        jpBaseAba2.add(jpAba2, BorderLayout.CENTER);
        jpBaseAba2.add(jpbBarra,BorderLayout.SOUTH);
        return jpBaseAba2;
    }

    private JPanel getJPanelAba3() {

        JPanel jpAba3 = new JPanel();
        JProgressBar jpbAba3Barra = new JProgressBar();
        JPanel jpAba3MsgBotton = new JPanel();
        jpAba3.setLayout(new BorderLayout());
        jpbAba3Barra.setMaximum(100);
        jpbAba3Barra.setMinimum(0);
        jpbAba3Barra.setSize(880, 2);
        jpbAba3Barra.setVisible(false);
        jpAba3MsgBotton.setLayout(new BoxLayout(jpAba3MsgBotton, BoxLayout.X_AXIS));
        jpAba3MsgBotton.add(Box.createHorizontalGlue());
        jpAba3MsgBotton.add(jlMsgBotton);
        jpAba3MsgBotton.add(Box.createHorizontalGlue());

        JButton export = new JButton("Gerar Planilha");
        export.addActionListener((ActionEvent e) -> {
            exportExcel.gerarExcel(tabela, jpbAba3Barra);
        });

        jpAba3MsgBotton.add(export);
        jpSul.add(jpAba3MsgBotton, BorderLayout.CENTER);
        jpSul.add(jpbAba3Barra, BorderLayout.SOUTH);
        jpAba3.add(sp, BorderLayout.CENTER);
        jpAba3.add(jpSul, BorderLayout.SOUTH);
        return jpAba3;
    }
}
