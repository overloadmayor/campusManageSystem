package com.me.mall.service.customer.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.mall.dao.customer.CustomerLinkmanDao;
import com.me.mall.dto.customer.CustomerLinkManSelectDto;
import com.me.mall.dto.customer.CustomerLinkmanDto;
import com.me.mall.mapper.CustomerLinkmanMapper;
import com.me.mall.model.CustomerLinkman;
import com.me.mall.service.customer.CustomerLinkmanService;
import com.me.mall.vo.customer.CustomerLinkManVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerLinkmanServiceImpl implements CustomerLinkmanService {
    @Autowired
    private CustomerLinkmanDao customerLinkmanDao;
    @Autowired
    private CustomerLinkmanMapper customerLinkmanMapper;

    /**
     * 分页过滤获取客户联系人信息
     * @param dto
     * @return
     */
    @Override
    public List<CustomerLinkManVo> getCustomerLinkManInfoByFilter(CustomerLinkManSelectDto dto) {
        //参数校验
        if (!dto.checkParam()) {
            throw new BadCredentialsException("参数异常");
        }
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        Page page = (Page) customerLinkmanDao.getCustomerLinkmanInfoByFilter(dto.getCompanyId(),
                dto.getCustomerId(), dto.getLinkmanName(),0);
        List<CustomerLinkManVo> result = page.getResult();
        return result;
    }

    /**
     * 更新客户联系人信息
     * @param dto
     * @return
     */
    @Override
    public Integer updateCustomeLinkmanInfo(CustomerLinkmanDto dto) {
        return customerLinkmanDao.updateCustomerLinkmanInfo(dto);
    }

    /**
     * 删除客户联系人信息
     * @param id
     * @return
     */
    @Override
    public Integer deleteCustomerLinkmanInfo(Long id) {
        return customerLinkmanDao.deleteCustomerLinkmanInfo(id,1);
    }

    /**
     * 新增客户联系人信息
     * @param dto
     * @return
     */
    @Override
    public Integer inertCustomeLinkmanInfo(CustomerLinkmanDto dto) {
        dto.setDeleteStatus(0);
        CustomerLinkman customerLinkman = new CustomerLinkman();
        BeanUtils.copyProperties(dto, customerLinkman);
        return customerLinkmanMapper.insert(customerLinkman);
    }


}
