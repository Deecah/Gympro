package job;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class StartupScheduler implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            JobDetail job = JobBuilder.newJob(ContractStatusUpdaterJob.class)
                .withIdentity("contractUpdater", "daily")
                .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "daily")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(1, 0))
                .build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(job, trigger); // nên trước
            scheduler.start();

            System.out.println("[Scheduler] Contract auto-updater started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Dừng nếu cần
    }
}
