# Image Processor

This program allows you to process a JPG image by applying an average color filter over square blocks of the image. You can choose between single-threaded or multi-threaded modes for processing. The program also measures the time taken for processing in milliseconds with 3 digits of accuracy.

## Requirements

- Java 8 or higher
- An image file in JPG format

## How to Use

### Running the Program

You can run the program by providing command-line arguments or interactively through the console. The program will prompt you for inputs if they are not provided as arguments.

#### Command-Line Arguments (Optional)

The program accepts the following arguments:

1. **Image File** (mandatory): The path to the image file you want to process. It must be a JPG or JPEG file.
2. **Square Size** (mandatory): The size of the square blocks that will be processed (a positive integer).
3. **Mode** (mandatory): The processing mode, either:
   - `S` for single-threaded processing.
   - `M` for multi-threaded processing.

For example, the command:

```bash
java ImageProcessor input.jpg 10 S
```

will process ```input.jpg``` in single-threaded mode with a square size of 10 pixels.

Without Command-Line Arguments (Interactive Mode)
If you do not provide arguments, the program will prompt you to input the following interactively:

1. Enter the file name: The name or path of the image file to process.
2. Enter the square size: A positive integer indicating the square size.
3. Enter the mode: S for single-threaded processing or M for multi-threaded processing.

## Example of Running the Program

```java
java ImageProcessor image.jpg 20 M
```

This will process ```image.jpg``` using multi-threaded mode with a square size of 10 pixels. Result will be saved as ```result.jpg``` file in the same folder.

Sample input:

![Image Processor](./JH_profile.jpg)

Sample output:

![Image Processor](./result.jpg)

## Notes
- The input image must be in JPG or JPEG format. If the file is not a valid image or not in JPG format, the program will ask you to re-enter the correct file.
- If the square size entered is not a positive integer, the program will ask you to re-enter it.
- If the mode is invalid (anything other than S or M), the program will ask you to re-enter it.
