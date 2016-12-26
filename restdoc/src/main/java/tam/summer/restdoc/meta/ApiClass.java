package tam.summer.restdoc.meta;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ApiClass extends ModelClass {
    @JSONField(serialize = false)
    private ApiModule apiModule;
    private List<ApiMethod> apiMethods = null;

    public ApiClass() {
        apiMethods = new ArrayList<>();
    }

    public ApiModule getApiModule() {
        return apiModule;
    }

    public void setApiModule(ApiModule apiModule) {
        this.apiModule = apiModule;
    }

    public List<ApiMethod> getApiMethods() {
        return apiMethods;
    }

    public void setApiMethods(List<ApiMethod> apiMethods) {
        this.apiMethods = apiMethods;
    }
}
