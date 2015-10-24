
package com.example.richtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.baidu.android.voicedemo.Config;
import com.baidu.android.voicedemo.Constants;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import flytv.compos.run.R;
import flytv.ext.utils.PlayerAmrUtil;

@SuppressLint("NewApi")
public class RichEditText extends EditText implements EditorActionModeListener {
    private static final String TAG = "RichEditText";
    // public Effect<Boolean> BOLD = new StyleEffect(Typeface.BOLD);
    // public static final Effect<Boolean> ITALIC = new StyleEffect(
    // Typeface.ITALIC);

    public static final Effect<Boolean> UNDERLINE = new UnderlineEffect();
    public static final Effect<Boolean> STRIKETHROUGH = new StrikethroughEffect();
    public static final Effect<Layout.Alignment> LINE_ALIGNMENT = new LineAlignmentEffect();
    public static final Effect<String> TYPEFACE = new TypefaceEffect();
    public static final Effect<Boolean> SUPERSCRIPT = new SuperscriptEffect();
    public static final Effect<Boolean> SUBSCRIPT = new SubscriptEffect();

    private static final ArrayList<Effect<?>> EFFECTS = new ArrayList<Effect<?>>();
    private boolean isSelectionChanging = false;
    private OnSelectionChangedListener selectionListener = null;
    private boolean actionModeIsShowing = false;
    private EditorActionModeCallback.Native mainMode = null;
    private boolean forceActionMode = false;
    private boolean keyboardShortcuts = true;

    private BaiduASRDigitalDialog mDialog = null;


    Context context;
    public ArrayList<Object> attArray;
    private ArrayList<Object> oldAttArray = new ArrayList<Object>();
    boolean isLongClickModule = false;
    float startX, startY;
    Timer timer;

    static {
        EFFECTS.add(UNDERLINE);
        EFFECTS.add(STRIKETHROUGH);
        EFFECTS.add(SUPERSCRIPT);
        EFFECTS.add(SUBSCRIPT);
        EFFECTS.add(LINE_ALIGNMENT);
        EFFECTS.add(TYPEFACE);
    }
    private Runnable mLongPressRunnable;
    private boolean isEdit = false;

    private int off;
    private boolean readOnly;

  
    public RichEditText(Context context) {
        super(context);
        this.context = context;

        attArray = new ArrayList<Object>();

        mLongPressRunnable = new Runnable() {
            public void run() {
                isEdit = true;
                Log.e("Runnable", "isEdit");
            }
        };

    }  

