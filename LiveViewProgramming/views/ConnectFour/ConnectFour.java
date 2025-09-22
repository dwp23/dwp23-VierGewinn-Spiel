class ConnectFour {
    constructor(canvas) {
        this.canvas = canvas; // Reference to the canvas element
        this.ctx = canvas.getContext('2d'); // Horizontal margin for the board
        this.marginWidth = this.canvas.width / 16;  // Horizontal margin for the board
        this.marginHeight = this.canvas.height / 8; // Vertical margin for the board
        this.fieldWidth = (this.canvas.width - 2 * this.marginWidth) / 7;  // Width of each board cell
        this.fieldHeight = (this.canvas.height - 2 * this.marginHeight) / 6;  // Height of each board cell
        this.board();
        this.drawLabels();
    }


    // Draws the game board with a blue background and white circles for each cell
    // a1
    board() {

        this.ctx.fillStyle = 'blue';   // Background color of the board
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

      // Iterate over columns and rows to draw circles for each board cell
        for (let col = 0; col < 7; col += 1) { 
            for (let row = 0; row < 6; row += 1) {
            const x = this.marginWidth + col * this.fieldWidth; // X-coordinate of the cell
            const y = this.marginHeight + row * this.fieldHeight; // Y-coordinate of the cell

            this.ctx.beginPath();
            this.ctx.fillStyle = "white"; // Color for the empty cell
            this.ctx.arc(x + this.fieldWidth / 2,
                y + this.fieldHeight / 2,
                Math.min(this.fieldHeight - 20, this.fieldWidth - 20) / 2,
                Math.PI * 2,
                0,
                false);
            this.ctx.fill(); // Fill the circle
            this.ctx.strokeStyle = "blue";
            this.ctx.stroke();
        }
    }
    } 
    // a1

    // Draws a token (red or yellow) in the specified column and row
    // a2
    async drawToken(isTurn, col, row) {
        // Calls up the animation to draw the pawn
        await this.animateToken(isTurn, col, row);
    }

    // a2


    //Draw the token of player 1 (red) at the indicated position.
    // a3
    drawP1(x, y) {
        this.ctx.beginPath();
        this.ctx.fillStyle = "red"; 
        this.ctx.arc(x + this.fieldWidth / 2, y + this.fieldHeight / 2, 
            Math.min(this.fieldHeight - 20, this.fieldWidth - 20) / 2, 
            Math.PI * 2, 
            0, 
            false);
        this.ctx.fill();
        this.ctx.stroke();
    }
 // a3


     //Draw the token of player 2 (yellow) at the indicated position.
 // a4
    drawP2(x, y) {
        this.ctx.beginPath();
        this.ctx.fillStyle = "yellow";  
        this.ctx.arc(x + this.fieldWidth / 2, y + this.fieldHeight / 2, 
            Math.min(this.fieldHeight - 20, this.fieldWidth - 20) / 2, 
            Math.PI * 2,
             0,
              false);
        this.ctx.fill();
        this.ctx.stroke();
    }
// a4
   

    
 
    





}