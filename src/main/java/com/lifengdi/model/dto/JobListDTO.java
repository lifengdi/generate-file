package com.lifengdi.model.dto;

import com.lifengdi.job.entity.Job;
import lombok.Data;

/**
 * @author 李锋镝
 * @date Create at 17:30 2019/4/18
 */
@Data
public class JobListDTO extends Job {

    private String fileCdn;
}
