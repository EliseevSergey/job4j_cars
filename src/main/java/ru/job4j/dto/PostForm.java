package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostForm {
    private String description;
    private String vin;
    private Integer brandId;
    private Integer modelId;
    private Integer colorId;
    private Integer fuelId;
    private Double volume;
    private Integer horsePower;
    private MultipartFile photo;
}
