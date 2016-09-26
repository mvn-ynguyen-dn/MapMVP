package congybk.com.mapmvp.models.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/26/2016.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ResultMarker {
    private String name;
    private String latitude;
    private String longitude;
}
