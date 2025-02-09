package com.campus.common.constants;

import java.time.LocalDate;
import java.time.Month;

public class CourseConstants {
    public static final String ADD_COURSE_TOPIC="courseForAdd:";
    public static final String ADD_LESSON_TOPIC="lessonForAdd:";
    public static final String ADD_COURSE_EXCHANGE="course.askForAdd";
    public static final String ADD_LESSON_EXCHANGE="lesson.askForAdd";
    public static final String ADD_COURSE_ROUTINGKEY="course.add";
    public static final String ADD_LESSON_ROUTINGKEY="lesson.add";
    public static final String COURSE_ALL="courseAll";
    public static final String COURSE_DETAIL="courseDetail:";
    public static final String LESSON_TIME="lessonTime:";
    public static final String DONE_COURSE="done_course:";
    public static final String COURSE_INFO="courseName:";

    public static LocalDate getTerm(){
        LocalDate term = LocalDate.now();
        if(term.getMonth().compareTo(Month.SEPTEMBER)<0&&term.getMonth().compareTo(Month.MARCH)>=0){
            term = LocalDate.of(term.getYear(), Month.MARCH, 1);
        }else if(term.getMonth().compareTo(Month.SEPTEMBER)>=0){

            term = LocalDate.of(term.getYear(), Month.SEPTEMBER, 1);
        }else{
            term = LocalDate.of(term.getYear()-1, Month.SEPTEMBER, 1);

        }
        return term;
    }
}
