import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class App extends JFrame {
    
    private JButton browseButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea logArea;
    private final static Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);
    private final static String LOCK_EMOJI = " \uD83D\uDD12";
    private final static Color DECRYPT_BUTTON_COLOR = Color.decode("#e9a017");
	private final static Color ENCRYPT_BUTTON_COLOR = Color.decode("#00FF7F");
	private final static Color ADD_NEW_FILE_BUTTON_COLOR = Color.decode("#9cc0e7");

    private String key;
    private File inputFile;
    private File outputFile;
    
    public App(){
        ascoltatore listener = new ascoltatore();
        setTitle("File Encryption App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));
        browseButton = new JButton("Scegli file");
        browseButton.setBackground(ADD_NEW_FILE_BUTTON_COLOR);
        browseButton.addActionListener(listener);
        mainPanel.add(browseButton);
        encryptButton = new JButton("Cripta");
        encryptButton.setBackground(ENCRYPT_BUTTON_COLOR);
        encryptButton.addActionListener(listener);
        decryptButton = new JButton("Decripta");
        decryptButton.setBackground(DECRYPT_BUTTON_COLOR);
        decryptButton.addActionListener(listener);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(encryptButton);
        encryptButton.setEnabled(false);
        buttonPanel.add(decryptButton);
        decryptButton.setEnabled(false);
        mainPanel.add(buttonPanel);
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(EMOJI_FONT);
        logArea.setText("Seleziona un file da criptare/decriptare");
        JScrollPane scrollPane = new JScrollPane(logArea);
        mainPanel.add(scrollPane);
        add(mainPanel);
        setVisible(true);
    }
    
class ascoltatore implements ActionListener	{

	public void actionPerformed(ActionEvent event) {
        
        if (event.getSource() == browseButton) {
            FileDialog fileDialog = new FileDialog(new Frame(), "Scegli un file", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String directory = fileDialog.getDirectory();
            String file = fileDialog.getFile();
    
            if (directory != null && file != null) {
                inputFile = new File(directory, file);
                logArea.setText("");
                logArea.setText("File selezionato: \n " + inputFile.getAbsolutePath());
                if (inputFile.getName().endsWith(".enc")){
                    logArea.append(LOCK_EMOJI);
                    encryptButton.setEnabled(false);
                    decryptButton.setEnabled(true);
                } else {
                    encryptButton.setEnabled(true);
                    decryptButton.setEnabled(false);
                }
                
                boolean keyValid = false;
                do {
                key = JOptionPane.showInputDialog("Inserisci la password per il tuo file (minimo 8 caratteri)", "");
                if (key == null) {
                    logArea.setText("");
                    logArea.setText("Inserimento annullato. Riseleziona il file.");
                    encryptButton.setEnabled(false);
                    decryptButton.setEnabled(false);
                    return;
                } else if (key.isEmpty() || key.length() < 8) {
                    logArea.setText("");
                    logArea.setText("Chiave non valida. Riprova");
                } else {
                    keyValid = true;
                }
                } while (!keyValid);

            } else {
                logArea.setText("");
                logArea.setText("Nessun file selezionato");
            }
            logArea.setText("");
            logArea.setText("Chiave inserita per il file selezionato: \n" + inputFile.getAbsolutePath());
            if (inputFile.getName().endsWith(".enc")){
                    logArea.append(LOCK_EMOJI);
            }
            key = AES.generateKeyFromPassword(key);
    
        } else if (event.getSource() == encryptButton){
           outputFile = new File(inputFile + ".enc");
            try {
                AES.encrypt(key, inputFile, outputFile);
                logArea.setText("");
                logArea.setText("Crittografia completata. \n Path->" + outputFile.getAbsolutePath() + LOCK_EMOJI);
            } catch (CryptoException ex) {
                logArea.setText("");
                logArea.setText("Errore durante la crittografia");
                ex.printStackTrace();
            }
            encryptButton.setEnabled(false);
   
    } else if (event.getSource() == decryptButton){
       
        if (inputFile.getName().endsWith(".enc")) {
            String originalFileName = inputFile.getName().substring(0, inputFile.getName().length() - 4); // Rimuovi ".enc" dall'estensione
            String decryptedFolderPath = inputFile.getParent() + File.separator + "Decrypted Files";
            File decryptedFolder = new File(decryptedFolderPath);
            if (!decryptedFolder.exists()) {
                decryptedFolder.mkdirs();
            }
            
            outputFile = new File(decryptedFolderPath, originalFileName);
            try {
                AES.decrypt(key, inputFile, outputFile);
                logArea.setText("");
                logArea.setText("File decifrato creato: \n" + outputFile.getAbsolutePath());
            } catch (CryptoException ex) {
                logArea.setText("");
                logArea.setText("Errore ! Chiave errata. Riprova.");
                ex.printStackTrace();
            }
            decryptButton.setEnabled(false);
            } else {
                logArea.setText("");
                logArea.setText("File selezionato: " + inputFile.getAbsolutePath());
                logArea.append("\nIl file specificato non ha estensione '.enc'. Impossibile decrittografare.");
            }
        } else {
            logArea.setText("");
            logArea.setText("Operazione non valida");
        }
	}
}


    public static void main(String[] args) {
        JFrame frame= new App();
        frame.setMinimumSize(new Dimension(120, 170));
		frame.setLocationRelativeTo(null);
}
}
