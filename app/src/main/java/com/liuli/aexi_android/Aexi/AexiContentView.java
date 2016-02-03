package com.liuli.aexi_android.Aexi;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.liuli.aexi_android.Aexi.Control.AexiInputConnection;
import com.liuli.aexi_android.Aexi.Model.Caret;
import com.liuli.aexi_android.Aexi.Model.Character;
import com.liuli.aexi_android.Aexi.Model.Glyph;
import com.liuli.aexi_android.Aexi.Model.LineBreaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/31 0031.
 */
public class AexiContentView extends View {

    private Paint paint;

    private List<Glyph> children;

    private Caret caret;

    private float textSize;
    public AexiContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        children = new ArrayList<>();
        textSize = 40f;
        caret = Caret.getInstance();

        setFocusable(true);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new AexiInputConnection(this, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }

    public void onFunctionalKeyTyped(KeyEvent keyEvent) {
        //这里涉及到长按检测,先暂时放着 不做
        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
            switch (keyEvent.getKeyCode()) {
                case 66:
                    Log.i("ime", "66");
                    children.add(new LineBreaker());
                    break;
            }
        }
    }

    public void onTextInputed(String newText) {
        Log.i("ime", "文字输入事件 : " + newText);
        children.add(new Character(newText,50f,paint));
    }
}