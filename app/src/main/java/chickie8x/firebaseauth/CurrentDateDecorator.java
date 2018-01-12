package chickie8x.firebaseauth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * Created by Admin on 1/7/2018.
 */

public class CurrentDateDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CurrentDateDecorator(Context context) {
        this.context = context;
        highlightDrawable = this.context.getDrawable(R.drawable.circlebackground);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.setBackgroundDrawable(highlightDrawable);
        view.addSpan(new ForegroundColorSpan(Color.RED));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.5f));

    }

}