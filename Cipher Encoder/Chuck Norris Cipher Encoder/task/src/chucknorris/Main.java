package chucknorris;

import com.sun.jdi.request.StepRequest;

import java.util.Scanner;

public class Main {
    private static final String WRONG_ENCODING = "Encoded string is not valid.";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        label:
        while(true) {
            System.out.println("Please input operation (encode/decode/exit):");
            String command = sc.nextLine();
            switch (command) {
                case "exit":
                    break label;
                case "encode":
                    System.out.println("Input string:");
                    String message = sc.nextLine();
                    System.out.println("Encoded string:");
                    System.out.println(encode(message));
                    break;
                case "decode":
                    System.out.println("Input encoded string:");
                    String encodedMessage = sc.nextLine();
                    String decodedMessage = decode(encodedMessage);
                    if (!decodedMessage.equals(WRONG_ENCODING))
                        System.out.println("Decoded string:\n" + decodedMessage);
                    else System.out.println(WRONG_ENCODING);
                    break;
                default:
                    System.out.println("There is no '" + command + "' operation");
                    break;
            }
            System.out.println();
        }
        System.out.println("Bye!");
    }

    private static String encode(String message) {
        return binaryToEncoding(messageToBinary(message));
    }

    private static String messageToBinary(String message) {
        StringBuilder fullBinary = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            String binary = Integer.toBinaryString(message.charAt(i));
            binary = String.format("%7s", binary).replace(' ', '0');
            fullBinary.append(binary);
        }
        return fullBinary.toString();
    }

    private static String binaryToEncoding(String binary) {
        StringBuilder encoding = new StringBuilder();
        char prev = ' ';
        for (int i = 0; i < binary.length(); i++) {
            char curr = binary.charAt(i);
            if (prev != curr) {
                if (!encoding.isEmpty()) encoding.append(" ");

                encoding.append(curr == '1' ? "0 " : "00 ");
            }
            encoding.append("0");
            prev = curr;
        }
        return encoding.toString();
    }

    private static String decode(String encoding) {
        return binaryToMessage((encodingToBinary(encoding)));
    }

    private static String encodingToBinary(String encoding) {
        StringBuilder fullBinary = new StringBuilder();
        for (int i = 0; i < encoding.length(); i++) {
            try {
                if (!encoding.substring(i, i + 2).equals("0 ") && !encoding.substring(i, i + 3).equals("00 "))
                    return WRONG_ENCODING;
            } catch (Exception e) {
                return WRONG_ENCODING;
            }

            StringBuilder binary = new StringBuilder();
            boolean check = encoding.charAt(i + 1) == '0';
            String value = check ? "0" : "1";
            i += check ? 3 : 2;

            while(true) {
                char curr = encoding.charAt(i);
                if (curr != '0' && curr != ' ') return WRONG_ENCODING;
                if (curr == ' ') break;
                binary.append(value);
                i++;
                if (i == encoding.length()) break;
            }
            if (binary.isEmpty()) return WRONG_ENCODING;
            fullBinary.append(binary);
        }
        return fullBinary.toString();
    }

    private static String binaryToMessage(String binary) {
        if (binary.equals(WRONG_ENCODING) || binary.length() % 7 != 0)
            return WRONG_ENCODING;

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 7) {
            String binaryChar = binary.substring(i, i + 7);
            message.append((char) Integer.parseInt(binaryChar, 2));
        }
        return message.toString();
    }
}