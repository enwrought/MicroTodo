import android.test.suitebuilder.annotation.MediumTest;

import com.bryant.microtodolist.TimePreference;


import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by bryant on 7/12/16.
 */

@MediumTest
public class TimePreferenceTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    /*
    protected void setUp() {
        String midnight1 = "0:00";
        String midnight2 = "00:00";

        String invalid_time1 = "29:01";
        String invalid_time2 = "12:73";
        String invalid_time3 = "1:60";

        String morning = "08:00";
        String morning2 = "8:36";

        String noon = "12:00";
        String afternoon = "13:15";

        String evening = "19:23";

        String late_night = "00:58";
        String late_night2 = "4:00";
    }
    */
    final String midnight1 = "0:00";
    final String midnight2 = "00:00";

    final String invalid_time1 = "29:01";
    final String invalid_time2 = "12:73";
    final String invalid_time3 = "1:60";

    String morning = "08:00";
    String morning2 = "9:36";

    String noon = "12:00";
    String afternoon = "13:15";

    String evening = "19:23";

    String late_night = "00:58";
    String late_night2 = "4:09";

    @Test
    public void testGetHour() throws Exception {
        assertEquals("Hour of 00:00 should be 0", TimePreference.getHour(midnight1), 0);
        assertEquals("Hour of 0:00 should be 0", TimePreference.getHour(midnight2), 0);

        assertEquals("Hour of 08:00 should be 8", TimePreference.getHour(morning), 8);
        assertEquals("Hour of 9:36 should be 0", TimePreference.getHour(morning2), 9);

        assertEquals("Hour of 13:15 should be 13", TimePreference.getHour(afternoon), 13);

        assertEquals("Hour of 00:58 should be 0", TimePreference.getHour(late_night), 0);
    }

    @Test
    public void testGetMinute() throws Exception {
        assertEquals("Minute of 00:00 should be 0", TimePreference.getMinute(midnight1), 0);
        assertEquals("Minute of 0:00 should be 0", TimePreference.getMinute(midnight2), 0);

        assertEquals("Minute of 08:00 should be 0", TimePreference.getMinute(morning), 0);
        assertEquals("Minute of 9:36 should be 36", TimePreference.getMinute(morning2), 36);

        assertEquals("Minute of 13:15 should be 15", TimePreference.getMinute(afternoon), 15);

        assertEquals("Minute of 00:58 should be 58", TimePreference.getMinute(late_night), 58);
        assertEquals("Minute of 4:09 should be 9", TimePreference.getMinute(late_night2), 9);
    }

    @Test
    public void testGetTimeValue() throws Exception {
        assertEquals("Value of 00:00 should be 0", TimePreference.getTimeValue(midnight1), 0);
        assertEquals("Value of 0:00 should be 0", TimePreference.getTimeValue(midnight2), 0);
        assertEquals("Value of 08:00 should be 480", TimePreference.getTimeValue(morning), 480);
        assertEquals("Value of 9:36 should be 576", TimePreference.getTimeValue(morning2), 576);
        assertEquals("Value of 13:15 should be 795", TimePreference.getTimeValue(afternoon), 795);
    }

    public void testIsBetweenTimes() throws Exception {
        assertTrue("Nothing between same times exclusive 1",
                TimePreference.isBetweenTimes(morning, midnight2, midnight1, false));
        assertTrue("Nothing between same times exclusive 2",
                TimePreference.isBetweenTimes(midnight1, midnight2, midnight1, false));

        assertTrue("Same times inclusive 1",
                TimePreference.isBetweenTimes(midnight1, midnight2, midnight1, true));
        assertTrue("Same times inclusive 2",
                TimePreference.isBetweenTimes(midnight1, midnight2, midnight1, false));


        // TODO: FINISH
    }

    @Test
    public void testGetSummary() throws Exception {
        // TODO: finish
    }

    @Test
    public void testValidInput() {
        // exception.expect(IndexOutOfBoundsException.class);

        // TODO: finish
    }
}