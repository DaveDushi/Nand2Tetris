// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.


//sets R2 to 0
@0
D=A
@R2
M=D
(LOOP)
//if R1 is 0 go to END
	@R1
	D=M
	@END
	D;JEQ
//sets R2 to R2 + R0
	@R0
	D=M
	@R2
	M=M+D
//Subtract 1 from R1
	@R1
	M=M-1
//goto LOOP
	@LOOP
	0;JMP
(END)
	@END
	0;JMP



