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

    // Добавляем недостающий метод для тестов
    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();

        // Собираем статистику по всем тренерам
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCountMap.put(coach, coachCountMap.getOrDefault(coach, 0) + 1);
                }
            }
        }

        // Преобразуем в список и сортируем по убыванию количества тренировок
        List<CoachTrainingCount> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }

        result.sort((c1, c2) -> c2.getTrainingCount() - c1.getTrainingCount());
        return result;
    }
}
