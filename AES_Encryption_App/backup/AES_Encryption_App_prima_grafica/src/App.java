import java.io.File;
import java.util.Scanner;

public class App {
    
	public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        
        File inputFile = new File("src/in.png");
        
        System.out.println("Inserire la chiave: ");
        String key = input.nextLine();
        key = AES.generateKeyFromPassword(key);
        
        System.out.println("Inserire la modalità (1 per crittografare, 2 per decrittografare): ");
        int mode = input.nextInt();
        
        File outputFile;
        
        if (mode == 1) {
            outputFile = new File(inputFile + ".enc");
            try {
                AES.encrypt(key, inputFile, outputFile);
                System.out.println("Crittografia completata. File cifrato creato: " + outputFile.getAbsolutePath());
            } catch (CryptoException ex) {
                System.out.println("Errore durante la crittografia: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (mode == 2) {
            if (inputFile.getName().endsWith(".enc")) {
                String originalFilePath = inputFile.getName().substring(0, inputFile.getName().length() - 4); // Rimuovi ".enc" dall'estensione
                outputFile = new File(originalFilePath);
                try {
                    AES.decrypt(key, inputFile, outputFile);
                    System.out.println("Decrittografia completata. File decifrato creato: " + outputFile.getAbsolutePath());
                } catch (CryptoException ex) {
                    System.out.println("Errore durante la decrittografia: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Il file specificato non ha estensione '.enc'. Impossibile decrittografare.");
            }
        } else {
            System.out.println("Modalità non valida. Inserire 1 per crittografare o 2 per decrittografare.");
        }
        
        input.close();
    
	
	}
}
