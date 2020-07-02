package com.sourav.stories.Stuffs;

import android.content.Context;
import android.util.AttributeSet;

import net.dankito.richtexteditor.android.command.BlockQuoteCommand;
import net.dankito.richtexteditor.android.command.BoldCommand;
import net.dankito.richtexteditor.android.command.InsertBulletListCommand;
import net.dankito.richtexteditor.android.command.ItalicCommand;
import net.dankito.richtexteditor.android.command.UnderlineCommand;
import net.dankito.richtexteditor.android.toolbar.EditorToolbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompactToolbar extends EditorToolbar {

    public CompactToolbar(@NotNull Context context) {
        super(context);
        initToolbar();
    }

    public CompactToolbar(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initToolbar();
    }

    public CompactToolbar(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initToolbar();
    }

    public CompactToolbar(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initToolbar();
    }

    private void initToolbar(){
        addCommand(new BoldCommand());
        addCommand(new ItalicCommand());
        addCommand(new UnderlineCommand());
        addCommand(new BlockQuoteCommand());
        addCommand(new InsertBulletListCommand());

    }

}
