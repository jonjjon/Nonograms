package edu.skku.map.pa2t1;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ItemSetter extends LinearLayout {


    TextView textView;
    ImageView imageView;

    public ItemSetter(Context context) {
        super(context);
        init(context);
    }

    public ItemSetter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item, this, true);

        textView = (TextView) findViewById(R.id.num);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void setNum(int num) {
        textView.setVisibility(VISIBLE);
        imageView.setVisibility(INVISIBLE);
        textView.setText(Integer.toString(num));

    }

    public void setImage(Bitmap img) {
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(INVISIBLE);
        imageView.setImageBitmap(img);

    }
}
