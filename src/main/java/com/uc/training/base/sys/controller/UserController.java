package com.uc.training.base.sys.controller;

import com.uc.training.base.sys.dto.SysUserDTO;
import com.uc.training.base.sys.re.SysMenuRE;
import com.uc.training.base.sys.re.SysUserRE;
import com.uc.training.base.sys.re.UserInfoRE;
import com.uc.training.base.sys.re.UserLoginRE;
import com.uc.training.base.sys.service.MenuService;
import com.uc.training.base.sys.service.RoleService;
import com.uc.training.base.sys.service.UserService;
import com.uc.training.base.sys.vo.UserEditPasswordVO;
import com.uc.training.base.sys.vo.UserListVO;
import com.uc.training.base.sys.vo.UserLoginVO;
import com.uc.training.base.sys.vo.UserVO;
import com.uc.training.common.annotation.AccessLogin;
import com.uc.training.common.base.controller.BaseController;
import com.uc.training.common.constant.Constant;
import com.uc.training.common.utils.EncryptUtil;
import com.uc.training.common.utils.MenuUtil;
import com.uc.training.common.utils.TokenUtil;
import com.uc.training.common.vo.PageVO;
import com.ycc.base.common.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: 余旭东
 * @Date: 2018/11/30 13:39
 * @Description:
 */
