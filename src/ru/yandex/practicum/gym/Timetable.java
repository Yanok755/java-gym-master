import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        // Инициализируем TreeMap для каждого дня недели с компаратором для TimeOfDay
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>((time1, time2) -> {
                if (time1.getHours() != time2.getHours()) {
                    return Integer.compare(time1.getHours(), time2.getHours());
                }
                return Integer.compare(time1.getMinutes(), time2.getMinutes());
            }));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        // Если для данного времени еще нет списка тренировок, создаем его
        daySchedule.computeIfAbsent(time, k -> new ArrayList<>()).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();

        // Проходим по всем временам в отсортированном порядке
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();

        // Считаем тренировки для каждого тренера
        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCountMap.put(coach, coachCountMap.getOrDefault(coach, 0) + 1);
                }
            }
        }

        // Создаем список объектов для сортировки
        List<CoachTrainingCount> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }

        // Сортируем по убыванию количества тренировок
        result.sort((c1, c2) -> Integer.compare(c2.getTrainingCount(), c1.getTrainingCount()));

        return result;
    }
}
