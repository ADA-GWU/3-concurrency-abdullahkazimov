import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessor {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Ask the user for the image file name if it's not provided as an argument
        String fileName;
        while (true) {
            if (args.length > 0) {
                fileName = args[0];
            } else {
                System.out.print("Enter the file name: ");
                fileName = scanner.nextLine();
            }

            File file = new File(fileName);

            // Check if the file exists and is a valid image file (.jpg or .jpeg)
            if (!file.exists() || file.isDirectory()) {
                System.out.println("Error: File does not exist. Please enter a valid file name.");
            } else if (!fileName.toLowerCase().endsWith(".jpg")) {
                System.out.println("Error: File is not a JPG file. Please enter a valid JPG file.");
            } else {
                break; // Exit loop when valid file is provided
            }
        }

        // Ask the user for the square size
        int squareSize;
        while (true) {
            try {
                if (args.length > 1) {
                    squareSize = Integer.parseInt(args[1]);
                } else {
                    System.out.print("Enter the square size (positive integer): ");
                    squareSize = Integer.parseInt(scanner.nextLine());
                }

                // Validate square size to ensure it's a positive integer
                if (squareSize > 0) {
                    break;
                } else {
                    System.out.println("Error: Square size must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a positive integer.");
                args = new String[0]; // Reset arguments to force console input again
            }
        }

        // Ask the user for the mode (single-threaded or multi-threaded)
        String mode;
        while (true) {
            if (args.length > 2) {
                mode = args[2].toUpperCase();
            } else {
                System.out.print("Enter the mode (S for single-threaded, M for multi-threaded): ");
                mode = scanner.nextLine().toUpperCase();
            }

            // Validate mode input to ensure it's either 'S' or 'M'
            if (mode.equals("S") || mode.equals("M")) {
                break;
            } else {
                System.out.println("Error: Invalid mode. Please enter 'S' or 'M'.");
                args = new String[0]; // Reset arguments to force console input again
            }
        }

        // Load the image based on the file name provided
        BufferedImage image = ImageIO.read(new File(fileName));

        // Depending on the mode, process the image either single-threaded or multi-threaded
        if (mode.equals("S")) {
            processSingleThreaded(image, squareSize);
        } else {
            processMultiThreaded(image, squareSize);
        }

        // Save the processed image to a new file
        File outputFile = new File("result.jpg");
        ImageIO.write(image, "jpg", outputFile);
        System.out.println("Processing complete. Result saved as result.jpg");
    }

    // Method to process the image in a single-threaded way
    private static void processSingleThreaded(BufferedImage image, int squareSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate over the image in square blocks and apply the average color
        for (int y = 0; y < height; y += squareSize) {
            for (int x = 0; x < width; x += squareSize) {
                applyAverageColor(image, x, y, squareSize);
            }
        }
    }

    // Method to process the image in a multi-threaded way
    private static void processMultiThreaded(BufferedImage image, int squareSize) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores); // Use one thread per core

        int width = image.getWidth();
        int height = image.getHeight();

        // Submit tasks for each square block of the image to be processed
        for (int y = 0; y < height; y += squareSize) {
            for (int x = 0; x < width; x += squareSize) {
                final int fx = x;
                final int fy = y;
                executor.submit(() -> applyAverageColor(image, fx, fy, squareSize)); // Process each block concurrently
            }
        }

        // Shut down the executor when all tasks are finished
        executor.shutdown();
        while (!executor.isTerminated()) {} // Wait for all threads to complete
    }

    // Method to calculate and apply the average color for a square block
    private static void applyAverageColor(BufferedImage image, int startX, int startY, int squareSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Calculate the boundaries of the square block (make sure it's within image bounds)
        int endX = Math.min(startX + squareSize, width);
        int endY = Math.min(startY + squareSize, height);

        // Initialize color accumulation variables
        int red = 0, green = 0, blue = 0, count = 0;

        // Iterate over the pixels in the square block to calculate the average color
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Color color = new Color(image.getRGB(x, y)); // Get the color of the pixel
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
                count++;
            }
        }

        // Compute the average red, green, and blue values
        int avgRed = red / count;
        int avgGreen = green / count;
        int avgBlue = blue / count;

        // Create the average color and apply it to the entire block
        Color avgColor = new Color(avgRed, avgGreen, avgBlue);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                image.setRGB(x, y, avgColor.getRGB()); // Set each pixel in the block to the average color
            }
        }
    }
}
