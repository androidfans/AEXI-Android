package com.liuli.aexi_android.Aexi.Model;

import android.graphics.Canvas;
import android.graphics.Color;

import com.liuli.aexi_android.Aexi.Control.Compositor;
import com.liuli.aexi_android.Aexi.Control.StandardCompositor;
import com.liuli.aexi_android.Aexi.Interface.CaretListener;
import com.liuli.aexi_android.Aexi.Interface.CompositionListener;
import com.liuli.aexi_android.Aexi.Interface.GlyphListener;

import java.util.List;

/**
 * Created by Liuli on 2015/3/21.
 */
public class Composition extends GlyphImplGroup implements CaretListener,GlyphListener {
    private Compositor compositor;
    private Document document;
    private CompositionListener compositionListener;
    private Caret caret;
    private Selection selection = new Selection(this);
    private static Composition composition = new Composition();

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    private Composition() {
        init();
    }

    public void init() {
        document = new Document();
        //设置页面的大小
        x = 50;
        y = 100;
        height = 600;
        width = 300;

        //TODO 改成工厂模式,使用配置文件生成
        Compositor compositor = new StandardCompositor();
        compositor.setComposition(this);
        setCompositor(compositor);
        compositor.compose();
        Caret caret = Caret.getInstance();
        caret.setComposition(this);
        setCaret(caret);
        caret.setHostGlyph(null);
    }

    public void setCompositor(Compositor compositor) {
        this.compositor = compositor;
    }

    public Caret getCaret() {
        return caret;
    }

    public void setCaret(Caret caret) {
        this.caret = caret;
    }

    @Override
    public boolean append(GlyphImpl glyph) {
        //TODO 不可能只有一个page需要改进
        //TODO 不需要重新new一个frame
        glyph.setX(x);
        glyph.setY(y);
        glyph.setWidth(width);
        glyph.setHeight(height);
        super.append(glyph);
        if (compositionListener != null)
            compositionListener.documentRefresh(this);
        return true;
    }

    public void setCompositionListener(CompositionListener compositionListener) {
        this.compositionListener = compositionListener;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (compositor != null) {
            compositor.compose();
            caret.calculateFrame();
        }
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (compositor != null) {
            compositor.compose();
            caret.calculateFrame();
        }
    }

    @Override
    public void CaretRefresh(Glyph glyph) {
        if (compositor != null)
            compositor.compose();
        if (compositionListener != null)
            compositionListener.documentRefresh(this);
    }

    @Override
    public boolean insert(GlyphImpl glyph, int index) {
        if (compositor == null) {
            return false;
        }
        document.add(index,glyph);
        compositor.compose();
        caret.setHostGlyph(glyph);
        return true;
    }

    @Override
    public boolean hitRect(int x, int y) {
        List<GlyphImpl> children = getChildren();
        for (Glyph glyph : children) {
            glyph.hitRect(x, y);
        }
        return true;
    }

    @Override
    public Glyph remove(int index) {
        Glyph glyph = document.remove(index);
        if (compositor != null) {
            compositor.compose();
        }
        return glyph;
    }

    public static Composition getInstance() {
        return composition;
    }

    @Override
    public void glyphRefresh() {
        if (compositor != null) {
            compositor.compose();
        }
    }
}
