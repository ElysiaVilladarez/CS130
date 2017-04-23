Assumptions:
1. The end token for this program is $
2. The input must be a .txt file
3. The following will invoke a BADLY FORMED NUMBER error:
	a.) an exponent with fraction or decimal
	b.) (digit)*(e)(not digit). For example: 1ef
	c.) (digit)*(period)(not digit + not space/newline). For example: 5.=
4. The following will invoke an UNTERMINATED STRING error:
	a.) (")(any)*(newline) + (')(any)*(newline)
	b.) (")(any)*(EOF) + (')(any)*(EOF)
5. The '#' symbol denotes a comment. The comment is terminated at newline.
6. When the parser encounters an error, the program immediately terminates with a message of what the error is.
	a.) IF x > 2 PRINT("TRUE"); will invoke "ER: NO LEFT PARENTHESIS"
	b.) result = 3 will invoke "ER: NO SEMICOLON"
	c.) 33.3 = IF(x > 2); will invoke "ER: NOT INCLUDED IN SCOPE"