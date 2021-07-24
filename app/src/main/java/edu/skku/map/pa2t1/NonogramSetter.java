package edu.skku.map.pa2t1;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.ArrayList;

public class NonogramSetter extends BaseAdapter  {
    private Context context;

    Bitmap white;
    Bitmap black;
    int max;
    int[][]answer;
    ArrayList<ArrayList<Integer>> rows;
    ArrayList<ArrayList<Integer>> cols;
    ArrayList<Integer> selected;

    public NonogramSetter(Context context, Bitmap white, Bitmap black, int max, int[][]answer, ArrayList<ArrayList<Integer>> rows,  ArrayList<ArrayList<Integer>> cols){
        this.context = context;
        this.white = white;
        this.black =  black;
        this.max = max;
        this.answer = new int[20][20];
        this.answer = answer;
        this.rows = new ArrayList<>();
        this.cols = new ArrayList<>();
        this.selected = new ArrayList<>();
        for(int i=0;i<20;i++){
            this.rows.add(rows.get(i));
            this.cols.add(cols.get(i));
        }
    }

    @Override
    public int getCount() {
        int size = 0;
        size = (this.max+20)*(this.max+20);
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        Exception e= new Exception();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemSetter itemSetter = new ItemSetter(context.getApplicationContext());
        int size = max + 20;

        int r = 0;
        int c = 0;

        int line = position / size;
        int pos = position % size; //0이면 첫번째 자리, 21이면 마지막자리    max~21이 그림들어가는 위치 / 0~max-1 은 숫자나 null

        if (line < max&& line>=0) {
            //아무것도 안들어가는 위치
            if (pos < max&& pos>=0) {
                return itemSetter;
            }
            //col에 검정숫자 위치
            else {


                if(pos-max>=0 && pos-max<size){
                    ArrayList<Integer> tempArr = cols.get(pos-max);
                    if(tempArr.size()>line&&tempArr.get(line)!=null){
                        itemSetter.setNum(tempArr.get(line));
                    }
                    if(tempArr.size()==0&&line==0){
                        itemSetter.setNum(0);
                    }
                }

                return itemSetter;
            }
        }

        else {
            //row 검정숫자
            if (pos < max&& pos>=0) {
                if(line-max>=0 && line-max<size){
                    ArrayList<Integer> tempArr = rows.get(line-max);
                    if(tempArr.size()>pos&&tempArr.get(pos)!=null){
                        itemSetter.setNum(tempArr.get(pos));
                    }
                    if(tempArr.size()==0&&pos==0){
                        itemSetter.setNum(0);
                    }
                }
                return itemSetter;

            }
            //그림시작
            else {
                itemSetter.setImage(white);
                for(int i=0;i<selected.size();i++){
                    if(position==selected.get(i)){
                        itemSetter.setImage(black);
                    }
                }
                return itemSetter;
            }
        }
    }

}

