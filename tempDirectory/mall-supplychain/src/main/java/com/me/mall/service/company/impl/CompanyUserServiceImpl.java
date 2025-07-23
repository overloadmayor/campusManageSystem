package com.me.mall.service.company.impl;

import com.github.pagehelper.PageHelper;
import com.me.mall.common.api.ErrorMessageCode;
import com.me.mall.common.util.StringUtil;
import com.me.mall.dto.company.CompanyDto;
import com.me.mall.mapper.CompanyMapper;
import com.me.mall.mapper.CompanyUserRoleRelationMapper;
import com.me.mall.model.*;
import com.me.mall.security.bo.CompanyUserDetails;
import com.me.mall.dto.company.CompanyUserDto;
import com.me.mall.mapper.CompanyUserMapper;
import com.me.mall.security.util.JwtTokenUtil;
import com.me.mall.security.vo.CompanyVo;
import com.me.mall.service.company.CompanyUserService;
import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.vo.company.CompanyUserAllVo;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 企业用户实现层
 */
@Service
public class CompanyUserServiceImpl implements CompanyUserService {

    @Resource
    private CompanyUserMapper companyUserMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private CompanyUserRoleRelationMapper companyUserRoleRelationMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录实现
     */
    @Override
    public String login(CompanyUserDto dto) {
        //校验参数
        login_checkParam(dto);

        //校验登陆并返回用户
        CompanyUserVo userVo = login_getCompanyUser(dto);

        //返回token
        CompanyUserDetails userDetails = new CompanyUserDetails(userVo);
        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * 用户登录实现-校验参数
     */
    private void login_checkParam(CompanyUserDto dto) {
        if (dto == null || dto.getLoginType() == null){
            throw new BadCredentialsException(ErrorMessageCode.PARAM_MISS.getMessage());
        }
        if (ObjectUtils.isEmpty(dto.getUserPhone()) || ObjectUtils.isEmpty(dto.getAuthCode())){
            throw new BadCredentialsException("请您输入完善手机号/验证码");
        }
        if (StringUtil.isLengthExceeded(dto.getUserPhone(), 11)){
            throw new BadCredentialsException("请您输入不超过11位数的手机号码");
        }
        if (StringUtil.isLengthExceeded(dto.getAuthCode(), 6)){
            throw new BadCredentialsException("请您输入不超过6位数的验证码");
        }
    }

    /**
     * 用户登录实现-校验登陆并返回用户
     */
    private CompanyUserVo login_getCompanyUser(CompanyUserDto dto) {
        CompanyUserVo userVo = null;
        //1、手机号 + 验证码
        if (dto.getLoginType() == 1){
            //校验验证码 todo

            //查询账号
            CompanyUserExample companyUserExample = new CompanyUserExample();
            companyUserExample.createCriteria().andUserPhoneEqualTo(dto.getUserPhone());
            List<CompanyUser> users = companyUserMapper.selectByExample(companyUserExample);
            if (users.isEmpty()){
                throw new BadCredentialsException("账号不存在，请您联系管理员");
            }
            CompanyUser user = users.get(0);

            //校验账号信息 todo

            userVo = new CompanyUserVo();
            BeanUtils.copyProperties(user, userVo);
        }

        //todo 2、钉钉扫码

        //todo 3、企微扫码

        return userVo;
    }

    /**
     * 获取当前登陆用户信息
     */
    @Override
    public CompanyUserVo getCurrentUser() {
        CompanyUserVo vo = new CompanyUserVo();
        vo.setUserId(0L);

        //解析用户
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CompanyUserDetails) {
                CompanyUserDetails userDetails = ((CompanyUserDetails) principal);

                //用户信息
                return this.getCurrentUser_getUserVo(userDetails);
            }
        }

