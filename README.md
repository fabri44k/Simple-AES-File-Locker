# Simple-AES-File-Locker
Simple Tool to enctypt/decrypt a single file

Simple tool to encrypt a single file using the Java Cryptographic Extension (JCE), a framework which implements the standard cryptographic algorithms such as AES, DES, DESede and RSA.



1. Choose a file for encryption or decryption. Any file without the .enc extension can be encrypted. Files ending with the .enc extension can only be decrypted.

2. You will be prompted to enter a password for encryption or decryption. The password must be a minimum of 8 characters. Please note that the password used for encryption must be the same for decryption. Make sure to remember your key to avoid the "Incorrect key" error.

3. You can now proceed to either encrypt or decrypt the selected file. An encrypted file will be marked with a 🔒 emoji and will be created in the same directory as the original file.

4. Once the file is created, you can close the application.

5. Decrypted files will be placed in a folder named "Decrypted Files." You have the option to rename your file, but please be aware that removing the .enc extension from an encrypted file will result in a failed decryption process.

6. If you intend to encrypt multiple files, consider creating a compressed folder using formats such as zip, rar, or 7z.




