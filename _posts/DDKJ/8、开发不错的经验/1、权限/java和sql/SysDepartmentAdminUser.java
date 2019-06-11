@Entity
@Table(name = "sys_dept_admin_user")
public class SysDepartmentAdminUser implements Serializable {

    private static final long serialVersionUID = -3792256317744904398L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long departmentId;
    private Long admId;

    public SysDepartmentAdminUser() {
    }

    public SysDepartmentAdminUser(Long departmentId, Long admId) {
        this.departmentId = departmentId;
        this.admId = admId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getAdmId() {
        return admId;
    }

    public void setAdmId(Long admId) {
        this.admId = admId;
    }
}
