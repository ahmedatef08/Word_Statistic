package WordStatistics;
import java.io.*;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import GUI.BottomPanel;
import java.util.concurrent.Semaphore;

public class FileThread implements Runnable{
    private Path filePath;
    private DefaultTableModel tableModel;
    private int rowIndex;
    private static int count;
    private JLabel Longest;
    private JLabel Shortest;
    private Semaphore semaphore;
    
    public FileThread(Path filePath, DefaultTableModel tableModel,int rowIndex,JLabel Longest,JLabel Shortest,Semaphore semaphore) {
        this.filePath = filePath;
        this.tableModel = tableModel;
        this.rowIndex = rowIndex;
        this.Longest = Longest;
        this.Shortest = Shortest;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            processFile(filePath);
        } catch (IOException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int num_of_words =0;
    private int num_of_is=0;
    private int num_of_you=0;
    private int num_of_are=0;
    private int longestWordLength = Integer.MIN_VALUE;
    private int shortestWordLength = Integer.MAX_VALUE;

    private void processFile(Path filePath) throws IOException {
            
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for(String word : words){
                    if(word.isEmpty())
                        continue;
                    SwingUtilities.invokeLater(() -> {
                        num_of_words++;
                        tableModel.setValueAt(num_of_words, rowIndex, 1);
                        if ("you".equalsIgnoreCase(word)) {
                            num_of_you++;
                            tableModel.setValueAt(num_of_you, rowIndex, 2);
                        } 
                        else if ("is".equalsIgnoreCase(word)) {
                            num_of_is++;
                            tableModel.setValueAt(num_of_is, rowIndex, 3);
                        } 
                        else if ("are".equalsIgnoreCase(word)) {
                            num_of_are++;
                            tableModel.setValueAt(num_of_are, rowIndex, 4);
                        }
                        if (Character.isLetter(word.charAt(0))) {
                            if (word.length() > longestWordLength) {
                                longestWordLength = word.length();
                                tableModel.setValueAt(word, rowIndex, 5);
                                try {
                                    semaphore.acquire();
                                        if(BottomPanel.longestLength < word.length()){
                                            Longest.setText("Longest Word : " + word);
                                            BottomPanel.longestLength = word.length();
                                            Thread.sleep(200);
                                        }
                                    semaphore.release();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                            if (word.length() < shortestWordLength && word.length()!=0) {
                                shortestWordLength = word.length();
                                tableModel.setValueAt(word, rowIndex, 6);
                                try {
                                    semaphore.acquire();
                                        if(BottomPanel.shortestLength > word.length()){
                                            Shortest.setText("Shortest Word : " + word);
                                            BottomPanel.shortestLength = word.length();
                                            //Thread.sleep(20);
                                        }
                                    semaphore.release();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        } 
                    });
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
