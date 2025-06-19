package com.example.MfTOjava.MfToJava;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class CobolDataTypeConverter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\nSelect COBOL data type to convert:");
			System.out.println("1 - COMP (Binary Integer, 4 bytes)");
			System.out.println("2 - COMP-1 (Single Precision Float, 4 bytes)");
			System.out.println("3 - COMP-2 (Double Precision Float, 8 bytes)");
			System.out.println("4 - COMP-3 (Packed Decimal)");
			System.out.println("5 - S9V99 (Packed Decimal with scale)");
			System.out.println("6 - Zoned Decimal (Simulated as ASCII digits)");
			System.out.println("7 - COMP-5 (Binary Integer, specify length)");
			System.out.println("0 - Exit");
			System.out.print("Enter choice (0-7): ");

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number between 0 and 7.");
				continue;
			}

			if (choice == 0) {
				System.out.println("Exiting program. Goodbye!");
				break;
			}

			try {
				switch (choice) {
					case 1:
						System.out.print("Enter 4 hex bytes for COMP (big-endian), e.g. 00 00 30 39: ");
						byte[] compBytes = hexStringToByteArray(scanner.nextLine());
						int compValue = convertCompBinary(compBytes);
						System.out.println("COMP (binary int) value: " + compValue);
						break;

					case 2:
						System.out.print("Enter 4 hex bytes for COMP-1 (big-endian), e.g. 41 45 70 A4: ");
						byte[] comp1Bytes = hexStringToByteArray(scanner.nextLine());
						float comp1Value = convertComp1(comp1Bytes);
						System.out.println("COMP-1 (float) value: " + comp1Value);
						break;

					case 3:
						System.out.print("Enter 8 hex bytes for COMP-2 (big-endian), e.g. 40 5E DD 2F 1A 9F BE 77: ");
						byte[] comp2Bytes = hexStringToByteArray(scanner.nextLine());
						double comp2Value = convertComp2(comp2Bytes);
						System.out.println("COMP-2 (double) value: " + comp2Value);
						break;

					case 4:
						System.out.print("Enter packed decimal hex bytes for COMP-3, e.g. 12 34 5C or negative 12 34 5D: ");
						byte[] comp3Bytes = hexStringToByteArray(scanner.nextLine());
						System.out.print("Enter scale (number of decimal places, e.g. 0): ");
						int scale3 = Integer.parseInt(scanner.nextLine());
						BigDecimal comp3Value = convertPackedDecimalToBigDecimal(comp3Bytes, scale3);
						System.out.println("COMP-3 (packed decimal) value: " + comp3Value.toPlainString());
						break;

					case 5:
						System.out.print("Enter packed decimal hex bytes for S9V99, e.g. 01 23 45 0C or negative 01 23 45 0D: ");
						byte[] s9v99Bytes = hexStringToByteArray(scanner.nextLine());
						System.out.print("Enter scale (number of decimal places, e.g. 2): ");
						int scale5 = Integer.parseInt(scanner.nextLine());
						BigDecimal s9v99Value = convertPackedDecimalToBigDecimal(s9v99Bytes, scale5);
						System.out.println("S9V99 (packed decimal with scale) value: " + s9v99Value.toPlainString());
						break;

					case 6:
						System.out.print("Enter ASCII digits string for Zoned Decimal (e.g. 12345): ");
						String zonedDecimal = scanner.nextLine();
						System.out.println("Zoned Decimal (simulated) value: " + zonedDecimal);
						break;

					case 7:
						System.out.print("Enter hex bytes for COMP-5 (big-endian), e.g. 00 01 for 2 bytes, 00 00 30 39 for 4 bytes: ");
						byte[] comp5Bytes = hexStringToByteArray(scanner.nextLine());
						Number comp5Value = convertComp5(comp5Bytes);
						System.out.println("COMP-5 value: " + comp5Value);
						break;

					default:
						System.out.println("Invalid choice. Please enter a number between 0 and 7.");
				}
			} catch (Exception e) {
				System.out.println("Error during conversion: " + e.getMessage());
			}
		}

		scanner.close();
	}

	// COMP binary 4 bytes big-endian to int
	public static int convertCompBinary(byte[] bytes) {
		if (bytes.length != 4) throw new IllegalArgumentException("COMP binary must be 4 bytes");
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
		return buffer.getInt();
	}

	// COMP-1 4 bytes float big-endian
	public static float convertComp1(byte[] bytes) {
		if (bytes.length != 4) throw new IllegalArgumentException("COMP-1 must be 4 bytes");
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
		return buffer.getFloat();
	}

	// COMP-2 8 bytes double big-endian
	public static double convertComp2(byte[] bytes) {
		if (bytes.length != 8) throw new IllegalArgumentException("COMP-2 must be 8 bytes");
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
		return buffer.getDouble();
	}

	// COMP-3 packed decimal to BigDecimal, supports negative sign nibbles D or B
	public static BigDecimal convertPackedDecimalToBigDecimal(byte[] packedBytes, int scale) {
		if (packedBytes.length == 0) throw new IllegalArgumentException("Packed decimal bytes empty");
		StringBuilder digits = new StringBuilder();
		for (int i = 0; i < packedBytes.length; i++) {
			int highNibble = (packedBytes[i] & 0xF0) >> 4;
			int lowNibble = packedBytes[i] & 0x0F;

			if (i == packedBytes.length - 1) {
				digits.append(highNibble);
				// Sign nibble: C or F = positive, D or B = negative
				boolean negative = (lowNibble == 0x0D || lowNibble == 0x0B);
				BigDecimal value = new BigDecimal(digits.toString());
				if (negative) value = value.negate();
				return value.movePointLeft(scale);
			} else {
				digits.append(highNibble).append(lowNibble);
			}
		}
		throw new IllegalArgumentException("Invalid packed decimal data");
	}

	// COMP-5 conversion: binary integer of length 2, 4, or 8 bytes (big-endian)
	public static Number convertComp5(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
		switch (bytes.length) {
			case 2:
				return buffer.getShort(); // returns short
			case 4:
				return buffer.getInt();   // returns int
			case 8:
				return buffer.getLong();  // returns long
			default:
				throw new IllegalArgumentException("COMP-5 must be 2, 4, or 8 bytes");
		}
	}

	// Utility: Convert space-separated hex string to byte array
	public static byte[] hexStringToByteArray(String hexString) {
		String[] hexBytes = hexString.trim().split("\\s+");
		byte[] bytes = new byte[hexBytes.length];
		for (int i = 0; i < hexBytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hexBytes[i], 16);
		}
		return bytes;
	}
}
