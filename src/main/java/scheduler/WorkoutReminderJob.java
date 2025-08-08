package scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WorkoutReminderJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int scheduleId = (int) context.getJobDetail().getJobDataMap().get("scheduleId");
        // Logic gửi nhắc nhở (email, thông báo, v.v.)
        System.out.println("Sending reminder for workout schedule ID: " + scheduleId);
    }
}