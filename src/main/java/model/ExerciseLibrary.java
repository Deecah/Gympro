package model;

public class ExerciseLibrary {
    private int exerciseID;
    private String name;
    private String description;
    private String videoURL;
    private String muscleGroup;
    private String equipment;

    public ExerciseLibrary() {
    }

    public ExerciseLibrary(int exerciseID, String name, String description, String videoURL, String muscleGroup, String equipment) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.description = description;
        this.videoURL = videoURL;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
