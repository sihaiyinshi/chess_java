package util;

public class util {

    public util(){

    }


    public String grid2loc(int i, int j){
        char column= (char) ('a'+j);
        int row=8-i;
        return column+""+row;
    }

    public int[] loc2grid(String loc){
        int[] array=new int[2];
        array[0]='8'-loc.charAt(1);
        array[1]=loc.charAt(0)-'a';
        return array;
    }

    public boolean onBoard(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }


}
