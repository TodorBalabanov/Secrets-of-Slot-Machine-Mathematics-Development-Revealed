import java.util.Arrays;

public class Analytical {
    private static int scatters[] = {15,16};

    private static int regular[] = {3,4,5,6,7,8,9,10,11,12};

    private static int base[][] = {
        {7,3,11,7,11,11,6,10,10,5,12,8,4,10,10,11,11,12,8,8,12,5,11,11,9,6,8,4,9,9,11,11,11,9,8,7,6,11,11,12,10,10,12,10,1,11,8,9,6,11,11,8,5},
        {10,5,16,8,10,10,12,12,12,10,9,11,8,12,12,7,8,12,10,6,7,12,12,7,6,4,12,10,15,12,11,9,7,12,4,8,9,11,6,3,6,5,1,12,11,9,8,5,10,12,9,12,7},
        {12,12,5,11,9,11,4,6,7,6,9,10,11,9,7,7,11,9,7,11,7,12,3,11,12,6,8,4,8,12,8,11,11,9,12,5,9,6,11,9,8,12,1,12,10,12,8,5,10,15,16,10,9},
        {7,5,5,16,6,5,6,11,7,12,10,12,12,1,4,6,11,8,12,10,10,6,8,12,9,8,10,9,12,12,11,4,10,11,10,11,8,11,7,15,11,7,7,12,12,10,3,8,11,10,11,10,10},
        {9,9,7,4,6,12,10,12,6,9,10,10,9,10,10,11,4,12,12,12,9,1,6,8,6,11,10,7,11,11,5,11,12,10,7,11,12,5,7,3,11,7,7,12,12,11,5,7,11,8,11,12,8},
    };

    public static void main(String[] args) {
        long total = 0;
        int freqencies[][] = new int[20][6];
        for (int a = 0; a < base[0].length; a++) {
            for (int b = 0; b < base[1].length; b++) {
                for(int c = 0; c < base[2].length; c++) {
                    for (int d = 0; d < base[3].length; d++) {
                        for (int e = 0; e < base[4].length; e++) {
                            int stops[] = {a, b, c, d, e};
                            total++;

                            int view[][] = new int[5][3];
                            for(int col = 0; col < 5; col++) {
                                for(int row = 0; row < 3; row++) {
                                    view[col][row] = base[col][(stops[col]+row)%base[col].length];
                                }
                            }

                            for(int symbol : scatters) {
                                int count = 0;
                                for(int col = 0; col < 5; col++) {
                                    for(int row = 0; row < 3; row++) {
                                        if (view[col][row] == symbol) {
                                            count++;
                                        }
                                    }
                                }

                                if (count > 0) {
                                    freqencies[symbol][count]++;
                                }
                            }

                            if(base[0][a] == 1 && base[1][b] == 1 && base[2][c] == 1 && base[3][d] == 1 && base[4][e] == 1) {
                                continue;
                            }

                            for(int symbol : regular) {
                                int count = 0;
                                if(base[0][a] != 1) {
                                    for(int col = 0; col < 5; col++) {
                                        if (base[col][stops[col]] == symbol || base[col][stops[col]] == 1) {
                                            count++;
                                        } else {
                                            break;
                                        }
                                    }
                                } else if(base[0][a] == 1) {
                                    int substitute = 0;
                                    for(int col = 0; col < 5; col++) {
                                        if(base[col][stops[col]] != 1) {
                                            substitute = base[col][stops[col]];
                                            break;
                                        }
                                    }

                                    for(int col = 0; substitute==symbol && col < 5; col++) {
                                        if (base[col][stops[col]] == substitute || base[col][stops[col]] == 1) {
                                            count++;
                                        } else {
                                            break;
                                        }
                                    }
                                }

                                if (count >= 3) {
                                    freqencies[symbol][count]++;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total Combinations: " + total);
        System.out.println("Number of Observations: " + Arrays.deepToString(freqencies)
            .replace("[[","[\n [")
            .replace("]]","]\n]")
            .replace("], [", "],\n ["));
    }
}
