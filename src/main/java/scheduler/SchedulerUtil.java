package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SchedulerUtil {
    public static void scheduleReminder(int scheduleId, LocalDateTime reminderTime) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDetail job = JobBuilder.newJob(WorkoutReminderJob.class)
                    .withIdentity("reminderJob" + scheduleId, "reminderGroup")
                    .usingJobData("scheduleId", scheduleId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("reminderTrigger" + scheduleId, "reminderGroup")
                    .startAt(Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}