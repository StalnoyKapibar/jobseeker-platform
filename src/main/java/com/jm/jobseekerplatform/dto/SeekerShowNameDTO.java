package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.SeekerShowName;
import org.springframework.data.domain.PageImpl;
import java.util.List;

public class SeekerShowNameDTO extends PageImpl<SeekerShowName> {

    private List<SeekerShowName> seekerShowName;

    public SeekerShowNameDTO(List<SeekerShowName> seekerShowName) {
        super(seekerShowName);
    }

    public List<SeekerShowName> getSeekerShowName() {
        return seekerShowName;
    }

    public void setSeekerShowName(List<SeekerShowName> seekerShowName) {
        this.seekerShowName = seekerShowName;
    }

}
