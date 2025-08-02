package dao;

import connectDB.ConnectDatabase;
import model.Workout;
import model.ExerciseLibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutDAO {
    public Map<Integer, List<Workout>> getWorkoutsByProgram(int programId) {
        Map<Integer, List<Workout>> workoutsMap = new HashMap<>();
        String sql = "SELECT w.WorkoutID, w.DayID, w.StartTime, w.EndTime, w.Title, w.Notes, w.CreatedAt, w.TrainerID " +
                "FROM Workout w " +
                "JOIN ProgramDay pd ON w.DayID = pd.DayID " +
                "JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                "WHERE pw.ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int dayId = rs.getInt("DayID");
                Workout workout = new Workout();
                workout.setWorkoutId(rs.getInt("WorkoutID"));
                workout.setDayId(dayId);
                workout.setStartTime(rs.getTime("StartTime"));
                workout.setEndTime(rs.getTime("EndTime"));
                workout.setTitle(rs.getString("Title"));
                workout.setNotes(rs.getString("Notes"));
                workout.setCreatedAt(rs.getTimestamp("CreatedAt"));
                workout.setTrainerId(rs.getInt("TrainerID"));
                workoutsMap.computeIfAbsent(dayId, k -> new ArrayList<>()).add(workout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workoutsMap;
    }

    public Map<Integer, List<ExerciseLibrary>> getExercisesByWorkouts(Map<Integer, List<Workout>> dayWorkouts) {
        Map<Integer, List<ExerciseLibrary>> exercisesMap = new HashMap<>();
        String sql = "SELECT e.WorkoutID, el.ExerciseLibraryID, el.ExerciseName, el.Sets, el.Reps, " +
                "el.RestTimeSeconds, el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment, el.TrainerID " +
                "FROM Exercise e " +
                "JOIN ExerciseLibrary el ON e.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE e.WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (List<Workout> workouts : dayWorkouts.values()) {
                for (Workout workout : workouts) {
                    int workoutId = workout.getWorkoutId();
                    ps.setInt(1, workoutId);
                    ResultSet rs = ps.executeQuery();
                    List<ExerciseLibrary> exercises = new ArrayList<>();
                    while (rs.next()) {
                        ExerciseLibrary exercise = new ExerciseLibrary();
                        exercise.setExerciseID(rs.getInt("ExerciseLibraryID"));
                        exercise.setName(rs.getString("ExerciseName"));
                        exercise.setSets(rs.getInt("Sets"));
                        exercise.setReps(rs.getInt("Reps"));
                        exercise.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                        exercise.setVideoURL(rs.getString("VideoUrl"));
                        exercise.setDescription(rs.getString("Description"));
                        exercise.setMuscleGroup(rs.getString("MuscleGroup"));
                        exercise.setEquipment(rs.getString("Equipment"));
                        exercise.setTrainerID(rs.getInt("TrainerID"));
                        exercises.add(exercise);
                    }
                    exercisesMap.put(workoutId, exercises);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exercisesMap;
    }

    public int addWorkout(Workout workout) {
        String sql = "INSERT INTO Workout (DayID, StartTime, EndTime, Title, Notes, TrainerID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, workout.getDayId());
            ps.setTime(2, workout.getStartTime());
            ps.setTime(3, workout.getEndTime());
            ps.setString(4, workout.getTitle());
            ps.setString(5, workout.getNotes());
            ps.setInt(6, workout.getTrainerId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated WorkoutID
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 on failure
    }

    public boolean deleteWorkout(int workoutId) {
        String sql = "DELETE FROM Workout WHERE WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}