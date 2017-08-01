/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author daniel
 */
public class exportExcel{
    public static void gerarExcel(javax.swing.JTable tabela, javax.swing.JProgressBar barra){
        barra.setVisible(true);
        barra.setMaximum(tabela.getRowCount() + 20);
        SwingWorker wk = new SwingWorker() {
            String path;
            @Override
            protected Object doInBackground() throws Exception {
                HSSFWorkbook wb = new HSSFWorkbook();
                Sheet sheet = wb.createSheet("Investimento");
                preencherPlanilha(tabela, wb, sheet,barra);
                gravarPlanilha(wb,barra);
                return null;
            }
            public void done(){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        barra.setVisible(false);
                        barra.setValue(0);
                    }
                });
            }
        };

        wk.execute();
    }
    private static void setTitulos(javax.swing.JTable tabela, Sheet sheet,CellStyle styleTitulos ){
        Row row = sheet.createRow(0);
        Cell cell = null;
        for(int i = 0 ; i< tabela.getColumnCount() ; i++){
            cell = row.createCell(i);
            cell.setCellValue(tabela.getColumnName(i));
            cell.setCellStyle(styleTitulos);
        }
    }
    private static void preencherPlanilha(javax.swing.JTable tabela,
                                          HSSFWorkbook wb,
                                          Sheet sheet,
                                          JProgressBar barra){
        long tempo = System.currentTimeMillis();
        int numCol = tabela.getColumnCount();
        setTitulos(tabela,sheet,getStyleTitulos(wb));
        CellStyle destaqueEntreLinha = getStyleDestaque(wb);
        CellStyle semCorEntreLinha = getStylePadrao(wb);
        Cell cell = null;
        Row row = null;

        for(int i = 0 ; i < tabela.getRowCount() ; i++){

            row = sheet.createRow(i+1);
            for(int j = 0 ; j < numCol; j++){
                cell = row.createCell(j);
                cell.setCellValue(""+tabela.getValueAt(i,j));
                if(i%2 == 0){
                    cell.setCellStyle(semCorEntreLinha);
                }else{
                    cell.setCellStyle(destaqueEntreLinha);
                }
            }
            final int fI = i;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Rectangle rect = tabela.getCellRect(fI, 0, true);
                    tabela.scrollRectToVisible(rect);
                    tabela.setRowSelectionInterval(fI, fI);
                    barra.setValue(fI);
                }
            });
        }
        row = sheet.createRow(sheet.getLastRowNum()+1);
        cell = row.createCell(numCol-2);
        cell.setCellValue("Planilha Preenchida em");
        CellStyle gray = getStyleGray(wb);
        cell.setCellStyle(gray);
        cell = row.createCell(numCol-1);
        cell.setCellValue((System.currentTimeMillis() - tempo)+" Milissegundos");
        cell.setCellStyle(gray);
        for(int i = 0; i < numCol ; i++){
            sheet.autoSizeColumn(i,true);
        }
    }
    private static CellStyle getStyleTitulos(HSSFWorkbook wb){
        CellStyle styleTitulos = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        styleTitulos.setAlignment(HorizontalAlignment.CENTER);
        styleTitulos.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleTitulos.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitulos.setFont(font);
        return styleTitulos;
    }
    private static CellStyle getStylePadrao(HSSFWorkbook wb){
        CellStyle semCorEntreLinha = wb.createCellStyle();
        semCorEntreLinha.setAlignment(HorizontalAlignment.CENTER);
        return semCorEntreLinha;
    }

    private static CellStyle getStyleGray(HSSFWorkbook wb){
        CellStyle styleGray = wb.createCellStyle();
        styleGray.setAlignment(HorizontalAlignment.CENTER);
        styleGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return styleGray;
    }

    private static CellStyle getStyleDestaque(HSSFWorkbook wb){
        CellStyle destaqueEntreLinha = wb.createCellStyle();
        destaqueEntreLinha.setAlignment(HorizontalAlignment.CENTER);
        destaqueEntreLinha.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        destaqueEntreLinha.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return destaqueEntreLinha;
    }
    private static void gravarPlanilha (HSSFWorkbook wb,JProgressBar barra){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barra.setValue(barra.getValue()+10);
            }
        });
        String tempPath = System.getProperty("user.home")+File.separator+"Simulacao-Investimento-Poupanca.xls";
        File f = new File(tempPath);
        int count = 1;
        while(f.exists()){
            tempPath = System.getProperty("user.home")+File.separator+"Simulacao-Investimento-Poupanca"+"_"+count+".xls";
            count++;
            f = new File(tempPath);
        }
        final String path = tempPath;
        try{
            FileOutputStream out = new FileOutputStream(path);
            wb.write(out);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    barra.setValue(barra.getValue()+10);
                }
            });
            out.flush();
            out.close();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int resposta = JOptionPane.showConfirmDialog(null,
                            "Planilha salva em: " + path+ ", \n Deseja Abrir?");
                    if(resposta == JOptionPane.YES_OPTION){
                        try {
                            java.awt.Desktop.getDesktop().open( new File( path ) );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage()+path,
                    "Erro ao Gravar",JOptionPane.INFORMATION_MESSAGE);
        }finally {
            System.gc();
        }
    }
}