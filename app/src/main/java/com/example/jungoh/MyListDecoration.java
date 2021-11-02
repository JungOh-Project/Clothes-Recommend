package com.example.jungoh;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MyListDecoration extends RecyclerView.ItemDecoration {
    //리스트뷰에 띄운 사진들 간격 띄우기
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 30;
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()) {
            outRect.right = 10;
        }
    }
}
