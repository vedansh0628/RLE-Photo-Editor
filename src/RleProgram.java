import java.util.Scanner;

public class RleProgram {
    public static void main(String[] args) {
        System.out.println("Welcome to the RLE image encoder!");
        System.out.println("Displaying Spectrum Image:");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);
        byte[] imageData = null;
        boolean startProgram = true;
        Scanner scanner = new Scanner(System.in);

        //Boolean allows menu to be printed out each time.
        while(startProgram) {
            System.out.println("RLE Menu");
            System.out.println("--------");
            System.out.println("0. Exit");
            System.out.println("1. Load File");
            System.out.println("2. Load Test Image");
            System.out.println("3. Read RLE String");
            System.out.println("4. Read RLE Hex String");
            System.out.println("5. Read Data Hex String");
            System.out.println("6. Display Image");
            System.out.println("7. Display RLE String");
            System.out.println("8. Display Hex RLE Data");
            System.out.println("9. Display Hex Flat Data");
            System.out.println("Select a Menu Option:");
            int input = scanner.nextInt();
            if(input == 0) {
                startProgram = false;
            }
            //Takes input of a file name.
            else if(input == 1) {
                System.out.println("Enter name of file to load: ");
                String file = scanner.next();
                imageData = ConsoleGfx.loadFile(file);
            }
            //Loads the test image/
            else if(input == 2) {
                imageData = ConsoleGfx.testImage;
                System.out.println("Test image data loaded.");
            }
            //Reads decimal notation with colons.
            else if(input == 3) {
                System.out.println("Enter an RLE string to be decoded: ");
                String info = scanner.next();
                imageData = decodeRle(stringToRle(info));
            }
            //Reads hexadecimal notation without colons.
            else if(input == 4) {
                System.out.println("Enter the hex string holding RLE data: ");
                String info = scanner.next();
                imageData = decodeRle(stringToData(info));
            }
            //Reads raw data in hexadecimal notation.
            else if(input == 5) {
                System.out.println("Enter the hex string holding flat data: ");
                String info = scanner.next();
                imageData = stringToData(info);
            }
            //Displays the image.
            else if(input == 6) {
                System.out.println("Displaying image...");
                ConsoleGfx.displayImage(imageData);
            }
            //Converts data into rle with colons.
            else if(input == 7) {
                System.out.println("RLE representation: ");
                System.out.println(toRleString(encodeRle(imageData)));
            }
            //Converts data into rle hexadecimal without colons.
            else if(input == 8) {
                System.out.println("RLE hex values: ");
                System.out.println(toHexString(encodeRle(imageData)));
            }
            //Converts data into raw data into hexadecimal without colons.
            else if(input == 9) {
                System.out.println("Flat hex values: ");
                System.out.println(toHexString(imageData));
            }
        }
    }
    //Goes through the array and changes it to a hexadecimal string.
    public static String toHexString(byte[] data) {
        String output = "";
        for(int i = 0; i < data.length; i++) {
            if(data[i] == 0 || data[i] == 1 || data[i] == 2 || data[i] == 3 || data[i] == 4 || data[i] == 5 || data[i] == 6 || data[i] == 7 || data[i] == 8 || data[i] == 9) {
                output += data[i];
            }
            else if(data[i] == 10) {
                output += "a";
            }
            else if(data[i] == 11) {
                output += "b";
            }
            else if(data[i] == 12) {
                output += "c";
            }
            else if(data[i] == 13) {
                output += "d";
            }
            else if(data[i] == 14) {
                output += "e";
            }
            else if(data[i] == 15) {
                output += "f";
            }
        }
        return output;
    }
    //Counts how many different numbers there are.
    public static int countRuns(byte[] flatData) {
        int secondCount = 0;
        int count = 1;
        for(int i = 1; i < flatData.length; i++) {
            if(flatData[i] == flatData [i - 1]) {
                secondCount++;
                if(secondCount > 15) {
                    count++;
                    secondCount = 0;
                }
            }
            if(flatData[i] != flatData [i - 1]) {
                count++;
            }
        }
        return count;
    }
    //Counts how much of a value there is in a row and inputs it in an array. If a value repeats more than 15 times a new count will start.
    public static byte[] encodeRle(byte[] flatData) {
        int count = 1;
        int index = 0;
        int secondCount = 0;
        byte[] arr = new byte[countRuns(flatData) * 2];
        for(int i = 0; i < flatData.length - 1; i++) {
            if(flatData[i] == flatData[i + 1]) {
                count++;
            }
            if(count >= 15) {
                arr[index] = (byte) count;
                index++;
                arr[index] = flatData[i];
                index++;
                count = 0;
                secondCount++;
            }
            if(flatData[i] != flatData[i + 1]) {
                arr[index] = (byte) count;
                index++;
                arr[index] = flatData[i];
                index++;
                count = 1;
            }
        }
        //Gets the last 2 for the array.
        arr[index] = (byte) count;
        index++;
        arr[index] = flatData[flatData.length - 1];
        index++;
        return arr;
    }
    //Gets the length of decoded data array from the encoded data.
    public static int getDecodedLength(byte[] rleData) {
        int length = 0;
        for(int i = 0; i < rleData.length; i = i + 2) {
            length += rleData[i];
        }
        return length;
    }
    //Outputs the decoded array.
    public static byte[] decodeRle(byte[] rleData) {
        byte[] arr = new byte[getDecodedLength(rleData)];
        int index = 0;
        for(int i = 0; i < rleData.length; i += 2) {
            for(int j = 0; j < rleData[i]; j++) {
                arr[index] = rleData[i + 1];
                index++;
            }
        }
        return arr;
    }
    //Converts a string to hexadecimal format.
    public static byte[] stringToData(String dataString) {
        int index = 0;
        byte[] arr = new byte[ dataString.length()];
        for(int i = 0; i < dataString.length(); i++) {
            if(dataString.charAt(i) == '0' || dataString.charAt(i) == '1' || dataString.charAt(i) == '2' || dataString.charAt(i) == '3' || dataString.charAt(i) == '4' || dataString.charAt(i) == '5' || dataString.charAt(i) == '6' || dataString.charAt(i) == '7' || dataString.charAt(i) == '8' || dataString.charAt(i) == '9') {
                arr[index] = (byte) (dataString.charAt(i) - 48);
                index++;
            }
            else if(dataString.charAt(i) == 'a' || dataString.charAt(i) == 'b' || dataString.charAt(i) == 'c' || dataString.charAt(i) == 'd' || dataString.charAt(i) == 'e' || dataString.charAt(i) == 'f') {
                arr[index] = (byte) (dataString.charAt(i) - 87);
                index++;
            }
        }
        return arr;
    }
    //Converts an array with rle byte data to a string with colons.
    public static String toRleString(byte[] rleData) {
        String output = "";
        for(int i = 0; i<rleData.length; i += 2) {
            output += rleData[i];
            if(rleData[i + 1] == 10) {
                output += "a";
            }
            else if(rleData[i + 1] == 11) {
                output += "b";
            }
            else if(rleData[i + 1] == 12) {
                output += "c";
            }
            else if(rleData[i + 1] == 13) {
                output += "d";
            }
            else if(rleData[i + 1] == 14) {
                output += "e";
            }
            else if(rleData[i + 1] == 15) {
                output += "f";
            }
            else {
                output += rleData[i + 1];
            }
            if(i != rleData.length - 2) {
                if(i % 2 == 0) {
                    output += ":";
                }
            }
        }
        return output;
    }
    //Converts a string with colons to an array with rle byte data.
    public static byte[] stringToRle(String rleString) {
        //Splits the string where there are colons and puts them into a string array.
        String[] input = rleString.split(":");
        byte[] arr = new byte[input.length * 2];
        int index = 0;
        int value;
        for(int i = 0; i < input.length; i++) {
            //If the string only has two characters it converts the first and second characters.
            if(input[i].length() == 2) {
                value = Integer.parseInt(input[i].substring(0, 1));
                arr[index] = (byte) value;
                index++;
                if (input[i].substring(1, 2).equals("a")) {
                    value = 10;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(1, 2).equals("b")) {
                    value = 11;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(1, 2).equals("c")) {
                    value = 12;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(1, 2).equals("d")) {
                    value = 13;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(1, 2).equals("e")) {
                    value = 14;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(1, 2).equals("f")) {
                    value = 15;
                    arr[index] = (byte) value;
                    index++;
                }
                else {
                    value = Integer.parseInt(input[i].substring(1, 2));
                    arr[index] = (byte) value;
                    index++;
                }
            }
            //If a string has 3 characters it converts the first 2 characters as 1 then the 3rd character.
            if(input[i].length() == 3) {
                value = Integer.parseInt(input[i].substring(0, 2));
                arr[index] = (byte) value;
                index++;
                if (input[i].substring(2, 3).equals("a")) {
                    value = 10;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(2, 3).equals("b")) {
                    value = 11;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(2, 3).equals("c")) {
                    value = 12;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(2, 3).equals("d")) {
                    value = 13;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(2, 3).equals("e")) {
                    value = 14;
                    arr[index] = (byte) value;
                    index++;
                }
                else if (input[i].substring(2, 3).equals("f")) {
                    value = 15;
                    arr[index] = (byte) value;
                    index++;
                }
                else {
                    value = Integer.parseInt(input[i].substring(2, 3));
                    arr[index] = (byte) value;
                    index++;
                }
            }
        }
        return arr;
    }
}