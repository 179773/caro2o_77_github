package cn.wolfcode.business.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DeployVo {
    private MultipartFile file;
    private String bpmnLabel;
    private Integer bpmnType;
    private String info;
}
