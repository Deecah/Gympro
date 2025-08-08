package scheduler;

import model.Workout;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;

public class ScheduleWorkoutJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Workout workout = (Workout) dataMap.get("workout");
    }

    public static void schedule(Workout workout, String dateStr, String startTimeStr) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        if (!scheduler.isStarted()) {
            scheduler.start();
        }

        LocalDateTime scheduledTime = LocalDateTime.parse(dateStr + "T" + startTimeStr);

        JobDataMap dataMap = new JobDataMap();
        dataMap.put("workout", workout);

        JobDetail job = JobBuilder.newJob(ScheduleWorkoutJob.class)
                .withIdentity("workoutJob" + workout.getWorkoutId(), "workoutGroup")
                .usingJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger" + workout.getWorkoutId(), "workoutGroup")
                .startAt(java.sql.Timestamp.valueOf(scheduledTime))
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}