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
    public class Main {
    public static void main(String[] args) {
        // Создаем расписание
        Timetable timetable = new Timetable();

        // Создаем тренеров
        Coach coach1 = new Coach("Иванов", "Петр", "Сергеевич");
        Coach coach2 = new Coach("Петрова", "Мария", "Александровна");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Викторович");

        // Создаем группы
        Group childGroup1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group childGroup2 = new Group("Гимнастика для детей", Age.CHILD, 45);
        Group adultGroup1 = new Group("Йога для взрослых", Age.ADULT, 90);
        Group adultGroup2 = new Group("Фитнес для взрослых", Age.ADULT, 60);

        // Добавляем тренировки в расписание
        timetable.addNewTrainingSession(new TrainingSession(childGroup1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup1, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup1, coach1, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(childGroup2, coach2, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup2, coach2, DayOfWeek.THURSDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession(adultGroup1, coach3, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adultGroup1, coach3, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adultGroup1, coach3, DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        timetable.addNewTrainingSession(new TrainingSession(adultGroup2, coach1, DayOfWeek.TUESDAY, new TimeOfDay(19, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adultGroup2, coach1, DayOfWeek.THURSDAY, new TimeOfDay(19, 0)));

        // Добавляем несколько тренировок в одно время (разные группы)
        timetable.addNewTrainingSession(new TrainingSession(childGroup1, coach1, DayOfWeek.SATURDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup2, coach2, DayOfWeek.SATURDAY, new TimeOfDay(12, 0)));

        // Демонстрация работы программы

        System.out.println("=== РАСПИСАНИЕ ГИМНАСТИЧЕСКОГО ЗАЛА ===\n");

        // 1. Показываем расписание на понедельник
        System.out.println("1. Расписание на понедельник:");
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        for (TrainingSession session : mondaySessions) {
            System.out.printf("  - %s: %s (%s) - тренер: %s %s.%s.%n",
                    session.getTimeOfDay().getHours() + ":" +
                            String.format("%02d", session.getTimeOfDay().getMinutes()),
                    session.getGroup().getTitle(),
                    session.getGroup().getDuration() + " мин",
                    session.getCoach().getSurname(),
                    session.getCoach().getName().charAt(0),
                    session.getCoach().getMiddleName().charAt(0));
        }

        // 2. Показываем тренировки в субботу в 12:00
        System.out.println("\n2. Тренировки в субботу в 12:00:");
        List<TrainingSession> saturday12Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.SATURDAY, new TimeOfDay(12, 0));
        for (TrainingSession session : saturday12Sessions) {
            System.out.printf("  - %s (%s) - тренер: %s%n",
                    session.getGroup().getTitle(),
                    session.getGroup().getDuration() + " мин",
                    session.getCoach().getSurname());
        }

        // 3. Показываем статистику по тренерам
        System.out.println("\n3. Статистика тренеров (количество тренировок в неделю):");
        List<CoachTrainingCount> coachStats = timetable.getCountByCoaches();
        for (CoachTrainingCount stat : coachStats) {
            System.out.printf("  - %s %s %s: %d тренировок%n",
                    stat.getCoach().getSurname(),
                    stat.getCoach().getName(),
                    stat.getCoach().getMiddleName(),
                    stat.getTrainingCount());
        }

        // 4. Показываем пустой день (воскресенье)
        System.out.println("\n4. Расписание на воскресенье:");
        List<TrainingSession> sundaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        if (sundaySessions.isEmpty()) {
            System.out.println("  - Занятий нет");
        }

        // 5. Проверяем поиск по несуществующему времени
        System.out.println("\n5. Тренировки в понедельник в 15:00:");
        List<TrainingSession> monday15Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        if (monday15Sessions.isEmpty()) {
            System.out.println("  - Занятий нет");
        }

        System.out.println("\n=== ПРОГРАММА ГОТОВА К РАБОТЕ ===");
    }
}
}
