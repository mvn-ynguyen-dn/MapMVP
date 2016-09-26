package congybk.com.mapmvp.models.network.core;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 23/09/2016.
 */
public final class ApiConfig {
    final Context mContext;
    final String mBaseUrl;

    private ApiConfig(Builder builder) {
        this.mContext = builder.mContext;
        this.mBaseUrl = builder.mBaseUrl;
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    /**
     * Builder
     */
    public static class Builder {
        private final Context mContext;
        private String mBaseUrl;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setBaseUrl(@NonNull String baseUrl) {
            this.mBaseUrl = baseUrl;
            return this;
        }

        public ApiConfig build() {
            return new ApiConfig(this);
        }
    }

}
