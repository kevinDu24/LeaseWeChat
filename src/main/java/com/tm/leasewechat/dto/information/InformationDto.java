package com.tm.leasewechat.dto.information;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/10/17.
 */
@Data
public class InformationDto implements Serializable{

    private static final long serialVersionUID = -7002275336572438691L;
    private Long id;
    private String title;
    private String createDate;
    private String author;
    private List<String> imgUrls;
    private String tag;

    public InformationDto(Map<String, Object> map){
        this.id = Long.parseLong(map.get("id").toString());
        this.title = map.get("title").toString();
        this.createDate = map.get("createDate").toString();
        this.author = map.get("author").toString();
        this.imgUrls = (List<String>) map.get("imgUrls");
        this.tag = map.get("tag") == null ? "" : map.get("tag").toString();
    }
}
