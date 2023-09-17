import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App extends JFrame {
    
    private JButton browseButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea logArea;
    
    private String key;
    File inputFile;
    File outputFile;
    
    public App(){
        ascoltatore listener = new ascoltatore();
        setTitle("File Encryption App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));
        
        browseButton = new JButton("Sfoglia...");
        browseButton.addActionListener(listener);
        mainPanel.add(browseButton);
        encryptButton = new JButton("Cripta");
        encryptButton.addActionListener(listener);
        decryptButton = new JButton("Decripta");
        decryptButton.addActionListener(listener);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(encryptButton);
        encryptButton.setEnabled(false);
        buttonPanel.add(decryptButton);
        decryptButton.setEnabled(false);
        mainPanel.add(buttonPanel);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setText("Seleziona un file da criptare/decriptare");
        JScrollPane scrollPane = new JScrollPane(logArea);
        mainPanel.add(scrollPane);

        add(mainPanel);
        setVisible(true);
    }
    
class ascoltatore implements ActionListener	{

	
public void actionPerformed(ActionEvent event) {
    if (event.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                inputFile = fileChooser.getSelectedFile();
                
                logArea.setText("Seleziona un file da criptare/decriptare");
                logArea.setText("\n File->"+ inputFile.getAbsolutePath());
                encryptButton.setEnabled(true);
                decryptButton.setEnabled(true);
                do {
                    key = JOptionPane.showInputDialog("Inserisci una password più lunga di 8 caratteri: ", "");
                    if (key == null || key.isEmpty() || key.length() < 8){
                        logArea.setText("");
                        logArea.setText("Chiave non valida. Riprova");
                    }
                } while(key == null || key.isEmpty() || key.length() < 8);
                logArea.setText("");
                logArea.setText("File->"+ inputFile.getAbsolutePath());
                logArea.append("\n Chiave inserita");
            
            } else {
                logArea.setText("");
                logArea.setText("Nessun file selezionato");
            }
            key = AES.generateKeyFromPassword(key);
    } else if (event.getSource() == encryptButton){
        
            outputFile = new File(inputFile + ".enc");
            try {
                AES.encrypt(key, inputFile, outputFile);
                logArea.setText("");
                logArea.setText("Crittografia completata. \n Path->" + outputFile.getAbsolutePath());
            } catch (CryptoException ex) {
                logArea.setText("");
                logArea.setText("Errore durante la crittografia: \n" + ex.getMessage());
                ex.printStackTrace();
            }

    } else if (event.getSource() == decryptButton){
       
        if (inputFile.getName().endsWith(".enc")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Scegli la cartella di destinazione");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int returnValue = fileChooser.showSaveDialog(null);
            
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                String outputFileName = inputFile.getName().substring(0, inputFile.getName().length() - 4); // Rimuovi ".enc" dall'estensione
                outputFile = new File(selectedDirectory, outputFileName);
                
                try {
                    AES.decrypt(key, inputFile, outputFile);
                    logArea.setText("");
                    logArea.setText("Decrittografia completata. \n File decifrato creato: " + outputFile.getAbsolutePath());
                } catch (CryptoException ex) {
                    logArea.setText("");
                    logArea.setText("Errore durante la decrittografia: \n" + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                logArea.setText("");
                logArea.setText("Operazione annullata.");
            }
        } else {
            logArea.setText("");
            logArea.setText("Il file specificato non ha estensione '.enc'. Impossibile decrittografare.");
        }
	
    }
}

}

    public static void main(String[] args) {
        JFrame frame= new App();
        frame.setMinimumSize(new Dimension(120, 150));
		frame.setLocationRelativeTo(null);
}
}
