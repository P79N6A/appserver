package com.idcq.appserver.service.admin.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.admin.IAdminDao;
import com.idcq.appserver.dto.admin.AdminDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.admin.IAdminService;

@Service
public class AdminServiceImpl implements IAdminService {
    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
    @Autowired
    private IAdminDao adminDao;
    
    @Override
    public void checkAdminValid(Integer id) throws Exception {
    	AdminDto admin = adminDao.getAdminById(id);
     	if (admin == null)
    		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"admin不存在");
    }
}
