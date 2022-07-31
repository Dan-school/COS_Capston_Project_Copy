package edu.usm.cos420.antenatal.model;

import java.io.Serializable;


public class Parity implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;
    
    private int gravidity;
        private int parity;

        public Parity() {
            this.parity = 0;
            this.gravidity = 0;
        }

        public Parity(int parity, int gravidity) {
            this.parity = parity;
            this.gravidity = gravidity;
        }

        public int getParity() {
            return parity;
        }

        public int getGravidity() {
            return gravidity;
        }

        public void setParity(int parity) {
            this.parity = parity;
        }

        public void setGravidity(int gravidity) {
            this.gravidity = gravidity;
        }

        @Override
        public String toString() {
            return String.format("Gravidity:%d Parity:%d", gravidity, parity);
        }
}
