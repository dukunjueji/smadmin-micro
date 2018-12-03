package com.uc.training.base.sys.controller;

import com.uc.training.base.sys.dto.SysRoleDTO;
import com.uc.training.base.sys.dto.SysRoleMenuDTO;
import com.uc.training.base.sys.dto.SysUserRoleDTO;
import com.uc.training.base.sys.re.SysRoleRE;
import com.uc.training.base.sys.service.RoleService;
import com.uc.training.base.sys.vo.RoleListVO;
import com.uc.training.base.sys.vo.RoleVO;
import com.uc.training.common.base.controller.BaseController;
import com.uc.training.common.vo.PageVO;
import com.ycc.base.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 余旭东
 * @Date: 2018/11/30 13:39
 * @Description:
 */
@Controller
@RequestMapping("/admin/sys/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "getRolePage.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result getRolePage(@Validated RoleListVO roleListVO) {
        try {
            PageVO<SysRoleRE> pageVO = new PageVO<SysRoleRE>();
            pageVO.setPageIndex(roleListVO.getPageIndex());
            pageVO.setPageSize(roleListVO.getPageSize());
            SysRoleDTO roleDTO = new SysRoleDTO();
            BeanUtils.copyProperties(roleListVO, roleDTO);
            pageVO.setTotal(roleService.getCount(roleDTO));
            pageVO.setDataList(roleService.getRolePage(roleDTO));
            return Result.getSuccessResult(pageVO);
        } catch (Exception e) {
            logger.error("查询符合条件错误！", e);
            return Result.getBusinessException("获取分页失败", null);
        }
    }

    /**
     * 获取角色列表
     * @return
     */
    @RequestMapping(value = "getRoleList.do_", method = RequestMethod.GET)
    @ResponseBody
    public Result getRoleList() {
        return Result.getSuccessResult(roleService.getRoleList());
    }

    /**
     * 获取指定用户的角色列表
     * @param uid
     * @return
     */
    @RequestMapping(value = "getRoleByUid.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result getRoleByUid(Long uid) {
        if (uid == null){
            return Result.getBusinessException("用户ID为空", null);
        }
        return Result.getSuccessResult(roleService.getRoleListByUid(uid));
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @RequestMapping(value = "addRole.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result addRole(@Validated RoleVO role){
        if (roleService.queryCountByName(role.getName()) > 0) {
            return Result.getBusinessException("该角色已存在", null);
        }
        SysRoleDTO roleDTO = new SysRoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        roleDTO.setCreateEmp(getUid());
        return Result.getSuccessResult(roleService.addRole(roleDTO));
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @RequestMapping(value = "updateRole.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result updateRole(@Validated RoleVO role){
        String oldName = roleService.getById(role.getId()).getName();
        if (roleService.queryCountByName(role.getName()) >0 && !role.getName().equals(oldName)){
            return Result.getBusinessException("该角色已存在", null);
        }
        SysRoleDTO roleDTO = new SysRoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        return Result.getSuccessResult(roleService.updateRole(roleDTO));
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteRole.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteRole(Long id){
        if (id == null){
            return Result.getBusinessException("id为空", null);
        }
        return Result.getSuccessResult(roleService.deleteById(id));
    }

    /**
     * 添加角色权限
     * @param rid
     * @param ids
     * @return
     */
    @RequestMapping(value = "addAuth.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> addAuth(Long rid, String[] ids){
        // 判空
        if (ids == null) {
            return Result.getBusinessException("添加失败", null);
        }
        List<Long> list = new ArrayList<>();
        for (String s : ids) {
            list.add(Long.parseLong(s));
        }
        SysRoleMenuDTO sysRoleMenuDTO = new SysRoleMenuDTO();
        sysRoleMenuDTO.setMenuId(list);
        return Result.getSuccessResult(roleService.addRoleAuth(sysRoleMenuDTO));
    }

    /**
     * 获取角色菜单权限ID
     * @param rid
     * @return
     */
    @RequestMapping(value = "getRoleMenu.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<Long>> getRoleMenu(Long rid){
        if (rid == null) {
            return Result.getBusinessException("查询失败", null);
        }
        return Result.getSuccessResult(roleService.getRoleMenuIdByRid(rid));
    }


    /**
     * 添加用户角色
     * @param uid
     * @param ids
     * @return
     */
    @RequestMapping(value = "addUserRole.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> addUserRole(Long uid, String[] ids){
        // 判空
        if (ids == null) {
            return Result.getBusinessException("角色添加失败", null);
        }
        List<Long> list = new ArrayList<>();
        for (String s : ids) {
            list.add(Long.parseLong(s));
        }
        SysUserRoleDTO sysUserRoleDTO = new SysUserRoleDTO();
        sysUserRoleDTO.setUserId(uid);
        sysUserRoleDTO.setRoleId(list);
        sysUserRoleDTO.setCreateEmp(getUid());
        return Result.getSuccessResult(roleService.addUserRole(sysUserRoleDTO));
    }

}
