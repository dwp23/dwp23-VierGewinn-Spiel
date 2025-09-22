class ConnectFour implements Clerk {
    final String ID;
    LiveView view;
    final int width, height;
    final int cols = 7;

    ConnectFour(LiveView view) {
        this.view = view;
        this.width = 800;
        this.height = 800;
        ID = Clerk.getHashID(this);
        Clerk.load(view, "views/ConnectFour/connectFour.js");
        Clerk.write(view, "<canvas id='connectFourCanvas" + ID + "' width='" + this.width + "' height='" + this.height
                + "' style='border:1px solid #000; border-radius : 15px;'></canvas>");
        Clerk.script(view,
                "const cF" + ID + " = new ConnectFour(document.getElementById('connectFourCanvas" + ID + "'));");
    }

    ConnectFour() {
        this(Clerk.view());
    }

    ConnectFour move(int turn, int col, int row) {
        Clerk.call(view, "cF" + ID + ".drawToken(" + (turn == -1) + ", " + col + ", " + row + ")");

        return this;
    }

    ConnectFour show(String message) {
        Clerk.call(view, "cF" + ID + ".show('" + message + "')");
        return this;
    }

    

}