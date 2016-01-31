package com.liuli.aexi_android.Aexi.Control;

import com.liuli.aexi_android.Aexi.Model.Composition;
import com.liuli.aexi_android.Aexi.Model.GlyphImpl;
import com.liuli.aexi_android.Aexi.Model.Selection;

/**
 * Created by Administrator on 2015/4/29.
 */
public class InsertCommand implements Command {
    private Composition composition;

    private GlyphImpl glyph;

    public InsertCommand(Composition composition, GlyphImpl glyph) {
        this.composition = composition;
        this.glyph = glyph;
    }

    @Override
    public boolean excute() {
        if (composition == null || glyph == null)
            return false;
        composition.getSelection().deleteSelection();
        composition.getSelection().setEndIndex(Selection.UN_SELECTED);
        int insertIndex = composition.getCaret().getInsertIndex();
        composition.getCaret().setHostGlyph(glyph);
        return composition.insert(glyph, insertIndex);
    }

    @Override
    public void unExcute() {
        //TODO 实现插入的撤销方法实现
    }

    @Override
    public boolean canUndo() {
        return true;
    }
}
