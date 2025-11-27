package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new EnumMap<>(DayOfWeek.class);
        // Инициализируем все дни недели
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>()); // TreeMap для сортировки по времени
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        
        // Получаем Map для данного дня и добавляем тренировку в список для данного времени
        timetable.get(day).computeIfAbsent(time, k -> new ArrayList<>()).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        // Получаем все тренировки за день (все временные слоты)
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }
        // Сортируем по времени начала
        result.sort((ts1, ts2) -> ts1.getTimeOfDay().compareTo(ts2.getTimeOfDay()));
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        // Прямой доступ к тренировкам по дню и времени - O(1)
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    // Дополнительные полезные методы:

    public List<TrainingSession> getTrainingSessionsForCoach(Coach coach) {
        List<TrainingSession> result = new ArrayList<>();
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    if (session.getCoach().equals(coach)) {
                        result.add(session);
                    }
                }
            }
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForGroup(Group group) {
        List<TrainingSession> result = new ArrayList<>();
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    if (session.getGroup().equals(group)) {
                        result.add(session);
                    }
                }
            }
        }
        return result;
    }

    public boolean removeTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule != null) {
            List<TrainingSession> sessions = daySchedule.get(time);
            if (sessions != null) {
                boolean removed = sessions.remove(trainingSession);
                if (sessions.isEmpty()) {
                    daySchedule.remove(time);
                }
                return removed;
            }
        }
        return false;
    }

    public boolean isTimeSlotAvailable(DayOfWeek day, TimeOfDay time, int duration) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule == null) return true;
        
        // Проверяем, нет ли пересечений по времени
        for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySchedule.entrySet()) {
            TimeOfDay existingTime = entry.getKey();
            // Простая проверка - если время начала совпадает, слот занят
            if (existingTime.equals(time)) {
                return false;
            }
            // Можно добавить более сложную логику проверки пересечений по времени
        }
        return true;
    }
}
