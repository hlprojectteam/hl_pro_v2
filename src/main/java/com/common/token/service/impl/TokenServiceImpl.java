package com.common.token.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.token.dao.ITokenDao;
import com.common.token.module.Token;
import com.common.token.service.ITokenService;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018-3-22
 *
 */
@Repository("tokenServiceImpl")
public class TokenServiceImpl extends BaseServiceImpl implements ITokenService{

	@Autowired
	public ITokenDao tokenDaoImpl;
	

	@Override
	public void saveOrUpdate(Token token) {
		if(StringUtils.isNotBlank(token.getId())){
			this.update(token);
		}else{
			this.save(token);
		}
	}

	@Override
	public void deleteToken(String id) {
		String[] idz = id.split(",");
		for (int i = 0; i < idz.length; i++) {
			this.delete(Token.class, idz[i]);
		}
	}

	@Override
	public Token queryEntityById(String id) {
		Token token = this.tokenDaoImpl.getEntityById(Token.class, id);
		return token;
	}
	
	@Override
	public boolean isHaveToken(String token) {
		// TODO Auto-generated method stub
		boolean flag=false;
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(token)){
			criterionsList.add(Restrictions.eq("signature", token));
		}
		List<Token> tList= this.tokenDaoImpl.queryEntityList(criterionsList, null, Token.class);
		if(tList!=null){
			if(tList.size()>0){
				flag=true;
			}
		}
		return flag;
	}
	
	@Override
	public Token getToken(String token) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(token)){
			criterionsList.add(Restrictions.eq("signature", token));
		}
		List<Token> tList= this.tokenDaoImpl.queryEntityList(criterionsList, null, Token.class);
		if(tList!=null){
			if(tList.size()>0){
				return tList.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void deleteTokenByUserId(String userId) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(userId)){
			criterionsList.add(Restrictions.eq("userId", userId));
		}
		List<Token> tList= this.tokenDaoImpl.queryEntityList(criterionsList, null, Token.class);
		if(tList!=null){
			if(tList.size()>0){
				for (int i = 0; i < tList.size(); i++) {
					this.tokenDaoImpl.delete(tList.get(i));
				}
			}
		}
	}
	
	public ITokenDao getTokenDaoImpl() {
		return tokenDaoImpl;
	}

	public void setTokenDaoImpl(ITokenDao tokenDaoImpl) {
		this.tokenDaoImpl = tokenDaoImpl;
	}

	


}
