// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

//If key is not pressed then go to PRESSED
(START)
    @KBD
    D=M
    @PRESSED
    D;JGT
//Turn screen white
    @i //set i=0
    M=0  
    (WHITE)  
        @i
        D = M
        @SCREEN
        A = D+A //A=@screen+i
        M= 0  //Set that pixel to white
        @i
        D=M
        M=M+1 //i++
        @8191
        D=D-A //check if i is < 8192, if less loop back to WHITE, else loop to START
        @WHITE
        D;JLT
    @START
    0;JMP
    //turn the screen black 
    (PRESSED)
        @j //set j=0
        M=0  
        (BLACK)
            @j
            D = M
            @SCREEN
            A = D+A //A=@screen+j
            M= -1 //set the pixel to black
            @j
            D=M
            M=M+1 //j++
            @8191
            D=D-A //check if i is < 8192, if less loop back to BLACK, else loop to START
            @BLACK
            D;JLT
    @START
    0;JMP