package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

package ru.yandex.practicum.gym;

package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0));

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0));
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));
        
        // Проверить время занятий в четверг
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> monday13Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, monday13Sessions.size());
        Assertions.assertEquals(singleTrainingSession, monday13Sessions.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> monday14Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertTrue(monday14Sessions.isEmpty());
    }

    @Test
    void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Петр", "Сергеевич");
        Coach coach2 = new Coach("Петрова", "Мария", "Александровна");
        
        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 45);
        
        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        
        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        
        // Проверить, что в одно время могут быть несколько тренировок
        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        Assertions.assertEquals(2, sessions.size());
        Assertions.assertTrue(sessions.contains(session1));
        Assertions.assertTrue(sessions.contains(session2));
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();
        
        // Проверить, что для пустого расписания возвращаются пустые списки
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0)).isEmpty());
        
        // Проверить статистику тренеров для пустого расписания
        List<CoachTrainingCount> coachStats = timetable.getCountByCoaches();
        Assertions.assertTrue(coachStats.isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();
        
        Coach coach1 = new Coach("Иванов", "Петр", "Сергеевич");
        Coach coach2 = new Coach("Петрова", "Мария", "Александровна");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Викторович");
        
        Group group = new Group("Фитнес", Age.ADULT, 60);
        
        // coach1: 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));
        
        // coach2: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(18, 0)));
        
        // coach3: 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY, new TimeOfDay(11, 0)));
        
        List<CoachTrainingCount> result = timetable.getCountByCoaches();
        
        Assertions.assertEquals(3, result.size());
        
        // Проверить сортировку по убыванию количества тренировок
        Assertions.assertEquals(3, result.get(0).getTrainingCount());
        Assertions.assertEquals(coach1, result.get(0).getCoach());
        
        Assertions.assertEquals(2, result.get(1).getTrainingCount());
        Assertions.assertEquals(coach2, result.get(1).getCoach());
        
        Assertions.assertEquals(1, result.get(2).getTrainingCount());
        Assertions.assertEquals(coach3, result.get(2).getCoach());
    }

    @Test
    void testAddTrainingToAllDays() {
        Timetable timetable = new Timetable();
        
        Coach coach = new Coach("Смирнов", "Дмитрий", "Олегович");
        Group group = new Group("Утренняя зарядка", Age.ADULT, 30);
        
        // Добавляем тренировки на все дни недели
        for (DayOfWeek day : DayOfWeek.values()) {
            TrainingSession session = new TrainingSession(group, coach, day, new TimeOfDay(7, 0));
            timetable.addNewTrainingSession(session);
        }
        
        // Проверить, что на каждый день есть по одной тренировке
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);
            Assertions.assertEquals(1, sessions.size());
            Assertions.assertEquals(new TimeOfDay(7, 0), sessions.get(0).getTimeOfDay());
        }
        
        // Проверить статистику тренера
        List<CoachTrainingCount> coachStats = timetable.getCountByCoaches();
        Assertions.assertEquals(1, coachStats.size());
        Assertions.assertEquals(7, coachStats.get(0).getTrainingCount());
        Assertions.assertEquals(coach, coachStats.get(0).getCoach());
    }
}
