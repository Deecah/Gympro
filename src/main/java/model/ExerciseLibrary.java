package model;

public class ExerciseLibrary {
    private int exerciseID;
    private String name;
    private String description;
    private String videoURL;
    private String muscleGroup;
    private String equipment;
    private int sets;
    private int reps;
    private int restTimeSeconds;
    private int trainerID;

    public int getExerciseID() { return exerciseID; }
    public void setExerciseID(int exerciseID) { this.exerciseID = exerciseID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVideoURL() { return videoURL; }
    public void setVideoURL(String videoURL) { this.videoURL = videoURL; }
    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    public int getRestTimeSeconds() { return restTimeSeconds; }
    public void setRestTimeSeconds(int restTimeSeconds) { this.restTimeSeconds = restTimeSeconds; }
    public int getTrainerID() { return trainerID; }
    public void setTrainerID(int trainerID) { this.trainerID = trainerID;}
}