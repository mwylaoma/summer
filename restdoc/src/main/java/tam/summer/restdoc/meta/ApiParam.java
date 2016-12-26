package tam.summer.restdoc.meta;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ApiParam {
    private String name;
    private String description;
    private boolean isPathVariable;
    private Class<?> type;
    private String typeName;
    private boolean showLinked;
    private ModelClass modelClass;

    public ModelClass getModelClass() {
        return modelClass;
    }

    public void setModelClass(ModelClass modelClass) {
        this.modelClass = modelClass;
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

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean getIsPathVariable() {
        return isPathVariable;
    }

    public void setIsPathVariable(boolean pathVariable) {
        isPathVariable = pathVariable;
    }

    public boolean getShowLinked() {
        return showLinked;
    }

    public void setShowLinked(boolean showLinked) {
        this.showLinked = showLinked;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