    public void init(Context context, boolean readOnly, ArrayList<Object> oldattArray) {
        this.context = context;
        this.readOnly = readOnly;
        attArray = new ArrayList<Object>();
        this.oldAttArray = oldattArray;
    }

 
    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RichEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
        isEdit = true;
    }

    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	
        Layout layout = getLayout();
        int line = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	  System.out.println("ACTION_DOWN");
                isLongClickModule = false;
                line = layout.getLineForVertical(getScrollY() + (int) ev.getY());
                off = layout.getOffsetForHorizontal(line, (int) ev.getX());
                startX = ev.getX();
                startY = ev.getY();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isLongClickModule = true;
                    }
                }, 600);

                if (isLongClickModule) {
                    int curOff = layout.getOffsetForHorizontal(line,
                            (int) ev.getX());
                    Selection.setSelection(getEditableText(), off, curOff);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (readOnly) {
                    break;
                }
                double deltaX = Math.sqrt((ev.getX() - startX)
                        * (ev.getX() - startX) + (ev.getY() - startY)
                        * (ev.getY() - startY));
                if (deltaX > 5 && timer != null) {
                    timer.cancel();
                    timer = null;
                }
                line = layout.getLineForVertical(getScrollY() + (int) ev.getY());
                int curOff = layout.getOffsetForHorizontal(line, (int) ev.getX());

                if (isLongClickModule) {
                    if (off > 0) {
                        int start = 0;
                        int end = 0;
                        if (off < curOff) {
                            start = off;
                            end = curOff + 1;

                        } else if (off > curOff) {
                            start = curOff;
                            end = off + 1;
                        }
                        try {
							Selection.setSelection(getEditableText(), start, end);
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                line = layout.getLineForVertical(getScrollY() + (int) ev.getY());
                int curOff1 = layout.getOffsetForHorizontal(line, (int) ev.getX());
                if (timer!=null) timer.cancel(); timer = null;
                System.out.println("ACTION_UP");

                if (isLongClickModule && !readOnly) {

                    int start = this.getSelectionStart();
                    int end = this.getSelectionEnd();
                    boolean intersection = false;
                    if (oldAttArray.size() > 0) {
                        for (int i = 0; i < oldAttArray.size(); i++) {
                            PointEntity pointentity = (PointEntity) oldAttArray.get(i);
                            int star = pointentity.commentRange.location;
                            int en = pointentity.commentRange.location
                                    + pointentity.commentRange.length;
                            if (start <= en && start >= star) {
                                intersection = true;
                            }
                            if (end <= en && end >= star) {
                                intersection = true;
                            }
                            if (start <= star && end >= en) {
                                intersection = true;
                            }
                        }
                    }
                    if (!intersection && attArray.size() > 0) {
                        for (int i = 0; i < attArray.size(); i++) {
                            PointEntity pointentity = (PointEntity) attArray.get(i);
                            int star = pointentity.commentRange.location;
                            int en = pointentity.commentRange.location
                                    + pointentity.commentRange.length;
                            if (start <= en && start >= star) {
                                intersection = true;
                            }
                            if (end <= en && end >= star) {
                                intersection = true;
                            }
                            if (start <= star && end >= en) {
                                intersection = true;
                            }
                        }
                    }

                    if (intersection) {
                        isLongClickModule = false;
                        this.setSelection(0);
                        break;
                    } else {

                        if (off != curOff1) {
                            Point point3 = new Point();
                            point3.x = (int) ev.getX();
                            point3.y = (int) ev.getY();
                            // 如果已存在注解  
                            this.showEditPopupWindow(this, start, end, point3);
                        }
                        isLongClickModule = false;
                    }
                } else {
                    if (off == curOff1 && off >= 1) {
                        this.setSelection(0);
                        Point point = new Point();
                        point.x = (int) ev.getX();
                        point.y = (int) ev.getY();
                        // 如果没有编辑  新建注解
                        this.showPopupWindow(this, off, curOff1, point);
                    }
                }

                isLongClickModule = false;
                break;
        }
        return true;
    }

    @Override
    public void onSelectionChanged(int start, int end) {
        super.onSelectionChanged(start, end);


        if (selectionListener != null) {
            ArrayList<Effect<?>> effects = new ArrayList<Effect<?>>();

            for (Effect<?> effect : EFFECTS) {
                if (effect.existsInSelection(this)) {
                    effects.add(effect);
                }
            }

            isSelectionChanging = true;
            selectionListener.onSelectionChanged(start, end, effects);
            isSelectionChanging = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (forceActionMode && mainMode != null && start != end) {
                postDelayed(new Runnable() {
                    public void run() {
                        if (!actionModeIsShowing) {
                            startActionMode(mainMode);
                        }
                    }
                }, 500);
            }
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyboardShortcuts
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.e("onKeyUp", "keyboardShortcuts");
            if (event.isCtrlPressed()) {
                if (keyCode == KeyEvent.KEYCODE_B) {

                    return (true);
                } else if (keyCode == KeyEvent.KEYCODE_I) {

                    return (true);
                } else if (keyCode == KeyEvent.KEYCODE_U) {
                    toggleEffect(RichEditText.UNDERLINE);

                    return (true);
                }
            }
        }

        return (super.onKeyUp(keyCode, event));
    }

    public void setOnSelectionChangedListener(
            OnSelectionChangedListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void setKeyboardShortcutsEnabled(boolean keyboardShortcuts) {
        this.keyboardShortcuts = keyboardShortcuts;
    }

    public <T> void applyEffect(Effect<T> effect, T value) {
        if (!isSelectionChanging) {
            effect.applyToSelection(this, value);
        }
    }

    public boolean hasEffect(Effect<?> effect) {
        return (effect.existsInSelection(this));
    }

    public <T> T getEffectValue(Effect<T> effect) {
        return (effect.valueInSelection(this));
    }

    public void toggleEffect(Effect<Boolean> effect) {
        if (!isSelectionChanging) {
            effect.applyToSelection(this, !effect.valueInSelection(this));
        }
    }

    @Override
    public boolean doAction(int itemId) {
        return (false);
    }

    @Override
    public void setIsShowing(boolean isShowing) {
        actionModeIsShowing = isShowing;
    }

    @SuppressLint("NewApi")
    public void enableActionModes(boolean forceActionMode) {
        this.forceActionMode = forceActionMode;

        EditorActionModeCallback.Native effectsMode = new EditorActionModeCallback.Native(
                (Activity) getContext(), R.menu.cwac_richedittext_effects,
                this, this);

        EditorActionModeCallback.Native fontsMode = new EditorActionModeCallback.Native(
                (Activity) getContext(), R.menu.cwac_richedittext_fonts, this,
                this);

        mainMode = new EditorActionModeCallback.Native((Activity) getContext(),
                R.menu.cwac_richedittext_main, this, this);

        mainMode.addChain(R.id.cwac_richedittext_effects, effectsMode);
        mainMode.addChain(R.id.cwac_richedittext_fonts, fontsMode);

        EditorActionModeCallback.Native entryMode = new EditorActionModeCallback.Native(
                (Activity) getContext(), R.menu.cwac_richedittext_entry, this,
                this);

        entryMode.addChain(R.id.cwac_richedittext_format, mainMode);

        setCustomSelectionActionModeCallback(entryMode);
    }

    public void disableActionModes() {

        setCustomSelectionActionModeCallback(null);
        mainMode = null;
    }
 
    public void deleteByRange(Range range) {
        int location = range.location;
        Spannable str = this.getText();
        for (int i = 0; i < attArray.size(); i++) {
            PointEntity pointentity = (PointEntity) attArray.get(i);
            int start = pointentity.commentRange.location;
            int end = pointentity.commentRange.location
                    + pointentity.commentRange.length;
            if (location >= start && location <= end) {
                MyURLSpan[] spans = str.getSpans(range.location, range.location
                        + range.length, MyURLSpan.class);
                for (MyURLSpan span : spans) {
                    str.removeSpan(span);
                }
                attArray.remove(pointentity);
            }
        }
    }

    public void changeContentByRange(Range range, String content) {
        int location = range.location;
        for (int i = 0; i < attArray.size(); i++) {
            PointEntity pointentity = (PointEntity) attArray.get(i);
            int start = pointentity.commentRange.location;
            int end = pointentity.commentRange.location
                    + pointentity.commentRange.length;
            if (location >= start && location <= end) {
            	pointentity.textEditContent = pointentity.textContent;
                pointentity.textContent = "";
                pointentity.textContent = content;
                break;
            }
        }
    }

    public void showPopupWindow(View view, int start, int end, Point point) {

        View contentView = LayoutInflater.from(this.context).inflate(
                R.layout.pop_window, null);
        EditText textview = (EditText) contentView.findViewById(R.id.textview);
        Button deleteBtn = (Button) contentView.findViewById(R.id.deleteBtn);

        Button submitBtn = (Button) contentView.findViewById(R.id.submitBtn);
        Button playBtn = (Button) contentView.findViewById(R.id.playBtn);
        PointEntity pointEntity = new PointEntity();
        boolean isOld = false;
        if (oldAttArray.size() > 0) {
            for (int i = 0; i < oldAttArray.size(); i++) {
                PointEntity pointentity = (PointEntity) oldAttArray.get(i);
                int star = pointentity.commentRange.location;
                int en = pointentity.commentRange.location
                        + pointentity.commentRange.length;
                if (start >= star && end <= en) {
                    pointEntity.pointID = pointentity.pointID;
                    pointEntity.textContent = pointentity.textContent;
                    pointEntity.commentRange = pointentity.commentRange;
                    pointEntity.mp3Name = pointentity.mp3Name;
                    isOld = true;
                }
            }
        }
        if (pointEntity.commentRange == null && attArray.size() > 0) {
            for (int i = 0; i < attArray.size(); i++) {
                PointEntity pointentity = (PointEntity) attArray.get(i);
                int star = pointentity.commentRange.location;
                int en = pointentity.commentRange.location
                        + pointentity.commentRange.length;
                if (start >= star && end <= en) {
                    pointEntity.pointID = pointentity.pointID;
                    pointEntity.textContent = pointentity.textContent;
                    pointEntity.commentRange = pointentity.commentRange;
                    pointEntity.mp3Name = pointentity.mp3Name;
                    isOld = false;
                }
            }
        }

        if (pointEntity.commentRange != null) {

        	int width = MainActivity.isHD ?400*2:400;
        	int height = MainActivity.isHD ?350*2:350;
            final PopupWindow popupWindow = new PopupWindow(contentView, width,
            		height, true);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, point.x + 50,
                    point.y + 130);// AsDropDown(view);
            if (pointEntity.textContent != null && pointEntity.textContent.length() > 0) {
                textview.setText(pointEntity.textContent);
                textview.setSelection(pointEntity.textContent.length());
                if (readOnly || isOld) {
                    deleteBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                    textview.setEnabled(false);
                } else {
                    deleteBtn.setOnClickListener(new commentDelete_listener(
                            pointEntity.commentRange, popupWindow));
                    submitBtn.setOnClickListener(new commentChange_listener(
                            pointEntity.commentRange, popupWindow, textview));

                }
            } else if (pointEntity.mp3Name != null && pointEntity.mp3Name.length() > 0) {
                textview.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);

                playBtn.setVisibility(View.VISIBLE);
                playBtn.setOnClickListener(new commentRecordPlay_listener(pointEntity.mp3Name));
                if (readOnly || isOld) {
                    deleteBtn.setVisibility(View.GONE);
                } else {
                    deleteBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setOnClickListener(new commentDelete_listener(
                            pointEntity.commentRange, popupWindow));
                }

            }

        }

    }

    public void showEditPopupWindow(View view, int start, int end, Point point) {

        View contentView = LayoutInflater.from(this.context).inflate(
                R.layout.edit_pop_window, null);
        int width = MainActivity.isHD ?250*2:300;
    	int height = MainActivity.isHD ?200*2:200;
        final PopupWindow popupWindow = new PopupWindow(contentView, width, height,
                true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final LinearLayout selectLyout = (LinearLayout) contentView
                .findViewById(R.id.selectView);
        selectLyout.setBackground(getResources().getDrawable(R.drawable.pop));
        final Button textBtn = (Button) contentView.findViewById(R.id.textBtn);
        final Button recordBtn = (Button) contentView
                .findViewById(R.id.recordBtn);
        final LinearLayout editLayout = (LinearLayout) contentView
                .findViewById(R.id.editLayout);
        final EditText textEditView = (EditText) contentView
                .findViewById(R.id.textEditView);
        final Button saveTextBtn = (Button) contentView
                .findViewById(R.id.saveTextBtn);
        final Button micBtn = (Button) contentView.findViewById(R.id.micBtn);
        micBtn.setOnClickListener(new commentMIC_listener(textEditView));
        textBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectLyout.setVisibility(View.INVISIBLE);
                editLayout.setVisibility(View.VISIBLE);
                popupWindow.update(600, 400);
            }
        });
        saveTextBtn.setOnClickListener(new saveTextBtn_listener(start, end,
                textEditView, popupWindow));

        recordBtn.setOnClickListener(new saveRecordBtn_listener(start, end, selectLyout));


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, point.x + 50,
                point.y + 130);
    }

    class saveTextBtn_listener implements Button.OnClickListener {
        int start;
        int end;
        EditText textEditView;
        PopupWindow popupWindow;

        saveTextBtn_listener(int start, int end, EditText textEditView,
                PopupWindow popupWindow) {
            this.start = start;
            this.end = end;
            this.textEditView = textEditView;
            this.popupWindow = popupWindow;
        }

        public void onClick(View v) {
            if (textEditView.getText().toString().length() > 0) {
                String stringid = StaticTools.getTimeformat("ddHHmmss");
                int pointID = Integer.parseInt(stringid);
                PointEntity pointEntity = new PointEntity();
                pointEntity.pointID = pointID;
                Range range = new Range();
                range.location = start;
                range.length = end - start + 1;
                pointEntity.commentRange = range;
                pointEntity.textContent = textEditView.getText().toString();
                pointEntity.textEditContent = textEditView.getText().toString();
                attArray.add(pointEntity);
                Effect<Boolean> BOLD = new StyleEffect(Typeface.BOLD, pointID);
                BOLD.applyToSelection(RichEditText.this, true);
                RichEditText.this.setSelection(0);

            }

            popupWindow.dismiss();
        }

    }

    class saveRecordBtn_listener implements Button.OnClickListener {
        int start;
        int end;
        LinearLayout selectLyout;

        saveRecordBtn_listener(int start, int end, LinearLayout selectLyout) {
            this.start = start;
            this.end = end;
            this.selectLyout = selectLyout;
        }

        public void onClick(View v) {

            selectLyout.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(MainActivity.instance, MyMP3Dialog.class);
            intent.putExtra("start", start);
            intent.putExtra("end", end);
            MainActivity.instance.startActivityForResult(intent, 2000);
        }
    }

    public String getRichtextString(){
    	HTMLDecoder decoder = new HTMLDecoder();
        String res = decoder.decode(Html.toHtml(this.getText()));

        String ios = StaticTools.andTOios(res);
    	return ios;
    }
    public void saveRecord(int start, int end, String path) {
        if (path.length() > 0) {
            String stringid = StaticTools.getTimeformat("ddHHmmss");
            int pointID = Integer.parseInt(stringid);
            PointEntity pointEntity = new PointEntity();
            pointEntity.pointID = pointID;
            Range range = new Range();
            range.location = start;
            range.length = end - start + 1;
            pointEntity.commentRange = range;
            pointEntity.mp3Name = path;
            pointEntity.mp3EditName = path;
            attArray.add(pointEntity);
            Effect<Boolean> BOLD = new StyleEffect(Typeface.BOLD, pointID);
            BOLD.applyToSelection(RichEditText.this, true);
            RichEditText.this.setSelection(0);
        }

    }

    class commentRecordPlay_listener implements Button.OnClickListener {
        String path;

        commentRecordPlay_listener(String path) {
            this.path = path;
        }

        @Override
        public void onClick(View v) {
        	Thread thread=new Thread(new Runnable()  
            {  
                @Override  
                public void run()  
                {  
                	Uri u = Uri.parse(path);
                    final MediaPlayer player = MediaPlayer.create(context, u);

                    PlayerAmrUtil.getInster(context).play(path, !path.contains("http://"));
                    
                    if (player!=null&&!player.isPlaying()) {
                        player.start();
                        
                        player.setOnErrorListener(new OnErrorListener() {

                            @Override
                            public boolean onError(MediaPlayer mp, int what,
                                    int extra) {
                                return false;
                            }
                        });

                        player.setOnCompletionListener(new OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                player.reset();
                                player.release();
                            }
                        });
                    }
                }  
            });  
            thread.start();
            

        }
    }

    class commentDelete_listener implements Button.OnClickListener {
        private Range range;
        private PopupWindow popupWindow;

        commentDelete_listener(Range range, PopupWindow popupWindow) {
            this.range = range;
            this.popupWindow = popupWindow;
        }

        public void onClick(View v) {
            if (range != null) {
                RichEditText.this.deleteByRange(range);
            }
            popupWindow.dismiss();
        }

    }

    class commentChange_listener implements Button.OnClickListener {
        private Range range;
        private PopupWindow popupWindow;
        private EditText textview;

        commentChange_listener(Range range, PopupWindow popupWindow,
                EditText textview) {
            this.range = range;
            this.popupWindow = popupWindow;
            this.textview = textview;
        }

        public void onClick(View v) {
            if (range != null && textview.getText().toString().length() > 0) {
                textview.refreshDrawableState();
                RichEditText.this.changeContentByRange(range, textview
                        .getText().toString());
            }
            popupWindow.dismiss();
        }

    }

    class commentMIC_listener  implements Button.OnClickListener {
        private EditText textview;
        commentMIC_listener(EditText textview){
            this.textview = textview;
        }

        public void onClick(View v) {


            Bundle params = new Bundle();
            params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, Constants.API_KEY);
            params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, Constants.SECRET_KEY);
            params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, Config.DIALOG_THEME);
            mDialog = new BaiduASRDigitalDialog(context, params);
            mDialog.setDialogRecognitionListener(new DialogRecognitionListener() {

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> rs = results != null ? results
                            .getStringArrayList(RESULTS_RECOGNITION) : null;
                    if (rs != null && rs.size() > 0) {
                        textview.setText(rs.get(0));
                    }

                }
            });

        mDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP, Config.CURRENT_PROP);
        mDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE,
                Config.getCurrentLanguage());
        Log.e("DEBUG", "Config.PLAY_START_SOUND = "+Config.PLAY_START_SOUND);
        mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE, Config.PLAY_START_SOUND);
        mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE, Config.PLAY_END_SOUND);
        mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE, Config.DIALOG_TIPS_SOUND);
        mDialog.show();
        }
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int start, int end, List<Effect<?>> effects);
    }

    private static class UnderlineEffect extends
            SimpleBooleanEffect<UnderlineSpan> {
        UnderlineEffect() {
            super(UnderlineSpan.class);
        }
    }

    private static class StrikethroughEffect extends
            SimpleBooleanEffect<StrikethroughSpan> {
        StrikethroughEffect() {
            super(StrikethroughSpan.class);
        }
    }

    private static class SuperscriptEffect extends
            SimpleBooleanEffect<SuperscriptSpan> {
        SuperscriptEffect() {
            super(SuperscriptSpan.class);
        }
    }

    private static class SubscriptEffect extends
            SimpleBooleanEffect<SubscriptSpan> {
        SubscriptEffect() {
            super(SubscriptSpan.class);
        }
    }

    public ArrayList<Object> getoldAttArray(){
    	return this.oldAttArray;
    }
    public void setoldAttArray(ArrayList<Object> oldArray){
    	
    	this.oldAttArray  = null;
    	this.oldAttArray = oldArray;
    }
}
