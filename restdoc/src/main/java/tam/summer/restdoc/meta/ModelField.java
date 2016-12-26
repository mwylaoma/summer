package tam.summer.restdoc.meta;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ModelField {
    private String name;
    private String description;
    private Class<?> type;
    private String typeName;
    private boolean required;
    private boolean showLinked;

    public ModelField() {
    }

    public ModelField(String name, String description, boolean required) {
        this();
        this.name = name;
        this.description = description;
        this.required = required;
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean getShowLinked() {
        return showLinked;
    }

    public void setShowLinked(boolean showLinked) {
        this.showLinked = showLinked;
    }
}