@Controller
@RequestMapping(value = "admin/sys/user/")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    /**
     * 用户登录
     *
     * @param userLoginVO 登录请求参数
     * @return com.ycc.base.common.Result<com.uc.training.smbase.sys.re.UserLoginRE>
     * @version 1.0 2018/10/18 15:22 by 吴佰川（baichuan.wu@ucarinc.com）创建
     */
    @ResponseBody
    @AccessLogin(required = false)
    @RequestMapping("login.do_")
    public Result login(@Validated UserLoginVO userLoginVO) {
        try {
            String password = EncryptUtil.md5(userLoginVO.getPassword());
            userLoginVO.setPassword(password);
            SysUserDTO userDTO = new SysUserDTO();
            BeanUtils.copyProperties(userLoginVO, userDTO);
            // 判断账号密码是否匹配
            SysUserRE user = userService.getUserLogin(userDTO);
            if (user == null) {
                logger.info("用户名或密码不正确");
                return Result.getBusinessException("用户名或密码不正确", null);
            } else {
                String token = TokenUtil.sign(user.getId());
                UserLoginRE userLoginRE = new UserLoginRE();
                userLoginRE.setToken(token);
                return Result.getSuccessResult(userLoginRE);
            }
        } catch (Exception e) {
            logger.error("用户登录异常！", e);
            return Result.getServiceError("用户登录异常", null);
        }
    }

    /**
     * 获取用户信息
     *
     * @param
     * @return com.ycc.base.common.Result<com.uc.training.smbase.sys.re.UserInfoRE>
     * @version 1.0 2018/10/18 20:18 by 吴佰川（baichuan.wu@ucarinc.com）创建
     */
    @ResponseBody
    @RequestMapping("getUserInfo.do_")
    public Result getUserInfo() {
        try {
            Long userId = getUid();
            // 获取用户信息
            SysUserRE user = userService.getById(userId);
            if (user == null) {
                logger.info("用户不存在");
                return Result.getBusinessException("用户不存在", null);
            } else {
                // 用户角色
                List<String> roles = roleService.getUserRoles(userId);
                // 用户权限
                List<String> perms = menuService.getUserPerms(userId);

                UserInfoRE userLoginRE = new UserInfoRE();
                userLoginRE.setUsername(user.getUserName());
                userLoginRE.setAdmin(user.getIsAdmin() == 1 ? true : false);
                userLoginRE.setRoles(roles);
                userLoginRE.setPerms(perms);
                return Result.getSuccessResult(userLoginRE);
            }
        } catch (Exception e) {
            logger.error("获取用户信息异常！", e);
            return Result.getServiceError("获取用户信息异常", null);
        }
    }

    /**
     * 用户修改密码
     *
     * @param userEditPasswordVO 用户修改密码参数vo
     * @return com.ycc.base.common.Result<java.lang.Integer>
     * @version 1.0 2018/10/24 20:38 by 吴佰川（baichuan.wu@ucarinc.com）创建
     */
    @ResponseBody
    @RequestMapping("editPassword.do_")
    public Result editPassword(@Validated UserEditPasswordVO userEditPasswordVO) {
        try {
            Long userId = getUid();
            SysUserRE user = userService.getById(userId);
            if (user == null) {
                return Result.getBusinessException("用户不存在", null);
            } else {
                String oldPwd = EncryptUtil.md5(userEditPasswordVO.getOldPwd());
                if (user.getPassword().equals(oldPwd)) {
                    SysUserDTO sysUser = new SysUserDTO();
                    sysUser.setId(userId);
                    sysUser.setPassword(EncryptUtil.md5(userEditPasswordVO.getNewPwd()));
                    sysUser.setModifyEmp(userId);
                    return Result.getSuccessResult(userService.updatePassword(sysUser));
                } else {
                    return Result.getBusinessException("原始密码不正确", null);
                }
            }
        } catch (Exception e) {
            logger.error("用户修改密码异常！", e);
            return Result.getServiceError("用户修改密码异常", null);
        }
    }

    /**
     * 获取用户列表
     *
     * @param userListVO
     * @return
     */
    @RequestMapping(value = "/getUserPage.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserList(@Validated UserListVO userListVO) {
        userListVO.setCreateEmp(getUid());
        try {
            PageVO<SysUserRE> pageVO = new PageVO<SysUserRE>();
            pageVO.setPageIndex(userListVO.getPageIndex());
            pageVO.setPageSize(userListVO.getPageSize());
            SysUserDTO sysUserDTO = new SysUserDTO();
            BeanUtils.copyProperties(userListVO, sysUserDTO);
            pageVO.setTotal(userService.queryUserCount(sysUserDTO));
            pageVO.setDataList(userService.getUserList(sysUserDTO));
            return Result.getSuccessResult(pageVO);
        } catch (Exception e) {
            logger.error("查询符合条件错误！", e);
            return Result.getBusinessException("获取分页失败", null);
        }
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @AccessLogin
    @RequestMapping(value = "/addUser.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> addUser(@Validated UserVO user) {
        // 判空
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return Result.getBusinessException("输入用户不能为空", null);
        }
        String pwd = EncryptUtil.md5(Constant.DEFAULT_PASSWORD);
        SysUserDTO name = new SysUserDTO();
        name.setUserName(user.getUserName());
        Long count = userService.queryUserCount(name);
        if (count > 0) {
            return Result.getBusinessException("该用户已存在", null);
        }
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setCreateEmp(getUid());
        userDTO.setPassword(pwd);
        Long id = userService.addUser(userDTO);
        return Result.getSuccessResult(id);
    }

    /**
     * 根据ID删除用户
     *
     * @param id
     * @return
     */
    @AccessLogin
    @RequestMapping(value = "/deleteUser.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> deleteUser(Long id) {
        return Result.getSuccessResult(userService.deleteById(id));
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @AccessLogin
    @RequestMapping(value = "/editUser.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> editUser(@Validated UserVO user) {
        // 判空
        if (user == null || StringUtils.isEmpty(user.getUserName()) || user.getId() == null) {
            return Result.getBusinessException("用户更新失败", null);
        }
        String oldName = userService.getById(user.getId()).getUserName();
        SysUserDTO name = new SysUserDTO();
        name.setUserName(user.getUserName());
        if (userService.queryUserCount(name) > 0 && !user.getUserName().equals(oldName)) {
            return Result.getBusinessException("该用户已存在", null);
        }
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setModifyEmp(getUid());
        return Result.getSuccessResult(userService.updateUser(userDTO));
    }

    /**
     * 根据用户ID获取用户的菜单权限
     *
     * @param uid
     * @return
     */
    @AccessLogin
    @RequestMapping(value = "/getUserMenuAuth.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserMenuAuth(Long uid) {
        if (uid == null) {
            return Result.getBusinessException("权限获取失败", null);
        }
        List<SysMenuRE> allMenu = userService.getMenuListByUserId(uid);
        List<SysMenuRE> rootMenu = MenuUtil.findTree(allMenu);
        return Result.getSuccessResult(rootMenu);
    }
}