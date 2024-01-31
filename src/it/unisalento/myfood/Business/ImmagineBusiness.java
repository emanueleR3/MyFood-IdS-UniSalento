package it.unisalento.myfood.Business;

import it.unisalento.myfood.model.Immagine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ImmagineBusiness {

    private static ImmagineBusiness instance = new ImmagineBusiness();

    final static String INITIAL_PATH = "src\\immagini\\articoli\\";

    private Immagine immagine;

    private ImmagineBusiness() {}

    public static ImmagineBusiness getInstance() {
        return instance;
    }

    public ArrayList<Immagine> showImagesPerArticolo(Integer idArticolo) {
        ArrayList<Immagine> immagini = new ArrayList<>();
        String path = INITIAL_PATH + idArticolo + "\\";
        System.out.println(path);//TODO: rimuovere
        File folder = new File(path);

        // Controlla se il percorso specificato è una cartella
        if (folder.isDirectory()) {
            // Elenco di tutti i file nella cartella
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Verifica se il file è un'immagine con estensione .jpg, .png, .gif, .jpeg
                    if (isImageFile(file)) {
                        try {
                            // Carica l'immagine utilizzando ImageIO
                            immagine = new Immagine();

                            String fileName = file.getName();

                            immagine.setImmagine(ImageIO.read(file));
                            immagine.setIdArticolo(idArticolo);
                            immagine.setPath(path + fileName);

                            immagini.add(immagine);
                        } catch (IOException e) {
                            System.err.println("Errore durante il caricamento dell'immagine da: " + file.getAbsolutePath());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("Il percorso specificato non è una cartella: " + path);
        }

        return immagini;
    }

    // TODO: amministratore preme su una X e rimuove l'immagine selezionata
    public void removeImage(File fileToRemove) {
        // Check se il file esiste
        if (fileToRemove.exists()) {
            boolean isDeleted = fileToRemove.delete();

            // Check se la rimozione è avvenuta
            if (isDeleted) {
                System.out.println("File rimosso con successo: " + fileToRemove.getAbsolutePath());
            } else {
                System.out.println("Impossibile rimuovere il file: " + fileToRemove.getAbsolutePath());
            }
        } else {
            System.out.println("File non trovato: " + fileToRemove.getAbsolutePath());
        }
    }

    public void chooseAndAddImage(Integer idArticolo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveImageToDirectory(selectedFile, idArticolo);
        }
    }

    private static void saveImageToDirectory(File selectedFile, Integer idArticolo) {
        String destinationDirectory = INITIAL_PATH + idArticolo + "\\";

        try {
            File destinationFile = new File(destinationDirectory, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(null, "Immagine salvata con successo!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dell'immagine");
        }
    }

    private static boolean isImageFile(File file) {
        // Controlla se il file è un'immagine con estensione .jpg, .png, .gif, .jpeg
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg");
    }
}
