package it.unisalento.myfood.Business;

import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Immagine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ImmagineBusiness {

    private static ImmagineBusiness instance = new ImmagineBusiness();

    public final static String INITIAL_PATH = "src/immagini/articoli/";
    public final static String TEMP_FOLDER_PATH = "src/immagini/temp/";

    private Immagine immagine;

    private ImmagineBusiness() {}

    public static ImmagineBusiness getInstance() {
        return instance;
    }

    public ArrayList<Immagine> showImagesPerArticolo(Integer idArticolo) {
        ArrayList<Immagine> immagini = new ArrayList<>();
        String path = INITIAL_PATH + idArticolo + "/";

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

    public File chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private static void saveImageToDirectory(File selectedFile, Integer idArticolo) {
        String destinationDirectory = INITIAL_PATH + idArticolo + "/";

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

    public ArrayList<File> getImagesPerViewArticolo() {
        ArrayList<File> immaginiProdotto = new ArrayList<>();
        Integer idArticoloSelezionato = ((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getId();

        String directoryPath = INITIAL_PATH + idArticoloSelezionato + "/";

        // Ottieni un array dei nomi dei file nella cartella
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        // Se la cartella non esiste o è vuota, esci
        if (files == null) {
            System.out.println("La cartella non esiste o è vuota.");
            return null;
        }

        // Aggiungi i nomi dei file all'ArrayList
        for (File file : files) {
            if (file.isFile()) { // controlla se è un file e non una directory
                immaginiProdotto.add(file);
            }
        }

        return immaginiProdotto;
    }

    public void createFolderAndSaveImages(ArrayList<File> fotoScelte) {
        String newPath = INITIAL_PATH + ArticoloDAO.getInstance().getLastInsertId();

        // Creazione della nuova cartella
        File newFolder = new File(newPath);
        if (!newFolder.exists()) {
            boolean success = newFolder.mkdirs();
            if (!success) {
                System.err.println("Errore durante la creazione della cartella.");
                return;
            }
        }

        // Arraylist di File con i file da duplicare
        // Itera sui file e duplicali nella nuova cartella
        for (File file : fotoScelte) {
            try {
                // Ottieni il nome del file
                String fileName = file.getName();
                // Crea il percorso di destinazione per il nuovo file
                Path destinationPath = Paths.get(newPath + "/", fileName);
                // Copia il file nella nuova cartella
                Files.copy(file.toPath(), destinationPath);
            } catch (IOException e) {
                System.err.println("Errore durante la duplicazione del file: " + e.getMessage());
            }
        }
    }

    private static void emptyFolder(Integer idArticolo) {
        String percorsoCartella = INITIAL_PATH + idArticolo + "/";

        // Creazione dell'oggetto File per la cartella
        File cartella = new File(percorsoCartella);

        // Verifica se il percorso specificato esiste ed è una cartella
        if (cartella.exists() && cartella.isDirectory()) {
            // Ottieni tutti i file nella cartella
            File[] files = cartella.listFiles();

            // Verifica se l'elenco dei file non è nullo
            if (files != null) {
                // Elimina ogni file nella cartella
                for (File file : files) {
                    if (!file.isDirectory()) { // Assicurati che non sia una sottocartella
                        file.delete(); // Elimina il file
                    }
                }
            }
        } else {
            System.out.println("Il percorso specificato non esiste o non è una cartella.");
        }
    }

    public void saveNewImages(ArrayList<File> newFotoScelte, Integer idArticolo) {
        saveFileInTempFolder(newFotoScelte);
        emptyFolder(idArticolo);

        String percorsoCartellaDestinazione = INITIAL_PATH + idArticolo + "/";

        // Creazione degli oggetti Path per le directory sorgente e di destinazione
        Path directorySorgente = Paths.get(TEMP_FOLDER_PATH);
        Path directoryDestinazione = Paths.get(percorsoCartellaDestinazione);

        try {
            // Copia dei file dalla directory sorgente alla directory di destinazione
            Files.walk(directorySorgente)
                    .filter(Files::isRegularFile) // Filtra solo i file (escludendo le directory)
                    .forEach(file -> {
                        try {
                            Path destinazioneFile = directoryDestinazione.resolve(directorySorgente.relativize(file));
                            Files.createDirectories(destinazioneFile.getParent()); // Crea le directory di destinazione se non esistono
                            Files.copy(file, destinazioneFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            System.err.println("Errore durante la copia del file: " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Errore durante la copia dei file: " + e.getMessage());
        }

        emptyTempFolder();
    }

    private static void saveFileInTempFolder(ArrayList<File> newFotoScelte) {
        // Creazione della cartella di destinazione se non esiste
        File cartellaDestinazione = new File(TEMP_FOLDER_PATH);
        if (!cartellaDestinazione.exists()) {
            cartellaDestinazione.mkdirs(); // Crea la cartella se non esiste
        }

        // Copia dei file nella cartella di destinazione
        for (File file : newFotoScelte) {
            try {
                // Ottieni il nome del file
                String nomeFile = file.getName();

                // Crea il percorso di destinazione per il file nella cartella di destinazione
                File destinazioneFile = new File(cartellaDestinazione, nomeFile);

                // Copia il file nella cartella di destinazione
                Files.copy(file.toPath(), destinazioneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Impossibile salvare il file: " + e.getMessage());
            }
        }
    }

    private static void emptyTempFolder() {
        // Creazione dell'oggetto File per la cartella
        File cartella = new File(TEMP_FOLDER_PATH);

        // Verifica se il percorso specificato esiste ed è una cartella
        if (cartella.exists() && cartella.isDirectory()) {
            // Ottieni tutti i file nella cartella
            File[] files = cartella.listFiles();

            // Verifica se l'elenco dei file non è nullo
            if (files != null) {
                // Elimina ogni file nella cartella
                for (File file : files) {
                    // Il file non è una sottocartella
                    if (!file.isDirectory()) {
                        file.delete(); // Elimina il file
                    }
                }
            }
        } else {
            System.out.println("Il percorso specificato non esiste o non è una cartella.");
        }
    }

    public boolean updateFolder(Integer idArticolo){
        String nomeCartella = String.valueOf(idArticolo);

        String newId = String.valueOf(ArticoloDAO.getInstance().getLastInsertId());

        File cartella = new File(ImmagineBusiness.INITIAL_PATH);

        if (cartella.exists() && cartella.isDirectory()) {

            File[] cartelle = cartella.listFiles();
            if (cartelle != null) {
                for (File f : cartelle) {
                    if (f.isDirectory() && f.getName().equals(nomeCartella)) {
                        File nuovaCartella = new File(f.getParent(), newId);
                        return f.renameTo(nuovaCartella);
                    }
                }
            }
        }
        return true;
    }
}
