package congybk.com.mapmvp.models.network;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 23/09/2016.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Error {

    private int stateCode;
    @SerializedName("errors")
    private String message;

}
