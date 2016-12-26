package tam.summer.restdoc.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/16.
 */
public class ModelClass {
    private String name;
    private String description;
    private Class<?> type;
    private String typeName;
    private List<ModelField> modelFields = null;

    public ModelClass() {
        modelFields = new ArrayList<>();
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

    public List<ModelField> getModelFields() {
        return modelFields;
    }

    public void setModelFields(List<ModelField> modelFields) {
        this.modelFields = modelFields;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
