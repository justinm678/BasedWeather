package com.example.basedweather;


import com.github.mikephil.charting.formatter.ValueFormatter;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.List;

public class TimeAxisValueFormatter extends ValueFormatter {

    private List<LocalDateTime> times;
    private DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dayHourFormatter = DateTimeFormatter.ofPattern("E HH:mm");

    public TimeAxisValueFormatter(List<LocalDateTime> times) {
        this.times = times;
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value;
        if (index < 0 || index >= times.size()) {
            return "";
        }

        LocalDateTime current = times.get(index);

        if (index >= 23) {
            return current.format(dayHourFormatter); // "Mon 00:00"
        } else {
            return current.format(hourFormatter);    // "01:00", "02:00", etc.
        }
    }
}