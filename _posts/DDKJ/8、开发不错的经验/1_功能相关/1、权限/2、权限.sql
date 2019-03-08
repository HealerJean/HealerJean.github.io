
# 1、系统管理员用户表，包括用户名和密码 ，创建管理员用户就会到这里 
select * from sys_admin_user s
		  where s.email='yujin.zhang@duodian.com';
-- 部门表
SELECT * from sys_department ;
-- 管理员部门对应表
select * from  sys_dept_admin_user s 
		 WHERE s.admid = '44';

# 2、系统角色表 
select * from  sys_auth_role
# 3、系统角色 和 用户id表
					-- private Long id;
					-- private Long roleId;  角色id
					-- private Long admId;   管理员 id  如果为1为则为系统管理员

select * from sys_auth_role_admin_user s 
		  where s.admid = 44;

# 4、角色菜单对应表
				-- 	   private Long id;
				--     private Long roleId;  //角色id
				--     private Long menuId;  //菜单id
select * from  sys_auth_role_menu s 
		where s.roleId in(4,17)  ;

-- 5、系统菜单 pid为上级
select * from sys_menu ;

