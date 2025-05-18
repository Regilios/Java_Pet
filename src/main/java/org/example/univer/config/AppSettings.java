package org.example.univer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppSettings {
    private String jndiUrl;
    private int maxLengthNameCathedra;
    private String startSymbolNameCathedra;
    private int minLengthNameAudience;
    private int minLengthNameGroup;
    private int maxDayHoliday;
    private String startDayHoliday;
    private int minimumLectureTimeMinutes;
    private String startLectionDay;
    private String endLectionDay;
    private int maxGroupSize;
    private int minSizeDescription;
    private String genderTeacher;
    private int minVacationDay;
    private int maxVacationDay;
    private int defaultPageSize;
    @NestedConfigurationProperty
    private RoomSettings roomSettings = new RoomSettings();

    public static class RoomSettings {
        private int sizeMax;
        private int sizeMin;

        public int getSizeMax() { return sizeMax; }
        public void setSizeMax(int sizeMax) { this.sizeMax = sizeMax; }

        public int getSizeMin() { return sizeMin; }
        public void setSizeMin(int sizeMin) { this.sizeMin = sizeMin; }
    }

    public String getJndiUrl() { return jndiUrl; }
    public void setJndiUrl(String jndiUrl) { this.jndiUrl = jndiUrl; }

    public int getMaxLengthNameCathedra() { return maxLengthNameCathedra; }
    public void setMaxLengthNameCathedra(int maxLengthNameCathedra) { this.maxLengthNameCathedra = maxLengthNameCathedra; }

    public String getStartSymbolNameCathedra() { return startSymbolNameCathedra; }
    public void setStartSymbolNameCathedra(String startSymbolNameCathedra) { this.startSymbolNameCathedra = startSymbolNameCathedra; }

    public int getMinLengthNameAudience() { return minLengthNameAudience; }
    public void setMinLengthNameAudience(int minLengthNameAudience) { this.minLengthNameAudience = minLengthNameAudience; }

    public int getMinLengthNameGroup() { return minLengthNameGroup; }
    public void setMinLengthNameGroup(int minLengthNameGroup) { this.minLengthNameGroup = minLengthNameGroup; }

    public int getMaxDayHoliday() { return maxDayHoliday; }
    public void setMaxDayHoliday(int maxDayHoliday) { this.maxDayHoliday = maxDayHoliday; }

    public String getStartDayHoliday() { return startDayHoliday; }
    public void setStartDayHoliday(String startDayHoliday) { this.startDayHoliday = startDayHoliday; }

    public int getMinimumLectureTimeMinutes() { return minimumLectureTimeMinutes; }
    public void setMinimumLectureTimeMinutes(int minimumLectureTimeMinutes) { this.minimumLectureTimeMinutes = minimumLectureTimeMinutes; }

    public String getStartLectionDay() { return startLectionDay; }
    public void setStartLectionDay(String startLectionDay) { this.startLectionDay = startLectionDay; }

    public String getEndLectionDay() { return endLectionDay; }
    public void setEndLectionDay(String endLectionDay) { this.endLectionDay = endLectionDay; }

    public int getMaxGroupSize() { return maxGroupSize; }
    public void setMaxGroupSize(int maxGroupSize) { this.maxGroupSize = maxGroupSize; }

    public int getMinSizeDescription() { return minSizeDescription; }
    public void setMinSizeDescription(int minSizeDescription) { this.minSizeDescription = minSizeDescription; }

    public String getGenderTeacher() { return genderTeacher; }
    public void setGenderTeacher(String genderTeacher) { this.genderTeacher = genderTeacher; }

    public int getMinVacationDay() { return minVacationDay; }
    public void setMinVacationDay(int minVacationDay) { this.minVacationDay = minVacationDay; }

    public int getMaxVacationDay() { return maxVacationDay; }
    public void setMaxVacationDay(int maxVacationDay) { this.maxVacationDay = maxVacationDay; }

    public int getDefaultPageSize() { return defaultPageSize; }
    public void setDefaultPageSize(int defaultPageSize) { this.defaultPageSize = defaultPageSize; }

    public RoomSettings getRoomSettings() { return roomSettings; }
    public void setRoomSettings(RoomSettings roomSettings) { this.roomSettings = roomSettings; }
}
