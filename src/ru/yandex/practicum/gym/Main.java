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
