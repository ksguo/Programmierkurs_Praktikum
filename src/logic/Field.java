package logic;



    public class Field {

        private int color;
        private int row;
        private int col;


        private boolean isConnected;

        private boolean isConnectedByPC;





        public Field(int row, int col, int color) {
            this.row = row;
            this.col = col;
            this.color = color;


        }








        public void setConnected(boolean isConnected) {
            this.isConnected = isConnected;
        }

        public boolean isConnected() {
            return isConnected;
        }

        public boolean isConnectedByPC() {
            return isConnectedByPC;
        }

        public void setConnectedByPC(boolean isConnectedByPC) {
            this.isConnectedByPC = isConnectedByPC;
        }





        /*
         * Getter und Setter
         */
        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
}
