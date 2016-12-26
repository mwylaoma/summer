package tam.summer.restdoc.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ApiMethod {
    private String name;
    private String methodName;
    private String description;
    private String url;
    private String httpMethod;
    private List<ApiParam> params = null;
    private ModelClass apiReturn = null;
    private boolean apiReturnShowLinked;
    private boolean requireAuth;

    public ApiMethod() {
        params = new ArrayList<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean getApiReturnShowLinked() {
        return apiReturnShowLinked;
    }

    public void setApiReturnShowLinked(boolean apiReturnShowLinked) {
        this.apiReturnShowLinked = apiReturnShowLinked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<ApiParam> getParams() {
        return params;
    }

    public void setParams(List<ApiParam> params) {
        this.params = params;
    }

    public ModelClass getApiReturn() {
        return apiReturn;
    }

    public void setApiReturn(ModelClass apiReturn) {
        this.apiReturn = apiReturn;
    }

    public boolean getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(boolean requireAuth) {
        this.requireAuth = requireAuth;
    }
}