        return null;
    }

    /**
     * "获取当前登陆 用户信息+企业信息+...
     */
    @Override
    public CompanyUserAllVo getCurrentUserAll() {
        CompanyUserAllVo vo = new CompanyUserAllVo();

        //解析用户
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CompanyUserDetails) {
                CompanyUserDetails userDetails = ((CompanyUserDetails) principal);

                //用户信息
                vo.setUserVo(this.getCurrentUser_getUserVo(userDetails));

                //企业信息
                vo.setCompanyVo(this.getCurrentUser_getCompanyVo(vo.getUserVo()));

                return vo;
            }
        }

        return null;
    }

    /**
     * @param userVo 用户信息
     * @return 返回当前登陆用户企业信息
     */
    private CompanyVo getCurrentUser_getCompanyVo(CompanyUserVo userVo) {
        CompanyVo companyVo = new CompanyVo();
        if (userVo == null || userVo.getCompanyId() == null){
            return companyVo;
        }

        Company company = companyMapper.selectByPrimaryKey(userVo.getCompanyId());
        if (company == null){
            return companyVo;
        }

        BeanUtils.copyProperties(company, companyVo);

        return companyVo;
    }

    /**
     *
     * @param userDetails token解析数据
     * @return 返回当前登陆用户信息
     */
    private CompanyUserVo getCurrentUser_getUserVo(CompanyUserDetails userDetails) {
        CompanyUserVo userVo = new CompanyUserVo();
        //用户主表
        CompanyUser companyUser = companyUserMapper.selectByPrimaryKey(userDetails.getId());
        if (companyUser == null){
            return userVo;
        }
        BeanUtils.copyProperties(companyUser, userVo);

        //角色
        CompanyUserRoleRelationExample companyUserRoleRelationExample = new CompanyUserRoleRelationExample();
        companyUserRoleRelationExample.createCriteria().andUserIdEqualTo(companyUser.getUserId());
        List<CompanyUserRoleRelation> roleRelationList = companyUserRoleRelationMapper.selectByExample(companyUserRoleRelationExample);
        if (!roleRelationList.isEmpty()){
            userVo.setRoleIdList(roleRelationList.stream().map(CompanyUserRoleRelation::getRoleId).collect(Collectors.toList()));
        }

        //组织架构

        return userVo;
    }

    /**
     * 企业注册
     */
    @Override
    public Integer insert(CompanyDto dto) {
        //校验参数
        insert_checkParam(dto);

        //新增入库
        return this.insert_deal(dto);
    }

    /**
     * 企业注册-新增入库
     */
    @Transactional(rollbackFor = Exception.class)
    public int insert_deal(CompanyDto dto) {
        int index = 0;

        //新增企业信息
        Company company = new Company();
        company.setCompanyName(dto.getFullName());
        company.setFullName(dto.getFullName());
        company.setLinkmanName(dto.getLinkmanName());
        company.setLinkmanPhone(dto.getLinkmanPhone());
        //todo 按规则生成企业编码
        company.setCompanyCode(this.insert_deal_getCode());
        index += companyMapper.insertSelective(company);

        //新增账号信息
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyId(company.getCompanyId());
        companyUser.setUsername(dto.getLinkmanName());
        companyUser.setUserPhone(dto.getLinkmanPhone());
        companyUser.setIsSuper(1);
        index += companyUserMapper.insertSelective(companyUser);

        //todo 新增默认部门

        return index;
    }

    /**
     * @return 获取企业编码
     */
    private synchronized String insert_deal_getCode() {
        String numEnd = "00000";
        String codeHead = "SDY";
        LocalDate now = LocalDate.now();

        PageHelper.startPage(1,1);
        CompanyExample companyExample = new CompanyExample();
        companyExample.createCriteria().andCompanyCodeEqualTo(codeHead + "-"
                + now.getYear() + "-" + String.format("%02d", now.getMonthValue()) + "-" + String.format("%02d", now.getDayOfMonth()) + "-"
        );
        companyExample.setOrderByClause("order by company_code desc");
        List<Company> companyList = companyMapper.selectByExample(companyExample);
        if (!companyList.isEmpty() && !ObjectUtils.isEmpty(companyList.get(0).getCompanyCode())) {
            String endCode = companyList.get(0).getCompanyCode();
            numEnd = endCode.substring(endCode.length() - 4);
        }

        return StringUtil.generateCode(codeHead, numEnd);
    }

    /**
     * 企业注册-校验参数
     */
    private void insert_checkParam(CompanyDto dto) {
        if (dto == null){
            throw new BadCredentialsException(ErrorMessageCode.PARAM_MISS.getMessage());
        }
        if (ObjectUtils.isEmpty(dto.getLinkmanPhone())){
            throw new BadCredentialsException("请您输入完善手机号码");
        }
        if (StringUtil.isLengthExceeded(dto.getLinkmanPhone(), 11)){
            throw new BadCredentialsException("请您输入不超过11位数的手机号码");
        }
        if (ObjectUtils.isEmpty(dto.getLinkmanName())){
            throw new BadCredentialsException("请您输入完善联系人名称");
        }
        if (StringUtil.isLengthExceeded(dto.getLinkmanName(), 50)){
            throw new BadCredentialsException("请您输入不超过50位数的联系人名称");
        }
        if (ObjectUtils.isEmpty(dto.getFullName())){
            throw new BadCredentialsException("请您输入完善企业全称");
        }
        if (StringUtil.isLengthExceeded(dto.getFullName(), 100)){
            throw new BadCredentialsException("请您输入不超过100位数的企业全称");
        }

        //todo 校验当前企业是否已注册

        //todo 校验当前企业名称的合格性

    }
}
