package model;

public class ExerciseProgram {
    private int exerciseProgramID;
    private int exerciseLibraryID;
    private int trainerID;
    private int programID;
    private String exerciseName;
    private int sets;
    private int reps;
    private int restTimeSeconds;
    private String videoUrl;
    private String description;
    private String muscleGroup;
    private String equipment;

    public int getExerciseProgramID() { return exerciseProgramID; }
    public void setExerciseProgramID(int exerciseProgramID) { this.exerciseProgramID = exerciseProgramID; }
    public int getExerciseLibraryID() { return exerciseLibraryID; }
    public void setExerciseLibraryID(int exerciseLibraryID) { this.exerciseLibraryID = exerciseLibraryID; }
    public int getTrainerID() { return trainerID; }
    public void setTrainerID(int trainerID) { this.trainerID = trainerID; }
    public int getProgramID() { return programID; }
    public void setProgramID(int programID) { this.programID = programID; }
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    public int getRestTimeSeconds() { return restTimeSeconds; }
    public void setRestTimeSeconds(int restTimeSeconds) { this.restTimeSeconds = restTimeSeconds; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
}