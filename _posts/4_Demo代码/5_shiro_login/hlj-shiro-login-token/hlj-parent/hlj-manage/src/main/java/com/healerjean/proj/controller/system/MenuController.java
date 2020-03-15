package com.healerjean.proj.controller.system;

import com.healerjean.proj.api.system.MenuService;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName MenuController
 * @Author TD
 * @Date 2019/3/18 13:35
 * @Description 菜单管理controller
 */

@Api(description = "系统管理-菜单管理")
@RestController
@RequestMapping("hlj/sys")
@Slf4j
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "菜单添加",
            notes = "菜单添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @PostMapping(value = "menu/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean addMenu(@RequestBody(required = false) MenuDTO menuDTO) {
        log.info("系统管理-菜单管理--------菜单添加--------请求参数：{}", menuDTO);
        menuService.addMenu(menuDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("菜单添加成功", menuDTO);
    }


    @ApiOperation(value = "菜单删除",
            notes = "菜单删除",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @DeleteMapping(value = "menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean deleteMenu(@RequestBody(required = false) List<Long> menuIds) {
        log.info("系统管理-菜单管理--------菜单删除--------菜单Ids：{}", menuIds);
        menuService.deleteMenu(menuIds);
        return ResponseBean.buildSuccess("菜单删除成功");
    }


    @ApiOperation(value = "菜单修改",
            notes = "菜单修改",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @PutMapping(value = "menu/{menuId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean updateMenu(@PathVariable Long menuId, @RequestBody(required = false) MenuDTO menuDTO) {
        log.info("系统管理-菜单管理--------菜单修改--------菜单Id：{}，请求参数：{}", menuId, menuDTO);
        menuDTO.setId(menuId);
        menuService.updateMenu(menuDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("菜单修改成功");
    }


    @ApiOperation(value = "菜单查询",
            notes = "菜单查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @GetMapping(value = "menu/{menuId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getMenu(@PathVariable Integer menuId) {
        log.info("系统管理-菜单管理--------菜单查询--------菜单Id：{}", menuId);
        return ResponseBean.buildSuccess("菜单查询成功", menuService.getMenu(menuId));
    }


    @ApiOperation(value = "菜单列表查询",
            notes = "菜单列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @GetMapping(value = "menus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getUsers(MenuDTO dto) {
        log.info("系统管理-菜单管理--------菜单列表查询--------请求参数：{}", dto);
        if (dto.getToTree() != null && dto.getToTree()) {
            return ResponseBean.buildSuccess("菜单列表查询成功", menuService.getMenusNoPageToTree(dto));
        }
        return ResponseBean.buildSuccess("菜单列表查询成功", menuService.getMenus(dto));
    }

}
