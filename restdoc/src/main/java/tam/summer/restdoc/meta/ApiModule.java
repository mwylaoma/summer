package tam.summer.restdoc.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ApiModule {
    private String name;
    private int sortIndex;
    private List<ApiClass> apiClasses = null;

    public ApiModule() {
        apiClasses = new ArrayList<>();
    }

    public ApiModule(String name, int sortIndex) {
        this();
        this.name = name;
        this.sortIndex = sortIndex;
    }

    public List<ApiClass> getApiClasses() {
        return apiClasses;
    }

    public void setApiClasses(List<ApiClass> apiClasses) {
        this.apiClasses = apiClasses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
