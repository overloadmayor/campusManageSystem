package com.campus.model.course.vo;

import com.campus.model.course.pojos.LessonTimePart;
import lombok.Data;


import java.util.Set;

@Data
public class LessonTimeVo {
    private Set<LessonTimePart> ban;
    private Set<LessonTimePart> notRequired;
}
