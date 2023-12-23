package GUI;
import java.util.concurrent.*; 
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static GUI.FrameGUI.Frame;
import WordStatistics.FileThread;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomPanel {
    public JButton start;
    public DefaultTableModel tableModel;
    public JTable resultTable;
    public JScrollPane scrollPane;
    public JPanel BottomPanel = new JPanel(new BorderLayout());
    public JPanel row1 = new JPanel(new BorderLayout());
    public JPanel row2 = new JPanel(new BorderLayout());
    public JPanel row3 = new JPanel(new BorderLayout());
    public JLabel Longest = new JLabel("Longest Word : ");
    public JLabel Shortest = new JLabel("Shortest Word : ");
    public File[] files;
    
    Semaphore semaphore = new Semaphore(1);

    public static int longestLength;
    public static int shortestLength;
    
    public BottomPanel(){
        
        start = new JButton("Start Processing");
        row1.add(start,BorderLayout.WEST);
        row1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        BottomPanel.add(row1,BorderLayout.NORTH);
            

        DisplayTable();
        
        row3.add(Longest,BorderLayout.NORTH);
        row3.add(Shortest,BorderLayout.SOUTH);
        row3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        BottomPanel.add(row3,BorderLayout.SOUTH);
        
        Frame.add(BottomPanel);
        start.addActionListener((ActionEvent e) -> {
            try{
                System.out.println(files);
                int numThreads = files.length;
                ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
                int count=0;
                for(File file : files){
                    executorService.submit(new FileThread(file.toPath(), tableModel,count, Longest,Shortest,semaphore));
                    count++;
                }
                executorService.shutdown();
            }catch(Exception ex){
                System.out.println("error");
            }

        });
    }

    public void setFiles(File[] files) {
        this.files = files;
    }
    
    private void DisplayTable(){
        Object[] columnName = 
        {"File Name", "# Words","# 'you'", "# 'is'", "# 'are'", "Longest Word", "Shortest Word"};
        tableModel = new DefaultTableModel(columnName,0);
        resultTable = new JTable(tableModel);
        scrollPane = new JScrollPane(resultTable);

        row2.add(scrollPane);
        row2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        BottomPanel.add(row2,BorderLayout.CENTER);
    }
    
    public void FillTable(){
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        DisplayTable();
        if(this.files!=null)
            for (File file : files) {
                SwingUtilities.invokeLater(() -> {
                    tableModel.addRow(new Object[]{
                            file.getName(),0,0,0,0,"",""
                    });
                    tableModel.fireTableDataChanged();
                });
            }
        
        longestLength = Integer.MIN_VALUE;
        shortestLength = Integer.MAX_VALUE;
        Longest.setText("Longest Word : ");
        Shortest.setText("Shortest Word : ");
        
        Frame.revalidate();
        Frame.repaint();
    }
}
