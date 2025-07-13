package model;

public class ProgramDay {
    private int dayId;
    private int weekId;
    private int dayNumber;

    public ProgramDay() {
    }

    public ProgramDay(int dayId, int weekId, int dayNumber) {
        this.dayId = dayId;
        this.weekId = weekId;
        this.dayNumber = dayNumber;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
