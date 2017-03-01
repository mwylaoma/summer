package tam.summer.example.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tam.summer.database.annotation.Column;
import tam.summer.database.annotation.Table;
import tam.summer.restdoc.annotation.ClassComment;
import tam.summer.restdoc.annotation.FieldComment;

import java.util.Date;

/**
 * Created by tanqimin on 2015/11/3.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(cached = true)
@ClassComment(value = "部门", description = "部门档案")
public class Department extends EntityBase {
    @Column
    @FieldComment(description = "编码", required = true)
    private String code;
    @Column
    @FieldComment(description = "名称", required = true)
    private String name;
    @Column
    @FieldComment(description = "是否停用？")
    private boolean disable;
    @Column
    @FieldComment(description = "描述")
    private String description;
    @Column(value = "hire_date")
    @FieldComment(description = "雇佣日期")
    private Date hireDate;
}
