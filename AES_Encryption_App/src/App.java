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

    public App() {
        ActionListener listener = new ActionListenerImpl();
        setTitle("File Encryption App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));
        browseButton = new JButton("Choose File");
        browseButton.setBackground(ADD_NEW_FILE_BUTTON_COLOR);
        browseButton.addActionListener(listener);
        mainPanel.add(browseButton);
        encryptButton = new JButton("Encrypt");
        encryptButton.setBackground(ENCRYPT_BUTTON_COLOR);
        encryptButton.addActionListener(listener);
        decryptButton = new JButton("Decrypt");
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
        logArea.setText("Select a file to encrypt/decrypt");
        JScrollPane scrollPane = new JScrollPane(logArea);
        mainPanel.add(scrollPane);
        add(mainPanel);
        setVisible(true);
    }

    class ActionListenerImpl implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == browseButton) {
                FileDialog fileDialog = new FileDialog(new Frame(), "Choose a File", FileDialog.LOAD);
                fileDialog.setVisible(true);
                String directory = fileDialog.getDirectory();
                String file = fileDialog.getFile();

                if (directory != null && file != null) {
                    inputFile = new File(directory, file);
                    logArea.setText("");
                    logArea.setText("Selected File: \n " + inputFile.getAbsolutePath());
                    if (inputFile.getName().endsWith(".enc")) {
                        logArea.append(LOCK_EMOJI);
                        encryptButton.setEnabled(false);
                        decryptButton.setEnabled(true);
                    } else {
                        encryptButton.setEnabled(true);
                        decryptButton.setEnabled(false);
                    }

                    boolean keyValid = false;
                    do {
                        key = JOptionPane.showInputDialog("Enter the password for your file (minimum 8 characters)", "");
                        if (key == null) {
                            logArea.setText("");
                            logArea.setText("Input canceled. Reselect the file.");
                            encryptButton.setEnabled(false);
                            decryptButton.setEnabled(false);
                            return;
                        } else if (key.isEmpty() || key.length() < 8) {
                            logArea.setText("");
                            logArea.setText("Invalid key. Please try again.");
                        } else {
                            keyValid = true;
                        }
                    } while (!keyValid);

                } else {
                    logArea.setText("");
                    logArea.setText("No file selected");
                }
                logArea.setText("");
                logArea.setText("Key entered for the selected file: \n" + inputFile.getAbsolutePath());
                if (inputFile.getName().endsWith(".enc")) {
                    logArea.append(LOCK_EMOJI);
                }
                key = AES.generateKeyFromPassword(key);

            } else if (event.getSource() == encryptButton) {
                outputFile = new File(inputFile + ".enc");
                try {
                    AES.encrypt(key, inputFile, outputFile);
                    logArea.setText("");
                    logArea.setText("Encryption completed. \n Path->" + outputFile.getAbsolutePath() + LOCK_EMOJI);
                } catch (CryptoException ex) {
                    logArea.setText("");
                    logArea.setText("Error during encryption");
                    ex.printStackTrace();
                }
                encryptButton.setEnabled(false);

            } else if (event.getSource() == decryptButton) {

                if (inputFile.getName().endsWith(".enc")) {
                    String originalFileName = inputFile.getName().substring(0, inputFile.getName().length() - 4); // Remove ".enc" extension
                    String decryptedFolderPath = inputFile.getParent() + File.separator + "Decrypted Files";
                    File decryptedFolder = new File(decryptedFolderPath);
                    if (!decryptedFolder.exists()) {
                        decryptedFolder.mkdirs();
                    }

                    outputFile = new File(decryptedFolderPath, originalFileName);
                    try {
                        AES.decrypt(key, inputFile, outputFile);
                        logArea.setText("");
                        logArea.setText("Decrypted file created: \n" + outputFile.getAbsolutePath());
                    } catch (CryptoException ex) {
                        logArea.setText("");
                        logArea.setText("Error! Incorrect key. Please try again.");
                        ex.printStackTrace();
                    }
                    decryptButton.setEnabled(false);
                } else {
                    logArea.setText("");
                    logArea.setText("Selected file: " + inputFile.getAbsolutePath());
                    logArea.append("\nThe specified file does not have a '.enc' extension. Decryption is not possible.");
                }
            } else {
                logArea.setText("");
                logArea.setText("Invalid operation");
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new App();
        frame.setMinimumSize(new Dimension(120, 170));
        frame.setLocationRelativeTo(null);
    }
}
