/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzip2;
import bzip2.Node;
/**
 *
 * @author Wojciech
 */
public class Tree {

        Node[] k;
        int l;

        public Tree() {
            k = new Node[10];
            l = 0;
        }

        private void heap_up() {

            int i = l - 1;
            Node v = k[i];
            int j = (i - 1) / 2;
            while (i > 0 && (k[j].compareTo(v) > 0)) {
                k[i] = k[j];
                i = j;
                j = (i - 1) / 2;
            }
            k[i] = v;

        }

        private Node heap_down() {
            Node r = k[0];
            Node v = k[l - 1];
            int i = 0;
            int j = 1;
            while (j < l) {
                if ((j + 1 < l) && (k[j + 1].compareTo(k[j]) <= 0)) {
                    j++;
                }
                if (v.compareTo(k[j]) < 0) {
                    break;
                }
                k[i] = k[j];
                i = j;
                j = 2 * j + 1;
            }
            k[i] = v;
            return r;
        }

        public void add(Node x) {
            if (l + 1 == k.length) {
                Node[] k2 = new Node[k.length * 2];
                for (int i = 0; i < k.length; i++) {
                    k2[i] = k[i];
                }
                k = k2;
            }
            k[l++] = x;
            heap_up();
        }

        public Node remove() {
            if (l == 0) {
                return null;
            }

            Node t = heap_down();
            l--;
            return t;
        }

        public void print() {
            for (int i = 0; i < l; i++) {
                System.out.format("%4s \n", k[i]);
            }
        }

        public Node createHuffTree(int[] liczniki) {
            int n = 0;
            
            for (int i = 0; i < liczniki.length; i++) {
                if (liczniki[i] > 0) {
                    //System.out.println(i+" "+liczniki[i]);
                    this.add(new Node(i, liczniki[i]));
                    n++;
                }
            }
            Node x = this.remove();
            while (n > 1) {
                n--;
                Node n2 = this.remove();
                Node nn = new Node(-1, x.p + n2.p);
                nn.l = x;
                nn.r = n2;
                /*System.out.print(x+"  + ");
                System.out.print(n2+" = ");
                System.out.println(nn);*/
                x=nn;
            }
            return x;

        }
    }
