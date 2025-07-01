package model;

public class ProgramWeek {
    private int weekId;
    private int programId;
    private int weekNumber;

    public ProgramWeek() {
    }

    public ProgramWeek(int weekId, int programId, int weekNumber) {
        this.weekId = weekId;
        this.programId = programId;
        this.weekNumber = weekNumber;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}