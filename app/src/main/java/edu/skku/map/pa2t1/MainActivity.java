package edu.skku.map.pa2t1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static edu.skku.map.pa2t1.ApiSearchImage.get;
import java.net.URL;
public class MainActivity extends AppCompatActivity {
    Button search, gallery;
    EditText keyword;
    Bitmap bitmapOriginal;
    //TextView test;
    GridView gridView;
    NonogramSetter nonogram;
    NonogramSetter nonogramInit;

    int[][] answer;
    int[][] clicked;
    int blackNum;
    int clickedNum;
    int size;
    int max;
    private  static final int REQUEST_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blackNum=0;
        clickedNum=0;

        answer = new int[20][20];
        clicked= new int[20][20];
        //test = (TextView)findViewById(R.id.test);

        gridView = (GridView)findViewById(R.id.gridView);


        search = (Button)findViewById(R.id.btnSearch);
        gallery = (Button)findViewById(R.id.btnGallery);
        keyword = (EditText)findViewById(R.id.EditTextQuery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, REQUEST_CODE);
                //이게 실행되고 나서 onActivityResult 에서 사진 선택

            }

        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = keyword.getText().toString();

                String clientId = "6BJ9bEpQwylEfKrqfCsK";
                String clientSecret = "BF2iGD48Dj";

                Thread imgSearchThread = new Thread() {
                    @Override
                    public void run() {
                        String text = null;
                        try {
                            text = URLEncoder.encode(query, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException("검색어 인코딩 실패", e);
                        }

                        String apiURL = "https://openapi.naver.com/v1/search/image?query=" + text;    // json 결과
                        Map<String, String> requestHeaders = new HashMap<>();
                        requestHeaders.put("X-Naver-Client-Id", clientId);
                        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                        String responseBody = get(apiURL, requestHeaders);

                        Gson gsonImg = new GsonBuilder().create();
                        final DataModel imgData = gsonImg.fromJson(responseBody, DataModel.class);


                        try{
                        URL url = new URL(imgData.getItems()[0].link);
                        HttpURLConnection imgConnection = (HttpURLConnection)url.openConnection();
                        imgConnection.setDoInput(true);
                        imgConnection.connect();
                        InputStream imgStream = imgConnection.getInputStream();
                        bitmapOriginal = BitmapFactory.decodeStream(imgStream);
                        imgStream.close();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nonogram = sequence(bitmapOriginal);
                                gridView.setAdapter(nonogram);
                            }
                        });

                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                };

                imgSearchThread.start();
                try{
                    imgSearchThread.join();

                }catch (InterruptedException e){
                    e.printStackTrace();
                }



            }//onclick


        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //짧게 누르는 옵셥!
                if(blackNum==0&&clickedNum==0){
                    Toast.makeText(getApplicationContext(), "FINISH!", Toast.LENGTH_LONG).show();
                }

                //정답이 눌리면 원래 이미지 반환
                //오답이 눌리면 리셋
                int line = position/size;
                int pos = position%size;
                if(line-max>=0 &&pos-max>=0 &&line-max<size && pos-max<size){
                    if(answer[line-max][pos-max]==1){

                        clicked[line-max][pos-max]=1;
                        clickedNum++;

                        nonogram.selected.add(position);
                        gridView.setAdapter(nonogram);

                        if(clickedNum==blackNum){
                            if(checkIfFinished(answer, clicked)==1){
                                Toast.makeText(getApplicationContext(), "FINISH!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Wrong - "+blackNum , Toast.LENGTH_SHORT).show();
                        nonogram.selected.clear();
                        clickedNum=0;
                        gridView.setAdapter(nonogram);
                    }
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream imgStreamGallery = getContentResolver().openInputStream(data.getData());
                    bitmapOriginal = BitmapFactory.decodeStream(imgStreamGallery);
                    imgStreamGallery.close();

                    nonogram = sequence(bitmapOriginal);
                    gridView.setAdapter(nonogram);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



    }
    public int checkIfFinished(int[][] answerSet , int[][] clickedSet){
        int check=0;
        for(int i=0;i<20;i++){
            for(int j=0;i<20;i++){
                if(answerSet[i][j]!=clickedSet[i][j]) {
                    check = 1;
                    return -1;
                }
            }
        }

        if(check==0){
            return 1;
        }
        else {
            return -1;
        }
    }

    public NonogramSetter sequence(Bitmap img){
        //1.이미지 겟또

        //2. 흑백변환
        ImgGrayscale imgGrayscale = new ImgGrayscale(img);
        //3. 이미지 짜르기
        Bitmap imgGray = imgGrayscale.bmpImg;
        ImgCutter imgCutter = new ImgCutter(imgGray);
        //4. 그리드뷰 적용
        size = imgCutter.max +20;
        max= imgCutter.max;
        blackNum = imgCutter.blacks;
        answer = imgCutter.cellsBlack;

        gridView.setNumColumns(size);

        NonogramSetter nonogramSetter = new NonogramSetter(getApplicationContext(),imgCutter.white , imgCutter.black, imgCutter.max,
            imgCutter.cellsBlack,imgCutter.rows, imgCutter.cols);

        return nonogramSetter;
    }

}//class 끝
