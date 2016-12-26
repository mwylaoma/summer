package tam.summer.example.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import tam.summer.common.DateTimeFormat;
import tam.summer.database.annotation.Column;
import tam.summer.restdoc.annotation.FieldComment;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tanqimin on 2015/11/24.
 */
public class EntityBase implements Serializable {
    @Column(isPrimaryKey = true)
    @FieldComment(description = "ID", required = true)
    private String id;
    @Column
    @FieldComment(description = "版本", required = true)
    private int version;
    @Column
    @FieldComment(description = "创建时间", required = true)
//    @JSONField(format = DateTimeFormat.DATE_TIME)
    private Date created;
    @Column
    @FieldComment(description = "最后修改时间", required = true)
//    @JSONField(format = DateTimeFormat.DATE_TIME)
    private Date modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void markNew() {
        markNew(UUID.randomUUID().toString());
    }

    public void markNew(String id) {
        this.id = id;
        this.version = 1;
        Date now = new Date();
        this.created = now;
        this.modified = now;
    }

    public void markModify() {
        this.version += 1;
        this.modified = new Date();
    }
}
