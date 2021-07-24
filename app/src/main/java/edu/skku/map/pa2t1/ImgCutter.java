package edu.skku.map.pa2t1;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

public class ImgCutter{

    int max;
    ArrayList<ArrayList<Integer>> rows;
    ArrayList<ArrayList<Integer>> cols;

    Bitmap imgOriginal;
    Bitmap[] imgAfterCutting;
    Bitmap[] imgGrayscaleSet;
    int[][] cellsBlack;
    Bitmap black;
    Bitmap white;
    int blacks;

    public ImgCutter(Bitmap img){
        this.imgAfterCutting = new Bitmap[400];
        this.imgGrayscaleSet = new Bitmap[400];
        this.cellsBlack= new int[20][20];
        int[] black = new int[400];
        this.blacks=0;
        this.imgOriginal=img;

        for(int i=0;i<400;i++){
            this.imgAfterCutting[i]=cutImg(this.imgOriginal)[i];
        }

        int n=0;
        for(Bitmap imgPieces : this.imgAfterCutting){
            this.imgGrayscaleSet[n++]=changeIntoBlackWhite(imgPieces);
        }

        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                this.cellsBlack[i][j]=answerSheet(imgGrayscaleSet)[i][j];
            }
        }
        if(blacks>0){
            blacks=blacks/400;
        }


        this.rows = new ArrayList<>();
        this.cols = new ArrayList<>();

        for(int i=0;i<20;i++){
            this.rows.add(countRowCells(cellsBlack).get(i));
        }
        for(int i=0;i<20;i++){
            this.cols.add(countColCells(cellsBlack).get(i));
        }

        int num1=0;
        int num2=0;
        num1 = countMax(this.rows);
        num2 = countMax(this.cols);
        if(num1>=num2){
            this.max=num1;
        }
        else{
            this.max=num2;
        }

        this.black = getBlack(this.imgGrayscaleSet);
        this.white = getWhite(this.imgGrayscaleSet);
    }

    public Bitmap getBlack(Bitmap[] imgSet){
        int pixel;
        int ifBlack;
        Bitmap black = null;
        int n=0;
        for(Bitmap imgPieces : imgSet){
            pixel = imgPieces.getPixel(1,1);
            ifBlack=Color.red(pixel);
            if (ifBlack == 0){
               black = imgPieces;
               break;
            }
            n++;
        }
        return black;
    }
    public Bitmap getWhite(Bitmap[] imgSet){
        int pixel;
        int ifWhite;
        Bitmap white = null;
        int n=0;
        for(Bitmap imgPieces : imgSet){
            pixel = imgPieces.getPixel(1,1);
            ifWhite=Color.red(pixel);
            if (ifWhite == 255){
                white = imgPieces;
                break;
            }
            n++;
        }
        return white;
    }

    public Bitmap[] cutImg(Bitmap bmpImg){
        //img 잘라서 배열로 저장 후 리턴
        int width,height;
        int partWidth, partHeight;
        Bitmap[] imgSet=new Bitmap[400];
        Bitmap[][] imgPieces = new Bitmap[20][20];
        int n=0;
        width = bmpImg.getWidth();
        height = bmpImg.getHeight();

        partWidth = width/20;
        partHeight = height/20;

        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++) {
                imgPieces[i][j]=Bitmap.createBitmap(bmpImg, j*partWidth,i*partHeight,partWidth, partHeight);
                imgSet[n++]= imgPieces[i][j];
            }
        }

        return imgSet; //잘린 이미지들의 모음집;
    }

    public Bitmap changeIntoBlackWhite(Bitmap bmpPiece){

        //흑백으로 조정된 이미지 조각 들어옴

        int width, height;
        width = bmpPiece.getWidth();
        height = bmpPiece.getHeight();

        int pixel;
        int total=0;
        double avgPixel=0;
        int Alpha=0;
        int pixelNum=0;
        int value;

        Bitmap bmpPieceBW = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = bmpPiece.getPixel(x, y);
                Alpha = Color.alpha(pixel);
                value = Color.red(pixel);
                total+=value;
                pixelNum++;
            }
        }
        avgPixel=(double)total/pixelNum;
        if (avgPixel > 128)
            avgPixel = 255;
        else
            avgPixel = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                bmpPieceBW.setPixel(x, y, Color.argb(Alpha, (int)avgPixel, (int)avgPixel, (int)avgPixel));
            }
        }

        return bmpPieceBW;
    }


    public int[][] answerSheet(Bitmap[] imgSet){
        int[][] answer=new int[20][20];
        int[] black=new int[400];
        int n=0;
        int pixel=0;
        int ifBlack=0;
        for(Bitmap imgPieces : imgSet){
            pixel = imgPieces.getPixel(5,5);
            ifBlack=Color.red(pixel);
            if (ifBlack == 0){
                black[n]=1; //검정이면 1.

            }
            else{
                black[n]=0;
            }
            n++;
        }

        int i=0;
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if(black[i]==1){
                    this.blacks++;
                }
                answer[x][y]=black[i++];
            }
        }
        return answer;

    }

    public  ArrayList<ArrayList<Integer>> countRowCells(int[][] answer){
        ArrayList<ArrayList<Integer>> rowsBlack = new ArrayList<>();
        for (int x = 0; x < 20; x++) {
            ArrayList<Integer>row = new ArrayList<>();
            int black=0;
            int consecutive=0;
            for (int y = 0; y < 20; y++) {
                //가로줄 검정 개수 찾는 중
                if(x==0 && y==0){ //예외처리
                    if(answer[x][y]==1){
                        black++;
                        consecutive=1;
                    }
                }
                else{
                    if(consecutive==1){
                        if(answer[x][y]==1){
                            if(y==19){
                                black++;
                                consecutive=0;
                                row.add(black);
                            }
                            black++;
                        }
                        if (answer[x][y]==0){
                            consecutive=0;
                            row.add(black);
                            black=0;
                        }
                    }
                    else{ //consecutive=0;
                        if(answer[x][y]==1){
                            black++;
                            consecutive=1;
                        }
                        else{
                            black=0;
                            consecutive=0;
                            continue;
                        }
                    }
                }


            }
            rowsBlack.add(row);
        }
        return rowsBlack;
    }

    public ArrayList<ArrayList<Integer>> countColCells(int[][] answer){
        ArrayList<ArrayList<Integer>> colsBlack = new ArrayList<>();
        for (int y = 0; y < 20; y++) {
            ArrayList<Integer>col=new ArrayList<>();
            int black=0;
            int consecutive=0;
            for (int x = 0; x < 20; x++) {
                //세로줄 검정 개수 찾는 중


                if(x==0 && y==0){ //예외처리
                    if(answer[x][y]==1) {
                        black = 1;
                        consecutive = 1;
                    }
                }
                else{
                    if(consecutive==1){
                        if(answer[x][y]==1){
                            if(x==19){
                                black++;
                                consecutive=0;
                                col.add(black);
                            }
                            black++;
                        }
                        if (answer[x][y]==0){
                            consecutive=0;
                            col.add(black);
                            black=0;
                        }

                    }
                    else{ //consecutive=0;
                        if(answer[x][y]==1){
                            black++;
                            consecutive=1;
                        }
                        else{
                            black=0;
                            consecutive=0;
                            continue;
                        }
                    }
                }
            }//x끝
            colsBlack.add(col);
        }
        return colsBlack;
    }

    public int countMax(ArrayList<ArrayList<Integer>> numList){
        int max=0;
        for(int i=0;i<numList.size();i++){
            ArrayList<Integer> temp = numList.get(i);
            if(temp.size()>= max){
                max=temp.size();
            }
        }
        if(max==0){
            return 1;
        }
        return max;
    }
}
