import java.util.*;

public class challange2 {

    public static class Pair {
        boolean first;
        int second;
        public Pair(boolean first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    public static void main(String[] args) {
        String s = "aaeeiioouu";

        int first = s.indexOf('a');
        int last = s.lastIndexOf('u');


        if(first == -1 || last == -1) {
            System.out.println(0);
            return;
        }

        s = s.substring(first, last + 1);

        Pair[][] data = new Pair[5][s.length()];

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                data[i][j] = new Pair(false, 0);
            }
        }

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == 'a') {
                data[0][i].first = true;
            } else if(s.charAt(i) == 'e') {
                data[1][i].first = true;
            } else if(s.charAt(i) == 'i') {
                data[2][i].first = true;
            } else if(s.charAt(i) == 'o') {
                data[3][i].first = true;
            } else if(s.charAt(i) == 'u') {
                data[4][i].first = true;
            }
        }

        System.out.println(trav(data, 0, 0, false));
    }

    public static int trav(Pair[][] data, int i, int j, boolean canGoDown) {
        int temp;

        if(i == data.length ||j == data[0].length) {
            return 0;
        }

        if(data[i][j].second != 0) {
            return data[i][j].second;
        }

        if(i < data.length - 1 && j == data[0].length - 1) {
            return data[i][j].second = -1;
        }

        if(data[i][j].first) {
            canGoDown = true;
            temp = trav(data, i, j+1, canGoDown);
            if(temp == -1) {
                return -1;
            } else {
                return temp + 1;
            }
        }

        if(!canGoDown) {
            return data[i][j].second = trav(data, i, j +1 , canGoDown);
        }

        return data[i][j].second = Math.max(trav(data, i, j+1, canGoDown), trav(data, i+1, j, canGoDown));

    }

    /** a a e e e i
     * a
     * e
     * i
     * o
     * u
     */


    /*static int[] countGroups(int[][] m, int[] t) {
        int rows = m.length;
        int columns = m.length;
        int[] result = new int[t.length];

        Arrays.sort(t);

        for(int k = t.length - 1; k >= 0; k--) {
            int current = t[k];

            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {

                }
            }
        }
    }*/

}
