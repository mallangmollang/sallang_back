package mallang.sallang.restArea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestAreaDto {
    private String placeId;
    private String name;
    private String address;
    private double lat;
    @JsonProperty("lon")
    private double lng;
    private int distance;
}
