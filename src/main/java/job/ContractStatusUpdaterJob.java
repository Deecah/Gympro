package job;

import connectDB.ConnectDatabase;
import org.quartz.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ContractStatusUpdaterJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        String sql = "UPDATE Contracts SET Status = 'expired' " +
                     "WHERE EndDate < ? AND Status = 'active'";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            int rowsUpdated = ps.executeUpdate();
            System.out.println("[Scheduler] Updated " + rowsUpdated + " expired contracts.");
        } catch (Exception e) {
            System.err.println("[Scheduler] Failed to update contracts.");
            e.printStackTrace();
        }
    }
}
