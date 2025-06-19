CobolDataTypeConverter
A simple interactive Java utility for converting mainframe COBOL numeric data types (such as COMP, COMP-1, COMP-2, COMP-3, COMP-5, S9V99, and Zoned Decimal) from their internal (hexadecimal) storage format to human-readable Java values.

Features
Interactive menu: Choose the COBOL data type to convert.

Supports:

COMP (Binary Integer, 4 bytes)

COMP-1 (Single Precision Float, 4 bytes)

COMP-2 (Double Precision Float, 8 bytes)

COMP-3 (Packed Decimal, variable length, supports negative)

S9V99 (Packed Decimal with scale, supports negative)

COMP-5 (Binary Integer, 2/4/8 bytes, supports negative)

Zoned Decimal (simulated as ASCII digits for demo)

User input: Enter hex bytes or ASCII digits as prompted.

Robust error handling: Handles invalid input and conversion errors gracefully.

Looping menu: Perform multiple conversions until you choose to exit.

Usage
Compile the program:

bash
javac CobolDataTypeConverter.java
Run the program:

bash
java CobolDataTypeConverter
Follow the prompts:

Select the COBOL data type to convert.

Enter the required bytes or string as prompted.

View the converted value.

Example
text
Select COBOL data type to convert:
1 - COMP (Binary Integer, 4 bytes)
2 - COMP-1 (Single Precision Float, 4 bytes)
3 - COMP-2 (Double Precision Float, 8 bytes)
4 - COMP-3 (Packed Decimal)
5 - S9V99 (Packed Decimal with scale)
6 - Zoned Decimal (Simulated as ASCII digits)
7 - COMP-5 (Binary Integer, specify length)
0 - Exit
Enter choice (0-7): 1
Enter 4 hex bytes for COMP (big-endian), e.g. 00 00 30 39: 00 00 30 39
COMP (binary int) value: 12345
Supported COBOL Data Types
Option	COBOL Type	Java Output Type	Input Example
1	COMP	int (4 bytes)	00 00 30 39
2	COMP-1	float (4 bytes)	41 45 70 A4
3	COMP-2	double (8 bytes)	40 5E DD 2F 1A 9F BE 77
4	COMP-3	BigDecimal	12 34 5C
5	S9V99	BigDecimal (scaled)	01 23 45 0C
6	Zoned Decimal	String	12345
7	COMP-5	short/int/long	00 01 (2 bytes), 00 00 30 39 (4 bytes)
Notes
Hex input: Enter bytes as space-separated hex (e.g., 12 34 5C).

Negative packed decimals: Use D or B as the last nibble (e.g., 12 34 5D).

COMP-5: Supports 2, 4, or 8 bytes (short, int, long).

Zoned Decimal: Simulated as ASCII digits for demonstration.

For actual EBCDIC or mainframe integration, further enhancements may be needed.
