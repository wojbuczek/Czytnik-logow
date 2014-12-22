/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzip2;

/**
 *
 * @author Wojciech
 */
public class Node implements Comparable {

        public Node l;
        public Node r;
        public long p;
        public int k;

        public Node(int k, long p) {
            this.p = p;
            this.k = k;
            this.l = this.r = null;
        }

        @Override
        public int compareTo(Object t) {
            return (int) (this.p - ((Node) t).p);
        }

        public String toString() {
            return k + ": " + p;
        }
    }

